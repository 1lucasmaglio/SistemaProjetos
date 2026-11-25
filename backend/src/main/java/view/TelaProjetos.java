package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jfree.chart.ChartPanel;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import client.ApiClient;
import model.Projeto;

public class TelaProjetos extends JFrame {

        private static final Font FONTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);

        private static final Font FONTE_NEGRITO = new Font("Segoe UI", Font.BOLD, 14);

        private final Preferences preferencias = Preferences.userNodeForPackage(TelaProjetos.class);

        private boolean temaClaro;

        private JTextField campoNome;
        private JTextField campoDescricao;
        private JTextField campoBusca;
        private DatePicker campoPrazo;

        private JComboBox<String> comboCategoria;
        private JComboBox<String> comboStatus;

        private JComboBox<String> comboTema;
        private JComboBox<String> comboIdioma;

        private String idiomaAtual = "pt-BR";
        private boolean atualizandoIdioma = false;

        private JTable tabela;
        private DefaultTableModel modelo;
        private CardLayout gerenciadorTelas;
        private JPanel painelTelas;
        private String telaAtual = "DASHBOARD";

        private JButton botaoCadastrar;
        private JButton botaoLimpar;
        private JButton botaoBuscar;
        private JButton botaoSair;
        private JLabel rotuloUsuario;
        private JLabel tituloSistema;
        private JPanel painelPaginacao;
        private JPanel painelLista;
        private JScrollPane scrollTabela;
        private List<Projeto> projetosFiltrados = new java.util.ArrayList<>();
        private int paginaAtual = 1;
        private static final int PROJETOS_POR_PAGINA = 5;

        private JLabel valorTotal;
        private JLabel valorConcluidos;
        private JLabel valorAndamento;
        private JLabel valorPlanejados;

        private JLabel rotuloDashboard;
        private JLabel rotuloProjetos;
        private JLabel rotuloConfiguracoes;

        private ApiClient cliente;

        private ViewChart viewChart;

        private JPanel subpainelStatus;
        private JPanel subpainelCategoria;
        private JPanel subpainelEvolucao;

        private ChartPanel painelGraficoEvolucao;
        private ChartPanel painelGraficoStatus;
        private ChartPanel painelGraficoCategoria;

        private int linhaHoverAcoes = -1;
        private int botaoHoverAcoes = -1;

        private JPanel itemDashboard;
        private JPanel itemProjetos;
        private JPanel itemConfiguracoes;
        private JPanel itemMenuAtivo;

        private JLabel tituloConfiguracoes;
        private JLabel subtituloConfiguracoes;

        private JLabel tituloAparencia;
        private JLabel descricaoAparencia;

        private JLabel tituloIdioma;
        private JLabel descricaoIdioma;
        private JLabel textoTema;
        private JLabel textoIdioma;


        private JLabel tituloDashboard;
        private JLabel subtituloDashboard;
        private JLabel tituloFormulario;
        private JLabel tituloLista;

        private final Map<String, JLabel> rotulosCards = new java.util.HashMap<>();

        private final Map<String, List<JLabel>> rotulosCampos = new java.util.HashMap<>();

        // Cores atualizadas de acordo com o tema selecionado.
        private Color AZUL_ESCURO;
        private Color AZUL_SIDEBAR;
        private Color AZUL;

        private Color FUNDO;
        private Color BRANCO;
        private Color TEXTO;
        private Color CINZA;

        private Color SUPERFICIE;
        private Color SUPERFICIE_SECUNDARIA;
        private Color BORDA;
        private Color BARRA_SUPERIOR;
        private Color MENU_ATIVO;
        private Color MENU_HOVER;

        private void configurarCores() {

                if (temaClaro) {

                        AZUL_ESCURO = new Color(35, 45, 60);
                        AZUL_SIDEBAR = new Color(240, 243, 247);
                        AZUL = new Color(0, 153, 153);

                        FUNDO = new Color(247, 248, 250);
                        BRANCO = Color.WHITE;
                        TEXTO = new Color(45, 50, 60);
                        CINZA = new Color(105, 113, 125);

                        SUPERFICIE = Color.WHITE;
                        SUPERFICIE_SECUNDARIA = new Color(242, 244, 247);
                        BORDA = new Color(220, 224, 230);
                        BARRA_SUPERIOR = Color.WHITE;
                        MENU_ATIVO = new Color(218, 237, 237);
                        MENU_HOVER = new Color(229, 234, 240);

                } else {

                        AZUL_ESCURO = new Color(245, 245, 247);
                        AZUL_SIDEBAR = new Color(25, 25, 27);
                        AZUL = new Color(0, 153, 153);

                        FUNDO = new Color(28, 28, 30);
                        BRANCO = new Color(245, 245, 247);
                        TEXTO = new Color(229, 229, 231);
                        CINZA = new Color(161, 161, 166);

                        SUPERFICIE = new Color(36, 36, 38);
                        SUPERFICIE_SECUNDARIA = new Color(48, 48, 51);
                        BORDA = new Color(58, 58, 60);
                        BARRA_SUPERIOR = new Color(34, 34, 36);
                        MENU_ATIVO = new Color(53, 53, 56);
                        MENU_HOVER = new Color(41, 41, 44);
                }
        }

        private final Map<String, Map<String, String>> traducoes = Map.of(

                        "pt-BR", Map.ofEntries(
                                        Map.entry("usuario", "Usuário"),
                                        Map.entry("sair", "Sair"),
                                        Map.entry("projetos", "Projetos"),
                                        Map.entry("configuracoes", "Configurações"),
                                        Map.entry("dashboard", "Dashboard"),
                                        Map.entry("descricaoDashboard",
                                                        "Acompanhe os indicadores e o desempenho dos seus projetos."),
                                        Map.entry("totalProjetos", "Total de Projetos"),
                                        Map.entry("concluidos", "Concluídos"),
                                        Map.entry("emDesenvolvimento", "Em Desenvolvimento"),
                                        Map.entry("planejados", "Planejados"),
                                        Map.entry("status", "Projetos por status"),
                                        Map.entry("categoria", "Projetos por categoria"),
                                        Map.entry("evolucao", "Evolução mensal de projetos"),
                                        Map.entry("cadastro", "Cadastro de Projeto"),
                                        Map.entry("nomeProjeto", "Nome do projeto"),
                                        Map.entry("descricao", "Descrição"),
                                        Map.entry("categoriaLabel", "Categoria"),
                                        Map.entry("statusLabel", "Status"),
                                        Map.entry("prazo", "Prazo"),
                                        Map.entry("placeholderNome", "Digite o nome do projeto"),
                                        Map.entry("placeholderDescricao", "Descreva o projeto"),
                                        Map.entry("limpar", "Limpar"),
                                        Map.entry("cadastrar", "Cadastrar projeto"),
                                        Map.entry("lista", "Lista de Projetos"),
                                        Map.entry("buscar", "Buscar projeto..."),
                                        Map.entry("nome", "Nome"),
                                        Map.entry("dataCriacao", "Data de criação"),
                                        Map.entry("acoes", "Ações"),
                                        Map.entry("nenhumCadastrado", "Nenhum projeto cadastrado."),
                                        Map.entry("nenhumEncontrado", "Nenhum projeto encontrado."),
                                        Map.entry("carregando", "Carregando projetos..."),
                                        Map.entry("carregado", "Projetos carregados com sucesso."),
                                        Map.entry("aparencia", "Aparência"),
                                        Map.entry("descricaoAparencia", "Personalize a aparência do sistema."),
                                        Map.entry("tema", "Tema"),
                                        Map.entry("descricaoTema", "Escolha entre o modo claro e escuro."),
                                        Map.entry("escuro", "Escuro"),
                                        Map.entry("claro", "Claro"),
                                        Map.entry("idioma", "Idioma"),
                                        Map.entry("descricaoIdioma", "Defina o idioma utilizado na interface."),
                                        Map.entry("idiomaSistema", "Idioma do sistema"),
                                        Map.entry("selecionarIdioma", "Selecione seu idioma preferido."),
                                        Map.entry("editar", "Editar Projeto"),
                                        Map.entry("cancelar", "Cancelar"),
                                        Map.entry("salvar", "Salvar alterações"),
                                        Map.entry("rodape", "Projeto acadêmico de Análise e Desenvolvimento de Sistemas"),
                                        Map.entry("statusConcluido", "Concluído"),
                                        Map.entry("statusAndamento", "Em desenvolvimento"),
                                        Map.entry("statusPlanejado", "Planejado"),
                                        Map.entry("quantidadeProjetos", "Quantidade de projetos"),
                                        Map.entry("projetosCriados", "Projetos criados"),
                                        Map.entry("subtituloConfiguracoes", "Personalize a aparência e o idioma do sistema."),
                                        Map.entry("subtituloDashboard", "Gerencie e acompanhe seus projetos."),
                                        Map.entry("tituloSistema", "Sistema de Projetos")),

                        "en", Map.ofEntries(
                                        Map.entry("usuario", "User"),
                                        Map.entry("sair", "Log out"),
                                        Map.entry("projetos", "Projects"),
                                        Map.entry("configuracoes", "Settings"),
                                        Map.entry("dashboard", "Dashboard"),
                                        Map.entry("descricaoDashboard", "Track your projects' indicators and performance."),
                                        Map.entry("totalProjetos", "Total Projects"),
                                        Map.entry("concluidos", "Completed"),
                                        Map.entry("emDesenvolvimento", "In Progress"),
                                        Map.entry("planejados", "Planned"),
                                        Map.entry("status", "Projects by status"),
                                        Map.entry("categoria", "Projects by category"),
                                        Map.entry("evolucao", "Monthly Project Progress"),
                                        Map.entry("cadastro", "Project Registration"),
                                        Map.entry("nomeProjeto", "Project name"),
                                        Map.entry("descricao", "Description"),
                                        Map.entry("categoriaLabel", "Category"),
                                        Map.entry("statusLabel", "Status"),
                                        Map.entry("prazo", "Deadline"),
                                        Map.entry("placeholderNome", "Enter the project name"),
                                        Map.entry("placeholderDescricao", "Describe the project"),
                                        Map.entry("limpar", "Clear"),
                                        Map.entry("cadastrar", "Register project"),
                                        Map.entry("lista", "Project List"),
                                        Map.entry("buscar", "Search projects..."),
                                        Map.entry("nome", "Name"),
                                        Map.entry("dataCriacao", "Creation date"),
                                        Map.entry("acoes", "Actions"),
                                        Map.entry("nenhumCadastrado", "No projects registered."),
                                        Map.entry("nenhumEncontrado", "No projects found."),
                                        Map.entry("carregando", "Loading projects..."),
                                        Map.entry("carregado", "Projects loaded successfully."),
                                        Map.entry("aparencia", "Appearance"),
                                        Map.entry("descricaoAparencia", "Customize the system's appearance."),
                                        Map.entry("tema", "Theme"),
                                        Map.entry("descricaoTema", "Choose between light and dark mode."),
                                        Map.entry("escuro", "Dark"),
                                        Map.entry("claro", "Light"),
                                        Map.entry("idioma", "Language"),
                                        Map.entry("descricaoIdioma", "Set the language used in the interface."),
                                        Map.entry("idiomaSistema", "System language"),
                                        Map.entry("selecionarIdioma", "Choose your preferred language."),
                                        Map.entry("editar", "Edit Project"),
                                        Map.entry("cancelar", "Cancel"),
                                        Map.entry("salvar", "Save changes"),
                                        Map.entry("rodape", "Academic project in Systems Analysis and Development"),
                                        Map.entry("statusConcluido", "Completed"),
                                        Map.entry("statusAndamento", "In Progress"),
                                        Map.entry("statusPlanejado", "Planned"),
                                        Map.entry("quantidadeProjetos", "Number of projects"),
                                        Map.entry("projetosCriados", "Projects created"),
                                        Map.entry("subtituloConfiguracoes", "Customize the system's appearance and language."),
                                        Map.entry("subtituloDashboard", "Manage and track your projects."),
                                        Map.entry("tituloSistema", "Project Management System")));

        private String traduzir(String chave) {
                return traducoes.get(idiomaAtual).get(chave);
        }

        public TelaProjetos(ApiClient cliente) {

                setTitle(idiomaAtual.equals("en") ? "Project Management System" : "Sistema de Projetos");
                setSize(1200, 760);
                setMinimumSize(new Dimension(950, 650));
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                this.cliente = cliente;

                idiomaAtual = preferencias.get("idioma", "pt-BR");
                temaClaro = preferencias.get("tema", "dark").equals("light");

                configurarCores();

                viewChart = new ViewChart(temaClaro);

                criarComponentes();
                aplicarIdioma();
                criarEventos();
                carregarTabela();
        }

        private void recriarInterface() {

                configurarCores();
                viewChart = new ViewChart(temaClaro);

                criarComponentes();
                aplicarIdioma();
                criarEventos();
                carregarTabela();

                // Retorna à tela que estava aberta antes da alteração do tema.
                gerenciadorTelas.show(painelTelas, telaAtual);

                // Atualiza o item selecionado no menu lateral.
                switch (telaAtual) {

                        case "PROJETOS":
                                atualizarMenuAtivo(itemProjetos);
                                break;

                        case "CONFIGURACOES":
                                atualizarMenuAtivo(itemConfiguracoes);
                                break;

                        default:
                                atualizarMenuAtivo(itemDashboard);
                                break;
                }

                revalidate();
                repaint();
        }

        private void criarComponentes() {

                JPanel painelPrincipal = new JPanel(new BorderLayout());
                painelPrincipal.setBackground(FUNDO);

                // Barra superior
                JPanel barraSuperior = new JPanel(new BorderLayout());
                barraSuperior.setBackground(BARRA_SUPERIOR);
                barraSuperior.setBorder(
                                BorderFactory.createEmptyBorder(15, 25, 15, 25));

                tituloSistema = new JLabel("Sistema de Projetos");
                tituloSistema.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                tituloSistema.setForeground(AZUL_ESCURO);
                barraSuperior.add(tituloSistema, BorderLayout.WEST);

                // Identificação do usuário e botão de saída (mesma organização da web).
                String caminhoFoto = temaClaro
                                ? "frontend/img/dark-bear.gif"
                                : "frontend/img/light-bear.png";
                ImageIcon iconeUsuarioOriginal = new ImageIcon(caminhoFoto);
                Image iconeUsuarioImagem = iconeUsuarioOriginal.getImage()
                                .getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                JLabel iconeUsuario = new JLabel(new ImageIcon(iconeUsuarioImagem));

                rotuloUsuario = new JLabel(traduzir("usuario"));
                rotuloUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
                rotuloUsuario.setForeground(temaClaro ? CINZA : BRANCO);

                JLabel usuario = new JLabel(cliente.getNomeUsuario());
                usuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                usuario.setForeground(temaClaro ? TEXTO : BRANCO);

                JPanel textoUsuario = new JPanel();
                textoUsuario.setLayout(new BoxLayout(textoUsuario, BoxLayout.Y_AXIS));
                textoUsuario.setOpaque(false);
                textoUsuario.add(rotuloUsuario);
                textoUsuario.add(Box.createVerticalStrut(5));
                textoUsuario.add(usuario);

                JPanel informacoesUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
                informacoesUsuario.setOpaque(false);
                informacoesUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
                informacoesUsuario.setPreferredSize(new Dimension(180, 55));
                informacoesUsuario.setMaximumSize(new Dimension(180, 55));
                informacoesUsuario.add(iconeUsuario);
                informacoesUsuario.add(textoUsuario);

                botaoSair = new JButton("Sair");
                String caminhoIconeSair = temaClaro
                                ? "frontend/img/icone-sair-dark-bear.gif"
                                : "frontend/img/icone-sair-light-bear.gif";
                ImageIcon iconeSair = new ImageIcon(caminhoIconeSair);
                Image imagemSair = iconeSair.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
                botaoSair.setIcon(new ImageIcon(imagemSair));
                botaoSair.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                botaoSair.setForeground(temaClaro ? TEXTO : BRANCO);
                botaoSair.setOpaque(false);
                botaoSair.setBorderPainted(false);
                botaoSair.setFocusPainted(false);
                botaoSair.setContentAreaFilled(false);
                botaoSair.setHorizontalAlignment(SwingConstants.LEFT);
                botaoSair.setIconTextGap(12);
                botaoSair.setMargin(new Insets(6, 2, 6, 2));
                botaoSair.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Menu lateral
                JPanel sidebar = new JPanel();
                sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
                sidebar.setBackground(AZUL_SIDEBAR);
                sidebar.setPreferredSize(new Dimension(260, 0));
                sidebar.setBorder(
                                BorderFactory.createEmptyBorder(25, 22, 20, 15));

                ImageIcon imagemOriginal = new ImageIcon("frontend/img/logoTitle.png");

                int largura = 180;
                int altura = imagemOriginal.getIconHeight() * largura
                                / imagemOriginal.getIconWidth();

                Image imagemRedimensionada = imagemOriginal.getImage()
                                .getScaledInstance(largura, altura, Image.SCALE_SMOOTH);

                JLabel marca = new JLabel(new ImageIcon(imagemRedimensionada));
                marca.setAlignmentX(LEFT_ALIGNMENT);
                marca.setMaximumSize(new Dimension(230, altura));

                sidebar.add(marca);
                sidebar.add(Box.createVerticalStrut(25));

                itemDashboard = criarItemMenu("Dashboard", true);
                itemProjetos = criarItemMenu("Projetos", false);
                itemConfiguracoes = criarItemMenu("Configurações", false);

                itemMenuAtivo = itemDashboard;

                sidebar.add(itemDashboard);
                sidebar.add(itemProjetos);
                sidebar.add(itemConfiguracoes);

                sidebar.add(Box.createVerticalGlue());

                JPanel acoesUsuario = new JPanel();
                acoesUsuario.setLayout(new BoxLayout(acoesUsuario, BoxLayout.Y_AXIS));
                acoesUsuario.setOpaque(false);
                acoesUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
                acoesUsuario.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(1, 0, 0, 0, temaClaro ? BORDA : new Color(65, 65, 68)),
                                BorderFactory.createEmptyBorder(18, 0, 0, 0)));
                informacoesUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
                botaoSair.setAlignmentX(Component.LEFT_ALIGNMENT);
                botaoSair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
                acoesUsuario.add(informacoesUsuario);
                acoesUsuario.add(Box.createVerticalStrut(16));
                acoesUsuario.add(botaoSair);
                sidebar.add(acoesUsuario);

                // Gerenciador de telas
                gerenciadorTelas = new CardLayout();

                painelTelas = new JPanel(gerenciadorTelas) {
                        private final Image fundo = new ImageIcon(
                                        temaClaro ? "frontend/img/pattern-background-light.png"
                                                        : "frontend/img/pattern-background.png")
                                        .getImage();

                        @Override
                        protected void paintComponent(Graphics g) {
                                super.paintComponent(g);
                                if (fundo != null) {
                                        g.drawImage(fundo, 0, 0, this);
                                }
                        }
                };
                painelTelas.setBackground(FUNDO);

                // Tela Dashboard
                JPanel telaDashboard = new JPanel();
                telaDashboard.setLayout(
                                new BoxLayout(telaDashboard, BoxLayout.Y_AXIS));
                telaDashboard.setOpaque(false);
                telaDashboard.setBorder(
                                BorderFactory.createEmptyBorder(25, 25, 25, 25));

                // Tela Projetos
                JPanel telaProjetos = new JPanel();
                telaProjetos.setLayout(
                                new BoxLayout(telaProjetos, BoxLayout.Y_AXIS));
                telaProjetos.setOpaque(false);
                telaProjetos.setBorder(
                                BorderFactory.createEmptyBorder(25, 25, 25, 25));

                // Tela Configurações
                JPanel telaConfiguracoes = new JPanel();
                telaConfiguracoes.setLayout(
                                new BoxLayout(telaConfiguracoes, BoxLayout.Y_AXIS));
                telaConfiguracoes.setOpaque(false);
                telaConfiguracoes.setBorder(
                                BorderFactory.createEmptyBorder(25, 25, 25, 25));

                criarTelaConfiguracoes(telaConfiguracoes);

                tituloDashboard = new JLabel("Dashboard");
                tituloDashboard.setFont(new Font("Segoe UI", Font.PLAIN, 25));
                tituloDashboard.setForeground(AZUL_ESCURO);
                tituloDashboard.setAlignmentX(LEFT_ALIGNMENT);

                subtituloDashboard = new JLabel(
                                "Gerencie e acompanhe seus projetos.");
                subtituloDashboard.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                subtituloDashboard.setForeground(CINZA);
                subtituloDashboard.setAlignmentX(LEFT_ALIGNMENT);

                telaDashboard.add(tituloDashboard);
                telaDashboard.add(Box.createVerticalStrut(5));
                telaDashboard.add(subtituloDashboard);
                telaDashboard.add(Box.createVerticalStrut(22));

                // Cards estatísticos
                JPanel painelCards = new JPanel(new GridLayout(1, 4, 15, 0));
                painelCards.setBackground(FUNDO);
                painelCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
                painelCards.setAlignmentX(LEFT_ALIGNMENT);

                valorTotal = new JLabel("0");
                valorConcluidos = new JLabel("0");
                valorAndamento = new JLabel("0");
                valorPlanejados = new JLabel("0");

                painelCards.add(
                                criarCard(
                                                "totalProjetos",
                                                valorTotal,
                                                AZUL_ESCURO,
                                                "frontend/img/card-total-projetos.png"));

                painelCards.add(
                                criarCard(
                                                "concluidos",
                                                valorConcluidos,
                                                new Color(67, 124, 30),
                                                "frontend/img/card-concluidos.png"));

                painelCards.add(
                                criarCard(
                                                "emDesenvolvimento",
                                                valorAndamento,
                                                new Color(0, 166, 166),
                                                "frontend/img/card-em-desenvolvimento.png"));

                painelCards.add(
                                criarCard(
                                                "planejados",
                                                valorPlanejados,
                                                new Color(240, 91, 103),
                                                "frontend/img/card-planejados.png"));

                telaDashboard.add(painelCards);
                telaDashboard.add(Box.createVerticalStrut(22));

                // Gráfico de status
                var dadosStatus = viewChart.criarDadosStatus(List.of());

                var graficoStatus = viewChart.criarGraficoStatus(
                                dadosStatus);

                painelGraficoStatus = viewChart.criarPainelGrafico(
                                graficoStatus);

                // Gráfico de categorias
                var dadosCategoria = viewChart.criarDadosCategoria(List.of());

                var graficoCategoria = viewChart.criarGraficoCategoria(
                                dadosCategoria);

                painelGraficoCategoria = viewChart.criarPainelGrafico(
                                graficoCategoria);

                // Gráfico de evolução mensal
                var dadosEvolucao = viewChart.criarDadosEvolucao(List.of());

                var graficoEvolucao = viewChart.criarGraficoEvolucao(
                                dadosEvolucao);

                painelGraficoEvolucao = viewChart.criarPainelGrafico(
                                graficoEvolucao);

                // Subpainel do gráfico de status
                subpainelStatus = criarPainelComBorda(
                                new BorderLayout(),
                                SUPERFICIE,
                                BORDA,
                                8);

                subpainelStatus.add(painelGraficoStatus, BorderLayout.CENTER);

                // Subpainel do gráfico de categorias
                subpainelCategoria = criarPainelComBorda(
                                new BorderLayout(),
                                SUPERFICIE,
                                BORDA,
                                8);

                subpainelCategoria.add(painelGraficoCategoria, BorderLayout.CENTER);

                // Subpainel do gráfico de evolução
                subpainelEvolucao = criarPainelComBorda(
                                new BorderLayout(),
                                SUPERFICIE,
                                BORDA,
                                8);

                subpainelEvolucao.add(
                                painelGraficoEvolucao,
                                BorderLayout.CENTER);

                // Primeira linha: gráficos de status e categoria
                JPanel linhaGraficos = new JPanel(
                                new GridLayout(1, 2, 15, 0));

                linhaGraficos.setBackground(FUNDO);
                linhaGraficos.add(subpainelStatus);
                linhaGraficos.add(subpainelCategoria);

                // Painel geral dos gráficos
                JPanel painelGraficos = new JPanel();

                painelGraficos.setLayout(
                                new BoxLayout(painelGraficos, BoxLayout.Y_AXIS));

                painelGraficos.setBackground(FUNDO);
                painelGraficos.setAlignmentX(LEFT_ALIGNMENT);

                linhaGraficos.setMaximumSize(
                                new Dimension(Integer.MAX_VALUE, 340));

                painelGraficos.add(linhaGraficos);
                painelGraficos.add(Box.createVerticalStrut(15));

                subpainelEvolucao.setMaximumSize(
                                new Dimension(Integer.MAX_VALUE, 300));

                painelGraficos.add(subpainelEvolucao);

                telaDashboard.add(painelGraficos);
                telaDashboard.add(Box.createVerticalStrut(22));

                // Formulário de cadastro
                JPanel painelFormulario = criarPainelComBorda(
                                new BorderLayout(10, 15),
                                SUPERFICIE,
                                BORDA,
                                20);

                painelFormulario.setAlignmentX(LEFT_ALIGNMENT);
                painelFormulario.setMaximumSize(
                                new Dimension(Integer.MAX_VALUE, 330));

                tituloFormulario = new JLabel("Cadastro de Projeto");
                tituloFormulario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                tituloFormulario.setForeground(AZUL_ESCURO);

                campoNome = new JTextField();
                campoDescricao = new JTextField();

                DatePickerSettings configuracoes = new DatePickerSettings();
                configuracoes.setFormatForDatesCommonEra("dd-MM-yyyy");
                campoPrazo = new DatePicker(configuracoes);

                comboCategoria = new JComboBox<>(new String[] {
                                "Web", "Software", "Mobile", "Desktop"
                });

                comboStatus = new JComboBox<>(new String[] {
                                "Planejado", "Em desenvolvimento", "Concluído"
                });

                estilizarCampo(campoNome);
                estilizarCampo(campoDescricao);
                estilizarCombo(comboCategoria);
                estilizarCombo(comboStatus);
                estilizarCampo(campoPrazo.getComponentDateTextField());
                campoPrazo.setPreferredSize(new Dimension(220, 34));

                JPanel campos = new JPanel(new GridLayout(3, 2, 15, 12));
                campos.setBackground(SUPERFICIE);

                campos.add(criarCampoComRotulo("nomeProjeto", campoNome));
                campos.add(criarCampoComRotulo("descricao", campoDescricao));
                campos.add(criarCampoComRotulo("categoriaLabel", comboCategoria));
                campos.add(criarCampoComRotulo("statusLabel", comboStatus));
                campos.add(criarCampoComRotulo("prazo", campoPrazo));

                botaoCadastrar = criarBotao(
                                "Cadastrar projeto",
                                new Color(48, 54, 58),
                                BRANCO);

                botaoLimpar = criarBotao(
                                "Limpar",
                                new Color(112, 120, 126),
                                BRANCO);

                JPanel botoesFormulario = new JPanel(
                                new FlowLayout(FlowLayout.RIGHT, 10, 0));

                botoesFormulario.setBackground(SUPERFICIE);
                botoesFormulario.add(botaoLimpar);
                botoesFormulario.add(botaoCadastrar);

                JPanel centroFormulario = new JPanel(new BorderLayout(0, 15));
                centroFormulario.setBackground(SUPERFICIE);
                centroFormulario.add(campos, BorderLayout.CENTER);
                centroFormulario.add(botoesFormulario, BorderLayout.SOUTH);

                painelFormulario.add(tituloFormulario, BorderLayout.NORTH);
                painelFormulario.add(centroFormulario, BorderLayout.CENTER);

                telaProjetos.add(painelFormulario);
                telaProjetos.add(Box.createVerticalStrut(22));

                // Lista de projetos
                painelLista = criarPainelComBorda(
                                new BorderLayout(0, 15),
                                SUPERFICIE,
                                BORDA,
                                20);

                painelLista.setAlignmentX(LEFT_ALIGNMENT);

                tituloLista = new JLabel("Lista de Projetos");
                tituloLista.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                tituloLista.setForeground(AZUL_ESCURO);

                campoBusca = new JTextField(18);
                campoBusca.setPreferredSize(new Dimension(220, 34));
                estilizarCampo(campoBusca);

                botaoBuscar = criarBotao("Buscar", AZUL, BRANCO);

                JPanel barraLista = new JPanel(new BorderLayout());
                barraLista.setBackground(SUPERFICIE);
                barraLista.add(tituloLista, BorderLayout.WEST);

                JPanel controlesBusca = new JPanel(
                                new FlowLayout(FlowLayout.RIGHT, 8, 0));

                controlesBusca.setBackground(SUPERFICIE);
                controlesBusca.add(campoBusca);
                controlesBusca.add(botaoBuscar);

                barraLista.add(controlesBusca, BorderLayout.EAST);

                // Modelo da tabela
                modelo = new DefaultTableModel(
                                new Object[] {
                                                "ID",
                                                traduzir("nome"),
                                                traduzir("categoriaLabel"),
                                                traduzir("statusLabel"),
                                                traduzir("dataCriacao"),
                                                traduzir("prazo"),
                                                traduzir("acoes")
                                },
                                0) {
                        @Override
                        public boolean isCellEditable(int linha, int coluna) {
                                return coluna == 6;
                        }
                };

                tabela = new JTable(modelo);

                tabela.getTableHeader().setReorderingAllowed(false);
                tabela.getTableHeader().setResizingAllowed(false);

                tabela.setRowHeight(38);
                tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                tabela.setForeground(TEXTO);
                tabela.setBackground(SUPERFICIE);
                tabela.setGridColor(BORDA);
                tabela.setShowVerticalLines(true);
                tabela.setShowHorizontalLines(true);
                tabela.setSelectionBackground(
                                temaClaro
                                                ? new Color(218, 237, 237)
                                                : new Color(53, 53, 56));
                tabela.setSelectionForeground(TEXTO);
                tabela.setFillsViewportHeight(true);

                tabela.getTableHeader().setFont(
                                new Font("Segoe UI", Font.PLAIN, 13));

                tabela.getTableHeader().setBackground(SUPERFICIE_SECUNDARIA);
                tabela.getTableHeader().setForeground(TEXTO);
                tabela.getTableHeader().setPreferredSize(new Dimension(0, 38));

                DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();

                centralizar.setHorizontalAlignment(SwingConstants.CENTER);
                centralizar.setBackground(SUPERFICIE);
                centralizar.setForeground(TEXTO);

                tabela.getColumnModel().getColumn(0).setCellRenderer(centralizar);

                tabela.getColumnModel().getColumn(6).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(6).setMaxWidth(100);

                tabela.getColumnModel().getColumn(6).setCellRenderer(
                                new ActionCellRenderer());

                tabela.getColumnModel().getColumn(6).setCellEditor(
                                new ActionCellEditor());

                // Detecta o mouse sobre os botões da coluna Ações.
                tabela.addMouseMotionListener(
                                new java.awt.event.MouseMotionAdapter() {

                                        @Override
                                        public void mouseMoved(java.awt.event.MouseEvent e) {

                                                int linha = tabela.rowAtPoint(e.getPoint());
                                                int coluna = tabela.columnAtPoint(e.getPoint());

                                                int novoBotaoHover = -1;

                                                if (linha >= 0 && coluna == 6) {

                                                        java.awt.Rectangle celula = tabela.getCellRect(linha, coluna,
                                                                        true);

                                                        int posicaoX = e.getX() - celula.x;

                                                        if (posicaoX < celula.width / 2) {
                                                                novoBotaoHover = 0;
                                                        } else {
                                                                novoBotaoHover = 1;
                                                        }
                                                }

                                                if (linhaHoverAcoes != linha
                                                                || botaoHoverAcoes != novoBotaoHover) {

                                                        linhaHoverAcoes = linha;
                                                        botaoHoverAcoes = novoBotaoHover;

                                                        tabela.repaint();
                                                }
                                        }
                                });

                tabela.addMouseListener(
                                new java.awt.event.MouseAdapter() {

                                        @Override
                                        public void mouseExited(java.awt.event.MouseEvent e) {

                                                linhaHoverAcoes = -1;
                                                botaoHoverAcoes = -1;

                                                tabela.repaint();
                                        }
                                });

                tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(130);
                tabela.getColumnModel().getColumn(4).setPreferredWidth(130);
                tabela.getColumnModel().getColumn(5).setPreferredWidth(110);
                tabela.getColumnModel().getColumn(6).setPreferredWidth(90);

                scrollTabela = new JScrollPane(tabela);

                scrollTabela.setVerticalScrollBarPolicy(
                                JScrollPane.VERTICAL_SCROLLBAR_NEVER);

                scrollTabela.getViewport().setBackground(SUPERFICIE);

                painelPaginacao = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
                painelPaginacao.setBackground(SUPERFICIE);
                atualizarControlesPaginacao();

                JPanel conteudoLista = new JPanel(new BorderLayout(0, 12));
                conteudoLista.setOpaque(false);
                conteudoLista.add(scrollTabela, BorderLayout.CENTER);
                conteudoLista.add(painelPaginacao, BorderLayout.SOUTH);
                painelLista.add(barraLista, BorderLayout.NORTH);
                painelLista.add(conteudoLista, BorderLayout.CENTER);

                telaProjetos.add(painelLista);

                // Adiciona as telas ao gerenciador.
                painelTelas.add(telaDashboard, "DASHBOARD");

                // Permite rolar a tela de projetos verticalmente.
                JScrollPane scrollTelaProjetos = new JScrollPane(telaProjetos);

                scrollTelaProjetos.setBorder(null);
                scrollTelaProjetos.setOpaque(false);
                scrollTelaProjetos.getViewport().setOpaque(false);
                scrollTelaProjetos.getVerticalScrollBar().setUnitIncrement(16);

                painelTelas.add(scrollTelaProjetos, "PROJETOS");

                painelTelas.add(telaConfiguracoes, "CONFIGURACOES");

                gerenciadorTelas.show(painelTelas, "DASHBOARD");

                // Navegação entre telas.
                adicionarEventoMenu(itemDashboard, "DASHBOARD");
                adicionarEventoMenu(itemProjetos, "PROJETOS");
                adicionarEventoMenu(itemConfiguracoes, "CONFIGURACOES");

                // Área direita: barra superior + conteúdo.
                JPanel painelDireito = new JPanel(new BorderLayout());
                painelDireito.setBackground(FUNDO);

                painelDireito.add(barraSuperior, BorderLayout.NORTH);
                painelDireito.add(painelTelas, BorderLayout.CENTER);

                painelPrincipal.add(sidebar, BorderLayout.WEST);
                painelPrincipal.add(painelDireito, BorderLayout.CENTER);

                setContentPane(painelPrincipal);
        }

        private void criarTelaConfiguracoes(JPanel tela) {

                // Introdução da tela
                tituloConfiguracoes = new JLabel("Configurações");
                tituloConfiguracoes.setFont(new Font("Segoe UI", Font.PLAIN, 25));
                tituloConfiguracoes.setForeground(AZUL_ESCURO);
                tituloConfiguracoes.setAlignmentX(LEFT_ALIGNMENT);

                subtituloConfiguracoes = new JLabel(
                                "Personalize a aparência e o idioma do sistema.");
                subtituloConfiguracoes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                subtituloConfiguracoes.setForeground(CINZA);
                subtituloConfiguracoes.setAlignmentX(LEFT_ALIGNMENT);

                tela.add(tituloConfiguracoes);
                tela.add(Box.createVerticalStrut(5));
                tela.add(subtituloConfiguracoes);
                tela.add(Box.createVerticalStrut(25));

                // Painel de aparência
                JPanel painelAparencia = criarPainelComBorda(
                                new BorderLayout(0, 20),
                                SUPERFICIE,
                                BORDA,
                                20);

                painelAparencia.setAlignmentX(LEFT_ALIGNMENT);
                painelAparencia.setMaximumSize(
                                new Dimension(Integer.MAX_VALUE, 145));

                tituloAparencia = new JLabel("Aparência");
                tituloAparencia.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                tituloAparencia.setForeground(AZUL_ESCURO);

                descricaoAparencia = new JLabel(
                                "Personalize a aparência do sistema.");
                descricaoAparencia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                descricaoAparencia.setForeground(CINZA);

                JPanel cabecalhoAparencia = new JPanel(
                                new BorderLayout(0, 5));
                cabecalhoAparencia.setBackground(SUPERFICIE);
                cabecalhoAparencia.add(tituloAparencia, BorderLayout.NORTH);
                cabecalhoAparencia.add(descricaoAparencia, BorderLayout.CENTER);

                textoTema = new JLabel(
                                "<html><b>Tema</b><br>"
                                                + "<font color='#A1A1A6'>"
                                                + "Escolha entre o modo claro e escuro."
                                                + "</font></html>");
                textoTema.setForeground(TEXTO);
                textoTema.setFont(FONTE_NORMAL);

                comboTema = new JComboBox<>(new String[] {
                                traduzir("escuro"), traduzir("claro")
                });

                comboTema.setSelectedItem(
                                temaClaro ? traduzir("claro") : traduzir("escuro"));

                estilizarCombo(comboTema);

                JPanel opcaoTema = new JPanel(new BorderLayout(15, 0));
                opcaoTema.setBackground(SUPERFICIE);
                opcaoTema.add(textoTema, BorderLayout.CENTER);
                opcaoTema.add(comboTema, BorderLayout.EAST);

                painelAparencia.add(cabecalhoAparencia, BorderLayout.NORTH);
                painelAparencia.add(opcaoTema, BorderLayout.CENTER);

                // Painel de idioma

                JPanel painelIdioma = criarPainelComBorda(
                                new BorderLayout(0, 20),
                                SUPERFICIE,
                                BORDA,
                                20);

                painelIdioma.setAlignmentX(LEFT_ALIGNMENT);
                painelIdioma.setMaximumSize(
                                new Dimension(Integer.MAX_VALUE, 145));

                tituloIdioma = new JLabel("Idioma");
                tituloIdioma.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                tituloIdioma.setForeground(AZUL_ESCURO);

                descricaoIdioma = new JLabel(
                                "Defina o idioma utilizado na interface.");
                descricaoIdioma.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                descricaoIdioma.setForeground(CINZA);

                JPanel cabecalhoIdioma = new JPanel(
                                new BorderLayout(0, 5));
                cabecalhoIdioma.setBackground(SUPERFICIE);
                cabecalhoIdioma.add(tituloIdioma, BorderLayout.NORTH);
                cabecalhoIdioma.add(descricaoIdioma, BorderLayout.CENTER);

                textoIdioma = new JLabel(
                                "<html><b>Idioma do sistema</b><br>"
                                                + "<font color='#A1A1A6'>"
                                                + "Selecione seu idioma preferido."
                                                + "</font></html>");
                textoIdioma.setForeground(TEXTO);
                textoIdioma.setFont(FONTE_NORMAL);

                comboIdioma = new JComboBox<>(new String[] {
                                "Português (Brasil)", "English"
                });

                comboIdioma.setSelectedItem(
                                preferencias.get("idioma", "pt-BR").equals("en")
                                                ? "English"
                                                : "Português (Brasil)");

                estilizarCombo(comboIdioma);

                JPanel opcaoIdioma = new JPanel(new BorderLayout(15, 0));
                opcaoIdioma.setBackground(SUPERFICIE);
                opcaoIdioma.add(textoIdioma, BorderLayout.CENTER);
                opcaoIdioma.add(comboIdioma, BorderLayout.EAST);

                painelIdioma.add(cabecalhoIdioma, BorderLayout.NORTH);
                painelIdioma.add(opcaoIdioma, BorderLayout.CENTER);

                // Adiciona os dois painéis à tela
                tela.add(painelAparencia);
                tela.add(Box.createVerticalStrut(18));
                tela.add(painelIdioma);

                // Salva e aplica o tema selecionado.
                comboTema.addActionListener(e -> {

                        if (atualizandoIdioma) {
                                return;
                        }

                        Object itemSelecionado = comboTema.getSelectedItem();

                        if (itemSelecionado == null) {
                                return;
                        }

                        boolean novoTemaClaro = itemSelecionado.equals(traduzir("claro"));

                        if (temaClaro != novoTemaClaro) {

                                temaClaro = novoTemaClaro;

                                preferencias.put(
                                                "tema",
                                                temaClaro ? "light" : "dark");

                                aplicarLookAndFeel();
                                recriarInterface();
                        }
                });
        }

        private void aplicarLookAndFeel() {

                try {

                        if (temaClaro) {
                                UIManager.setLookAndFeel(new FlatLightLaf());
                        } else {
                                UIManager.setLookAndFeel(new FlatDarkLaf());
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void aplicarIdioma() {

                // Settings

                setTitle(traduzir("tituloSistema"));
                tituloSistema.setText(traduzir("tituloSistema"));

                tituloConfiguracoes.setText(traduzir("configuracoes"));
                subtituloConfiguracoes.setText(traduzir("subtituloConfiguracoes"));

                tituloAparencia.setText(traduzir("aparencia"));
                descricaoAparencia.setText(traduzir("descricaoAparencia"));

                tituloIdioma.setText(traduzir("idioma"));
                descricaoIdioma.setText(traduzir("descricaoIdioma"));

                textoTema.setText(
                                "<html><b>" + traduzir("tema") + "</b><br>"
                                                + "<font color='#A1A1A6'>"
                                                + traduzir("descricaoTema")
                                                + "</font></html>");

                textoIdioma.setText(
                                "<html><b>" + traduzir("idiomaSistema") + "</b><br>"
                                                + "<font color='#A1A1A6'>"
                                                + traduzir("selecionarIdioma")
                                                + "</font></html>");

                // Atualiza as opções do tema conforme o idioma.
                atualizandoIdioma = true;

                comboTema.removeAllItems();
                comboTema.addItem(traduzir("escuro"));
                comboTema.addItem(traduzir("claro"));

                comboTema.setSelectedItem(
                                temaClaro ? traduzir("claro") : traduzir("escuro"));

                atualizandoIdioma = false;

                // Sidebar
                rotuloDashboard.setText(traduzir("dashboard"));
                rotuloProjetos.setText(traduzir("projetos"));
                rotuloConfiguracoes.setText(traduzir("configuracoes"));
                botaoSair.setText(traduzir("sair"));
                rotuloUsuario.setText(traduzir("usuario"));

                // Dashboard
                tituloDashboard.setText(traduzir("dashboard"));
                subtituloDashboard.setText(traduzir("subtituloDashboard"));

                // Formulário e lista
                tituloFormulario.setText(traduzir("cadastro"));
                tituloLista.setText(traduzir("lista"));

                botaoCadastrar.setText(traduzir("cadastrar"));
                botaoLimpar.setText(traduzir("limpar"));
                botaoBuscar.setText(traduzir("buscar"));

                campoBusca.setToolTipText(traduzir("buscar"));
                campoNome.setToolTipText(traduzir("placeholderNome"));
                campoDescricao.setToolTipText(traduzir("placeholderDescricao"));

                // Cards
                for (Map.Entry<String, JLabel> entrada : rotulosCards.entrySet()) {
                        entrada.getValue().setText(traduzir(entrada.getKey()));
                }

                // Rótulos dos campos
                for (Map.Entry<String, List<JLabel>> entrada : rotulosCampos.entrySet()) {
                        for (JLabel label : entrada.getValue()) {
                                label.setText(traduzir(entrada.getKey()));
                        }
                }

                // Cabeçalhos da tabela
                String[] cabecalhos = {
                                "ID",
                                traduzir("nome"),
                                traduzir("categoriaLabel"),
                                traduzir("statusLabel"),
                                traduzir("dataCriacao"),
                                traduzir("prazo"),
                                traduzir("acoes")
                };

                for (int i = 0; i < cabecalhos.length; i++) {
                        tabela.getColumnModel().getColumn(i).setHeaderValue(cabecalhos[i]);
                }

                tabela.getTableHeader().repaint();

                tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(130);
                tabela.getColumnModel().getColumn(4).setPreferredWidth(130);
                tabela.getColumnModel().getColumn(5).setPreferredWidth(110);
                tabela.getColumnModel().getColumn(6).setPreferredWidth(90);
        }

        private JPanel criarPainelComBorda(
                        java.awt.LayoutManager layout,
                        Color fundo,
                        Color borda,
                        int padding) {

                JPanel painel = new JPanel(layout);
                painel.setBackground(fundo);

                painel.setBorder(
                                BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(borda),
                                                BorderFactory.createEmptyBorder(
                                                                padding,
                                                                padding,
                                                                padding,
                                                                padding)));

                return painel;
        }

        // Cria um item do menu lateral.
        private JPanel criarItemMenu(String texto, boolean selecionado) {

                JPanel item = new JPanel(new BorderLayout());

                item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                item.setPreferredSize(new Dimension(180, 42));

                item.setBackground(
                                selecionado ? MENU_ATIVO : AZUL_SIDEBAR);

                item.setBorder(
                                BorderFactory.createEmptyBorder(0, 12, 0, 8));

                JLabel rotulo = new JLabel(texto);

                if (texto.equals("Dashboard")) {
                        rotuloDashboard = rotulo;
                } else if (texto.equals("Projetos")) {
                        rotuloProjetos = rotulo;
                } else if (texto.equals("Configurações")) {
                        rotuloConfiguracoes = rotulo;
                }

                rotulo.setFont(new Font(
                                "Segoe UI",
                                selecionado ? Font.BOLD : Font.PLAIN,
                                14));

                rotulo.setForeground(
                                selecionado
                                                ? (temaClaro ? AZUL_ESCURO : BRANCO)
                                                : CINZA);

                item.add(rotulo, BorderLayout.CENTER);
                item.setAlignmentX(LEFT_ALIGNMENT);

                item.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseEntered(MouseEvent e) {

                                if (item != itemMenuAtivo) {
                                        item.setBackground(MENU_HOVER);
                                }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                                if (item != itemMenuAtivo) {
                                        item.setBackground(AZUL_SIDEBAR);
                                }
                        }
                });

                return item;
        }

        // Permite clicar tanto no painel quanto no texto do menu.
        private void adicionarEventoMenu(JPanel item, String tela) {

                MouseAdapter evento = new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {

                                telaAtual = tela;

                                gerenciadorTelas.show(painelTelas, tela);
                                atualizarMenuAtivo(item);
                        }
                };

                item.addMouseListener(evento);

                for (Component componente : item.getComponents()) {
                        componente.addMouseListener(evento);
                }
        }

        // Atualiza o fundo e o texto do item selecionado.
        private void atualizarMenuAtivo(JPanel itemSelecionado) {

                JPanel[] itens = {
                                itemDashboard,
                                itemProjetos,
                                itemConfiguracoes
                };

                for (JPanel item : itens) {

                        boolean selecionado = item == itemSelecionado;

                        item.setBackground(
                                        selecionado ? MENU_ATIVO : AZUL_SIDEBAR);

                        JLabel rotulo = (JLabel) item.getComponent(0);

                        rotulo.setForeground(
                                        selecionado
                                                        ? (temaClaro ? AZUL_ESCURO : BRANCO)
                                                        : CINZA);

                        rotulo.setFont(new Font(
                                        "Segoe UI",
                                        selecionado ? Font.BOLD : Font.PLAIN,
                                        14));
                }

                itemMenuAtivo = itemSelecionado;
        }

        private JPanel criarCard(
                        String titulo,
                        JLabel valor,
                        Color cor,
                        String caminhoIcone) {

                JPanel card = criarPainelComBorda(
                                new BorderLayout(12, 0),
                                SUPERFICIE,
                                BORDA,
                                12);

                JLabel icone = new JLabel(carregarIconeCard(caminhoIcone));

                icone.setPreferredSize(new Dimension(44, 44));
                icone.setHorizontalAlignment(SwingConstants.CENTER);
                icone.setVerticalAlignment(SwingConstants.CENTER);

                JPanel conteudo = new JPanel();

                conteudo.setLayout(
                                new BoxLayout(conteudo, BoxLayout.Y_AXIS));

                conteudo.setBackground(SUPERFICIE);

                valor.setFont(new Font("Segoe UI", Font.PLAIN, 24));
                valor.setForeground(cor);

                JLabel rotulo = new JLabel(traduzir(titulo));
                rotulosCards.put(titulo, rotulo);

                rotulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                rotulo.setForeground(CINZA);

                conteudo.add(valor);
                conteudo.add(Box.createVerticalStrut(2));
                conteudo.add(rotulo);

                card.add(icone, BorderLayout.WEST);
                card.add(conteudo, BorderLayout.CENTER);

                return card;
        }

        private ImageIcon carregarIconeCard(String caminho) {

                Image imagem = new ImageIcon(caminho)
                                .getImage()
                                .getScaledInstance(44, 44, Image.SCALE_SMOOTH);

                return new ImageIcon(imagem);
        }

        private JPanel criarCampoComRotulo(
                        String chave,
                        Component campo) {

                JPanel painel = new JPanel(new BorderLayout(0, 6));
                painel.setBackground(SUPERFICIE);

                JLabel label = new JLabel(traduzir(chave));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                label.setForeground(TEXTO);

                rotulosCampos
                                .computeIfAbsent(chave, k -> new java.util.ArrayList<>())
                                .add(label);

                painel.add(label, BorderLayout.NORTH);
                painel.add(campo, BorderLayout.CENTER);

                return painel;
        }

        private JButton criarBotao(
                        String texto,
                        Color fundo,
                        Color frente) {

                JButton botao = new JButton(texto);

                botao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                botao.setBackground(fundo);
                botao.setForeground(frente);
                botao.setFocusPainted(false);
                botao.setBorderPainted(false);
                botao.setOpaque(true);
                botao.setMargin(new Insets(10, 15, 10, 15));

                botao.setCursor(new java.awt.Cursor(
                                java.awt.Cursor.HAND_CURSOR));

                return botao;
        }

        private void estilizarCampo(JTextField campo) {

                campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                campo.setForeground(TEXTO);
                campo.setBackground(SUPERFICIE_SECUNDARIA);
                campo.setCaretColor(temaClaro ? AZUL_ESCURO : BRANCO);
                campo.setPreferredSize(new Dimension(200, 34));

                campo.setBorder(
                                BorderFactory.createEmptyBorder(5, 8, 5, 8));
        }

        private void estilizarCombo(JComboBox<String> combo) {

                combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                combo.setForeground(TEXTO);
                combo.setBackground(SUPERFICIE_SECUNDARIA);
                combo.setPreferredSize(new Dimension(200, 34));
        }

        private void criarEventos() {

                botaoLimpar.addActionListener(e -> limparFormulario());
                botaoCadastrar.addActionListener(e -> cadastrar());
                botaoBuscar.addActionListener(e -> buscarProjetos());

                botaoSair.addActionListener(e -> {

                        int resposta = JOptionPane.showConfirmDialog(
                                        this,
                                        "Deseja realmente sair?",
                                        "Sair",
                                        JOptionPane.YES_NO_OPTION);

                        if (resposta == JOptionPane.YES_OPTION) {

                                cliente.limparToken();

                                TelaLogin tela = new TelaLogin();
                                tela.setVisible(true);

                                dispose();
                        }
                });

                comboIdioma.addActionListener(e -> {

                        String idioma = comboIdioma.getSelectedItem().equals("English")
                                        ? "en"
                                        : "pt-BR";

                        idiomaAtual = idioma;
                        preferencias.put("idioma", idioma);

                        aplicarIdioma();
                });
        }

        private void limparFormulario() {

                campoNome.setText("");
                campoDescricao.setText("");
                campoPrazo.clear();

                comboCategoria.setSelectedIndex(0);
                comboStatus.setSelectedIndex(0);

                campoNome.requestFocus();
        }

        private void cadastrar() {

                String nome = campoNome.getText().trim();
                String descricao = campoDescricao.getText().trim();
                LocalDate prazo = campoPrazo.getDate();

                String categoria = comboCategoria.getSelectedItem().toString();
                String status = comboStatus.getSelectedItem().toString();

                Projeto projeto = new Projeto(
                                nome,
                                descricao,
                                categoria,
                                status,
                                prazo);

                try {

                        cliente.cadastrarProjeto(projeto);

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Projeto cadastrado com sucesso!");

                        limparFormulario();
                        carregarTabela();

                } catch (Exception e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Erro ao cadastrar projeto: " + e.getMessage());
                }
        }

        private void carregarTabela() {

                try {
                        List<Projeto> projetos = cliente.listarProjetos();
                        atualizarGraficos(projetos);

                        int total = projetos.size();
                        int concluidos = 0;
                        int emDesenvolvimento = 0;
                        int planejados = 0;

                        for (Projeto projeto : projetos) {
                                if ("Concluído".equalsIgnoreCase(projeto.getStatus())) {
                                        concluidos++;
                                } else if ("Em desenvolvimento".equalsIgnoreCase(projeto.getStatus())) {
                                        emDesenvolvimento++;
                                } else if ("Planejado".equalsIgnoreCase(projeto.getStatus())) {
                                        planejados++;
                                }
                        }

                        valorTotal.setText(String.valueOf(total));
                        valorConcluidos.setText(String.valueOf(concluidos));
                        valorAndamento.setText(String.valueOf(emDesenvolvimento));
                        valorPlanejados.setText(String.valueOf(planejados));

                        projetosFiltrados = new java.util.ArrayList<>(projetos);
                        paginaAtual = 1;
                        atualizarPaginaTabela();

                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                                        "Erro ao carregar projetos: " + e.getMessage());
                }
        }

        private void atualizarPaginaTabela() {
                modelo.setRowCount(0);
                int totalPaginas = Math.max(1, (int) Math.ceil(
                                projetosFiltrados.size() / (double) PROJETOS_POR_PAGINA));
                paginaAtual = Math.min(Math.max(1, paginaAtual), totalPaginas);
                int inicio = (paginaAtual - 1) * PROJETOS_POR_PAGINA;
                int fim = Math.min(inicio + PROJETOS_POR_PAGINA, projetosFiltrados.size());

                for (int i = inicio; i < fim; i++) {
                        Projeto projeto = projetosFiltrados.get(i);
                        modelo.addRow(new Object[] {
                                        projeto.getId(), projeto.getNome(), projeto.getCategoria(),
                                        projeto.getStatus(),
                                        projeto.getDataCriacao().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                        projeto.getPrazo().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                        ""
                        });
                }

                atualizarControlesPaginacao();
                ajustarAlturaLista();
        }

        // Mantém a tabela na altura das linhas exibidas, sem espaço vazio até a
        // paginação.
        private void ajustarAlturaLista() {
                if (scrollTabela == null || painelLista == null || tabela == null)
                        return;

                int quantidadeLinhas = Math.max(1, tabela.getRowCount());
                int alturaCabecalho = tabela.getTableHeader().getPreferredSize().height;
                int alturaLinhas = quantidadeLinhas * tabela.getRowHeight();

                scrollTabela.setPreferredSize(new Dimension(
                                scrollTabela.getPreferredSize().width,
                                alturaCabecalho + alturaLinhas + 2));

                Dimension tamanhoPreferido = painelLista.getPreferredSize();
                painelLista.setMaximumSize(new Dimension(
                                Integer.MAX_VALUE,
                                tamanhoPreferido.height));

                painelLista.revalidate();
                painelLista.repaint();
        }

        private void mudarPagina(int direcao) {
                paginaAtual += direcao;
                atualizarPaginaTabela();
        }

        // Mostra no máximo cinco números de página, além das setas laterais.
        private void atualizarControlesPaginacao() {
                if (painelPaginacao == null)
                        return;

                painelPaginacao.removeAll();
                int totalPaginas = Math.max(1, (int) Math.ceil(
                                projetosFiltrados.size() / (double) PROJETOS_POR_PAGINA));

                JButton anterior = criarBotaoPaginacao("‹", false);
                anterior.setEnabled(paginaAtual > 1);
                anterior.addActionListener(e -> mudarPagina(-1));
                painelPaginacao.add(anterior);

                int primeira = Math.max(1, paginaAtual - 2);
                int ultima = Math.min(totalPaginas, primeira + 4);
                primeira = Math.max(1, ultima - 4);

                for (int numero = primeira; numero <= ultima; numero++) {
                        final int pagina = numero;
                        JButton botaoPagina = criarBotaoPaginacao(
                                        String.valueOf(numero), numero == paginaAtual);
                        botaoPagina.addActionListener(e -> {
                                paginaAtual = pagina;
                                atualizarPaginaTabela();
                        });
                        painelPaginacao.add(botaoPagina);
                }

                JButton proxima = criarBotaoPaginacao("›", false);
                proxima.setEnabled(paginaAtual < totalPaginas);
                proxima.addActionListener(e -> mudarPagina(1));
                painelPaginacao.add(proxima);

                painelPaginacao.revalidate();
                painelPaginacao.repaint();
        }

        private JButton criarBotaoPaginacao(String texto, boolean selecionado) {
                Color fundo = selecionado ? new Color(53, 137, 35) : SUPERFICIE_SECUNDARIA;
                JButton botao = new JButton(texto);
                botao.setPreferredSize(new Dimension(40, 38));
                botao.setMinimumSize(new Dimension(40, 38));
                botao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                botao.setForeground(TEXTO);
                if (selecionado)
                        botao.setForeground(Color.WHITE);
                botao.setBackground(fundo);
                botao.setOpaque(true);
                botao.setFocusPainted(false);
                botao.setBorderPainted(true);
                botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
                botao.setEnabled(!("‹".equals(texto) && paginaAtual == 1)
                                && !("›".equals(texto)
                                                && paginaAtual >= Math.max(1, (int) Math.ceil(
                                                                projetosFiltrados.size()
                                                                                / (double) PROJETOS_POR_PAGINA))));
                return botao;
        }

        private void atualizarGraficos(List<Projeto> projetos) {

                // Atualiza os dados do gráfico de status.
                var novosDadosStatus = viewChart.criarDadosStatus(projetos);

                var novoGraficoStatus = viewChart.criarGraficoStatus(
                                novosDadosStatus);

                painelGraficoStatus.setChart(novoGraficoStatus);

                // Atualiza os dados do gráfico de categorias.
                var novosDadosCategoria = viewChart.criarDadosCategoria(projetos);

                var novoGraficoCategoria = viewChart.criarGraficoCategoria(
                                novosDadosCategoria);

                painelGraficoCategoria.setChart(novoGraficoCategoria);

                // Atualiza os dados do gráfico de evolução.
                var novosDadosEvolucao = viewChart.criarDadosEvolucao(projetos);

                var novoGraficoEvolucao = viewChart.criarGraficoEvolucao(
                                novosDadosEvolucao);

                painelGraficoEvolucao.setChart(novoGraficoEvolucao);
        }

        private void buscarProjetos() {
                String busca = campoBusca.getText().trim().toLowerCase();
                try {
                        List<Projeto> todosProjetos = cliente.listarProjetos();
                        projetosFiltrados = new java.util.ArrayList<>();

                        for (Projeto projeto : todosProjetos) {
                                boolean corresponde = busca.isEmpty()
                                                || projeto.getNome().toLowerCase().contains(busca)
                                                || projeto.getCategoria().toLowerCase().contains(busca)
                                                || projeto.getStatus().toLowerCase().contains(busca);
                                if (corresponde)
                                        projetosFiltrados.add(projeto);
                        }

                        paginaAtual = 1;
                        atualizarPaginaTabela();
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                                        "Erro ao buscar projetos: " + e.getMessage());
                }
        }

        private void editarProjeto(int id) {

                try {

                        List<Projeto> projetos = cliente.listarProjetos();

                        for (Projeto projeto : projetos) {

                                if (projeto.getId() == id) {
                                        abrirPopupEdicao(projeto);
                                        return;
                                }
                        }

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Projeto não encontrado.");

                } catch (Exception e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Erro ao carregar projeto: " + e.getMessage());
                }
        }

        private void abrirPopupEdicao(Projeto projeto) {

                JDialog dialog = new JDialog(
                                this,
                                "Editar projeto",
                                true);

                dialog.setResizable(false);

                JPanel painel = criarPainelComBorda(
                                new BorderLayout(15, 15),
                                SUPERFICIE,
                                BORDA,
                                25);

                painel.setPreferredSize(new Dimension(560, 500));

                JLabel titulo = new JLabel("Editar Projeto");
                titulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                titulo.setForeground(AZUL_ESCURO);

                JTextField nome = new JTextField(projeto.getNome());
                JTextField descricao = new JTextField(projeto.getDescricao());

                DatePickerSettings configuracoes = new DatePickerSettings();
                configuracoes.setFormatForDatesCommonEra("dd-MM-yyyy");

                DatePicker prazo = new DatePicker(configuracoes);
                prazo.setDate(projeto.getPrazo());

                JComboBox<String> categoria = new JComboBox<>(new String[] {
                                "Web", "Software", "Mobile", "Desktop"
                });

                JComboBox<String> status = new JComboBox<>(new String[] {
                                "Planejado", "Em desenvolvimento", "Concluído"
                });

                categoria.setSelectedItem(projeto.getCategoria());
                status.setSelectedItem(projeto.getStatus());

                estilizarCampo(nome);
                estilizarCampo(descricao);
                estilizarCampo(prazo.getComponentDateTextField());
                estilizarCombo(categoria);
                estilizarCombo(status);

                JPanel campos = new JPanel(new GridLayout(5, 1, 10, 14));
                campos.setBackground(SUPERFICIE);

                campos.add(criarCampoComRotulo("nomeProjeto", nome));
                campos.add(criarCampoComRotulo("descricao", descricao));
                campos.add(criarCampoComRotulo("categoriaLabel", categoria));
                campos.add(criarCampoComRotulo("statusLabel", status));
                campos.add(criarCampoComRotulo("prazo", prazo));

                JButton botaoSalvar = criarBotao(
                                "Salvar alterações",
                                new Color(48, 54, 58),
                                BRANCO);

                JButton botaoCancelar = criarBotao(
                                "Cancelar",
                                new Color(112, 120, 126),
                                BRANCO);

                JPanel botoes = new JPanel(
                                new FlowLayout(FlowLayout.RIGHT, 10, 0));

                botoes.setBackground(SUPERFICIE);
                botoes.add(botaoCancelar);
                botoes.add(botaoSalvar);

                painel.add(titulo, BorderLayout.NORTH);
                painel.add(campos, BorderLayout.CENTER);
                painel.add(botoes, BorderLayout.SOUTH);

                dialog.setContentPane(painel);

                dialog.pack();
                dialog.setLocationRelativeTo(this);

                botaoCancelar.addActionListener(e -> dialog.dispose());

                botaoSalvar.addActionListener(e -> {

                        String novoNome = nome.getText().trim();
                        String novaDescricao = descricao.getText().trim();
                        LocalDate novoPrazo = prazo.getDate();

                        String novaCategoria = categoria.getSelectedItem().toString();
                        String novoStatus = status.getSelectedItem().toString();

                        if (novoNome.isBlank()) {
                                JOptionPane.showMessageDialog(
                                                dialog,
                                                "Informe o nome do projeto.");
                                return;
                        }

                        Projeto projetoAtualizado = new Projeto(
                                        novoNome,
                                        novaDescricao,
                                        novaCategoria,
                                        novoStatus,
                                        novoPrazo);

                        try {

                                cliente.alterarProjeto(
                                                projeto.getId(),
                                                projetoAtualizado);

                                JOptionPane.showMessageDialog(
                                                dialog,
                                                "Projeto alterado com sucesso!");

                                dialog.dispose();
                                carregarTabela();

                        } catch (Exception ex) {

                                JOptionPane.showMessageDialog(
                                                dialog,
                                                "Erro ao alterar projeto: " + ex.getMessage());
                        }
                });

                dialog.setVisible(true);
        }

        private void excluir(int id) {

                int resposta = JOptionPane.showConfirmDialog(
                                this,
                                "Deseja realmente excluir este projeto?",
                                "Confirmar exclusão",
                                JOptionPane.YES_NO_OPTION);

                if (resposta != JOptionPane.YES_OPTION) {
                        return;
                }

                try {

                        cliente.excluirProjeto(id);

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Projeto excluído com sucesso!");

                        carregarTabela();

                } catch (Exception e) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Erro ao excluir projeto: " + e.getMessage());
                }
        }

        // Renderer dos botões da coluna Ações.
        private class ActionCellRenderer extends JPanel
                        implements TableCellRenderer {

                private final JButton editar = new JButton();
                private final JButton excluir = new JButton();

                private final ImageIcon editarNormal = carregarIconeAcao("frontend/img/icone-editar.png");

                private final ImageIcon editarHover = carregarIconeAcao("frontend/img/icone-editar-hover2.png");

                private final ImageIcon excluirNormal = carregarIconeAcao("frontend/img/icone-excluir.png");

                private final ImageIcon excluirHover = carregarIconeAcao("frontend/img/icone-excluir-hover1.png");

                public ActionCellRenderer() {

                        setLayout(new FlowLayout(FlowLayout.CENTER, 4, 3));
                        setBackground(SUPERFICIE);

                        configurarBotaoAcao(
                                        editar,
                                        "frontend/img/icone-editar.png",
                                        "frontend/img/icone-editar-hover2.png");

                        configurarBotaoAcao(
                                        excluir,
                                        "frontend/img/icone-excluir.png",
                                        "frontend/img/icone-excluir-hover1.png");

                        add(editar);
                        add(excluir);
                }

                @Override
                public Component getTableCellRendererComponent(
                                JTable tabela,
                                Object valor,
                                boolean selecionado,
                                boolean foco,
                                int linha,
                                int coluna) {

                        setBorder(BorderFactory.createMatteBorder(
                                        0, 0, 1, 1, BORDA));

                        boolean linhaEmHover = linha == linhaHoverAcoes;

                        editar.setIcon(
                                        linhaEmHover && botaoHoverAcoes == 0
                                                        ? editarHover
                                                        : editarNormal);

                        excluir.setIcon(
                                        linhaEmHover && botaoHoverAcoes == 1
                                                        ? excluirHover
                                                        : excluirNormal);

                        return this;
                }
        }

        // Editor dos botões da coluna Ações.
        private class ActionCellEditor extends AbstractCellEditor
                        implements TableCellEditor {

                private final JPanel painel = new JPanel(
                                new FlowLayout(FlowLayout.CENTER, 4, 3));

                private final JButton editar = new JButton();
                private final JButton excluir = new JButton();

                private int idSelecionado;

                public ActionCellEditor() {

                        painel.setBackground(SUPERFICIE);

                        configurarBotaoAcao(
                                        editar,
                                        "frontend/img/icone-editar.png",
                                        "frontend/img/icone-editar-hover2.png");

                        configurarBotaoAcao(
                                        excluir,
                                        "frontend/img/icone-excluir.png",
                                        "frontend/img/icone-excluir-hover1.png");

                        editar.setToolTipText("Editar projeto");
                        excluir.setToolTipText("Excluir projeto");

                        painel.add(editar);
                        painel.add(excluir);

                        editar.addActionListener(e -> {

                                stopCellEditing();
                                editarProjeto(idSelecionado);
                        });

                        excluir.addActionListener(e -> {

                                stopCellEditing();
                                excluir(idSelecionado);
                        });
                }

                @Override
                public Component getTableCellEditorComponent(
                                JTable tabela,
                                Object valor,
                                boolean selecionado,
                                int linha,
                                int coluna) {

                        int linhaModelo = tabela.convertRowIndexToModel(linha);

                        idSelecionado = (int) modelo.getValueAt(linhaModelo, 0);

                        return painel;
                }

                @Override
                public Object getCellEditorValue() {
                        return "";
                }
        }

        private ImageIcon carregarIconeAcao(String caminho) {

                Image imagem = new ImageIcon(caminho)
                                .getImage()
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

                return new ImageIcon(imagem);
        }

        private void configurarBotaoAcao(
                        JButton botao,
                        String caminhoNormal,
                        String caminhoClicado) {

                Image imagemNormal = new ImageIcon(caminhoNormal)
                                .getImage()
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

                Image imagemClicada = new ImageIcon(caminhoClicado)
                                .getImage()
                                .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

                botao.setIcon(new ImageIcon(imagemNormal));
                botao.setRolloverIcon(new ImageIcon(imagemClicada));
                botao.setRolloverEnabled(true);

                botao.setBackground(SUPERFICIE);
                botao.setFocusPainted(false);
                botao.setBorderPainted(false);
                botao.setContentAreaFilled(false);
                botao.setOpaque(false);

                botao.setPreferredSize(new Dimension(30, 27));
                botao.setMargin(new Insets(0, 0, 0, 0));

                botao.setCursor(new java.awt.Cursor(
                                java.awt.Cursor.HAND_CURSOR));
        }

        public static void main(String[] args) {

                SwingUtilities.invokeLater(() -> {

                        try {

                                Preferences preferencias = Preferences.userNodeForPackage(TelaProjetos.class);

                                boolean temaClaro = preferencias.get("tema", "dark").equals("light");

                                if (temaClaro) {
                                        FlatLightLaf.setup();
                                } else {
                                        FlatDarkLaf.setup();
                                }

                                UIManager.put(
                                                "defaultFont",
                                                new Font("Segoe UI", Font.PLAIN, 14));

                                UIManager.put("Component.arc", 8);
                                UIManager.put("Button.arc", 6);
                                UIManager.put("TextComponent.arc", 6);
                                UIManager.put("ComboBox.arc", 6);
                                UIManager.put("ScrollBar.thumbArc", 999);

                                UIManager.put("Component.focusWidth", 1);

                                UIManager.put(
                                                "Button.margin",
                                                new Insets(8, 12, 8, 12));

                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        TelaLogin tela = new TelaLogin();
                        tela.setVisible(true);
                });
        }
}