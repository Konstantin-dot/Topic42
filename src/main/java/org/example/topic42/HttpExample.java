package org.example.topic42;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpExample {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException {
        getTodos();
        createTodo();
        updateTodo();
        deleteTodo();
    }

    // 1. GET-запрашивает представление ресурса
    public static void getTodos() throws IOException, InterruptedException {
        System.out.println("GET");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/todos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Todo[] todos = mapper.readValue(response.body(), Todo[].class);

        for (int i = 0; i < 5 && i < todos.length; i++) {
            System.out.println("ID: " + todos[i].id + ", Title: " + todos[i].title);
        }
    }

    // POST-отправляет данные для обработки к ресурсу
    public static void createTodo() throws IOException, InterruptedException {
        System.out.println("\nPOST");

        ObjectNode json = mapper.createObjectNode();
        json.put("title", "Learn Java HTTP");
        json.put("completed", false);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.body());
    }

    // PUT-загружает представление ресурса
    public static void updateTodo() throws IOException, InterruptedException {
        System.out.println("\nPUT /todos/1");

        ObjectNode json = mapper.createObjectNode();
        json.put("title", "Updated Title");
        json.put("completed", true);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/todos/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.body());
    }

    // DELETE-удаляет указанный ресурс
    public static void deleteTodo() throws IOException, InterruptedException {
        System.out.println("\n=== DELETE /todos/1 ===");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/todos/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + response.statusCode());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Todo {
        public int id;
        public String title;
        public boolean completed;
    }
}
