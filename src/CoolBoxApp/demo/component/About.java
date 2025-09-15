package CoolBoxApp.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.LoggingFacade;
import net.miginfocom.swing.MigLayout;

import CoolBoxApp.demo.Demo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class About extends JPanel {

    public About() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 5 30 5 30,width 400", "[fill,330::]", ""));

        JTextPane title = createText("Proyecto Coolbox");
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +5");

        JTextPane description = createText("");
        description.setContentType("text/html");
        description.setText(getDescriptionText());
        description.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                showUrl(e.getURL());
            }
        });

        add(title);
        add(description);
        add(createSystemInformation());
    }

    private JTextPane createText(String text) {
        JTextPane textPane = new JTextPane();
        textPane.setBorder(BorderFactory.createEmptyBorder());
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
            }
        });
        return textPane;
    }

    private String getDescriptionText() {
       String text = "Este es un proyecto creado para la clase de " +
                "Pogramacion Orienta a objetos y Diseño de Patrones usando FlatLaf y <br>" +
                "FranquitoLibrary (actualizada a SantaCruzDept)<br>" +
                "<br>" +
                "Creada por el grupo 5, visita <a href=\"https://team-tafolio.vercel.app/\">El portafolio</a>";

        return text;
    }

    private String getSystemInformationText() {
        String text = "<b>Version Demo: </b>%s<br/>" +
                "<br>" +
                "<b>Grupo 5: </b>%s<br/>" +
                "<br>" +
                "<b>Profesor: </b>%s<br/>"+
                "<b>Profesor 2: </b>%s<br/>";

        return text;
    }

    private JComponent createSystemInformation() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Informacion del sistema"));
        JTextPane textPane = createText("");
        textPane.setContentType("text/html");
        String version = Demo.DEMO_VERSION;
        String Grupo = "<br>-Siesquen Santamaria Gian Franco<br>" +
                       "-Delgado Palomino Luis Francisco<br>" +
                       "-Farfan Mallqui Yefferson Adrian<br>" +
                       "-Vidal Figueroa Eytan Elias";
        String Profesor = "Herman Francisco Peña";
        String Profesor2 = "Miguel Angel Robledo Coveñas"; 

        String text = String.format(getSystemInformationText(),
                version,
                Grupo,
                Profesor,
                Profesor2);
        textPane.setText(text);
        panel.add(textPane);
        return panel;
    }

    private void showUrl(URL url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(url.toURI());
                } catch (IOException | URISyntaxException e) {
                    LoggingFacade.INSTANCE.logSevere("Error al navegar por la URL", e);
                }
            }
        }
    }
}