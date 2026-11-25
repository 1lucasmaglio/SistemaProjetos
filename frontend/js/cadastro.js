/* CONFIGURAÇÃO DE TEMA */
const temaSalvo = localStorage.getItem("tema") || "dark";
document.body.classList.toggle("tema-claro", temaSalvo === "light");

/* CONFIGURAÇÃO DA API */
const API_URL = "http://localhost:7070/api/cadastro";

const formCadastro = document.getElementById("formCadastro");

formCadastro.addEventListener("submit", function (evento) {
  evento.preventDefault();

  const nome = document.getElementById("nome").value;
  const email = document.getElementById("email").value;
  const senha = document.getElementById("senha").value;

  const dadosCadastro = {
    nome: nome,
    email: email,
    senha: senha,
  };

  fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(dadosCadastro),
  })
    .then(function (resposta) {
      return resposta.json().then(function (dados) {
        if (!resposta.ok) {
          throw new Error(
            dados.erro || "Não foi possível realizar o cadastro.",
          );
        }

        return dados;
      });
    })
    .then(function (dados) {
      alert("Conta criada com sucesso! Faça login para continuar.");
      window.location.href = "/frontend/login.html";
    })
    .catch(function (erro) {
      const mensagem = document.getElementById("mensagemCadastro");
      mensagem.textContent = erro.message;
    });
});
