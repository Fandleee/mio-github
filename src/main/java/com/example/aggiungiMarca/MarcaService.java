package com.example.aggiungiMarca;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarcaService {

    // Repository usato per eseguire le operazioni sul database.
    // Essendo final, viene assegnato una sola volta nel costruttore.
    private final MarcaRepository marcaRepository;

    // Costruttore con dependency injection.
    // Spring fornisce automaticamente il repository quando crea il service.
    MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Transactional
    public void createMarca(String nomeMarca) {

        // Creiamo una nuova entità Marca con il nome ricevuto dal form.
        var marca = new Marca(nomeMarca);

        // Salviamo subito nel database e forziamo la sincronizzazione immediata.
        // saveAndFlush() è utile quando vuoi essere certo che il dato venga scritto subito.
        //
        // FUTURO CAMBIO:
        // se non hai bisogno del flush immediato, spesso basta save(marca).
        marcaRepository.saveAndFlush(marca);
    }

    @Transactional(readOnly = true)
    public List<Marca> list(Pageable pageable) {

        // Metodo di sola lettura:
        // recupera una porzione di marche dal database in base alla paginazione.
        //
        // readOnly = true segnala che qui non stiamo modificando dati,
        // ed è una buona pratica per le query di sola lettura.
        return marcaRepository.findAllBy(pageable).toList();
    }

    @Transactional
    public void deleteMarca(Long id){

        // Elimina una marca in base al suo id.
        //
        // ATTENZIONE:
        // se l'id non esiste, oppure se la marca è collegata ad altri record
        // (es. modelli o veicoli), potresti avere errori o vincoli del database.
        //
        // FUTURO CAMBIO:
        // aggiungere controlli prima della cancellazione
        // o messaggi più chiari per l'utente.
        marcaRepository.deleteById(id);
    }
}