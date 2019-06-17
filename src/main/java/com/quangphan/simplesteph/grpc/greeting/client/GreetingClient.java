package com.quangphan.simplesteph.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GreetingClient {

    ManagedChannel channel;

    private void run() {

        channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

//        doUnaryCall(channel);
//        doServerStreamCall(channel);
        doClientStreamCall(channel);

        channel.shutdown();
    }

    private void doClientStreamCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                // we get reponse from server

                // onNext will be called only once
            }

            @Override
            public void onError(Throwable t) {
                // we get error from server
            }

            @Override
            public void onCompleted() {
                // the server done sending us data

                // onComplete will be called right after onNext()
            }
        });

    }

    private void doServerStreamCall(ManagedChannel channel) {

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

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
    }

    private void doUnaryCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Quang")
                .setLastName("Dung")
                .build();

        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        GreetResponse response = greetClient.greet(greetRequest);

        System.out.println(response.getResult());

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
    }

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        GreetingClient main = new GreetingClient();

        main.run();

        System.out.println("Done");
    }

}
