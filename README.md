 <p align="center">
  <img src="frontend/img/logoTitle.png" alt="Logo do Sistema de Projetos" width="400" height="400">
</p>

# Sistema de Projetos

Projeto acadêmico desenvolvido durante o curso de **Análise e Desenvolvimento de Sistemas da FATEC**, com base nos conteúdos, exemplos e atividades apresentados pelo professor. 

O sistema tem como objetivo aplicar conceitos de programação Java, orientação a objetos, persistência de dados, desenvolvimento de APIs REST, autenticação de usuários e integração entre diferentes interfaces.

A aplicação permite gerenciar projetos por meio de uma interface desktop desenvolvida com Java Swing e de uma interface web construída com HTML, CSS e JavaScript. Ambas utilizam o mesmo backend, desenvolvido em Java, permitindo compartilhar os dados por meio de uma API REST.

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

### Gerenciamento de usuários
- Autenticação de usuários por meio de login.
- Identificação do usuário responsável pelos projetos.
- Associação dos projetos aos respectivos usuários.
- Controle de acesso aos projetos de cada usuário.

### Gerenciamento de projetos
- Cadastro de projetos.
- Listagem dos projetos cadastrados.
- Busca de projetos por ID.
- Consulta e filtragem por categoria e status.
- Alteração das informações dos projetos.
- Exclusão de projetos.
- Registro da data de criação.
- Definição de prazo para conclusão dos projetos.

### Integração entre interfaces
- Interface desktop integrada à API.
- Interface web integrada à API.
- Compartilhamento dos dados entre as duas interfaces por meio do backend.
- Atualização da listagem após operações realizadas na interface web.

### Dashboard
- Exibição de indicadores relacionados aos projetos cadastrados.

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
    ├── img/
    │   └── logoTitle.gif
    ├── js/
    │   └── script.js
    └── projetos.html
```

*Observação: a estrutura acima apresenta os principais arquivos do projeto.*

### Organização do código

- `model` — Representa os dados utilizados pela aplicação.
- `service` — Contém as regras de negócio e as operações de gerenciamento.
- `dao` — Responsável pela leitura e gravação dos dados em CSV.
- `api` — Disponibiliza os endpoints REST utilizando Javalin.
- `client` — Realiza requisições HTTP para a API Java.
- `view` — Contém a interface gráfica desktop desenvolvida com Swing.
- `frontend` — Contém a interface web, seus estilos e o JavaScript responsável pela comunicação com a API.
- `dados` — Armazena os registros dos projetos em arquivos CSV.

## API REST

A API é executada localmente na porta `7070` e disponibiliza os seguintes endpoints:

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/login` | Realiza a autenticação do usuário |
| GET | `/api/projetos` | Lista os projetos do usuário autenticado |
| GET | `/api/projetos/{id}` | Consulta um projeto pelo ID |
| POST | `/api/projetos` | Cadastra um novo projeto |
| PUT | `/api/projetos/{id}` | Atualiza um projeto |
| DELETE | `/api/projetos/{id}` | Exclui um projeto |

A comunicação entre as interfaces e o backend utiliza HTTP, com envio e recebimento de dados em JSON.

As requisições que exigem autenticação utilizam um token de acesso para identificar o usuário e controlar o acesso aos dados.

## Interface web

A interface web foi desenvolvida com HTML, CSS e JavaScript, permitindo gerenciar projetos diretamente pelo navegador.

O JavaScript utiliza a **Fetch API** para realizar requisições ao backend, incluindo operações de cadastro, consulta, alteração e exclusão.

A interface também conta com:
- Tela de login.
- Formulário de cadastro de projetos.
- Definição de prazo para conclusão.
- Edição das informações dos projetos.
- Exibição das datas de criação e dos prazos.
- Filtragem de projetos.
- Dashboard com indicadores.
- Layout responsivo para diferentes tamanhos de tela.

## Interface desktop

A aplicação desktop utiliza Java Swing para apresentar uma interface gráfica de gerenciamento de projetos.

A comunicação com o backend é realizada pela classe `ApiClient`, utilizando `HttpClient` para enviar requisições HTTP à API.

A interface permite realizar operações de gerenciamento de projetos e visualizar informações como categoria, status, data de criação e prazo.

As interfaces desktop e web compartilham os mesmos dados por meio do backend.

## Persistência de dados

Os projetos são armazenados em arquivos CSV, localizados no diretório `backend/dados/`.

A classe `ProjetoCSV` é responsável pelas operações de leitura e gravação, permitindo manter os dados registrados entre as execuções da aplicação.

## Objetivo acadêmico

O sistema foi desenvolvido como atividade prática para acompanhar a evolução de uma aplicação Java, desde a criação de classes e objetos até a implementação de persistência de dados, autenticação, comunicação HTTP, API REST e integração com interfaces desktop e web.

O desenvolvimento também permite praticar a organização do código em diferentes responsabilidades e compreender a comunicação entre frontend, backend e armazenamento de dados.

---

<p align="center"><img src="frontend/img/Asset-2.webp" alt="Logo do Sistema de Projetos" width="128" height="128"></p>
