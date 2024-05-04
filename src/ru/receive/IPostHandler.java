package ru.receive;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.enums.ERequestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * @author : faint
 * @date : 04.05.2024
 * @time : 13:45
 */
public abstract class IPostHandler implements HttpHandler {
    protected Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "{\"status\":\"%s\"}";
        switch (ERequestMethod.valueOf(exchange.getRequestMethod())) {
            case POST -> {
                try {
                    response = handlePost(exchange, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            default -> exchange.sendResponseHeaders(405, -1); // Method Not Allowed;
        }
        try (OutputStream os = exchange.getResponseBody()) {
            System.out.println("IPostHandler response =" + response);
            os.write(response.getBytes());
        }
    }

    /**
     * Возвращает измененную строку response
     * @param exchange
     * @param response
     * @return
     * @throws IOException
     */
    public abstract String handlePost(HttpExchange exchange, String response) throws IOException;
    protected String convertUsingScanner(InputStream is) {
        try (Scanner scanner = new Scanner(is, "UTF-8")) {
            return scanner.useDelimiter("\\A").next();
        }
    }
}
