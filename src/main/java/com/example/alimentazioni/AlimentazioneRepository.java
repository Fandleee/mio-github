package com.example.alimentazioni;

import com.example.aggiungiMarca.Marca;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlimentazioneRepository extends JpaRepository<Alimentazione, Long>, JpaSpecificationExecutor<Alimentazione> {
    Slice<Alimentazione> findAllBy(Pageable pageable);
}
