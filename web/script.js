const API = "http://localhost:7070/api/projetos";

async function listarProjetos() {

    const resposta = await fetch(API);
    const projetos = await resposta.json();

    const tabela = document.getElementById("tabelaProjetos");

    tabela.innerHTML = "";

    projetos.forEach(projeto => {

        tabela.innerHTML += `
            <tr>
                <td>${projeto.id}</td>
                <td>${projeto.nome}</td>
                <td>${projeto.descricao}</td>
                <td>${projeto.categoria}</td>
                <td>${projeto.status}</td>
                <td>
                    <button onclick="excluirProjeto(${projeto.id})">
                        Excluir
                    </button>
                </td>
            </tr>
        `;
    });
}

document.getElementById("formProjeto").addEventListener(
    "submit",
    async function(event) {

        event.preventDefault();

        const projeto = {
            id: Number(document.getElementById("id").value),
            nome: document.getElementById("nome").value,
            descricao: document.getElementById("descricao").value,
            categoria: document.getElementById("categoria").value,
            status: document.getElementById("status").value
        };

        await fetch(API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(projeto)
        });

        document.getElementById("formProjeto").reset();

        listarProjetos();
    }
);

async function excluirProjeto(id) {

    await fetch(`${API}/${id}`, {
        method: "DELETE"
    });

    listarProjetos();
}

listarProjetos();