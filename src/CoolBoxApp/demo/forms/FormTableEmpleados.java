package CoolBoxApp.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import SantaCruzDept.modal.ModalDialog;
import SantaCruzDept.component.SimpleModalBorder;
import SantaCruzDept.option.Location;
import SantaCruzDept.option.Option;

import CoolBoxApp.demo.model.ModelEmployee;
import CoolBoxApp.demo.model.ModelProfile;
import CoolBoxApp.demo.sample.SampleData;
import CoolBoxApp.demo.simple.InputEmpleados;
import CoolBoxApp.demo.system.Form;
import CoolBoxApp.demo.utils.SystemForm;
import CoolBoxApp.demo.utils.table.CheckBoxTableHeaderRenderer;
import CoolBoxApp.demo.utils.table.TableHeaderAlignment;
import CoolBoxApp.demo.utils.table.TableProfileCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SystemForm(name = "Tabla de empleados", description = "Area de administracion de empleados", tags = {"list"})
public class FormTableEmpleados extends Form {

    public FormTableEmpleados() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 7 15 7 15", "[fill]", "[][fill,grow]"));
        add(createInfo("Tabla para la administracion de empleados", "Fwaeh", 1));
        add(createCustomTable(), "gapx 7 7");
    }
    
        private JPanel createInfo(String title, String description, int level) {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel lbTitle = new JLabel(title);
        JTextPane text = new JTextPane();
        text.setText(description);
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +" + (4 - level));
        panel.add(lbTitle);
        panel.add(text, "width 500");
        return panel;
    }
        
    private Component createCustomTable() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 10 0 10 0", "[fill]", "[][][]0[fill,grow]"));

        // columnas
        Object columns[] = new Object[]{
            "SELECT", "#", "NOMBRE", "FECHA", "SALARIO", "POSICION", "EMAIL", "TELEFONO"
        };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // solo checkbox editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;  // checkbox
                if (columnIndex == 2) return ModelProfile.class; // profile
                return super.getColumnClass(columnIndex);
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // tamanios de columnas
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(250);
        table.getColumnModel().getColumn(7).setPreferredWidth(150);

        // configuracion de tabla
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultRenderer(ModelProfile.class, new TableProfileCellRenderer(table));
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));

        // alineacion cabecera
        table.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(table) {
            @Override
            protected int getAlignment(int column) {
                return (column == 1) ? SwingConstants.CENTER : SwingConstants.LEADING;
            }
        });

        // estilos
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:$Table.background;");
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "height:30;hoverBackground:null;pressedBackground:null;separatorColor:$TableHeader.background;");
        table.putClientProperty(FlatClientProperties.STYLE, "rowHeight:70;showHorizontalLines:true;intercellSpacing:0,1;cellFocusColor:$TableHeader.hoverBackground;selectionBackground:$TableHeader.hoverBackground;selectionInactiveBackground:$TableHeader.hoverBackground;selectionForeground:$Table.foreground;");
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "trackArc:$ScrollBar.thumbArc;trackInsets:3,3,3,3;thumbInsets:3,3,3,3;background:$Table.background;");

        // titulo
        JLabel title = new JLabel("Area de empleados");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(title, "gapx 20");

        // barra de acciones
        panel.add(createHeaderAction());
        
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "" +  "foreground:$Table.gridColor;");
        panel.add(separator, "height 2");

        // tabla
        panel.add(scrollPane);

        // datos de ejemplo
        for (ModelEmployee d : SampleData.getSampleEmployeeData(false)) {
            model.addRow(d.toTableRowCustom(table.getRowCount() + 1));
        }
        return panel;
    }

    private Component createHeaderAction() {
        JPanel panel = new JPanel(new MigLayout("insets 5 20 5 20", "[fill,230]push[][]"));

        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("CoolBoxApp/demo/icons/search.svg", 0.4f));
        JButton cmdCreate = new JButton("Registrar");
        JButton cmdEdit = new JButton("Editar");
        JButton cmdDelete = new JButton("Eliminar");

        cmdCreate.addActionListener(e -> showModal());
        panel.add(txtSearch);
        panel.add(cmdCreate);
        panel.add(cmdEdit);
        panel.add(cmdDelete);

        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        return panel;
    }

    private void showModal() {
        Option option = ModalDialog.createOption();
        option.getLayoutOption().setSize(-1, 1f)
                .setLocation(Location.TRAILING, Location.TOP)
                .setAnimateDistance(0.7f, 0);
        ModalDialog.showModal(this, new SimpleModalBorder(
                new InputEmpleados(), "Registro de Empleados", SimpleModalBorder.YES_NO_OPTION,
                (controller, action) -> {}), option);
    }
}
