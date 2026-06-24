package com.example.veicolo.ui;

import com.example.aggiungiMarca.Marca;
import com.example.aggiungiMarca.MarcaService;
import com.example.base.ui.ViewTitle;
import com.example.modelli.Modello;
import com.example.modelli.ModelloService;
import com.example.veicolo.Veicolo;
import com.example.veicolo.VeicoloService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route(value = "")
@PageTitle("Veicoli")
@Menu(order = 5, icon = "icons/car.svg", title = "Veicoli")
public class VeicoliListView extends VerticalLayout {

    private final VeicoloService veicoloService;
    private Veicolo veicoloInModifica;

    private final Select<Marca> marcaSelect;
    private final Select<Modello> modelliSelect;
    private final TextField targa;
    private final DatePicker dataUltimaPrenotazione;
    private final IntegerField prenotataPerGiorni;
    private final DatePicker dataScadenzaAssicurazione;

    private final Dialog formDialog;
    private final Button createBtn;
    private Button updateBtn;
    private final Grid<Veicolo> veicoloGrid;

    VeicoliListView(ModelloService modelloService, VeicoloService veicoloService, MarcaService marcaService) {
        this.veicoloService = veicoloService;

        addClassName("veicoli-view");

        marcaSelect = new Select<>();
        marcaSelect.addClassName("veicoli-select-full");

        modelliSelect = new Select<>();
        modelliSelect.addClassName("veicoli-select-full");

        targa = new TextField();
        targa.addClassNames("veicoli-text-full", "veicoli-targa-field");

        dataUltimaPrenotazione = new DatePicker();
        dataUltimaPrenotazione.addClassName("veicoli-date-full");

        prenotataPerGiorni = new IntegerField();
        prenotataPerGiorni.addClassNames("veicoli-integer-full", "veicoli-prenotazione-field");

        dataScadenzaAssicurazione = new DatePicker();
        dataScadenzaAssicurazione.addClassName("veicoli-date-full");

        createBtn = new Button("Aggiungi", event -> createVeicolo());
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        veicoloGrid = new Grid<>();
        veicoloGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        formDialog = createFormDialog();

        modelliSelect.setEnabled(false);

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

        marcaSelect.addValueChangeListener(event -> {
            Marca marcaScelta = event.getValue();
            modelliSelect.clear();

            if (marcaScelta == null) {
                modelliSelect.setEnabled(false);
                return;
            }

            List<Modello> modelli = modelloService.listByMarca(marcaScelta);
            modelliSelect.setItems(modelli);
            modelliSelect.setEnabled(!modelli.isEmpty());
        });

        modelliSelect.setLabel("Modelli");
        modelliSelect.setPlaceholder("Seleziona modello");
        List<Modello> modelli = modelloService.list(Pageable.unpaged());
        if (modelli.isEmpty()) {
            modelliSelect.setEnabled(false);
            modelliSelect.setPlaceholder("Nessun modello disponibile");
        } else {
            modelliSelect.setItems(modelli);
            modelliSelect.setItemLabelGenerator(Modello::getNomeModello);
        }

        targa.setPlaceholder("AA000AA");
        targa.setLabel("Targa veicolo");
        targa.setAriaLabel("Targa veicolo");
        targa.setMaxLength(Veicolo.TARGA_VEICOLO_MAX_LENGTH);
        targa.setMinLength(7);

        dataUltimaPrenotazione.setLabel("Data inizio ultima prenotazione");
        dataUltimaPrenotazione.setAriaLabel("Data inizio ultima prenotazione");

        prenotataPerGiorni.setLabel("Durata prenotazione");
        prenotataPerGiorni.setAriaLabel("Durata prenotazione");

        dataScadenzaAssicurazione.setLabel("Data scadenza assicurazione");
        dataScadenzaAssicurazione.setAriaLabel("Data scadenza assicurazione");

        var openDialogBtn = new Button("+", event -> openCreateDialog());
        openDialogBtn.addClassNames("form-btn", "veicoli-open-dialog-btn");

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.addClassName("veicoli-toolbar");
        toolbar.add(new ViewTitle("Lista veicoli"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.addClassNames("outer-wrapper-shadow", "veicoli-outer-wrapper");
        outerWrapper.add(toolbar, openDialogBtn);

        veicoloGrid.setItems(query -> veicoloService.list(toSpringPageRequest(query)).stream());

        veicoloGrid.addColumn(veicolo -> veicolo.getMarca().getMarca())
                .setHeader("Marca")
                .setSortProperty("marca.marca");

        veicoloGrid.addColumn(veicolo -> veicolo.getNomeModello().getNomeModello())
                .setHeader("Modello")
                .setSortProperty("nomeModello.nomeModello");

        veicoloGrid.addColumn(Veicolo::getTarga)
                .setHeader("Targa")
                .setSortProperty("targa");

        veicoloGrid.addColumn(new LocalDateRenderer<>(
                Veicolo::getDataUltimaPrenotazione,
                () -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        )).setHeader("Noleggiata dal").setSortProperty("dataUltimaPrenotazione");

        veicoloGrid.addColumn(Veicolo::getPrenotataPerGiorni)
                .setHeader("Durata noleggio")
                .setSortProperty("prenotataPerGiorni");

        veicoloGrid.addColumn(Veicolo::getFatturatoDaPrenotazione)
                .setHeader("Guadagno in euro")
                .setSortProperty("fatturatoDaPrenotazione");

        veicoloGrid.addColumn(new LocalDateRenderer<>(
                Veicolo::getDataPrimaDisponibilita,
                () -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        )).setHeader("Prima disponibilita").setSortProperty("dataPrimaDisponibilita");

        veicoloGrid.addColumn(new LocalDateRenderer<>(
                Veicolo::getDataScadenzaAssicurazione,
                () -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        )).setHeader("Scadenza polizza").setSortProperty("dataScadenzaAssicurazione");

        veicoloGrid.addComponentColumn(veicolo -> {
            boolean assicurato = veicolo.isAssicurato();
            Icon icon = assicurato ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
            icon.setClassName(assicurato ? "icon-green" : "icon-red");
            return icon;
        }).setHeader("Polizza valida?");

        veicoloGrid.addComponentColumn(veicolo -> {
            Button modifica = new Button(new Icon(VaadinIcon.PENCIL), click -> openEditDialog(veicolo));
            Button elimina = new Button(new Icon(VaadinIcon.TRASH), click -> deleteVeicolo(veicolo.getId()));

            modifica.addClassName("veicoli-action-btn");
            elimina.addClassNames("veicoli-action-btn");
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);

            HorizontalLayout actions = new HorizontalLayout(modifica, elimina);
            actions.addClassName("veicoli-actions-layout");
            return actions;
        }).setHeader("Azioni");

        veicoloGrid.setEmptyStateText("Non ci sono veicoli registrati");
        veicoloGrid.addClassNames("grid-style", "veicoli-grid");
        veicoloGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.NO_ROW_BORDERS
        );

        veicoloGrid.setSizeFull();
        setSizeFull();

        add(outerWrapper, veicoloGrid);
    }

    private boolean isFormValid() {
        if (marcaSelect.getValue() == null) {
            marcaSelect.setInvalid(true);
            marcaSelect.setErrorMessage("Marca richiesta");
            return false;
        }
        if (modelliSelect.getValue() == null) {
            modelliSelect.setInvalid(true);
            modelliSelect.setErrorMessage("Modello richiesto");
            return false;
        }
        if (targa.getValue().isBlank()) {
            targa.setInvalid(true);
            targa.setErrorMessage("Targa richiesta");
            return false;
        }
        if (dataUltimaPrenotazione.getValue() == null) {
            dataUltimaPrenotazione.setInvalid(true);
            dataUltimaPrenotazione.setErrorMessage("Data richiesta");
            return false;
        }
        if (prenotataPerGiorni.getValue() == null) {
            prenotataPerGiorni.setInvalid(true);
            prenotataPerGiorni.setErrorMessage("Durata richiesta");
            return false;
        }
        if (dataScadenzaAssicurazione.getValue() == null) {
            dataScadenzaAssicurazione.setInvalid(true);
            dataScadenzaAssicurazione.setErrorMessage("Data scadenza richiesta");
            return false;
        }

        return true;
    }

    private void createVeicolo() {
        if (!isFormValid()) {
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
        clearForm();
        formDialog.close();

        Notification.show(targaValore + " aggiunto!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void updateVeicolo(Long id) {
        if (!isFormValid()) {
            return;
        }

        String targaValore = targa.getValue();

        veicoloService.updateVeicolo(
                id,
                marcaSelect.getValue(),
                modelliSelect.getValue(),
                targaValore,
                dataUltimaPrenotazione.getValue(),
                prenotataPerGiorni.getValue(),
                dataScadenzaAssicurazione.getValue()
        );

        veicoloGrid.getDataProvider().refreshAll();
        clearForm();
        formDialog.close();

        Notification.show(targaValore + " modificato!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void deleteVeicolo(Long id) {
        veicoloService.deleteVeicolo(id);
        veicoloGrid.getDataProvider().refreshAll();

        Notification.show("Veicolo eliminato!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.WARNING);
    }

    private void clearForm() {
        marcaSelect.setValue(null);
        modelliSelect.setValue(null);
        targa.clear();
        dataUltimaPrenotazione.setValue(null);
        prenotataPerGiorni.setValue(null);
        dataScadenzaAssicurazione.setValue(null);
    }

    private void openCreateDialog() {
        veicoloInModifica = null;
        clearForm();

        formDialog.setHeaderTitle("Aggiungi veicolo");
        createBtn.setVisible(true);
        updateBtn.setVisible(false);
        formDialog.open();
    }

    private void openEditDialog(Veicolo veicolo) {
        veicoloInModifica = veicolo;

        marcaSelect.setValue(veicolo.getMarca());
        modelliSelect.setValue(veicolo.getNomeModello());
        targa.setValue(veicolo.getTarga());
        dataUltimaPrenotazione.setValue(veicolo.getDataUltimaPrenotazione());
        prenotataPerGiorni.setValue(veicolo.getPrenotataPerGiorni());
        dataScadenzaAssicurazione.setValue(veicolo.getDataScadenzaAssicurazione());

        formDialog.setHeaderTitle("Modifica " + veicolo.getNomeModello().getNomeModello() + ": " + veicolo.getTarga());
        createBtn.setVisible(false);
        updateBtn.setVisible(true);
        formDialog.open();
    }

    private Dialog createFormDialog() {
        var dialog = new Dialog();
        dialog.addClassName("veicoli-form-dialog");

        var form = new FormLayout();
        form.addClassName("veicoli-form");
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("700px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );

        form.add(
                marcaSelect,
                modelliSelect,
                targa,
                dataUltimaPrenotazione,
                prenotataPerGiorni,
                dataScadenzaAssicurazione
        );

        dialog.add(form);

        updateBtn = new Button("Aggiorna", e -> updateVeicolo(veicoloInModifica.getId()));
        updateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateBtn.setVisible(false);

        var annullaBtn = new Button("Annulla", e -> dialog.close());
        dialog.getFooter().add(annullaBtn, createBtn, updateBtn);

        return dialog;
    }
}
