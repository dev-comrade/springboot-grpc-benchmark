package comrade.test.vanilla;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Executors;

@Slf4j
public class VanillaServiceLauncher {
    private static volatile Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        var service = new VanillaGrpcService();
        server = NettyServerBuilder
                .forPort(62004)
                .executor(Executors.newFixedThreadPool(1))
                .addService(service)
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server != null) {
                server.shutdown();
            }
            log.info("Stopped");
        }));

        log.info("gRPC Server started, listening on port 62004.");
        server.awaitTermination();
    }
}