package com.example.aggiungiMarca.ui;

import com.example.aggiungiMarca.Marca;
import com.example.aggiungiMarca.MarcaService;
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

@Route(value = "marche")
@PageTitle("Marche")
@Menu(order = 4, icon = "", title = "Marche")

class MarcaListView extends VerticalLayout {

    private final MarcaService marcaService;

    final TextField nomeMarca;
    final Button createBtn;
    final Grid<Marca> marcaGrid;

    MarcaListView(MarcaService marcaService) {

        this.marcaService = marcaService;

        nomeMarca = new TextField();
        createBtn = new Button("Aggiungi", event -> createMarca());
        marcaGrid = new Grid<>();

        nomeMarca.setPlaceholder("BMW");
        nomeMarca.setAriaLabel("Nome marca");
        nomeMarca.setMaxLength(Marca.NOME_MARCA_MAX_LENGTH);
        nomeMarca.setMinWidth("15em");

        createBtn.addThemeVariants(ButtonVariant.PRIMARY);

        var toolbar = new HorizontalLayout();

        toolbar.add(new ViewTitle("Lista marche"), nomeMarca, createBtn);

        toolbar.setFlexGrow(1, nomeMarca);
        toolbar.setWrap(true);
        toolbar.setWidthFull();

        marcaGrid.setItems(query -> marcaService.list(toSpringPageRequest(query)).stream());
        marcaGrid.addColumn(Marca::getMarca).setHeader("Nome");
        marcaGrid.setEmptyStateText("Non ci sono marchi registrati");
        marcaGrid.setSizeFull();

        setSizeFull();

        add(toolbar, marcaGrid);
    }

    private void createMarca() {

        if (nomeMarca.getValue().isBlank()) {
            nomeMarca.setInvalid(true);
            nomeMarca.setErrorMessage("Nome richiesto");
            return;
        }

        String nome = nomeMarca.getValue();
        marcaService.createMarca(nomeMarca.getValue());
        marcaGrid.getDataProvider().refreshAll();
        nomeMarca.clear();
        Notification.show(nome + " aggiunta!", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.SUCCESS);
    }
}
