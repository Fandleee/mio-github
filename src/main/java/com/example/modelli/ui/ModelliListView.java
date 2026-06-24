package com.example.modelli.ui;

import com.example.aggiungiMarca.Marca;
import com.example.aggiungiMarca.MarcaService;
import com.example.alimentazioni.Alimentazione;
import com.example.alimentazioni.AlimentazioneService;
import com.example.base.ui.ViewTitle;
import com.example.modelli.Modello;
import com.example.modelli.ModelloService;
import com.example.tipologieVeicolo.TipologiaVeicolo;
import com.example.tipologieVeicolo.TipologiaVeicoloService;
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
@Menu(order = 4, icon = "icons/modelli.svg", title = "Modelli")
public class ModelliListView extends VerticalLayout {

    private final ModelloService modelloService;

    private final TextField nomeModello;
    private final IntegerField cilindrata;
    private final IntegerField numeroPasseggeri;
    private final IntegerField costoGiornaliero;
    private final IntegerField quantita;

    private final Dialog formDialog;
    private final Button createBtn;
    private final Grid<Modello> modelloGrid;

    private final Select<Alimentazione> alimentazioneSelect;
    private final Select<TipologiaVeicolo> tipologiaVeicoloSelect;
    private final Select<Marca> marcaSelect;
    private final Select<Integer> numeroCilindri;

    ModelliListView(
            ModelloService modelloService,
            AlimentazioneService alimentazioneService,
            TipologiaVeicoloService tipologiaVeicoloService,
            MarcaService marcaService
    ) {
        this.modelloService = modelloService;

        addClassName("modelli-view");

        marcaSelect = new Select<>();
        marcaSelect.addClassNames("modelli-field", "modelli-select-full");

        nomeModello = new TextField();
        nomeModello.addClassNames("modelli-field", "modelli-text-full");

        tipologiaVeicoloSelect = new Select<>();
        tipologiaVeicoloSelect.addClassNames("modelli-field", "modelli-select-full");

        numeroCilindri = new Select<>();
        numeroCilindri.addClassNames("modelli-field", "modelli-select-full");

        cilindrata = new IntegerField();
        cilindrata.addClassNames("modelli-field", "modelli-integer-full");

        alimentazioneSelect = new Select<>();
        alimentazioneSelect.addClassNames("modelli-field", "modelli-select-full");

        numeroPasseggeri = new IntegerField();
        numeroPasseggeri.addClassNames("modelli-field", "modelli-integer-full");

        costoGiornaliero = new IntegerField();
        costoGiornaliero.addClassNames("modelli-field", "modelli-integer-full");

        quantita = new IntegerField();
        quantita.addClassNames("modelli-field", "modelli-integer-full");

        createBtn = new Button("Aggiungi", event -> createModello());
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        modelloGrid = new Grid<>();
        formDialog = createFormDialog();

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

        nomeModello.setPlaceholder("Serie 3");
        nomeModello.setLabel("Nome modello");
        nomeModello.setAriaLabel("Nome modello");
        nomeModello.setMaxLength(Modello.NOME_MODELLO_MAX_LENGTH);

        tipologiaVeicoloSelect.setLabel("Tipologia veicolo");
        tipologiaVeicoloSelect.setPlaceholder("Seleziona tipologia");
        List<TipologiaVeicolo> tipologie = tipologiaVeicoloService.list(Pageable.unpaged());
        if (tipologie.isEmpty()) {
            tipologiaVeicoloSelect.setEnabled(false);
            tipologiaVeicoloSelect.setPlaceholder("Nessuna tipologia disponibile");
        } else {
            tipologiaVeicoloSelect.setItems(tipologie);
            tipologiaVeicoloSelect.setItemLabelGenerator(TipologiaVeicolo::getTipologia);
        }

        numeroCilindri.setLabel("Numero cilindri");
        numeroCilindri.setItems(0, 1, 2, 3, 4, 5, 6, 8, 10, 12);

        cilindrata.setPlaceholder("Es. 1998");
        cilindrata.setLabel("Cilindrata");
        cilindrata.setAriaLabel("Cilindrata");
        cilindrata.setMin(0);

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

        numeroPasseggeri.setPlaceholder("Es. 5");
        numeroPasseggeri.setLabel("Numero passeggeri");
        numeroPasseggeri.setAriaLabel("Numero passeggeri");
        numeroPasseggeri.setMin(1);
        numeroPasseggeri.setMax(9);

        costoGiornaliero.setPlaceholder("Es. 80");
        costoGiornaliero.setLabel("Costo noleggio giornaliero");
        costoGiornaliero.setAriaLabel("Costo noleggio giornaliero");
        costoGiornaliero.setMin(0);

        quantita.setPlaceholder("Es. 3");
        quantita.setLabel("Quantità");
        quantita.setAriaLabel("Quantità");
        quantita.setMin(0);

        var openDialogBtn = new Button("+", e -> formDialog.open());
        openDialogBtn.addClassNames("form-btn", "modelli-open-dialog-btn");

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.addClassName("modelli-toolbar");
        toolbar.add(new ViewTitle("Lista modelli"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.addClassNames("outer-wrapper-shadow", "modelli-outer-wrapper");
        outerWrapper.add(toolbar, openDialogBtn);

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
            elimina.addClassName("modelli-delete-btn");
            return elimina;
        }).setHeader("Azioni");

        modelloGrid.setEmptyStateText("Non ci sono modelli registrati");
        modelloGrid.addClassNames("grid-style", "modelli-grid");
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

        Notification.show(nome + " aggiunto!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.SUCCESS);
    }

    private void deleteModello(Long id) {
        modelloService.deleteModello(id);
        modelloGrid.getDataProvider().refreshAll();

        Notification.show("Modello eliminato!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {
        var dialog = new Dialog();
        dialog.setHeaderTitle("Aggiungi modello");
        dialog.addClassName("modelli-form-dialog");

        var form = new FormLayout();
        form.addClassName("modelli-form");
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("700px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );

        form.add(
                marcaSelect,
                nomeModello,
                tipologiaVeicoloSelect,
                numeroCilindri,
                alimentazioneSelect,
                cilindrata,
                numeroPasseggeri,
                costoGiornaliero,
                quantita
        );

        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());
        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }
}