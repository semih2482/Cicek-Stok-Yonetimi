package CicekStokYonetimi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GirisEkrani extends JFrame {

    private KullaniciYonetimi kullaniciYonetimi;
    private JTextField txtEmail;
    private JPasswordField txtSifre;
    private JCheckBox chkBeniHatirla;

    private Map<String, String> kayitliKullanicilar = new HashMap<>();


    private final Color CLR_SIDEBAR_BG1 = new Color(216, 29, 29);
    private final Color CLR_SIDEBAR_BG2 = new Color(72, 126, 176);
    private final Color CLR_ACCENT      = new Color(85, 204, 46);
    private final Color CLR_TEXT_MAIN   = new Color(0, 0, 0);
    private final Color CLR_TEXT_SUB    = new Color(0, 0, 0);


    private final String HATIRLA_DOSYASI = "beni_hatirla.txt";

    public GirisEkrani() {
        kullaniciYonetimi = new KullaniciYonetimi();
        setupFrame();
        initUI();

        beniHatirlaYukle();
        setupEmailAutocomplete();
    }

    private void setupFrame() {
        setTitle("Çiçek Stok Yönetim Sistemi");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    private void initUI() {

        JPanel pnlLeft = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, CLR_SIDEBAR_BG1, getWidth(), getHeight(), CLR_SIDEBAR_BG2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlLeft.setPreferredSize(new Dimension(350, 0));
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setBorder(new EmptyBorder(0, 40, 0, 40));

        JLabel lblLogo = new JLabel("🌺");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrand = new JLabel("ÇİÇEK STOK");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlLeft.add(Box.createVerticalGlue());
        pnlLeft.add(lblLogo);
        pnlLeft.add(Box.createVerticalStrut(20));
        pnlLeft.add(lblBrand);
        pnlLeft.add(Box.createVerticalGlue());


        JPanel pnlRight = new JPanel();
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setLayout(new GridBagLayout());
        pnlRight.setBorder(new EmptyBorder(40, 60, 40, 60));

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblGirisBaslik = new JLabel("Hoş Geldiniz!");
        lblGirisBaslik.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblGirisBaslik.setForeground(CLR_TEXT_MAIN);
        lblGirisBaslik.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblGirisAlt = new JLabel("Devam etmek için lütfen bilgilerinizi giriniz.");
        lblGirisAlt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblGirisAlt.setForeground(CLR_TEXT_SUB);
        lblGirisAlt.setAlignmentX(Component.LEFT_ALIGNMENT);


        txtEmail = createModernTextField();
        JPanel pnlEmail = createInputWrapper("✉", txtEmail);

        txtSifre = createModernPasswordField();
        txtSifre.setEchoChar('•');
        JPanel pnlSifre = createPasswordWrapper("🔒", txtSifre);

        JPanel pnlOptions = new JPanel(new BorderLayout());
        pnlOptions.setBackground(Color.WHITE);
        pnlOptions.setMaximumSize(new Dimension(400, 30));
        pnlOptions.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkBeniHatirla = new JCheckBox("Beni Hatırla");
        chkBeniHatirla.setBackground(Color.WHITE);
        chkBeniHatirla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkBeniHatirla.setForeground(CLR_TEXT_SUB);
        chkBeniHatirla.setFocusPainted(false);

        JLabel lblUnuttum = new JLabel("<html><u>Şifremi Unuttum</u></html>");
        lblUnuttum.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUnuttum.setForeground(CLR_ACCENT);
        lblUnuttum.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlOptions.add(chkBeniHatirla, BorderLayout.WEST);
        pnlOptions.add(lblUnuttum, BorderLayout.EAST);


        JButton btnGiris = createWideButton("GİRİŞ YAP", CLR_ACCENT, Color.WHITE);
        JButton btnKayit = createWideButton("Hesap Oluştur", Color.WHITE, CLR_ACCENT);
        btnKayit.setBorder(new LineBorder(CLR_ACCENT, 1));

        pnlForm.add(lblGirisBaslik);
        pnlForm.add(Box.createVerticalStrut(5));
        pnlForm.add(lblGirisAlt);
        pnlForm.add(Box.createVerticalStrut(35));
        pnlForm.add(new JLabel("E-Posta Adresi"));
        pnlForm.add(Box.createVerticalStrut(5));
        pnlForm.add(pnlEmail);
        pnlForm.add(Box.createVerticalStrut(15));
        pnlForm.add(new JLabel("Şifre"));
        pnlForm.add(Box.createVerticalStrut(5));
        pnlForm.add(pnlSifre);
        pnlForm.add(Box.createVerticalStrut(10));
        pnlForm.add(pnlOptions);
        pnlForm.add(Box.createVerticalStrut(30));
        pnlForm.add(btnGiris);
        pnlForm.add(Box.createVerticalStrut(10));
        pnlForm.add(btnKayit);

        pnlRight.add(pnlForm);

        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        btnGiris.addActionListener(e -> {
            String mail = txtEmail.getText().trim();
            String pass = new String(txtSifre.getPassword()).trim();

            if (kullaniciYonetimi.girisYap(mail, pass)) {
                beniHatirlaIslemi(mail, pass);
                new CicekArayuz().setVisible(true);
                this.dispose();
            } else {
                msg("Kullanıcı adı veya şifre hatalı.", true);
            }
        });

        btnKayit.addActionListener(e -> {
            String mail = txtEmail.getText().trim();
            String pass = new String(txtSifre.getPassword()).trim();

            if (mail.isEmpty() || pass.isEmpty()) {
                msg("Lütfen tüm alanları doldurunuz.", true);
                return;
            }
            if (pass.length() < 6) {
                msg("Şifre en az 6 karakterden oluşmalıdır!", true);
                return;
            }

            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if (!mail.matches(emailRegex)) {
                msg("Geçersiz E-Posta Formatı!", true);
                return;
            }

            if (kullaniciYonetimi.kayitOl(mail, pass)) {
                msg("Kayıt Başarılı! Şimdi giriş yapabilirsiniz.", false);
            } else {
                msg("Bu e-posta adresi zaten kayıtlı.", true);
            }
        });

        lblUnuttum.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                String mail = JOptionPane.showInputDialog(GirisEkrani.this, "Sıfırlama kodu için mail adresinizi giriniz:");

                if (mail != null && !mail.trim().isEmpty()) {

                    if (!mail.contains("@")) {
                        msg("Geçersiz mail formatı!", true);
                        return;
                    }

                    String gercekKod = kullaniciYonetimi.sifreSifirla(mail);

                    if (gercekKod != null) {

                        String girilenKod = JOptionPane.showInputDialog(GirisEkrani.this,
                                "Mail adresinize (" + mail + ") doğrulama kodu gönderildi.\n" +
                                        "Lütfen mailinizi kontrol edip kodu giriniz:");

                        if (girilenKod != null && girilenKod.equals(gercekKod)) {

                            String yeniSifre = JOptionPane.showInputDialog(GirisEkrani.this, "Doğrulama Başarılı! Yeni şifrenizi giriniz:");

                            if (yeniSifre != null && yeniSifre.trim().length() < 6) {
                                msg("Şifre en az 6 karakterden oluşmalıdır! İşlem iptal edildi.", true);
                                return;
                            }

                            if (yeniSifre != null && !yeniSifre.trim().isEmpty()) {
                                kullaniciYonetimi.sifreDegistir(mail, yeniSifre);
                                msg("Şifreniz başarıyla güncellendi.", false);
                            }
                        } else {
                            if (girilenKod != null) msg("Hatalı Kod! İşlem iptal edildi.", true);
                        }
                    } else {
                        msg("Bu mail adresiyle kayıtlı kullanıcı bulunamadı.", true);
                    }
                }
            }
        });
    }


    private void setupEmailAutocomplete() {
        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }

                String yazilan = txtEmail.getText();
                if (yazilan.isEmpty()) return;

                for (String kayitliMail : kayitliKullanicilar.keySet()) {

                    if (kayitliMail.toLowerCase().startsWith(yazilan.toLowerCase())) {

                        txtEmail.setText(kayitliMail);
                        txtEmail.setCaretPosition(kayitliMail.length());
                        txtEmail.moveCaretPosition(yazilan.length());
                        String kayitliSifre = kayitliKullanicilar.get(kayitliMail);
                        txtSifre.setText(kayitliSifre);
                        chkBeniHatirla.setSelected(true);

                        break;
                    }
                }
            }
        });
    }


    private void beniHatirlaIslemi(String mail, String pass) {
        File file = new File(HATIRLA_DOSYASI);

        if (chkBeniHatirla.isSelected()) {
            kayitliKullanicilar.put(mail, pass);
        } else {
            kayitliKullanicilar.remove(mail);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Map.Entry<String, String> entry : kayitliKullanicilar.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }


    private void beniHatirlaYukle() {
        File file = new File(HATIRLA_DOSYASI);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(";")) {
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {

                        kayitliKullanicilar.put(parts[0], parts[1]);
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private JPanel createInputWrapper(String icon, JComponent field) {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setBackground(new Color(245, 246, 250));
        pnl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 221, 225), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        pnl.setMaximumSize(new Dimension(400, 45));
        JLabel lbl = new JLabel(icon);
        lbl.setForeground(CLR_TEXT_SUB);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        pnl.add(lbl, BorderLayout.WEST);
        pnl.add(field, BorderLayout.CENTER);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                pnl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(CLR_ACCENT, 1), new EmptyBorder(8, 12, 8, 12)));
                pnl.setBackground(Color.WHITE);
            }
            public void focusLost(FocusEvent e) {
                pnl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 221, 225), 1), new EmptyBorder(8, 12, 8, 12)));
                pnl.setBackground(new Color(245, 246, 250));
            }
        });
        return pnl;
    }

    private JPanel createPasswordWrapper(String icon, JPasswordField field) {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setBackground(new Color(245, 246, 250));
        pnl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 221, 225), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        pnl.setMaximumSize(new Dimension(400, 45));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setForeground(CLR_TEXT_SUB);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        pnl.add(lblIcon, BorderLayout.WEST);
        pnl.add(field, BorderLayout.CENTER);

        JToggleButton btnShow = new JToggleButton("👁");
        btnShow.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnShow.setForeground(CLR_TEXT_SUB);
        btnShow.setBorder(null);
        btnShow.setContentAreaFilled(false); // Arkaplanı şeffaf yap
        btnShow.setFocusPainted(false);
        btnShow.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnShow.addActionListener(e -> {
            if (btnShow.isSelected()) {
                field.setEchoChar((char) 0);
                btnShow.setForeground(CLR_ACCENT);
            } else {
                field.setEchoChar('•');
                btnShow.setForeground(CLR_TEXT_SUB);
            }
        });

        pnl.add(btnShow, BorderLayout.EAST);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                pnl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(CLR_ACCENT, 1), new EmptyBorder(8, 12, 8, 12)));
                pnl.setBackground(Color.WHITE);
            }
            public void focusLost(FocusEvent e) {
                pnl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 221, 225), 1), new EmptyBorder(8, 12, 8, 12)));
                pnl.setBackground(new Color(245, 246, 250));
            }
        });

        return pnl;
    }

    private JTextField createModernTextField() {
        JTextField tf = new JTextField();
        tf.setBorder(null);
        tf.setBackground(new Color(245, 246, 250));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(CLR_TEXT_MAIN);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { tf.setBackground(Color.WHITE); }
            public void focusLost(FocusEvent e) { tf.setBackground(new Color(245, 246, 250)); }
        });
        return tf;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setBorder(null);
        pf.setBackground(new Color(245, 246, 250));
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pf.setForeground(CLR_TEXT_MAIN);
        pf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { pf.setBackground(Color.WHITE); }
            public void focusLost(FocusEvent e) { pf.setBackground(new Color(245, 246, 250)); }
        });
        return pf;
    }

    private JButton createWideButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(400, 45));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (bg != Color.WHITE) btn.setBackground(bg.brighter());
                else btn.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void msg(String m, boolean err) {
        JOptionPane.showMessageDialog(this, m, err ? "Hata" : "Bilgi", err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}