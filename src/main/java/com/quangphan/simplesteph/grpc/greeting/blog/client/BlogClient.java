package com.quangphan.simplesteph.grpc.greeting.blog.client;

import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {

        System.out.println("Hello I'm gRPC client");

        BlogClient client = new BlogClient();

        client.run();

    }

    private void run() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();


        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setAuthorId("Quang")
                .setTitle("New Blog")
                .setContent("Hello world this is my first blog")
        .build();

        CreateBlogResponse createResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                .setBlog(blog)
                .build()
        );

        System.out.println("Received create blog response");
        System.out.println(createResponse.getBlog());

        String blogId = createResponse.getBlog().getId();

        System.out.println("Reading blog...");
        ReadBlogResponse readResponse = blogClient.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build()
        );
        System.out.println(readResponse.toString());

        // trigger a not found error
        System.out.println("Reading blog with non existing id...");
        ReadBlogResponse readResponseNotFound = blogClient.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId("fake-id")
                .build()
        );

    }


}
