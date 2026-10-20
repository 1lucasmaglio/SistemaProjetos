
package api;

import java.util.List;

import io.javalin.Javalin;
import model.Projeto;
import service.ProjetoService;

public class Api {

    public static void main(String[] args)
            throws Exception {

        ProjetoService service =
                new ProjetoService();

        service.carregar();

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

            // Listar projetos
            config.routes.get(
                    "/api/projetos",
                    ctx -> {

                        List<Projeto> projetos =
                                service.listar();

                        ctx.json(projetos);
                    }
            );

            // Buscar projeto por ID
            config.routes.get(
                    "/api/projetos/{id}",
                    ctx -> {

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

                        ctx.json(projeto);
                    }
            );

            // Cadastrar projeto
            config.routes.post(
                    "/api/projetos",
                    ctx -> {

                        Projeto projeto =
                                ctx.bodyAsClass(Projeto.class);

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

                        int id =
                                Integer.parseInt(
                                        ctx.pathParam("id")
                                );

                        Projeto projeto =
                                ctx.bodyAsClass(Projeto.class);

                        projeto.setId(id);

                        boolean alterado =
                                service.alterar(projeto);

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

                        int id =
                                Integer.parseInt(
                                        ctx.pathParam("id")
                                );

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
}
