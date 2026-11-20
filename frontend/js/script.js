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


/* NAVEGAÇÃO ENTRE AS TELAS */

const telaDashboard = document.getElementById("telaDashboard");
const telaProjetos = document.getElementById("telaProjetos");

const linkDashboard = document.getElementById("linkDashboard");
const linkProjetos = document.getElementById("linkProjetos");

function mostrarTela(telaSelecionada) {

    // Mostra a tela selecionada e esconde a outra.
    telaDashboard.classList.toggle("ativa", telaSelecionada === "dashboard");
    telaProjetos.classList.toggle("ativa", telaSelecionada === "projetos");

    // Destaca no menu o link da tela que está aberta.
    linkDashboard.classList.toggle("ativo", telaSelecionada === "dashboard");
    linkProjetos.classList.toggle("ativo", telaSelecionada === "projetos");
}

linkDashboard.addEventListener("click", function(evento) {

    evento.preventDefault();

    mostrarTela("dashboard");

});

linkProjetos.addEventListener("click", function(evento) {

    evento.preventDefault();

    mostrarTela("projetos");

});


/* ARMAZENAMENTO DOS PROJETOS */

let projetosCarregados = [];

let projetoEmEdicao = null;


/* GRÁFICO DE STATUS */

let graficoStatus = null;

let graficoCategoria = null;

let graficoMensal = null;


function atualizarGraficoStatus(concluidos, emAndamento, planejados) {

    const elementoGraficoStatus = document.getElementById("graficoStatus");

    const contextoGrafico = elementoGraficoStatus.getContext("2d");

    const dados = [concluidos, emAndamento, planejados];

    if (graficoStatus === null) {

        graficoStatus = new Chart(contextoGrafico, {

            type: "doughnut",

            data: {
                labels: ["Concluídos", "Em desenvolvimento", "Planejados"],

                datasets: [{
                    data: dados,

                    backgroundColor: [
                        "#437C1E", // Concluídos
                        "#009999", // Em desenvolvimento
                        "#EF626C"  // Planejados
                    ],

                    borderWidth: 0
                }]
            },

            options: {
                responsive: true,
                maintainAspectRatio: false,
                devicePixelRatio: 3
            }

        });

    } else {

        graficoStatus.data.datasets[0].data = dados;

        graficoStatus.update();

    }
}



/* GRÁFICO DE PROJETOS POR CATEGORIA */

function atualizarGraficoCategoria(web, software, mobile, outro) {

    const elementoGraficoCategoria = document.getElementById("graficoCategoria");

    const contextoGraficoCategoria = elementoGraficoCategoria.getContext("2d");

    const dados = [web, software, mobile, outro];

    if (graficoCategoria === null) {

        graficoCategoria = new Chart(contextoGraficoCategoria, {

            type: "bar",

            data: {
                labels: ["Web", "Software", "Mobile", "Outro"],

                datasets: [{
                    label: "Quantidade de projetos",
                    data: dados,

                    backgroundColor: [
                        "#009999", // Web
                        "#437C1E", // Software
                        "#EF626C", // Mobile
                        "#EFF6EE"  // Outro
                    ],

                    borderWidth: 0
                }]
            },

            options: {
                responsive: true,
                maintainAspectRatio: false,
                devicePixelRatio: 3,

                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }

        });

    } else {

        graficoCategoria.data.datasets[0].data = dados;

        graficoCategoria.update();

    }
}



/* GRÁFICO DE EVOLUÇÃO MENSAL */

function atualizarGraficoMensal(projetos) {

    const elementoGraficoMensal = document.getElementById("graficoMensal");

    const contextoGraficoMensal = elementoGraficoMensal.getContext("2d");

    const contagemPorMes = {};

    projetos.forEach(projeto => {

        const ano = projeto.dataCriacao[0];
        const mes = projeto.dataCriacao[1];

        const chave = `${ano}-${String(mes).padStart(2, "0")}`;

        if (contagemPorMes[chave] === undefined) {
            contagemPorMes[chave] = 0;
        }

        contagemPorMes[chave]++;

    });

    const meses = Object.keys(contagemPorMes).sort();

    const nomesMeses = [
        "Janeiro", "Fevereiro", "Março", "Abril",
        "Maio", "Junho", "Julho", "Agosto",
        "Setembro", "Outubro", "Novembro", "Dezembro"
    ];

    const labels = meses.map(chave => {

        const [ano, mes] = chave.split("-");

        return `${nomesMeses[Number(mes) - 1]} ${ano}`;

    });

    const dados = meses.map(mes => contagemPorMes[mes]);

    if (graficoMensal === null) {

        graficoMensal = new Chart(contextoGraficoMensal, {

            type: "line",

            data: {
                labels: labels,

                datasets: [{
                    label: "Projetos criados",
                    data: dados,
                    borderColor: "#009999",
                    backgroundColor: "rgba(0, 153, 153, 0.15)",
                    pointBackgroundColor: "#EF626C",
                    pointBorderColor: "#EFF6EE",
                }]
            },

            options: {
                responsive: true,
                maintainAspectRatio: false,
                devicePixelRatio: 3,

                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }

        });

    } else {

        graficoMensal.data.labels = labels;
        graficoMensal.data.datasets[0].data = dados;

        graficoMensal.update();

    }
}



/* LISTAGEM DOS PROJETOS */

function listarProjetos() {

    const mensagem = document.getElementById("mensagem");

    mensagem.textContent = "Carregando projetos...";

    fetch(API_URL, {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Erro ao consultar a API.");
        }

        return response.json();

    })
    .then(projetos => {

        projetosCarregados = projetos;

        atualizarEstatisticas(projetos);

        filtrarProjetos();

        mensagem.textContent = "Projetos carregados com sucesso.";

    })
    .catch(erro => {

        mensagem.textContent = "Não foi possível carregar os projetos.";

        console.error(erro);

    });
}


/* ESTATÍSTICAS DO DASHBOARD */

function atualizarEstatisticas(projetos) {

    let total = 0;
    let concluidos = 0;
    let emAndamento = 0;
    let planejados = 0;

    let web = 0;
    let software = 0;
    let mobile = 0;
    let outro = 0;

    projetos.forEach(projeto => {

        total++;

        if (projeto.status === "Concluído") {
            concluidos++;
        }

        if (projeto.status === "Em desenvolvimento") {
            emAndamento++;
        }

        if (projeto.status === "Planejado") {
            planejados++;
        }

        if (projeto.categoria === "Web") {
            web++;

        }

        if (projeto.categoria === "Software") {
            software++;
        }

        if (projeto.categoria === "Mobile") {
            mobile++;
        }

        if (projeto.categoria === "Outro") {
            outro++;
        }

    });

    const valorTotal = document.getElementById("valorTotal");
    const valorConcluidos = document.getElementById("valorConcluidos");
    const valorAndamento = document.getElementById("valorAndamento");
    const valorPlanejados = document.getElementById("valorPlanejados");

    valorTotal.textContent = total;
    valorConcluidos.textContent = concluidos;
    valorAndamento.textContent = emAndamento;
    valorPlanejados.textContent = planejados;

    atualizarGraficoStatus(concluidos, emAndamento, planejados);
    atualizarGraficoCategoria(web, software, mobile, outro);
    atualizarGraficoMensal(projetos);
}


/* PESQUISA DE PROJETOS */

function filtrarProjetos() {

    const campoBusca = document.getElementById("campoBusca");

    const termoBusca = campoBusca.value.toLowerCase();

    const projetosFiltrados = [];

    projetosCarregados.forEach(projeto => {

        const nome = projeto.nome.toLowerCase();
        const categoria = projeto.categoria.toLowerCase();
        const status = projeto.status.toLowerCase();

        if (
            nome.includes(termoBusca) ||
            categoria.includes(termoBusca) ||
            status.includes(termoBusca)
        ) {

            projetosFiltrados.push(projeto);

        }

    });

    exibirProjetos(projetosFiltrados);
}


/* FORMATAÇÃO DAS DATAS */

function formatarData(data) {

    const [ano, mes, dia] = data;

    return `${String(dia).padStart(2, "0")}/${String(mes).padStart(2, "0")}/${ano}`;
}


/* EXIBIÇÃO DOS PROJETOS NA TABELA */

function exibirProjetos(projetos) {

    const listaProjetos = document.getElementById("listaProjetos");

    listaProjetos.replaceChildren();

    if (projetos.length === 0) {

        const linha = document.createElement("tr");

        const coluna = document.createElement("td");

        coluna.colSpan = 7;

        if (projetosCarregados.length === 0) {

            coluna.textContent = "Nenhum projeto cadastrado.";

        } else {

            coluna.textContent = "Nenhum projeto encontrado.";

        }

        linha.appendChild(coluna);

        listaProjetos.appendChild(linha);

        return;
    }

    projetos.forEach(projeto => {

        const linha = document.createElement("tr");

        const colunaId = document.createElement("td");
        colunaId.textContent = projeto.id;

        const colunaNome = document.createElement("td");
        colunaNome.textContent = projeto.nome;

        const colunaCategoria = document.createElement("td");
        colunaCategoria.textContent = projeto.categoria;

        const colunaStatus = document.createElement("td");

        const etiquetaStatus = document.createElement("span");

        etiquetaStatus.textContent = projeto.status;

        etiquetaStatus.classList.add("status-badge");

        if (projeto.status === "Concluído") {
            etiquetaStatus.classList.add("status-concluido");
        }

        if (projeto.status === "Em desenvolvimento") {
            etiquetaStatus.classList.add("status-andamento");
        }

        if (projeto.status === "Planejado") {
            etiquetaStatus.classList.add("status-planejado");
        }

        colunaStatus.appendChild(etiquetaStatus);

        const colunaDataCriacao = document.createElement("td");
        colunaDataCriacao.textContent = formatarData(projeto.dataCriacao);

        const colunaPrazo = document.createElement("td");
        colunaPrazo.textContent = formatarData(projeto.prazo);

        const colunaAcoes = document.createElement("td");

        const botaoEditar = document.createElement("button");
        botaoEditar.classList.add("btn", "btn-primary", "btn-sm");

        const iconeEditar = document.createElement("img");
        iconeEditar.src = "img/icone-editar.png";
        iconeEditar.alt = "Editar";
        iconeEditar.classList.add("icone-acao");

        botaoEditar.appendChild(iconeEditar);

        // Quando o mouse entra no botão
        botaoEditar.addEventListener("mouseenter", function() {
            iconeEditar.src = "img/icone-editar-hover2.png";
        });

        // Quando o mouse sai do botão
        botaoEditar.addEventListener("mouseleave", function() {
            iconeEditar.src = "img/icone-editar.png";
        });

        const botaoExcluir = document.createElement("button");
        botaoExcluir.classList.add("btn", "btn-danger", "btn-sm");

        const iconeExcluir = document.createElement("img");
        iconeExcluir.src = "img/icone-excluir.png";
        iconeExcluir.alt = "Excluir";
        iconeExcluir.classList.add("icone-acao");

        botaoExcluir.appendChild(iconeExcluir);

        // Quando o mouse entra no botão
        botaoExcluir.addEventListener("mouseenter", function() {
            iconeExcluir.src = "img/icone-excluir-hover1.png";
        });

        // Quando o mouse sai do botão
        botaoExcluir.addEventListener("mouseleave", function() {
            iconeExcluir.src = "img/icone-excluir.png";
        });

        botaoEditar.title = "Editar projeto";
        botaoExcluir.title = "Excluir projeto";

        botaoEditar.classList.add("botao-editar");
        botaoExcluir.classList.add("botao-excluir");

        botaoEditar.addEventListener("click", function() {

            editarProjeto(projeto.id);

        });

        botaoExcluir.addEventListener("click", function() {

            excluirProjeto(projeto.id);

        });

        const containerAcoes = document.createElement("div");
        containerAcoes.style.display = "flex";
        containerAcoes.style.justifyContent = "center";
        containerAcoes.style.gap = "8px";

        containerAcoes.appendChild(botaoEditar);
        containerAcoes.appendChild(botaoExcluir);

        colunaAcoes.appendChild(containerAcoes);

        linha.appendChild(colunaId);
        linha.appendChild(colunaNome);
        linha.appendChild(colunaCategoria);
        linha.appendChild(colunaStatus);
        linha.appendChild(colunaDataCriacao);
        linha.appendChild(colunaPrazo);
        linha.appendChild(colunaAcoes);

        listaProjetos.appendChild(linha);

    });
}


/* CADASTRO DE PROJETOS */

document.getElementById("formProjeto").addEventListener("submit", function(evento) {

    evento.preventDefault();

    const projeto = {
        nome: document.getElementById("nome").value,
        descricao: document.getElementById("descricao").value,
        categoria: document.getElementById("categoria").value,
        status: document.getElementById("status").value,
        prazo: document.getElementById("prazo").value
    };

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(projeto)
    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Erro ao cadastrar o projeto.");
        }

        return response;

    })
    .then(() => {

        alert("Projeto cadastrado com sucesso!");

        document.getElementById("formProjeto").reset();

        listarProjetos();

    })
    .catch(erro => {

        alert("Não foi possível cadastrar o projeto.");

        console.error(erro);

    });
});


/* EXCLUSÃO DE PROJETOS */

function excluirProjeto(id) {

    const confirmar = confirm("Deseja realmente excluir este projeto?");

    if (!confirmar) {
        return;
    }

    fetch(`${API_URL}/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Erro ao excluir o projeto.");
        }

        alert("Projeto excluído com sucesso!");

        listarProjetos();

    })
    .catch(erro => {

        alert("Não foi possível excluir o projeto.");

        console.error(erro);

    });
}


/* EDIÇÃO DE PROJETOS */

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


/* INICIALIZAÇÃO */

listarProjetos();

document.getElementById("campoBusca").addEventListener("input", filtrarProjetos);

document.getElementById("botaoCancelarEdicao").addEventListener("click", function() {

    document.getElementById("popupEdicao").style.display = "none";

});


/* SALVAR ALTERAÇÕES */

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