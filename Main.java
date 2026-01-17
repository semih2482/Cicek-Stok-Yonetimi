package CicekStokYonetimi;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GirisEkrani().setVisible(true));
    }
}