package com.example.Calendario.ui;

import com.example.base.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "calendario", layout = MainLayout.class)
@PageTitle("Calendario")
public class CalendarioListView extends VerticalLayout {

    public CalendarioListView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
    }
}