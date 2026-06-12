package com.example.tipologieVeicolo;

import jakarta.persistence.*;

@Entity
@Table(name = "tipologie_veicolo")
public class TipologiaVeicolo {

    public static final int NOME_MAX_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "tipologia", nullable = false)
    private String tipologia = "";

    protected TipologiaVeicolo() { // To keep Hibernate happy
    }

    public TipologiaVeicolo(String tipologia) {
        setTipologia(tipologia);
    }

    public Long getId() {
        return this.id;
    }

    public String getTipologia() {
        return this.tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        TipologiaVeicolo other = (TipologiaVeicolo) obj;
        return getTipologia() != null && getTipologia().equals(other.getTipologia());
    }
}
