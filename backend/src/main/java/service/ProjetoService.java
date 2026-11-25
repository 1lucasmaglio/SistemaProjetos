
package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.ProjetoCSV;
import model.Projeto;

public class ProjetoService {

    private List<Projeto> projetos;
    private ProjetoCSV dao;

    public ProjetoService() {
        projetos = new ArrayList<>();
        dao = new ProjetoCSV();
    }

    public void carregar() throws Exception {
        projetos = dao.listar();
    }

    public void salvar() throws Exception {
        dao.salvar(projetos);
    }

    private void validarProjeto(Projeto projeto) {

        // 1. Validate name

        if (projeto.getNome() == null) {
            throw new IllegalArgumentException(
                    "O nome do projeto é obrigatório.");
        }

        String nome = projeto.getNome().trim();

        if (nome.isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome do projeto é obrigatório.");
        }

        if (nome.length() < 3 || nome.length() > 100) {
            throw new IllegalArgumentException(
                    "O nome deve ter entre 3 e 100 caracteres.");
        }

        projeto.setNome(nome);

        // 2. Validate description

        if (projeto.getDescricao() == null) {
            throw new IllegalArgumentException(
                    "A descrição do projeto é obrigatória.");
        }

        String descricao = projeto.getDescricao().trim();

        if (descricao.length() < 10 || descricao.length() > 500) {
            throw new IllegalArgumentException(
                    "A descrição deve ter entre 10 e 500 caracteres.");
        }

        projeto.setDescricao(descricao);

        // 3. Validate category

        if (projeto.getCategoria() == null ||
                !List.of("Software", "Mobile", "Web", "Desktop")
                        .contains(projeto.getCategoria())) {

            throw new IllegalArgumentException(
                    "Categoria inválida.");
        }

        // 4. Validate status

        if (projeto.getStatus() == null ||
                !List.of(
                        "Planejado",
                        "Em Desenvolvimento",
                        "Concluído",
                        "Cancelado").contains(projeto.getStatus())) {

            throw new IllegalArgumentException(
                    "Status inválido.");
        }

        // 5. Validate deadline

        if (projeto.getPrazo() == null) {
            throw new IllegalArgumentException(
                    "O prazo do projeto é obrigatório.");
        }

        if (projeto.getPrazo().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "O prazo não pode ser uma data passada.");
        }

        if (projeto.getDataCriacao() != null &&
                projeto.getPrazo().isBefore(projeto.getDataCriacao())) {

            throw new IllegalArgumentException(
                    "O prazo não pode ser anterior à data de criação.");
        }
    }

    public void adicionar(Projeto projeto) {

        LocalDate hoje = LocalDate.now();

        projeto.setDataCriacao(hoje);

        validarProjeto(projeto);

        int maiorId = 0;

        for (Projeto p : projetos) {

            if (p.getId() > maiorId) {
                maiorId = p.getId();
            }
        }

        projeto.setId(maiorId + 1);
        projetos.add(projeto);
    }

    public List<Projeto> listar() {
        return projetos;
    }

    public List<Projeto> listarPorUsuario(int usuarioId) {

        List<Projeto> resultado = new ArrayList<>();

        for (Projeto projeto : projetos) {

            if (projeto.getUsuarioId() == usuarioId) {
                resultado.add(projeto);
            }
        }

        return resultado;
    }

    public Projeto buscarPorId(int id) {

        for (Projeto projeto : projetos) {

            if (projeto.getId() == id) {
                return projeto;
            }
        }

        return null;
    }

    public List<Projeto> buscarPorCategoria(String categoria) {

        List<Projeto> resultado = new ArrayList<>();

        for (Projeto projeto : projetos) {

            if (projeto.getCategoria()
                    .equalsIgnoreCase(categoria)) {

                resultado.add(projeto);
            }
        }

        return resultado;
    }

    public List<Projeto> buscarPorStatus(String status) {

        List<Projeto> resultado = new ArrayList<>();

        for (Projeto projeto : projetos) {

            if (projeto.getStatus()
                    .equalsIgnoreCase(status)) {

                resultado.add(projeto);
            }
        }

        return resultado;
    }

    public boolean removerPorId(int id) {

        Projeto projeto = buscarPorId(id);

        if (projeto == null) {
            return false;
        }

        projetos.remove(projeto);

        return true;
    }

    public boolean alterar(Projeto projetoAtualizado) {

        Projeto projeto = buscarPorId(projetoAtualizado.getId());

        if (projeto == null) {
            return false;
        }

        projetoAtualizado.setDataCriacao(projeto.getDataCriacao());

        validarProjeto(projetoAtualizado);

        projeto.setNome(projetoAtualizado.getNome());
        projeto.setDescricao(projetoAtualizado.getDescricao());
        projeto.setCategoria(projetoAtualizado.getCategoria());
        projeto.setStatus(projetoAtualizado.getStatus());
        projeto.setPrazo(projetoAtualizado.getPrazo());

        return true;
    }
}
