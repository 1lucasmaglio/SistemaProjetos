console.log("PROJETOS.JS INICIADO");

const API_URL = "http://localhost:7070/api/projetos";

const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "/frontend/login.html";
}

document.getElementById("botaoSair").addEventListener("click", function() {

    localStorage.removeItem("token");

    window.location.href = "/frontend/login.html";

});

/* Armazena os projetos recebidos da API para utilizá-los na pesquisa e nas estatísticas. */
let projetosCarregados = [];

let projetoEmEdicao = null;

/* Busca os projetos cadastrados e atualiza a tabela e as estatísticas do dashboard. */
function listarProjetos() {

    // Localiza o elemento responsável por exibir mensagens na página.
    const mensagem = document.getElementById("mensagem");

    // Informa ao usuário que os projetos estão sendo carregados.
    mensagem.textContent = "Carregando projetos...";

    // Solicita à API Java a lista de projetos cadastrados.
    fetch(API_URL, {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {
        // Verifica se a consulta foi realizada com sucesso.
        if (!response.ok) {
            throw new Error("Erro ao consultar a API.");
        }

        // Converte a resposta JSON em dados JavaScript.
        return response.json();
    })
    .then(projetos => {
        // Guarda os projetos recebidos para utilizá-los em outras funções.
        projetosCarregados = projetos;

        // Atualiza os números apresentados nos cartões do dashboard.
        atualizarEstatisticas(projetos);

        // Exibe os projetos na tabela, considerando o texto da pesquisa.
        filtrarProjetos();

        // Informa que os projetos foram carregados.
        mensagem.textContent = "Projetos carregados com sucesso.";
    })
    .catch(erro => {
        // Informa que ocorreu um problema durante a consulta.
        mensagem.textContent = "Não foi possível carregar os projetos.";

        // Registra o erro no console para auxiliar nos testes.
        console.error(erro);
    });
}

/* Calcula e atualiza a quantidade de projetos em cada situação. */
function atualizarEstatisticas(projetos) {

    // Inicializa os contadores das estatísticas.
    let total = 0;
    let concluidos = 0;
    let emAndamento = 0;
    let planejados = 0;

    // Percorre todos os projetos recebidos da API.
    projetos.forEach(projeto => {

        // Aumenta a quantidade total de projetos.
        total++;

        // Verifica se o projeto está concluído.
        if (projeto.status === "Concluído") {
            concluidos++;
        }

        // Verifica se o projeto está em andamento.
        if (projeto.status === "Em andamento") {
            emAndamento++;
        }

        // Verifica se o projeto está planejado.
        if (projeto.status === "Planejado") {
            planejados++;
        }

    });

    // Localiza os elementos HTML responsáveis por mostrar as estatísticas.
    const valorTotal = document.getElementById("valorTotal");
    const valorConcluidos = document.getElementById("valorConcluidos");
    const valorAndamento = document.getElementById("valorAndamento");
    const valorPlanejados = document.getElementById("valorPlanejados");

    // Atualiza os valores apresentados no dashboard.
    valorTotal.textContent = total;
    valorConcluidos.textContent = concluidos;
    valorAndamento.textContent = emAndamento;
    valorPlanejados.textContent = planejados;
}

/* Filtra os projetos de acordo com o nome, a categoria ou o status pesquisado. */
function filtrarProjetos() {

    // Recupera o texto digitado no campo de pesquisa.
    const campoBusca = document.getElementById("campoBusca");

    // Converte o texto para letras minúsculas para facilitar a comparação.
    const termoBusca = campoBusca.value.toLowerCase();

    // Cria uma lista que receberá apenas os projetos correspondentes à pesquisa.
    const projetosFiltrados = [];

    // Percorre todos os projetos armazenados.
    projetosCarregados.forEach(projeto => {

        // Converte os dados do projeto para letras minúsculas.
        const nome = projeto.nome.toLowerCase();
        const categoria = projeto.categoria.toLowerCase();
        const status = projeto.status.toLowerCase();

        // Verifica se algum dos campos contém o texto pesquisado.
        if (
            nome.includes(termoBusca) ||
            categoria.includes(termoBusca) ||
            status.includes(termoBusca)
        ) {
            // Adiciona o projeto à lista de resultados.
            projetosFiltrados.push(projeto);
        }

    });

    // Exibe somente os projetos encontrados.
    exibirProjetos(projetosFiltrados);
}

function formatarData(data) {
    const [ano, mes, dia] = data;

    return `${String(dia).padStart(2, "0")}/${String(mes).padStart(2, "0")}/${ano}`;
}


/* Exibe os projetos recebidos na tabela do sistema. */
function exibirProjetos(projetos) {

    // Localiza o corpo da tabela no HTML.
    const listaProjetos = document.getElementById("listaProjetos");

    // Remove as linhas exibidas anteriormente.
    listaProjetos.replaceChildren();

    // Verifica se existem projetos para exibir.
    if (projetos.length === 0) {

        // Cria uma nova linha para apresentar uma mensagem.
        const linha = document.createElement("tr");

        // Cria uma coluna para a mensagem.
        const coluna = document.createElement("td");

        // Faz a coluna ocupar o espaço das cinco colunas da tabela.
        coluna.colSpan = 7;

        // Verifica se não existem projetos cadastrados.
        if (projetosCarregados.length === 0) {

            coluna.textContent = "Nenhum projeto cadastrado.";

        } else {

            // Informa que a pesquisa não encontrou resultados.
            coluna.textContent = "Nenhum projeto encontrado.";

        }

        // Adiciona a coluna à linha.
        linha.appendChild(coluna);

        // Adiciona a linha à tabela.
        listaProjetos.appendChild(linha);

        // Encerra a função porque não existem projetos para exibir.
        return;
    }

    // Percorre os projetos que devem aparecer na tabela.
    projetos.forEach(projeto => {

        // Cria uma nova linha para o projeto.
        const linha = document.createElement("tr");

        // Cria a coluna responsável por mostrar o ID.
        const colunaId = document.createElement("td");

        // Define o ID do projeto como conteúdo da coluna.
        colunaId.textContent = projeto.id;

        // Cria a coluna responsável por mostrar o nome.
        const colunaNome = document.createElement("td");

        // Define o nome do projeto como conteúdo da coluna.
        colunaNome.textContent = projeto.nome;

        // Cria a coluna responsável por mostrar a categoria.
        const colunaCategoria = document.createElement("td");

        // Define a categoria do projeto como conteúdo da coluna.
        colunaCategoria.textContent = projeto.categoria;

        // Cria a coluna responsável por mostrar o status.
        const colunaStatus = document.createElement("td");

        // Define o status do projeto como conteúdo da coluna.
        colunaStatus.textContent = projeto.status;

        const colunaDataCriacao = document.createElement("td");

        colunaDataCriacao.textContent = formatarData(projeto.dataCriacao);

        const colunaPrazo = document.createElement("td");

        colunaPrazo.textContent = formatarData(projeto.prazo);

        // Cria a coluna responsável pelas ações.
        const colunaAcoes = document.createElement("td");

        // Cria o botão de edição.
        const botaoEditar = document.createElement("button");

        botaoEditar.textContent = "✎";

        // Cria o botão de exclusão.
        const botaoExcluir = document.createElement("button");

        botaoExcluir.textContent = "✕";

        botaoEditar.title = "Editar projeto";
        botaoExcluir.title = "Excluir projeto";

        botaoEditar.classList.add("botao-editar");
        botaoExcluir.classList.add("botao-excluir");

        // Define o que acontece quando o botão de edição é clicado.
        botaoEditar.addEventListener("click", function() {

            editarProjeto(projeto.id);

        });

        // Define o que acontece quando o botão de exclusão é clicado.
        botaoExcluir.addEventListener("click", function() {

            excluirProjeto(projeto.id);

        });

        // Adiciona os botões à coluna de ações.
        colunaAcoes.appendChild(botaoEditar);
        colunaAcoes.appendChild(botaoExcluir);

        // Adiciona todas as colunas à linha.
        linha.appendChild(colunaId);
        linha.appendChild(colunaNome);
        linha.appendChild(colunaCategoria);
        linha.appendChild(colunaStatus);
        linha.appendChild(colunaDataCriacao);
        linha.appendChild(colunaPrazo);
        linha.appendChild(colunaAcoes);

        // Adiciona a linha completa ao corpo da tabela.
        listaProjetos.appendChild(linha);

    });
}

/* Executa o cadastro de um projeto quando o formulário é enviado. */
document.getElementById("formProjeto").addEventListener("submit", function(evento) {

    // Impede que o formulário recarregue a página automaticamente.
    evento.preventDefault();

    // Organiza os dados digitados pelo usuário em um objeto.
    const projeto = {
        nome: document.getElementById("nome").value,
        descricao: document.getElementById("descricao").value,
        categoria: document.getElementById("categoria").value,
        status: document.getElementById("status").value,
        prazo: document.getElementById("prazo").value
    };

    // Envia o projeto para a API Java.
    fetch(API_URL, {
        method: "POST",
        headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
        },
        // Converte o objeto projeto para JSON antes do envio.
        body: JSON.stringify(projeto)
    })
    .then(response => {
        // Verifica se o cadastro foi realizado com sucesso.
        if (!response.ok) {
            throw new Error("Erro ao cadastrar o projeto.");
        }

        // Retorna a resposta da API.
        return response;
    })
    .then(() => {
        // Informa ao usuário que o cadastro foi realizado.
        alert("Projeto cadastrado com sucesso!");

        // Limpa os campos do formulário.
        document.getElementById("formProjeto").reset();

        // Atualiza a tabela e as estatísticas.
        listarProjetos();
    })
    .catch(erro => {
        // Informa que ocorreu um problema durante o cadastro.
        alert("Não foi possível cadastrar o projeto.");

        // Registra o erro no console.
        console.error(erro);
    });
});

/* Exclui um projeto de acordo com seu ID. */
function excluirProjeto(id) {

    // Solicita a confirmação do usuário antes da exclusão.
    const confirmar = confirm("Deseja realmente excluir este projeto?");

    // Encerra a função caso o usuário cancele a operação.
    if (!confirmar) {
        return;
    }

    // Envia uma solicitação DELETE para a API Java.
    fetch(`${API_URL}/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {
        // Verifica se a exclusão foi realizada com sucesso.
        if (!response.ok) {
            throw new Error("Erro ao excluir o projeto.");
        }

        // Informa que o projeto foi excluído.
        alert("Projeto excluído com sucesso!");

        // Atualiza a tabela e as estatísticas.
        listarProjetos();
    })
    .catch(erro => {
        // Informa que ocorreu um problema durante a exclusão.
        alert("Não foi possível excluir o projeto.");

        // Registra o erro no console.
        console.error(erro);
    });
}

function editarProjeto(id) {

    const projeto = projetosCarregados.find(projeto => projeto.id === id);

    projetoEmEdicao = projeto.id;

    const popup = document.getElementById("popupEdicao");

    document.getElementById("editarNome").value = projeto.nome;
    document.getElementById("editarDescricao").value = projeto.descricao;
    document.getElementById("editarCategoria").value = projeto.categoria;
    document.getElementById("editarStatus").value = projeto.status;
    
    const prazo = projeto.prazo;

    document.getElementById("editarPrazo").value =
    `${prazo[0]}-${String(prazo[1]).padStart(2, "0")}-${String(prazo[2]).padStart(2, "0")}`;

    popup.style.display = "flex";
}

// Carrega os projetos quando a página é aberta.
listarProjetos();

// Atualiza os resultados sempre que o usuário digita no campo de pesquisa.
document.getElementById("campoBusca").addEventListener("input", filtrarProjetos);

document.getElementById("botaoCancelarEdicao").addEventListener("click", function() {

    document.getElementById("popupEdicao").style.display = "none";

});

document.getElementById("formEdicao").addEventListener("submit", function(evento) {
    evento.preventDefault();

    const projetoAtualizado = {
        nome: document.getElementById("editarNome").value,
        descricao: document.getElementById("editarDescricao").value,
        categoria: document.getElementById("editarCategoria").value,
        status: document.getElementById("editarStatus").value,
        prazo: document.getElementById("editarPrazo").value
    };

    fetch(`${API_URL}/${projetoEmEdicao}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(projetoAtualizado)
    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Erro ao atualizar o projeto.");
        }

    })
    .then(() => {

        alert("Projeto atualizado com sucesso!");

        document.getElementById("popupEdicao").style.display = "none";

        listarProjetos();

    })
    .catch(erro => {

        alert("Não foi possível atualizar o projeto.");

        console.error(erro);

    });

});