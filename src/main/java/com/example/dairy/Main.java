package com.example.dairy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.context.ApplicationListener<org.springframework.boot.web.context.WebServerInitializedEvent> onStartup() {
        return event -> {
            int port = event.getWebServer().getPort();
            System.out.println("\n----------------------------------------------------------");
            System.out.println("Application is running! Access it at: http://localhost:" + port);
            System.out.println("----------------------------------------------------------\n");
        };
    }
}
