package seminarNine.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import seminarNine.controller.IUserController;
import seminarNine.exceptions.PathParameterException;
import seminarNine.exceptions.UserNotFountException;

import java.io.*;
import java.util.Arrays;

public class UserHandler implements HttpHandler {
    private final IUserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserHandler(IUserController userStorage) {
        this.userController = userStorage;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        StringBuilder responseBody = new StringBuilder();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        String[] pathElements = exchange.getRequestURI().getPath().split("/");

        switch (requestMethod) {
            case "GET":
                responseBody.append(objectMapper.writeValueAsString(userController.getUsers()));
                exchange.sendResponseHeaders(200, responseBody.length());
                break;
            case "POST":
                try {
                    if (pathElements.length == 3) {
                        User newUser = userController.addUser(pathElements[2]);
                        responseBody.append(objectMapper.writeValueAsString(newUser));
                        exchange.sendResponseHeaders(201, responseBody.length());
                    } else if (pathElements.length == 2) {
                        String bodyString = getRequestBodyString(exchange.getRequestBody());
                        userController.addAll(Arrays.asList(objectMapper.readValue(bodyString, User[].class)));
                        exchange.sendResponseHeaders(200, responseBody.length());
                    } else {
                        throw new PathParameterException("user name not found");
                    }
                } catch (PathParameterException e) {
                    responseBody.append("{\"error\":\"method POST : ").append(e.getMessage()).append("\"}");
                    exchange.sendResponseHeaders(400, responseBody.length());
                } catch (Exception e) {
                    responseBody.append(e.getMessage());
                    exchange.sendResponseHeaders(500, responseBody.length());
                }
                break;
            case "DELETE":
                try {
                    if (pathElements.length == 3) {
                        int userId = Integer.parseInt(pathElements[2]);
                        responseBody.append(objectMapper.writeValueAsString(userController.deleteUser(userId)));
                        exchange.sendResponseHeaders(200, responseBody.length());
                    } else {
                        throw new PathParameterException("user id not found");
                    }
                } catch (UserNotFountException e) {
                    responseBody.append("{\"error\":\"method DELETE : ").append(e.getMessage()).append("\"}");
                    exchange.sendResponseHeaders(404, responseBody.length());
                } catch (Exception e) {
                    responseBody.append("{\"error\":\"method DELETE : ").append(e.getMessage()).append("\"}");
                    exchange.sendResponseHeaders(400, responseBody.length());
                }
                break;
            case "OPTIONS":
                exchange.sendResponseHeaders(200, responseBody.length());
                break;
            default:
                responseBody.append("{\"error\":\"Method not allowed\"}");
                exchange.sendResponseHeaders(405, responseBody.length());
        }

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBody.toString().getBytes());
        }
    }

    private String getRequestBodyString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder result = new StringBuilder();
            while (reader.ready()) {
                result.append(reader.readLine());
            }
            return result.toString();
        }
    }
}
