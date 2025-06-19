package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfileSystem extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel pageInfoLabel;
    private int currentPage = 1;
    private int pageSize = 5;
    private int totalPages = 0;

    // 列名数组
    private final String[] columnNames = {
        "用户ID", "用户名", "邮箱", "电话", "注册日期", "状态"
    };

    public UserProfileSystem() {
        setTitle("用户信息中心");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // 创建顶部欢迎面板
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 创建搜索面板
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        
        // 创建表格面板
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // 创建分页控制面板
        JPanel paginationPanel = createPaginationPanel();
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 添加模拟数据并初始化分页
        addSampleData();
        updateTableForPage();
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel welcomeLabel = new JLabel("欢迎，张三！");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 70, 140));
        panel.add(welcomeLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JLabel searchLabel = new JLabel("搜索:");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JButton searchButton = createStyledButton("搜索", new Color(70, 130, 180));
        JButton resetButton = createStyledButton("重置", new Color(150, 150, 150));
        
        // 搜索按钮事件
        searchButton.addActionListener(e -> performSearch());
        
        // 重置按钮事件
        resetButton.addActionListener(e -> {
            searchField.setText("");
            resetTable();
        });
        
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(resetButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(80, 30));
        return button;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 创建表格模型
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.setRowHeight(30);
        userTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        // 设置表格样式 - 确保列名完全可见
        userTable.setShowGrid(true);
        userTable.setGridColor(new Color(220, 220, 220));
        userTable.getTableHeader().setBackground(new Color(70, 130, 180));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        // 设置列宽 - 确保列名完全可见
        userTable.getColumnModel().getColumn(0).setPreferredWidth(100);  // ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(120); // 用户名
        userTable.getColumnModel().getColumn(2).setPreferredWidth(200); // 邮箱
        userTable.getColumnModel().getColumn(3).setPreferredWidth(120); // 电话
        userTable.getColumnModel().getColumn(4).setPreferredWidth(120); // 注册日期
        userTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // 状态
        
        // 添加滚动面板 - 自动调整大小
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(0, 0)); // 让布局管理器决定大小
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPaginationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        
        // 上一页按钮
        JButton prevButton = createStyledButton("上一页", new Color(100, 100, 150));
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateTableForPage();
            }
        });
        
        // 下一页按钮
        JButton nextButton = createStyledButton("下一页", new Color(100, 100, 150));
        nextButton.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateTableForPage();
            }
        });
        
        // 页码信息标签
        pageInfoLabel = new JLabel();
        pageInfoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        panel.add(prevButton);
        panel.add(pageInfoLabel);
        panel.add(nextButton);
        
        return panel;
    }
    
    private void addSampleData() {
        // 添加模拟用户数据
        addUser("U001", "张三", "zhangsan@example.com", "13800138000", "2023-01-15", "激活");
        addUser("U002", "李四", "lisi@example.com", "13900139000", "2023-02-20", "激活");
        addUser("U003", "王五", "wangwu@example.com", "13700137000", "2023-03-10", "禁用");
        addUser("U004", "赵六", "zhaoliu@example.com", "13600136000", "2023-04-05", "激活");
        addUser("U005", "钱七", "qianqi@example.com", "13500135000", "2023-05-12", "激活");
        addUser("U006", "孙八", "sunba@example.com", "13400134000", "2023-06-18", "禁用");
        addUser("U007", "周九", "zhoujiu@example.com", "13300133000", "2023-07-22", "激活");
        addUser("U008", "吴十", "wushi@example.com", "13200132000", "2023-08-30", "激活");
        addUser("U009", "郑十一", "zhengshiyi@example.com", "13100131000", "2023-09-05", "禁用");
        addUser("U010", "王十二", "wangshier@example.com", "13000130000", "2023-10-15", "激活");
    }
    
    private void addUser(String id, String name, String email, String phone, String regDate, String status) {
        tableModel.addRow(new Object[]{id, name, email, phone, regDate, status});
    }
    
    private void updateTableForPage() {
        // 计算总页数
        int totalRows = tableModel.getRowCount();
        totalPages = (int) Math.ceil((double) totalRows / pageSize);
        
        // 创建临时模型用于分页
        DefaultTableModel tempModel = new DefaultTableModel(columnNames, 0);
        
        // 计算当前页的起始行和结束行
        int startRow = (currentPage - 1) * pageSize;
        int endRow = Math.min(startRow + pageSize, totalRows);
        
        // 添加当前页的数据
        for (int i = startRow; i < endRow; i++) {
            Object[] rowData = {
                tableModel.getValueAt(i, 0),
                tableModel.getValueAt(i, 1),
                tableModel.getValueAt(i, 2),
                tableModel.getValueAt(i, 3),
                tableModel.getValueAt(i, 4),
                tableModel.getValueAt(i, 5)
            };
            tempModel.addRow(rowData);
        }
        
        // 更新表格
        userTable.setModel(tempModel);
        
        // 更新分页信息
        pageInfoLabel.setText("第 " + currentPage + " 页 / 共 " + totalPages + " 页 (共 " + totalRows + " 条记录)");
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim().toLowerCase();
        
        if (searchText.isEmpty()) {
            resetTable();
            return;
        }
        
        // 创建临时模型用于存储搜索结果
        DefaultTableModel searchModel = new DefaultTableModel(columnNames, 0);
        
        // 遍历所有行进行搜索
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String name = tableModel.getValueAt(i, 1).toString().toLowerCase();
            String email = tableModel.getValueAt(i, 2).toString().toLowerCase();
            String phone = tableModel.getValueAt(i, 3).toString().toLowerCase();
            
            if (id.contains(searchText) || 
                name.contains(searchText) || 
                email.contains(searchText) || 
                phone.contains(searchText)) {
                
                Object[] rowData = {
                    tableModel.getValueAt(i, 0),
                    tableModel.getValueAt(i, 1),
                    tableModel.getValueAt(i, 2),
                    tableModel.getValueAt(i, 3),
                    tableModel.getValueAt(i, 4),
                    tableModel.getValueAt(i, 5)
                };
                searchModel.addRow(rowData);
            }
        }
        
        // 更新表格模型
        userTable.setModel(searchModel);
        
        // 重置分页信息
        currentPage = 1;
        int totalRows = searchModel.getRowCount();
        totalPages = (int) Math.ceil((double) totalRows / pageSize);
        pageInfoLabel.setText("搜索结果: " + totalRows + " 条记录");
    }
    
    private void resetTable() {
        userTable.setModel(tableModel);
        currentPage = 1;
        updateTableForPage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统外观
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            
            new UserProfileSystem().setVisible(true);
        });
    }
}