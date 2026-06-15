package com.example.modelli;


import com.example.aggiungiMarca.Marca;
import com.example.alimentazioni.Alimentazione;
import com.example.tipologieVeicolo.TipologiaVeicolo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModelloService {

    private final ModelloRepository modelloRepository;

    ModelloService(ModelloRepository modelloRepository) {
        this.modelloRepository = modelloRepository;
    }

    @Transactional
    public void createModello(
            Marca marca,
            String nomeModello,
            TipologiaVeicolo tipologiaVeicolo,
            int numeroCilindri,
            int cilindrata,
            Alimentazione alimentazione,
            int numeroPasseggeri,
            int costoNoleggioGiornaliero,
            int quantita) {
        var modello = new Modello(marca, nomeModello, tipologiaVeicolo, numeroCilindri, cilindrata, alimentazione, numeroPasseggeri, costoNoleggioGiornaliero, quantita);
        modelloRepository.saveAndFlush(modello);
    }

    @Transactional(readOnly = true)
    public List<Modello> list(Pageable pageable) {
        return modelloRepository.findAllBy(pageable).toList();
    }

    @Transactional
    public void deleteModello(Long id){
        modelloRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Modello> listByMarca(Marca marca) {
        return modelloRepository.findByMarca(marca);
    }
}
