package com.example.modelli;

import com.example.aggiungiMarca.Marca;
import com.example.alimentazioni.Alimentazione;
import com.example.tipologieVeicolo.TipologiaVeicolo;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "modelli")
public class Modello {

    public static final int NOME_MODELLO_MAX_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @Column(name = "nome", nullable = false)
    private String nomeModello;

    @ManyToOne
    @JoinColumn(name = "id_tipologia_veicolo", nullable = false)
    private TipologiaVeicolo tipologiaVeicolo;

    @Column(name = "numero_cilindri", nullable = false)
    private int numeroCilindri;

    @Column(name = "cilindrata", nullable = false)
    private int cilindrata;

    @ManyToOne
    @JoinColumn(name = "id_alimentazione", nullable = false)
    private Alimentazione alimentazione;

    @Column(name = "numero_passeggeri", nullable = false)
    private int numeroPasseggeri;

    @Column(name = "costo_noleggio_giornaliero", nullable = false)
    private int costoNoleggioGiornaliero;

    @Column(name = "quantita", nullable = false)
    private int quantita;

    protected Modello() {}

    public Modello(
            Marca marca,
            String nomeModello,
            TipologiaVeicolo tipologiaVeicolo,
            int numeroCilindri,
            int cilindrata,
            Alimentazione alimentazione,
            int numeroPasseggeri,
            int costoNoleggioGiornaliero,
            int quantita
    ) {
        setMarca(marca);
        setNomeModello(nomeModello);
        setTipologiaVeicolo(tipologiaVeicolo);
        setNumeroCilindri(numeroCilindri);
        setCilindrata(cilindrata);
        setAlimentazione(alimentazione);
        setNumeroPasseggeri(numeroPasseggeri);
        setCostoNoleggioGiornaliero(costoNoleggioGiornaliero);
        setQuantita(quantita);
    }

    // Getters
    public Long getId() { return this.id; }
    public Marca getMarca() { return this.marca; }
    public String getNomeModello() { return this.nomeModello; }
    public TipologiaVeicolo getTipologiaVeicolo() { return this.tipologiaVeicolo; }
    public int getNumeroCilindri() { return this.numeroCilindri; }
    public int getCilindrata() { return this.cilindrata; }
    public Alimentazione getAlimentazione() { return this.alimentazione; }
    public int getNumeroPasseggeri() { return this.numeroPasseggeri; }
    public int getCostoNoleggioGiornaliero() { return this.costoNoleggioGiornaliero; }
    public int getQuantita() { return this.quantita; }

    // Setters
    public void setMarca(Marca marca) { this.marca = marca; }
    public void setNomeModello(String nomeModello) { this.nomeModello = nomeModello; }
    public void setTipologiaVeicolo(TipologiaVeicolo tipologiaVeicolo) { this.tipologiaVeicolo = tipologiaVeicolo; }
    public void setNumeroCilindri(int numeroCilindri) { this.numeroCilindri = numeroCilindri; }
    public void setCilindrata(int cilindrata) { this.cilindrata = cilindrata; }
    public void setAlimentazione(Alimentazione alimentazione) { this.alimentazione = alimentazione; }
    public void setNumeroPasseggeri(int numeroPasseggeri) { this.numeroPasseggeri = numeroPasseggeri; }
    public void setCostoNoleggioGiornaliero(int costoNoleggioGiornaliero) { this.costoNoleggioGiornaliero = costoNoleggioGiornaliero; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Modello other)) return false;

        return numeroCilindri == other.numeroCilindri &&
                cilindrata == other.cilindrata &&
                numeroPasseggeri == other.numeroPasseggeri &&
                costoNoleggioGiornaliero == other.costoNoleggioGiornaliero &&
                quantita == other.quantita &&
                Objects.equals(id, other.id) &&
                Objects.equals(nomeModello, other.nomeModello) &&
                Objects.equals(marca, other.marca) &&
                Objects.equals(tipologiaVeicolo, other.tipologiaVeicolo) &&
                Objects.equals(alimentazione, other.alimentazione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marca, nomeModello, tipologiaVeicolo,
                numeroCilindri, cilindrata, alimentazione,
                numeroPasseggeri, costoNoleggioGiornaliero, quantita);
    }
}