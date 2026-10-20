
package view;

import java.awt.GridLayout;
import java.util.List;

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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import client.ApiClient;
import model.Projeto;

public class TelaProjetos extends JFrame {

    private JTextField campoNome;
    private JTextField campoDescricao;

    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;
    private JButton botaoExcluir;

    private ApiClient cliente;

    public TelaProjetos() {

        setTitle("Sistema de Projetos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cliente = new ApiClient();

        criarComponentes();
        criarEventos();

        carregarTabela();
    }

    private void criarComponentes() {

        campoNome = new JTextField(20);
        campoDescricao = new JTextField(20);

        comboCategoria = new JComboBox<>();
        comboCategoria.addItem("Web");
        comboCategoria.addItem("Software");
        comboCategoria.addItem("Mobile");
        comboCategoria.addItem("Outro");

        comboStatus = new JComboBox<>();
        comboStatus.addItem("Planejado");
        comboStatus.addItem("Em desenvolvimento");
        comboStatus.addItem("Concluído");

        botaoCadastrar = new JButton("Cadastrar");
        botaoLimpar = new JButton("Limpar");
        botaoExcluir = new JButton("Excluir selecionado");

        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Categoria");
        modelo.addColumn("Status");

        tabela = new JTable(modelo);

        JPanel painelFormulario = new JPanel(
            new GridLayout(6, 2, 10, 10)
        );

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(campoDescricao);

        painelFormulario.add(new JLabel("Categoria:"));
        painelFormulario.add(comboCategoria);

        painelFormulario.add(new JLabel("Status:"));
        painelFormulario.add(comboStatus);

        painelFormulario.add(botaoCadastrar);
        painelFormulario.add(botaoLimpar);

        painelFormulario.add(botaoExcluir);
        painelFormulario.add(new JLabel(""));

        setLayout(
            new BoxLayout(
                getContentPane(),
                BoxLayout.Y_AXIS
            )
        );

        add(painelFormulario);
        add(new JScrollPane(tabela));
    }

    private void criarEventos() {

        botaoLimpar.addActionListener(
            e -> limparFormulario()
        );

        botaoCadastrar.addActionListener(
            e -> cadastrar()
        );

        botaoExcluir.addActionListener(
            e -> excluir()
        );
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

        String categoria = comboCategoria
            .getSelectedItem()
            .toString();

        String status = comboStatus
            .getSelectedItem()
            .toString();

        if (nome.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "Informe o nome."
            );

            return;
        }

        Projeto projeto = new Projeto(
            nome,
            descricao,
            categoria,
            status
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

            List<Projeto> projetos =
                cliente.listarProjetos();

            for (Projeto projeto : projetos) {
                modelo.addRow(
                    new Object[]{
                        projeto.getId(),
                        projeto.getNome(),
                        projeto.getCategoria(),
                        projeto.getStatus()
                    }
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar projetos: " + e.getMessage()
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

        int linhaModelo = tabela.convertRowIndexToModel(
            linhaSelecionada
        );

        int id = (int) modelo.getValueAt(
            linhaModelo,
            0
        );

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

            TelaProjetos tela = new TelaProjetos();
            tela.setVisible(true);
        });
    }
}
