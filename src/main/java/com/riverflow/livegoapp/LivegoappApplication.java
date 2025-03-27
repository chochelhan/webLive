package com.riverflow.livegoapp;


import com.riverflow.livegoapp.jsonRpcHandler.StreamJsonRpcHandler;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.kurento.jsonrpc.internal.server.config.JsonRpcConfiguration;
import org.kurento.jsonrpc.server.JsonRpcConfigurer;
import org.kurento.jsonrpc.server.JsonRpcHandlerRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;

@Import(JsonRpcConfiguration.class)
@SpringBootApplication
public class LivegoappApplication implements JsonRpcConfigurer  {

    public static void main(String[] args) {
        SpringApplication.run(LivegoappApplication.class, args);
    }

    @RequestMapping("/")
    public String home() {
        return "index.html";
    }


    @Override
    public void registerJsonRpcHandlers(JsonRpcHandlerRegistry registry) {
        registry.addHandler(new StreamJsonRpcHandler(), "/jsonRpc"); // “/echo” is the path relative to the server’s URL

    }


}

