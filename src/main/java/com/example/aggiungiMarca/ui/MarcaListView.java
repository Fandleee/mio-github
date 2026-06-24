package com.example.aggiungiMarca.ui;

import com.example.aggiungiMarca.Marca;
import com.example.aggiungiMarca.MarcaService;
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

@Route(value = "marche")
@PageTitle("Marche")
@Menu(order = 1, icon = "icons/marche.svg", title = "Marche")
class MarcaListView extends VerticalLayout {

    private final MarcaService marcaService;

    private final Dialog formDialog;
    private final TextField nomeMarca;
    private final Button createBtn;
    private final Grid<Marca> marcaGrid;

    MarcaListView(MarcaService marcaService) {
        this.marcaService = marcaService;

        addClassName("marca-view");

        nomeMarca = new TextField();
        nomeMarca.setPlaceholder("BMW");
        nomeMarca.setAriaLabel("Nome marca");
        nomeMarca.setMaxLength(Marca.NOME_MARCA_MAX_LENGTH);
        nomeMarca.addClassName("marca-nome-field");

        createBtn = new Button("Aggiungi", event -> createMarca());
        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        marcaGrid = new Grid<>();
        formDialog = createFormDialog();

        var openDialogBtn = new Button("+", e -> formDialog.open());
        openDialogBtn.addClassNames("form-btn", "marca-open-dialog-btn");

        var toolbar = new HorizontalLayout();
        toolbar.setWrap(true);
        toolbar.setHeightFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.addClassName("marca-toolbar");
        toolbar.add(new ViewTitle("Lista marchi"));

        var outerWrapper = new HorizontalLayout();
        outerWrapper.setWidthFull();
        outerWrapper.setSpacing(false);
        outerWrapper.setAlignItems(Alignment.CENTER);
        outerWrapper.setFlexGrow(1, toolbar);
        outerWrapper.addClassNames("outer-wrapper-shadow", "marca-outer-wrapper");
        outerWrapper.add(toolbar, openDialogBtn);

        marcaGrid.setItems(query -> marcaService.list(toSpringPageRequest(query)).stream());
        marcaGrid.addColumn(Marca::getMarca).setHeader("Nome");

        marcaGrid.addComponentColumn(marca -> {
            Button elimina = new Button("Elimina", click -> deleteMarca(marca.getId()));
            elimina.addThemeVariants(ButtonVariant.LUMO_ERROR);
            elimina.addClassName("marca-delete-btn");
            return elimina;
        }).setHeader("Azioni");

        marcaGrid.setEmptyStateText("Non ci sono marchi registrati");
        marcaGrid.addClassNames("grid-style", "marca-grid");
        marcaGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_ROW_STRIPES,
                GridVariant.NO_ROW_BORDERS
        );

        marcaGrid.setSizeFull();
        setSizeFull();

        add(outerWrapper, marcaGrid);
    }

    private void createMarca() {
        if (nomeMarca.getValue().isBlank()) {
            nomeMarca.setInvalid(true);
            nomeMarca.setErrorMessage("Nome richiesto");
            return;
        }

        String nome = nomeMarca.getValue();
        marcaService.createMarca(nome);
        marcaGrid.getDataProvider().refreshAll();
        nomeMarca.clear();
        formDialog.close();

        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.SUCCESS);
    }

    private void deleteMarca(Long id) {
        marcaService.deleteMarca(id);
        marcaGrid.getDataProvider().refreshAll();

        Notification.show("Marca eliminata!", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.WARNING);
    }

    private Dialog createFormDialog() {
        var dialog = new Dialog();
        dialog.setHeaderTitle("Aggiungi marchio");
        dialog.addClassName("marca-form-dialog");

        var form = new FormLayout(nomeMarca);
        dialog.add(form);

        var annullaBtn = new Button("Annulla", e -> dialog.close());
        dialog.getFooter().add(annullaBtn, createBtn);

        return dialog;
    }
}