
package api;

import java.util.List;
import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.Context;
import model.LoginRequest;
import model.Projeto;
import model.Usuario;
import service.ProjetoService;
import service.SessaoService;
import service.UsuarioService;

public class Api {

    public static void main(String[] args)
            throws Exception {

        ProjetoService service =
                new ProjetoService();

        service.carregar();

        UsuarioService usuarioService =
                new UsuarioService();

        usuarioService.carregar();

        SessaoService sessaoService =
                new SessaoService();

        var app = Javalin.create(config -> {

            // Permitir acesso pelo navegador
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.anyHost();
                });
            });

            // Rota inicial
            config.routes.get(
                    "/",
                    ctx -> {
                        ctx.result(
                                "API Sistema de Projetos"
                        );
                    }
            );

            // Login
            config.routes.post(
                    "/api/login",
                    ctx -> {

                        LoginRequest request =
                                ctx.bodyAsClass(LoginRequest.class);

                        Usuario usuario =
                                usuarioService.login(
                                        request.getEmail(),
                                        request.getSenha()
                                );

                        if (usuario == null) {
                            ctx.status(401);
                            return;
                        }

                        String token =
                                sessaoService.criarSessao(usuario);

                        ctx.json(
                                Map.of(
                                        "token", token,
                                        "id", usuario.getId(),
                                        "nome", usuario.getNome(),
                                        "email", usuario.getEmail()
                                )
                        );
                    }
            );

            // Listar projetos
            config.routes.get(
                    "/api/projetos",
                    ctx -> {

                        Usuario usuario =
                                validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        List<Projeto> projetos =
                                service.listarPorUsuario(usuario.getId());

                        ctx.json(projetos);
                    }
            );

            // Buscar projeto por ID
            config.routes.get(
                    "/api/projetos/{id}",
                    ctx -> {

                        Usuario usuario =
                                validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        int id =
                                Integer.parseInt(
                                        ctx.pathParam("id")
                                );

                        Projeto projeto =
                                service.buscarPorId(id);

                        if (projeto == null) {
                            ctx.status(404);
                            return;
                        }

                        if (projeto.getUsuarioId() != usuario.getId()) {
                            ctx.status(404);
                            return;
                        }

                        ctx.json(projeto);
                    }
            );

            // Cadastrar projeto
            config.routes.post(
                    "/api/projetos",
                    ctx -> {

                        Usuario usuario =
                                validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        Projeto projeto =
                                ctx.bodyAsClass(Projeto.class);

                        projeto.setUsuarioId(usuario.getId());

                        service.adicionar(projeto);

                        service.salvar();

                        ctx.status(201);
                        ctx.json(projeto);
                    }
            );

            // Alterar projeto
            config.routes.put(
                    "/api/projetos/{id}",
                    ctx -> {

                        Usuario usuario =
                                validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        int id =
                                Integer.parseInt(
                                        ctx.pathParam("id")
                                );

                        Projeto existente = service.buscarPorId(id);

                        if (existente == null) {
                            ctx.status(404);
                            return;
                        }

                        if (existente.getUsuarioId() != usuario.getId()) {
                            ctx.status(404);
                            return;
                        }

                        Projeto projeto =
                                ctx.bodyAsClass(Projeto.class);

                        projeto.setId(id);  
                        projeto.setUsuarioId(existente.getUsuarioId());

                        boolean alterado;

                        try {
                            alterado = service.alterar(projeto);
                        } catch (IllegalArgumentException e) {
                            ctx.status(400);
                            ctx.json(Map.of("erro", e.getMessage()));
                            return;
                        }

                        if (!alterado) {
                            ctx.status(404);
                            return;
                        }

                        service.salvar();

                        ctx.json(projeto);
                    }
            );

            // Excluir projeto
            config.routes.delete(
                    "/api/projetos/{id}",
                    ctx -> {

                        Usuario usuario =
                                validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        int id =
                                Integer.parseInt(
                                        ctx.pathParam("id")
                                );

                        Projeto existente = service.buscarPorId(id);

                        if (existente == null ||
                            existente.getUsuarioId() != usuario.getId()) {
                            ctx.status(404);
                            return;
                        }

                        boolean removido =
                                service.removerPorId(id);

                        if (!removido) {
                            ctx.status(404);
                            return;
                        }

                        service.salvar();

                        ctx.status(204);
                    }
            );

        }).start(7070);
    }

    /* Verifica se a requisição possui um token válido. */
    private static Usuario validarToken(
            Context ctx,
            SessaoService sessaoService) {

        // Recupera o conteúdo do cabeçalho Authorization.
        String authorization = ctx.header("Authorization");

        // Verifica se o cabeçalho existe e começa com Bearer.
        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            ctx.status(401);
            return null;
        }

        // Remove a parte "Bearer " e recupera somente o token.
        String token = authorization.substring(7);

        // Procura o usuário associado ao token.
        Usuario usuario = sessaoService.buscarUsuario(token);

        // Verifica se o token corresponde a uma sessão existente.
        if (usuario == null) {
            ctx.status(401);
            return null;
        }

        // Retorna o usuário autenticado.
        return usuario;
    }
}
