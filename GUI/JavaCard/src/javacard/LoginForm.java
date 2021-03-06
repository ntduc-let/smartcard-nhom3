/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import connectDB.DataUser;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.security.PublicKey;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javacard.connect.ConnectCard;
import javacard.connect.RSAAppletHelper;
import javacard.utils.RSAData;
import javacard.utils.RandomUtil;
import javax.smartcardio.CardException;
import javax.swing.BorderFactory;

/**
 *
 * @author kqhuynh
 */
public class LoginForm extends javax.swing.JFrame {

    private int firstUSE;
    private static int login_status = 0; 

    /**
     * Creates new form LoginForm
     */
    public LoginForm() {
        initComponents();
        
        if(login_status == 0){
            loginSetEnabled(false);
        }
        else{
            loginSetEnabled(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_admin = new javax.swing.JButton();
        edt_pin = new javax.swing.JPasswordField();
        txt_pin = new javax.swing.JLabel();
        chk_show_pw = new javax.swing.JCheckBox();
        txt_login = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnConnect = new javax.swing.JButton();
        form_login = new javax.swing.JLabel();
        bg_icon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Đăng nhập");
        setLocationByPlatform(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_admin.setBackground(new java.awt.Color(0, 255, 255));
        btn_admin.setText("AdminForm");
        btn_admin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_adminActionPerformed(evt);
            }
        });
        getContentPane().add(btn_admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 550, -1, -1));

        edt_pin.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edt_pin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        getContentPane().add(edt_pin, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 240, 40));

        txt_pin.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        txt_pin.setText("Mã PIN");
        getContentPane().add(txt_pin, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, -1, -1));

        chk_show_pw.setBackground(new java.awt.Color(255, 255, 255));
        chk_show_pw.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        chk_show_pw.setText("Hiện mã PIN");
        chk_show_pw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_show_pwActionPerformed(evt);
            }
        });
        getContentPane().add(chk_show_pw, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 350, -1, -1));

        txt_login.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        txt_login.setText("Đăng nhập");
        getContentPane().add(txt_login, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 200, 40));

        btnLogin.setBackground(new java.awt.Color(0, 102, 153));
        btnLogin.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Đăng nhập");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 390, 240, 30));

        btnConnect.setBackground(new java.awt.Color(153, 255, 255));
        btnConnect.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnConnect.setText("Kết nối thẻ");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });
        getContentPane().add(btnConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 540, 350, -1));

        form_login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Trang.jpg"))); // NOI18N
        getContentPane().add(form_login, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 350, 390));

        bg_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/bg_login.jpg"))); // NOI18N
        getContentPane().add(bg_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 610));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void chk_show_pwActionPerformed(ActionEvent evt) {//GEN-FIRST:event_chk_show_pwActionPerformed
        // TODO add your handling code here:
        if (chk_show_pw.isSelected()) {
            edt_pin.setEchoChar((char) 0);
        } else {
            edt_pin.setEchoChar('*');
        }
    }//GEN-LAST:event_chk_show_pwActionPerformed

    private void btnLoginActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        if(ConnectCard.getInstance().ReadInformation()){
            if (ConnectCard.getInstance().strID.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Thẻ chưa được khởi tạo");
                return;
            }
        }
        
        String pin = edt_pin.getText();
        
        if (ConnectCard.getInstance().verifyPin(pin)) {
            if (firstUSE == 1) {
                
                PinForm pinform = new PinForm();
                pinform.setVisible(true);
                this.dispose();
                
            } else {
                HomeForm home = new HomeForm();
                home.setVisible(true);
                this.dispose();
            }
            login_status = 1;
        } else {
            System.out.println("Mã pin sai");
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btn_adminActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btn_adminActionPerformed
        // TODO add your handling code here:
        new AdminForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_adminActionPerformed

    private void btnConnectActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        String response = ConnectCard.getInstance().connectapplet();
        if (response.equals("Error")) {
            JOptionPane.showMessageDialog(null, "Kết nối bị lỗi");
        } else {
            if ((response.split("="))[1].equals("9000")) {
                loginSetEnabled(true);
                btnConnect.setText("Đã kết nối thẻ");
                btnConnect.setEnabled(false);
                firstUSE = (int) ((ConnectCard.getInstance().data)[0] & 0xFF);
                
                if (firstUSE == 0) {
                    if (rsaAuthentication()) {
                        ConnectCard.getInstance().setUp();
                        JOptionPane.showMessageDialog(null, "Kết nối thẻ thành công");
                    }else{
                        JOptionPane.showMessageDialog(null, "Thẻ không xác thực");
                    }
                }else{
                    ConnectCard.getInstance().setUp();
                    JOptionPane.showMessageDialog(null, "Kết nối thẻ thành công");
                }
                
            } else {
                JOptionPane.showMessageDialog(null, "Kết nối bị lỗi");
            }
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    /**
     * @param args the command line arguments
     */
        public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel bg_icon;
    javax.swing.JButton btnConnect;
    javax.swing.JButton btnLogin;
    javax.swing.JButton btn_admin;
    javax.swing.JCheckBox chk_show_pw;
    javax.swing.JPasswordField edt_pin;
    javax.swing.JLabel form_login;
    javax.swing.JLabel txt_login;
    javax.swing.JLabel txt_pin;
    // End of variables declaration//GEN-END:variables

    private void loginSetEnabled(boolean b) {
        form_login.setEnabled(b);
        if(b){
            form_login.setBorder(BorderFactory.createLineBorder(Color.blue, 1, true));
            txt_login.setForeground(Color.blue);
            edt_pin.setBackground(Color.white);
            chk_show_pw.setBackground(Color.white);
        }else{
            form_login.setBorder(null);
            txt_login.setForeground(Color.black);
            edt_pin.setBackground(Color.decode("#d7d7d7"));
            chk_show_pw.setBackground(Color.decode("#d7d7d7"));
        }
        txt_login.setEnabled(b);
        txt_pin.setEnabled(b);
        edt_pin.setEnabled(b);
        chk_show_pw.setEnabled(b);
        btnLogin.setEnabled(b);
        btn_admin.setEnabled(b);
    }
    
    private boolean rsaAuthentication() {
        try {
            PublicKey publicKeys = RSAData.getPublicKey();
            if (publicKeys == null) {
                return false;
            }
            System.out.println("publicKeys: " + Arrays.toString(publicKeys.getEncoded()));
            byte[] data = RandomUtil.randomData(20);

            byte[] signed = RSAAppletHelper.getInstance(
                    ConnectCard.getInstance().channel).requestSign(data);

            if (signed == null) {
                return false;
            }

            System.out.println("signed: " + Arrays.toString(signed));
            
            
            return RSAData.verify(publicKeys, signed, data);
        } catch (CardException ex) {
        }
        return false;
    }
}
