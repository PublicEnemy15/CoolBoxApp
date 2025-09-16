package CoolBoxApp.demo.auth;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import SantaCruzDept.component.DropShadowBorder;

import CoolBoxApp.demo.component.LabelButton;
import CoolBoxApp.demo.menu.MyDrawerBuilder;
import CoolBoxApp.demo.model.ModelUser;
import CoolBoxApp.demo.system.Form;
import CoolBoxApp.demo.system.FormManager;

import javax.swing.*;
import java.awt.*;

public class Login extends Form {

    public Login() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("al center center"));
        createLogin();
    }

    private void createLogin() {
        JPanel panelLogin = new JPanel(new BorderLayout()) {
            @Override
            public void updateUI() {
                super.updateUI();
                applyShadowBorder(this);
            }
        };
        panelLogin.setOpaque(false);
        applyShadowBorder(panelLogin);

        JPanel loginContent = new JPanel(new MigLayout("fillx,wrap,insets 35 35 25 35", "[fill,300]"));

        ImageIcon icono = new ImageIcon(getClass().getResource("/CoolBoxApp/demo/login/logo.png"));
        JLabel lbTitle = new JLabel("", icono, JLabel.CENTER);
        JLabel lbDescription = new JLabel("Inicie sesión para acceder a su cuenta", JLabel.CENTER);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +12;");

        loginContent.add(lbTitle);
        loginContent.add(lbDescription);

        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JCheckBox chRememberMe = new JCheckBox("Recuerdame");
        JButton cmdLogin = new JButton("Login") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };

        // Estilos
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu usuario");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu contraseña");

        panelLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[dark]background:tint($Panel.background,1%);");

        loginContent.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");

        txtUsername.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:4,10,4,10;" +
                "arc:12;");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:4,10,4,10;" +
                "arc:12;" +
                "showRevealButton:true;");

        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:4,10,4,10;" +
                "arc:12;");

        loginContent.add(new JLabel("Usuario"), "gapy 25");
        loginContent.add(txtUsername);

        loginContent.add(new JLabel("Contraseña"), "gapy 10");
        loginContent.add(txtPassword);
        loginContent.add(chRememberMe);
        loginContent.add(cmdLogin, "gapy 20");
        loginContent.add(createInfo());

        panelLogin.add(loginContent);
        add(panelLogin);

        // eventos
        cmdLogin.addActionListener(e -> {
            String userName = txtUsername.getText();
            String password = String.valueOf(txtPassword.getPassword());
            ModelUser user = getUser(userName, password);
            MyDrawerBuilder.getInstance().setUser(user);
            FormManager.login();
        });
    }

    private JPanel createInfo() {
        JPanel panelInfo = new JPanel(new MigLayout("wrap,al center", "[center]"));
        panelInfo.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");

        panelInfo.add(new JLabel("No recuerda su cuenta?"));
        panelInfo.add(new JLabel("Contactar a"), "split 2");
        LabelButton lbLink = new LabelButton("Yefferson@info.com");

        panelInfo.add(lbLink);

        // eventos
        lbLink.addOnClick(e -> {

        });
        return panelInfo;
    }

    private void applyShadowBorder(JPanel panel) {
        if (panel != null) {
            panel.setBorder(new DropShadowBorder(new Insets(5, 8, 12, 8), 1, 25));
        }
    }

    private ModelUser getUser(String user, String password) {

        // Solo para probar.
        // Ingrese cualquier usuario y la contrasenia predeterminada es admin.
        // usuario='personal' contraseña='123' si queremos probar el menú de validación para el rol personal.

        if (user.equals("staff") && password.equals("123")) {
            return new ModelUser("Yefferson Adrian", "Yefferson@gmail.com", ModelUser.Role.STAFF);
        }
        return new ModelUser("Franco", "Franco@gmail.com", ModelUser.Role.ADMIN);
    }
}
