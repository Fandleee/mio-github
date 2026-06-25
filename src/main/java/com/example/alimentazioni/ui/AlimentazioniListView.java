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

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route(value = "alimentazioni")
@PageTitle("Alimentazioni")
@Menu(order = 3, icon = "icons/gas.svg", title = "Alimentazioni")
class AlimentazioniListView extends VerticalLayout {

    private final AlimentazioneService alimentazioneService;

    private final Dialog formDialog;
    private final TextField nomeAlimentazione;
    private final Button createBtn;
    private final Grid<Alimentazione> alimentazioneGrid;

    AlimentazioniListView(AlimentazioneService alimentazioneService) {
        this.alimentazioneService = alimentazioneService;

        addClassName("alimentazioni-view");

        nomeAlimentazione = new TextField();
        nomeAlimentazione.setPlaceholder("Benzina");
        nomeAlimentazione.setAriaLabel("Nome alimentazione");
        nomeAlimentazione.setMaxLength(Alimentazione.NOME_MAX_LENGTH);
        nomeAlimentazione.addClassName("alimentazioni-nome-field");

        createBtn = new Button("Aggiungi", event -> createAlimentazione());
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        alimentazioneGrid = new Grid<>();
        formDialog = createFormDialog();

        var openDialogBtn = new Button("+", e -> formDialog.open());
        openDialogBtn.addClassNames("form-btn", "alimentazioni-open-dialog-btn");

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.addClassName("alimentazioni-toolbar");
        toolbar.add(new ViewTitle("Lista alimentazioni"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.addClassNames("outer-wrapper-shadow", "alimentazioni-outer-wrapper");
        outerWrapper.add(toolbar, openDialogBtn);

        alimentazioneGrid.setItems(query -> alimentazioneService.list(toSpringPageRequest(query)).stream());
        alimentazioneGrid.addColumn(Alimentazione::getAlimentazione).setHeader("Nome");

        alimentazioneGrid.addComponentColumn(alimentazione -> {
            Button elimina = new Button("Elimina", click -> deleteAlimentazione(alimentazione.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            elimina.addClassName("alimentazioni-delete-btn");
            return elimina;
        }).setHeader("Azioni");

        alimentazioneGrid.setEmptyStateText("Non ci sono alimentazioni registrate");
        alimentazioneGrid.addClassNames("grid-style", "alimentazioni-grid");
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
        alimentazioneService.createAlimentazione(nome);
        alimentazioneGrid.getDataProvider().refreshAll();
        nomeAlimentazione.clear();
        formDialog.close();

        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.SUCCESS);
    }

    private void deleteAlimentazione(Long id) {
        alimentazioneService.deleteAlimentazione(id);
        alimentazioneGrid.getDataProvider().refreshAll();

        Notification.show("Alimentazione eliminata!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {
        var dialog = new Dialog();
        dialog.setHeaderTitle("Aggiungi alimentazione");
        dialog.addClassName("alimentazioni-form-dialog");

        var form = new FormLayout(nomeAlimentazione);
        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());
        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }
}