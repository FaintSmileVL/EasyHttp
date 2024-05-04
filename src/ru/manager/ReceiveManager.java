package ru.manager;

import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import ru.receive.IPostHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author : faint
 * @date : 04.05.2024
 * @time : 13:29
 */
public class ReceiveManager {
    @Getter(lazy = true)
    private static final ReceiveManager instance = new ReceiveManager();
    private HttpServer server;
    private ReceiveManager() {
    }

    public void init(int serviceSocket) {
        try {
            server = HttpServer.create(new InetSocketAddress(serviceSocket), 0);
            server.setExecutor(null); // создает исполнителя по умолчанию
            server.start();
            System.out.println("Remote control server started using port " + serviceSocket);
        } catch (IOException e) {
            System.out.println("Remote control server cannot start using port " + serviceSocket);
            e.printStackTrace();
        }
    }

    public void addHandler(String contextName, IPostHandler handler) {
        server.createContext(contextName, handler);
    }
}
