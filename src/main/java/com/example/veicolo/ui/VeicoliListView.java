package com.example.veicolo.ui;

import com.example.aggiungiMarca.Marca;
import com.example.aggiungiMarca.MarcaService;
import com.example.base.ui.ViewTitle;
import com.example.modelli.Modello;
import com.example.modelli.ModelloService;
import com.example.veicolo.Veicolo;
import com.example.veicolo.VeicoloService;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
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

import javax.swing.*;
import java.util.List;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route(value = "")
@PageTitle("Veicoli")
@Menu(order = 0, icon = "", title = "Veicoli")

public class VeicoliListView extends VerticalLayout {

    private final VeicoloService veicoloService;

    final Select<Marca> marcaSelect;
    final Select<Modello> modelliSelect;
    final TextField targa;
    final DatePicker dataUltimaPrenotazione;
    final IntegerField prenotataPerGiorni;
    final DatePicker dataScadenzaAssicurazione;

    final Dialog formDialog;
    final Button createBtn;
    final Grid<Veicolo> veicoloGrid;

    VeicoliListView(ModelloService modelloService, VeicoloService veicoloService, MarcaService marcaService){

        this.veicoloService = veicoloService;

        marcaSelect = new Select<>();
        modelliSelect = new Select<>();
        targa = new TextField();
        dataUltimaPrenotazione = new DatePicker();
        prenotataPerGiorni = new IntegerField();
        dataScadenzaAssicurazione = new DatePicker();

        createBtn = new Button("Aggiungi", event -> createVeicolo());
        veicoloGrid = new Grid<>();

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

        // Modello Select
        modelliSelect.setLabel("Modelli");
        modelliSelect.setPlaceholder("Seleziona marca");
        List<Modello> modello = modelloService.list(Pageable.unpaged());
        if (modello.isEmpty()) {
            modelliSelect.setEnabled(false);
            modelliSelect.setPlaceholder("Nessun modello disponibile");
        } else {
            modelliSelect.setItems(modello);
            modelliSelect.setItemLabelGenerator(Modello::getNomeModello);
        }

        // Targa
        targa.setPlaceholder("AA000AA");
        targa.setLabel("Targa veicolo");
        targa.setAriaLabel("Targa veicolo");
        targa.setMaxLength(Veicolo.TARGA_VEICOLO_MAX_LENGTH);
        targa.setMinWidth("15em");

        // Data ultima prenotazione
        dataUltimaPrenotazione.setLabel("Data inizio ultima prenotazione");
        dataUltimaPrenotazione.setAriaLabel("Data inizio ultima prenotazione");
        dataUltimaPrenotazione.setMinWidth("15em");

        // Giorni di penotazione
        prenotataPerGiorni.setLabel("Durata prenotazione");
        prenotataPerGiorni.setAriaLabel("Durata prenotazione");
        prenotataPerGiorni.setMinWidth("15em");

        // Scadenza assicurazione
        dataScadenzaAssicurazione.setLabel("Data scadenza assicurazione");
        dataScadenzaAssicurazione.setAriaLabel("Data scadenza assicurazione");
        dataScadenzaAssicurazione.setMinWidth("15em");

        // Button
        createBtn.setText("Aggiungi");
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);


        var openDialogBtn = new Button("+", event -> formDialog.open());
        openDialogBtn.setClassName("form-btn");
        openDialogBtn.setHeightFull();

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setClassName("form-standard-style");
        toolbar.add(new ViewTitle("Lista veicoli"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.setClassName("outer-wrapper-shadow");
        outerWrapper.add(toolbar, openDialogBtn);

        // Visualizzazione record
        veicoloGrid.setItems(query -> veicoloService.list(toSpringPageRequest(query)).stream());

        veicoloGrid.addColumn(veicolo -> veicolo.getMarca().getMarca()).setHeader("Marca");
        veicoloGrid.addColumn(veicolo -> veicolo.getNomeModello().getNomeModello()).setHeader("Modello");
        veicoloGrid.addColumn(Veicolo::getTarga).setHeader("Targa");
        veicoloGrid.addColumn(Veicolo::getDataUltimaPrenotazione).setHeader("Noleggiata dal");
        veicoloGrid.addColumn(Veicolo::getPrenotataPerGiorni).setHeader("Durata noleggio");
        veicoloGrid.addColumn(Veicolo::getFatturatoDaPrenotazione).setHeader("Guadagno in euro");
        veicoloGrid.addColumn(Veicolo::getDataPrimaDisponibilita).setHeader("Prima disponibilita");
        veicoloGrid.addColumn(Veicolo::getDataScadenzaAssicurazione).setHeader("Scadenza polizza");

        veicoloGrid.addComponentColumn(veicolo -> {
            boolean assicurato = Boolean.TRUE.equals(veicolo.geteAssicurato());
            Icon icon = VaadinIcon.CHECK.create();

            if (assicurato) {
                icon.setClassName("icon-green");
            } else {
                icon = VaadinIcon.CLOSE.create();
                icon.setClassName("icon-red");
            }

            return icon;
        }).setHeader("Polizza valida?");

        veicoloGrid.addComponentColumn(veicolo -> {
            Button elimina = new Button("Elimina", click -> deleteVeicolo(veicolo.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return elimina;
        }).setHeader("Azioni");

        veicoloGrid.setEmptyStateText("Non ci sono veicoli registrati");

        veicoloGrid.setClassName("grid-style");
        veicoloGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.NO_ROW_BORDERS
                );

        veicoloGrid.setSizeFull();

        setSizeFull();

        add(outerWrapper, veicoloGrid);
    }

    private void createVeicolo() {

        if (marcaSelect.getValue() == null) {
            marcaSelect.setInvalid(true);
            marcaSelect.setErrorMessage("Marca richiesta");
            return;
        }
        if (modelliSelect.getValue() == null) {
            modelliSelect.setInvalid(true);
            modelliSelect.setErrorMessage("Modello richiesto");
            return;
        }
        if (targa.getValue().isBlank()) {
            targa.setInvalid(true);
            targa.setErrorMessage("Targa richiesta");
            return;
        }
        if (dataUltimaPrenotazione.getValue() == null) {
            dataUltimaPrenotazione.setInvalid(true);
            dataUltimaPrenotazione.setErrorMessage("Data richiesta");
            return;
        }
        if (prenotataPerGiorni.getValue() == null) {
            prenotataPerGiorni.setInvalid(true);
            prenotataPerGiorni.setErrorMessage("Durata richiesta");
            return;
        }
        if (dataScadenzaAssicurazione.getValue() == null) {
            dataScadenzaAssicurazione.setInvalid(true);
            dataScadenzaAssicurazione.setErrorMessage("Data scadenza richiesta");
            return;
        }

        String targaValore = targa.getValue();
        veicoloService.createVeicolo(
                marcaSelect.getValue(),
                modelliSelect.getValue(),
                targaValore,
                dataUltimaPrenotazione.getValue(),
                prenotataPerGiorni.getValue(),
                dataScadenzaAssicurazione.getValue()
        );

        veicoloGrid.getDataProvider().refreshAll();

        marcaSelect.setValue(null);
        modelliSelect.setValue(null);
        targa.clear();
        dataUltimaPrenotazione.setValue(null);
        prenotataPerGiorni.setValue(null);
        dataScadenzaAssicurazione.setValue(null);
        formDialog.close();
        Notification.show(targaValore + " aggiunto!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void deleteVeicolo(Long id){
        veicoloService.deleteVeicolo(id);
        veicoloGrid.getDataProvider().refreshAll();
        Notification.show("Veicolo eliminato!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {

        var dialog = new Dialog();

        dialog.setHeaderTitle("Aggiungi modello");
        dialog.setMaxWidth("700px");

        var form = new FormLayout(marcaSelect, modelliSelect, targa, dataUltimaPrenotazione, prenotataPerGiorni, dataScadenzaAssicurazione);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 6));
        form.setColspan(marcaSelect ,3);
        form.setColspan(modelliSelect ,3);
        form.setColspan(targa ,3);
        form.setColspan(dataUltimaPrenotazione ,3);
        form.setColspan(prenotataPerGiorni ,3);
        form.setColspan(dataScadenzaAssicurazione ,3);

        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());

        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }
}
