package main.app;

import com.sun.net.httpserver.HttpServer;
import main.handler.RelatorioHandler;
import main.handler.RelatorioCsvHandler;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/relatorio", new RelatorioHandler());
        server.createContext("/csv", new RelatorioCsvHandler());
        server.setExecutor(null);
        System.out.println("Servidor rodando em http://localhost:8000");
        server.start();
    }
}