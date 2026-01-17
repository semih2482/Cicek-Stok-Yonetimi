package CicekStokYonetimi;

public class Cicek {
    private int id;
    private String ad;
    private String renk;
    private int stokAdedi;
    private int fiyat;
    private int satilanMiktar;

    public Cicek(int id, String ad, String renk, int stokAdedi, int fiyat, int satilanMiktar) {
        this.id = id;
        this.ad = ad;
        this.renk = renk;
        this.stokAdedi = stokAdedi;
        this.fiyat = fiyat;
        this.satilanMiktar = satilanMiktar;
    }

    public String toCsvString(String ayra) {
        return id + ayra + ad + ayra + renk + ayra + stokAdedi + ayra + fiyat + ayra + satilanMiktar;
    }

    public static Cicek fromCsvString(String csvLine, String ayra) {
        try {
            String[] parts = csvLine.split(ayra);
            int id = Integer.parseInt(parts[0]);
            String ad = parts[1];
            String renk = parts[2];
            int stok = Integer.parseInt(parts[3]);
            int fiyat = Integer.parseInt(parts[4]);

            int satilan = 0;
            if (parts.length > 5) {
                satilan = Integer.parseInt(parts[5]);
            }

            return new Cicek(id, ad, renk, stok, fiyat, satilan);
        } catch (Exception e) {
            return null;
        }
    }

    public void satisYap(int adet) {
        this.stokAdedi -= adet;
        this.satilanMiktar += adet;
    }

    public int getId() { return id; }
    public String getAd() { return ad; }
    public String getRenk() { return renk; }
    public int getStokAdedi() { return stokAdedi; }
    public void setStokAdedi(int stokAdedi) { this.stokAdedi = stokAdedi; }
    public int getFiyat() { return fiyat; }
    public int getSatilanMiktar() { return satilanMiktar; }


    public void setAd(String ad) {
        this.ad = ad;
    }

    public void setRenk(String renk) {
        this.renk = renk;
    }

    public void setFiyat(int fiyat) {
        this.fiyat = fiyat;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Ad: " + ad + " | Stok: " + stokAdedi + " | Fiyat: " + fiyat + " TL";
    }
}