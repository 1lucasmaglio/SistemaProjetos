# Sistema de Gerenciamento de Projetos

Sistema desenvolvido em **Java** para cadastro e gerenciamento de projetos.

O projeto está sendo desenvolvido durante as aulas com o objetivo de aplicar na prática conceitos de **orientação a objetos, organização em camadas, persistência de dados, interface gráfica e API REST**.

---

## Como funciona?

O sistema é dividido em diferentes partes, cada uma com sua responsabilidade:

- `model` — representa os objetos do sistema.
- `service` — concentra as operações e regras relacionadas aos projetos.
- `dao` — realiza a leitura e escrita dos dados.
- `view` — contém a interface gráfica.
- `api` — disponibiliza os dados através de uma API REST.

Os projetos são armazenados em um arquivo CSV, permitindo que os dados permaneçam salvos mesmo depois que o programa é encerrado.

---

## Arquitetura

```text
        VIEW
          │
          ▼
       SERVICE
          │
          ▼
         DAO
          │
          ▼
  dados/projetos.csv

API ─────► SERVICE
```

---

## Tecnologias utilizadas

- **Java**
- **Java Swing** — interface gráfica
- **Javalin** — API REST
- **Maven** — gerenciamento de dependências
- **CSV** — persistência dos dados

---

## Estrutura do projeto

```text
SistemaProjetos/
├── dados/
│   └── projetos.csv
│
├── src/main/java/
│   ├── api/
│   ├── dao/
│   ├── model/
│   ├── service/
│   ├── view/
│   └── Main.java
│
├── pom.xml
└── README.md
```

---

## Executando o projeto

Para utilizar a interface gráfica, execute:

```text
Main.java
```

Para iniciar a API REST, execute a classe responsável pela API dentro do pacote:

```text
api/
```

---

## Status

🚧 **Em desenvolvimento**

Projeto desenvolvido e atualizado durante as aulas, acompanhando a implementação de novos conceitos e funcionalidades.
