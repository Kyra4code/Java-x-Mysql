import javax.swing.*;
import java.awt.*;
import java.net.ConnectException;
import java.sql.*;
import java.util.*;


public class Main{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de dados do usuario");
        JTextField nome = new JTextField(10);
        JTextField idade = new JTextField(10);
        JTextField email = new JTextField(10);
        JTextField senha = new JTextField(10);
        JButton botao = new JButton("Cadastrar");
        JButton botao2 = new JButton("Ver usuario");
        JButton botao3 = new JButton("Atualizar senha com email");
        JButton botao4 = new JButton("Excluir usuário com email");

        botao.setBounds(300, 300, 100, 50);
        botao.addActionListener(e -> {
            try {
                int year = Integer.parseInt(idade.getText());
                inserir(nome.getText(), year, email.getText(), senha.getText());
            }catch(NumberFormatException nfe){
                JOptionPane.showMessageDialog(null, "Os campos devem ser preenchidos corretamente.");
            }
        });

        botao2.addActionListener(e -> {
           listar(nome.getText());
        });

        botao3.addActionListener(e -> {
            atualizar(email.getText(), senha.getText());
        });

        botao4.addActionListener(e -> {
            deletar(email.getText());
        });

        nome.setBorder(BorderFactory.createTitledBorder("Nome"));
        idade.setBorder(BorderFactory.createTitledBorder("Idade"));
        email.setBorder(BorderFactory.createTitledBorder("Email"));
        senha.setBorder(BorderFactory.createTitledBorder("Senha"));

        frame.add(nome);
        frame.add(idade);
        frame.add(email);
        frame.add(senha);
        frame.add(botao);
        frame.add(botao2);
        frame.add(botao3);
        frame.add(botao4);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public static Connection connection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/banco", "root", "");
        }catch(ClassNotFoundException e){
            System.out.println("Error ao encontrar o Driver");
        }catch(SQLException e){
            System.out.println("Erro ao conectar ao banco");
        }
        return null;
    }

    public static void inserir(String name, int idade, String email, String senha){
        Connection con = connection();
        try {
        String sql = "insert into usuario (nome, idade, email, senha) values (?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.setInt(2, idade);
        stmt.setString(3, email);
        stmt.setString(4, senha);
        stmt.executeUpdate();
        System.out.println("Inserido com sucesso!");
        JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
        }catch(SQLException e){
            System.out.println("Erro ao inserir na tabela");
            JOptionPane.showMessageDialog(null, "Erro ao inserir o dado na tabela");
        }
    }
    public static void listar(String nome){
        Connection con = connection();
        String sql = "select * from usuario where nome = ?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                String idade = rs.getString("idade");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                JOptionPane.showMessageDialog(null, "Seu nome é " + nome + " Você tem " + idade + " anos" + " seu email é " + email + " e sua senha é " + senha);
                con.close();
            }else if(rs.next() == false){
                JOptionPane.showMessageDialog(null, "Usuário não encontrado!");
            }
        }catch(SQLException e){
            System.out.println("Erro ao buscar o usuario no banco");
            JOptionPane.showMessageDialog(null, "Erro ao buscar o usuario no banco");
        }finally {
            try {
                con.close();
            }catch(SQLException i){}
        }
    }
    public static void atualizar(String email, String senha){
        try {
            Connection con = connection();
            String sql = "update usuario set senha = ? where email = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, senha);
            stmt.setString(2, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
        }catch (SQLException e){
            System.out.println("Erro ao atualizar o usuario no banco");
        }
    }
    public static void deletar(String email){
        try{
            Connection con = connection();
            String sql = "delete from usuario where email = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deletado com sucesso!");
        }catch(SQLException e){

            System.out.println("Erro ao deletar o usuario no banco");
        }
    }
}