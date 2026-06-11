package com.example.veicolo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VeicoloRepository extends JpaRepository<Veicolo, Long>, JpaSpecificationExecutor<Veicolo> {
    Slice<Veicolo> findAllBy(Pageable pageable);
}
