package com.quangphan.simplesteph.grpc.greeting.blog.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class BlogServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Hello gRPC");

        Server server = ServerBuilder.forPort(50051)
                .addService(new BlogServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {

            System.out.println("Received Shutdown Request");

            server.shutdown();

            System.out.println("Successfully stopped the server");

        }));

        server.awaitTermination();

    }

}
