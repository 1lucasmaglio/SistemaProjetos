
package service;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.UsuarioCSV;
import model.Usuario;

public class UsuarioService {

    private List<Usuario> usuarios;
    private UsuarioCSV dao;

    public UsuarioService() {
        usuarios = new ArrayList<>();
        dao = new UsuarioCSV();
    }

    public void carregar() throws Exception {
        usuarios = dao.listar();
    }

    public void salvar() throws Exception {
        dao.salvar(usuarios);
    }

    public Usuario buscarPorEmail(String email) {

        for (Usuario usuario : usuarios) {

            if (usuario.getEmail()
                    .equalsIgnoreCase(email)) {

                return usuario;
            }
        }

        return null;
    }

    public Usuario cadastrar(
            String nome,
            String email,
            String senha
    ) throws Exception {

        if (buscarPorEmail(email) != null) {
            throw new IllegalArgumentException(
                "Email já cadastrado."
            );
        }

        int maiorId = 0;

        for (Usuario usuario : usuarios) {

            if (usuario.getId() > maiorId) {
                maiorId = usuario.getId();
            }
        }

        String senhaHash = BCrypt.hashpw(
            senha,
            BCrypt.gensalt()
        );

        Usuario usuario = new Usuario(
            maiorId + 1,
            nome,
            email,
            senhaHash
        );

        usuarios.add(usuario);

        salvar();

        return usuario;
    }

    public Usuario login(
            String email,
            String senha
    ) {

        Usuario usuario = buscarPorEmail(email);

        if (usuario == null) {
            return null;
        }

        boolean senhaCorreta = BCrypt.checkpw(
            senha,
            usuario.getSenhaHash()
        );

        if (!senhaCorreta) {
            return null;
        }

        return usuario;
    }
}
