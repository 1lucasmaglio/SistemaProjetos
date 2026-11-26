package dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;

public class UsuarioCSV {

    private Path caminho;

    public UsuarioCSV() {
        caminho = Paths.get("backend/dados/usuarios.csv");
    }

    public void salvar(List<Usuario> usuarios) throws Exception {

        List<String> linhas = new ArrayList<>();

        linhas.add("id;nome;email;senhaHash");

        for (Usuario usuario : usuarios) {

            String linha = usuario.getId() + ";" +
                    usuario.getNome() + ";" +
                    usuario.getEmail() + ";" +
                    usuario.getSenhaHash();

            linhas.add(linha);
        }

        Files.createDirectories(caminho.getParent());
        Files.write(caminho, linhas);
    }

    public List<Usuario> listar() throws Exception {

        List<Usuario> usuarios = new ArrayList<>();

        if (!Files.exists(caminho)) {
            return usuarios;
        }

        List<String> linhas = Files.readAllLines(caminho);

        for (int i = 1; i < linhas.size(); i++) {

            String linha = linhas.get(i);

            String[] dados = linha.split(";");

            int id = Integer.parseInt(dados[0]);

            Usuario usuario = new Usuario(
                    id,
                    dados[1],
                    dados[2],
                    dados[3]);

            usuarios.add(usuario);
        }

        return usuarios;
    }
}