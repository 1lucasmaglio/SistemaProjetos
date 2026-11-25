
package api;

import java.util.List;
import java.util.Map;

import dao.ArquivoLog;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.LoginRequest;
import model.Projeto;
import model.Usuario;
import service.LogService;
import service.ProjetoService;
import service.SessaoService;
import service.UsuarioService;
import model.CadastroRequest;

public class Api {

    public static void main(String[] args)
            throws Exception {

        ArquivoLog arquivoLog = new ArquivoLog();

        // Carregar projetos
        ProjetoService service = new ProjetoService();

        try {

            service.carregar();

        } catch (Exception e) {

            registrarSeguro(
                    arquivoLog,
                    "error.log",
                    "Erro ao carregar projetos do CSV: " +
                            e.getMessage());

            throw e;
        }

        LogService logService = new LogService();

        // Carregar usuários
        UsuarioService usuarioService = new UsuarioService();

        try {

            usuarioService.carregar();

        } catch (Exception e) {

            registrarSeguro(
                    arquivoLog,
                    "error.log",
                    "Erro ao carregar usuários do CSV: " +
                            e.getMessage());

            throw e;
        }

        SessaoService sessaoService = new SessaoService();

        var app = Javalin.create(config -> {

            // Permitir acesso pelo navegador
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.anyHost();
                });
            });

            // Registrar requisições da API
            config.requestLogger.http((ctx, ms) -> {

                registrarSeguro(
                        arquivoLog,
                        "api.log",
                        ctx.method() + " " +
                                ctx.path() +
                                " - Status: " + ctx.status() +
                                " - " + ms + " ms");
            });

            // Registrar inicialização e encerramento
            config.events.serverStarted(() -> {

                registrarSeguro(
                        arquivoLog,
                        "application.log",
                        "API iniciada na porta 7070.");
            });

            config.events.serverStopping(() -> {

                registrarSeguro(
                        arquivoLog,
                        "application.log",
                        "API sendo encerrada.");
            });

            // Registrar erros inesperados
            config.routes.exception(Exception.class, (e, ctx) -> {

                registrarSeguro(
                        arquivoLog,
                        "error.log",
                        "Erro inesperado: " +
                                e.getClass().getSimpleName() +
                                " - " + e.getMessage());

                registrarSeguro(
                        arquivoLog,
                        "application.log",
                        "Erro geral na aplicação: " +
                                e.getMessage());

                ctx.status(500);
                ctx.json(Map.of("erro", "Erro interno do servidor"));
            });

            // Rota inicial
            config.routes.get(
                    "/",
                    ctx -> {
                        ctx.result(
                                "API Sistema de Projetos");
                    });

            // Login
            config.routes.post(
                    "/api/login",
                    ctx -> {

                        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);

                        Usuario usuario = usuarioService.login(
                                request.getEmail(),
                                request.getSenha());

                        if (usuario == null) {

                            registrarSeguro(
                                    arquivoLog,
                                    "access.log",
                                    "Tentativa de login sem sucesso: " +
                                            request.getEmail());

                            ctx.status(401);
                            return;
                        }

                        String token = sessaoService.criarSessao(usuario);

                        registrarSeguro(
                                arquivoLog,
                                "access.log",
                                "Usuário " + usuario.getId() +
                                        " (" + usuario.getNome() +
                                        ") realizou login.");

                        ctx.json(
                                Map.of(
                                        "token", token,
                                        "id", usuario.getId(),
                                        "nome", usuario.getNome(),
                                        "email", usuario.getEmail()));
                    });

            // Cadastro
            config.routes.post(
                    "/api/cadastro",
                    ctx -> {

                        CadastroRequest request = ctx.bodyAsClass(CadastroRequest.class);

                        try {

                            Usuario usuario = usuarioService.cadastrar(
                                    request.getNome(),
                                    request.getEmail(),
                                    request.getSenha());

                            // Registrar criação da conta
                            registrarSeguro(
                                    arquivoLog,
                                    "application.log",
                                    "Conta criada: " + usuario.getEmail());

                            ctx.status(201);

                            ctx.json(
                                    Map.of(
                                            "id", usuario.getId(),
                                            "nome", usuario.getNome(),
                                            "email", usuario.getEmail()));

                        } catch (IllegalArgumentException e) {

                            ctx.status(409);
                            ctx.json(
                                    Map.of("erro", e.getMessage()));
                        }
                    });

            // Listar projetos
            config.routes.get(
                    "/api/projetos",
                    ctx -> {

                        Usuario usuario = validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        List<Projeto> projetos = service.listarPorUsuario(usuario.getId());

                        ctx.json(projetos);
                    });

            // Buscar projeto por ID
            config.routes.get(
                    "/api/projetos/{id}",
                    ctx -> {

                        Usuario usuario = validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        int id = Integer.parseInt(
                                ctx.pathParam("id"));

                        Projeto projeto = service.buscarPorId(id);

                        if (projeto == null) {
                            ctx.status(404);
                            return;
                        }

                        if (projeto.getUsuarioId() != usuario.getId()) {
                            ctx.status(404);
                            return;
                        }

                        ctx.json(projeto);
                    });

            // Cadastrar projeto
            config.routes.post(
                    "/api/projetos",
                    ctx -> {

                        Usuario usuario = validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        Projeto projeto = ctx.bodyAsClass(Projeto.class);

                        projeto.setUsuarioId(usuario.getId());

                        try {

                            service.adicionar(projeto);

                        } catch (IllegalArgumentException e) {

                            registrarSeguro(
                                    arquivoLog,
                                    "error.log",
                                    "Erro ao cadastrar projeto '" +
                                            projeto.getNome() + "': " +
                                            e.getMessage());

                            ctx.status(400);
                            ctx.json(Map.of("erro", e.getMessage()));
                            return;
                        }

                        service.salvar();

                        logService.adicionar(
                                usuario.getId(),
                                "Inclusão",
                                "Projeto '" + projeto.getNome() +
                                        "' (ID: " + projeto.getId() + ") criado.");

                        ctx.status(201);
                        ctx.json(projeto);
                    });

            // Alterar projeto
            config.routes.put(
                    "/api/projetos/{id}",
                    ctx -> {

                        Usuario usuario = validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        int id = Integer.parseInt(
                                ctx.pathParam("id"));

                        Projeto existente = service.buscarPorId(id);

                        if (existente == null ||
                                existente.getUsuarioId() != usuario.getId()) {

                            ctx.status(404);
                            return;
                        }

                        Projeto projeto = ctx.bodyAsClass(Projeto.class);

                        projeto.setId(id);
                        projeto.setUsuarioId(existente.getUsuarioId());

                        boolean alterado;

                        try {

                            alterado = service.alterar(projeto);

                        } catch (IllegalArgumentException e) {

                            registrarSeguro(
                                    arquivoLog,
                                    "error.log",
                                    "Erro ao alterar projeto '" +
                                            existente.getNome() + "' (ID: " +
                                            id + "): " + e.getMessage());

                            ctx.status(400);
                            ctx.json(Map.of("erro", e.getMessage()));
                            return;
                        }

                        if (!alterado) {
                            ctx.status(404);
                            return;
                        }

                        service.salvar();

                        logService.adicionar(
                                usuario.getId(),
                                "Alteração",
                                "Projeto '" + projeto.getNome() +
                                        "' (ID: " + projeto.getId() + ") alterado.");

                        ctx.json(projeto);
                    });

            // Excluir projeto
            config.routes.delete(
                    "/api/projetos/{id}",
                    ctx -> {

                        Usuario usuario = validarToken(ctx, sessaoService);

                        if (usuario == null) {
                            return;
                        }

                        int id = Integer.parseInt(
                                ctx.pathParam("id"));

                        Projeto existente = service.buscarPorId(id);

                        if (existente == null ||
                                existente.getUsuarioId() != usuario.getId()) {

                            ctx.status(404);
                            return;
                        }

                        boolean removido = service.removerPorId(id);

                        if (!removido) {
                            ctx.status(404);
                            return;
                        }

                        service.salvar();

                        logService.adicionar(
                                usuario.getId(),
                                "Exclusão",
                                "Projeto '" + existente.getNome() +
                                        "' (ID: " + id + ") excluído.");

                        ctx.status(204);
                    });

        }).start(7070);
    }

    // Registrar mensagens sem interromper a API caso ocorra erro no log
    private static void registrarSeguro(
            ArquivoLog arquivoLog,
            String arquivo,
            String mensagem) {

        try {

            arquivoLog.registrar(arquivo, mensagem);

        } catch (Exception e) {

            System.err.println(
                    "Erro ao escrever no arquivo " +
                            arquivo + ": " + e.getMessage());
        }
    }

    /* Verifica se a requisição possui um token válido. */
    private static Usuario validarToken(
            Context ctx,
            SessaoService sessaoService) {

        String authorization = ctx.header("Authorization");

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            ctx.status(401);
            return null;
        }

        String token = authorization.substring(7);

        Usuario usuario = sessaoService.buscarUsuario(token);

        if (usuario == null) {
            ctx.status(401);
            return null;
        }

        return usuario;
    }
}
