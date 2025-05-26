package main.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class RelatorioCsvHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String csv = "Categoria,Valor\nEnergia,1000\nMateriais,500\nTransporte,300";
        exchange.getResponseHeaders().set("Content-Type", "text/csv");
        exchange.sendResponseHeaders(200, csv.length());
        OutputStream os = exchange.getResponseBody();
        os.write(csv.getBytes());
        os.close();
    }
}