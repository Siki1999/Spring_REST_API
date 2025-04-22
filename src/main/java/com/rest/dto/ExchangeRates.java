package com.rest.dto;

/**
 * Represents currency exchange rates data structure
 */
public class ExchangeRates {

    /** Exchange rate bulletin number */
    private String broj_tecajnice;
    
    /** Effective date of exchange rates */
    private String datum_primjene;
    
    /** Country name */
    private String drzava;
    
    /** ISO country code */
    private String drzava_iso;
    
    /** Buying exchange rate */
    private String kupovni_tecaj;
    
    /** Selling exchange rate */
    private String prodajni_tecaj;
    
    /** Currency code */
    private String sifra_valute;
    
    /** Average exchange rate */
    private String srednji_tecaj;
    
    /** Currency name */
    private String valuta;

    /** Constructor
     * @param broj_tecajnice Exchange rate bulletin number
     * @param datum_primjene Effective date of exchange rates
     * @param drzava Country name
     * @param drzava_iso ISO country code
     * @param kupovni_tecaj Buying exchange rate
     * @param prodajni_tecaj Selling exchange rate
     * @param sifra_valute Currency code
     * @param srednji_tecaj Average exchange rate
     * @param valuta Currency name
     */
    public ExchangeRates(String broj_tecajnice, String datum_primjene, String drzava, String drzava_iso, String kupovni_tecaj, String prodajni_tecaj, String sifra_valute, String srednji_tecaj, String valuta) {
        this.broj_tecajnice = broj_tecajnice;
        this.datum_primjene = datum_primjene;
        this.drzava = drzava;
        this.drzava_iso = drzava_iso;
        this.kupovni_tecaj = kupovni_tecaj;
        this.prodajni_tecaj = prodajni_tecaj;
        this.sifra_valute = sifra_valute;
        this.srednji_tecaj = srednji_tecaj;
        this.valuta = valuta;
    }

    /**
     * @return Exchange rate bulletin number
     */
    public String getBroj_tecajnice() {
        return broj_tecajnice;
    }

    /**
     * @param broj_tecajnice New bulletin number
     */
    public void setBroj_tecajnice(String broj_tecajnice) {
        this.broj_tecajnice = broj_tecajnice;
    }

    /**
     * @return Effective date of exchange rates
     */
    public String getDatum_primjene() {
        return datum_primjene;
    }

    /**
     * @param datum_primjene New effective date
     */
    public void setDatum_primjene(String datum_primjene) {
        this.datum_primjene = datum_primjene;
    }

    /**
     * @return Country name
     */
    public String getDrzava() {
        return drzava;
    }

    /**
     * @param drzava New country name
     */
    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    /**
     * @return ISO country code
     */
    public String getDrzava_iso() {
        return drzava_iso;
    }

    /**
     * @param drzava_iso New ISO country code
     */
    public void setDrzava_iso(String drzava_iso) {
        this.drzava_iso = drzava_iso;
    }

    /**
     * @return Buying exchange rate
     */
    public String getKupovni_tecaj() {
        return kupovni_tecaj;
    }

    /**
     * @param kupovni_tecaj New buying exchange rate
     */
    public void setKupovni_tecaj(String kupovni_tecaj) {
        this.kupovni_tecaj = kupovni_tecaj;
    }

    /**
     * @return Selling exchange rate
     */
    public String getProdajni_tecaj() {
        return prodajni_tecaj;
    }

    /**
     * @param prodajni_tecaj New selling exchange rate
     */
    public void setProdajni_tecaj(String prodajni_tecaj) {
        this.prodajni_tecaj = prodajni_tecaj;
    }

    /**
     * @return Currency code
     */
    public String getSifra_valute() {
        return sifra_valute;
    }

    /**
     * @param sifra_valute New currency code
     */
    public void setSifra_valute(String sifra_valute) {
        this.sifra_valute = sifra_valute;
    }

    /**
     * @return Average exchange rate
     */
    public String getSrednji_tecaj() {
        return srednji_tecaj;
    }

    /**
     * @param srednji_tecaj New average exchange rate
     */
    public void setSrednji_tecaj(String srednji_tecaj) {
        this.srednji_tecaj = srednji_tecaj;
    }

    /**
     * @return Currency name
     */
    public String getValuta() {
        return valuta;
    }

    /**
     * @param valuta New currency name
     */
    public void setValuta(String valuta) {
        this.valuta = valuta;
    }
}
