/* Endereço da API Java responsável pelos projetos. */
const API = "http://localhost:7070/api/projetos";

/* Busca o formulário de cadastro pelo ID definido no HTML. */
const formulario = document.getElementById("formProjeto");

/* Executa esta função quando o usuário envia o formulário. */
formulario.addEventListener("submit", function(event) {

    // Impede que o formulário recarregue a página automaticamente.
    event.preventDefault();

    // Obtém os dados preenchidos nos campos do formulário.
    const nome = document.getElementById("nome").value;
    const descricao = document.getElementById("descricao").value;
    const categoria = document.getElementById("categoria").value;
    const status = document.getElementById("status").value;

    // Organiza os dados em um objeto JavaScript chamado projeto.
    const projeto = {
        nome: nome,
        descricao: descricao,
        categoria: categoria,
        status: status
    };

    // Envia os dados do projeto para a API Java realizar o cadastro.
    fetch(API, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },

        // Converte o objeto projeto para JSON antes de enviá-lo.
        body: JSON.stringify(projeto)
    })
    .then(response => {

        // Verifica se o servidor retornou algum erro.
        if (!response.ok) {
            throw new Error("Erro ao cadastrar projeto.");
        }

        return response.json();
    })

    .then(() => {

        // Informa que o projeto foi cadastrado.
        alert("Projeto cadastrado com sucesso!");

        // Limpa os campos do formulário.
        formulario.reset();

        // Atualiza a tabela com os projetos cadastrados.
        listarProjetos();
    })

    .catch(erro => {

        // Mostra o erro no console.
        console.error(erro);

        // Informa que não foi possível realizar o cadastro.
        alert("Não foi possível cadastrar o projeto.");
    });

});

/* Busca os projetos cadastrados na API Java. */
function listarProjetos() {

    fetch(API, {
        method: "GET"
    })
    .then(response => {

        // Verifica se ocorreu algum erro na consulta.
        if (!response.ok) {
            throw new Error("Erro ao listar projetos.");
        }

        // Converte a resposta JSON para JavaScript.
        return response.json();
    })

    .then(projetos => {

        // Busca a tabela de projetos no HTML.
        const tabela = document.getElementById("tabelaProjetos");

        // Limpa os registros anteriores da tabela.
        tabela.innerHTML = "";

        // Percorre todos os projetos recebidos da API.
        projetos.forEach(projeto => {

            // Adiciona uma linha na tabela para cada projeto.
            tabela.innerHTML += `
                <tr>
                    <td>${projeto.id}</td>
                    <td>${projeto.nome}</td>
                    <td>${projeto.categoria}</td>
                    <td>${projeto.status}</td>
                </tr>
            `;
        });
    })

    .catch(erro => {

        // Mostra o erro no console.
        console.error(erro);

    });

}

/* Carrega os projetos automaticamente quando a página é aberta. */
listarProjetos();