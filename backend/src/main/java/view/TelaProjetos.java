
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import client.ApiClient;
import model.Projeto;

public class TelaProjetos extends JFrame {

    private JTextField campoNome;
    private JTextField campoDescricao;
    private JTextField campoBusca;
    private DatePicker campoPrazo;

    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;
    private JButton botaoBuscar;
    private JButton botaoSair;

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

        botaoSair = criarBotao(
            "Sair",
            new Color(190, 55, 65),
            BRANCO
        );

        JPanel painelUsuario = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 15, 0)
        );

        painelUsuario.setBackground(BRANCO);
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

        // Formulário de cadastro
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
        campos.setBackground(BRANCO);

        campos.add(criarCampoComRotulo("Nome do projeto", campoNome));
        campos.add(criarCampoComRotulo("Descrição", campoDescricao));
        campos.add(criarCampoComRotulo("Categoria", comboCategoria));
        campos.add(criarCampoComRotulo("Status", comboStatus));
        campos.add(criarCampoComRotulo("Prazo (DD-MM-AAAA)", campoPrazo));

        botaoCadastrar = criarBotao("Cadastrar projeto", AZUL, BRANCO);
        botaoLimpar = criarBotao(
            "Limpar",
            new Color(232, 237, 245),
            TEXTO
        );

        JPanel botoesFormulario = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
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

        JPanel barraLista = new JPanel(new BorderLayout());
        barraLista.setBackground(BRANCO);
        barraLista.add(tituloLista, BorderLayout.WEST);

        JPanel controlesBusca = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        controlesBusca.setBackground(BRANCO);
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

        // Configuração da coluna Ações
        tabela.getColumnModel().getColumn(6).setCellRenderer(
            new ActionCellRenderer()
        );

        tabela.getColumnModel().getColumn(6).setCellEditor(
            new ActionCellEditor()
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

    private JPanel criarCampoComRotulo(
        String rotulo,
        Component campo
    ) {

        JPanel painel = new JPanel(new BorderLayout(0, 6));
        painel.setBackground(BRANCO);

        JLabel label = new JLabel(rotulo);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
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

    // Cadastro de um novo projeto.
    private void cadastrar() {

        String nome = campoNome.getText().trim();
        String descricao = campoDescricao.getText().trim();
        LocalDate prazo = campoPrazo.getDate();

        String categoria = comboCategoria.getSelectedItem().toString();
        String status = comboStatus.getSelectedItem().toString();

        if (nome.isBlank()) {
            JOptionPane.showMessageDialog(this, "Informe o nome.");
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

    // Carrega todos os projetos na tabela.
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

    // Busca projetos pelo nome, categoria ou status.
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

    // Abre o popup de edição do projeto selecionado.
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

    // Cria e exibe a janela modal de edição.
    private void abrirPopupEdicao(Projeto projeto) {

        JDialog dialog = new JDialog(
            this,
            "Editar projeto",
            true
        );

        dialog.setResizable(false);

        JPanel painel = new JPanel(new BorderLayout(15, 15));
        painel.setBackground(BRANCO);
        painel.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        // Define um tamanho maior para acomodar todos os campos.
        painel.setPreferredSize(new Dimension(560, 500));

        JLabel titulo = new JLabel("Editar Projeto");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(AZUL_ESCURO);

        // Campos preenchidos com os dados atuais.
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

        // Organiza os cinco campos verticalmente.
        JPanel campos = new JPanel(new GridLayout(5, 1, 10, 14));
        campos.setBackground(BRANCO);

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
            new Color(232, 237, 245),
            TEXTO
        );

        JPanel botoes = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        botoes.setBackground(BRANCO);
        botoes.add(botaoCancelar);
        botoes.add(botaoSalvar);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(campos, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);

        dialog.setContentPane(painel);

        // Calcula o tamanho da janela com base no conteúdo.
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

    // Exclui o projeto correspondente ao ID recebido.
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

    // Desenha os botões na coluna Ações.
    private class ActionCellRenderer extends JPanel
        implements TableCellRenderer {

        private final JButton editar = new JButton("✎");
        private final JButton excluir = new JButton("🗑");

        public ActionCellRenderer() {

            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 3));
            setBackground(BRANCO);

            configurarBotaoAcao(editar, AZUL);
            configurarBotaoAcao(excluir, new Color(190, 55, 65));

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

            return this;
        }
    }

    // Controla os cliques nos botões da coluna Ações.
    private class ActionCellEditor extends AbstractCellEditor
        implements TableCellEditor {

        private final JPanel painel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 4, 3)
        );

        private final JButton editar = new JButton("✎");
        private final JButton excluir = new JButton("🗑");

        private int idSelecionado;

        public ActionCellEditor() {

            painel.setBackground(BRANCO);

            configurarBotaoAcao(editar, AZUL);
            configurarBotaoAcao(excluir, new Color(190, 55, 65));

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

    // Configuração visual dos botões de ação.
    private void configurarBotaoAcao(JButton botao, Color cor) {

        botao.setFont(new Font("SansSerif", Font.BOLD, 15));
        botao.setForeground(cor);
        botao.setBackground(BRANCO);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.setPreferredSize(new Dimension(30, 27));
        botao.setMargin(new Insets(0, 0, 0, 0));
        botao.setCursor(new java.awt.Cursor(
            java.awt.Cursor.HAND_CURSOR
        ));
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                com.formdev.flatlaf.FlatLightLaf.setup();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TelaLogin tela = new TelaLogin();
            tela.setVisible(true);
        });
    }
}
