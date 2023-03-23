package comrade.test.spring;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executors;

//TODO if you want test it with custom server
//@Component
@Slf4j
public class ServiceConfig implements Closeable {
    private static volatile Server server;

    @Bean
    public void createServer() throws IOException {
        var service = new SpringGrpcService();
        server =  NettyServerBuilder
                .forPort(64004)
                .executor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()))
                .addService(service)
                .build();
        log.info("Server started on port {}", 64004);
        server.start();
    }

    @Override
    public void close() throws IOException {
        try {
            server.shutdown().awaitTermination();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
