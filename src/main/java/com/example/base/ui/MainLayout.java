package com.example.base.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

@Layout
public final class MainLayout extends AppLayout {

    private static final String EXPANDED_WIDTH = "240px";
    private static final String COLLAPSED_WIDTH = "88px";

    private boolean collapsed = false;

    private HorizontalLayout headerBox;
    private VerticalLayout drawerBox;
    private VerticalLayout footerBox;
    private Button collapseButton;

    private Div lightOption;
    private Div darkOption;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addClassName("app-shell");
        getElement().getStyle().set("height", "100%");
        addToDrawer(createDrawerContent());
        syncThemeToggle();
    }

    @Override
    public void setContent(Component content) {
        Div note = new Div("©J-Software");
        note.addClassName("bottom-right-note");
        content.getElement().appendChild(note.getElement());
        super.setContent(content);
    }

    private Component createDrawerContent() {
        headerBox = createApplicationHeader();
        Component drawerContainer = createApplicationDrawerContainer();

        VerticalLayout root = new VerticalLayout(headerBox, drawerContainer);
        root.addClassName("drawer-root");
        root.setPadding(false);
        root.setSpacing(false);
        root.setMargin(false);
        root.setAlignItems(FlexComponent.Alignment.CENTER);
        root.setWidthFull();
        root.setHeightFull();

        root.setFlexGrow(0, headerBox);
        root.setFlexGrow(1, drawerContainer);

        return root;
    }

    private HorizontalLayout createApplicationHeader() {
        Avatar avatar = new Avatar("Alessio");
        avatar.addClassName("drawer-avatar");

        Span nome = new Span("Alessio");
        nome.addClassName("drawer-header-name");

        ContextMenu userMenu = new ContextMenu();
        userMenu.setTarget(avatar);
        userMenu.setOpenOnClick(true);

        HorizontalLayout header = new HorizontalLayout(avatar, nome);
        header.addClassName("drawer-header");
        header.setPadding(false);
        header.setMargin(false);
        header.setSpacing(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        header.setWidth(EXPANDED_WIDTH);

        return header;
    }

    private Component createApplicationDrawerContainer() {
        SideNav sideNav = createSideNav();

        Scroller scroller = new Scroller(sideNav);
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        scroller.setWidthFull();
        scroller.setHeightFull();

        footerBox = createApplicationFooter();

        drawerBox = new VerticalLayout();
        drawerBox.addClassName("drawer-box");
        drawerBox.setPadding(false);
        drawerBox.setSpacing(false);
        drawerBox.setMargin(false);
        drawerBox.setAlignItems(FlexComponent.Alignment.STRETCH);
        drawerBox.setWidth(EXPANDED_WIDTH);
        drawerBox.setHeightFull();

        drawerBox.add(scroller, footerBox);
        drawerBox.setFlexGrow(1, scroller);
        drawerBox.setFlexGrow(0, footerBox);

        collapseButton = new Button("❮");
        collapseButton.addClassName("drawer-collapse-button");
        collapseButton.addClickListener(event -> toggleDrawer());

        Div container = new Div(drawerBox, collapseButton);
        container.addClassName("drawer-container");
        container.setHeightFull();

        return container;
    }

    private VerticalLayout createApplicationFooter() {
        Component themeToggle = createThemeToggle();

        Image logo = new Image("icons/jsoft.png", "Logo");
        logo.addClassName("drawer-footer-logo");
        logo.setWidth("56px");

        Span text = new Span("CARS");
        text.addClassName("drawer-footer-text");

        HorizontalLayout brandRow = new HorizontalLayout(logo, text);
        brandRow.addClassName("drawer-footer-brand-row");
        brandRow.setPadding(false);
        brandRow.setSpacing(false);
        brandRow.setMargin(false);
        brandRow.setWidthFull();
        brandRow.setAlignItems(FlexComponent.Alignment.CENTER);
        brandRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout footer = new VerticalLayout(themeToggle, brandRow);
        footer.addClassName("drawer-footer");
        footer.setPadding(false);
        footer.setSpacing(false);
        footer.setMargin(false);
        footer.setWidth(EXPANDED_WIDTH);
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return footer;
    }

    private Component createThemeToggle() {
        Image sunIcon = new Image("icons/sun-2.svg", "Light mode");
        sunIcon.addClassName("theme-icon");

        Image moonIcon = new Image("icons/moon.svg", "Dark mode");
        moonIcon.addClassName("theme-icon");

        lightOption = new Div(sunIcon);
        lightOption.addClassName("theme-option");
        lightOption.addClickListener(event -> setDarkMode(false));

        darkOption = new Div(moonIcon);
        darkOption.addClassName("theme-option");
        darkOption.addClickListener(event -> setDarkMode(true));

        Div toggle = new Div(lightOption, darkOption);
        toggle.addClassName("theme-toggle");

        return toggle;
    }

    private boolean isDarkMode = false;  // ← metti come campo della classe in cima, con gli altri campi

    private void setDarkMode(boolean dark) {
        if (UI.getCurrent() == null) return;

        isDarkMode = dark;

        if (dark) {
            UI.getCurrent().getPage().executeJs(
                    "document.documentElement.setAttribute('theme', 'dark')"
            );
        } else {
            UI.getCurrent().getPage().executeJs(
                    "document.documentElement.removeAttribute('theme')"
            );
        }

        syncThemeToggle();
    }

    private void syncThemeToggle() {
        if (lightOption == null || darkOption == null) return;

        if (isDarkMode) {
            darkOption.addClassName("theme-option-active");
            lightOption.removeClassName("theme-option-active");
        } else {
            lightOption.addClassName("theme-option-active");
            darkOption.removeClassName("theme-option-active");
        }
    }

    private SideNav createSideNav() {
        SideNav nav = new SideNav();
        nav.setMinWidth(80, Unit.PIXELS);
        nav.addClassName("app-side-nav");
        
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

    private void toggleDrawer() {
        collapsed = !collapsed;

        if (collapsed) {
            addClassName("drawer-collapsed");
            collapseButton.setText("❯");
            headerBox.setWidth(COLLAPSED_WIDTH);
            headerBox.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            drawerBox.setWidth(COLLAPSED_WIDTH);
            footerBox.setWidth(COLLAPSED_WIDTH);
        } else {
            removeClassName("drawer-collapsed");
            collapseButton.setText("❮");
            headerBox.setWidth(EXPANDED_WIDTH);
            headerBox.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
            drawerBox.setWidth(EXPANDED_WIDTH);
            footerBox.setWidth(EXPANDED_WIDTH);
        }
    }
}