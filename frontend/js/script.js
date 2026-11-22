
console.log("PROJETOS.JS INICIADO");

const API_URL = "http://localhost:7070/api/projetos";

const token = localStorage.getItem("token");

let idiomaAtual = localStorage.getItem("idioma") || "pt-BR";

/* TRATAMENTO DAS RESPOSTAS DA API */

function processarResposta(response, mensagemPadrao) {

    if (!response.ok) {

        return response.text().then(corpo => {

            let mensagem = mensagemPadrao;

            if (corpo) {

                try {
                    const dados = JSON.parse(corpo);

                    if (dados.erro) {
                        mensagem = dados.erro;
                    }

                } catch (erro) {
                    // Mantém a mensagem padrão se o corpo não for JSON.
                }

            }

            throw new Error(mensagem);

        });

    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}


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
const telaConfiguracoes = document.getElementById("telaConfiguracoes");

const linkDashboard = document.getElementById("linkDashboard");
const linkProjetos = document.getElementById("linkProjetos");
const linkConfiguracoes = document.getElementById("linkConfiguracoes");

function mostrarTela(telaSelecionada) {

    // Mostra a tela selecionada e esconde as outras.
    telaDashboard.classList.toggle("ativa", telaSelecionada === "dashboard");
    telaProjetos.classList.toggle("ativa", telaSelecionada === "projetos");
    telaConfiguracoes.classList.toggle("ativa", telaSelecionada === "configuracoes");

    // Destaca no menu o link da tela que está aberta.
    linkDashboard.classList.toggle("ativo", telaSelecionada === "dashboard");
    linkProjetos.classList.toggle("ativo", telaSelecionada === "projetos");
    linkConfiguracoes.classList.toggle("ativo", telaSelecionada === "configuracoes");
}

linkDashboard.addEventListener("click", function(evento) {

    evento.preventDefault();

    mostrarTela("dashboard");

});

linkProjetos.addEventListener("click", function(evento) {

    evento.preventDefault();

    mostrarTela("projetos");

});

linkConfiguracoes.addEventListener("click", function(evento) {

    evento.preventDefault();

    mostrarTela("configuracoes");

});


/* ARMAZENAMENTO DOS PROJETOS */

let projetosCarregados = [];

let paginaAtual = 1;
const projetosPorPagina = 5;

let projetoEmEdicao = null;


/* GRÁFICOS */

let graficoStatus = null;
let graficoCategoria = null;
let graficoMensal = null;


/* GRÁFICO DE STATUS */

function atualizarGraficoStatus(concluidos, emDesenvolvimento, planejados) {

    const elementoGraficoStatus = document.getElementById("graficoStatus");

    const contextoGrafico = elementoGraficoStatus.getContext("2d");

    const dados = [concluidos, emDesenvolvimento, planejados];

    if (graficoStatus === null) {

        graficoStatus = new Chart(contextoGrafico, {

            type: "doughnut",

            data: {
                labels: idiomaAtual === "en"
                    ? ["Completed", "In Progress", "Planned"]
                    : ["Concluídos", "Em desenvolvimento", "Planejados"],

                datasets: [{
                    data: dados,

                    backgroundColor: [
                        "#437C1E",
                        "#009999",
                        "#EF626C"
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

        graficoStatus.data.labels = idiomaAtual === "en"
            ? ["Completed", "In Progress", "Planned"]
            : ["Concluídos", "Em desenvolvimento", "Planejados"];

        graficoStatus.data.datasets[0].data = dados;

        graficoStatus.update();

    }
}


/* GRÁFICO DE PROJETOS POR CATEGORIA */

function atualizarGraficoCategoria(desktop, mobile, software, web) {

    const elementoGraficoCategoria = document.getElementById("graficoCategoria");

    const contextoGraficoCategoria = elementoGraficoCategoria.getContext("2d");

    const dados = [desktop, mobile, software, web];

    if (graficoCategoria === null) {

        graficoCategoria = new Chart(contextoGraficoCategoria, {

            type: "bar",

            data: {
                labels: [
                    "Desktop",
                    "Mobile",
                    "Software",
                    "Web"
                ],

                datasets: [{
                    label: traducoes[idiomaAtual].quantidadeProjetos,
                    data: dados,

                    backgroundColor: [
                        "#EFF6EE",
                        "#EF626C",
                        "#437C1E",
                        "#009999"
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

        graficoCategoria.data.labels = [
            "Desktop",
            "Mobile",
            "Software",
            "Web"
        ];

        graficoCategoria.data.datasets[0].label =
            traducoes[idiomaAtual].quantidadeProjetos;

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

    const nomesMeses = idiomaAtual === "en"
        ? [
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        ]
        : [
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
                    label: traducoes[idiomaAtual].projetosCriados,
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

        graficoMensal.data.datasets[0].label =
            traducoes[idiomaAtual].projetosCriados;

        graficoMensal.data.datasets[0].data = dados;

        graficoMensal.update();

    }
}


/* LISTAGEM DOS PROJETOS */

function listarProjetos() {

    const mensagem = document.getElementById("mensagem");

    mensagem.textContent = traducoes[idiomaAtual].carregando;

    fetch(API_URL, {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => {
        return processarResposta(response, "Erro ao consultar a API.");
    })
    .then(projetos => {

        projetosCarregados = projetos;

        atualizarEstatisticas(projetos);

        filtrarProjetos();

        mensagem.textContent = traducoes[idiomaAtual].carregado;

    })
    .catch(erro => {

        mensagem.textContent = erro.message;

        console.error(erro);

    });
}


/* ESTATÍSTICAS DO DASHBOARD */

function atualizarEstatisticas(projetos) {

    let total = 0;
    let concluidos = 0;
    let emDesenvolvimento = 0;
    let planejados = 0;

    let web = 0;
    let software = 0;
    let mobile = 0;
    let desktop = 0;

    projetos.forEach(projeto => {

        total++;

        if (projeto.status === "Concluído") {
            concluidos++;
        }

        if (projeto.status === "Em desenvolvimento") {
            emDesenvolvimento++;
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

        if (projeto.categoria === "Desktop") {
            desktop++;
        }

    });

    const valorTotal = document.getElementById("valorTotal");
    const valorConcluidos = document.getElementById("valorConcluidos");
    const valorEmDesenvolvimento = document.getElementById("valorEmDesenvolvimento");
    const valorPlanejados = document.getElementById("valorPlanejados");

    valorTotal.textContent = total;
    valorConcluidos.textContent = concluidos;
    valorEmDesenvolvimento.textContent = emDesenvolvimento;
    valorPlanejados.textContent = planejados;

    atualizarGraficoStatus(concluidos, emDesenvolvimento, planejados);
    atualizarGraficoCategoria(desktop, mobile, software, web);
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

    projetosFiltradosAtuais = projetosFiltrados;

    paginaAtual = 1;

    exibirProjetos(projetosFiltradosAtuais);
}


/* FORMATAÇÃO DAS DATAS */

function formatarData(data) {

    const [ano, mes, dia] = data;

    return `${String(dia).padStart(2, "0")}/${String(mes).padStart(2, "0")}/${ano}`;
}


/* PAGINAÇÃO */

function atualizarPaginacao(totalProjetos) {

    const paginacao = document.getElementById("paginacaoProjetos");

    paginacao.replaceChildren();

    const totalPaginas = Math.ceil(totalProjetos / projetosPorPagina);

    if (totalPaginas <= 1) {
        return;
    }

    // Botão de página anterior
    const botaoAnterior = document.createElement("button");
    botaoAnterior.type = "button";
    botaoAnterior.textContent = "‹";
    botaoAnterior.classList.add("pagina-btn");

    botaoAnterior.disabled = paginaAtual === 1;

    botaoAnterior.addEventListener("click", function() {
        mudarPagina(paginaAtual - 1);
    });

    paginacao.appendChild(botaoAnterior);

    // Botões numerados
    for (let i = 1; i <= totalPaginas; i++) {

        const botaoPagina = document.createElement("button");
        botaoPagina.type = "button";
        botaoPagina.textContent = i;
        botaoPagina.classList.add("pagina-btn");

        if (i === paginaAtual) {
            botaoPagina.classList.add("pagina-ativa");
        }

        botaoPagina.addEventListener("click", function() {
            mudarPagina(i);
        });

        paginacao.appendChild(botaoPagina);
    }

    // Botão de próxima página
    const botaoProximo = document.createElement("button");
    botaoProximo.type = "button";
    botaoProximo.textContent = "›";
    botaoProximo.classList.add("pagina-btn");

    botaoProximo.disabled = paginaAtual === totalPaginas;

    botaoProximo.addEventListener("click", function() {
        mudarPagina(paginaAtual + 1);
    });

    paginacao.appendChild(botaoProximo);
}


function mudarPagina(novaPagina) {

    // Saves the current vertical scroll position.
    const posicaoScroll = window.scrollY;

    // Updates the current page.
    paginaAtual = novaPagina;

    // Updates the projects table and pagination buttons.
    exibirProjetos(projetosFiltradosAtuais);

    // Restores the previous scroll position.
    window.scrollTo(0, posicaoScroll);
}


/* EXIBIÇÃO DOS PROJETOS NA TABELA */

function exibirProjetos(projetos) {

    const listaProjetos = document.getElementById("listaProjetos");

    listaProjetos.replaceChildren();

    atualizarPaginacao(projetos.length);

    if (projetos.length === 0) {

        const linha = document.createElement("tr");

        const coluna = document.createElement("td");

        coluna.colSpan = 7;

        if (projetosCarregados.length === 0) {

            coluna.textContent = traducoes[idiomaAtual].nenhumCadastrado;

        } else {

            coluna.textContent = traducoes[idiomaAtual].nenhumEncontrado;

        }

        linha.appendChild(coluna);

        listaProjetos.appendChild(linha);

        return;
    }

    const inicio = (paginaAtual - 1) * projetosPorPagina;
    const fim = inicio + projetosPorPagina;

    const projetosDaPagina = projetos.slice(inicio, fim);

    projetosDaPagina.forEach(projeto => {

        const linha = document.createElement("tr");

        const colunaId = document.createElement("td");
        colunaId.textContent = projeto.id;

        const colunaNome = document.createElement("td");
        colunaNome.textContent = projeto.nome;

        const colunaCategoria = document.createElement("td");
        colunaCategoria.textContent = projeto.categoria;

        const colunaStatus = document.createElement("td");

        const etiquetaStatus = document.createElement("span");

        etiquetaStatus.textContent = traduzirStatus(projeto.status);

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
        iconeEditar.alt = idiomaAtual === "en" ? "Edit" : "Editar";
        iconeEditar.classList.add("icone-acao");

        botaoEditar.appendChild(iconeEditar);

        botaoEditar.addEventListener("mouseenter", function() {
            iconeEditar.src = "img/icone-editar-hover2.png";
        });

        botaoEditar.addEventListener("mouseleave", function() {
            iconeEditar.src = "img/icone-editar.png";
        });

        const botaoExcluir = document.createElement("button");
        botaoExcluir.classList.add("btn", "btn-danger", "btn-sm");

        const iconeExcluir = document.createElement("img");
        iconeExcluir.src = "img/icone-excluir.png";
        iconeExcluir.alt = idiomaAtual === "en" ? "Delete" : "Excluir";
        iconeExcluir.classList.add("icone-acao");

        botaoExcluir.appendChild(iconeExcluir);

        botaoExcluir.addEventListener("mouseenter", function() {
            iconeExcluir.src = "img/icone-excluir-hover1.png";
        });

        botaoExcluir.addEventListener("mouseleave", function() {
            iconeExcluir.src = "img/icone-excluir.png";
        });

        botaoEditar.title = idiomaAtual === "en"
            ? "Edit project"
            : "Editar projeto";

        botaoExcluir.title = idiomaAtual === "en"
            ? "Delete project"
            : "Excluir projeto";

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
        return processarResposta(response, "Erro ao cadastrar o projeto.");
    })
    .then(() => {

        alert(idiomaAtual === "en"
            ? "Project registered successfully!"
            : "Projeto cadastrado com sucesso!");

        document.getElementById("formProjeto").reset();

        listarProjetos();

    })
    .catch(erro => {

        alert(erro.message);

        console.error(erro);

    });
});


/* EXCLUSÃO DE PROJETOS */

function excluirProjeto(id) {

    const confirmar = confirm(
        idiomaAtual === "en"
            ? "Are you sure you want to delete this project?"
            : "Deseja realmente excluir este projeto?"
    );

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
        return processarResposta(response, "Erro ao excluir o projeto.");
    })
    .then(() => {

        alert(
            idiomaAtual === "en"
                ? "Project deleted successfully!"
                : "Projeto excluído com sucesso!"
        );

        listarProjetos();

    })
    .catch(erro => {

        alert(erro.message);

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
        return processarResposta(response, "Erro ao atualizar o projeto.");
    })
    .then(() => {

        alert(
            idiomaAtual === "en"
                ? "Project updated successfully!"
                : "Projeto atualizado com sucesso!"
        );

        document.getElementById("popupEdicao").style.display = "none";

        listarProjetos();

    })
    .catch(erro => {

        alert(erro.message);

        console.error(erro);

    });

});


/* USUÁRIO */

const nomeUsuario = localStorage.getItem("nomeUsuario");

document.getElementById("nomeUsuario").textContent =
    nomeUsuario || "Usuário";


/* CONFIGURAÇÃO DE TEMA */

const seletorTema = document.getElementById("seletorTema");

function aplicarTema(tema) {

    document.body.classList.toggle("tema-claro", tema === "light");

    seletorTema.value = tema;

    localStorage.setItem("tema", tema);
}   

const temaSalvo = localStorage.getItem("tema") || "dark";

aplicarTema(temaSalvo);

seletorTema.addEventListener("change", function() {

    aplicarTema(seletorTema.value);

});


/* CONFIGURAÇÃO DE IDIOMA */

const seletorIdioma = document.getElementById("seletorIdioma");

const traducoes = {

    "pt-BR": {
        usuario: "Usuário",
        sair: "Sair",
        projetos: "Projetos",
        configuracoes: "Configurações",

        dashboard: "Dashboard",
        descricaoDashboard: "Acompanhe os indicadores e o desempenho dos seus projetos.",
        totalProjetos: "Total de Projetos",
        concluidos: "Concluídos",
        andamento: "Em Desenvolvimento",
        planejados: "Planejados",

        status: "Projetos por status",
        categoria: "Projetos por categoria",
        evolucao: "Evolução mensal de projetos",

        cadastro: "Cadastro de Projeto",
        nomeProjeto: "Nome do projeto",
        descricao: "Descrição",
        categoriaLabel: "Categoria",
        statusLabel: "Status",
        prazo: "Prazo",
        placeholderNome: "Digite o nome do projeto",
        placeholderDescricao: "Descreva o projeto",
        limpar: "Limpar",
        cadastrar: "Cadastrar projeto",

        lista: "Lista de Projetos",
        buscar: "Buscar projeto...",
        nome: "Nome",
        dataCriacao: "Data de criação",
        acoes: "Ações",

        nenhumCadastrado: "Nenhum projeto cadastrado.",
        nenhumEncontrado: "Nenhum projeto encontrado.",
        carregando: "Carregando projetos...",
        carregado: "Projetos carregados com sucesso.",

        aparencia: "Aparência",
        descricaoAparencia: "Personalize a aparência do sistema.",
        tema: "Tema",
        descricaoTema: "Escolha entre o modo claro e escuro.",
        escuro: "Escuro",
        claro: "Claro",

        idioma: "Idioma",
        descricaoIdioma: "Defina o idioma utilizado na interface.",
        idiomaSistema: "Idioma do sistema",
        selecionarIdioma: "Selecione seu idioma preferido.",

        editar: "Editar Projeto",
        cancelar: "Cancelar",
        salvar: "Salvar alterações",

        rodape: "Projeto acadêmico de Análise e Desenvolvimento de Sistemas",

        statusConcluido: "Concluído",
        statusAndamento: "Em desenvolvimento",
        statusPlanejado: "Planejado",

        quantidadeProjetos: "Quantidade de projetos",
        projetosCriados: "Projetos criados"
    },

    "en": {
        usuario: "User",
        sair: "Log out",
        projetos: "Projects",
        configuracoes: "Settings",

        dashboard: "Dashboard",
        descricaoDashboard: "Track your projects' indicators and performance.",
        totalProjetos: "Total Projects",
        concluidos: "Completed",
        andamento: "In Progress",
        planejados: "Planned",

        status: "Projects by status",
        categoria: "Projects by category",
        evolucao: "Monthly Project Progress",

        cadastro: "Project Registration",
        nomeProjeto: "Project name",
        descricao: "Description",
        categoriaLabel: "Category",
        statusLabel: "Status",
        prazo: "Deadline",
        placeholderNome: "Enter the project name",
        placeholderDescricao: "Describe the project",
        limpar: "Clear",
        cadastrar: "Register project",

        lista: "Project List",
        buscar: "Search projects...",
        nome: "Name",
        dataCriacao: "Creation date",
        acoes: "Actions",

        nenhumCadastrado: "No projects registered.",
        nenhumEncontrado: "No projects found.",
        carregando: "Loading projects...",
        carregado: "Projects loaded successfully.",

        aparencia: "Appearance",
        descricaoAparencia: "Customize the system's appearance.",
        tema: "Theme",
        descricaoTema: "Choose between light and dark mode.",
        escuro: "Dark",
        claro: "Light",

        idioma: "Language",
        descricaoIdioma: "Set the language used in the interface.",
        idiomaSistema: "System language",
        selecionarIdioma: "Choose your preferred language.",

        editar: "Edit Project",
        cancelar: "Cancel",
        salvar: "Save changes",

        rodape: "Academic project in Systems Analysis and Development",

        statusConcluido: "Completed",
        statusAndamento: "In Progress",
        statusPlanejado: "Planned",

        quantidadeProjetos: "Number of projects",
        projetosCriados: "Projects created"
    }

};


/* TRADUÇÃO DOS STATUS */

function traduzirStatus(status) {

    const textos = traducoes[idiomaAtual];

    if (status === "Concluído") {
        return textos.statusConcluido;
    }

    if (status === "Em desenvolvimento") {
        return textos.statusAndamento;
    }

    if (status === "Planejado") {
        return textos.statusPlanejado;
    }

    return status;
}


/* APLICAÇÃO DO IDIOMA */

function aplicarIdioma(idioma) {

    idiomaAtual = idioma;

    const t = traducoes[idioma];

    // Navegação
    document.getElementById("linkProjetos").textContent = t.projetos;
    document.getElementById("linkConfiguracoes").textContent = t.configuracoes;
    document.querySelector(".texto-usuario span:first-child").textContent = t.usuario;
    document.querySelector("#botaoSair span").textContent = t.sair;

    // Dashboard
    document.querySelector("#telaDashboard .introducao h1").textContent = t.dashboard;
    document.querySelector("#telaDashboard .introducao p").textContent = t.descricaoDashboard;

    const textosCards = document.querySelectorAll(".card-estatistica span");

    textosCards[0].textContent = t.totalProjetos;
    textosCards[1].textContent = t.concluidos;
    textosCards[2].textContent = t.andamento;
    textosCards[3].textContent = t.planejados;

    const titulosGraficos = document.querySelectorAll(".graficos-superiores .painel h2");

    titulosGraficos[0].textContent = t.status;
    titulosGraficos[1].textContent = t.categoria;

    document.querySelector("#telaDashboard > .painel h2").textContent = t.evolucao;

    // Cadastro
    document.querySelector("#cadastro h2").textContent = t.cadastro;

    document.querySelector('label[for="nome"]').textContent = t.nomeProjeto;
    document.querySelector('label[for="descricao"]').textContent = t.descricao;
    document.querySelector('label[for="categoria"]').textContent = t.categoriaLabel;
    document.querySelector('label[for="status"]').textContent = t.statusLabel;
    document.querySelector('label[for="prazo"]').textContent = t.prazo;

    document.getElementById("nome").placeholder = t.placeholderNome;
    document.getElementById("descricao").placeholder = t.placeholderDescricao;

    document.getElementById("botaoLimpar").textContent = t.limpar;
    document.getElementById("botaoCadastrar").textContent = t.cadastrar;

    // Lista
    document.querySelector("#projetos h2").textContent = t.lista;
    document.getElementById("campoBusca").placeholder = t.buscar;
    document.getElementById("campoBusca").setAttribute("aria-label", t.buscar);

    const cabecalhos = document.querySelectorAll("#projetos thead th");

    cabecalhos[1].textContent = t.nome;
    cabecalhos[2].textContent = t.categoriaLabel;
    cabecalhos[3].textContent = t.statusLabel;
    cabecalhos[4].textContent = t.dataCriacao;
    cabecalhos[5].textContent = t.prazo;
    cabecalhos[6].textContent = t.acoes;

    // Configurações
    document.querySelector("#telaConfiguracoes .introducao h1").textContent = t.configuracoes;

    document.querySelector("#telaConfiguracoes .introducao p").textContent =
        idioma === "en"
            ? "Customize the system's appearance and language."
            : "Personalize a aparência e o idioma do sistema.";

    const titulosConfiguracao = document.querySelectorAll(".configuracao-cabecalho h2");

    titulosConfiguracao[0].textContent = t.aparencia;
    titulosConfiguracao[1].textContent = t.idioma;

    const descricoesConfiguracao = document.querySelectorAll(".configuracao-cabecalho p");

    descricoesConfiguracao[0].textContent = t.descricaoAparencia;
    descricoesConfiguracao[1].textContent = t.descricaoIdioma;

    const textosConfiguracao = document.querySelectorAll(".configuracao-texto strong");

    textosConfiguracao[0].textContent = t.tema;
    textosConfiguracao[1].textContent = t.idiomaSistema;

    const descricoesOpcoes = document.querySelectorAll(".configuracao-texto span");

    descricoesOpcoes[0].textContent = t.descricaoTema;
    descricoesOpcoes[1].textContent = t.selecionarIdioma;

    // Opções de tema
    const opcoesTema = seletorTema.options;

    opcoesTema[0].textContent = t.escuro;
    opcoesTema[1].textContent = t.claro;

    // Opções de status

    const statusPortugues = ["Planejado", "Em desenvolvimento", "Concluído"];
    const statusIngles = ["Planned", "In Progress", "Completed"];

    const seletoresStatus = [
        document.getElementById("status"),
        document.getElementById("editarStatus")
    ];

    seletoresStatus.forEach(seletor => {

        Array.from(seletor.options).forEach((opcao, indice) => {

            opcao.textContent = idioma === "en"
                ? statusIngles[indice]
                : statusPortugues[indice];

        });

    });

    // Popup de edição
    document.querySelector("#popupEdicao h2").textContent = t.editar;

    document.querySelector('label[for="editarNome"]').textContent = t.nomeProjeto;
    document.querySelector('label[for="editarDescricao"]').textContent = t.descricao;
    document.querySelector('label[for="editarCategoria"]').textContent = t.categoriaLabel;
    document.querySelector('label[for="editarStatus"]').textContent = t.statusLabel;
    document.querySelector('label[for="editarPrazo"]').textContent = t.prazo;

    document.getElementById("botaoCancelarEdicao").textContent = t.cancelar;

    document.querySelector("#formEdicao button[type='submit']").textContent = t.salvar;

    // Rodapé
    document.querySelector("footer span:last-child").textContent = t.rodape;

    // Atualiza os gráficos
    if (graficoCategoria !== null) {

        graficoCategoria.data.datasets[0].label = t.quantidadeProjetos;

        graficoCategoria.update();

    }

    if (graficoMensal !== null) {
        atualizarGraficoMensal(projetosCarregados);
    }

    // Atualiza os projetos exibidos
    exibirProjetos(
        typeof projetosFiltradosAtuais !== "undefined"
            ? projetosFiltradosAtuais
            : projetosCarregados
    )

    document.documentElement.lang = idioma;

    localStorage.setItem("idioma", idioma);

    seletorIdioma.value = idioma;
}


/* CARREGA O IDIOMA SALVO */

aplicarIdioma(idiomaAtual);


/* DETECTA QUANDO O USUÁRIO MUDA O IDIOMA */

seletorIdioma.addEventListener("change", function() {

    aplicarIdioma(seletorIdioma.value);

});


/* INICIALIZAÇÃO */

document.getElementById("campoBusca").addEventListener("input", filtrarProjetos);

document.getElementById("botaoCancelarEdicao").addEventListener("click", function() {

    document.getElementById("popupEdicao").style.display = "none";

});

// Carrega os projetos somente depois que as traduções foram inicializadas.
listarProjetos();
