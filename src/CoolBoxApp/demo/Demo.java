package CoolBoxApp.demo;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.util.FontUtils;
import SantaCruzDept.modal.Drawer;

import CoolBoxApp.demo.menu.MyDrawerBuilder;
import CoolBoxApp.demo.system.FormManager;
import CoolBoxApp.demo.utils.DemoPreferences;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Demo extends JFrame {

    public static final String DEMO_VERSION = "2.0.0";

    public Demo() {
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        Drawer.installDrawer(this, MyDrawerBuilder.getInstance());
        FormManager.install(this);
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        DemoPreferences.init();
        FlatRobotoFont.install();
        FlatMacDarkLaf.setup();
        FlatLaf.registerCustomDefaultsSource("CoolBoxApp.demo.themes");
        UIManager.put("defaultFont", FontUtils.getCompositeFont(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        DemoPreferences.setupLaf();
        EventQueue.invokeLater(() -> new Demo().setVisible(true));
    }
}
