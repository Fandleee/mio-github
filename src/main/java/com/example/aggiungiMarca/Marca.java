package com.example.aggiungiMarca;

import jakarta.persistence.*;

@Entity
@Table(name = "marca")
public class Marca {

    // Costante usata per definire la lunghezza massima del nome marca.
    // È utile perché evita di scrivere numeri "magici" sparsi nel codice.
    //
    // FUTURO CAMBIO:
    // questa costante andrebbe usata anche nella validazione del form
    // e possibilmente allineata alla lunghezza della colonna nel database.
    public static final int NOME_MARCA_MAX_LENGTH = 50;

    // Chiave primaria della tabella.
    // @Id indica il campo identificativo univoco del record.
    // @GeneratedValue dice che il valore viene generato automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    // Campo che contiene il nome della marca, ad esempio "Fiat", "BMW", "Audi".
    // nullable = false significa che nel database questo valore non può essere nullo.
    @Column(name = "marca", nullable = false)
    private String marca = "";

    // Costruttore vuoto protetto richiesto da JPA/Hibernate.
    // Hibernate lo usa per creare l'oggetto quando legge i dati dal database.
    // Non va rimosso anche se sembra "inutile".
    protected Marca() { // To keep Hibernate happy
    }

    // Costruttore comodo da usare quando vogliamo creare una nuova marca nel codice.
    // Invece di assegnare direttamente il valore al campo,
    // passiamo dal setter per mantenere un unico punto di modifica.
    public Marca(String marca){
        setMarca(marca);
    }

    // Restituisce l'id del record.
    // L'id viene valorizzato dal database/Hibernate dopo il salvataggio.
    public Long getId(){
        return this.id;
    }

    // Restituisce il nome della marca.
    public String getMarca(){
        return this.marca;
    }

    // Imposta/modifica il nome della marca.
    //
    // FUTURO CAMBIO IMPORTANTE:
    // qui conviene aggiungere controlli, ad esempio:
    // - se il valore è null
    // - se è vuoto
    // - se supera NOME_MARCA_MAX_LENGTH
    // - se va fatto trim() per eliminare spazi iniziali/finali
    public void setMarca(String marca){
        this.marca = marca;
    }

    @Override
    public boolean equals(Object obj){

        // Se l'oggetto passato è null oppure non è compatibile con questa classe,
        // i due oggetti non possono essere considerati uguali.
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }

        // Se stiamo confrontando lo stesso oggetto in memoria,
        // allora è sicuramente uguale.
        if (obj == this) {
            return true;
        }

        // Cast dell'oggetto per poter accedere ai suoi campi/metodi.
        Marca other = (Marca) obj;

        // Qui stai dicendo:
        // "due oggetti Marca sono uguali se il testo del campo marca è uguale".
        //
        // ESEMPIO:
        // new Marca("Fiat") è uguale a new Marca("Fiat")
        //
        // ATTENZIONE:
        // in JPA/Hibernate equals() va progettato con attenzione.
        // Se usi un campo modificabile o non univoco, puoi avere confronti ambigui.
        return getMarca() != null && getMarca().equals(other.getMarca());
    }
}