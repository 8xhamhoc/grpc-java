package com.quangphan.simplesteph.grpc.greeting.calculator.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {

        int num1 = request.getCal().getNum1();
        int num2 = request.getCal().getNum2();

        int result = num1 + num2;

        CalculatorResponse response = CalculatorResponse.newBuilder()
                .setResult(result)
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }

    @Override
    public StreamObserver<FindMaximumRequest> findMaximum(StreamObserver<FindMaximumResponse> responseObserver) {

        StreamObserver<FindMaximumRequest> requestObserver = new StreamObserver<FindMaximumRequest>() {

            int currentMaximum = 0;

            @Override
            public void onNext(FindMaximumRequest value) {

                int number = value.getNumber();

                if (number > currentMaximum) {
                    currentMaximum = number;
                    responseObserver.onNext(FindMaximumResponse.newBuilder()
                            .setMaximum(number)
                            .build());
                } else {
                    // nothing
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                // send the current last maximum
                responseObserver.onNext(FindMaximumResponse.newBuilder()
                        .setMaximum(currentMaximum)
                        .build());

                // server is done sending data
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }


    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {

        Integer number = request.getNumber();

        if (number > 0) {
            double numberRoot = Math.sqrt(number);
            responseObserver.onNext(SquareRootResponse.newBuilder()
                    .setNumberRoot(numberRoot)
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                    .withDescription("The number being send is not positive")
                    .augmentDescription("Number send: " + number)
                    .asRuntimeException()
            );
        }
    }

}
