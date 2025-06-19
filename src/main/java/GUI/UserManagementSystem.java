package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserManagementSystem extends JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> pageSizeComboBox;
    private JComboBox<Integer> pageComboBox;
    private List<User> userData;
    private List<User> filteredData;
    private int currentPage = 1;
    private int totalPages = 1;
    private int pageSize = 10;
    private JLabel statusLabel;

    public UserManagementSystem() {
        // 初始化模拟数据
        initializeMockData();
        
        // 设置窗口属性
        setTitle("用户管理系统");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(mainPanel);
        
        // 顶部面板 - 欢迎信息和搜索
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 中央面板 - 用户表格
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // 底部面板 - 分页控制
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // 加载初始数据
        loadTableData();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 欢迎标签
        JLabel welcomeLabel = new JLabel("欢迎使用用户管理系统");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        JButton resetButton = new JButton("重置");
        
        // 搜索按钮事件
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            filterData(keyword);
        });
        
        // 重置按钮事件
        resetButton.addActionListener(e -> {
            searchField.setText("");
            filteredData = new ArrayList<>(userData);
            currentPage = 1;
            loadTableData();
        });
        
        searchPanel.add(new JLabel("搜索:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // 创建表格模型
        String[] columnNames = {"用户ID", "姓名", "邮箱", "电话", "注册日期", "状态"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 设置表头样式
        JTableHeader header = userTable.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setReorderingAllowed(false);
        
        // 设置表格字体
        userTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // 分页控制面板
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        // 每页显示数量
        paginationPanel.add(new JLabel("每页显示:"));
        pageSizeComboBox = new JComboBox<>(new Integer[]{5, 10, 20, 50});
        pageSizeComboBox.setSelectedItem(pageSize);
        pageSizeComboBox.addActionListener(e -> {
            pageSize = (int) pageSizeComboBox.getSelectedItem();
            currentPage = 1;
            loadTableData();
        });
        paginationPanel.add(pageSizeComboBox);
        
        // 分页导航
        JButton firstButton = new JButton("首页");
        JButton prevButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");
        JButton lastButton = new JButton("末页");
        
        // 页面选择
        paginationPanel.add(firstButton);
        paginationPanel.add(prevButton);
        paginationPanel.add(new JLabel("页码:"));
        pageComboBox = new JComboBox<>();
        paginationPanel.add(pageComboBox);
        paginationPanel.add(nextButton);
        paginationPanel.add(lastButton);
        
        // 添加事件监听
        firstButton.addActionListener(e -> goToFirstPage());
        prevButton.addActionListener(e -> goToPrevPage());
        nextButton.addActionListener(e -> goToNextPage());
        lastButton.addActionListener(e -> goToLastPage());
        
        // 修复页码选择器事件处理
        pageComboBox.addActionListener(e -> {
            if (pageComboBox.getSelectedItem() != null) {
                int selectedPage = (int) pageComboBox.getSelectedItem();
                if (selectedPage != currentPage) {
                    currentPage = selectedPage;
                    loadTableData();
                }
            }
        });
        
        bottomPanel.add(paginationPanel, BorderLayout.CENTER);
        
        // 状态栏
        statusLabel = new JLabel("共 0 条记录");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return bottomPanel;
    }

    private void initializeMockData() {
        userData = new ArrayList<>();
        // 生成模拟数据
        for (int i = 1; i <= 100; i++) {
            userData.add(new User(
                i,
                "用户" + i,
                "user" + i + "@example.com",
                "138" + String.format("%08d", i),
                "2023-" + String.format("%02d", (i % 12) + 1) + "-" + String.format("%02d", (i % 28) + 1),
                i % 4 == 0 ? "禁用" : "激活"
            ));
        }
        filteredData = new ArrayList<>(userData);
    }

    private void filterData(String keyword) {
        if (keyword.isEmpty()) {
            filteredData = new ArrayList<>(userData);
        } else {
            filteredData = new ArrayList<>();
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
        // 防止页面选择器事件触发不必要的重载
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
        
        if (filteredData.isEmpty()) return;
        
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
                user.getRegisterDate(),
                user.getStatus()
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
    
    // 分页导航方法
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

    // 用户数据模型
    private static class User {
        private int id;
        private String name;
        private String email;
        private String phone;
        private String registerDate;
        private String status;

        public User(int id, String name, String email, String phone, String registerDate, String status) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.registerDate = registerDate;
            this.status = status;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getRegisterDate() { return registerDate; }
        public String getStatus() { return status; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置UI风格
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            UserManagementSystem frame = new UserManagementSystem();
            frame.setVisible(true);
        });
    }
}