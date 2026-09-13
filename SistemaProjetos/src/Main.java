import model.Projeto;

public class Main {

    public static void main(String[] args) {

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

        projeto1.exibirDados();
        System.out.println();
        projeto2.exibirDados();
        
    }
}
