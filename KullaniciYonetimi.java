package CicekStokYonetimi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import CicekStokYonetimi.MailServisi;

public class KullaniciYonetimi {
    private List<Kullanici> kullanicilar;
    private static final String DOSYA_ADI = "kullanicilar.txt";

    public KullaniciYonetimi() {
        kullanicilar = new ArrayList<>();
        dosyadanYukle();

        if (kullanicilar.isEmpty()) {
            kayitOl("admin", "123");
        }
    }

    public boolean kayitOl(String email, String sifre) {

        if (sifre == null || sifre.length() < 6) {
            return false;
        }

        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(emailRegex)) {
            return false;
        }

        String guvenliMail = email.trim().toLowerCase(Locale.ENGLISH);

        for (Kullanici k : kullanicilar) {
            if (k.getEmail().equals(guvenliMail)) {
                return false;
            }
        }


        kullanicilar.add(new Kullanici(guvenliMail, sifre));
        dosyayaKaydet();
        return true;
    }

    public boolean girisYap(String email, String sifre) {
        String guvenliMail = email.trim().toLowerCase(Locale.ENGLISH);

        for (Kullanici k : kullanicilar) {

            if (k.getEmail().equals(guvenliMail) && k.getSifre().equals(sifre)) {
                return true;
            }
        }
        return false;
    }


    public String sifreSifirla(String email) {
        String guvenliMail = email.trim().toLowerCase(Locale.ENGLISH);

        for (Kullanici k : kullanicilar) {
            if (k.getEmail().equals(guvenliMail)) {

                int rastgeleKod = 100000 + (int)(Math.random() * 900000);
                String kodStr = String.valueOf(rastgeleKod);

                new Thread(() -> {
                    MailServisi mailServisi = new MailServisi();
                    mailServisi.mailGonder(guvenliMail, kodStr);
                }).start();

                return kodStr;
            }
        }
        return null;
    }

    public void sifreDegistir(String email, String yeniSifre) {

        if (yeniSifre == null || yeniSifre.length() < 6) {
            return;
        }

        String guvenliMail = email.trim().toLowerCase(Locale.ENGLISH);
        for (Kullanici k : kullanicilar) {
            if (k.getEmail().equals(guvenliMail)) {
                k.setSifre(yeniSifre);
                dosyayaKaydet();
                return;
            }
        }
    }

    private void dosyadanYukle() {
        File file = new File(DOSYA_ADI);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Kullanici k = Kullanici.fromCsvString(line);
                if (k != null) kullanicilar.add(k);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dosyayaKaydet() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DOSYA_ADI, false)))) {
            for (Kullanici k : kullanicilar) {
                out.println(k.toCsvString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}