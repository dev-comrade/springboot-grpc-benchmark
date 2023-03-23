package comrade.test.spring;

import comrade.test.proto.ComradeRequest;
import comrade.test.proto.ComradeResponse;
import comrade.test.proto.ReactorComradeGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

@GrpcService
public class SpringGrpcService extends ReactorComradeGrpc.ComradeImplBase {
    private final Random ran;
    private final List<String> results = List.of("are", "aren't");

    public SpringGrpcService() {
        ran = new Random();
    }

    @Override
    public Mono<ComradeResponse> isComrade(Mono<ComradeRequest> request) {
        return request.map(comradeRequest -> {
            var result = String.format("You %s %s", results.get(ran.nextInt(results.size())), comradeRequest.getName());
            return ComradeResponse.newBuilder().setComradeInfo(result).build();
        });
    }
}
