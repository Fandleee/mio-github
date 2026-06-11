package com.example.tipologieVeicolo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TipologiaVeicoloRepository extends JpaRepository<TipologiaVeicolo, Long>, JpaSpecificationExecutor<TipologiaVeicolo> {
    Slice<TipologiaVeicolo> findAllBy(Pageable pageable);
}
