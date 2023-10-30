package seminarNine;

import com.sun.net.httpserver.HttpServer;
import seminarNine.model.UserHandler;
import seminarNine.controller.IUserController;
import seminarNine.controller.UserControllerList;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        IUserController userStorage = new UserControllerList();
        server.createContext("/user", new UserHandler(userStorage));
        server.start();
        System.out.println("Server started on port 8081");

    }
}
