package com.example.modelli;

import com.example.modelli.Modello;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModelloRepository extends JpaRepository<Modello, Long>, JpaSpecificationExecutor<Modello> {
    Slice<Modello> findAllBy(Pageable pageable);
}
