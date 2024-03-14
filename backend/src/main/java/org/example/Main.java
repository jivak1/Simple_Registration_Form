package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.controllers.EditUserController;
import org.example.controllers.LoginController;
import org.example.controllers.RegisterController;
import org.example.model.UserEntity;
import org.example.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/register", new RegisterController());
        server.createContext("/login", new LoginController());
        server.createContext("/edit-profile", new EditUserController());


        server.start();

        System.out.println("Server started on port 8000");

    }
}