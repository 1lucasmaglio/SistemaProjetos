package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ApiClient;

public class TelaLogin extends JFrame {

    private ApiClient cliente;

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoLogin;
    private JLabel mensagemLogin;

    public TelaLogin() {

        setTitle("Login - Sistema de Projetos");
        setSize(520, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cliente = new ApiClient();

        // Campos
        campoEmail = new JTextField();
        campoSenha = new JPasswordField();

        botaoLogin = new JButton("Entrar");

        mensagemLogin = new JLabel(" ");
        mensagemLogin.setForeground(Color.decode("#be3741"));
        mensagemLogin.setFont(new Font("Arial", Font.PLAIN, 13));

        // Painel principal: fundo da tela
        JPanel painelPrincipal = new JPanel(new GridBagLayout());

        painelPrincipal.setBackground(
            Color.decode("#f3f6fb")
        );

        // Painel branco: cartão de login
        JPanel painelLogin = new JPanel();

        painelLogin.setLayout(
            new BoxLayout(painelLogin, BoxLayout.Y_AXIS)
        );

        painelLogin.setBackground(Color.WHITE);

        painelLogin.setPreferredSize(
            new Dimension(400, 460)
        );

        painelLogin.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    Color.decode("#e1e6ee")
                ),
                BorderFactory.createEmptyBorder(35, 35, 35, 35)
            )
        );

        // Marca
        JLabel marca = new JLabel(
            "<html><span style='color:#17243a'>SP </span>"
            + "<span style='color:#3165e6'>|</span>"
            + "<span style='color:#17243a'> Projetos</span></html>"
        );

        marca.setFont(new Font("Arial", Font.BOLD, 21));
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Título
        JLabel titulo = new JLabel("Bem-vindo!");

        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        titulo.setForeground(Color.decode("#17243a"));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descrição
        JLabel descricao = new JLabel(
            "Entre com sua conta para acessar seus projetos."
        );

        descricao.setFont(new Font("Arial", Font.PLAIN, 13));
        descricao.setForeground(Color.decode("#737d8c"));
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Labels
        JLabel labelEmail = new JLabel("E-mail");
        JLabel labelSenha = new JLabel("Senha");

        labelEmail.setFont(new Font("Arial", Font.BOLD, 12));
        labelSenha.setFont(new Font("Arial", Font.BOLD, 12));

        labelEmail.setForeground(Color.decode("#2d3748"));
        labelSenha.setForeground(Color.decode("#2d3748"));

        labelEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelSenha.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Estilo dos campos
        campoEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        campoSenha.setFont(new Font("Arial", Font.PLAIN, 13));

        campoEmail.setForeground(Color.decode("#2d3748"));
        campoSenha.setForeground(Color.decode("#2d3748"));

        campoEmail.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    Color.decode("#d7dee8")
                ),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            )
        );

        campoSenha.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    Color.decode("#d7dee8")
                ),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            )
        );

        campoEmail.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );

        campoSenha.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );

        campoEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoSenha.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Estilo do botão
        botaoLogin.setFont(new Font("Arial", Font.BOLD, 12));
        botaoLogin.setBackground(Color.decode("#3165e6"));
        botaoLogin.setForeground(Color.WHITE);
        botaoLogin.setFocusPainted(false);
        botaoLogin.setBorderPainted(false);
        botaoLogin.setOpaque(true);

        botaoLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        botaoLogin.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38)
        );

        // Montagem do cartão
        painelLogin.add(marca);
        painelLogin.add(Box.createVerticalStrut(30));

        painelLogin.add(titulo);
        painelLogin.add(Box.createVerticalStrut(8));

        painelLogin.add(descricao);
        painelLogin.add(Box.createVerticalStrut(30));

        painelLogin.add(labelEmail);
        painelLogin.add(Box.createVerticalStrut(6));

        painelLogin.add(campoEmail);
        painelLogin.add(Box.createVerticalStrut(18));

        painelLogin.add(labelSenha);
        painelLogin.add(Box.createVerticalStrut(6));

        painelLogin.add(campoSenha);
        painelLogin.add(Box.createVerticalStrut(12));

        painelLogin.add(mensagemLogin);
        painelLogin.add(Box.createVerticalStrut(8));

        painelLogin.add(botaoLogin);

        painelPrincipal.add(painelLogin);

        this.add(painelPrincipal);

        // Evento do botão Entrar
        botaoLogin.addActionListener(e -> {

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

            }

        });

    }

}