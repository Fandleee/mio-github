package com.example.dashboard.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "Dashboard")
@PageTitle("Dashboard")
@Menu(order = 0, icon = "icons/dashboard.svg", title = "Dashboard")
public class DashboardListView extends VerticalLayout {

    public DashboardListView() {

    }
}