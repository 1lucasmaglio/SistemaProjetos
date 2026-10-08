# Sistema de Gerenciamento de Projetos

Sistema desenvolvido em **Java** para cadastro e gerenciamento de projetos.

O projeto está sendo desenvolvido durante as aulas com o objetivo de aplicar na prática conceitos de **orientação a objetos, organização em camadas, persistência de dados, interface gráfica e comunicação HTTP**.

---

## Como funciona?

O sistema é dividido em diferentes partes, cada uma com sua responsabilidade:

- `model` — representa os objetos do sistema.
- `service` — concentra as operações e regras relacionadas aos projetos.
- `dao` — realiza a leitura e escrita dos dados.
- `view` — contém a interface gráfica desktop.
- `api` — recebe e responde requisições HTTP.

A aplicação possui uma interface desktop desenvolvida com **Java Swing** e um servidor HTTP que será utilizado para disponibilizar as funcionalidades do sistema na web.

Os projetos são armazenados em um arquivo CSV, permitindo que os dados permaneçam salvos mesmo depois que o programa é encerrado.

---

## Arquitetura

```text
     DESKTOP (Swing)
            │
            ▼
         SERVICE
            │
            ▼
           DAO
            │
            ▼
    dados/projetos.csv

API (HttpServer) ───► SERVICE
```

---

## Tecnologias utilizadas

- **Java** — linguagem de programação
- **Java Swing** — interface desktop
- **HttpServer (JDK)** — servidor HTTP
- **CSV** — persistência dos dados

---

## Funcionalidades

### Interface desktop

- Cadastro de projetos
- Visualização dos projetos em tabela
- Seleção de categoria e status
- Armazenamento dos dados em CSV

### API HTTP

O sistema utiliza o `HttpServer`, disponível no JDK, para implementar a comunicação HTTP sem a necessidade de frameworks externos.

Atualmente, o servidor possui uma rota inicial de identificação da aplicação.

A implementação dos endpoints para gerenciamento dos projetos está em desenvolvimento.

---

## Estrutura do projeto

```text
SistemaProjetos/
├── dados/
│   └── projetos.csv
│
├── src/
│   ├── api/
│   │   └── Api.java
│   ├── dao/
│   │   └── ProjetoCSV.java
│   ├── model/
│   │   └── Projeto.java
│   ├── service/
│   │   └── ProjetoService.java
│   ├── view/
│   │   └── TelaProjetos.java
│   └── Main.java
│
├── .gitignore
└── README.md
```

---

## Executando o projeto

### Interface desktop

Para iniciar a aplicação desktop, execute a classe:

```text
src/view/TelaProjetos.java
```

A interface permite cadastrar projetos e visualizar os registros armazenados no arquivo CSV.

### Servidor HTTP

Para iniciar o servidor, execute:

```text
src/api/Api.java
```

O servidor será iniciado na porta `7070`.

Acesse:

```text
http://localhost:7070/
```

Resposta atual:

```text
API Sistema de Projetos
```

---

## Status

🚧 **Em desenvolvimento**

Projeto desenvolvido e atualizado durante as aulas, acompanhando a implementação de novos conceitos e funcionalidades.

O desenvolvimento está concentrado na interface desktop e na expansão da API HTTP para permitir o gerenciamento dos projetos pela web.
