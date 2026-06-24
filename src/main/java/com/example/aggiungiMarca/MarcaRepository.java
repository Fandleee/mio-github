package com.example.aggiungiMarca;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MarcaRepository extends JpaRepository<Marca, Long>, JpaSpecificationExecutor<Marca> {

    // Metodo per leggere le marche in modo paginato.
    // Pageable contiene informazioni come:
    // - numero pagina
    // - quantità di record per pagina
    // - ordinamento
    //
    // Slice è utile quando vuoi ottenere "un blocco" di risultati
    // senza necessariamente conoscere il numero totale di record.
    //
    // FUTURO CAMBIO:
    // se ti servirà anche il conteggio totale delle marche,
    // potresti valutare Page invece di Slice.
    Slice<Marca> findAllBy(Pageable pageable);
}