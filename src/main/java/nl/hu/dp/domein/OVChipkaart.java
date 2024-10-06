package nl.hu.dp.domein;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ov_chipkaart")
public class OVChipkaart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kaart_nummer", nullable = false)
    private int kaartNummer;

    @Column(name = "geldig_tot", nullable = false)
    private Date geldigTot;

    @Column(name = "klasse", nullable = false)
    private int klasse;

    @Column(name = "saldo", nullable = false)
    private double saldo;

    @ManyToOne
    @JoinColumn(name = "reiziger_id", nullable = false)
    private Reiziger reiziger;

    @ManyToMany
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "kaart_nummer"),
            inverseJoinColumns = @JoinColumn(name = "product_nummer")
    )  private List<Product> producten = new ArrayList<>();


    public OVChipkaart() {
    }

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }


    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public void voegProductToe(Product product) {
        if (!producten.contains(product)) {
            producten.add(product);
            product.voegChipkaartToe(this);
        }
    }

    public void verwijderProduct(Product product) {
        if (producten.contains(product)) {
            producten.remove(product);
            product.verwijderChipkaart(this);
        }
    }

    @Override
    public String toString() {
        String reizigerInfo = (reiziger != null) ? reiziger.getId() + " - " + reiziger.getAchternaam() : "Geen reiziger";
        return String.format("OVChipkaart{kaart_nummer=%d, geldig_tot=%s, klasse=%d, saldo=%.2f, reiziger=%s}",
                kaartNummer, geldigTot.toString(), klasse, saldo, reizigerInfo);
    }
}