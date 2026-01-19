package CicekStokYonetimi;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailServisi {

    private final String GONDEREN_MAIL = "example@gmail.com";
    private final String GONDEREN_SIFRE = "ksuu tcdm hcym euty";

    public boolean mailGonder(String aliciMail, String kod) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GONDEREN_MAIL, GONDEREN_SIFRE);
            }
        });

        try {
            Message mesaj = new MimeMessage(session);
            mesaj.setFrom(new InternetAddress(GONDEREN_MAIL));
            mesaj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aliciMail));
            mesaj.setSubject("Şifre Sıfırlama Kodu - Çiçek Stok");

            String icerik = "Merhaba,\n\n"
                    + "Şifre sıfırlama talebiniz alındı.\n"
                    + "Doğrulama Kodunuz: " + kod + "\n\n"
                    + "Bu kodu kimseyle paylaşmayın.";

            mesaj.setText(icerik);
            Transport.send(mesaj);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
