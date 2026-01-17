package CicekStokYonetimi;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CicekArayuz extends JFrame {
    private StokYonetimi yonetim;
    private DefaultTableModel tabloModeli;
    private JTable tablo;
    private JTextField txtAd, txtRenk, txtStok, txtFiyat, txtArama;
    private JLabel lblSeciliId;
    private JLabel lblCardStok, lblCardDeger, lblCardCesit;


    private final Color CLR_SIDEBAR_START = new Color(39, 71, 33); // Koyu Mor
    private final Color CLR_SIDEBAR_END   = new Color(188, 99, 200);
    private final Color CLR_ACCENT        = new Color(52, 152, 219);
    private final Color CLR_DANGER        = new Color(231, 76, 60);
    private final Color CLR_SUCCESS       = new Color(46, 204, 113);
    private final Color CLR_WARNING       = new Color(241, 196, 15);
    private final Color CLR_GLASS_WHITE   = new Color(7, 178, 237, 255);
    private final Color CLR_TEXT_DARK     = new Color(19, 65, 145);


    private final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 22);
    private final Font FONT_STD    = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD   = new Font("Segoe UI", Font.BOLD, 14);

    public CicekArayuz() {
        yonetim = new StokYonetimi();
        setupFrame();
        initUI();
        listeyiGuncelle(yonetim.getCicekListesi());
    }

    private void setupFrame() {
        setTitle("Profesyonel Stok Yönetimi");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bgPanel = new BackgroundPanel("_.jpeg");
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);
    }

    private void initUI() {
        GradientPanel pnlSidebar = new GradientPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setPreferredSize(new Dimension(340, 0));
        pnlSidebar.setBorder(new EmptyBorder(30, 30, 30, 30));


        JLabel lblLogo = new JLabel("🌺 ÇiçekStok");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Envanter Yönetim Sistemi");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(189, 195, 199));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlSidebar.add(lblLogo);
        pnlSidebar.add(lblSub);
        pnlSidebar.add(Box.createVerticalStrut(40));


        addSidebarSectionTitle(pnlSidebar, "🌼 YENİ KAYIT / DÜZENLEME");

        lblSeciliId = new JLabel("Seçim: Yok");
        lblSeciliId.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSeciliId.setForeground(CLR_WARNING);
        lblSeciliId.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlSidebar.add(lblSeciliId);
        pnlSidebar.add(Box.createVerticalStrut(10));

        txtAd = addModernInput(pnlSidebar, "Çiçek Adı");
        txtRenk = addModernInput(pnlSidebar, "Renk");
        txtStok = addModernInput(pnlSidebar, "Stok Adedi");
        txtFiyat = addModernInput(pnlSidebar, "Birim Fiyat (TL)");

        pnlSidebar.add(Box.createVerticalStrut(25));

        JPanel pnlBtns = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlBtns.setOpaque(false);
        pnlBtns.setMaximumSize(new Dimension(300, 90));
        pnlBtns.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnEkle = createModernButton("EKLE", CLR_SUCCESS);
        JButton btnGuncelle = createModernButton("GÜNCELLE", CLR_WARNING);
        JButton btnSil = createModernButton("SİL", CLR_DANGER);
        JButton btnTemizle = createModernButton("TEMİZLE", new Color(149, 165, 166));

        pnlBtns.add(btnEkle); pnlBtns.add(btnGuncelle);
        pnlBtns.add(btnSil); pnlBtns.add(btnTemizle);
        pnlSidebar.add(pnlBtns);

        pnlSidebar.add(Box.createVerticalStrut(30));
        pnlSidebar.add(new JSeparator(SwingConstants.HORIZONTAL));
        pnlSidebar.add(Box.createVerticalStrut(20));


        JButton btnSatis = createModernButton("🛒 HIZLI SATIŞ", CLR_ACCENT);
        btnSatis.setMaximumSize(new Dimension(300, 45));
        pnlSidebar.add(btnSatis);

        pnlSidebar.add(Box.createVerticalStrut(10));

        JButton btnCikis = createModernButton("🔒 ÇIKIŞ YAP", new Color(44, 62, 80).darker());
        btnCikis.setMaximumSize(new Dimension(300, 45));
        pnlSidebar.add(btnCikis);



        JPanel pnlContent = new JPanel(new BorderLayout(25, 25));
        pnlContent.setOpaque(false);
        pnlContent.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel pnlStats = new JPanel(new GridLayout(1, 3, 25, 0));
        pnlStats.setOpaque(false);
        pnlStats.setPreferredSize(new Dimension(0, 130));

        lblCardStok = createStatCard(pnlStats, "TOPLAM STOK", "📦", CLR_SUCCESS);
        lblCardDeger = createStatCard(pnlStats, "FİNANSAL DEĞER", "💵", CLR_ACCENT);
        lblCardCesit = createStatCard(pnlStats, "ÇEŞİT SAYISI", "🌸", new Color(155, 89, 182));

        pnlContent.add(pnlStats, BorderLayout.NORTH);

        JPanel pnlTableCard = new RoundedPanel(20, CLR_GLASS_WHITE);
        pnlTableCard.setLayout(new BorderLayout(0, 0));
        pnlTableCard.setBorder(new EmptyBorder(20, 20, 20, 20));


        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 5, 15, 5));

        JLabel lblTableTitle = new JLabel("Envanter Listesi");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTableTitle.setForeground(CLR_TEXT_DARK);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setOpaque(false);

        txtArama = new JTextField(" Ara...", 15);
        styleSearchField(txtArama);

        JButton btnYenile = new JButton("\u21BB");
        styleMiniButton(btnYenile);

        pnlSearch.add(txtArama);
        pnlSearch.add(btnYenile);

        pnlHeader.add(lblTableTitle, BorderLayout.WEST);
        pnlHeader.add(pnlSearch, BorderLayout.EAST);

        pnlTableCard.add(pnlHeader, BorderLayout.NORTH);

        String[] cols = {"ID", "Çiçek Adı", "Renk", "Stok", "Fiyat (TL)", "Satılan"};
        tabloModeli = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablo = new JTable(tabloModeli);
        styleTableProfessional();

        JScrollPane scroll = new JScrollPane(tablo);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setOpaque(false);
        pnlTableCard.add(scroll, BorderLayout.CENTER);


        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        pnlFooter.add(new JLabel("Hızlı Sıralama: "));
        JButton s1 = createChipButton("Fiyat (Artan)");
        JButton s2 = createChipButton("Fiyat (Azalan)");
        JButton s3 = createChipButton("Çok Satanlar");
        pnlFooter.add(s1); pnlFooter.add(s2); pnlFooter.add(s3);

        pnlTableCard.add(pnlFooter, BorderLayout.SOUTH);

        pnlContent.add(pnlTableCard, BorderLayout.CENTER);

        add(pnlSidebar, BorderLayout.WEST);
        add(pnlContent, BorderLayout.CENTER);

        setupActions(btnEkle, btnGuncelle, btnSil, btnTemizle, btnSatis, btnCikis, btnYenile, s1, s2, s3);
    }


    private void setupActions(JButton ekle, JButton guncelle, JButton sil, JButton temizle, JButton satis, JButton cikis, JButton yenile, JButton s1, JButton s2, JButton s3) {

        tablo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tablo.getSelectedRow();
                if (row != -1) {
                    lblSeciliId.setText("Seçili ID: " + tabloModeli.getValueAt(row, 0));
                    txtAd.setText(tabloModeli.getValueAt(row, 1).toString());
                    txtRenk.setText(tabloModeli.getValueAt(row, 2).toString());
                    txtStok.setText(tabloModeli.getValueAt(row, 3).toString());
                    txtFiyat.setText(tabloModeli.getValueAt(row, 4).toString());
                }
            }
        });


        ekle.addActionListener(e -> islemEkle());
        guncelle.addActionListener(e -> islemGuncelle());
        sil.addActionListener(e -> islemSil());
        temizle.addActionListener(e -> formuTemizle());
        satis.addActionListener(e -> islemSatis());

        cikis.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Oturumu kapatmak istiyor musunuz?", "Çıkış", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.dispose();
                new GirisEkrani().setVisible(true);
            }
        });


        txtArama.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                String text = txtArama.getText().equals(" Ara...") ? "" : txtArama.getText();
                listeyiGuncelle(yonetim.ismeGoreAra(text));
            }
        });
        yenile.addActionListener(e -> { txtArama.setText(" Ara..."); listeyiGuncelle(yonetim.getCicekListesi()); });
        s1.addActionListener(e -> listeyiGuncelle(yonetim.getFiyataGoreSirali(true)));
        s2.addActionListener(e -> listeyiGuncelle(yonetim.getFiyataGoreSirali(false)));
        s3.addActionListener(e -> listeyiGuncelle(yonetim.getEnCokSatilanlar()));
    }

    private JLabel createStatCard(JPanel parent, String title, String icon, Color accentColor) {
        JPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));


        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        card.add(lblIcon, BorderLayout.WEST);


        JPanel pnlText = new JPanel(new GridLayout(2, 1));
        pnlText.setOpaque(false);
        pnlText.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel("0");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(accentColor);

        pnlText.add(lblTitle);
        pnlText.add(lblValue);
        card.add(pnlText, BorderLayout.CENTER);

        parent.add(card);
        return lblValue;
    }

    private JTextField addModernInput(JPanel p, String title) {
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(new Color(200, 200, 200));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(5));

        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(300, 40));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setOpaque(false);
        tf.setBackground(new Color(255, 255, 255, 30)); // %12 Opak beyaz

        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255, 50), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(255, 255, 255, 180), 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                tf.setBackground(new Color(255, 255, 255, 50));
            }
            public void focusLost(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(255, 255, 255, 50), 1, true),
                        new EmptyBorder(5, 10, 5, 10)
                ));
                tf.setBackground(new Color(255, 255, 255, 30));
            }
        });

        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(tf);
        p.add(Box.createVerticalStrut(15));
        return tf;
    }

    private JButton createModernButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private void styleTableProfessional() {
        tablo.setRowHeight(55);
        tablo.setShowVerticalLines(false);
        tablo.setShowHorizontalLines(true);
        tablo.setGridColor(new Color(240, 240, 240));
        tablo.setIntercellSpacing(new Dimension(0, 0));
        tablo.setSelectionBackground(new Color(236, 240, 241));
        tablo.setSelectionForeground(CLR_TEXT_DARK);


        JTableHeader header = tablo.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(Color.WHITE);
                lbl.setForeground(new Color(150, 150, 150));
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(245, 245, 245)));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setPreferredSize(new Dimension(0, 50));
                return lbl;
            }
        });


        tablo.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                if(!isSelected) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(240, 248, 255));
                }
                c.setForeground(CLR_TEXT_DARK);
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
                setBorder(new EmptyBorder(0, 15, 0, 15));

                if(col == 3) {
                    try {
                        int stok = Integer.parseInt(value.toString());
                        setFont(new Font("Segoe UI", Font.BOLD, 14));
                        if(stok < 400) {
                            c.setForeground(CLR_DANGER);
                        } else {
                            c.setForeground(CLR_SUCCESS);
                        }
                    } catch (NumberFormatException e) {
                        c.setForeground(CLR_TEXT_DARK);
                    }
                }


                if(col == 4) {
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                    setHorizontalAlignment(JLabel.RIGHT);
                    setText(value + " ₺");
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                }


                if(col == 1) setHorizontalAlignment(JLabel.LEFT);

                return c;
            }
        });
    }


    private void islemEkle() {
        String ad = txtAd.getText().trim();
        String renk = txtRenk.getText().trim();
        if(!validasyon(ad, renk)) return;

        try {
            int stok = Integer.parseInt(txtStok.getText().trim());
            int fiyat = Integer.parseInt(txtFiyat.getText().trim());

            if (stok < 0 || fiyat < 0) throw new NumberFormatException();

            if (stok > 500 || fiyat > 250) {
                msg("Hata: Stok 500'den veya Fiyat 250'den büyük olamaz!", true);
                return;
            }

            boolean sonuc = yonetim.cicekEkle(ad, renk, stok, fiyat);

            if (sonuc) {
                listeyiGuncelle(yonetim.getCicekListesi());
                formuTemizle();
                msg("Çiçek başarıyla eklendi.", false);
            } else {
                msg("HATA: '" + ad + "' isminde bir çiçek zaten mevcut!", true);
            }


        } catch (NumberFormatException e) { msg("Sayısal değerler hatalı.", true); }
    }

    private void islemGuncelle() {
        int row = tablo.getSelectedRow();
        if (row == -1) { msg("Seçim yapınız.", true); return; }

        int id = (int) tabloModeli.getValueAt(row, 0);
        String ad = txtAd.getText().trim();
        String renk = txtRenk.getText().trim();
        if(!validasyon(ad, renk)) return;

        try {
            int stok = Integer.parseInt(txtStok.getText().trim());
            int fiyat = Integer.parseInt(txtFiyat.getText().trim());
            if (stok < 0 || fiyat < 0) throw new NumberFormatException();

            if (fiyat > 250) {
                msg("Hata: Fiyat 250 TL'den fazla olamaz!", true);
                return;
            }

            boolean sonuc = yonetim.cicekGuncelle(id, ad, renk, stok, fiyat);

            if (sonuc) {
                stokBildirimiKontrol(ad, stok);
                listeyiGuncelle(yonetim.getCicekListesi());
                formuTemizle();
                msg("Kayıt güncellendi.", false);
            } else {
                msg("HATA: '" + ad + "' ismi başka bir çiçek tarafından kullanılıyor!", true);
            }

        } catch (NumberFormatException e) { msg("Sayısal değer hatası.", true); }
    }

    private void islemSil() {
        int row = tablo.getSelectedRow();
        if (row != -1) {
            if (JOptionPane.showConfirmDialog(this, "Silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                yonetim.cicekSil((int) tabloModeli.getValueAt(row, 0));
                listeyiGuncelle(yonetim.getCicekListesi());
                formuTemizle();
            }
        } else msg("Silinecek kaydı seçiniz.", true);
    }

    private void islemSatis() {
        int row = tablo.getSelectedRow();
        if (row == -1) { msg("Satış yapılacak çiçeği seçiniz.", true); return; }
        String miktar = JOptionPane.showInputDialog(this, "Satış Adedi:");
        if(miktar != null) {
            try {
                int adet = Integer.parseInt(miktar);
                int id = (int) tabloModeli.getValueAt(row, 0);
                if(yonetim.cicekSat(id, adet)) {

                    for(Cicek c : yonetim.getCicekListesi()) {
                        if(c.getId() == id) { stokBildirimiKontrol(c.getAd(), c.getStokAdedi()); break; }
                    }
                    listeyiGuncelle(yonetim.getCicekListesi());
                    msg("Satış Başarılı.", false);
                } else msg("Yetersiz Stok!", true);
            } catch(Exception e) { msg("Geçersiz miktar.", true); }
        }
    }

    private void listeyiGuncelle(List<Cicek> liste) {
        tabloModeli.setRowCount(0);
        long tStok = 0;
        long tDeger = 0;
        for (Cicek c : liste) {
            tabloModeli.addRow(new Object[]{c.getId(), c.getAd(), c.getRenk(), c.getStokAdedi(), c.getFiyat(), c.getSatilanMiktar()});
            tStok += c.getStokAdedi();
            tDeger += (long)c.getStokAdedi() * c.getFiyat();
        }
        lblCardStok.setText(String.valueOf(tStok));
        lblCardDeger.setText(NumberFormat.getCurrencyInstance(new Locale("tr", "TR")).format(tDeger));
        lblCardCesit.setText(String.valueOf(liste.size()));
    }

    private boolean validasyon(String ad, String renk) {
        if (ad.isEmpty() || renk.isEmpty()) { msg("Alanlar boş olamaz.", true); return false; }
        if (!ad.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ ]+") || !renk.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ ]+")) {
            msg("İsim/Renk alanında sayı olamaz.", true); return false;
        }
        return true;
    }

    private void formuTemizle() {
        txtAd.setText(""); txtRenk.setText(""); txtStok.setText(""); txtFiyat.setText("");
        lblSeciliId.setText("Seçim: Yok"); tablo.clearSelection();
    }

    private void msg(String m, boolean err) {
        JOptionPane.showMessageDialog(this, m, err ? "Hata" : "İşlem Tamam", err ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    private void stokBildirimiKontrol(String ad, int stok) {
        if (stok < 400) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "⚠️ DİKKAT: " + ad + " stoğu kritik seviyede! (" + stok + ")", "Stok Uyarısı", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addSidebarSectionTitle(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(new Color(255, 255, 255, 100));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(5));
    }

    private void styleSearchField(JTextField tf) {
        tf.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        tf.setPreferredSize(new Dimension(200, 30));
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(tf.getText().contains("Ara")) tf.setText(""); tf.setBorder(new LineBorder(CLR_ACCENT, 1, true)); }
            public void focusLost(FocusEvent e) { if(tf.getText().isEmpty()) tf.setText(" Ara..."); tf.setBorder(new LineBorder(new Color(200, 200, 200), 1, true)); }
        });
    }

    private void styleMiniButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        btn.setPreferredSize(new Dimension(35, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JButton createChipButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(CLR_TEXT_DARK);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gp = new GradientPaint(0, 0, CLR_SIDEBAR_START, 0, getHeight(), CLR_SIDEBAR_END);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }


    class BackgroundPanel extends JPanel {
        private Image img;
        public BackgroundPanel(String path) {
            try { img = new ImageIcon(path).getImage(); } catch(Exception e){}
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(img!=null) g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            else { g.setColor(new Color(236, 240, 241)); g.fillRect(0,0,getWidth(),getHeight()); }
        }
    }

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}