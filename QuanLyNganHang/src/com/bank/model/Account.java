package com.bank.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho thực thể Tài khoản (TaiKhoan)
 */
public class Account {
    private String accountNumber; // Số tài khoản (PK)
    private double balance; // Số dư
    private String customerId; // Mã khách hàng (FK)
    private List<String> cardIds; // Danh sách các thẻ liên kết

    public Account(String accountNumber, String customerId, double initialBalance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = initialBalance;
        this.cardIds = new ArrayList<>();
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getCustomerId() { return customerId; }
    public List<String> getCardIds() { return cardIds; }

    // Phương thức nghiệp vụ cơ bản
    // Logic kiểm tra sẽ nằm trong Service
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false; // Không đủ tiền
    }
    
    public void addCardId(String cardId) {
        this.cardIds.add(cardId);
    }
}