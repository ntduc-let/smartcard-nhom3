/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import connectDB.DataUser;
import javacard.connect.ConnectCard;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javacard.connect.RSAAppletHelper;
import javacard.utils.RSAData;
import javacard.utils.RandomUtil;
import javax.imageio.ImageIO;
import javax.smartcardio.CardException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author kqhuynh
 */
public class HomeForm extends javax.swing.JFrame {

    /**
     * Creates new form HomeForm
     */
    private int CheckEnd = 0;
    private String startTime = "";
    private boolean isEditing = false;
    public DataUser dataUser = new DataUser();
    
    
    private final String DATA = "D:/JavaCard/smartcarddata.bin";

    public HomeForm() {
        dataUser.setMaNV("NV001");
        initComponents();
        initInformation();
        
        initUI();
        
        showDate();
        showTime();
    }
    private void showDate(){
        Date date = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        txt_date.setText(s.format(date));
    }
    
    private void showTime(){
        new Timer(0, (ActionEvent e) -> {
            Date d = new Date();
            SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
            txt_time.setText(s.format(d));
        }).start();
    }
    
    public boolean hasObject(File f) {
        // thu doc xem co object nao chua
        FileInputStream fi;
        boolean check = true;
        try {
            fi = new FileInputStream(f);
            ObjectInputStream inStream = new ObjectInputStream(fi);
            if (inStream.readObject() == null) {
                check = false;
            }
            inStream.close();
        } catch (FileNotFoundException e) {
            check = false;
        } catch (IOException e) {
            check = false;
        } catch (ClassNotFoundException e) {
            check = false;
        }
        return check;
    }
    
    void inputTime(String dateString,String startTimeString,String endTimeString){
        try {
            File f = new File(DATA);
            FileOutputStream fo;
            ObjectOutputStream oStream = null;
            if (!f.exists()) {
                fo = new FileOutputStream(f);
                oStream = new ObjectOutputStream(fo);
            } else { 
                if (!hasObject(f)) {
                    fo = new FileOutputStream(f);
                    oStream = new ObjectOutputStream(fo);
                } else { // neu co roi thi ghi them vao
 
                    fo = new FileOutputStream(f, true);
 
                    oStream = new ObjectOutputStream(fo) {
                        @Override
                        protected void writeStreamHeader() throws IOException {
                            reset();
                        }
                    };
                }
            }
            StockFile s = new StockFile(dateString, startTimeString, endTimeString);
            System.out.println("=>>>>>>>>time");
            DataUser up = new DataUser(dataUser.maNV);
            up.chamCong.add(dateString + " : " + startTimeString + " | " + endTimeString);
            up.Update();
            oStream.writeObject(s);
            oStream.close();
 
        } catch (IOException e) {
            System.out.println("javacard.HomeForm.inputTime()" + e);
        }
    }
    
    void outputTime(){
        try {
            File f = new File(DATA);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream inStream = new ObjectInputStream(fis);
            Object s;
            int i = 0;
            txt_log.setText("");
            while (true) {
                s = inStream.readObject();
                String log = ++i + ":" + s.toString() + "\n";
                txt_log.append(log);
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("javacard.HomeForm.outputTime()" + e.toString());
        }
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
            DataUser up = new DataUser(dataUser.maNV);
            up.setPublicKey(Arrays.toString(publicKeys.getEncoded()));
            up.Update();

            return RSAData.verify(publicKeys, signed, data);
        } catch (CardException ex) {
        }
        return false;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jp_home = new javax.swing.JPanel();
        txt_home = new javax.swing.JLabel();
        jp_content = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txt_user = new javax.swing.JLabel();
        btn_infor = new javax.swing.JLabel();
        btn_pin = new javax.swing.JLabel();
        txt_diem_danh = new javax.swing.JLabel();
        btn_diem_danh = new javax.swing.JLabel();
        txt_option = new javax.swing.JLabel();
        btn_disconnect = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jp_pin = new javax.swing.JPanel();
        txt_pin_cu = new javax.swing.JLabel();
        edt_pin_cu = new javax.swing.JPasswordField();
        txt_pin_moi = new javax.swing.JLabel();
        edt_pin_moi = new javax.swing.JPasswordField();
        txt_xac_nhan = new javax.swing.JLabel();
        edt_xac_nhan = new javax.swing.JPasswordField();
        btn_update_pin = new javax.swing.JButton();
        btn_cancel_pin = new javax.swing.JButton();
        jp_infor = new javax.swing.JPanel();
        txt_ma_nv = new javax.swing.JLabel();
        edt_ma_nv = new javax.swing.JTextField();
        txt_name = new javax.swing.JLabel();
        edt_name = new javax.swing.JTextField();
        txt_ngay_sinh = new javax.swing.JLabel();
        edt_ngay_sinh = new javax.swing.JTextField();
        txt_co_quan = new javax.swing.JLabel();
        edt_co_quan = new javax.swing.JTextField();
        txt_chuc_vu = new javax.swing.JLabel();
        edt_chuc_vu = new javax.swing.JTextField();
        txt_sdt = new javax.swing.JLabel();
        edt_sdt = new javax.swing.JTextField();
        img_ava = new javax.swing.JLabel();
        txt_ava = new javax.swing.JLabel();
        btn_ava = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        txt_noti_ma_nv = new javax.swing.JLabel();
        txt_noti_name = new javax.swing.JLabel();
        txt_noti_date = new javax.swing.JLabel();
        txt_noti_chuc_vu = new javax.swing.JLabel();
        txt_noti_co_quan = new javax.swing.JLabel();
        txt_noti_phone = new javax.swing.JLabel();
        jp_diem_danh = new javax.swing.JPanel();
        txt_title_diem_danh = new javax.swing.JLabel();
        txt_date = new javax.swing.JLabel();
        txt_time = new javax.swing.JLabel();
        btn_diemdanh = new javax.swing.JButton();
        txt_log_diemdanh = new javax.swing.JScrollPane();
        txt_log = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Trang chủ");
        setLocationByPlatform(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jp_home.setBackground(new java.awt.Color(0, 102, 153));
        jp_home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_home.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txt_home.setForeground(new java.awt.Color(102, 204, 255));
        txt_home.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_home.setText("Trang chủ");
        jp_home.add(txt_home, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 30, 980, -1));

        getContentPane().add(jp_home, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 110));

        txt_user.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_user.setForeground(new java.awt.Color(255, 0, 51));
        txt_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_user.setText("Người dùng");

        btn_infor.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_infor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_infor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-user-32.png"))); // NOI18N
        btn_infor.setText("Thông tin");
        btn_infor.setOpaque(true);
        btn_infor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_inforMouseClicked(evt);
            }
        });

        btn_pin.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_pin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_pin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-pin-code-32.png"))); // NOI18N
        btn_pin.setText("Mã PIN");
        btn_pin.setOpaque(true);
        btn_pin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pinMouseClicked(evt);
            }
        });

        txt_diem_danh.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_diem_danh.setForeground(new java.awt.Color(255, 0, 51));
        txt_diem_danh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_diem_danh.setText("Điểm danh");

        btn_diem_danh.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_diem_danh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_diem_danh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-attendance-32.png"))); // NOI18N
        btn_diem_danh.setText("Điểm danh");
        btn_diem_danh.setOpaque(true);
        btn_diem_danh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_diem_danhMouseClicked(evt);
            }
        });

        txt_option.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_option.setForeground(new java.awt.Color(255, 0, 51));
        txt_option.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_option.setText("Tùy chọn");

        btn_disconnect.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_disconnect.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_disconnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-link-32.png"))); // NOI18N
        btn_disconnect.setText("Ngắt kết nối");
        btn_disconnect.setOpaque(true);
        btn_disconnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_disconnectMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_infor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_diem_danh, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_diem_danh, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_option, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_disconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_infor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_diem_danh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_diem_danh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_option, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_disconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jp_pin.setBackground(new java.awt.Color(255, 255, 255));
        jp_pin.setPreferredSize(new java.awt.Dimension(712, 465));

        txt_pin_cu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_pin_cu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_pin_cu.setText("Mã PIN cũ");

        edt_pin_cu.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_pin_moi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_pin_moi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_pin_moi.setText("Mã PIN mới");

        edt_pin_moi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_xac_nhan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_xac_nhan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_xac_nhan.setText("Xác nhận mã PIN");

        edt_xac_nhan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        btn_update_pin.setBackground(new java.awt.Color(0, 102, 153));
        btn_update_pin.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btn_update_pin.setForeground(new java.awt.Color(255, 255, 255));
        btn_update_pin.setText("Cập nhật");
        btn_update_pin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_update_pinMouseClicked(evt);
            }
        });
        btn_update_pin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_update_pinActionPerformed(evt);
            }
        });

        btn_cancel_pin.setBackground(new java.awt.Color(0, 102, 153));
        btn_cancel_pin.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btn_cancel_pin.setForeground(new java.awt.Color(255, 255, 255));
        btn_cancel_pin.setText("Hủy bỏ");
        btn_cancel_pin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_pinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jp_pinLayout = new javax.swing.GroupLayout(jp_pin);
        jp_pin.setLayout(jp_pinLayout);
        jp_pinLayout.setHorizontalGroup(
            jp_pinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_pinLayout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addGroup(jp_pinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_pinLayout.createSequentialGroup()
                        .addComponent(txt_pin_cu, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edt_pin_cu, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_pinLayout.createSequentialGroup()
                        .addComponent(txt_pin_moi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edt_pin_moi, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_pinLayout.createSequentialGroup()
                        .addComponent(txt_xac_nhan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edt_xac_nhan, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jp_pinLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(btn_update_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(121, 121, 121)
                        .addComponent(btn_cancel_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(81, 81, 81))
        );
        jp_pinLayout.setVerticalGroup(
            jp_pinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_pinLayout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(jp_pinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_pin_cu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edt_pin_cu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jp_pinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_pin_moi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edt_pin_moi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jp_pinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_xac_nhan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edt_xac_nhan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jp_pinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_update_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancel_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jPanel4.add(jp_pin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 410));

        jp_infor.setBackground(new java.awt.Color(255, 255, 255));

        txt_ma_nv.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_ma_nv.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_ma_nv.setText("Mã nhân viên");

        edt_ma_nv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edt_ma_nv.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_name.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_name.setText("Họ tên");

        edt_name.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edt_name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_ngay_sinh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_ngay_sinh.setText("Ngày sinh");

        edt_ngay_sinh.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edt_ngay_sinh.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_co_quan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_co_quan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt_co_quan.setText("Tên cơ quan");

        edt_co_quan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edt_co_quan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_chuc_vu.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_chuc_vu.setText("Chức vụ");

        edt_chuc_vu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edt_chuc_vu.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_sdt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_sdt.setText("Số điện thoại");

        edt_sdt.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edt_sdt.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txt_ava.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_ava.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_ava.setText("Ảnh cá nhân");

        btn_ava.setText("chọn ảnh");
        btn_ava.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_avaActionPerformed(evt);
            }
        });

        btn_edit.setBackground(new java.awt.Color(0, 102, 153));
        btn_edit.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btn_edit.setForeground(new java.awt.Color(255, 255, 255));
        btn_edit.setText("Sửa");
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_update.setBackground(new java.awt.Color(0, 102, 153));
        btn_update.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btn_update.setForeground(new java.awt.Color(255, 255, 255));
        btn_update.setText("Cập nhật");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        txt_noti_ma_nv.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        txt_noti_ma_nv.setForeground(new java.awt.Color(255, 0, 0));
        txt_noti_ma_nv.setText("Mã nhân viên không được để trống");

        txt_noti_name.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        txt_noti_name.setForeground(new java.awt.Color(255, 0, 0));
        txt_noti_name.setText("Họ tên không được để trống");

        txt_noti_date.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        txt_noti_date.setForeground(new java.awt.Color(255, 0, 0));
        txt_noti_date.setText("Ngày sinh không được để trống");

        txt_noti_chuc_vu.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        txt_noti_chuc_vu.setForeground(new java.awt.Color(255, 0, 0));
        txt_noti_chuc_vu.setText("Chức vụ không được để trống");

        txt_noti_co_quan.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        txt_noti_co_quan.setForeground(new java.awt.Color(255, 0, 0));
        txt_noti_co_quan.setText("Tên cơ quan không được để trống");

        txt_noti_phone.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        txt_noti_phone.setForeground(new java.awt.Color(255, 0, 0));
        txt_noti_phone.setText("Số điện thoại không được để trống");

        javax.swing.GroupLayout jp_inforLayout = new javax.swing.GroupLayout(jp_infor);
        jp_infor.setLayout(jp_inforLayout);
        jp_inforLayout.setHorizontalGroup(
            jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_inforLayout.createSequentialGroup()
                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_inforLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_inforLayout.createSequentialGroup()
                                .addComponent(txt_ma_nv, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_noti_ma_nv, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edt_ma_nv, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jp_inforLayout.createSequentialGroup()
                                .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_noti_name, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jp_inforLayout.createSequentialGroup()
                                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_co_quan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_ngay_sinh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_noti_chuc_vu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edt_co_quan, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edt_ngay_sinh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_noti_date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jp_inforLayout.createSequentialGroup()
                                .addComponent(txt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_noti_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jp_inforLayout.createSequentialGroup()
                                .addComponent(txt_chuc_vu, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_noti_co_quan, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edt_chuc_vu, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(img_ava, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_ava, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_ava, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jp_inforLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(144, 144, 144)
                        .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 18, Short.MAX_VALUE))
        );
        jp_inforLayout.setVerticalGroup(
            jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_inforLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_inforLayout.createSequentialGroup()
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_ma_nv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_ma_nv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_noti_ma_nv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_noti_name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_ngay_sinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_ngay_sinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_noti_date)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_co_quan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_co_quan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_noti_co_quan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edt_chuc_vu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_chuc_vu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_noti_chuc_vu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(edt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_noti_phone))
                    .addGroup(jp_inforLayout.createSequentialGroup()
                        .addComponent(img_ava, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_ava, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_ava, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jp_inforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jp_infor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 410));

        jp_diem_danh.setBackground(new java.awt.Color(255, 255, 255));
        jp_diem_danh.setPreferredSize(new java.awt.Dimension(712, 465));

        txt_title_diem_danh.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_title_diem_danh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_title_diem_danh.setText("Điểm danh");

        txt_date.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txt_time.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txt_time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btn_diemdanh.setBackground(new java.awt.Color(0, 102, 153));
        btn_diemdanh.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btn_diemdanh.setForeground(new java.awt.Color(255, 255, 255));
        btn_diemdanh.setText("Điểm danh");
        btn_diemdanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_diemdanhActionPerformed(evt);
            }
        });

        txt_log.setEditable(false);
        txt_log.setColumns(20);
        txt_log.setRows(5);
        txt_log_diemdanh.setViewportView(txt_log);

        javax.swing.GroupLayout jp_diem_danhLayout = new javax.swing.GroupLayout(jp_diem_danh);
        jp_diem_danh.setLayout(jp_diem_danhLayout);
        jp_diem_danhLayout.setHorizontalGroup(
            jp_diem_danhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_diem_danhLayout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addComponent(txt_title_diem_danh, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_diem_danhLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_diemdanh, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(238, 238, 238))
            .addGroup(jp_diem_danhLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jp_diem_danhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_log_diemdanh)
                    .addGroup(jp_diem_danhLayout.createSequentialGroup()
                        .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                        .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jp_diem_danhLayout.setVerticalGroup(
            jp_diem_danhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_diem_danhLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_title_diem_danh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jp_diem_danhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_time, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_diemdanh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_log_diemdanh, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jp_diem_danh, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 410));

        javax.swing.GroupLayout jp_contentLayout = new javax.swing.GroupLayout(jp_content);
        jp_content.setLayout(jp_contentLayout);
        jp_contentLayout.setHorizontalGroup(
            jp_contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_contentLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jp_contentLayout.setVerticalGroup(
            jp_contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_contentLayout.createSequentialGroup()
                .addGroup(jp_contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jp_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 980, 430));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_pinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pinMouseClicked
        // TODO add your handling code here:
        jp_infor.setVisible(false);
        jp_pin.setVisible(true);
        jp_diem_danh.setVisible(false);
        
        btn_infor.setBackground(new Color(240,240,240));
        btn_pin.setBackground(Color.white);
        btn_diem_danh.setBackground(new Color(240,240,240));
        btn_disconnect.setBackground(new Color(240,240,240));
    }//GEN-LAST:event_btn_pinMouseClicked

    private void btn_inforMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_inforMouseClicked
        // TODO add your handling code here:
        clickInfor();
        
    }//GEN-LAST:event_btn_inforMouseClicked

    private void btn_disconnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_disconnectMouseClicked
        // TODO add your handling code here:
        jp_infor.setVisible(false);
        jp_pin.setVisible(false);
        jp_diem_danh.setVisible(false);
        
        btn_infor.setBackground(new Color(240,240,240));
        btn_pin.setBackground(new Color(240,240,240));
        btn_diem_danh.setBackground(new Color(240,240,240));
        btn_disconnect.setBackground(Color.white);
    }//GEN-LAST:event_btn_disconnectMouseClicked

    private void btn_diem_danhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_diem_danhMouseClicked
        // TODO add your handling code here:
        jp_infor.setVisible(false);
        jp_pin.setVisible(false);
        jp_diem_danh.setVisible(true);
        
        btn_infor.setBackground(new Color(240,240,240));
        btn_pin.setBackground(new Color(240,240,240));
        btn_diem_danh.setBackground(Color.white);
        btn_disconnect.setBackground(new Color(240,240,240));
        outputTime();
    }//GEN-LAST:event_btn_diem_danhMouseClicked

    private void btn_avaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_avaActionPerformed
        // TODO add your handling code here:
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new JPEGImageFileFilter());
        jfc.showOpenDialog(this);
        File file = jfc.getSelectedFile();

        if (file != null) {
            if (file.length() > 10000) {
                JOptionPane.showMessageDialog(null, "Kích thước quá lớn. Vui lòng chọn ảnh khác!");
                return;
            }
            try {
                BufferedImage bImage = ImageIO.read(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", bos);
                byte[] napanh = bos.toByteArray();
                DataUser up = new DataUser(dataUser.maNV);
                up.setImage(Arrays.toString(napanh));
                up.Update();
                
            } catch (IOException ex) {
                Logger.getLogger(HomeForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            ReviewAvatarUI avatarUI = new ReviewAvatarUI(file, this);
            avatarUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            avatarUI.setLocationRelativeTo(null);
            avatarUI.setVisible(true);
        }
    }//GEN-LAST:event_btn_avaActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        // TODO add your handling code here:
        if(isEditing){
            editInfor(false);
            reloadInfor();
        }else{
            editInfor(true);
        }
        
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_update_pinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_update_pinMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btn_update_pinMouseClicked

    private void btn_diemdanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_diemdanhActionPerformed
        // TODO add your handling code here:
        if(rsaAuthentication()){
            String date = txt_date.getText();
            String time = txt_time.getText();
            switch (CheckEnd) {
                case 0:
                this.startTime = time;
                this.CheckEnd = 1;
                JOptionPane.showMessageDialog(null, "Điểm danh đến thành công! Chúc bạn một ngày làm việc vui vẻ");
                break;
                case 1:
                inputTime(date, startTime, time);
                outputTime();
                this.CheckEnd = 2;
                JOptionPane.showMessageDialog(null, "Điểm danh thành công!");
                break;
                default:
                JOptionPane.showMessageDialog(null, "Bạn đã điểm danh ngày hôm nay! Vui lòng quay lại vào ngày mai");
                break;
            }
        }
        else{
            System.out.println("RSA ERROR");
        }
    }//GEN-LAST:event_btn_diemdanhActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        // TODO add your handling code here:
        String strId = edt_ma_nv.getText().trim();
        String strName = edt_name.getText().trim();
        String strDate = edt_ngay_sinh.getText().trim();
        String strCoQuan = edt_co_quan.getText().trim();
        String strChucVu = edt_chuc_vu.getText().trim();
        String strPhone = edt_sdt.getText().trim();
           
        if(!checkInfor(strId, strName, strDate, strCoQuan, strChucVu, strPhone)){
            return;
        }
        DataUser up = new DataUser(dataUser.maNV);
        up.setBirth(strDate);
        up.setMaNV(strId);
        up.setName(strName);
        up.setCoquan(strCoQuan);
        up.setChucvu(strChucVu);
        up.setPhone(strPhone);
        
        byte[] byteID = strId.getBytes();
        byte[] byteName = strName.getBytes();
        byte[] byteDate = strDate.getBytes();
        byte[] byteCoQuan = strCoQuan.getBytes();
        byte[] byteChucVu = strChucVu.getBytes();
        byte[] bytePhone = strPhone.getBytes();

        byte[] data = new byte[byteID.length+byteName.length+byteDate.length+bytePhone.length+byteCoQuan.length+byteChucVu.length+12];
        //byteID
        int offSet = 0;
        data[offSet] = (byte)0x02;
        offSet += 1;
        System.arraycopy(byteID, 0,data, offSet, byteID.length);
        offSet += byteID.length;
        data[offSet] = (byte)0x03;
        
        //byteName
        offSet += 1;
        data[offSet] = (byte)0x02;
        offSet += 1;
        System.arraycopy(byteName, 0,data, offSet, byteName.length);
        offSet += byteName.length;
        data[offSet] = (byte) 0x03;
        
        //byteDate
        offSet += 1;
        data[offSet] = (byte) 0x02;
        offSet += 1;
        System.arraycopy(byteDate, 0, data, offSet, byteDate.length);
        offSet += byteDate.length;
        data[offSet] = (byte)0x03;
        
        //bytePhone
        offSet += 1;
        data[offSet] = (byte)0x02;
        offSet += 1;
        System.arraycopy(bytePhone, 0, data, offSet, bytePhone.length);
        offSet += bytePhone.length;
        data[offSet] = (byte)0x03;
        
        //byteCoQuan
        offSet += 1;
        data[offSet] = (byte)0x02;
        offSet += 1;
        System.arraycopy(byteCoQuan, 0, data, offSet, byteCoQuan.length);
        offSet += byteCoQuan.length;
        data[offSet] = (byte)0x03;
        
        //byteChucVu
        offSet += 1;
        data[offSet] = (byte)0x02;
        offSet += 1;
        System.arraycopy(byteChucVu, 0, data, offSet, byteChucVu.length);
        offSet += byteChucVu.length;
        data[offSet] = (byte)0x03;

        if(ConnectCard.getInstance().EditInformation(data)){
            try {
                PublicKey publicKeys = RSAAppletHelper.getInstance(
                    ConnectCard.getInstance().channel).getPublicKey();
            } catch (CardException ex) {
                Logger.getLogger(HomeForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            reloadInfor();

            System.out.println("Success");
            
            int checkDB = up.Update();
            if (checkDB == 1){
                System.out.println("Post Success to DB");
            }else {
                System.out.println("Post Err to DB");
            }
        }
        else{
            System.out.println("Sending Error");
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_cancel_pinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_pinActionPerformed
        // TODO add your handling code here:
        resetFormPin();
    }//GEN-LAST:event_btn_cancel_pinActionPerformed

    private void btn_update_pinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_update_pinActionPerformed
        // TODO add your handling code here:
        String strOld = edt_pin_cu.getText();
        String strNew = edt_pin_moi.getText();
        String strCofirm = edt_xac_nhan.getText();

        if(strNew.equals(strCofirm) && !strNew.equals(strOld)){
            if(ConnectCard.getInstance().ChangePIN(strOld, strNew)){
                System.out.println("PIN CHANGE SUCCESS");                
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] digest = md.digest(strNew.getBytes(StandardCharsets.UTF_8));
                    String sha256 = DatatypeConverter.printHexBinary(digest).toLowerCase();
                    
                    DataUser up = new DataUser(dataUser.maNV);
                    up.setPin(sha256);
                    up.Update();
                    System.out.println("=>>>>>>>>>>>>>>>>>>>");
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(HomeForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                resetFormPin();
            }
            else{
                System.out.println("PIN CHANGE ERROR");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Kiểm tra mã PIN");
        }
    }//GEN-LAST:event_btn_update_pinActionPerformed

    private void initInformation() {
        if(ConnectCard.getInstance().ReadInformation()){
            if(ConnectCard.getInstance().strID.isEmpty()){
                edt_ma_nv.setText("NV001");
            }else{
                edt_ma_nv.setText(ConnectCard.getInstance().strID);
            }
            edt_name.setText(ConnectCard.getInstance().strName);
            System.out.println(ConnectCard.getInstance().strName);
            String add = ConnectCard.getInstance().strName;
            byte[] byteArray = add.getBytes();
            for(int i =0; i< byteArray.length; i++){
                System.out.printf("0x%02X", byteArray[i]);
            }
            edt_ngay_sinh.setText(ConnectCard.getInstance().strDate);
            edt_co_quan.setText(ConnectCard.getInstance().strCoQuan);
            edt_chuc_vu.setText(ConnectCard.getInstance().strChucVu);
            edt_sdt.setText(ConnectCard.getInstance().strPhone);
        }
        getImage();
    }

    private void clickInfor() {
        jp_infor.setVisible(true);
        jp_pin.setVisible(false);
        jp_diem_danh.setVisible(false);
        
        btn_infor.setBackground(Color.white);
        btn_pin.setBackground(new Color(240,240,240));
        btn_diem_danh.setBackground(new Color(240,240,240));
        btn_disconnect.setBackground(new Color(240,240,240));
    }

    private void initUI() {
        clickInfor();
        editInfor(false);
    }

    private void editInfor(boolean b) {
        if(b){
            btn_edit.setText("Huỷ");
        }else{
            btn_edit.setText("Sửa");
        }
        isEditing = b;
        
//        txt_ma_nv.setEnabled(b);
        txt_name.setEnabled(b);
        txt_ngay_sinh.setEnabled(b);
        txt_co_quan.setEnabled(b);
        txt_chuc_vu.setEnabled(b);
        txt_sdt.setEnabled(b);
        edt_ma_nv.setEnabled(b);
        edt_name.setEnabled(b);
        edt_ngay_sinh.setEnabled(b);
        edt_co_quan.setEnabled(b);
        edt_chuc_vu.setEnabled(b);
        edt_sdt.setEnabled(b);
        btn_update.setEnabled(b);
//        img_ava.setEnabled(b);
        txt_ava.setEnabled(b);
        btn_ava.setEnabled(b);
        
        txt_noti_ma_nv.setText(" ");
        txt_noti_name.setText(" ");
        txt_noti_date.setText(" ");
        txt_noti_co_quan.setText(" ");
        txt_noti_chuc_vu.setText(" ");
        txt_noti_phone.setText(" ");
    }

    private void reloadInfor() {
        HomeForm home = new HomeForm();
            home.setVisible(true);
            this.dispose();
    }

    private boolean checkInfor(String strId, String strName, String strDate, String strCoQuan, String strChucVu, String strPhone) {
        boolean check = true;
        if(strId.isEmpty()){
            txt_noti_ma_nv.setText("Mã nhân viên không được để trống");
            check = false;
        }else{
            txt_noti_ma_nv.setText(" ");
        }
        if(strName.isEmpty()){
            txt_noti_name.setText("Họ tên không được để trống");
            check = false;
        }else{
            txt_noti_name.setText(" ");
        }
        if(strDate.isEmpty()){
            txt_noti_date.setText("Ngày sinh không được để trống");
            check = false;
        }else{
            txt_noti_date.setText(" ");
        }
        if(strCoQuan.isEmpty()){
            txt_noti_co_quan.setText("Tên cơ quan không được để trống");
            check = false;
        }else{
            txt_noti_co_quan.setText(" ");
        }
        if(strChucVu.isEmpty()){
            txt_noti_chuc_vu.setText("Chức vụ không được để trống");
            check = false;
        }else{
            txt_noti_chuc_vu.setText(" ");
        }
        if(strPhone.isEmpty()){
            txt_noti_phone.setText("Số điện thoại không được để trống");
            check = false;
        }else{
            txt_noti_phone.setText(" ");
        }
        return check;
    }

    private void resetFormPin() {
        edt_pin_cu.setText("");
        edt_pin_moi.setText("");
        edt_xac_nhan.setText("");
    }

    public class JPEGImageFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.getName().toLowerCase().endsWith(".jpeg")) {
                return true;
            }
            if (f.getName().toLowerCase().endsWith(".jpg")) {
                return true;
            }
            return f.isDirectory();
        }

        @Override
        public String getDescription() {
            return "JPEG files";
        }

    }
    public void getImage() {
        BufferedImage imageBuf = ConnectCard.getInstance().DownloadImage();
        if (imageBuf != null) {
            img_ava.setIcon(new ImageIcon(imageBuf));
        } else {
            img_ava.setHorizontalAlignment(JTextField.CENTER);
            img_ava.setText("Chưa cập nhật");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_ava;
    private javax.swing.JButton btn_cancel_pin;
    private javax.swing.JLabel btn_diem_danh;
    private javax.swing.JButton btn_diemdanh;
    private javax.swing.JLabel btn_disconnect;
    private javax.swing.JButton btn_edit;
    private javax.swing.JLabel btn_infor;
    private javax.swing.JLabel btn_pin;
    private javax.swing.JButton btn_update;
    private javax.swing.JButton btn_update_pin;
    private javax.swing.JTextField edt_chuc_vu;
    private javax.swing.JTextField edt_co_quan;
    private javax.swing.JTextField edt_ma_nv;
    private javax.swing.JTextField edt_name;
    private javax.swing.JTextField edt_ngay_sinh;
    private javax.swing.JPasswordField edt_pin_cu;
    private javax.swing.JPasswordField edt_pin_moi;
    private javax.swing.JTextField edt_sdt;
    private javax.swing.JPasswordField edt_xac_nhan;
    private javax.swing.JLabel img_ava;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jp_content;
    private javax.swing.JPanel jp_diem_danh;
    private javax.swing.JPanel jp_home;
    private javax.swing.JPanel jp_infor;
    private javax.swing.JPanel jp_pin;
    private javax.swing.JLabel txt_ava;
    private javax.swing.JLabel txt_chuc_vu;
    private javax.swing.JLabel txt_co_quan;
    private javax.swing.JLabel txt_date;
    private javax.swing.JLabel txt_diem_danh;
    private javax.swing.JLabel txt_home;
    private javax.swing.JTextArea txt_log;
    private javax.swing.JScrollPane txt_log_diemdanh;
    private javax.swing.JLabel txt_ma_nv;
    private javax.swing.JLabel txt_name;
    private javax.swing.JLabel txt_ngay_sinh;
    private javax.swing.JLabel txt_noti_chuc_vu;
    private javax.swing.JLabel txt_noti_co_quan;
    private javax.swing.JLabel txt_noti_date;
    private javax.swing.JLabel txt_noti_ma_nv;
    private javax.swing.JLabel txt_noti_name;
    private javax.swing.JLabel txt_noti_phone;
    private javax.swing.JLabel txt_option;
    private javax.swing.JLabel txt_pin_cu;
    private javax.swing.JLabel txt_pin_moi;
    private javax.swing.JLabel txt_sdt;
    private javax.swing.JLabel txt_time;
    private javax.swing.JLabel txt_title_diem_danh;
    private javax.swing.JLabel txt_user;
    private javax.swing.JLabel txt_xac_nhan;
    // End of variables declaration//GEN-END:variables
}
