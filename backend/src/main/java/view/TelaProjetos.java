
package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import client.ApiClient;
import model.Projeto;


public class TelaProjetos extends JFrame {

    private static final Font FONTE_NORMAL =
            new Font("Segoe UI", Font.PLAIN, 14);

    private static final Font FONTE_NEGRITO =
            new Font("Segoe UI", Font.BOLD, 14);

    private JTextField campoNome;
    private JTextField campoDescricao;
    private JTextField campoBusca;
    private DatePicker campoPrazo;

    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;

    private JTable tabela;
    private DefaultTableModel modelo;
    private CardLayout gerenciadorTelas;
    private JPanel painelTelas;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;
    private JButton botaoBuscar;
    private JButton botaoSair;

    private JLabel valorTotal;
    private JLabel valorConcluidos;
    private JLabel valorAndamento;
    private JLabel valorPlanejados;

    private ApiClient cliente;

    private ViewChart viewChart = new ViewChart();

    private JPanel subpainelStatus;
    private JPanel subpainelCategoria;
    private JPanel subpainelEvolucao;
    private ChartPanel painelGraficoEvolucao;

    private int linhaHoverAcoes = -1;
    private int botaoHoverAcoes = -1;

    private JPanel itemDashboard;
    private JPanel itemProjetos;
    private JPanel itemMenuAtivo;

    private final Color AZUL_ESCURO = new Color(245, 245, 247);
    private final Color AZUL_SIDEBAR = new Color(25, 25, 27);
    private final Color AZUL = new Color(0, 153, 153);

    private final Color FUNDO = new Color(28, 28, 30);
    private final Color BRANCO = new Color(245, 245, 247);
    private final Color TEXTO = new Color(229, 229, 231);
    private final Color CINZA = new Color(161, 161, 166);

    private final Color SUPERFICIE = new Color(36, 36, 38);
    private final Color SUPERFICIE_SECUNDARIA = new Color(48, 48, 51);
    private final Color BORDA = new Color(58, 58, 60);
    private final Color BARRA_SUPERIOR = new Color(34, 34, 36);
    private final Color MENU_ATIVO = new Color(53, 53, 56);
    private final Color MENU_HOVER = new Color(41, 41, 44);


    public TelaProjetos(ApiClient cliente) {

        setTitle("Sistema de Projetos");
        setSize(1200, 760);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.cliente = cliente;

        criarComponentes();
        criarEventos();
        carregarTabela();
    }


    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(FUNDO);

        // Barra superior
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(BARRA_SUPERIOR);
        barraSuperior.setBorder(
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
        );

        JLabel tituloSistema = new JLabel("Sistema de Projetos");
        tituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloSistema.setForeground(AZUL_ESCURO);

        JLabel usuario = new JLabel("Usuário");
        usuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuario.setForeground(CINZA);

        botaoSair = criarBotao(
                "Sair",
                new Color(190, 55, 65),
                BRANCO
        );

        JPanel painelUsuario = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 15, 0)
        );

        painelUsuario.setBackground(BARRA_SUPERIOR);
        painelUsuario.add(usuario);
        painelUsuario.add(botaoSair);

        barraSuperior.add(tituloSistema, BorderLayout.WEST);
        barraSuperior.add(painelUsuario, BorderLayout.EAST);


        // Menu lateral
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AZUL_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(
                BorderFactory.createEmptyBorder(25, 15, 20, 15)
        );

        ImageIcon imagemOriginal = new ImageIcon("frontend/img/logo6.png");

        Image imagemRedimensionada = imagemOriginal.getImage()
                .getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        JLabel logo = new JLabel(new ImageIcon(imagemRedimensionada));

        JLabel textoMarca = new JLabel("Projetos");
        textoMarca.setFont(new Font("Segoe UI", Font.BOLD, 19));
        textoMarca.setForeground(BRANCO);

        JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        marca.setBackground(AZUL_SIDEBAR);
        marca.setAlignmentX(LEFT_ALIGNMENT);
        marca.setPreferredSize(new Dimension(180, 32));
        marca.setMaximumSize(new Dimension(180, 32));
        marca.setMinimumSize(new Dimension(180, 32));

        marca.add(logo);
        marca.add(textoMarca);

        sidebar.add(marca);
        sidebar.add(Box.createVerticalStrut(25));

        itemDashboard = criarItemMenu("Dashboard", true);
        itemProjetos = criarItemMenu("Projetos", false);

        JPanel itemRelatorios = criarItemMenu("Relatórios", false);
        JPanel itemConfiguracoes = criarItemMenu("Configurações", false);

        itemMenuAtivo = itemDashboard;

        sidebar.add(itemDashboard);
        sidebar.add(itemProjetos);
        sidebar.add(itemRelatorios);
        sidebar.add(itemConfiguracoes);


        // Gerenciador de telas
        gerenciadorTelas = new CardLayout();

        painelTelas = new JPanel(gerenciadorTelas);
        painelTelas.setBackground(FUNDO);


        // Tela Dashboard
        JPanel telaDashboard = new JPanel();
        telaDashboard.setLayout(
                new BoxLayout(telaDashboard, BoxLayout.Y_AXIS)
        );
        telaDashboard.setBackground(FUNDO);
        telaDashboard.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );


        // Tela Projetos
        JPanel telaProjetos = new JPanel();
        telaProjetos.setLayout(
                new BoxLayout(telaProjetos, BoxLayout.Y_AXIS)
        );
        telaProjetos.setBackground(FUNDO);
        telaProjetos.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JLabel tituloPagina = new JLabel("Dashboard");
        tituloPagina.setFont(new Font("Segoe UI", Font.BOLD, 25));
        tituloPagina.setForeground(AZUL_ESCURO);
        tituloPagina.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel(
                "Gerencie e acompanhe seus projetos."
        );
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(CINZA);
        subtitulo.setAlignmentX(LEFT_ALIGNMENT);

        telaDashboard.add(tituloPagina);
        telaDashboard.add(Box.createVerticalStrut(5));
        telaDashboard.add(subtitulo);
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
                        "Total de Projetos",
                        valorTotal,
                        AZUL,
                        "frontend/img/card-total-projetos.png"
                )
        );

        painelCards.add(
                criarCard(
                        "Concluídos",
                        valorConcluidos,
                        new Color(67, 124, 30),
                        "frontend/img/card-concluidos.png"
                )
        );

        painelCards.add(
                criarCard(
                        "Em Andamento",
                        valorAndamento,
                        new Color(239, 98, 108),
                        "frontend/img/card-em-andamento.png"
                )
        );

        painelCards.add(
                criarCard(
                        "Planejados",
                        valorPlanejados,
                        new Color(239, 246, 238),
                        "frontend/img/card-planejados.png"
                )
        );

        telaDashboard.add(painelCards);
        telaDashboard.add(Box.createVerticalStrut(22));


        // Gráfico de status
        var dadosStatus = viewChart.criarDadosStatus();
        var graficoStatus = viewChart.criarGraficoStatus(dadosStatus);

        ChartPanel painelGraficoStatus =
                viewChart.criarPainelGrafico(graficoStatus);


        // Gráfico de categorias
        var dadosCategoria = viewChart.criarDadosCategoria();
        var graficoCategoria =
                viewChart.criarGraficoCategoria(dadosCategoria);

        ChartPanel painelGraficoCategoria =
                viewChart.criarPainelGrafico(graficoCategoria);


        // Gráfico de evolução mensal
        var dadosEvolucao = viewChart.criarDadosEvolucao(List.of());
        var graficoEvolucao =
                viewChart.criarGraficoEvolucao(dadosEvolucao);

        painelGraficoEvolucao =
                viewChart.criarPainelGrafico(graficoEvolucao);


        // Subpainel do gráfico de status
        subpainelStatus = criarPainelArredondado(
                new BorderLayout(),
                SUPERFICIE,
                BORDA,
                8,
                8
        );

        subpainelStatus.add(painelGraficoStatus, BorderLayout.CENTER);


        // Subpainel do gráfico de categorias
        subpainelCategoria = criarPainelArredondado(
                new BorderLayout(),
                SUPERFICIE,
                BORDA,
                8,
                8
        );

        subpainelCategoria.add(painelGraficoCategoria, BorderLayout.CENTER);


        // Subpainel do gráfico de evolução
        subpainelEvolucao = criarPainelArredondado(
                new BorderLayout(),
                SUPERFICIE,
                BORDA,
                8,
                8
        );

        subpainelEvolucao.add(
                painelGraficoEvolucao,
                BorderLayout.CENTER
        );


        // Primeira linha: gráficos de status e categoria
        JPanel linhaGraficos = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        linhaGraficos.setBackground(FUNDO);
        linhaGraficos.add(subpainelStatus);
        linhaGraficos.add(subpainelCategoria);


        // Painel geral dos gráficos
        JPanel painelGraficos = new JPanel();

        painelGraficos.setLayout(
                new BoxLayout(painelGraficos, BoxLayout.Y_AXIS)
        );

        painelGraficos.setBackground(FUNDO);
        painelGraficos.setAlignmentX(LEFT_ALIGNMENT);

        linhaGraficos.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 340)
        );

        painelGraficos.add(linhaGraficos);
        painelGraficos.add(Box.createVerticalStrut(15));

        subpainelEvolucao.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 300)
        );

        painelGraficos.add(subpainelEvolucao);

        telaDashboard.add(painelGraficos);
        telaDashboard.add(Box.createVerticalStrut(22));


        // Formulário de cadastro
        JPanel painelFormulario = criarPainelArredondado(
                new BorderLayout(10, 15),
                SUPERFICIE,
                BORDA,
                20,
                8
        );

        painelFormulario.setAlignmentX(LEFT_ALIGNMENT);
        painelFormulario.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 270)
        );

        JLabel tituloFormulario = new JLabel("Cadastro de Projeto");
        tituloFormulario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloFormulario.setForeground(AZUL_ESCURO);

        campoNome = new JTextField();
        campoDescricao = new JTextField();

        DatePickerSettings configuracoes = new DatePickerSettings();
        configuracoes.setFormatForDatesCommonEra("dd-MM-yyyy");
        campoPrazo = new DatePicker(configuracoes);

        comboCategoria = new JComboBox<>(new String[]{
                "Web", "Software", "Mobile", "Outro"
        });

        comboStatus = new JComboBox<>(new String[]{
                "Planejado", "Em desenvolvimento", "Concluído"
        });

        estilizarCampo(campoNome);
        estilizarCampo(campoDescricao);
        estilizarCombo(comboCategoria);
        estilizarCombo(comboStatus);
        estilizarCampo(campoPrazo.getComponentDateTextField());

        JPanel campos = new JPanel(new GridLayout(3, 2, 15, 12));
        campos.setBackground(SUPERFICIE);

        campos.add(criarCampoComRotulo("Nome do projeto", campoNome));
        campos.add(criarCampoComRotulo("Descrição", campoDescricao));
        campos.add(criarCampoComRotulo("Categoria", comboCategoria));
        campos.add(criarCampoComRotulo("Status", comboStatus));
        campos.add(criarCampoComRotulo("Prazo (DD-MM-AAAA)", campoPrazo));

        botaoCadastrar = criarBotao("Cadastrar projeto", AZUL, BRANCO);

        botaoLimpar = criarBotao(
                "Limpar",
                SUPERFICIE_SECUNDARIA,
                TEXTO
        );

        JPanel botoesFormulario = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

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
        JPanel painelLista = criarPainelArredondado(
                new BorderLayout(0, 15),
                SUPERFICIE,
                BORDA,
                20,
                8
        );

        painelLista.setAlignmentX(LEFT_ALIGNMENT);

        JLabel tituloLista = new JLabel("Lista de Projetos");
        tituloLista.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloLista.setForeground(AZUL_ESCURO);

        campoBusca = new JTextField(18);
        campoBusca.setPreferredSize(new Dimension(220, 34));
        estilizarCampo(campoBusca);

        botaoBuscar = criarBotao("Buscar", AZUL, BRANCO);

        JPanel barraLista = new JPanel(new BorderLayout());
        barraLista.setBackground(SUPERFICIE);
        barraLista.add(tituloLista, BorderLayout.WEST);

        JPanel controlesBusca = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        controlesBusca.setBackground(SUPERFICIE);
        controlesBusca.add(campoBusca);
        controlesBusca.add(botaoBuscar);

        barraLista.add(controlesBusca, BorderLayout.EAST);


        // Modelo da tabela
        modelo = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nome",
                        "Categoria",
                        "Status",
                        "Data de criação",
                        "Prazo",
                        "Ações"
                },
                0
        ) {
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
        tabela.setShowVerticalLines(false);
        tabela.setSelectionBackground(new Color(53, 53, 56));
        tabela.setSelectionForeground(BRANCO);
        tabela.setFillsViewportHeight(true);

        tabela.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        tabela.getTableHeader().setBackground(SUPERFICIE_SECUNDARIA);
        tabela.getTableHeader().setForeground(TEXTO);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 38));

        DefaultTableCellRenderer centralizar =
                new DefaultTableCellRenderer();

        centralizar.setHorizontalAlignment(SwingConstants.CENTER);
        centralizar.setBackground(SUPERFICIE);
        centralizar.setForeground(TEXTO);

        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizar);

        tabela.getColumnModel().getColumn(6).setCellRenderer(
                new ActionCellRenderer()
        );

        tabela.getColumnModel().getColumn(6).setCellEditor(
                new ActionCellEditor()
        );


        // Detecta o mouse sobre os botões da coluna Ações.
        tabela.addMouseMotionListener(
                new java.awt.event.MouseMotionAdapter() {

                    @Override
                    public void mouseMoved(java.awt.event.MouseEvent e) {

                        int linha = tabela.rowAtPoint(e.getPoint());
                        int coluna = tabela.columnAtPoint(e.getPoint());

                        int novoBotaoHover = -1;

                        if (linha >= 0 && coluna == 6) {

                            java.awt.Rectangle celula =
                                    tabela.getCellRect(linha, coluna, true);

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
                }
        );

        tabela.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {

                        linhaHoverAcoes = -1;
                        botaoHoverAcoes = -1;

                        tabela.repaint();
                    }
                }
        );

        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(90);

        JScrollPane scrollTabela = new JScrollPane(tabela);

        scrollTabela.setBorder(
                new RoundedLineBorder(BORDA, 7)
        );

        scrollTabela.getViewport().setBackground(SUPERFICIE);

        painelLista.add(barraLista, BorderLayout.NORTH);
        painelLista.add(scrollTabela, BorderLayout.CENTER);

        telaProjetos.add(painelLista);


        // Adiciona as telas ao gerenciador.
        painelTelas.add(telaDashboard, "DASHBOARD");
        painelTelas.add(telaProjetos, "PROJETOS");

        gerenciadorTelas.show(painelTelas, "DASHBOARD");


        // Navegação entre telas.
        adicionarEventoMenu(itemDashboard, "DASHBOARD");
        adicionarEventoMenu(itemProjetos, "PROJETOS");


        // Área direita: barra superior + conteúdo.
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBackground(FUNDO);

        painelDireito.add(barraSuperior, BorderLayout.NORTH);
        painelDireito.add(painelTelas, BorderLayout.CENTER);

        painelPrincipal.add(sidebar, BorderLayout.WEST);
        painelPrincipal.add(painelDireito, BorderLayout.CENTER);

        setContentPane(painelPrincipal);
    }


    // Cria um painel com fundo e borda arredondados.
    private JPanel criarPainelArredondado(
            java.awt.LayoutManager layout,
            Color fundo,
            Color borda,
            int padding,
            int raio
    ) {

        JPanel painel = new RoundedPanel(
                layout,
                fundo,
                borda,
                raio
        );

        painel.setBorder(
                BorderFactory.createEmptyBorder(
                        padding,
                        padding,
                        padding,
                        padding
                )
        );

        return painel;
    }


    // Cria um item do menu lateral.
    private JPanel criarItemMenu(String texto, boolean selecionado) {

        JPanel item = new JPanel(new BorderLayout());

        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        item.setPreferredSize(new Dimension(180, 42));

        item.setBackground(
                selecionado ? MENU_ATIVO : AZUL_SIDEBAR
        );

        item.setBorder(
                BorderFactory.createEmptyBorder(0, 12, 0, 8)
        );

        JLabel rotulo = new JLabel(texto);

        rotulo.setFont(new Font(
                "Segoe UI",
                selecionado ? Font.BOLD : Font.PLAIN,
                14
        ));

        rotulo.setForeground(
                selecionado ? BRANCO : CINZA
        );

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
                itemProjetos
        };

        for (JPanel item : itens) {

            boolean selecionado = item == itemSelecionado;

            item.setBackground(
                    selecionado ? MENU_ATIVO : AZUL_SIDEBAR
            );

            JLabel rotulo = (JLabel) item.getComponent(0);

            rotulo.setForeground(
                    selecionado ? BRANCO : CINZA
            );

            rotulo.setFont(new Font(
                    "Segoe UI",
                    selecionado ? Font.BOLD : Font.PLAIN,
                    14
            ));
        }

        itemMenuAtivo = itemSelecionado;
    }


    private JPanel criarCard(
            String titulo,
            JLabel valor,
            Color cor,
            String caminhoIcone
    ) {

        JPanel card = criarPainelArredondado(
                new BorderLayout(12, 0),
                SUPERFICIE,
                BORDA,
                12,
                8
        );

        JLabel icone = new JLabel(carregarIconeCard(caminhoIcone));

        icone.setPreferredSize(new Dimension(44, 44));
        icone.setHorizontalAlignment(SwingConstants.CENTER);
        icone.setVerticalAlignment(SwingConstants.CENTER);

        JPanel conteudo = new JPanel();

        conteudo.setLayout(
                new BoxLayout(conteudo, BoxLayout.Y_AXIS)
        );

        conteudo.setBackground(SUPERFICIE);

        valor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valor.setForeground(cor);

        JLabel rotulo = new JLabel(titulo);

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
            String rotulo,
            Component campo
    ) {

        JPanel painel = new JPanel(new BorderLayout(0, 6));
        painel.setBackground(SUPERFICIE);

        JLabel label = new JLabel(rotulo);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXTO);

        painel.add(label, BorderLayout.NORTH);
        painel.add(campo, BorderLayout.CENTER);

        return painel;
    }


    private JButton criarBotao(
            String texto,
            Color fundo,
            Color frente
    ) {

        JButton botao = new JButton(texto);

        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(fundo);
        botao.setForeground(frente);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.setMargin(new Insets(10, 15, 10, 15));

        botao.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR
        ));

        return botao;
    }


    private void estilizarCampo(JTextField campo) {

        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(TEXTO);
        campo.setBackground(SUPERFICIE_SECUNDARIA);
        campo.setCaretColor(BRANCO);
        campo.setPreferredSize(new Dimension(200, 34));

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedLineBorder(new Color(72, 72, 74), 6),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                )
        );
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
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {

                cliente.limparToken();

                TelaLogin tela = new TelaLogin();
                tela.setVisible(true);

                dispose();
            }
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

        // Validar nome obrigatório
        if (nome.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do projeto."
            );
            return;
        }

        // Validar tamanho mínimo do nome
        if (nome.length() < 3) {
            JOptionPane.showMessageDialog(
                    this,
                    "O nome deve ter pelo menos 3 caracteres."
            );
            return;
        }

        // Validar tamanho máximo do nome
        if (nome.length() > 100) {
            JOptionPane.showMessageDialog(
                    this,
                    "O nome deve ter no máximo 100 caracteres."
            );
            return;
        }

        // Validar descrição obrigatória
        if (descricao.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Adicione a descrição do projeto."
            );
            return;
        }

        // Validar tamanho mínimo da descrição
        if (descricao.length() < 10) {
            JOptionPane.showMessageDialog(
                    this,
                    "A descrição deve ter pelo menos 10 caracteres."
            );
            return;
        }

        // Validar tamanho máximo da descrição
        if (descricao.length() > 500) {
            JOptionPane.showMessageDialog(
                    this,
                    "A descrição deve ter no máximo 500 caracteres."
            );
            return;
        }

        // Validar prazo obrigatório
        if (prazo == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o prazo do projeto."
            );
            return;
        }

        // Validar prazo
        if (prazo.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(
                    this,
                    "O prazo não pode ser anterior à data atual."
            );
            return;
        }

        Projeto projeto = new Projeto(
                nome,
                descricao,
                categoria,
                status,
                prazo
        );

        try {

            cliente.cadastrarProjeto(projeto);

            JOptionPane.showMessageDialog(
                    this,
                    "Projeto cadastrado com sucesso!"
            );

            limparFormulario();
            carregarTabela();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar projeto: " + e.getMessage()
            );
        }
    }


    private void carregarTabela() {

        try {

            modelo.setRowCount(0);

            List<Projeto> projetos = cliente.listarProjetos();

            atualizarGraficoEvolucao(projetos);

            int total = projetos.size();
            int concluidos = 0;
            int andamento = 0;
            int planejados = 0;

            for (Projeto projeto : projetos) {

                modelo.addRow(new Object[]{
                        projeto.getId(),
                        projeto.getNome(),
                        projeto.getCategoria(),
                        projeto.getStatus(),
                        projeto.getDataCriacao().format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        ),
                        projeto.getPrazo().format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        ),
                        ""
                });

                if ("Concluído".equalsIgnoreCase(projeto.getStatus())) {
                    concluidos++;

                } else if (
                        "Em desenvolvimento".equalsIgnoreCase(projeto.getStatus())
                ) {
                    andamento++;

                } else if ("Planejado".equalsIgnoreCase(projeto.getStatus())) {
                    planejados++;
                }
            }

            valorTotal.setText(String.valueOf(total));
            valorConcluidos.setText(String.valueOf(concluidos));
            valorAndamento.setText(String.valueOf(andamento));
            valorPlanejados.setText(String.valueOf(planejados));

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar projetos: " + e.getMessage()
            );
        }
    }


    private void atualizarGraficoEvolucao(List<Projeto> projetos) {

        var novosDados = viewChart.criarDadosEvolucao(projetos);
        var novoGrafico = viewChart.criarGraficoEvolucao(novosDados);

        painelGraficoEvolucao.setChart(novoGrafico);
    }


    private void buscarProjetos() {

        String busca = campoBusca.getText().trim().toLowerCase();

        try {

            modelo.setRowCount(0);

            List<Projeto> projetos = cliente.listarProjetos();

            for (Projeto projeto : projetos) {

                boolean corresponde =
                        projeto.getNome().toLowerCase().contains(busca)
                        || projeto.getCategoria().toLowerCase().contains(busca)
                        || projeto.getStatus().toLowerCase().contains(busca);

                if (corresponde) {

                    modelo.addRow(new Object[]{
                            projeto.getId(),
                            projeto.getNome(),
                            projeto.getCategoria(),
                            projeto.getStatus(),
                            projeto.getDataCriacao().format(
                                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            ),
                            projeto.getPrazo().format(
                                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            ),
                            ""
                    });
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao buscar projetos: " + e.getMessage()
            );
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
                    "Projeto não encontrado."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar projeto: " + e.getMessage()
            );
        }
    }


    private void abrirPopupEdicao(Projeto projeto) {

        JDialog dialog = new JDialog(
                this,
                "Editar projeto",
                true
        );

        dialog.setResizable(false);

        JPanel painel = criarPainelArredondado(
                new BorderLayout(15, 15),
                SUPERFICIE,
                BORDA,
                25,
                10
        );

        painel.setPreferredSize(new Dimension(560, 500));

        JLabel titulo = new JLabel("Editar Projeto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(AZUL_ESCURO);

        JTextField nome = new JTextField(projeto.getNome());
        JTextField descricao = new JTextField(projeto.getDescricao());

        DatePickerSettings configuracoes = new DatePickerSettings();
        configuracoes.setFormatForDatesCommonEra("dd-MM-yyyy");

        DatePicker prazo = new DatePicker(configuracoes);
        prazo.setDate(projeto.getPrazo());

        JComboBox<String> categoria = new JComboBox<>(new String[]{
                "Web", "Software", "Mobile", "Outro"
        });

        JComboBox<String> status = new JComboBox<>(new String[]{
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

        campos.add(criarCampoComRotulo("Nome do projeto", nome));
        campos.add(criarCampoComRotulo("Descrição", descricao));
        campos.add(criarCampoComRotulo("Categoria", categoria));
        campos.add(criarCampoComRotulo("Status", status));
        campos.add(criarCampoComRotulo("Prazo (DD-MM-AAAA)", prazo));

        JButton botaoSalvar = criarBotao(
                "Salvar alterações",
                AZUL,
                BRANCO
        );

        JButton botaoCancelar = criarBotao(
                "Cancelar",
                SUPERFICIE_SECUNDARIA,
                TEXTO
        );

        JPanel botoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

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
                        "Informe o nome do projeto."
                );
                return;
            }

            Projeto projetoAtualizado = new Projeto(
                    novoNome,
                    novaDescricao,
                    novaCategoria,
                    novoStatus,
                    novoPrazo
            );

            try {

                cliente.alterarProjeto(
                        projeto.getId(),
                        projetoAtualizado
                );

                JOptionPane.showMessageDialog(
                        dialog,
                        "Projeto alterado com sucesso!"
                );

                dialog.dispose();
                carregarTabela();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Erro ao alterar projeto: " + ex.getMessage()
                );
            }
        });

        dialog.setVisible(true);
    }


    private void excluir(int id) {

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este projeto?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            cliente.excluirProjeto(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Projeto excluído com sucesso!"
            );

            carregarTabela();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir projeto: " + e.getMessage()
            );
        }
    }


    // Renderer dos botões da coluna Ações.
    private class ActionCellRenderer extends JPanel
            implements TableCellRenderer {

        private final JButton editar = new JButton();
        private final JButton excluir = new JButton();

        private final ImageIcon editarNormal =
                carregarIconeAcao("frontend/img/icone-editar.png");

        private final ImageIcon editarHover =
                carregarIconeAcao("frontend/img/icone-editar-hover2.png");

        private final ImageIcon excluirNormal =
                carregarIconeAcao("frontend/img/icone-excluir.png");

        private final ImageIcon excluirHover =
                carregarIconeAcao("frontend/img/icone-excluir-hover1.png");


        public ActionCellRenderer() {

            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 3));
            setBackground(SUPERFICIE);

            configurarBotaoAcao(
                    editar,
                    "frontend/img/icone-editar.png",
                    "frontend/img/icone-editar-hover2.png"
            );

            configurarBotaoAcao(
                    excluir,
                    "frontend/img/icone-excluir.png",
                    "frontend/img/icone-excluir-hover1.png"
            );

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
                int coluna
        ) {

            boolean linhaEmHover = linha == linhaHoverAcoes;

            editar.setIcon(
                    linhaEmHover && botaoHoverAcoes == 0
                            ? editarHover
                            : editarNormal
            );

            excluir.setIcon(
                    linhaEmHover && botaoHoverAcoes == 1
                            ? excluirHover
                            : excluirNormal
            );

            return this;
        }
    }


    // Editor dos botões da coluna Ações.
    private class ActionCellEditor extends AbstractCellEditor
            implements TableCellEditor {

        private final JPanel painel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 4, 3)
        );

        private final JButton editar = new JButton();
        private final JButton excluir = new JButton();

        private int idSelecionado;


        public ActionCellEditor() {

            painel.setBackground(SUPERFICIE);

            configurarBotaoAcao(
                    editar,
                    "frontend/img/icone-editar.png",
                    "frontend/img/icone-editar-hover2.png"
            );

            configurarBotaoAcao(
                    excluir,
                    "frontend/img/icone-excluir.png",
                    "frontend/img/icone-excluir-hover1.png"
            );

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
                int coluna
        ) {

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
            String caminhoClicado
    ) {

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
                java.awt.Cursor.HAND_CURSOR
        ));
    }


    // Painel com fundo e borda arredondados.
    private static class RoundedPanel extends JPanel {

        private final Color fundo;
        private final Color borda;
        private final int raio;


        public RoundedPanel(
                java.awt.LayoutManager layout,
                Color fundo,
                Color borda,
                int raio
        ) {

            super(layout);

            this.fundo = fundo;
            this.borda = borda;
            this.raio = raio;

            setOpaque(false);
        }


        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(fundo);

            g2.fill(
                    new RoundRectangle2D.Float(
                            0,
                            0,
                            getWidth() - 1,
                            getHeight() - 1,
                            raio,
                            raio
                    )
            );

            g2.dispose();

            super.paintComponent(g);
        }


        @Override
        protected void paintBorder(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(borda);

            g2.draw(
                    new RoundRectangle2D.Float(
                            0.5f,
                            0.5f,
                            getWidth() - 1,
                            getHeight() - 1,
                            raio,
                            raio
                    )
            );

            g2.dispose();
        }
    }


    // Borda arredondada para campos e tabelas.
    private static class RoundedLineBorder extends AbstractBorder {

        private final Color cor;
        private final int raio;


        public RoundedLineBorder(Color cor, int raio) {
            this.cor = cor;
            this.raio = raio;
        }


        @Override
        public void paintBorder(
                Component componente,
                Graphics g,
                int x,
                int y,
                int largura,
                int altura
        ) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(cor);

            g2.draw(
                    new RoundRectangle2D.Float(
                            x + 0.5f,
                            y + 0.5f,
                            largura - 1,
                            altura - 1,
                            raio,
                            raio
                    )
            );

            g2.dispose();
        }


        @Override
        public Insets getBorderInsets(Component componente) {
            return new Insets(1, 1, 1, 1);
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                com.formdev.flatlaf.FlatDarkLaf.setup();

                UIManager.put(
                        "defaultFont",
                        new Font("Segoe UI", Font.PLAIN, 14)
                );

                UIManager.put("Component.arc", 8);
                UIManager.put("Button.arc", 6);
                UIManager.put("TextComponent.arc", 6);
                UIManager.put("ComboBox.arc", 6);
                UIManager.put("ScrollBar.thumbArc", 999);

                UIManager.put("Component.focusWidth", 1);

                UIManager.put(
                        "Button.margin",
                        new Insets(8, 12, 8, 12)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }

            TelaLogin tela = new TelaLogin();
            tela.setVisible(true);
        });
    }
}
