package ru.manager;

import com.google.gson.Gson;
import lombok.Getter;
import ru.factory.RequestFactory;
import ru.request.IRequestHandler;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;

/**
 * @author : faint
 * @date : 04.05.2024
 * @time : 12:08
 */
public class RequestManager {
    @Getter(lazy = true)
    private static final RequestManager instance = new RequestManager();
    private HashMap<String, IRequestHandler> handlerMap = new HashMap<String, IRequestHandler>();
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new Gson();
    @Getter
    private String externalApiServerAddress = "http://localhost:port/handler";

    /**
     * Устанавливаем ip адрес сервера
     * @param address
     */
    public void setExternalAddress(String address) {
        externalApiServerAddress = externalApiServerAddress.replace("localhost", address);
    }

    /**
     * Устанавливаем порт сервера
     * @param port
     */
    public void setPort(String port) {
        externalApiServerAddress = externalApiServerAddress.replace("port", port);
    }

    /**
     * Добавляем обработчик
     * @param handlerName
     * @param value
     */
    public void addHandler(String handlerName, IRequestHandler value) {
        handlerMap.putIfAbsent(handlerName, value);
    }

    /**
     *
     * @param handlerName
     * @return
     */
    public String handle(String handlerName) {
        var handler = handlerMap.get(handlerName);
        return handler.handle();
    }

    /**
     *
     * @param jsonBody
     * @param handlerName
     * @return
     */
    public <T> T request(String jsonBody, String handlerName, Class<T> clazz) {
        var request = RequestFactory.create(jsonBody, handlerName);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
