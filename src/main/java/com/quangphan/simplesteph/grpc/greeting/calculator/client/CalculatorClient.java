package com.quangphan.simplesteph.grpc.greeting.calculator.client;


import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.FindMaximumRequest;
import com.proto.calculator.FindMaximumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    public static void main(String[] args) {

        System.out.println("Hello I'm gRPC client");

        CalculatorClient client = new CalculatorClient();

        client.run();

    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        doBidiStreamCall(channel);

        channel.shutdown();
    }


    private void doBidiStreamCall(ManagedChannel channel) {

        CountDownLatch latch = new CountDownLatch(1);

        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

        StreamObserver<FindMaximumRequest> findMaximumRequestStreamObserver = asyncClient.findMaximum(new StreamObserver<FindMaximumResponse>() {

            @Override
            public void onNext(FindMaximumResponse value) {
                System.out.println("Got new maximum from server: " + value.getMaximum());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending messages");
            }
        });

        Arrays.asList(3, 5, 17, 9, 8, 30, 12).forEach(
                number -> {
                    System.out.println("Sending number: " + number);
                    findMaximumRequestStreamObserver.onNext(FindMaximumRequest.newBuilder()
                            .setNumber(number)
                            .build());

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        findMaximumRequestStreamObserver.onCompleted();

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
