package org.springframework.grpc.sample.config;


import io.grpc.netty.NettyServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.ServerBuilderCustomizer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class GrpcServerConfig {
    @Bean
    public ServerBuilderCustomizer<NettyServerBuilder> grpcServerConfigurer() {
        return serverBuilder -> {
            Executor virtualExecutor = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("grpc-vt-", 0).factory()
            );
            serverBuilder.executor(virtualExecutor);
        };
    }
}
