# Gestione Noleggio Veicoli

Applicazione web per la gestione di un parco veicoli a noleggio. Permette di catalogare marche, alimentazioni, tipologie, modelli e singoli veicoli con le relative prenotazioni e stato assicurativo.
 
---

## Stack tecnologico

| Layer | Tecnologia |
|---|---|
| UI | [Vaadin Flow 24](https://vaadin.com/docs/latest/flow) |
| Backend | Spring Boot 3 |
| Persistenza | Spring Data JPA / Hibernate |
| Database | Configurabile (default: H2 embedded in dev) |
| Test UI | Vaadin Browserless Test |
 
---

## Struttura del progetto

```
src/main/java/com/example/
├── Application.java              # Entry point Spring Boot
├── base/ui/                      # Componenti condivisi (MainLayout, ViewTitle)
├── aggiungiMarca/                # Entità Marca + Service + Repository + UI
├── alimentazioni/                # Entità Alimentazione + Service + Repository + UI
├── tipologieVeicolo/             # Entità TipologiaVeicolo + Service + Repository + UI
├── modelli/                      # Entità Modello + Service + Repository + UI
└── veicolo/                      # Entità Veicolo + Service + Repository + UI
```

Ogni modulo segue lo stesso pattern a tre livelli: `Repository → Service → View`.
 
---

## Modello dati

```
Marca ──────────────────────────────────────┐
                                            ▼
Alimentazione ──── Modello ──── (1:N) ──── Veicolo
                     ▲
TipologiaVeicolo ───┘
```

Il costruttore di `Veicolo` calcola automaticamente:
- `fatturatoDaPrenotazione` = `costoNoleggioGiornaliero × prenotataPerGiorni`
- `dataPrimaDisponibilita` = `dataUltimaPrenotazione + prenotataPerGiorni` (giorni)
- `eAssicurato` = `true` se `dataScadenzaAssicurazione >= oggi`
---

## Viste disponibili

| Route | Vista | Ordine menu |
|---|---|---|
| `/` | Veicoli | 0 |
| `/modelli` | Modelli | 1 |
| `/alimentazioni` | Alimentazioni | 2 |
| `/tipologie-veicolo` | Tipologie Veicolo | 3 |
| `/marche` | Marche | 4 |
 
---

## Avvio in sviluppo

**Prerequisiti:** Java 17+, Maven 3.8+

```bash
./mvnw spring-boot:run
```

L'applicazione parte su `http://localhost:8080`. Il browser viene aperto automaticamente (configurato in `application.properties`).

> **Nota:** `spring.jpa.hibernate.ddl-auto=update` crea/aggiorna lo schema automaticamente. **Non usare in produzione.** Per ambienti stabili, integrare [Flyway](https://vaadin.com/docs/latest/building-apps/forms-data/add-flyway).
 
---

## Eseguire i test

```bash
./mvnw test
```

I test UI usano `SpringBrowserlessTest` di Vaadin, che esegue i componenti server-side senza un browser reale. Documentazione: [Vaadin Browserless Testing](https://vaadin.com/docs/latest/flow/testing/browserless).
 
---

## Configurazione

Tutte le proprietà si trovano in `src/main/resources/application.properties`.

| Proprietà | Default | Descrizione |
|---|---|---|
| `server.port` | `8080` | Porta HTTP (sovrascrivibile con env `PORT`) |
| `vaadin.launch-browser` | `true` | Apre il browser all'avvio in dev |
| `spring.jpa.hibernate.ddl-auto` | `update` | Strategia DDL Hibernate |
 
---