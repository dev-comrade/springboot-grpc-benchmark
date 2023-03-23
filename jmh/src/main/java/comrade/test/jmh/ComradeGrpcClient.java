package comrade.test.jmh;

import comrade.test.proto.ComradeGrpc;
import comrade.test.proto.ComradeRequest;
import comrade.test.proto.ComradeResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ComradeGrpcClient {
    private final ManagedChannel channel;
    private final ComradeGrpc.ComradeStub stub;

    private static List<String> retryableStatusCodes = Arrays.asList(
            "UNAVAILABLE",
            "DATA_LOSS",
            "INTERNAL",
            "DEADLINE_EXCEEDED"
    );

    public ComradeGrpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port));
    }

    private ComradeGrpcClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder
                .enableRetry()
                .usePlaintext()
                .defaultServiceConfig(getServiceConfig())
                .build();
        stub = ComradeGrpc.newStub(channel);
    }

    /**
     * @see <a href="https://github.com/grpc/proposal/blob/master/A6-client-retries.md#integration-with-service-config">Дока на описание конфига</a>
     * @see <a href="https://github.com/grpc/proposal/blob/master/A6-client-retries.md#retry-policy-capabilities">Описание retryPolicy</a>
     */
    private Map<String, Object> getServiceConfig() {
        Map<String, Object> retryPolicy = new HashMap<>();
        Map<String, Object> serviceConfig = new HashMap<>();
        Map<String, Object> name = new HashMap<>();
        Map<String, Object> methodConfig = new HashMap<>();

        retryPolicy.put("maxAttempts", 4D);
        retryPolicy.put("initialBackoff", "0.1s");
        retryPolicy.put("maxBackoff", "1s");
        retryPolicy.put("backoffMultiplier", 2D);
        retryPolicy.put("retryableStatusCodes", retryableStatusCodes);
        methodConfig.put("retryPolicy", retryPolicy);

        name.put("service", "comrade.test.proto.ComradeGrpc");
        methodConfig.put("name", Collections.<Object>singletonList(name));

        serviceConfig.put("loadBalancingPolicy", "round_robin");
        serviceConfig.put("methodConfig", Collections.<Object>singletonList(methodConfig));

        return serviceConfig;
    }

    public String testComrade(String name) throws ExecutionException, InterruptedException {
        Completer<ComradeResponse> completer = new Completer<>();
        testComradeAsync(name, completer);
        return completer.get().getComradeInfo();
    }

    private void testComradeAsync(String name, StreamObserver<ComradeResponse> completer) {
        var request = ComradeRequest.newBuilder().setName(name).build();
        stub.isComrade(request, completer);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * Класс для превращения асинхронного вызова grpc в синхронный.
     */
    private static class Completer<T> implements StreamObserver<T> {

        private final CompletableFuture<T> future = new CompletableFuture<>();

        public T get() throws ExecutionException, InterruptedException {
            return future.get();
        }

        @Override
        public void onNext(T t) {
            future.complete(t);
        }

        @Override
        public void onError(Throwable throwable) {
            future.completeExceptionally(throwable);
        }

        @Override
        public void onCompleted() {
        }
    }
}
