package com.quangphan.simplesteph.grpc.greeting.calculator.server;

import com.proto.calculator.*;
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

}
