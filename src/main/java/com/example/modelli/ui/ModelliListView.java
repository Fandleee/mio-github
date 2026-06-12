package com.example.modelli.ui;

import com.example.alimentazioni.*;
import com.example.base.ui.ViewTitle;
import com.example.modelli.ModelloService;
import com.example.tipologieVeicolo.*;
import com.example.aggiungiMarca.*;
import com.example.aggiungiMarca.Marca;
import com.example.alimentazioni.Alimentazione;
import com.example.modelli.Modello;
import com.example.tipologieVeicolo.TipologiaVeicolo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route(value = "modelli")
@PageTitle("Modelli")
@Menu(order = 1, icon = "", title = "Modelli")

public class ModelliListView extends VerticalLayout {

    private final ModelloService modelloService;

    final TextField nomeModello;

    final IntegerField cilindrata;
    final IntegerField numeroPasseggeri;
    final IntegerField costoGiornaliero;
    final IntegerField quantita;

    final Dialog formDialog;
    final Button createBtn;
    final Grid<Modello> modelloGrid;

    final Select<Alimentazione> alimentazioneSelect;
    final Select<TipologiaVeicolo> tipologiaVeicoloSelect;
    final Select<Marca> marcaSelect;
    final Select<Integer> numeroCilindri;

    ModelliListView(ModelloService modelloService, AlimentazioneService alimentazioneService, TipologiaVeicoloService tipologiaVeicoloService, MarcaService marcaService){

        this.modelloService = modelloService;

        marcaSelect = new Select<>();
        nomeModello = new TextField();
        tipologiaVeicoloSelect = new Select<>();
        numeroCilindri = new Select<>();
        cilindrata = new IntegerField();
        alimentazioneSelect = new Select<>();
        numeroPasseggeri = new IntegerField();
        costoGiornaliero = new IntegerField();
        quantita = new IntegerField();

        createBtn = new Button("Aggiungi", event -> createModello());
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        modelloGrid = new Grid<>();

        formDialog = createFormDialog();

        // Marca Select
        marcaSelect.setLabel("Marca");
        marcaSelect.setPlaceholder("Seleziona marca");
        List<Marca> marche = marcaService.list(Pageable.unpaged());
        if (marche.isEmpty()) {
            marcaSelect.setEnabled(false);
            marcaSelect.setPlaceholder("Nessuna marca disponibile");
        } else {
            marcaSelect.setItems(marche);
            marcaSelect.setItemLabelGenerator(Marca::getMarca);
        }

        // Nome Modello
        nomeModello.setPlaceholder("Serie 3");
        nomeModello.setLabel("Nome modello");
        nomeModello.setAriaLabel("Nome modello");
        nomeModello.setMaxLength(Modello.NOME_MODELLO_MAX_LENGTH);
        nomeModello.setMinWidth("10em");

        // Tipologia Veicolo Select
        tipologiaVeicoloSelect.setLabel("Tipologia veicolo");
        tipologiaVeicoloSelect.setPlaceholder("Seleziona tipologia");
        List<TipologiaVeicolo> tipologia = tipologiaVeicoloService.list(Pageable.unpaged());
        if (tipologia.isEmpty()) {
            tipologiaVeicoloSelect.setEnabled(false);
            tipologiaVeicoloSelect.setPlaceholder("Nessuna alimentazione disponibile");
        } else {
            tipologiaVeicoloSelect.setItems(tipologia);
            tipologiaVeicoloSelect.setItemLabelGenerator(TipologiaVeicolo::getTipologia);
        }

        // Numero Cilindri
        numeroCilindri.setLabel("Numero cilindri");
        numeroCilindri.setItems(0, 1, 2, 3, 4, 5, 6, 8, 10, 12);

        // Cilindrata
        cilindrata.setPlaceholder("Es. 1998");
        cilindrata.setLabel("Cilindrata");
        cilindrata.setAriaLabel("Cilindrata");
        cilindrata.setMin(0);
        cilindrata.setMinWidth("10em");

        // Alimentazione Select
        alimentazioneSelect.setLabel("Alimentazione");
        alimentazioneSelect.setPlaceholder("Seleziona alimentazione");
        List<Alimentazione> alimentazioni = alimentazioneService.list(Pageable.unpaged());
        if (alimentazioni.isEmpty()) {
            alimentazioneSelect.setEnabled(false);
            alimentazioneSelect.setPlaceholder("Nessuna alimentazione disponibile");
        } else {
            alimentazioneSelect.setItems(alimentazioni);
            alimentazioneSelect.setItemLabelGenerator(Alimentazione::getAlimentazione);
        }

        // Numero Passeggeri
        numeroPasseggeri.setPlaceholder("Es. 5");
        numeroPasseggeri.setLabel("Numero passeggeri");
        numeroPasseggeri.setAriaLabel("Numero passeggeri");
        numeroPasseggeri.setMin(1);
        numeroPasseggeri.setMax(9);
        numeroPasseggeri.setMinWidth("10em");

        // Costo Giornaliero
        costoGiornaliero.setPlaceholder("Es. 80");
        costoGiornaliero.setLabel("Costo noleggio giornaliero");
        costoGiornaliero.setAriaLabel("Costo noleggio giornaliero");
        costoGiornaliero.setMin(0);
        costoGiornaliero.setMinWidth("10em");

        // Quantita
        quantita.setPlaceholder("Es. 3");
        quantita.setLabel("Quantità");
        quantita.setAriaLabel("Quantità");
        quantita.setMin(0);
        quantita.setMinWidth("10em");


        var openDialogBtn = new Button("+", e -> formDialog.open());
        openDialogBtn.setClassName("form-btn");
        openDialogBtn.setHeightFull();

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setClassName("form-standard-style");
        toolbar.add(new ViewTitle("Lista modelli"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.setClassName("outer-wrapper-shadow");
        outerWrapper.add(toolbar, openDialogBtn);


        // Visualizzazione record
        modelloGrid.setItems(query -> modelloService.list(toSpringPageRequest(query)).stream());
        modelloGrid.addColumn(modello -> modello.getMarca().getMarca()).setHeader("Marca");
        modelloGrid.addColumn(Modello::getNomeModello).setHeader("Modello");
        modelloGrid.addColumn(Modello::getCilindrata).setHeader("Cilindrata");
        modelloGrid.addColumn(Modello::getNumeroCilindri).setHeader("Numero cilindri");
        modelloGrid.addColumn(Modello::getNumeroPasseggeri).setHeader("Numero passeggeri");
        modelloGrid.addColumn(Modello::getCostoNoleggioGiornaliero).setHeader("Costo giornaliero in euro");
        modelloGrid.addColumn(Modello::getQuantita).setHeader("Quantita");

        modelloGrid.addComponentColumn(modello -> {
            Button elimina = new Button("Elimina", click -> deleteModello(modello.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return elimina;
        }).setHeader("Azioni");

        modelloGrid.setEmptyStateText("Non ci sono marchi registrati");

        modelloGrid.setClassName("grid-style");
        modelloGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.NO_ROW_BORDERS
        );

        modelloGrid.setSizeFull();

        setSizeFull();

        add(outerWrapper, modelloGrid);
    }

    private void createModello() {

        if (nomeModello.getValue().isBlank()) {
            nomeModello.setInvalid(true);
            nomeModello.setErrorMessage("Nome richiesto");
            return;
        }
        if (marcaSelect.getValue() == null) {
            marcaSelect.setInvalid(true);
            marcaSelect.setErrorMessage("Marca richiesta");
            return;
        }
        if (tipologiaVeicoloSelect.getValue() == null) {
            tipologiaVeicoloSelect.setInvalid(true);
            tipologiaVeicoloSelect.setErrorMessage("Tipologia richiesta");
            return;
        }
        if (alimentazioneSelect.getValue() == null) {
            alimentazioneSelect.setInvalid(true);
            alimentazioneSelect.setErrorMessage("Alimentazione richiesta");
            return;
        }
        if (numeroCilindri.getValue() == null) {
            numeroCilindri.setInvalid(true);
            numeroCilindri.setErrorMessage("Numero cilindri richiesto");
            return;
        }
        if (cilindrata.getValue() == null) {
            cilindrata.setInvalid(true);
            cilindrata.setErrorMessage("Cilindrata richiesta");
            return;
        }
        if (numeroPasseggeri.getValue() == null) {
            numeroPasseggeri.setInvalid(true);
            numeroPasseggeri.setErrorMessage("Numero passeggeri richiesto");
            return;
        }
        if (costoGiornaliero.getValue() == null) {
            costoGiornaliero.setInvalid(true);
            costoGiornaliero.setErrorMessage("Costo giornaliero richiesto");
            return;
        }
        if (quantita.getValue() == null) {
            quantita.setInvalid(true);
            quantita.setErrorMessage("Quantità richiesta");
            return;
        }

        String nome = nomeModello.getValue();
        modelloService.createModello(
                marcaSelect.getValue(),
                nomeModello.getValue(),
                tipologiaVeicoloSelect.getValue(),
                numeroCilindri.getValue(),
                cilindrata.getValue(),
                alimentazioneSelect.getValue(),
                numeroPasseggeri.getValue(),
                costoGiornaliero.getValue(),
                quantita.getValue()
        );

        modelloGrid.getDataProvider().refreshAll();

        nomeModello.clear();
        marcaSelect.setValue(null);
        tipologiaVeicoloSelect.setValue(null);
        alimentazioneSelect.setValue(null);
        numeroCilindri.setValue(null);
        cilindrata.clear();
        numeroPasseggeri.clear();
        costoGiornaliero.clear();
        quantita.clear();
        formDialog.close();
        Notification.show(nome + " aggiunto!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.SUCCESS);
    }

    private void deleteModello(Long id) {
        modelloService.deleteModello(id);
        modelloGrid.getDataProvider().refreshAll();
        Notification.show("Modello eliminato!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {

        var dialog = new Dialog();

        dialog.setHeaderTitle("Aggiungi modello");
        dialog.setMaxWidth("700px");

        var form = new FormLayout(marcaSelect, nomeModello, tipologiaVeicoloSelect, numeroCilindri, alimentazioneSelect, cilindrata, numeroPasseggeri, costoGiornaliero, quantita);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 6));
        form.setColspan(marcaSelect ,3);
        form.setColspan(nomeModello ,3);
        form.setColspan(tipologiaVeicoloSelect ,3);
        form.setColspan(numeroCilindri ,3);
        form.setColspan(alimentazioneSelect ,3);
        form.setColspan(cilindrata ,3);
        form.setColspan(numeroPasseggeri ,2);
        form.setColspan(costoGiornaliero ,2);
        form.setColspan(quantita ,2);

        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());

        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }
}
