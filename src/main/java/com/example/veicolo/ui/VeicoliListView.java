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

    final Select<Marca> marcaSelect;
    final Select<Modello> modelliSelect;
    final TextField targa;
    final DatePicker dataUltimaPrenotazione;
    final IntegerField prenotataPerGiorni;
    final DatePicker dataScadenzaAssicurazione;

    final Dialog formDialog;
    final Button createBtn;
    Button updateBtn;
    final Grid<Veicolo> veicoloGrid;

    VeicoliListView(ModelloService modelloService, VeicoloService veicoloService, MarcaService marcaService) {
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

        modelliSelect.setEnabled(false);

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

        // Modello Select
        modelliSelect.setLabel("Modelli");
        modelliSelect.setPlaceholder("Seleziona modello");
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
        targa.setMinLength(7);
        targa.setWidth("12ch");

        // Data ultima prenotazione
        dataUltimaPrenotazione.setLabel("Data inizio ultima prenotazione");
        dataUltimaPrenotazione.setAriaLabel("Data inizio ultima prenotazione");

        // Giorni di prenotazione
        prenotataPerGiorni.setLabel("Durata prenotazione");
        prenotataPerGiorni.setAriaLabel("Durata prenotazione");
        prenotataPerGiorni.setWidth("10ch");

        // Scadenza assicurazione
        dataScadenzaAssicurazione.setLabel("Data scadenza assicurazione");
        dataScadenzaAssicurazione.setAriaLabel("Data scadenza assicurazione");

        // Button
        createBtn.setText("Aggiungi");
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        var openDialogBtn = new Button("+", event -> openCreateDialog());
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
                Veicolo::getDataUltimaPrenotazione, () -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        )).setHeader("Noleggiata dal").setSortProperty("dataUltimaPrenotazione");

        veicoloGrid.addColumn(Veicolo::getPrenotataPerGiorni)
                .setHeader("Durata noleggio")
                .setSortProperty("prenotataPerGiorni");

        veicoloGrid.addColumn(Veicolo::getFatturatoDaPrenotazione)
                .setHeader("Guadagno in euro")
                .setSortProperty("fatturatoDaPrenotazione");

        veicoloGrid.addColumn(new LocalDateRenderer<>(
                Veicolo::getDataPrimaDisponibilita, () -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        )).setHeader("Prima disponibilita").setSortProperty("dataPrimaDisponibilita");

        veicoloGrid.addColumn(new LocalDateRenderer<>(
                Veicolo::getDataScadenzaAssicurazione, () -> DateTimeFormatter.ofPattern("dd-MM-yyyy")
        )).setHeader("Scadenza polizza").setSortProperty("dataScadenzaAssicurazione");

        veicoloGrid.addComponentColumn(veicolo -> {
            boolean assicurato = veicolo.isAssicurato();
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
            Button modifica = new Button(new Icon(VaadinIcon.PENCIL), click -> openEditDialog(veicolo));
            Button elimina = new Button(new Icon(VaadinIcon.TRASH), click -> deleteVeicolo(veicolo.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return new HorizontalLayout(modifica, elimina);
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

    private Boolean isFormValid() {
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
        if (!isFormValid()) return;

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
        if (!isFormValid()) return;

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

        // Più compatto su desktop, quasi pieno su mobile
        dialog.setWidth("95vw");
        dialog.setMaxWidth("820px");

        marcaSelect.setWidthFull();
        modelliSelect.setWidthFull();
        targa.setWidthFull();
        dataUltimaPrenotazione.setWidthFull();
        prenotataPerGiorni.setWidthFull();
        dataScadenzaAssicurazione.setWidthFull();

        var form = new FormLayout();
        form.setWidthFull();

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