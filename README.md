# Sistema de Projetos

Projeto acadêmico desenvolvido durante o curso de **Análise e Desenvolvimento de Sistemas da FATEC**, com base nos conteúdos, exemplos e atividades apresentados pelo professor.

O projeto tem como objetivo aplicar conceitos de programação Java, orientação a objetos, persistência de dados, desenvolvimento de APIs REST e integração entre diferentes interfaces.

O sistema permite gerenciar projetos por meio de uma aplicação desktop desenvolvida com Java Swing e de uma interface web construída com HTML, CSS e JavaScript. Ambas utilizam o mesmo backend, desenvolvido em Java.

## Tecnologias utilizadas

**Backend**
- Java 21
- Maven
- Javalin
- Jackson
- HTTP e JSON

**Frontend web**
- HTML5
- CSS3
- JavaScript
- Fetch API

**Interface desktop**
- Java Swing
- Java HttpClient

**Persistência de dados**
- Arquivos CSV

## Funcionalidades

- Cadastro de projetos
- Listagem de projetos cadastrados
- Busca de projetos por ID
- Consulta e filtragem por categoria e status
- Alteração de informações dos projetos
- Exclusão de projetos
- Armazenamento dos dados em arquivo CSV
- API REST com operações CRUD
- Interface desktop integrada à API
- Interface web integrada à API
- Atualização da listagem após cadastros e exclusões na interface web

As operações disponíveis podem variar conforme a interface utilizada.

## Estrutura do projeto

```text
SistemaProjetos/
│
├── backend/
│   ├── dados/
│   │   └── projetos.csv
│   │
│   ├── src/main/java/
│   │   ├── api/
│   │   │   └── Api.java
│   │   ├── client/
│   │   │   └── ApiClient.java
│   │   ├── dao/
│   │   │   └── ProjetoCSV.java
│   │   ├── model/
│   │   │   └── Projeto.java
│   │   ├── service/
│   │   │   └── ProjetoService.java
│   │   └── view/
│   │       └── TelaProjetos.java
│   │
│   └── pom.xml
│
└── frontend/
    ├── css/
    │   └── style.css
    ├── js/
    │   └── script.js
    └── projetos.html
```

### Organização do código

- `model` — Representa os dados dos projetos.
- `service` — Contém as regras de negócio e as operações de gerenciamento.
- `dao` — Responsável pela leitura e gravação dos dados em CSV.
- `api` — Disponibiliza os endpoints REST utilizando Javalin.
- `client` — Realiza requisições HTTP para a API Java.
- `view` — Contém a interface gráfica desktop desenvolvida com Swing.
- `frontend` — Contém a interface web, seus estilos e o JavaScript responsável pela comunicação com a API.
- `dados` — Armazena os registros dos projetos em arquivo CSV.

## API REST

A API é executada localmente na porta `7070` e disponibiliza os seguintes endpoints:

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/projetos` | Lista os projetos |
| GET | `/api/projetos/{id}` | Consulta um projeto pelo ID |
| POST | `/api/projetos` | Cadastra um novo projeto |
| PUT | `/api/projetos/{id}` | Atualiza um projeto |
| DELETE | `/api/projetos/{id}` | Exclui um projeto |

A comunicação entre as interfaces e o backend utiliza HTTP, com envio e recebimento de dados em JSON.

## Interface web

A interface web foi desenvolvida com HTML, CSS e JavaScript, permitindo cadastrar, visualizar e excluir projetos diretamente pelo navegador.

O JavaScript utiliza a **Fetch API** para realizar requisições ao backend.

O CSS foi personalizado para oferecer uma interface moderna, com formulário organizado, cartões para exibição dos projetos e layout responsivo para diferentes tamanhos de tela.

## Interface desktop

A aplicação desktop utiliza Java Swing para apresentar uma interface gráfica de gerenciamento de projetos.

A comunicação com o backend é realizada pela classe `ApiClient`, utilizando `HttpClient` para enviar requisições HTTP à API.

As interfaces desktop e web compartilham os mesmos dados por meio do backend.

## Persistência de dados

Os projetos são armazenados no arquivo `backend/dados/projetos.csv`.

A classe `ProjetoCSV` é responsável pelas operações de leitura e gravação, permitindo manter os dados registrados entre as execuções da aplicação.

## Objetivo acadêmico

O sistema foi desenvolvido como atividade prática para acompanhar a evolução de uma aplicação Java, desde a criação de classes e objetos até a implementação de persistência de dados, comunicação HTTP, API REST e integração com interfaces desktop e web.

O desenvolvimento também permitiu praticar a organização do código em diferentes responsabilidades e compreender a comunicação entre frontend, backend e armazenamento de dados.

---

**Observação:** Este projeto possui finalidade educacional e foi desenvolvido com base nas orientações, nos exemplos e nos materiais disponibilizados pelo professor durante as aulas, com personalizações realizadas ao longo do desenvolvimento.
