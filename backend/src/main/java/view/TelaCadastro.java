
package view;

import java.awt.*;
import javax.swing.*;

import java.util.Arrays;
import java.util.prefs.Preferences;

import client.ApiClient;

public class TelaCadastro extends JFrame {

    private final ApiClient cliente = new ApiClient();

    private final Preferences preferencias = Preferences.userNodeForPackage(TelaProjetos.class);

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoCadastrar;
    private JButton botaoVoltarLogin;
    private JLabel mensagemCadastro;

    private boolean temaClaro;

    private Color FUNDO;
    private Color SUPERFICIE;
    private Color SUPERFICIE_SECUNDARIA;
    private Color TEXTO;
    private Color CINZA;
    private Color AZUL;
    private Color BORDA;
    private Color ERRO;

    public TelaCadastro() {

        setTitle("Cadastrar conta - Sistema de Projetos");
        setSize(560, 720);
        setMinimumSize(new Dimension(500, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        temaClaro = preferencias.get("tema", "dark").equals("light");

        configurarCores();
        criarComponentes();
        configurarEventoCadastro();
    }

    private void configurarCores() {

        AZUL = new Color(0, 153, 153);
        ERRO = new Color(239, 98, 108);

        if (temaClaro) {
            FUNDO = new Color(247, 248, 250);
            SUPERFICIE = Color.WHITE;
            SUPERFICIE_SECUNDARIA = new Color(242, 244, 247);
            TEXTO = new Color(45, 50, 60);
            CINZA = new Color(105, 113, 125);
            BORDA = new Color(220, 224, 230);
        } else {
            FUNDO = new Color(28, 28, 30);
            SUPERFICIE = new Color(36, 36, 38);
            SUPERFICIE_SECUNDARIA = new Color(48, 48, 51);
            TEXTO = new Color(229, 229, 231);
            CINZA = new Color(161, 161, 166);
            BORDA = new Color(58, 58, 60);
        }
    }

    private void criarComponentes() {

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(FUNDO);
        painelPrincipal.setLayout(new GridBagLayout());

        JPanel painelCadastro = new JPanel();
        painelCadastro.setBackground(SUPERFICIE);
        painelCadastro.setLayout(new BoxLayout(painelCadastro, BoxLayout.Y_AXIS));
        painelCadastro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(35, 40, 15, 40)));

        // Logo com identificação visual
        ImageIcon imagemOriginal = new ImageIcon("frontend/img/logoTitle.png");

        int largura = 180;
        int altura = imagemOriginal.getIconHeight() * largura
                / imagemOriginal.getIconWidth();

        Image imagemRedimensionada = imagemOriginal.getImage()
                .getScaledInstance(largura, altura, Image.SCALE_SMOOTH);

        JLabel marca = new JLabel(new ImageIcon(imagemRedimensionada));
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Criar conta");
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        titulo.setForeground(TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Cadastre-se para começar a organizar seus projetos.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(CINZA);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoNome = new JTextField();
        campoEmail = new JTextField();
        campoSenha = new JPasswordField();

        estilizarCampo(campoNome);
        estilizarCampo(campoEmail);
        estilizarCampo(campoSenha);

        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botaoCadastrar.setForeground(Color.WHITE);
        botaoCadastrar.setBackground(new Color(52, 58, 62));
        botaoCadastrar.setFocusPainted(false);
        botaoCadastrar.setBorderPainted(false);
        botaoCadastrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        botaoCadastrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        botaoVoltarLogin = new JButton("Já possui uma conta? Entrar");
        botaoVoltarLogin.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        botaoVoltarLogin.setForeground(Color.WHITE);
        botaoVoltarLogin.setText("<html>Já possui uma conta? <u>Entrar</u></html>");
        botaoVoltarLogin.setBackground(SUPERFICIE);
        botaoVoltarLogin.setFocusPainted(false);
        botaoVoltarLogin.setBorderPainted(false);
        botaoVoltarLogin.setContentAreaFilled(false);
        botaoVoltarLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        botaoVoltarLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        mensagemCadastro = new JLabel(" ");
        mensagemCadastro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mensagemCadastro.setForeground(ERRO);
        mensagemCadastro.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Ordem dos componentes
        painelCadastro.add(marca);
        painelCadastro.add(Box.createVerticalStrut(10));
        painelCadastro.add(titulo);
        painelCadastro.add(Box.createVerticalStrut(8));
        painelCadastro.add(subtitulo);
        painelCadastro.add(Box.createVerticalStrut(30));

        painelCadastro.add(criarLabel("Nome"));
        painelCadastro.add(Box.createVerticalStrut(8));
        painelCadastro.add(campoNome);
        painelCadastro.add(Box.createVerticalStrut(18));

        painelCadastro.add(criarLabel("E-mail"));
        painelCadastro.add(Box.createVerticalStrut(8));
        painelCadastro.add(campoEmail);
        painelCadastro.add(Box.createVerticalStrut(18));

        painelCadastro.add(criarLabel("Senha"));
        painelCadastro.add(Box.createVerticalStrut(8));
        painelCadastro.add(campoSenha);
        painelCadastro.add(Box.createVerticalStrut(25));

        painelCadastro.add(botaoCadastrar);
        painelCadastro.add(Box.createVerticalStrut(0));
        painelCadastro.add(mensagemCadastro);
        painelCadastro.add(Box.createVerticalStrut(0));
        painelCadastro.add(botaoVoltarLogin);

        painelPrincipal.add(painelCadastro);

        add(painelPrincipal);
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setBackground(SUPERFICIE_SECUNDARIA);
        campo.setCaretColor(TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setPreferredSize(new Dimension(350, 44));
    }

    private void configurarEventoCadastro() {

        botaoCadastrar.addActionListener(e -> {

            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            char[] senha = campoSenha.getPassword();

            if (nome.isEmpty() || email.isEmpty() || senha.length == 0) {
                mensagemCadastro.setText("Preencha todos os campos.");
                Arrays.fill(senha, '\0');
                return;
            }

            botaoCadastrar.setEnabled(false);

            try {

                cliente.cadastrarUsuario(nome, email, senha);

                JOptionPane.showMessageDialog(
                        this,
                        "Conta cadastrada com sucesso!",
                        "Cadastro",
                        JOptionPane.INFORMATION_MESSAGE);

                TelaLogin telaLogin = new TelaLogin();
                telaLogin.setVisible(true);
                dispose();

            } catch (Exception ex) {

                mensagemCadastro.setText(ex.getMessage());
                botaoCadastrar.setEnabled(true);

            } finally {

                Arrays.fill(senha, '\0');
                campoSenha.setText("");

            }
        });

        botaoVoltarLogin.addActionListener(e -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
            dispose();
        });
    }
}
