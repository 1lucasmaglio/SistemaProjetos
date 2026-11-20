
const API_URL = "http://localhost:7070/api/login";

const formLogin = document.getElementById("formLogin");

formLogin.addEventListener("submit", function(event) {

    event.preventDefault();

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    const dadosLogin = {
        email: email,
        senha: senha
    };

    fetch(API_URL, {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(dadosLogin)

    })
    .then(function(resposta) {

        if (!resposta.ok) {
            throw new Error("E-mail ou senha incorretos.");
        }

        return resposta.json();

    })
    .then(function(dados) {

        localStorage.setItem("token", dados.token);

        localStorage.setItem("nomeUsuario", dados.nome);

        window.location.href = "/frontend/projetos.html";

    })
    .catch(function(erro) {

        const mensagem = document.getElementById("mensagemLogin");

        mensagem.textContent = erro.message;

    });

});
