package com.example.veicolo;

import com.example.aggiungiMarca.Marca;
import com.example.alimentazioni.Alimentazione;
import com.example.modelli.Modello;
import com.example.tipologieVeicolo.TipologiaVeicolo;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import javax.xml.crypto.Data;

@Entity
@Table(name = "veicoli")
public class Veicolo {

    public static final int TARGA_VEICOLO_MAX_LENGTH = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_modello", nullable = false)
    private Modello nomeModello;

    @Column(name = "targa", nullable = false)
    private String targa;

    @Column(name = "dataUltimaPrenotazione", nullable = false)
    private LocalDate dataUltimaPrenotazione;

    @Column(name = "prenotataPerGiorni", nullable = false)
    private int prenotataPerGiorni;

    @Column(name = "fatturatoDaPrenotazione", nullable = false)
    private float fatturatoDaPrenotazione;

    @Column(name = "dataPrimaDisponibilita", nullable = false)
    private LocalDate dataPrimaDisponibilita;

    @Column(name = "dataScadenzaAssicurazione", nullable = false)
    private LocalDate dataScadenzaAssicurazione;

    @Column(name = "eAssicurato", nullable = false)
    private Boolean eAssicurato;

    protected Veicolo() {}

    public Veicolo(Marca marca, Modello nomeModello, String targa, LocalDate dataUltimaPrenotazione, int prenotataPerGiorni, LocalDate dataScadenzaAssicurazione) {

        this.marca = marca;
        this.nomeModello = nomeModello;
        this.targa = targa;
        this.dataUltimaPrenotazione = dataUltimaPrenotazione;
        this.prenotataPerGiorni = prenotataPerGiorni;
        this.fatturatoDaPrenotazione = nomeModello.getCostoNoleggioGiornaliero()*this.prenotataPerGiorni;
        this.dataPrimaDisponibilita = dataUltimaPrenotazione.plusDays(prenotataPerGiorni);
        this.dataScadenzaAssicurazione = dataScadenzaAssicurazione;

        if (dataScadenzaAssicurazione.isAfter(LocalDate.now()) || dataScadenzaAssicurazione.equals(LocalDate.now())) {
            this.eAssicurato = true;
        } else {
            this.eAssicurato = false;
        }

    }

    // Getters
    public Long getId() {
        return id;
    }

    public Marca getMarca() {
        return marca;
    }

    public Modello getNomeModello() {
        return nomeModello;
    }

    public String getTarga() {
        return targa;
    }

    public LocalDate getDataUltimaPrenotazione() {
        return dataUltimaPrenotazione;
    }

    public int getPrenotataPerGiorni() {
        return prenotataPerGiorni;
    }

    public float getFatturatoDaPrenotazione(){
        return fatturatoDaPrenotazione;
    }

    public LocalDate getDataPrimaDisponibilita() {
        return dataPrimaDisponibilita;
    }

    public LocalDate getDataScadenzaAssicurazione() {
        return dataScadenzaAssicurazione;
    }

    public Boolean geteAssicurato() {
        return eAssicurato;
    }

    // Setters
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public void setNomeModello(Modello nomeModello) {
        this.nomeModello = nomeModello;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public void setDataUltimaPrenotazione(LocalDate dataUltimaPrenotazione) {
        this.dataUltimaPrenotazione = dataUltimaPrenotazione;
    }

    public void setPrenotataPerGiorni(int prenotataPerGiorni) {
        this.prenotataPerGiorni = prenotataPerGiorni;
    }

    public void setDataPrimaDisponibilita(LocalDate dataPrimaDisponibilita) {
        this.dataPrimaDisponibilita = dataPrimaDisponibilita;
    }

    public void setDataScadenzaAssicurazione(LocalDate dataScadenzaAssicurazione) {
        this.dataScadenzaAssicurazione = dataScadenzaAssicurazione;
    }

    public void seteAssicurato(Boolean eAssicurato) {
        this.eAssicurato = eAssicurato;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Veicolo veicolo = (Veicolo) o;
        return getPrenotataPerGiorni() == veicolo.getPrenotataPerGiorni() && Objects.equals(getId(), veicolo.getId()) && Objects.equals(getMarca(), veicolo.getMarca()) && Objects.equals(getNomeModello(), veicolo.getNomeModello()) && Objects.equals(getTarga(), veicolo.getTarga()) && Objects.equals(getDataUltimaPrenotazione(), veicolo.getDataUltimaPrenotazione()) && Objects.equals(getDataPrimaDisponibilita(), veicolo.getDataPrimaDisponibilita()) && Objects.equals(getDataScadenzaAssicurazione(), veicolo.getDataScadenzaAssicurazione()) && Objects.equals(geteAssicurato(), veicolo.geteAssicurato());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMarca(), getNomeModello(), getTarga(), getDataUltimaPrenotazione(), getPrenotataPerGiorni(), getDataPrimaDisponibilita(), getDataScadenzaAssicurazione(), geteAssicurato());
    }
}
