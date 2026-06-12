package com.example.tipologieVeicolo;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipologiaVeicoloService {

    private final TipologiaVeicoloRepository tipologiaVeicoloRepository;

    TipologiaVeicoloService(TipologiaVeicoloRepository tipologiaVeicoloRepository) {
        this.tipologiaVeicoloRepository = tipologiaVeicoloRepository;
    }

    @Transactional
    public void createTipologia(String nomeTipologia) {
        var tipologia = new TipologiaVeicolo(nomeTipologia);
        tipologiaVeicoloRepository.saveAndFlush(tipologia);
    }

    @Transactional(readOnly = true)
    public List<TipologiaVeicolo> list(Pageable pageable) {
        return tipologiaVeicoloRepository.findAllBy(pageable).toList();
    }

    @Transactional
    public void deleteTipologia(Long id){
        tipologiaVeicoloRepository.deleteById(id);
    }
}
