package com.example.tipologieVeicolo.ui;

import com.example.base.ui.ViewTitle;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route(value = "tipologie-veicolo")
@PageTitle("Tipologie Veicolo")
@Menu(order = 2, icon = "icons/tipologie.svg", title = "Tipologie Veicolo")
class TipologieVeicoloListView extends VerticalLayout {

    private final TipologiaVeicoloService tipologiaVeicoloService;

    private final Dialog formDialog;
    private final TextField nomeTipologia;
    private final Button createBtn;
    private final Grid<TipologiaVeicolo> tipologiaGrid;

    TipologieVeicoloListView(TipologiaVeicoloService tipologiaVeicoloService) {
        this.tipologiaVeicoloService = tipologiaVeicoloService;

        addClassName("tipologie-view");

        nomeTipologia = new TextField();
        nomeTipologia.setPlaceholder("SUV");
        nomeTipologia.setAriaLabel("Tipologia veicolo");
        nomeTipologia.setMaxLength(TipologiaVeicolo.NOME_MAX_LENGTH);
        nomeTipologia.addClassName("tipologie-nome-field");

        createBtn = new Button("Aggiungi", event -> createTipologia());
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        tipologiaGrid = new Grid<>();
        formDialog = createFormDialog();

        var openDialogBtn = new Button("+", e -> formDialog.open());
        openDialogBtn.addClassNames("form-btn", "tipologie-open-dialog-btn");

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.addClassName("tipologie-toolbar");
        toolbar.add(new ViewTitle("Lista tipologie"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.addClassNames("outer-wrapper-shadow", "tipologie-outer-wrapper");
        outerWrapper.add(toolbar, openDialogBtn);

        tipologiaGrid.setItems(query -> tipologiaVeicoloService.list(toSpringPageRequest(query)).stream());
        tipologiaGrid.addColumn(TipologiaVeicolo::getTipologia).setHeader("Tipologia");

        tipologiaGrid.addComponentColumn(tipologiaVeicolo -> {
            Button elimina = new Button("Elimina", click -> deleteTipologiaVeicolo(tipologiaVeicolo.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            elimina.addClassName("tipologie-delete-btn");
            return elimina;
        }).setHeader("Azioni");

        tipologiaGrid.setEmptyStateText("Non ci sono tipologie di veicolo registrate");
        tipologiaGrid.addClassNames("grid-style", "tipologie-grid");
        tipologiaGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.NO_ROW_BORDERS
        );

        tipologiaGrid.setSizeFull();
        setSizeFull();

        add(outerWrapper, tipologiaGrid);
    }

    private void createTipologia() {
        if (nomeTipologia.getValue().isBlank()) {
            nomeTipologia.setInvalid(true);
            nomeTipologia.setErrorMessage("Nome richiesto");
            return;
        }

        String nome = nomeTipologia.getValue();
        tipologiaVeicoloService.createTipologia(nome);
        tipologiaGrid.getDataProvider().refreshAll();
        nomeTipologia.clear();
        formDialog.close();

        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.SUCCESS);
    }

    private void deleteTipologiaVeicolo(Long id) {
        tipologiaVeicoloService.deleteTipologia(id);
        tipologiaGrid.getDataProvider().refreshAll();

        Notification.show("Tipologia eliminata!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {
        var dialog = new Dialog();
        dialog.setHeaderTitle("Aggiungi tipologia");
        dialog.addClassName("tipologie-form-dialog");

        var form = new FormLayout(nomeTipologia);
        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());
        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }
}