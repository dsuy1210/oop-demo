package com.bank.view;

import com.bank.model.*;
import com.bank.repository.DatabaseSimulator;
import com.bank.service.AccountService;
import com.bank.service.AuthService;
import java.awt.*;
import java.awt.event.ActionEvent; // Import tất cả model
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

/**
 * Lớp Giao diện Người dùng (View) chính
 * Sử dụng Java Swing.
 */
public class BankGUI extends JFrame {

    // Các thành phần Service và Repository
    private static DatabaseSimulator db;
    private static AuthService authService;
    private static AccountService accountService;

    // Trạng thái người dùng
    private User currentUser;

    // Các thành phần UI chính
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel dashboardPanel;

    // Thành phần trên Dashboard
    private JLabel welcomeLabel;
    private JTabbedPane tabbedPane;
    private BankLoan bankLoanPanel;
    private BankNap bankNapPanel;

    public BankGUI() {
        // 1. Khởi tạo services
        db = new DatabaseSimulator();
        authService = new AuthService(db);
        accountService = new AccountService(db);

        // 2. Nạp dữ liệu mẫu
        setupSampleData();

        // 3. Cài đặt cửa sổ chính (JFrame)
        setTitle("Ngân hàng OOP");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Giữa màn hình

        // 4. Sử dụng CardLayout để chuyển đổi giữa các màn hình
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 5. Tạo các màn hình (panel)
        createLoginPanel();
        createDashboardPanel();

        // 6. Thêm các panel vào panel chính
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");

        // 7. Thêm panel chính vào JFrame và hiển thị
        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN"); // Hiển thị màn hình login đầu tiên
    }

    /**
     * Tạo màn hình đăng nhập
     */
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Tiêu đề
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("🏦 NGÂN HÀNG OOP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginPanel.add(titleLabel, gbc);

        // Lựa chọn loại người dùng
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton userTypeButton = new JButton("👤 Đăng nhập User");
        userTypeButton.setBackground(new Color(70, 130, 180));
        userTypeButton.setForeground(Color.WHITE);
        userTypeButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(userTypeButton, gbc);

        gbc.gridx = 1;
        JButton adminTypeButton = new JButton("👨‍💼 Đăng nhập Admin");
        adminTypeButton.setBackground(new Color(200, 16, 46));
        adminTypeButton.setForeground(Color.WHITE);
        adminTypeButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(adminTypeButton, gbc);

        // Dòng phân tách
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JSeparator separator = new JSeparator();
        loginPanel.add(separator, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField emailField = new JTextField(20);
        loginPanel.add(emailField, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        // Nút Đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(loginButton, gbc);

        // Nút Đăng ký
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Đăng ký");
        registerButton.setBackground(new Color(255, 140, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(registerButton, gbc);

        // Xử lý nút Đăng nhập User
        userTypeButton.addActionListener((ActionEvent e) -> {
            emailField.setText("");
            passwordField.setText("");
            emailField.requestFocus();
        });

        // Xử lý nút Đăng nhập Admin
        adminTypeButton.addActionListener((ActionEvent e) -> {
            emailField.setText("");
            passwordField.setText("");
            emailField.requestFocus();
        });

        // Xử lý sự kiện nút Đăng nhập
        loginButton.addActionListener((ActionEvent e) -> {
            try {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                // Gọi AuthService
                currentUser = authService.login(email, password);
                
                // Nếu thành công
                updateDashboardInfo(); // Cập nhật thông tin
                cardLayout.show(mainPanel, "DASHBOARD"); // Chuyển màn hình
                
            } catch (Exception ex) {
                // Nếu thất bại, hiển thị lỗi
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi Đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Xử lý sự kiện nút Đăng ký
        registerButton.addActionListener((ActionEvent e) -> {
            // Hiển thị dialog với form đăng ký
            JDialog registerDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                                                "Đăng ký khách hàng mới", true);
            registerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            registerDialog.setSize(600, 600);
            registerDialog.setLocationRelativeTo(this);
            
            CustomerRegistrationView registrationView = new CustomerRegistrationView(db);
            registerDialog.add(registrationView);
            registerDialog.setVisible(true);
        });
    }

    /**
     * Tạo màn hình chính (Dashboard) sau khi đăng nhập
     */
    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout(10, 10)); // Layout chính
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        welcomeLabel = new JLabel("Chào mừng!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dashboardPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Tạo các tab chức năng (sẽ được điền sau khi đăng nhập)
        tabbedPane = new JTabbedPane();
        
        dashboardPanel.add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Đăng xuất");
        dashboardPanel.add(logoutButton, BorderLayout.SOUTH);

        // Xử lý sự kiện nút Đăng xuất
        logoutButton.addActionListener((e) -> {
            currentUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });
    }

    /**
     * Tạo panel "Lịch sử giao dịch"
     */
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JButton refreshButton = new JButton("Làm mới");
        panel.add(refreshButton, BorderLayout.NORTH);

        JTextArea historyArea = new JTextArea(15, 50);
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Xử lý sự kiện nút Làm mới
        refreshButton.addActionListener((e) -> {
            try {
                String accountId = currentUser.getAccountIds().get(0);
                List<Transaction> history = accountService.getTransactionHistory(accountId);
                historyArea.setText("");
                if (history.isEmpty()) {
                    historyArea.append("Không có lịch sử giao dịch.");
                } else {
                    for (Transaction tx : history) {
                        historyArea.append(tx.toString() + "\n");
                    }
                }
            } catch (Exception ex) {
                historyArea.setText("Lỗi: " + ex.getMessage());
            }
        });

        // Load dữ liệu lần đầu
        refreshButton.doClick();

        return panel;
    }

    /**
     * Tạo panel "Chuyển tiền" cho User
     */
    private JPanel createTransferPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tài khoản nhận:"), gbc);
        gbc.gridx = 1;
        JTextField toAccountField = new JTextField(20);
        toAccountField.setText("ACC002");
        panel.add(toAccountField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Số tiền:"), gbc);
        gbc.gridx = 1;
        JTextField amountField = new JTextField(20);
        panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nội dung:"), gbc);
        gbc.gridx = 1;
        JTextField contentField = new JTextField(20);
        contentField.setText("Chuyen tien");
        panel.add(contentField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton transferButton = new JButton("Thực hiện chuyển tiền");
        panel.add(transferButton, gbc);

        // Xử lý sự kiện nút Chuyển tiền
        transferButton.addActionListener((e) -> {
            try {
                String fromAccountId = currentUser.getAccountIds().get(0);
                String toAccountId = toAccountField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String content = contentField.getText();

                accountService.transfer(fromAccountId, toAccountId, amount, content);
                
                JOptionPane.showMessageDialog(this, "Chuyển tiền thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Số tiền không hợp lệ.", "Lỗi Dữ liệu", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi Giao dịch", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    /**
     * Cập nhật thông tin trên Dashboard (số dư, lịch sử)
     */
    private void updateDashboardInfo() {
        if (currentUser == null) return;

        try {
            // Cập nhật tên chào mừng
            welcomeLabel.setText("Chào mừng, " + currentUser.getFullName() + 
                               (currentUser.getRole() == User.UserRole.ADMIN ? " (Quản trị viên)" : " (Khách hàng)"));

            // Clear all existing tabs
            tabbedPane.removeAll();

            // Nếu là ADMIN
            if (currentUser.getRole() == User.UserRole.ADMIN) {
                AdminDashboard adminDashboard = new AdminDashboard(db, currentUser, accountService);
                tabbedPane.addTab("Bảng điều khiển", adminDashboard);
                tabbedPane.addTab("Quản lý hệ thống", createSystemManagementPanel());
            } else {
                // Nếu là CUSTOMER
                bankNapPanel = new BankNap(db, currentUser, accountService);
                tabbedPane.addTab("Nạp tiền", bankNapPanel);
                
                tabbedPane.addTab("Chuyển tiền", createTransferPanel());
                
                bankLoanPanel = new BankLoan(db, currentUser, accountService);
                tabbedPane.addTab("Vay tiền", bankLoanPanel);
                
                // Thêm tab lịch sử giao dịch
                tabbedPane.addTab("Lịch sử giao dịch", createHistoryPanel());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Tạo panel quản lý hệ thống cho Admin
     */
    private JPanel createSystemManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("🔧 QUẢN LÝ HỆ THỐNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Nút chức năng
        JButton exportButton = new JButton("📊 Xuất báo cáo");
        JButton settingsButton = new JButton("⚙️ Cài đặt hệ thống");
        JButton backupButton = new JButton("💾 Sao lưu dữ liệu");

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(exportButton, gbc);

        gbc.gridx = 1;
        panel.add(settingsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(backupButton, gbc);

        // Thêm sự kiện
        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Chức năng xuất báo cáo sẽ được cập nhật sớm!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        settingsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Chức năng cài đặt hệ thống sẽ được cập nhật sớm!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        backupButton.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Chức năng sao lưu dữ liệu sẽ được cập nhật sớm!",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        return panel;
    }
    
    /**
     * Nạp dữ liệu mẫu (Giống trong file Main.java cũ)
     */
    private static void setupSampleData() {
        // Tạo User 1 (Alice)
        User alice = new User("CUS001", "Nguyen Van A", new Date(), "001234567890", 
                            "0912345678", "alice@bank.com", "123 Duong ABC", 
                            User.UserRole.CUSTOMER, "123");
        db.saveUser(alice);

        // Tạo User 2 (Bob)
        User bob = new User("CUS002", "Tran Thi B", new Date(), "001234567891", 
                          "0912345679", "bob@bank.com", "456 Duong XYZ", 
                          User.UserRole.CUSTOMER, "123");
        db.saveUser(bob);
        
        // Tạo User 3 (Admin)
         User admin = new User("ADM001", "Quan Tri Vien", new Date(), "000000000000", 
                          "0900000000", "admin@gmail.com", "Ngan Hang", 
                          User.UserRole.ADMIN, "admin");
        db.saveUser(admin);

        // Tạo Tài khoản cho Alice
        Account accAlice = new Account("ACC001", alice.getCustomerId(), 50000000);
        alice.addAccountId(accAlice.getAccountNumber());
        db.saveAccount(accAlice);

        // Tạo Tài khoản cho Bob
        Account accBob = new Account("ACC002", bob.getCustomerId(), 10000000);
        bob.addAccountId(accBob.getAccountNumber());
        db.saveAccount(accBob);
        
        // Tạo Thẻ cho Alice
        // Thẻ Debit
        Date expiry = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365 * 3)); // Hết hạn sau 3 năm
        Card debitAlice = new DebitCard("CARD001", accAlice.getAccountNumber(), new Date(), expiry);
        accAlice.addCardId(debitAlice.getCardNumber());
        db.saveCard(debitAlice);
        
        // Thẻ Credit
        Card creditAlice = new CreditCard("CARD002", accAlice.getAccountNumber(), new Date(), expiry, 20000000); // Hạn mức 20tr
        accAlice.addCardId(creditAlice.getCardNumber());
        db.saveCard(creditAlice);
        
        System.out.println("Dữ liệu mẫu đã được nạp.");
        System.out.println("\n========== HƯỚNG DẪN ĐĂNG NHẬP ==========");
        System.out.println("👤 Tài khoản ADMIN:");
        System.out.println("   Email: admin@bank.com");
        System.out.println("   Mật khẩu: admin");
        System.out.println("\n👤 Tài khoản KHÁCH HÀNG:");
        System.out.println("   Email: alice@bank.com / Mật khẩu: 123");
        System.out.println("   Email: bob@bank.com / Mật khẩu: 123");
        System.out.println("========================================\n");
    }

    /**
     * Phương thức main để khởi chạy ứng dụng GUI
     */
    public static void main(String[] args) {
        // Chạy ứng dụng Swing trên Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            BankGUI app = new BankGUI();
            app.setVisible(true);
        });
    }
}
