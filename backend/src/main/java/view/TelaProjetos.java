
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import client.ApiClient;
import model.Projeto;

public class TelaProjetos extends JFrame {

    private JTextField campoNome;
    private JTextField campoDescricao;
    private JTextField campoBusca;

    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;
    private JButton botaoExcluir;
    private JButton botaoBuscar;

    private JLabel valorTotal;
    private JLabel valorConcluidos;
    private JLabel valorAndamento;
    private JLabel valorPlanejados;

    private ApiClient cliente;

    private final Color AZUL_ESCURO = new Color(23, 36, 58);
    private final Color AZUL_SIDEBAR = new Color(29, 45, 69);
    private final Color AZUL = new Color(49, 101, 230);
    private final Color FUNDO = new Color(243, 246, 251);
    private final Color BRANCO = Color.WHITE;
    private final Color TEXTO = new Color(45, 55, 72);
    private final Color CINZA = new Color(115, 125, 140);

    public TelaProjetos() {

        setTitle("Sistema de Projetos");
        setSize(1200, 760);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cliente = new ApiClient();

        criarComponentes();
        criarEventos();
        carregarTabela();
    }

    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(FUNDO);

        // Barra superior
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(BRANCO);
        barraSuperior.setBorder(
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        );

        JLabel tituloSistema = new JLabel("Sistema de Projetos");
        tituloSistema.setFont(new Font("SansSerif", Font.BOLD, 20));
        tituloSistema.setForeground(AZUL_ESCURO);

        JLabel usuario = new JLabel("Usuário");
        usuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usuario.setForeground(CINZA);

        barraSuperior.add(tituloSistema, BorderLayout.WEST);
        barraSuperior.add(usuario, BorderLayout.EAST);

        // Menu lateral
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AZUL_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(
            BorderFactory.createEmptyBorder(25, 15, 20, 15)
        );

        JLabel marca = new JLabel("  SP  |  Projetos");
        marca.setFont(new Font("SansSerif", Font.BOLD, 19));
        marca.setForeground(BRANCO);
        marca.setAlignmentX(LEFT_ALIGNMENT);

        sidebar.add(marca);
        sidebar.add(Box.createVerticalStrut(40));

        sidebar.add(criarItemMenu("Dashboard", false));
        sidebar.add(criarItemMenu("Projetos", true));
        sidebar.add(criarItemMenu("Relatórios", false));
        sidebar.add(criarItemMenu("Configurações", false));

        // Área principal
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(FUNDO);
        conteudo.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JLabel tituloPagina = new JLabel("Dashboard");
        tituloPagina.setFont(new Font("SansSerif", Font.BOLD, 25));
        tituloPagina.setForeground(AZUL_ESCURO);
        tituloPagina.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel(
            "Gerencie e acompanhe seus projetos."
        );
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(CINZA);
        subtitulo.setAlignmentX(LEFT_ALIGNMENT);

        conteudo.add(tituloPagina);
        conteudo.add(Box.createVerticalStrut(5));
        conteudo.add(subtitulo);
        conteudo.add(Box.createVerticalStrut(22));

        // Cards estatísticos
        JPanel painelCards = new JPanel(new GridLayout(1, 4, 15, 0));
        painelCards.setBackground(FUNDO);
        painelCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        painelCards.setAlignmentX(LEFT_ALIGNMENT);

        valorTotal = new JLabel("0");
        valorConcluidos = new JLabel("0");
        valorAndamento = new JLabel("0");
        valorPlanejados = new JLabel("0");

        painelCards.add(
            criarCard("Total de projetos", valorTotal, new Color(49, 101, 230))
        );
        painelCards.add(
            criarCard("Concluídos", valorConcluidos, new Color(30, 150, 95))
        );
        painelCards.add(
            criarCard("Em andamento", valorAndamento, new Color(220, 145, 35))
        );
        painelCards.add(
            criarCard("Planejados", valorPlanejados, new Color(125, 90, 200))
        );

        conteudo.add(painelCards);
        conteudo.add(Box.createVerticalStrut(22));

        // Formulário
        JPanel painelFormulario = new JPanel(new BorderLayout(10, 15));
        painelFormulario.setBackground(BRANCO);
        painelFormulario.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 238)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        );
        painelFormulario.setAlignmentX(LEFT_ALIGNMENT);
        painelFormulario.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 270)
        );

        JLabel tituloFormulario = new JLabel("Cadastro de Projeto");
        tituloFormulario.setFont(new Font("SansSerif", Font.BOLD, 18));
        tituloFormulario.setForeground(AZUL_ESCURO);

        campoNome = new JTextField();
        campoDescricao = new JTextField();

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

        JPanel campos = new JPanel(new GridLayout(2, 2, 15, 12));
        campos.setBackground(BRANCO);

        campos.add(criarCampoComRotulo("Nome do projeto", campoNome));
        campos.add(criarCampoComRotulo("Descrição", campoDescricao));
        campos.add(criarCampoComRotulo("Categoria", comboCategoria));
        campos.add(criarCampoComRotulo("Status", comboStatus));

        botaoCadastrar = criarBotao("Cadastrar projeto", AZUL, BRANCO);
        botaoLimpar = criarBotao("Limpar", new Color(232, 237, 245), TEXTO);

        JPanel botoesFormulario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoesFormulario.setBackground(BRANCO);
        botoesFormulario.add(botaoLimpar);
        botoesFormulario.add(botaoCadastrar);

        JPanel centroFormulario = new JPanel(new BorderLayout(0, 15));
        centroFormulario.setBackground(BRANCO);
        centroFormulario.add(campos, BorderLayout.CENTER);
        centroFormulario.add(botoesFormulario, BorderLayout.SOUTH);

        painelFormulario.add(tituloFormulario, BorderLayout.NORTH);
        painelFormulario.add(centroFormulario, BorderLayout.CENTER);

        conteudo.add(painelFormulario);
        conteudo.add(Box.createVerticalStrut(22));

        // Lista de projetos
        JPanel painelLista = new JPanel(new BorderLayout(0, 15));
        painelLista.setBackground(BRANCO);
        painelLista.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 238)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        );
        painelLista.setAlignmentX(LEFT_ALIGNMENT);

        JLabel tituloLista = new JLabel("Lista de Projetos");
        tituloLista.setFont(new Font("SansSerif", Font.BOLD, 18));
        tituloLista.setForeground(AZUL_ESCURO);

        campoBusca = new JTextField(18);
        campoBusca.setPreferredSize(new Dimension(220, 34));
        botaoBuscar = criarBotao("Buscar", AZUL, BRANCO);
        botaoExcluir = criarBotao(
            "Excluir selecionado",
            new Color(190, 55, 65),
            BRANCO
        );

        JPanel barraLista = new JPanel(new BorderLayout());
        barraLista.setBackground(BRANCO);
        barraLista.add(tituloLista, BorderLayout.WEST);

        JPanel controlesBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controlesBusca.setBackground(BRANCO);
        controlesBusca.add(campoBusca);
        controlesBusca.add(botaoBuscar);
        controlesBusca.add(botaoExcluir);

        barraLista.add(controlesBusca, BorderLayout.EAST);

        modelo = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Categoria", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(38);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setForeground(TEXTO);
        tabela.setBackground(BRANCO);
        tabela.setGridColor(new Color(235, 238, 243));
        tabela.setShowVerticalLines(false);
        tabela.setSelectionBackground(new Color(224, 235, 255));
        tabela.setSelectionForeground(AZUL_ESCURO);
        tabela.setFillsViewportHeight(true);
        tabela.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );
        tabela.getTableHeader().setBackground(new Color(245, 247, 251));
        tabela.getTableHeader().setForeground(TEXTO);
        tabela.getTableHeader().setPreferredSize(new Dimension(0, 38));

        DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
        centralizar.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centralizar);

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(
            BorderFactory.createLineBorder(new Color(235, 238, 243))
        );

        painelLista.add(barraLista, BorderLayout.NORTH);
        painelLista.add(scrollTabela, BorderLayout.CENTER);

        conteudo.add(painelLista);

        // Montagem final da janela
        painelPrincipal.add(barraSuperior, BorderLayout.NORTH);
        painelPrincipal.add(sidebar, BorderLayout.WEST);
        painelPrincipal.add(conteudo, BorderLayout.CENTER);

        setContentPane(painelPrincipal);
    }

    private JPanel criarItemMenu(String texto, boolean selecionado) {

        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        item.setPreferredSize(new Dimension(180, 42));
        item.setBackground(
            selecionado ? new Color(49, 101, 230) : AZUL_SIDEBAR
        );
        item.setBorder(
            BorderFactory.createEmptyBorder(0, 12, 0, 8)
        );

        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(new Font(
            "SansSerif",
            selecionado ? Font.BOLD : Font.PLAIN,
            14
        ));
        rotulo.setForeground(BRANCO);

        item.add(rotulo, BorderLayout.CENTER);
        item.setAlignmentX(LEFT_ALIGNMENT);

        return item;
    }

    private JPanel criarCard(String titulo, JLabel valor, Color cor) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BRANCO);
        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 238)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        );

        JLabel rotulo = new JLabel(titulo);
        rotulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rotulo.setForeground(CINZA);

        valor.setFont(new Font("SansSerif", Font.BOLD, 25));
        valor.setForeground(cor);

        card.add(rotulo);
        card.add(Box.createVerticalStrut(8));
        card.add(valor);

        return card;
    }

    private JPanel criarCampoComRotulo(String rotulo, java.awt.Component campo) {

        JPanel painel = new JPanel(new BorderLayout(0, 6));
        painel.setBackground(BRANCO);

        JLabel label = new JLabel(rotulo);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXTO);

        painel.add(label, BorderLayout.NORTH);
        painel.add(campo, BorderLayout.CENTER);

        return painel;
    }

    private JButton criarBotao(String texto, Color fundo, Color frente) {

        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
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
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(200, 34));
        campo.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 222, 232)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            )
        );
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(200, 34));
    }

    private void criarEventos() {

        botaoLimpar.addActionListener(e -> limparFormulario());
        botaoCadastrar.addActionListener(e -> cadastrar());
        botaoExcluir.addActionListener(e -> excluir());
        botaoBuscar.addActionListener(e -> buscarProjetos());
    }

    private void limparFormulario() {

        campoNome.setText("");
        campoDescricao.setText("");
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        campoNome.requestFocus();
    }

    private void cadastrar() {

        String nome = campoNome.getText();
        String descricao = campoDescricao.getText();

        String categoria = comboCategoria.getSelectedItem().toString();
        String status = comboStatus.getSelectedItem().toString();

        if (nome.isBlank()) {
            JOptionPane.showMessageDialog(this, "Informe o nome.");
            return;
        }

        Projeto projeto = new Projeto(
            nome, descricao, categoria, status
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

            int total = projetos.size();
            int concluidos = 0;
            int andamento = 0;
            int planejados = 0;

            for (Projeto projeto : projetos) {

                modelo.addRow(new Object[]{
                    projeto.getId(),
                    projeto.getNome(),
                    projeto.getCategoria(),
                    projeto.getStatus()
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
                        projeto.getStatus()
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

    private void excluir() {

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Selecione um projeto na tabela."
            );
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linhaSelecionada);

        int id = (int) modelo.getValueAt(linhaModelo, 0);

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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                com.formdev.flatlaf.FlatLightLaf.setup();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TelaProjetos tela = new TelaProjetos();
            tela.setVisible(true);
        });
    }
}
