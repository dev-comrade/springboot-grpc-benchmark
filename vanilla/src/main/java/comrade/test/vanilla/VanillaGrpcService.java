package comrade.test.vanilla;

import comrade.test.proto.ComradeGrpc;
import comrade.test.proto.ComradeRequest;
import comrade.test.proto.ComradeResponse;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Random;

public class VanillaGrpcService extends ComradeGrpc.ComradeImplBase {
    private final Random ran;
    private final List<String> results = List.of("are", "aren't");

    public VanillaGrpcService() {
        ran = new Random();
    }

    @Override
    public void isComrade(ComradeRequest request, StreamObserver<ComradeResponse> responseObserver) {
        var result = String.format("You %s %s", results.get(ran.nextInt(results.size())), request.getName());
        var response = ComradeResponse.newBuilder().setComradeInfo(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
