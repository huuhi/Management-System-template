package GUI;

import DAO.dao.UserDao;
import DAO.domain.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginSystem extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private JTextField loginUsername, registerUsername;
    private JPasswordField loginPassword, registerPassword, confirmPassword;
    private final UserDao userDao = new UserDao();
    public LoginSystem() {
        setTitle("用户管理系统");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // 创建登录面板和注册面板
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createRegisterPanel(), "REGISTER");
        
        add(cardPanel);
        cardLayout.show(cardPanel, "LOGIN");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("用户登录", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        // 用户名
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("用户名:"), gbc);
        
        gbc.gridx = 1;
        loginUsername = new JTextField(15);
        panel.add(loginUsername, gbc);

        // 密码
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("密码:"), gbc);
        
        gbc.gridx = 1;
        loginPassword = new JPasswordField(15);
        panel.add(loginPassword, gbc);

        // 登录按钮
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("登录");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> attemptLogin());
        panel.add(loginButton, gbc);

        // 注册链接
        gbc.gridy = 4;
        JLabel registerLink = new JLabel("<html><u>没有账号？立即注册</u></html>");
        registerLink.setForeground(Color.BLUE);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "REGISTER");
            }
        });
        panel.add(registerLink, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("用户注册", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        // 用户名
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("用户名:"), gbc);
        
        gbc.gridx = 1;
        registerUsername = new JTextField(15);
        panel.add(registerUsername, gbc);

        // 密码
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("密码:"), gbc);
        
        gbc.gridx = 1;
        registerPassword = new JPasswordField(15);
        panel.add(registerPassword, gbc);

        // 确认密码
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("确认密码:"), gbc);
        
        gbc.gridx = 1;
        confirmPassword = new JPasswordField(15);
        panel.add(confirmPassword, gbc);

        // 注册按钮
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("注册");
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> attemptRegister());
        panel.add(registerButton, gbc);

        // 返回登录链接
        gbc.gridy = 5;
        JLabel loginLink = new JLabel("<html><u>已有账号？返回登录</u></html>");
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "LOGIN");
            }
        });
        panel.add(loginLink, gbc);

        return panel;
    }

    private void attemptLogin() {
        String username = loginUsername.getText().trim();
        String password = new String(loginPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 这里添加实际的登录验证逻辑
//        TODO 进行登录校验
        User user = userDao.verifyUser(username, password);
        if(user==null){
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
//         如果没有问题，根据用户的角色跳转到不同的页面
        JOptionPane.showMessageDialog(this, "登录成功！\n用户名: " + username, "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    private void attemptRegister() {
        String username = registerUsername.getText().trim();
        String password = new String(registerPassword.getPassword());
        String confirm = new String(confirmPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段必须填写", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        


//        TODO 进行注册
        // 这里添加实际的注册逻辑
        JOptionPane.showMessageDialog(this, "注册成功！\n用户名: " + username, "成功", JOptionPane.INFORMATION_MESSAGE);
        
        // 清空注册表单
        registerUsername.setText("");
        registerPassword.setText("");
        confirmPassword.setText("");
        
        // 切换到登录界面
        cardLayout.show(cardPanel, "LOGIN");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            
            new LoginSystem().setVisible(true);
        });
    }
}