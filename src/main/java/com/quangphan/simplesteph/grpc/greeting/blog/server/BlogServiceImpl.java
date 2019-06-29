package com.quangphan.simplesteph.grpc.greeting.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase database = mongoClient.getDatabase("mydb");
    private MongoCollection<Document> collection = database.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

        System.out.println("Received Create Blog Request");

        Blog blog = request.getBlog();

        Document document = new Document("author_id", blog.getAuthorId())
                .append("title", blog.getTitle())
                .append("content", blog.getContent());

        System.out.println("Inserting blog...");
        collection.insertOne(document);

        String id = document.getObjectId("_id").toString();
        System.out.println("Inserted blog: " + id);
        // #1
        /*CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(Blog.newBuilder()
                        .setAuthorId(blog.getAuthorId())
                        .setContent(blog.getContent())
                        .setTitle(blog.getTitle())
                        .setId(id))
                .build();*/

        // #2
        CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(blog.toBuilder().setId(id))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }


    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {

        System.out.println("Received Read Blog Request");

        String blogId = request.getBlogId();

        System.out.println("Searching for a blog");
        Document result = null;

        try {
            result = collection.find(Filters.eq("_id", new ObjectId(blogId))).first();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with corresponding id was not found")
                            .augmentDescription(e.getLocalizedMessage())
                            .asRuntimeException()
            );
        }

        if (result == null) {

            System.out.println("Blog not found");

            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with corresponding id was not found")
                            .asRuntimeException()
            );

        } else {

            Blog blog = Blog.newBuilder()
                    .setAuthorId(result.getString("author_id"))
                    .setTitle(result.getString("title"))
                    .setContent(result.getString("content"))
                    .setId(blogId)
                    .build();

            responseObserver.onNext(
                    ReadBlogResponse.newBuilder()
                            .setBlog(blog)
                            .build()
            );

            responseObserver.onCompleted();
        }

    }

}

