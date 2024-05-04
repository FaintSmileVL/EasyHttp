package ru.factory;

import ru.manager.RequestManager;

import java.net.URI;
import java.net.http.HttpRequest;

/**
 * @author : faint
 * @date : 04.05.2024
 * @time : 12:08
 */
public class RequestFactory {
    public static HttpRequest create(String jsonBody, String handlerName) {
        return HttpRequest.newBuilder()
                .uri(
                        URI.create(
                                RequestManager.getInstance().getExternalApiServerAddress().replace("handler", handlerName)
                        )
                ) // URL вашего API
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }
}
