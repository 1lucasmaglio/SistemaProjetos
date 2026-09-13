import model.Projeto;

public class Main {

    public static void main(String[] args) {

        System.out.println("==============================");
        System.out.println("     SISTEMA DE PROJETOS     ");
        System.out.println("==============================");
        System.out.println();
        System.out.println("Projeto: Portfólio Acadêmico");
        System.out.println("Desenvolvido em Java");
        System.out.println("Versão: 1.0");
        System.out.println();

        Projeto projeto1 = new Projeto(
            1,
            "Sistema Acadêmico",
            "Sistema para gerenciamento acadêmico",
            "Software",
            "Em desenvolvimento"
        );

        Projeto projeto2 = new Projeto(
            2,
            "Site Institucional",
            "Website de uma instituição",
            "Web",
            "Concluído"
        );

        Projeto projeto3 = new Projeto(
            3,
            "Aplicativo Mobile",
            "Aplicativo",
            "Mobile",
            "Planejado"
        );

        projeto1.exibirDados();
        System.out.println();
        projeto2.exibirDados();
        System.out.println();
        projeto3.exibirDados();
    }
}
