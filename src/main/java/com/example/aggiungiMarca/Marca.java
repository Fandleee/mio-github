package com.example.aggiungiMarca;

import jakarta.persistence.*;

@Entity
@Table(name = "marca")

public class Marca {

    public static final int NOME_MARCA_MAX_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "marca", nullable = false)
    private String marca = "";

    protected Marca() { // To keep Hibernate happy
    }

    public Marca(String marca){
        setMarca(marca);
    }

    public String getMarca(){
        return this.marca;
    }

    public void setMarca(String marca){
        this.marca = marca;
    }

    @Override
    public boolean equals(Object obj){

        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Marca other = (Marca) obj;
        return getMarca() != null && getMarca().equals(other.getMarca());
    }
}
