package com.example.alimentazioni;

import jakarta.persistence.*;

@Entity
@Table(name = "alimentazioni")

public class Alimentazione {

    public static final int NOME_MAX_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "alimentazione", nullable = false)
    private String alimentazione = "";

    protected Alimentazione() { // To keep Hibernate happy
    }

    public Alimentazione(String alimentazione){
        setAlimentazione(alimentazione);
    }

    public String getAlimentazione(){
        return this.alimentazione;
    }

    public void setAlimentazione(String alimentazione){
        this.alimentazione = alimentazione;
    }

    @Override
    public boolean equals(Object obj){

        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Alimentazione other = (Alimentazione) obj;
        return getAlimentazione() != null && getAlimentazione().equals(other.getAlimentazione());
    }
}
