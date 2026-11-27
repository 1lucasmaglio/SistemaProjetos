# Relatório – Qualidade, Métricas e Auditoria do Sistema de Projetos

## Integrantes do grupo

- Lucas Maglio Chiabai
- Thiago Alexandre Marques Basílio
- Thiago Santos de Almeida
- Yann Sahmuel Escobar de Campos

## 1. Apresentação do sistema

O Sistema de Projetos é uma aplicação desenvolvida como projeto acadêmico durante o curso de Análise e Desenvolvimento de Sistemas da FATEC. Seu objetivo é permitir o gerenciamento de projetos de maneira organizada, possibilitando o cadastro, a consulta, a edição, a exclusão e o acompanhamento das informações registradas.

O sistema foi desenvolvido utilizando a linguagem Java no backend, oferecendo duas formas de utilização: uma interface Desktop, construída com Java Swing, e uma interface Web, desenvolvida com HTML, CSS e JavaScript.

As duas interfaces se comunicam com o mesmo backend por meio de uma API REST, permitindo que os dados sejam compartilhados e centralizados.

Além do gerenciamento de projetos, a aplicação possui autenticação de usuários, armazenamento de informações em arquivos CSV, dashboard com indicadores e gráficos, além de arquivos de log para registrar atividades e acompanhar o funcionamento do sistema.

## 2. Descrição geral do funcionamento

O funcionamento do sistema está dividido em três partes principais: frontend, backend e armazenamento de dados.

### 2.1. Frontend – Interfaces do sistema

O frontend corresponde à parte visual com a qual o usuário interage. O projeto possui duas interfaces diferentes.

**Interface Web**

A interface Web foi desenvolvida utilizando:

- HTML5: responsável pela estrutura e organização dos elementos da página.
- CSS3: responsável pela aparência, cores, espaçamentos, posicionamento dos elementos e adaptação da página a diferentes tamanhos de tela.
- JavaScript: responsável pelas funcionalidades, interações, atualização das informações e comunicação com o backend.
- Fetch API: utilizada para enviar requisições HTTP à API e receber os dados.

A interface Web possui tela de login, tela de cadastro de usuários e área principal de gerenciamento dos projetos.

Na área principal, o usuário encontra o dashboard, os indicadores, os gráficos e a listagem dos projetos. Também é possível cadastrar novos projetos, editar informações, excluir registros, pesquisar e filtrar os projetos.

**Interface Desktop**

A interface Desktop foi desenvolvida utilizando Java Swing, biblioteca utilizada para construir interfaces gráficas em aplicações Java.

Ela permite que o usuário realize o gerenciamento dos projetos por meio de janelas, formulários, botões, tabelas e outros componentes visuais.

A comunicação com o backend é realizada pela classe ApiClient, que utiliza Java HttpClient para enviar requisições HTTP.

Dessa maneira, tanto a versão Web quanto a Desktop utilizam a mesma API e compartilham os mesmos dados.

### 2.2. Backend – Processamento e regras do sistema

O backend foi desenvolvido em Java 21 e é responsável pelo processamento das informações, aplicação das regras de negócio, autenticação e comunicação com o armazenamento.

A aplicação utiliza Maven para gerenciamento das dependências e Javalin para disponibilizar a API REST.

O código está organizado em pacotes, cada um com uma responsabilidade específica:

- **api:** responsável por disponibilizar os endpoints e receber as requisições das interfaces.
- **client:** responsável pela comunicação HTTP utilizada pela interface Desktop.
- **dao:** responsável pela leitura e gravação dos dados em arquivos CSV e pelo registro dos logs.
- **model:** contém as classes que representam os objetos utilizados pelo sistema, como Projeto, Usuario e Log.
- **service:** concentra as regras de negócio, autenticação, gerenciamento de projetos e registro de atividades.
- **view:** contém as telas da aplicação Desktop.

Essa organização separa as responsabilidades e facilita a manutenção e o desenvolvimento do sistema.

### 2.3. API REST – Comunicação entre as interfaces

A API REST funciona como intermediária entre as interfaces e o backend.

Ela recebe as solicitações realizadas pelo usuário, processa as informações e retorna uma resposta.

A API utiliza os métodos HTTP:

- GET: consulta informações.
- POST: realiza cadastros.
- PUT: atualiza informações existentes.
- DELETE: exclui registros.

Os dados são enviados e recebidos no formato JSON.

A aplicação disponibiliza os seguintes endpoints:

| Método | Endpoint | Função |
|---|---|---|
| POST | `/api/login` | Realiza o login do usuário |
| GET | `/api/projetos` | Lista os projetos do usuário |
| GET | `/api/projetos/{id}` | Busca um projeto pelo ID |
| POST | `/api/projetos` | Cadastra um projeto |
| PUT | `/api/projetos/{id}` | Altera um projeto |
| DELETE | `/api/projetos/{id}` | Exclui um projeto |

A API é executada localmente na porta `7070`.

As operações que necessitam de autenticação utilizam um token de acesso para identificar o usuário e controlar o acesso aos projetos.

### 2.4. Autenticação e gerenciamento de usuários

O sistema possui funcionalidades de cadastro e autenticação de usuários.

Cada usuário realiza o login para acessar suas informações e gerenciar seus próprios projetos.

Os projetos são associados ao identificador do usuário responsável pelo cadastro. Dessa forma, o sistema consegue identificar a quem cada projeto pertence e controlar quais registros podem ser acessados.

A aplicação também utiliza arquivos CSV para armazenar os dados dos usuários.

### 2.5. Gerenciamento de projetos

O gerenciamento de projetos é a principal funcionalidade da aplicação.

Cada projeto possui as seguintes informações:

- **ID:** identificador numérico gerado automaticamente.
- **Nome:** nome do projeto.
- **Descrição:** informações sobre o projeto.
- **Categoria:** classificação do projeto.
- **Status:** situação atual do projeto.
- **Data de criação:** data em que o projeto foi cadastrado.
- **Prazo:** data definida para a conclusão.

As categorias disponíveis são Software, Mobile, Web e Desktop.

Os status disponíveis são Planejado, Em Desenvolvimento, Concluído e Cancelado.

O sistema permite realizar as quatro operações principais de um CRUD:

- **Create (Criar):** cadastrar novos projetos.
- **Read (Consultar):** visualizar, pesquisar e filtrar projetos.
- **Update (Atualizar):** editar as informações de projetos existentes.
- **Delete (Excluir):** remover projetos cadastrados.

Também existem regras de validação para os dados informados.

O nome deve ser obrigatório e possuir entre 3 e 100 caracteres. A descrição deve possuir entre 10 e 500 caracteres. A categoria e o status devem corresponder às opções permitidas pelo sistema.

O prazo é obrigatório, não pode ser uma data passada e não pode ser anterior à data de criação do projeto.

Essas validações ajudam a evitar o armazenamento de informações incorretas.

### 2.6. Armazenamento dos dados

O sistema utiliza arquivos CSV para armazenar os dados dos projetos e usuários.

Os arquivos estão localizados no diretório `backend/dados`.

A classe ProjetoCSV é responsável pela leitura e gravação dos projetos, enquanto a classe UsuarioCSV realiza as operações relacionadas aos usuários.

Esse modelo permite manter os registros armazenados mesmo após o encerramento da aplicação.

## 3. Dashboard – Indicadores e visualização de dados

O dashboard permite acompanhar informações gerais dos projetos cadastrados por meio de indicadores e gráficos.

Ele transforma os dados armazenados em informações visuais, facilitando a compreensão da situação dos projetos.

### 3.1. Indicadores apresentados

O dashboard Web possui quatro indicadores principais:

**1. Total de Projetos**

Apresenta a quantidade total de projetos cadastrados pelo usuário.

**2. Projetos Concluídos**

Mostra quantos projetos possuem o status Concluído.

**3. Projetos em Desenvolvimento**

Apresenta a quantidade de projetos que estão atualmente em desenvolvimento.

**4. Projetos Planejados**

Mostra quantos projetos ainda estão na etapa de planejamento.

Esses indicadores são atualizados de acordo com os projetos carregados pelo sistema.

### 3.2. Gráficos apresentados

O dashboard possui três gráficos desenvolvidos utilizando a biblioteca Chart.js.

**1. Projetos por Status**

Utiliza um gráfico do tipo rosca (Doughnut).

Apresenta a distribuição dos projetos de acordo com os status Concluído, Em Desenvolvimento e Planejado.

Seu objetivo é facilitar a visualização da situação dos projetos.

**2. Projetos por Categoria**

Utiliza um gráfico de barras.

Apresenta a quantidade de projetos nas categorias Desktop, Mobile, Software e Web.

Seu objetivo é permitir a comparação entre as diferentes categorias.

**3. Evolução Mensal de Projetos**

Utiliza um gráfico de linhas.

Apresenta a quantidade de projetos cadastrados em cada mês, considerando a data de criação dos registros.

Seu objetivo é demonstrar a evolução dos cadastros ao longo do tempo.

## 4. Testes e documentação dos casos de teste

A etapa de testes tem como objetivo verificar se as funcionalidades do Sistema de Projetos estão funcionando corretamente e se as informações são processadas conforme as regras estabelecidas.

Foram realizados 20 casos de teste, envolvendo o cadastro, a consulta, a alteração, a exclusão, a autenticação, a persistência dos dados, a validação de campos e a integração entre as interfaces Web e Desktop.

Cada caso de teste apresenta seu identificador, nome, descrição, ação realizada, resultado esperado, resultado obtido e status final.

### 4.1. Casos de teste

**Caso de Teste 01 – ID: 2026001**

- **Nome:** Login no Web.
- **Funcionalidade testada:** Autenticação de usuário na interface Web.
- **Ação:** Acessar a página de login, preencher os campos de usuário e senha com dados válidos e clicar no botão "Entrar".
- **Resultado esperado:** Login realizado com sucesso, com redirecionamento para a página principal do sistema.
- **Resultado obtido:** Login realizado com sucesso e acesso à página principal.
- **Status:** Aprovado.

---

**Caso de Teste 02 – ID: 2026002**

- **Nome:** Login no Desktop.
- **Funcionalidade testada:** Autenticação de usuário na interface Desktop.
- **Ação:** Acessar a tela de login, preencher os campos de usuário e senha com dados válidos e clicar no botão "Entrar".
- **Resultado esperado:** Login realizado com sucesso, com acesso à página principal do sistema.
- **Resultado obtido:** Login realizado com sucesso e acesso à página principal.
- **Status:** Aprovado.

---

**Caso de Teste 03 – ID: 2026003**

- **Nome:** Cadastro de projeto no Web.
- **Funcionalidade testada:** Cadastro de novos projetos na interface Web.
- **Ação:** Preencher os campos do formulário com informações válidas e clicar no botão de cadastro.
- **Resultado esperado:** O projeto deve ser cadastrado com sucesso e exibido na lista de projetos.
- **Resultado obtido:** O projeto foi cadastrado com sucesso nas opções "Planejado" e "Concluído". Porém, ao selecionar a opção "Em Desenvolvimento", aparece uma mensagem informando "Status inválido".
- **Status:** Reprovado.

---

**Caso de Teste 04 – ID: 2026004**

- **Nome:** Cadastro de projeto no Desktop.
- **Funcionalidade testada:** Cadastro de novos projetos na interface Desktop.
- **Ação:** Preencher os campos do formulário com informações válidas e clicar no botão de cadastro.
- **Resultado esperado:** O projeto deve ser cadastrado com sucesso e exibido na lista de projetos.
- **Resultado obtido:** O projeto foi cadastrado com sucesso.
- **Status:** Aprovado.

---

**Caso de Teste 05 – ID: 2026005**

- **Nome:** Cadastro de conta no Web.
- **Funcionalidade testada:** Cadastro de novos usuários na interface Web.
- **Ação:** Acessar a página de cadastro, preencher os campos com informações válidas e clicar no botão de cadastrar conta.
- **Resultado esperado:** A conta deve ser criada com sucesso e o sistema deve confirmar a conclusão do cadastro.
- **Resultado obtido:** A conta foi cadastrada com sucesso e o sistema confirmou a conclusão do cadastro.
- **Status:** Aprovado.

---

**Caso de Teste 06 – ID: 2026006**

- **Nome:** Cadastro de conta no Desktop.
- **Funcionalidade testada:** Cadastro de novos usuários na interface Desktop.
- **Ação:** Acessar a página de cadastro, preencher os campos com informações válidas e clicar no botão de cadastrar conta.
- **Resultado esperado:** A conta deve ser criada com sucesso e o sistema deve confirmar a conclusão do cadastro.
- **Resultado obtido:** A conta foi cadastrada com sucesso e o sistema confirmou a conclusão do cadastro.
- **Status:** Aprovado.

---

**Caso de Teste 07 – ID: 2026007**

- **Nome:** Edição de projeto no Web.
- **Funcionalidade testada:** Alteração de informações de projetos na interface Web.
- **Ação:** Selecionar um projeto existente, alterar suas informações e salvar as modificações.
- **Resultado esperado:** As alterações devem ser salvas e apresentadas nos dados do projeto.
- **Resultado obtido:** As alterações foram salvas e exibidas corretamente.
- **Status:** Aprovado.

---

**Caso de Teste 08 – ID: 2026008**

- **Nome:** Edição de projeto no Desktop.
- **Funcionalidade testada:** Alteração de informações de projetos na interface Desktop.
- **Ação:** Selecionar um projeto existente, alterar suas informações e salvar as modificações.
- **Resultado esperado:** As alterações devem ser salvas e apresentadas nos dados do projeto.
- **Resultado obtido:** As alterações foram salvas com sucesso nas opções "Planejado" e "Concluído". Porém, ao selecionar a opção "Em Desenvolvimento", aparece uma mensagem informando "Status inválido".
- **Status:** Reprovado.

---

**Caso de Teste 09 – ID: 2026009**

- **Nome:** Exclusão de projeto no Web.
- **Funcionalidade testada:** Exclusão de projetos na interface Web.
- **Ação:** Selecionar um projeto existente e executar a opção de exclusão.
- **Resultado esperado:** O projeto deve ser removido da lista de projetos cadastrados.
- **Resultado obtido:** O projeto foi excluído com sucesso e deixou de aparecer na lista.
- **Status:** Aprovado.

---

**Caso de Teste 10 – ID: 2026010**

- **Nome:** Exclusão de projeto no Desktop.
- **Funcionalidade testada:** Exclusão de projetos na interface Desktop.
- **Ação:** Selecionar um projeto existente e executar a opção de exclusão.
- **Resultado esperado:** O projeto deve ser removido da lista de projetos cadastrados.
- **Resultado obtido:** O projeto foi excluído com sucesso e deixou de aparecer na lista.
- **Status:** Aprovado.

---

**Caso de Teste 11 – ID: 2026011**

- **Nome:** Validação de credenciais inválidas no Web.
- **Funcionalidade testada:** Validação de autenticação na interface Web.
- **Ação:** Preencher os campos de login com credenciais inválidas e tentar acessar o sistema.
- **Resultado esperado:** O sistema deve rejeitar as credenciais e impedir o acesso à plataforma.
- **Resultado obtido:** O sistema rejeitou as credenciais inválidas e impediu o acesso.
- **Status:** Aprovado.

---

**Caso de Teste 12 – ID: 2026012**

- **Nome:** Validação de credenciais inválidas no Desktop.
- **Funcionalidade testada:** Validação de autenticação na interface Desktop.
- **Ação:** Preencher os campos de login com credenciais inválidas e tentar acessar o sistema.
- **Resultado esperado:** O sistema deve rejeitar as credenciais e impedir o acesso à plataforma.
- **Resultado obtido:** O sistema rejeitou as credenciais inválidas e impediu o acesso.
- **Status:** Aprovado.

---

**Caso de Teste 13 – ID: 2026013**

- **Nome:** Persistência dos projetos cadastrados no Web.
- **Funcionalidade testada:** Armazenamento e recuperação dos projetos na interface Web.
- **Ação:** Cadastrar um projeto, sair do sistema ou atualizar a página e consultar novamente a lista.
- **Resultado esperado:** O projeto deve permanecer disponível após o novo acesso ao sistema.
- **Resultado obtido:** O projeto permaneceu armazenado e disponível após o novo acesso.
- **Status:** Aprovado.

---

**Caso de Teste 14 – ID: 2026014**

- **Nome:** Persistência dos projetos cadastrados no Desktop.
- **Funcionalidade testada:** Armazenamento e recuperação dos projetos na interface Desktop.
- **Ação:** Cadastrar um projeto, sair do sistema e acessar novamente para consultar a lista.
- **Resultado esperado:** O projeto deve permanecer disponível após o novo acesso ao sistema.
- **Resultado obtido:** O projeto permaneceu armazenado e disponível após o novo acesso.
- **Status:** Aprovado.

---

**Caso de Teste 15 – ID: 2026015**

- **Nome:** Validação de campos obrigatórios.
- **Funcionalidade testada:** Validação dos campos obrigatórios no cadastro de projetos.
- **Ação:** Tentar cadastrar um projeto deixando um ou mais campos obrigatórios em branco.
- **Resultado esperado:** O sistema deve impedir o cadastro e informar que os campos obrigatórios precisam ser preenchidos.
- **Resultado obtido:** O sistema impediu o cadastro incompleto e realizou a validação dos campos obrigatórios.
- **Status:** Aprovado.

---

**Caso de Teste 16 – ID: 2026016**

- **Nome:** Exibição do projeto cadastrado.
- **Funcionalidade testada:** Listagem e apresentação dos projetos cadastrados.
- **Ação:** Cadastrar um projeto e consultar sua exibição na lista.
- **Resultado esperado:** O projeto deve aparecer na lista com as informações correspondentes ao cadastro.
- **Resultado obtido:** O projeto foi exibido corretamente na lista com suas respectivas informações.
- **Status:** Aprovado.

---

**Caso de Teste 17 – ID: 2026017**

- **Nome:** Consistência dos projetos entre Web e Desktop.
- **Funcionalidade testada:** Integração e consistência dos dados entre as duas interfaces.
- **Ação:** Consultar os mesmos projetos nas versões Web e Desktop e comparar as informações exibidas.
- **Resultado esperado:** As informações dos projetos devem corresponder nas duas versões do sistema.
- **Resultado obtido:** As informações dos projetos foram apresentadas corretamente nas duas versões.
- **Status:** Aprovado.

---

**Caso de Teste 18 – ID: 2026018**

- **Nome:** Atualização de dados entre plataformas.
- **Funcionalidade testada:** Sincronização das informações entre as interfaces Web e Desktop.
- **Ação:** Alterar as informações de um projeto em uma das plataformas e consultar os dados na outra versão.
- **Resultado esperado:** A outra plataforma deve apresentar as informações atualizadas corretamente.
- **Resultado obtido:** Os dados atualizados foram apresentados corretamente na outra plataforma.
- **Status:** Aprovado.

---

**Caso de Teste 19 – ID: 2026019**

- **Nome:** Exibição do dashboard.
- **Funcionalidade testada:** Carregamento dos indicadores e gráficos do dashboard.
- **Ação:** Acessar o dashboard e consultar os indicadores e dados exibidos.
- **Resultado esperado:** O dashboard deve carregar corretamente e apresentar as informações disponíveis no sistema.
- **Resultado obtido:** O dashboard foi carregado corretamente e apresentou as informações esperadas.
- **Status:** Aprovado.

---

**Caso de Teste 20 – ID: 2026020**

- **Nome:** Registro de logs do sistema.
- **Funcionalidade testada:** Registro das operações realizadas pelos usuários nos arquivos de log.
- **Ação:** Executar uma operação no sistema e consultar os registros de logs correspondentes.
- **Resultado esperado:** O sistema deve registrar a operação realizada com as informações disponíveis para o evento.
- **Resultado obtido:** O registro da operação foi encontrado nos logs do sistema.
- **Status:** Aprovado.

## 5. Análise dos erros encontrados

Após a execução dos 20 casos de teste, foram identificadas duas reprovações relacionadas ao uso do status "Em Desenvolvimento" no gerenciamento de projetos.

No cadastro de projetos pela interface Web (Caso de Teste 03), o sistema permitiu cadastrar projetos com os status "Planejado" e "Concluído", mas apresentou a mensagem "Status inválido" ao selecionar "Em Desenvolvimento".

O mesmo problema foi identificado na edição de projetos pela interface Desktop (Caso de Teste 08). As alterações foram realizadas corretamente com os status "Planejado" e "Concluído", porém a seleção de "Em Desenvolvimento" resultou na mensagem "Status inválido".

Esses resultados indicam uma inconsistência na validação ou no tratamento do status entre as interfaces e o backend, que precisa ser investigada e corrigida.

### 5.1. Resumo dos resultados

| Resultado | Quantidade |
|---|---:|
| Testes aprovados | 18 |
| Testes reprovados | 2 |
| Total de testes | 20 |
| Percentual de aprovação | 90% |
| Percentual de reprovação | 10% |

**Conclusão da análise:** A maioria das funcionalidades avaliadas apresentou o comportamento esperado. Entretanto, os problemas identificados com o status "Em Desenvolvimento" demonstram a necessidade de revisar a validação dos status de projetos, garantindo o funcionamento correto nas interfaces Web e Desktop.

## 6. Logs – Registro e auditoria do sistema

Os logs são arquivos utilizados para registrar acontecimentos importantes durante a execução da aplicação.

No Sistema de Projetos, eles permitem acompanhar as operações realizadas pelos usuários, registrar requisições da API, identificar erros e verificar informações relacionadas ao funcionamento da aplicação.

A implementação utiliza a classe ArquivoLog para escrever os registros e a classe LogService para organizar os eventos de auditoria.

O sistema possui cinco arquivos principais de log:

### 6.1. application.log – Log da aplicação

Registra acontecimentos relacionados ao funcionamento geral da aplicação.

Entre os registros estão:

- Inicialização da API.
- Encerramento da API.
- Erros gerais da aplicação.

Esse arquivo permite acompanhar quando a API foi iniciada e quando foi encerrada, além de registrar problemas gerais.

### 6.2. access.log – Log de acesso

Registra informações relacionadas aos acessos dos usuários.

Permite acompanhar os registros de login, identificando o usuário e o momento em que a operação ocorreu.

Esse arquivo ajuda a acompanhar o acesso ao sistema.

### 6.3. audit.log – Log de auditoria

É um dos arquivos mais importantes para acompanhar as atividades realizadas nos projetos.

Registra operações como:

- Inclusão de projetos.
- Alteração de projetos.
- Exclusão de projetos.

Cada registro contém informações como data, horário, identificador do usuário, tipo da operação e descrição da atividade.

Dessa maneira, é possível identificar quem realizou determinada operação e qual projeto foi afetado.

### 6.4. error.log – Log de erros

Armazena informações sobre erros encontrados durante a execução do sistema.

Pode registrar problemas relacionados às validações dos campos, erros inesperados e outras situações que impedem a execução correta de uma operação.

Esse arquivo facilita a identificação e a análise de problemas.

### 6.5. api.log – Log da API

Registra as requisições HTTP recebidas pela API.

Entre as informações registradas estão:

- Método HTTP utilizado.
- Caminho do endpoint acessado.
- Código de resposta HTTP.
- Tempo de processamento da requisição.

Esse arquivo permite acompanhar as operações realizadas pela comunicação entre as interfaces e o backend.

### 6.6. Importância dos logs para auditoria

Os logs permitem acompanhar o histórico de funcionamento e utilização do sistema.

Com eles, é possível verificar operações realizadas, identificar usuários responsáveis por alterações, acompanhar requisições e investigar problemas.

Isso contribui para a rastreabilidade das informações, facilitando a manutenção e o controle das atividades realizadas na aplicação.

## 7. Conclusão

O Sistema de Projetos reúne diferentes conceitos e tecnologias estudados durante o desenvolvimento do projeto acadêmico.

A aplicação apresenta duas interfaces de utilização, Web e Desktop, integradas por meio de uma API REST desenvolvida em Java.

Seu funcionamento envolve autenticação de usuários, gerenciamento de projetos, validação de informações, armazenamento em arquivos CSV e comunicação HTTP.

Além das operações tradicionais de um CRUD, o sistema possui um dashboard com quatro indicadores e três gráficos, permitindo acompanhar a quantidade de projetos, seus status, categorias e evolução mensal.

A implementação dos arquivos de log também contribui para o acompanhamento das operações, identificação de erros e auditoria das atividades realizadas.

A execução dos 20 casos de teste permitiu avaliar o comportamento das funcionalidades. Foram obtidos 18 testes aprovados e 2 reprovados, resultando em um índice de aprovação de 90%. Os problemas encontrados estão relacionados à validação do status "Em Desenvolvimento" em operações realizadas pelas interfaces Web e Desktop.

Dessa forma, o projeto demonstra a aplicação de conceitos de desenvolvimento de software, organização de código, qualidade, análise de dados e auditoria, aproximando uma aplicação acadêmica das características encontradas em sistemas utilizados profissionalmente.
