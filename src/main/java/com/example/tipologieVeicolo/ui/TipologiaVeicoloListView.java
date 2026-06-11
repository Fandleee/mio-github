package com.example.tipologieVeicolo.ui;

import com.example.tipologieVeicolo.TipologiaVeicolo;
import com.example.tipologieVeicolo.TipologiaVeicoloService;
import com.example.base.ui.ViewTitle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
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

        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        var toolbar = new HorizontalLayout();

        toolbar.add(new ViewTitle("Lista tipologie veicolo"), nomeTipologia, createBtn);

        toolbar.setFlexGrow(1, nomeTipologia);
        toolbar.setWrap(true);
        toolbar.setWidthFull();

        tipologiaGrid.setItems(query -> tipologiaVeicoloService.list(toSpringPageRequest(query)).stream());
        tipologiaGrid.addColumn(TipologiaVeicolo::getTipologia).setHeader("Tipologia");
        tipologiaGrid.setEmptyStateText("Non ci sono tipologie di veicolo registrate");
        tipologiaGrid.setSizeFull();

        setSizeFull();

        add(toolbar, tipologiaGrid);
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
        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.SUCCESS);
    }
}
