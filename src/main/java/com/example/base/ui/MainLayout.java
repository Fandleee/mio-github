package com.example.base.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        getElement().getStyle().set("height", "100%");
        addToDrawer(createApplicationHeader(), createApplicationDrawerContainer());
    }

    private Component createApplicationHeader() {
        Avatar avatar = new Avatar("Alessio");
        avatar.getStyle().set("margin-left", "10px");
        Span nome = new Span("Alessio");
        nome.getStyle().set("font-size", "18px");
        nome.getStyle().set("font-weight", "600");
        nome.getStyle().set("color", "#000000");
        nome.getStyle().set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(avatar, nome);
        header.setPadding(false);
        header.setMargin(false);
        header.setSpacing(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.getStyle().set("padding", "10px");
        header.getStyle().set("background-color", "#ffffff");

        return header;
    }

    private Component createApplicationDrawerContainer() {
        VerticalLayout wrapper = new VerticalLayout(createApplicationDrawer());
        wrapper.setPadding(false);
        wrapper.setMargin(false);
        wrapper.setSpacing(false);
        wrapper.setSizeFull();
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        return wrapper;
    }

    private Component createApplicationDrawer() {
        SideNav sideNav = createSideNav();

        Scroller scroller = new Scroller(sideNav);
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        scroller.setWidthFull();
        scroller.setHeight("800px");

        Component footer = createApplicationFooter();

        VerticalLayout drawerBox = new VerticalLayout(scroller, footer);
        drawerBox.setPadding(false);
        drawerBox.setSpacing(false);
        drawerBox.setMargin(false);
        drawerBox.setWidth("220px");
        drawerBox.setAlignItems(FlexComponent.Alignment.STRETCH);

        drawerBox.getStyle().set("background-color", "#e8ebef");
        drawerBox.getStyle().set("border-radius", "10px");
        drawerBox.getStyle().set("overflow", "hidden");

        return drawerBox;
    }

    private Component createApplicationFooter() {
        Image logo = new Image("icons/jsoft.png", "Logo");
        logo.setWidth("60px");
        logo.getStyle().set("margin", "0");

        Span testo = new Span("CARS");
        testo.getStyle().set("font-size", "28px");
        testo.getStyle().set("font-weight", "600");
        testo.getStyle().set("color", "#000000");
        testo.getStyle().set("margin", "0");
        testo.getStyle().set("line-height", "1");

        HorizontalLayout footer = new HorizontalLayout(logo, testo);
        footer.setPadding(false);
        footer.setSpacing(false);
        footer.setMargin(false);
        footer.setWidthFull();
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        footer.getStyle().set("gap", "6px");
        footer.getStyle().set("background-color", "e8ebef");
        footer.getStyle().set("padding-top", "8px");
        footer.getStyle().set("padding-bottom", "8px");

        return footer;
    }

    private SideNav createSideNav() {
        SideNav nav = new SideNav();
        nav.setMinWidth(100, Unit.PIXELS);
        MenuConfiguration.getMenuEntries()
                .forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        SideNavItem item = new SideNavItem("", menuEntry.path());
        item.setMatchNested(true);

        if (menuEntry.icon() != null) {
            Component icon;
            if (menuEntry.icon().contains(".svg")) {
                icon = new SvgIcon(menuEntry.icon());
            } else {
                icon = new Icon(menuEntry.icon());
            }
            item.setPrefixComponent(icon);
        }

        Span label = new Span(menuEntry.title());
        label.addClassName("nav-label");
        item.getElement().appendChild(label.getElement());

        return item;
    }
}