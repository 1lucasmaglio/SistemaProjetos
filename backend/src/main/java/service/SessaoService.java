package service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import model.Usuario;

public class SessaoService {

    private final Map<String, Usuario> sessoes =
            new ConcurrentHashMap<>();

    public String criarSessao(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        sessoes.put(token, usuario);

        return token;
    }

    public Usuario buscarUsuario(String token) {
        return sessoes.get(token);
    }

    public void encerrarSessao(String token) {
        sessoes.remove(token);
    }
}