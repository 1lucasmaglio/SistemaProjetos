package api;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import service.ProjetoService;

public class Api {

    public static void main(String[] args)
            throws Exception {

        ProjetoService service =
                new ProjetoService();

        service.carregar();

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(7070),
                        0
                );

        server.createContext("/", exchange -> {

            String resposta =
                    "API Sistema de Projetos";

            exchange.sendResponseHeaders(
                    200,
                    resposta.getBytes().length
            );

            exchange.getResponseBody()
                    .write(resposta.getBytes());

            exchange.close();
        });

        server.start();

        System.out.println(
                "Servidor iniciado"
        );
    }
}