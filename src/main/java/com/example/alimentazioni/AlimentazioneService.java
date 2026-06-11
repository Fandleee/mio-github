package com.example.alimentazioni;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlimentazioneService {

    private final AlimentazioneRepository alimentazioneRepository;

    AlimentazioneService(AlimentazioneRepository alimentazioneRepository) {
        this.alimentazioneRepository = alimentazioneRepository;
    }

    @Transactional
    public void createAlimentazione(String nomeAlimentazione) {
        var alimentazione = new Alimentazione(nomeAlimentazione);
        alimentazioneRepository.saveAndFlush(alimentazione);
    }

    @Transactional(readOnly = true)
    public List<Alimentazione> list(Pageable pageable) {
        return alimentazioneRepository.findAllBy(pageable).toList();
    }
}