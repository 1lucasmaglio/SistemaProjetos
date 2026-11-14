
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

    public void adicionar(Projeto projeto) {

        LocalDate hoje = LocalDate.now();

        projeto.setDataCriacao(hoje);

        validarPrazo(projeto);

        int maiorId = 0;

        for (Projeto p : projetos) {

            if (p.getId() > maiorId) {
                maiorId = p.getId();
            }
        }

        projeto.setId(maiorId + 1);
        projetos.add(projeto);
    }

    private void validarPrazo(Projeto projeto) {

        if (projeto.getPrazo() == null) {
            throw new IllegalArgumentException(
                    "O prazo do projeto é obrigatório."
            );
        }

        if (projeto.getPrazo().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "O prazo não pode ser uma data passada."
            );
        }

        if (projeto.getPrazo().isBefore(projeto.getDataCriacao())) {
            throw new IllegalArgumentException(
                    "O prazo não pode ser anterior à data de criação."
            );
        }
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

        Projeto projeto =
                buscarPorId(projetoAtualizado.getId());

        if (projeto == null) {
            return false;
        }

        projeto.setNome(projetoAtualizado.getNome());
        projeto.setDescricao(projetoAtualizado.getDescricao());
        projeto.setCategoria(projetoAtualizado.getCategoria());
        projeto.setStatus(projetoAtualizado.getStatus());
        projeto.setPrazo(projetoAtualizado.getPrazo());
        validarPrazo(projeto);

        return true;
    }
}
