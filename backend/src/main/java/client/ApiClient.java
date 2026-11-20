
package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.Projeto;

public class ApiClient {

    private final HttpClient client;
    private final ObjectMapper mapper;

    private String token;

    private final String baseUrl = "http://localhost:7070";

    public ApiClient() {

        client = HttpClient.newHttpClient();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public void login(String email, char[] senha)
        throws Exception {

        Map<String, Object> dadosLogin = new HashMap<>();

        dadosLogin.put("email", email);
        dadosLogin.put("senha", senha);

        String json =
            mapper.writeValueAsString(dadosLogin);

        HttpRequest request =
            HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        baseUrl + "/api/login"
                    )
                )
                .header(
                    "Content-Type",
                    "application/json"
                )
                .POST(
                    HttpRequest.BodyPublishers
                        .ofString(json)
                )
                .build();

        HttpResponse<String> response =
            client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                "E-mail ou senha incorretos."
            );
        }

        Map<String, Object> resposta =
            mapper.readValue(
                response.body(),
                new TypeReference<Map<String, Object>>() {}
            );

        token = (String) resposta.get("token");
    }

    public List<Projeto> listarProjetos()
        throws Exception {

        HttpRequest request =
            HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        baseUrl + "/api/projetos"
                    )
                )
                .header(
                    "Authorization",
                    "Bearer " + token
                )
                .GET().build();

        HttpResponse<String> response =
            client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                "Erro: " +
                response.statusCode()
            );
        }

        return mapper.readValue(
            response.body(),
            new TypeReference<List<Projeto>>() {}
        );
    }

    public Projeto cadastrarProjeto(
        Projeto projeto
    ) throws Exception {

        String json =
            mapper.writeValueAsString(projeto);

        HttpRequest request =
            HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        baseUrl + "/api/projetos"
                    )
                )
                .header(
                    "Content-Type",
                    "application/json"
                )
                .header(
                    "Authorization",
                    "Bearer " + token
                )
                .POST(
                    HttpRequest.BodyPublishers
                        .ofString(json)
                )
                .build();

        HttpResponse<String> response =
            client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        if (response.statusCode() != 201) {

            Map<String, Object> erro =
                mapper.readValue(
                    response.body(),
                    new TypeReference<Map<String, Object>>() {}
                );

            throw new RuntimeException(
                erro.get("erro").toString()
            );
        }

        return mapper.readValue(
            response.body(),
            Projeto.class
        );
    }

    public Projeto alterarProjeto(
        int id,
        Projeto projeto
    ) throws Exception {

        String json =
            mapper.writeValueAsString(projeto);

        HttpRequest request =
            HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        baseUrl +
                        "/api/projetos/" +
                        id
                    )
                )
                .header(
                    "Content-Type",
                    "application/json"
                )
                .header(
                    "Authorization",
                    "Bearer " + token
                )
                .PUT(
                    HttpRequest.BodyPublishers
                        .ofString(json)
                )
                .build();

        HttpResponse<String> response =
            client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        if (response.statusCode() != 200) {

            Map<String, Object> erro =
                mapper.readValue(
                    response.body(),
                    new TypeReference<Map<String, Object>>() {}
                );

            throw new RuntimeException(
                erro.get("erro").toString()
            );
        }

        return mapper.readValue(
            response.body(),
            Projeto.class
        );
    }

    public void excluirProjeto(int id)
        throws Exception {

        HttpRequest request =
            HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        baseUrl +
                        "/api/projetos/" +
                        id
                    )
                )
                .header(
                    "Authorization",
                    "Bearer " + token
                )
                .DELETE()
                .build();

        HttpResponse<String> response =
            client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

        if (response.statusCode() != 204) {

            throw new RuntimeException(
                "Erro ao excluir projeto: " +
                response.statusCode()
            );
        }
    }

    public void limparToken() {
        token = null;
    }
}
