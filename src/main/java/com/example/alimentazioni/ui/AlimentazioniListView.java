package com.example.alimentazioni.ui;

import com.example.alimentazioni.Alimentazione;
import com.example.alimentazioni.AlimentazioneService;
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

import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route(value = "alimentazioni")
@PageTitle("Alimentazioni")
@Menu(order = 2, icon = "", title = "Alimentazioni")

class AlimentazioniListView extends VerticalLayout {

    private final AlimentazioneService alimentazioneService;

    final TextField nomeAlimentazione;
    final Button createBtn;
    final Grid<Alimentazione> alimentazioneGrid;

    AlimentazioniListView(AlimentazioneService alimentazioneService) {

        this.alimentazioneService = alimentazioneService;

        nomeAlimentazione = new TextField();
        createBtn = new Button("+", event -> createAlimentazione());
        alimentazioneGrid = new Grid<>();

        nomeAlimentazione.setPlaceholder("Benzina");
        nomeAlimentazione.setAriaLabel("Nome alimentazione");
        nomeAlimentazione.setMaxLength(Alimentazione.NOME_MAX_LENGTH);
        nomeAlimentazione.setMinWidth("15em");
        nomeAlimentazione.setClassName("padding-left-form");

        createBtn.setClassName("form-btn");
        createBtn.setHeightFull();

        var toolbar = new HorizontalLayout();

        toolbar.setFlexGrow(1, nomeAlimentazione);
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setClassName("form-standard-style");
        toolbar.add(new ViewTitle("Lista alimentazioni"), nomeAlimentazione);

        var outerWrapper = new HorizontalLayout();

        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.setClassName("outer-wrapper-shadow");
        outerWrapper.add(toolbar, createBtn);

        alimentazioneGrid.setItems(query -> alimentazioneService.list(toSpringPageRequest(query)).stream());
        alimentazioneGrid.addColumn(Alimentazione::getAlimentazione).setHeader("Nome");
        alimentazioneGrid.setEmptyStateText("Non ci sono alimentazioni registrate");
        alimentazioneGrid.setSizeFull();

        setSizeFull();

        add(outerWrapper, alimentazioneGrid);
    }

    private void createAlimentazione() {

        if (nomeAlimentazione.getValue().isBlank()) {
            nomeAlimentazione.setInvalid(true);
            nomeAlimentazione.setErrorMessage("Nome richiesto");
            return;
        }

        String nome = nomeAlimentazione.getValue();
        alimentazioneService.createAlimentazione(nomeAlimentazione.getValue());
        alimentazioneGrid.getDataProvider().refreshAll();
        nomeAlimentazione.clear();
        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.SUCCESS);
    }
}
