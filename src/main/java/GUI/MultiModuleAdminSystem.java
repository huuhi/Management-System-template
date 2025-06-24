package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MultiModuleAdminSystem extends JFrame {

    private final JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    // 用户管理组件
    private UserManagementPanel userManagementPanel;
    
    // 图书管理组件
    private BookManagementPanel bookManagementPanel;

    public MultiModuleAdminSystem() {
        // 设置窗口属性
        setTitle("多模块管理系统");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建主面板
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);
        
        // 创建顶部菜单栏
        createTopMenu();
        
        // 创建主内容区
        createMainContent();
        
        // 默认显示用户管理
        switchToPanel("users");
    }

    private void createTopMenu() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(70, 130, 180));
        menuPanel.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        // 左侧系统标题
        JLabel titleLabel = new JLabel("多模块管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        menuPanel.add(titleLabel, BorderLayout.WEST);
        
        // 右侧导航按钮
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navPanel.setOpaque(false);
        
        // 用户管理按钮
        JButton usersBtn = createNavButton("用户管理", "👥", new Color(46, 204, 113));
        usersBtn.addActionListener(e -> switchToPanel("users"));
        
        // 图书管理按钮
        JButton booksBtn = createNavButton("图书管理", "📚", new Color(52, 152, 219));
        booksBtn.addActionListener(e -> switchToPanel("books"));
        
        navPanel.add(usersBtn);
        navPanel.add(booksBtn);
        
        menuPanel.add(navPanel, BorderLayout.EAST);
        mainPanel.add(menuPanel, BorderLayout.NORTH);
    }
    
    private JButton createNavButton(String text, String icon, Color bgColor) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + text + "</center></html>");
        button.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    private void createMainContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // 创建用户管理面板
        userManagementPanel = new UserManagementPanel();
        contentPanel.add(userManagementPanel, "users");
        
        // 创建图书管理面板
        bookManagementPanel = new BookManagementPanel();
        contentPanel.add(bookManagementPanel, "books");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void switchToPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置UI风格
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            MultiModuleAdminSystem frame = new MultiModuleAdminSystem();
            frame.setVisible(true);
        });
    }
    
    // 用户管理面板
    class UserManagementPanel extends JPanel {
        private final DefaultTableModel tableModel;
        private final JComboBox<Integer> pageSizeComboBox;
        private final JComboBox<Integer> pageComboBox;
        private List<User> userData;
        private List<User> filteredData;
        private int currentPage = 1;
        private int pageSize = 10;
        private int totalPages = 1;
        private final JLabel statusLabel;
        private final JTextField searchField;

        public UserManagementPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(new EmptyBorder(10, 10, 10, 10));
            
            // 顶部面板 - 标题和操作按钮
            JPanel topPanel = new JPanel(new BorderLayout());
            
            JLabel titleLabel = new JLabel("用户管理");
            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
            topPanel.add(titleLabel, BorderLayout.WEST);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            
            // 添加用户按钮
            JButton addButton = new JButton("添加用户");
            addButton.setBackground(new Color(46, 204, 113));
            addButton.setForeground(Color.WHITE);
            addButton.setFocusPainted(false);
            addButton.addActionListener(e -> showAddUserDialog());
            buttonPanel.add(addButton);
            
            topPanel.add(buttonPanel, BorderLayout.EAST);
            add(topPanel, BorderLayout.NORTH);
            
                     // 搜索面板 - 已修复：添加搜索框并正确布局
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            searchPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

            searchField = new JTextField(25);
            JButton searchButton = new JButton("搜索");
            searchButton.setBackground(new Color(52, 152, 219));
            searchButton.setForeground(Color.WHITE);
            searchButton.setFocusPainted(false);

            searchPanel.add(new JLabel("搜索用户:"));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            // 将搜索面板添加到顶部面板下方，表格面板上方
            add(searchPanel, BorderLayout.PAGE_START); // 或者使用 add(searchPanel, BorderLayout.NORTH)

            
            // 用户表格
            JPanel tablePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"用户ID", "姓名", "邮箱", "电话", "状态", "操作"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5; // 只有操作列可编辑
                }
            };

            JTable userTable = new JTable(tableModel);
            userTable.setRowHeight(40);
            userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            userTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            
            // 设置表头样式
            JTableHeader header = userTable.getTableHeader();
            header.setFont(new Font("微软雅黑", Font.BOLD, 14));
            header.setBackground(new Color(70, 130, 180));
            header.setForeground(Color.WHITE);
            header.setReorderingAllowed(false);
            
            // 添加操作按钮
            userTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            userTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "users"));
            
            JScrollPane scrollPane = new JScrollPane(userTable);
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            add(tablePanel, BorderLayout.CENTER);
            
            // 分页控制面板
            JPanel paginationPanel = new JPanel(new BorderLayout());
            paginationPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            
            // 分页导航
            JButton firstButton = new JButton("首页");
            JButton prevButton = new JButton("上一页");
            JButton nextButton = new JButton("下一页");
            JButton lastButton = new JButton("末页");
            
            pageComboBox = new JComboBox<>();
            
            controlPanel.add(firstButton);
            controlPanel.add(prevButton);
            controlPanel.add(new JLabel("页码:"));
            controlPanel.add(pageComboBox);
            controlPanel.add(nextButton);
            controlPanel.add(lastButton);
            
            // 每页显示数量
            JPanel pageSizePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pageSizePanel.add(new JLabel("每页显示:"));
            pageSizeComboBox = new JComboBox<>(new Integer[]{5, 10, 20, 50});
            pageSizeComboBox.setSelectedItem(pageSize);
            pageSizeComboBox.addActionListener(e -> {
                pageSize = (int) pageSizeComboBox.getSelectedItem();
                currentPage = 1;
                loadTableData();
            });
            pageSizePanel.add(pageSizeComboBox);
            
            paginationPanel.add(controlPanel, BorderLayout.CENTER);
            paginationPanel.add(pageSizePanel, BorderLayout.EAST);
            
            // 状态栏
            statusLabel = new JLabel("加载中...");
            statusLabel.setBorder(new EmptyBorder(5, 15, 5, 15));
            statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            
            paginationPanel.add(statusLabel, BorderLayout.SOUTH);
            add(paginationPanel, BorderLayout.SOUTH);
            
            // 添加事件监听
            firstButton.addActionListener(e -> goToFirstPage());
            prevButton.addActionListener(e -> goToPrevPage());
            nextButton.addActionListener(e -> goToNextPage());
            lastButton.addActionListener(e -> goToLastPage());
            
            pageComboBox.addActionListener(e -> {
                if (pageComboBox.getSelectedItem() != null) {
                    int selectedPage = (int) pageComboBox.getSelectedItem();
                    if (selectedPage != currentPage) {
                        currentPage = selectedPage;
                        loadTableData();
                    }
                }
            });
            
            searchButton.addActionListener(e -> {
                String keyword = searchField.getText().trim().toLowerCase();
                filterData(keyword);
            });
            
            // 初始化数据
            initializeMockData();
            filteredData = new ArrayList<>(userData);
            loadTableData();
        }

        private void initializeMockData() {
            userData = new ArrayList<>();
            // 生成50条模拟数据
            for (int i = 1; i <= 50; i++) {
                userData.add(new User(
                    i,
                    "用户" + i,
                    "user" + i + "@example.com",
                    "138" + String.format("%08d", i),
                    i % 4 == 0 ? "禁用" : "激活"
                ));
            }
        }

        private void loadTableData() {
            // 计算总页数
            totalPages = (int) Math.ceil((double) filteredData.size() / pageSize);
            if (totalPages == 0) totalPages = 1;
            
            // 确保当前页有效
            if (currentPage > totalPages) currentPage = totalPages;
            if (currentPage < 1) currentPage = 1;
            
            // 更新页面选择器
            updatePageSelector();
            
            // 显示当前页数据
            displayPageData();
            
            // 更新状态栏
            updateStatusBar();
        }

        private void updatePageSelector() {
            // 临时移除事件监听器
            ActionListener[] listeners = pageComboBox.getActionListeners();
            for (ActionListener listener : listeners) {
                pageComboBox.removeActionListener(listener);
            }
            
            pageComboBox.removeAllItems();
            for (int i = 1; i <= totalPages; i++) {
                pageComboBox.addItem(i);
            }
            pageComboBox.setSelectedItem(currentPage);
            
            // 重新添加事件监听
            for (ActionListener listener : listeners) {
                pageComboBox.addActionListener(listener);
            }
        }

        private void displayPageData() {
            // 清空表格
            tableModel.setRowCount(0);
            
            if (filteredData.isEmpty()) {
                return;
            }
            
            // 计算起始索引
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, filteredData.size());
            
            // 添加当前页数据
            for (int i = start; i < end; i++) {
                User user = filteredData.get(i);
                tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getStatus(),
                    "编辑|删除" // 操作按钮文本
                });
            }
        }
        
        private void updateStatusBar() {
            int total = filteredData.size();
            int start = (currentPage - 1) * pageSize + 1;
            int end = Math.min(currentPage * pageSize, total);
            
            String status = String.format("显示 %d 到 %d 条，共 %d 条记录", 
                                        start, end, total);
            statusLabel.setText(status);
        }
        
        private void goToFirstPage() {
            if (currentPage != 1) {
                currentPage = 1;
                loadTableData();
            }
        }
        
        private void goToPrevPage() {
            if (currentPage > 1) {
                currentPage--;
                loadTableData();
            }
        }
        
        private void goToNextPage() {
            if (currentPage < totalPages) {
                currentPage++;
                loadTableData();
            }
        }
        
        private void goToLastPage() {
            if (currentPage != totalPages) {
                currentPage = totalPages;
                loadTableData();
            }
        }
        
        private void filterData(String keyword) {
            filteredData = new ArrayList<>();
            
            if (keyword.isEmpty()) {
                filteredData.addAll(userData);
            } else {
                for (User user : userData) {
                    if (user.getName().toLowerCase().contains(keyword) ||
                        user.getEmail().toLowerCase().contains(keyword) ||
                        user.getPhone().contains(keyword) ||
                        user.getStatus().toLowerCase().contains(keyword)) {
                        filteredData.add(user);
                    }
                }
            }
            
            currentPage = 1;
            loadTableData();
        }
        
        public void editUser(int tableRow) {
            // 计算实际数据索引
            int dataIndex = (currentPage - 1) * pageSize + tableRow;
            if (dataIndex < 0 || dataIndex >= filteredData.size()) return;
            
            User user = filteredData.get(dataIndex);
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "编辑用户", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // 表单字段
            JLabel idLabel = new JLabel("用户ID:");
            JTextField idField = new JTextField(String.valueOf(user.getId()));
            idField.setEditable(false);
            
            JLabel nameLabel = new JLabel("姓名:");
            JTextField nameField = new JTextField(user.getName());
            
            JLabel emailLabel = new JLabel("邮箱:");
            JTextField emailField = new JTextField(user.getEmail());
            
            JLabel phoneLabel = new JLabel("电话:");
            JTextField phoneField = new JTextField(user.getPhone());
            
            JLabel statusLabel = new JLabel("状态:");
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"激活", "禁用"});
            statusCombo.setSelectedItem(user.getStatus());
            
            formPanel.add(idLabel);
            formPanel.add(idField);
            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(emailLabel);
            formPanel.add(emailField);
            formPanel.add(phoneLabel);
            formPanel.add(phoneField);
            formPanel.add(statusLabel);
            formPanel.add(statusCombo);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            
            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("保存");
            saveButton.setBackground(new Color(46, 204, 113));
            saveButton.setForeground(Color.WHITE);
            saveButton.setFocusPainted(false);
            
            JButton cancelButton = new JButton("取消");
            cancelButton.setBackground(new Color(231, 76, 60));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);
            
            saveButton.addActionListener(e -> {
                // 更新用户信息
                user.setName(nameField.getText());
                user.setEmail(emailField.getText());
                user.setPhone(phoneField.getText());
                user.setStatus((String) statusCombo.getSelectedItem());
                
                // 刷新表格
                loadTableData();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this, 
                    "用户信息已更新!", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        }
        
        public void deleteUser(int tableRow) {
            // 计算实际数据索引
            int dataIndex = (currentPage - 1) * pageSize + tableRow;
            if (dataIndex < 0 || dataIndex >= filteredData.size()) return;
            
            User user = filteredData.get(dataIndex);
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "确定要删除用户 " + user.getName() + " (ID: " + user.getId() + ") 吗?",
                "确认删除",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // 从原始数据和过滤数据中删除
                userData.remove(user);
                filteredData.remove(user);
                
                // 重新加载数据
                loadTableData();
                
                JOptionPane.showMessageDialog(this, "用户已删除!");
            }
        }
        
        private void showAddUserDialog() {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "添加新用户", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // 表单字段
            JLabel nameLabel = new JLabel("姓名:");
            JTextField nameField = new JTextField();
            
            JLabel emailLabel = new JLabel("邮箱:");
            JTextField emailField = new JTextField();
            
            JLabel phoneLabel = new JLabel("电话:");
            JTextField phoneField = new JTextField();
            
            JLabel statusLabel = new JLabel("状态:");
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"激活", "禁用"});
            
            formPanel.add(nameLabel);
            formPanel.add(nameField);
            formPanel.add(emailLabel);
            formPanel.add(emailField);
            formPanel.add(phoneLabel);
            formPanel.add(phoneField);
            formPanel.add(statusLabel);
            formPanel.add(statusCombo);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            
            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("保存");
            saveButton.setBackground(new Color(46, 204, 113));
            saveButton.setForeground(Color.WHITE);
            saveButton.setFocusPainted(false);
            
            JButton cancelButton = new JButton("取消");
            cancelButton.setBackground(new Color(231, 76, 60));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);
            
            saveButton.addActionListener(e -> {
                // 创建新用户
                int newId = userData.size() + 1;
                User newUser = new User(
                    newId,
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    (String) statusCombo.getSelectedItem()
                );
                
                // 添加到数据源
                userData.add(newUser);
                filteredData.add(newUser);
                
                // 刷新表格
                loadTableData();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this, 
                    "用户添加成功!", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        }
        
        // 用户数据模型
        class User {
            private int id;
            private String name;
            private String email;
            private String phone;
            private String status;

            public User(int id, String name, String email, String phone, String status) {
                this.id = id;
                this.name = name;
                this.email = email;
                this.phone = phone;
                this.status = status;
            }

            public int getId() { return id; }
            public String getName() { return name; }
            public String getEmail() { return email; }
            public String getPhone() { return phone; }
            public String getStatus() { return status; }
            
            public void setName(String name) { this.name = name; }
            public void setEmail(String email) { this.email = email; }
            public void setPhone(String phone) { this.phone = phone; }
            public void setStatus(String status) { this.status = status; }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                User user = (User) obj;
                return id == user.id;
            }
        }
    }
    
    // 图书管理面板
    class BookManagementPanel extends JPanel {
        private JTable bookTable;
        private DefaultTableModel tableModel;
        private JComboBox<Integer> pageSizeComboBox;
        private JComboBox<Integer> pageComboBox;
        private List<Book> bookData;
        private List<Book> filteredData;
        private int currentPage = 1;
        private int pageSize = 10;
        private int totalPages = 1;
        private JLabel statusLabel;
        private JTextField searchField;

        public BookManagementPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(new EmptyBorder(10, 10, 10, 10));
            
            // 顶部面板 - 标题和操作按钮
            JPanel topPanel = new JPanel(new BorderLayout());
            
            JLabel titleLabel = new JLabel("图书管理");
            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
            topPanel.add(titleLabel, BorderLayout.WEST);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            
            // 添加图书按钮
            JButton addButton = new JButton("添加图书");
            addButton.setBackground(new Color(46, 204, 113));
            addButton.setForeground(Color.WHITE);
            addButton.setFocusPainted(false);
            addButton.addActionListener(e -> showAddBookDialog());
            buttonPanel.add(addButton);
            
            topPanel.add(buttonPanel, BorderLayout.EAST);
            add(topPanel, BorderLayout.NORTH);

            // 搜索面板 - 已修复：添加搜索框并正确布局
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            searchPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

            searchField = new JTextField(25);
            JButton searchButton = new JButton("搜索");
            searchButton.setBackground(new Color(52, 152, 219));
            searchButton.setForeground(Color.WHITE);
            searchButton.setFocusPainted(false);

            searchPanel.add(new JLabel("搜索图书:"));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            // 将搜索面板添加到顶部面板下方，表格面板上方
            add(searchPanel, BorderLayout.PAGE_START); //
            
            // 图书表格
            JPanel tablePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"图书ID", "书名", "作者", "ISBN", "分类", "库存", "操作"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 6; // 只有操作列可编辑
                }
            };
            
            bookTable = new JTable(tableModel);
            bookTable.setRowHeight(40);
            bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bookTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            
            // 设置表头样式
            JTableHeader header = bookTable.getTableHeader();
            header.setFont(new Font("微软雅黑", Font.BOLD, 14));
            header.setBackground(new Color(70, 130, 180));
            header.setForeground(Color.WHITE);
            header.setReorderingAllowed(false);
            
            // 添加操作按钮
            bookTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
            bookTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), "books"));
            
            JScrollPane scrollPane = new JScrollPane(bookTable);
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            add(tablePanel, BorderLayout.CENTER);
            
            // 分页控制面板
            JPanel paginationPanel = new JPanel(new BorderLayout());
            paginationPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            
            // 分页导航
            JButton firstButton = new JButton("首页");
            JButton prevButton = new JButton("上一页");
            JButton nextButton = new JButton("下一页");
            JButton lastButton = new JButton("末页");
            
            pageComboBox = new JComboBox<>();
            
            controlPanel.add(firstButton);
            controlPanel.add(prevButton);
            controlPanel.add(new JLabel("页码:"));
            controlPanel.add(pageComboBox);
            controlPanel.add(nextButton);
            controlPanel.add(lastButton);
            
            // 每页显示数量
            JPanel pageSizePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pageSizePanel.add(new JLabel("每页显示:"));
            pageSizeComboBox = new JComboBox<>(new Integer[]{5, 10, 20, 50});
            pageSizeComboBox.setSelectedItem(pageSize);
            pageSizeComboBox.addActionListener(e -> {
                pageSize = (int) pageSizeComboBox.getSelectedItem();
                currentPage = 1;
                loadTableData();
            });
            pageSizePanel.add(pageSizeComboBox);
            
            paginationPanel.add(controlPanel, BorderLayout.CENTER);
            paginationPanel.add(pageSizePanel, BorderLayout.EAST);
            
            // 状态栏
            statusLabel = new JLabel("加载中...");
            statusLabel.setBorder(new EmptyBorder(5, 15, 5, 15));
            statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            
            paginationPanel.add(statusLabel, BorderLayout.SOUTH);
            add(paginationPanel, BorderLayout.SOUTH);
            
            // 添加事件监听
            firstButton.addActionListener(e -> goToFirstPage());
            prevButton.addActionListener(e -> goToPrevPage());
            nextButton.addActionListener(e -> goToNextPage());
            lastButton.addActionListener(e -> goToLastPage());
            
            pageComboBox.addActionListener(e -> {
                if (pageComboBox.getSelectedItem() != null) {
                    int selectedPage = (int) pageComboBox.getSelectedItem();
                    if (selectedPage != currentPage) {
                        currentPage = selectedPage;
                        loadTableData();
                    }
                }
            });
            
            searchButton.addActionListener(e -> {
                String keyword = searchField.getText().trim().toLowerCase();
                filterData(keyword);
            });
            
            // 初始化数据
            initializeMockData();
            filteredData = new ArrayList<>(bookData);
            loadTableData();
        }

        private void initializeMockData() {
            bookData = new ArrayList<>();
            String[] categories = {"小说", "科技", "历史", "文学", "艺术", "教育"};
            String[] authors = {"作者A", "作者B", "作者C", "作者D", "作者E"};
            
            // 生成40条模拟图书数据
            for (int i = 1; i <= 40; i++) {
                bookData.add(new Book(
                    i,
                    "图书" + i,
                    authors[i % authors.length],
                    "ISBN" + String.format("%013d", i),
                    categories[i % categories.length],
                    (int) (Math.random() * 100) + 1
                ));
            }
            bookData.add(new Book(209, "《机器学习》", "作者F", "ISBN209", "科技", 50));
        }

        private void loadTableData() {
            // 计算总页数
            totalPages = (int) Math.ceil((double) filteredData.size() / pageSize);
            if (totalPages == 0) totalPages = 1;
            
            // 确保当前页有效
            if (currentPage > totalPages) currentPage = totalPages;
            if (currentPage < 1) currentPage = 1;
            
            // 更新页面选择器
            updatePageSelector();
            
            // 显示当前页数据
            displayPageData();
            
            // 更新状态栏
            updateStatusBar();
        }

        private void updatePageSelector() {
            // 临时移除事件监听器
            ActionListener[] listeners = pageComboBox.getActionListeners();
            for (ActionListener listener : listeners) {
                pageComboBox.removeActionListener(listener);
            }
            
            pageComboBox.removeAllItems();
            for (int i = 1; i <= totalPages; i++) {
                pageComboBox.addItem(i);
            }
            pageComboBox.setSelectedItem(currentPage);
            
            // 重新添加事件监听
            for (ActionListener listener : listeners) {
                pageComboBox.addActionListener(listener);
            }
        }

        private void displayPageData() {
            // 清空表格
            tableModel.setRowCount(0);
            
            if (filteredData.isEmpty()) {
                return;
            }
            
            // 计算起始索引
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, filteredData.size());
            
            // 添加当前页数据
            for (int i = start; i < end; i++) {
                Book book = filteredData.get(i);
                tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getCategory(),
                    book.getStock(),
                    "编辑|删除" // 操作按钮文本
                });
            }
        }
        
        private void updateStatusBar() {
            int total = filteredData.size();
            int start = (currentPage - 1) * pageSize + 1;
            int end = Math.min(currentPage * pageSize, total);
            
            String status = String.format("显示 %d 到 %d 条，共 %d 条记录", 
                                        start, end, total);
            statusLabel.setText(status);
        }
        
        private void goToFirstPage() {
            if (currentPage != 1) {
                currentPage = 1;
                loadTableData();
            }
        }
        
        private void goToPrevPage() {
            if (currentPage > 1) {
                currentPage--;
                loadTableData();
            }
        }
        
        private void goToNextPage() {
            if (currentPage < totalPages) {
                currentPage++;
                loadTableData();
            }
        }
        
        private void goToLastPage() {
            if (currentPage != totalPages) {
                currentPage = totalPages;
                loadTableData();
            }
        }
        
        private void filterData(String keyword) {
            filteredData = new ArrayList<>();
            
            if (keyword.isEmpty()) {
                filteredData.addAll(bookData);
            } else {
                for (Book book : bookData) {
                    if (book.getTitle().toLowerCase().contains(keyword) ||
                        book.getAuthor().toLowerCase().contains(keyword) ||
                        book.getIsbn().contains(keyword) ||
                        book.getCategory().toLowerCase().contains(keyword)) {
                        filteredData.add(book);
                    }
                }
            }
            
            currentPage = 1;
            loadTableData();
        }
        
        public void editBook(int tableRow) {
            // 计算实际数据索引
            int dataIndex = (currentPage - 1) * pageSize + tableRow;
            if (dataIndex < 0 || dataIndex >= filteredData.size()) return;
            
            Book book = filteredData.get(dataIndex);
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "编辑图书", true);
            dialog.setSize(450, 350);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // 表单字段
            JLabel idLabel = new JLabel("图书ID:");
            JTextField idField = new JTextField(String.valueOf(book.getId()));
            idField.setEditable(false);
            
            JLabel titleLabel = new JLabel("书名:");
            JTextField titleField = new JTextField(book.getTitle());
            
            JLabel authorLabel = new JLabel("作者:");
            JTextField authorField = new JTextField(book.getAuthor());
            
            JLabel isbnLabel = new JLabel("ISBN:");
            JTextField isbnField = new JTextField(book.getIsbn());
            
            JLabel categoryLabel = new JLabel("分类:");
            JComboBox<String> categoryCombo = new JComboBox<>(
                new String[]{"小说", "科技", "历史", "文学", "艺术", "教育"});
            categoryCombo.setSelectedItem(book.getCategory());
            
            JLabel stockLabel = new JLabel("库存:");
            JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(book.getStock(), 1, 1000, 1));
            
            formPanel.add(idLabel);
            formPanel.add(idField);
            formPanel.add(titleLabel);
            formPanel.add(titleField);
            formPanel.add(authorLabel);
            formPanel.add(authorField);
            formPanel.add(isbnLabel);
            formPanel.add(isbnField);
            formPanel.add(categoryLabel);
            formPanel.add(categoryCombo);
            formPanel.add(stockLabel);
            formPanel.add(stockSpinner);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            
            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("保存");
            saveButton.setBackground(new Color(46, 204, 113));
            saveButton.setForeground(Color.WHITE);
            saveButton.setFocusPainted(false);
            
            JButton cancelButton = new JButton("取消");
            cancelButton.setBackground(new Color(231, 76, 60));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);
            
            saveButton.addActionListener(e -> {
                // 更新图书信息
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setIsbn(isbnField.getText());
                book.setCategory((String) categoryCombo.getSelectedItem());
                book.setStock((int) stockSpinner.getValue());
                
                // 刷新表格
                loadTableData();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this, 
                    "图书信息已更新!", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        }
        
        public void deleteBook(int tableRow) {
            // 计算实际数据索引
            int dataIndex = (currentPage - 1) * pageSize + tableRow;
            if (dataIndex < 0 || dataIndex >= filteredData.size()) return;
            
            Book book = filteredData.get(dataIndex);
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "确定要删除图书 '" + book.getTitle() + "' (ID: " + book.getId() + ") 吗?",
                "确认删除",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // 从原始数据和过滤数据中删除
                bookData.remove(book);
                filteredData.remove(book);
                
                // 重新加载数据
                loadTableData();
                
                JOptionPane.showMessageDialog(this, "图书已删除!");
            }
        }
        
        private void showAddBookDialog() {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "添加新图书", true);
            dialog.setSize(450, 350);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            
            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            // 表单字段
            JLabel titleLabel = new JLabel("书名:");
            JTextField titleField = new JTextField();
            
            JLabel authorLabel = new JLabel("作者:");
            JTextField authorField = new JTextField();
            
            JLabel isbnLabel = new JLabel("ISBN:");
            JTextField isbnField = new JTextField();
            
            JLabel categoryLabel = new JLabel("分类:");
            JComboBox<String> categoryCombo = new JComboBox<>(
                new String[]{"小说", "科技", "历史", "文学", "艺术", "教育"});
            
            JLabel stockLabel = new JLabel("库存:");
            JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
            
            formPanel.add(titleLabel);
            formPanel.add(titleField);
            formPanel.add(authorLabel);
            formPanel.add(authorField);
            formPanel.add(isbnLabel);
            formPanel.add(isbnField);
            formPanel.add(categoryLabel);
            formPanel.add(categoryCombo);
            formPanel.add(stockLabel);
            formPanel.add(stockSpinner);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            
            // 按钮面板
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("保存");
            saveButton.setBackground(new Color(46, 204, 113));
            saveButton.setForeground(Color.WHITE);
            saveButton.setFocusPainted(false);
            
            JButton cancelButton = new JButton("取消");
            cancelButton.setBackground(new Color(231, 76, 60));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);
            
            saveButton.addActionListener(e -> {
                // 创建新图书
                int newId = bookData.size() + 1;
                Book newBook = new Book(
                    newId,
                    titleField.getText(),
                    authorField.getText(),
                    isbnField.getText(),
                    (String) categoryCombo.getSelectedItem(),
                    (int) stockSpinner.getValue()
                );
                
                // 添加到数据源
                bookData.add(newBook);
                filteredData.add(newBook);
                
                // 刷新表格
                loadTableData();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this, 
                    "图书添加成功!", 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        }
        
        // 图书数据模型
        class Book {
            private int id;
            private String title;
            private String author;
            private String isbn;
            private String category;
            private int stock;

            public Book(int id, String title, String author, String isbn, String category, int stock) {
                this.id = id;
                this.title = title;
                this.author = author;
                this.isbn = isbn;
                this.category = category;
                this.stock = stock;
            }

            public int getId() { return id; }
            public String getTitle() { return title; }
            public String getAuthor() { return author; }
            public String getIsbn() { return isbn; }
            public String getCategory() { return category; }
            public int getStock() { return stock; }
            
            public void setTitle(String title) { this.title = title; }
            public void setAuthor(String author) { this.author = author; }
            public void setIsbn(String isbn) { this.isbn = isbn; }
            public void setCategory(String category) { this.category = category; }
            public void setStock(int stock) { this.stock = stock; }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Book book = (Book) obj;
                return id == book.id;
            }
        }
    }
    
    // 操作列按钮渲染器
    static class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton editButton;
        private final JButton deleteButton;
        
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            
            editButton = new JButton("编辑");
            editButton.setBackground(new Color(52, 152, 219));
            editButton.setForeground(Color.WHITE);
            editButton.setFocusPainted(false);
            editButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            
            deleteButton = new JButton("删除");
            deleteButton.setBackground(new Color(231, 76, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            
            add(editButton);
            add(deleteButton);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // 操作列按钮编辑器
    class ButtonEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;
        private int row;
        private String moduleType;
        
        public ButtonEditor(JCheckBox checkBox, String moduleType) {
            super(checkBox);
            this.moduleType = moduleType;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            editButton = new JButton("编辑");
            editButton.setBackground(new Color(52, 152, 219));
            editButton.setForeground(Color.WHITE);
            editButton.setFocusPainted(false);
            editButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            
            deleteButton = new JButton("删除");
            deleteButton.setBackground(new Color(231, 76, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            
            panel.add(editButton);
            panel.add(deleteButton);
            
            // 添加按钮事件
            editButton.addActionListener(e -> {
                fireEditingStopped();
                editItem(row);
            });
            
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteItem(row);
            });
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }
        
        public Object getCellEditorValue() {
            return "编辑|删除";
        }
        
        private void editItem(int tableRow) {
            if ("users".equals(moduleType)) {
                userManagementPanel.editUser(tableRow);
            } else if ("books".equals(moduleType)) {
                bookManagementPanel.editBook(tableRow);
            }
        }
        
        private void deleteItem(int tableRow) {
            if ("users".equals(moduleType)) {
                userManagementPanel.deleteUser(tableRow);
            } else if ("books".equals(moduleType)) {
                bookManagementPanel.deleteBook(tableRow);
            }
        }
    }
}