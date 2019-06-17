package com.quangphan.simplesteph.grpc.greeting.client;

import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {

        System.out.println("Hello I'm a gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // Unary
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
//
//        Greeting greeting = Greeting.newBuilder()
//                .setFirstName("Quang")
//                .setLastName("Dung")
//                .build();
//
//        GreetRequest greetRequest = GreetRequest.newBuilder()
//                .setGreeting(greeting)
//                .build();
//
//        GreetResponse response = greetClient.greet(greetRequest);
//
//        System.out.println(response.getResult());
//
//        Calculator calculator = Calculator.newBuilder()
//                .setNum1(10)
//                .setNum2(3)
//                .build();
//
//        CalculatorRequest request = CalculatorRequest.newBuilder()
//                .setCal(calculator)
//                .build();
//
//        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);
//
//        CalculatorResponse calculatorResponse = calculatorClient.sum(request);
//
//        System.out.println("Sum: " + calculatorResponse.getResult());

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Quang")
                .setLastName("Dung")
                .build();

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

        System.out.println("Done");
        channel.shutdown();

    }

}
