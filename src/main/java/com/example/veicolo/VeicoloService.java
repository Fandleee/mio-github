package com.example.veicolo;

import com.example.aggiungiMarca.Marca;
import com.example.modelli.Modello;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class VeicoloService {

    private final VeicoloRepository veicoloRepository;

    VeicoloService(VeicoloRepository veicoloRepository) {
        this.veicoloRepository = veicoloRepository;
    }

    @Transactional
    public void createVeicolo(
            Marca marca,
            Modello nomeModello,
            String targa,
            LocalDate dataUltimaPrenotazione,
            int prenotataPerGiorni,
            LocalDate dataScadenzaAssicurazione
    ){
        var veicolo = new Veicolo(marca, nomeModello, targa, dataUltimaPrenotazione, prenotataPerGiorni, dataScadenzaAssicurazione);
        veicoloRepository.saveAndFlush(veicolo);
    }

    @Transactional(readOnly = true)
    public List<Veicolo> list(Pageable pageable) { return veicoloRepository.findAllBy(pageable).toList(); }

    @Transactional
    public void updateVeicolo(Long id, Marca marca, Modello nomeModello, String targa, LocalDate dataUltimaPrenotazione, int prenotataPerGiorni, LocalDate dataScadenzaAssicurazione) {
        var veicolo = veicoloRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Veicolo non trovato: " + targa));
        veicolo.aggiornaVeicolo(marca, nomeModello, targa, dataUltimaPrenotazione, prenotataPerGiorni, dataScadenzaAssicurazione);
        veicoloRepository.saveAndFlush(veicolo);
    }

    @Transactional
    public void deleteVeicolo(Long id){
        veicoloRepository.deleteById(id);
    }
}
