package CicekStokYonetimi;

public class Kullanici {
    private String email;
    private String sifre;

    public Kullanici(String email, String sifre) {
        this.email = email;
        this.sifre = sifre;
    }

    public String getEmail() { return email; }
    public String getSifre() { return sifre; }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String toCsvString() {
        return email + ";" + sifre;
    }

    public static Kullanici fromCsvString(String line) {
        try {
            String[] parts = line.split(";");
            return new Kullanici(parts[0], parts[1]);
        } catch (Exception e) {
            return null;
        }
    }
}