
package dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Projeto;

public class ProjetoCSV {

    private Path caminho;

    public ProjetoCSV() {
        caminho = Paths.get("backend/dados/projetos.csv");
    }

    public void salvar(List<Projeto> projetos) throws Exception {

        List<String> linhas = new ArrayList<>();

        linhas.add("id;usuarioId;nome;descricao;categoria;status;dataCriacao;prazo");

        for (Projeto projeto : projetos) {

            String linha = projeto.getId() + ";" +
                    projeto.getUsuarioId() + ";" +
                    projeto.getNome() + ";" +
                    projeto.getDescricao() + ";" +
                    projeto.getCategoria() + ";" +
                    projeto.getStatus() + ";" +
                    projeto.getDataCriacao() + ";" +
                    projeto.getPrazo();

            linhas.add(linha);
        }

        Files.createDirectories(caminho.getParent());
        Files.write(caminho, linhas);
    }

    public List<Projeto> listar() throws Exception {

        List<Projeto> projetos = new ArrayList<>();

        if (!Files.exists(caminho)) {
            return projetos;
        }

        List<String> linhas = Files.readAllLines(caminho);

        for (int i = 1; i < linhas.size(); i++) {

            String[] dados = linhas.get(i).split(";");

            int id = Integer.parseInt(dados[0]);
            int usuarioId = Integer.parseInt(dados[1]);
            String nome = dados[2];
            String descricao = dados[3];
            String categoria = dados[4];
            String status = dados[5];
            LocalDate dataCriacao = LocalDate.parse(dados[6]);
            LocalDate prazo = LocalDate.parse(dados[7]);

            Projeto projeto = new Projeto(
                    id,
                    nome,
                    descricao,
                    categoria,
                    status);

            projeto.setUsuarioId(usuarioId);
            projeto.setDataCriacao(dataCriacao);
            projeto.setPrazo(prazo);

            projetos.add(projeto);
        }

        return projetos;
    }
}
