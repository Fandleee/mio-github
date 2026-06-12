package com.example.tipologieVeicolo.ui;

import com.example.tipologieVeicolo.TipologiaVeicolo;
import com.example.tipologieVeicolo.TipologiaVeicoloService;
import com.example.base.ui.ViewTitle;
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
@Menu(order = 3, icon = "", title = "Tipologie Veicolo")
class TipologieVeicoloListView extends VerticalLayout {

    private final TipologiaVeicoloService tipologiaVeicoloService;

    final Dialog formDialog;
    final TextField nomeTipologia;
    final Button createBtn;
    final Grid<TipologiaVeicolo> tipologiaGrid;

    TipologieVeicoloListView(TipologiaVeicoloService tipologiaVeicoloService) {

        this.tipologiaVeicoloService = tipologiaVeicoloService;

        nomeTipologia = new TextField();
        createBtn = new Button("Aggiungi", event -> createTipologia());
        tipologiaGrid = new Grid<>();

        nomeTipologia.setPlaceholder("SUV");
        nomeTipologia.setAriaLabel("Tipologia veicolo");
        nomeTipologia.setMaxLength(TipologiaVeicolo.NOME_MAX_LENGTH);
        nomeTipologia.setMinWidth("15em");
        nomeTipologia.setClassName("padding-left-form");

        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        formDialog = createFormDialog();

        var openDialogBtn = new Button("+", e -> formDialog.open());
        openDialogBtn.setClassName("form-btn");
        openDialogBtn.setHeightFull();

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setClassName("form-standard-style");
        toolbar.add(new ViewTitle("Lista tipologie"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.setClassName("outer-wrapper-shadow");
        outerWrapper.add(toolbar, openDialogBtn);

        tipologiaGrid.setItems(query -> tipologiaVeicoloService.list(toSpringPageRequest(query)).stream());
        tipologiaGrid.addColumn(TipologiaVeicolo::getTipologia).setHeader("Tipologia");

        tipologiaGrid.addComponentColumn(tipologiaVeicolo -> {
            Button elimina = new Button("Elimina", click -> deleteTipologiaVeicolo(tipologiaVeicolo.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return elimina;
        }).setHeader("Azioni");

        tipologiaGrid.setEmptyStateText("Non ci sono tipologie di veicolo registrate");

        tipologiaGrid.setClassName("grid-style");
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
        tipologiaVeicoloService.createTipologia(nomeTipologia.getValue());
        tipologiaGrid.getDataProvider().refreshAll();
        nomeTipologia.clear();
        formDialog.close();
        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.SUCCESS);
    }

    private void deleteTipologiaVeicolo(Long id){
        tipologiaVeicoloService.deleteTipologia(id);
        tipologiaGrid.getDataProvider().refreshAll();
        Notification.show("Tipologia eliminata!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {

        var dialog = new Dialog();
        dialog.setHeaderTitle("Aggiungi tipologia");
        dialog.setMaxWidth("700px");

        var form = new FormLayout(nomeTipologia);

        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());

        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }
}
