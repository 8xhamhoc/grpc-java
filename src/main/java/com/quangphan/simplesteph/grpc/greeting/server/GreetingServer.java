package com.quangphan.simplesteph.grpc.greeting.server;

import com.quangphan.simplesteph.grpc.greeting.calculator.server.CalculatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;

public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Hello gRPC");

        // Plaintext server
        /*Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .addService(new CalculatorServiceImpl())
                .build();*/


        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .useTransportSecurity(
                        new File("ssl/server.crt"),
                        new File("ssl/server.pem")
                )
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
