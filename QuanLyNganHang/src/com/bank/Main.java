package com.bank;

// Xóa các import không cần thiết cho CLI
import com.bank.view.BankGUI;
import javax.swing.SwingUtilities;

/**
 * Lớp chính (View)
 * CẬP NHẬT: Khởi chạy giao diện GUI thay vì CLI.
 */
public class Main {

    public static void main(String[] args) {
        // --- Khởi chạy ứng dụng GUI ---
        // Chạy ứng dụng Swing trên Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            BankGUI app = new BankGUI();
            app.setVisible(true);
        });
    }
    
    // --- TOÀN BỘ LOGIC CLI CŨ ĐÃ BỊ XÓA ---
    // (Bao gồm showLoginMenu, showMainMenu, handleWithdrawDemo)
    // (setupSampleData đã được chuyển vào BankGUI.java)
}