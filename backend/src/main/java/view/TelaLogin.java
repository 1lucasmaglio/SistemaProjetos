package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ApiClient;

public class TelaLogin extends JFrame {

    private final ApiClient cliente;
    private final Preferences preferencias =
            Preferences.userNodeForPackage(TelaProjetos.class);

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoLogin;
    private JLabel mensagemLogin;

    private boolean temaClaro;

    private Color FUNDO;
    private Color SUPERFICIE;
    private Color SUPERFICIE_SECUNDARIA;
    private Color TEXTO;
    private Color CINZA;
    private Color AZUL;
    private Color BORDA;
    private Color ERRO;

    public TelaLogin() {

        setTitle("Login - Sistema de Projetos");
        setSize(560, 680);
        setMinimumSize(new Dimension(500, 620));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cliente = new ApiClient();

        temaClaro = preferencias.get("tema", "dark").equals("light");
        configurarCores();
        criarComponentes();
        configurarEventoLogin();
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

        JPanel painelPrincipal = new JPanel(new GridBagLayout()) {
            private final Image fundo = new ImageIcon(
                    temaClaro ? "frontend/img/pattern-background-light.png"
                              : "frontend/img/pattern-background.png"
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fundo != null) {
                    g.drawImage(fundo, 0, 0, this);
                }
            }
        };
        painelPrincipal.setBackground(FUNDO);

        JPanel painelLogin = new JPanel();
        painelLogin.setLayout(new BoxLayout(painelLogin, BoxLayout.Y_AXIS));
        painelLogin.setBackground(SUPERFICIE);
        painelLogin.setPreferredSize(new Dimension(430, 520));
        painelLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(38, 38, 38, 38)
        ));

        // Marca: mesma imagem e identificação visual da tela de projetos.
        ImageIcon imagemOriginal = new ImageIcon("frontend/img/logo6.png");
        Image imagemRedimensionada = imagemOriginal.getImage()
                .getScaledInstance(46, 46, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(imagemRedimensionada));

        JLabel textoMarca = new JLabel("Projetos");
        textoMarca.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        textoMarca.setForeground(TEXTO);

        JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        marca.setOpaque(false);
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        marca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        marca.add(logo);
        marca.add(textoMarca);

        JLabel titulo = new JLabel("Bem-vindo!");
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        titulo.setForeground(TEXTO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricao = new JLabel(
                "Entre com sua conta para acessar seus projetos."
        );
        descricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descricao.setForeground(CINZA);
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelEmail = criarLabel("E-mail");
        JLabel labelSenha = criarLabel("Senha");

        campoEmail = new JTextField();
        campoSenha = new JPasswordField();
        estilizarCampo(campoEmail);
        estilizarCampo(campoSenha);

        // Botão principal no mesmo padrão dos botões de ação da TelaProjetos.
        // O fundo é desenhado manualmente para não depender do Look and Feel do Swing.
        botaoLogin = new JButton("Entrar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color corBotao = AZUL;
                if (!isEnabled()) {
                    corBotao = new Color(100, 145, 145);
                } else if (getModel().isPressed()) {
                    corBotao = AZUL.darker();
                } else if (getModel().isRollover()) {
                    corBotao = AZUL.brighter();
                }

                g2.setColor(corBotao);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();

                super.paintComponent(g);
            }
        };
        botaoLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        botaoLogin.setForeground(Color.WHITE);
        botaoLogin.setFocusPainted(false);
        botaoLogin.setBorderPainted(false);
        botaoLogin.setContentAreaFilled(false);
        botaoLogin.setOpaque(false);
        botaoLogin.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        botaoLogin.setRolloverEnabled(true);
        botaoLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        botaoLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        botaoLogin.setPreferredSize(new Dimension(350, 46));

        mensagemLogin = new JLabel(" ");
        mensagemLogin.setForeground(ERRO);
        mensagemLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mensagemLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelLogin.add(marca);
        painelLogin.add(Box.createVerticalStrut(10));
        painelLogin.add(titulo);
        painelLogin.add(Box.createVerticalStrut(10));
        painelLogin.add(descricao);
        painelLogin.add(Box.createVerticalStrut(38));
        painelLogin.add(labelEmail);
        painelLogin.add(Box.createVerticalStrut(9));
        painelLogin.add(campoEmail);
        painelLogin.add(Box.createVerticalStrut(24));
        painelLogin.add(labelSenha);
        painelLogin.add(Box.createVerticalStrut(9));
        painelLogin.add(campoSenha);
        painelLogin.add(Box.createVerticalStrut(15));
        painelLogin.add(mensagemLogin);
        painelLogin.add(Box.createVerticalStrut(12));
        painelLogin.add(botaoLogin);

        painelPrincipal.add(painelLogin);
        setContentPane(painelPrincipal);
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
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setPreferredSize(new Dimension(350, 44));
    }

    private void configurarEventoLogin() {
        botaoLogin.addActionListener(e -> {
            botaoLogin.setEnabled(false);

            try {
                String email = campoEmail.getText();
                char[] senha = campoSenha.getPassword();

                try {
                    cliente.login(email, senha);
                } finally {
                    Arrays.fill(senha, '\0');
                    campoSenha.setText("");
                }

                TelaProjetos tela = new TelaProjetos(cliente);
                tela.setVisible(true);
                dispose();

            } catch (Exception erro) {
                mensagemLogin.setText(erro.getMessage());
                botaoLogin.setEnabled(true);
            }
        });
    }
}
