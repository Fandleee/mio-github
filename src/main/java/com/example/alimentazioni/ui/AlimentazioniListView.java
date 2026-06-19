package com.example.alimentazioni.ui;

import com.example.alimentazioni.Alimentazione;
import com.example.alimentazioni.AlimentazioneService;
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

import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route(value = "alimentazioni")
@PageTitle("Alimentazioni")
@Menu(order = 3, icon = "icons/alimentazioni.svg", title = "Alimentazioni")

class AlimentazioniListView extends VerticalLayout {

    private final AlimentazioneService alimentazioneService;

    final Dialog formDialog;
    final TextField nomeAlimentazione;
    final Button createBtn;
    final Grid<Alimentazione> alimentazioneGrid;

    AlimentazioniListView(AlimentazioneService alimentazioneService) {

        this.alimentazioneService = alimentazioneService;

        nomeAlimentazione = new TextField();
        createBtn = new Button("Aggiungi", event -> createAlimentazione());
        alimentazioneGrid = new Grid<>();

        nomeAlimentazione.setPlaceholder("Benzina");
        nomeAlimentazione.setAriaLabel("Nome alimentazione");
        nomeAlimentazione.setMaxLength(Alimentazione.NOME_MAX_LENGTH);
        nomeAlimentazione.setMinWidth("15em");
        nomeAlimentazione.setClassName("padding-left-form");

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
        toolbar.add(new ViewTitle("Lista alimentazioni"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.setClassName("outer-wrapper-shadow");
        outerWrapper.add(toolbar, openDialogBtn);

        alimentazioneGrid.setItems(query -> alimentazioneService.list(toSpringPageRequest(query)).stream());
        alimentazioneGrid.addColumn(Alimentazione::getAlimentazione).setHeader("Nome");

        alimentazioneGrid.addComponentColumn(alimentazione -> {
            Button elimina = new Button("Elimina", click -> deleteAlimentazione(alimentazione.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return elimina;
        }).setHeader("Azioni");

        alimentazioneGrid.setEmptyStateText("Non ci sono alimentazioni registrate");

        alimentazioneGrid.setClassName("grid-style");
        alimentazioneGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.NO_ROW_BORDERS
        );

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
        formDialog.close();
        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.SUCCESS);
    }

    private void deleteAlimentazione(Long id) {
        alimentazioneService.deleteAlimentazione(id);
        alimentazioneGrid.getDataProvider().refreshAll();
        Notification.show("Alimentazione eliminata!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {

        var dialog = new Dialog();
        dialog.setHeaderTitle("Aggiungi alimentazione");
        dialog.setMaxWidth("700px");

        var form = new FormLayout(nomeAlimentazione);

        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());

        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }

}
