package CicekStokYonetimi;

import java.io.*;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StokYonetimi {

    private List<Cicek> cicekListesi;
    private int sonrakiId = 1;

    private static final String DOSYA_ADI = "cicek-stok-yonetimi.txt";
    private static final String AYRAC = ";";

    public StokYonetimi() {
        this.cicekListesi = new LinkedList<>();
        dosyadanYukle();
    }

    public boolean cicekEkle(String ad, String renk, int stok, int fiyat) {
        for (Cicek c : cicekListesi) {
            if (c.getAd().equalsIgnoreCase(ad)) {
                return false;
            }
        }

        Cicek yeniCicek = new Cicek(sonrakiId, ad, renk, stok, fiyat, 0);
        cicekListesi.add(yeniCicek);
        tumListeyiDosyayaKaydet();
        sonrakiId++;
        return true;
    }

    public boolean cicekSat(int id, int adet) {
        Cicek cicek = idIleCicekBul(id);
        if (cicek != null && cicek.getStokAdedi() >= adet) {
            cicek.satisYap(adet);
            tumListeyiDosyayaKaydet();
            return true;
        }
        return false;
    }

    public boolean cicekGuncelle(int id, String yeniAd, String yeniRenk, int yeniStok, int yeniFiyat) {

        for (Cicek c : cicekListesi) {

            if (c.getAd().equalsIgnoreCase(yeniAd) && c.getId() != id) {
                return false;
            }
        }

        Cicek cicek = idIleCicekBul(id);
        if (cicek != null) {
            cicek.setAd(yeniAd);
            cicek.setRenk(yeniRenk);
            cicek.setStokAdedi(yeniStok);
            cicek.setFiyat(yeniFiyat);
            tumListeyiDosyayaKaydet();
            return true;
        }
        return false;
    }

    public void cicekSil(int id) {
        Cicek cicek = idIleCicekBul(id);
        if (cicek != null) {
            cicekListesi.remove(cicek);
            tumListeyiDosyayaKaydet();
        }
    }

    public List<Cicek> getFiyataGoreSirali(boolean artan) {

        List<Cicek> siraliListe = new LinkedList<>(cicekListesi);
        if (artan) {
            siraliListe.sort(Comparator.comparingInt(Cicek::getFiyat));
        } else {
            siraliListe.sort(Comparator.comparingInt(Cicek::getFiyat).reversed());
        }
        return siraliListe;
    }

    public List<Cicek> getAlfabetikSirali() {
        List<Cicek> siraliListe = new LinkedList<>(cicekListesi);
        siraliListe.sort(Comparator.comparing(Cicek::getAd, String.CASE_INSENSITIVE_ORDER));
        return siraliListe;
    }

    public List<Cicek> getEnCokSatilanlar() {

        List<Cicek> siraliListe = new LinkedList<>(cicekListesi);
        siraliListe.sort(Comparator.comparingInt(Cicek::getSatilanMiktar).reversed());
        return siraliListe;
    }

    public List<Cicek> ismeGoreAra(String arananMetin) {
        return cicekListesi.stream()
                .filter(c -> c.getAd().toLowerCase().contains(arananMetin.toLowerCase()))

                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Cicek idIleCicekBul(int id) {
        for (Cicek cicek : cicekListesi) {
            if (cicek.getId() == id) {
                return cicek;
            }
        }
        return null;
    }

    private void dosyadanYukle() {
        File file = new File(DOSYA_ADI);
        if (!file.exists()) return;

        int enBuyukId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(DOSYA_ADI))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Cicek cicek = Cicek.fromCsvString(line, AYRAC);
                if (cicek != null) {
                    cicekListesi.add(cicek);
                    if (cicek.getId() > enBuyukId) {
                        enBuyukId = cicek.getId();
                    }
                }
            }
            this.sonrakiId = enBuyukId + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tumListeyiDosyayaKaydet() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DOSYA_ADI, false)))) {
            for (Cicek cicek : cicekListesi) {
                out.println(cicek.toCsvString(AYRAC));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Cicek> getCicekListesi() {
        return cicekListesi;
    }
}