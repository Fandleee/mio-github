package com.example.aggiungiMarca;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Transactional
    public void createMarca(String nomeMarca) {
        var marca = new Marca(nomeMarca);
        marcaRepository.saveAndFlush(marca);
    }

    @Transactional(readOnly = true)
    public List<Marca> list(Pageable pageable) {
        return marcaRepository.findAllBy(pageable).toList();
    }

}
