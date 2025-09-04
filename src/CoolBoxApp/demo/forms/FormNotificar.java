package CoolBoxApp.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import SantaCruzDept.component.ModalBorderAction;
import SantaCruzDept.component.SimpleModalBorder;
import raven.datetime.DatePicker;

import CoolBoxApp.demo.FormNotificar.components.ButtonPlanType;
import CoolBoxApp.demo.FormNotificar.components.PlanType;
import CoolBoxApp.demo.system.Form;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FormNotificar extends Form {
    
    private JFormattedTextField dateEditor;

    public FormNotificar() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets n 35 n 35", "[fill]"));

        add(createBoldText("ID del item"), "cell 0 0");
        add(createBoldText("Nombre del item"), " cell 2 0");

        JTextField txtID = new JTextField();
        txtID.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese el ID del item");
        add(txtID, "width 500,cell 0 1");

        add(new JLabel("/"), "cell 1 1");

        JTextField txtDatabase = new JTextField();
        txtDatabase.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Una vez ingresado el ID aca aprecera el nombre del item");
        add(txtDatabase, "cell 2 1,width 500");

        JLabel lbDataDescription = new JLabel("Tenga cuidado y revise bien el ID del item");
        lbDataDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground");
        add(lbDataDescription, "cell 0 2,gapy n 10");  
        
        
        
        
        
        add(createBoldText("Cantidad"), "cell 0 4");
        add(createBoldText("Fecha"), " cell 2 4");
        
         JTextField txtCantidad = new JTextField();
        txtCantidad.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese la cantidad a notificar");
        add(txtCantidad, "width 500,cell 0 5");

        add(new JLabel("/"), "cell 1 5");

        
        dateEditor = new JFormattedTextField();
        DatePicker datePicker = new DatePicker();
        datePicker.setEditor(dateEditor);
        
        add(dateEditor,"cell 2 5,width 500");
        
        JLabel lbDataDescription2 = new JLabel("Asegurese de ingresar correctamente la cantidad");
        lbDataDescription2.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground");
        add(lbDataDescription2, "cell 0 6,gapy n 10");  
        
        
        

        JComboBox comboRegion = new JComboBox();
        comboRegion.addItem("Lima centro");
        comboRegion.addItem("Lima Norte");
        comboRegion.addItem("Lima Sur");
        comboRegion.addItem("Villa el Salvador");
        comboRegion.addItem("San Isidro");

        comboRegion.setRenderer(new CustomComboRenderer(comboRegion));

        add(comboRegion, "span 3");

        JLabel lbRegionDescription = new JLabel("Escoja el almacen destinado a su tienda");
        lbRegionDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground");
        add(lbRegionDescription, "span 3,gapy n 10");

        add(createBoldText("Problemas mas frecuentes"));

        // create plan type
        ButtonGroup group = new ButtonGroup();
        List<PlanType> list = getListPlanType();
        for (int i = 0; i < list.size(); i++) {
            PlanType planType = list.get(i);
            ButtonPlanType buttonPlanType = new ButtonPlanType(planType);
            group.add(buttonPlanType);
            if (i == list.size() - 1) {
                add(buttonPlanType, "span 3,gapy 5 10");
            } else {
                add(buttonPlanType, "span 3, gapy 5");
            }
        }

        // create action button
        JButton cmdNext = new JButton("Notificar", new FlatSVGIcon("CoolboxApp/demo/icons/notificar/next.svg", 0.9f)) {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };

        cmdNext.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:#F0F0F0;");
        cmdNext.setHorizontalTextPosition(SwingConstants.LEADING);

        cmdNext.addActionListener(actionEvent -> {
            ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.OK_OPTION);
        });

        add(cmdNext, "grow 0, al trailing");
    }

    private List<PlanType> getListPlanType() {
        List<PlanType> list = new ArrayList<>();
        list.add(new PlanType("Producto Daniado", "Aca debera cambiar segun el almacen escojido", "bla bla bla", null, "bla bla bla", 48));
        list.add(new PlanType("Error de Recepcion", "Aca debera cambiar segun el almacen escojido", "bla bla bla", null, "bla bla bla", 28));
        list.add(new PlanType("Producto bajo de Stock", "Aca debera cambiar segun el almacen escojido", "bla bla bla", null, "bla bla bla", 0));
        return list;
    }

    private JLabel createBoldText(String text) {
        JLabel label = new JLabel(text);
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold;");
        return label;
    }

    private static class CustomComboRenderer extends DefaultListCellRenderer {

        private final JComboBox combo;
        private final JPanel panel;

        public CustomComboRenderer(JComboBox combo) {
            this.combo = combo;
            panel = new JPanel(new MigLayout("insets 0 1 0 1,fill", "[grow 0][grow 0][fill]", "[fill]"));
            JLabel lbRegion = new JLabel("Almacen");
            lbRegion.putClientProperty(FlatClientProperties.STYLE, "" +
                    "foreground:$Label.disabledForeground;");
            panel.add(lbRegion);
            panel.add(new JSeparator(JSeparator.VERTICAL));
        }

        @Override
        public Component getListCellRendererComponent(JList<?> jList, Object o, int i, boolean b, boolean b1) {
            Component com = super.getListCellRendererComponent(jList, o, i, b, b1);
            if (i == -1) {
                com.setBackground(combo.getBackground());
                if (panel.getComponentCount() > 2) {
                    panel.remove(2);
                }
                panel.add(com);
                return panel;
            } else {
                return com;
            }
        }
    }
}