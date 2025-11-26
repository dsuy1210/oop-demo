package com.bank.view;

import com.bank.model.Account;
import com.bank.model.User;
import com.bank.repository.DatabaseSimulator;
import com.bank.service.CustomerAccountService;
import com.bank.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lớp CustomerRegistrationView - Giao diện đăng ký khách hàng mới
 * Cho phép người dùng:
 * - Nhập thông tin cá nhân
 * - Tạo tài khoản mới
 * - Tạo tài khoản ngân hàng đầu tiên
 */
public class CustomerRegistrationView extends JPanel {
    private DatabaseSimulator db;
    private UserService userService;
    private CustomerAccountService customerAccountService;

    // Các thành phần UI
    private JTextField fullNameField;
    private JTextField idNumberField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JSpinner dateOfBirthSpinner;
    private JTextArea resultArea;

    public CustomerRegistrationView(DatabaseSimulator db) {
        this.db = db;
        this.userService = new UserService(db);
        this.customerAccountService = new CustomerAccountService(db);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Tạo panel tiêu đề
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(70, 130, 180));

        JLabel titleLabel = new JLabel("ĐĂNG KÝ KHÁCH HÀNG MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    /**
     * Tạo panel form đăng ký
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(25);
        panel.add(fullNameField, gbc);

        // Ngày sinh
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Ngày sinh (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        dateOfBirthSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateOfBirthSpinner, "dd/MM/yyyy");
        dateOfBirthSpinner.setEditor(dateEditor);
        panel.add(dateOfBirthSpinner, gbc);

        // CMND/CCCD
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("CMND/CCCD:"), gbc);
        gbc.gridx = 1;
        idNumberField = new JTextField(25);
        panel.add(idNumberField, gbc);

        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(25);
        panel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(25);
        panel.add(emailField, gbc);

        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(25);
        panel.add(addressField, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(25);
        panel.add(passwordField, gbc);

        // Xác nhận mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(25);
        panel.add(confirmPasswordField, gbc);

        // Nút Đăng ký
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Đăng ký");
        registerButton.setBackground(new Color(34, 139, 34));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> registerNewCustomer());

        return panel;
    }

    /**
     * Tạo panel chân trang (kết quả)
     */
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        resultArea = new JTextArea(4, 50);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Xử lý đăng ký khách hàng mới
     */
    private void registerNewCustomer() {
        try {
            // Kiểm tra và lấy dữ liệu
            String fullName = fullNameField.getText().trim();
            String idNumber = idNumberField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            Date dateOfBirth = (Date) dateOfBirthSpinner.getValue();

            // Kiểm tra các trường bắt buộc
            if (fullName.isEmpty() || idNumber.isEmpty() || phone.isEmpty() || 
                email.isEmpty() || address.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!",
                                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra mật khẩu
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!",
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo khách hàng mới
            User newCustomer = userService.createCustomer(fullName, dateOfBirth, idNumber,
                                                          phone, email, address, password);

            resultArea.append("✓ Khách hàng được tạo thành công!\n");
            resultArea.append("  Mã khách hàng: " + newCustomer.getCustomerId() + "\n");
            resultArea.append("  Họ tên: " + newCustomer.getFullName() + "\n");
            resultArea.append("  Email: " + newCustomer.getEmail() + "\n\n");

            // Tạo tài khoản ngân hàng đầu tiên
            Account account = customerAccountService.createAccount(newCustomer.getCustomerId(), 0);
            
            resultArea.append("✓ Tài khoản ngân hàng được tạo thành công!\n");
            resultArea.append("  Số tài khoản: " + account.getAccountNumber() + "\n");
            resultArea.append("  Số dư: " + account.getBalance() + " VND\n\n");

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this,
                                        "Đăng ký thành công!\n" +
                                        "Mã khách hàng: " + newCustomer.getCustomerId() + "\n" +
                                        "Số tài khoản: " + account.getAccountNumber() + "\n\n" +
                                        "Bạn có thể đăng nhập bằng email: " + email,
                                        "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Xóa form
            clearForm();

        } catch (Exception ex) {
            resultArea.append("✗ Lỗi: " + ex.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(),
                                        "Lỗi Đăng ký", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xóa dữ liệu form
     */
    private void clearForm() {
        fullNameField.setText("");
        idNumberField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        dateOfBirthSpinner.setValue(new Date());
    }
}
