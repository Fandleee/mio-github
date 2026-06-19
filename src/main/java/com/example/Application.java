package com.example;

import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;

/*La pagina Application.java permette di collegare la struttura di codice java
* e le view tramite @route. Vaadin Flow tiene sincronizzati oggetti Java lato
* server con elementi HTML lato client. Ultimamente il debug avviene
*  puntando alla classe main() che avvia Spring Boot*/


@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@StyleSheet("styles.css") // Your custom styles
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
