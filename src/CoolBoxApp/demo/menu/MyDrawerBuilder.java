package CoolBoxApp.demo.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import SantaCruzDept.extras.AvatarIcon;
import SantaCruzDept.drawer.DrawerPanel;
import SantaCruzDept.drawer.item.Item;
import SantaCruzDept.drawer.item.MenuItem;
import SantaCruzDept.drawer.menu.MenuAction;
import SantaCruzDept.drawer.menu.MenuEvent;
import SantaCruzDept.drawer.menu.MenuOption;
import SantaCruzDept.drawer.menu.MenuStyle;
import SantaCruzDept.drawer.renderer.DrawerStraightDotLineStyle;
import SantaCruzDept.drawer.simple.SimpleDrawerBuilder;
import SantaCruzDept.drawer.simple.footer.LightDarkButtonFooter;
import SantaCruzDept.drawer.simple.footer.SimpleFooterData;
import SantaCruzDept.drawer.simple.header.SimpleHeader;
import SantaCruzDept.drawer.simple.header.SimpleHeaderData;
import SantaCruzDept.option.Option;

import CoolBoxApp.demo.Demo;
import CoolBoxApp.demo.forms.*;
import CoolBoxApp.demo.model.ModelUser;
import CoolBoxApp.demo.system.AllForms;
import CoolBoxApp.demo.system.Form;
import CoolBoxApp.demo.system.FormManager;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MyDrawerBuilder extends SimpleDrawerBuilder {

    private static MyDrawerBuilder instance;
    private ModelUser user;

    public static MyDrawerBuilder getInstance() {
        if (instance == null) {
            instance = new MyDrawerBuilder();
        }
        return instance;
    }

    public ModelUser getUser() {
        return user;
    }

    public void setUser(ModelUser user) {
        boolean updateMenuItem = this.user == null || this.user.getRole() != user.getRole();

        this.user = user;

        // set user to menu validation
        MyMenuValidation.setUser(user);

        // setup drawer header
        SimpleHeader header = (SimpleHeader) getHeader();
        SimpleHeaderData data = header.getSimpleHeaderData();
        AvatarIcon icon = (AvatarIcon) data.getIcon();
        String iconName = user.getRole() == ModelUser.Role.ADMIN ? "avatar_male.svg" : "avatar_female.svg";

        icon.setIcon(new FlatSVGIcon("CoolBoxApp/demo/drawer/image/" + iconName, 100, 100));
        data.setTitle(user.getUserName());
        data.setDescription(user.getMail());
        header.setSimpleHeaderData(data);

        if (updateMenuItem) {
            rebuildMenu();
        }
    }

    private final int SHADOW_SIZE = 12;

    private MyDrawerBuilder() {
        super(createSimpleMenuOption());
        LightDarkButtonFooter lightDarkButtonFooter = (LightDarkButtonFooter) getFooter();
        lightDarkButtonFooter.addModeChangeListener(isDarkMode -> {
            // event for light dark mode changed
        });
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon = new AvatarIcon(new FlatSVGIcon("CoolBoxApp/demo/drawer/image/avatar_male.svg", 100, 100), 50, 50, 3.5f);
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);

        changeAvatarIconBorderColor(icon);

        UIManager.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("lookAndFeel")) {
                changeAvatarIconBorderColor(icon);
            }
        });

        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle("Franco")
                .setDescription("Franco@gmail.com");
    }

    private void changeAvatarIconBorderColor(AvatarIcon icon) {
        icon.setBorderColor(new AvatarIcon.BorderColor(UIManager.getColor("Component.accentColor"), 0.7f));
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("CoolBoxApp")
                .setDescription("Version " + Demo.DEMO_VERSION);
    }

    @Override
    public Option createOption() {
        Option option = super.createOption();
        option.setOpacity(0.3f);
        option.getBorderOption()
                .setShadowSize(new Insets(0, 0, 0, SHADOW_SIZE));
        return option;
    }

    public static MenuOption createSimpleMenuOption() {

        // create simple menu option
        MenuOption simpleMenuOption = new MenuOption();

        MenuItem items[] = new MenuItem[]{
                new Item.Label("MAIN"),
                new Item("Dashboard", "dashboard.svg", FormDashboard.class),
                new Item.Label("GESTION"),
                new Item("Administrar", "forms.svg")
                        .subMenu("Administrar Empleados", FormTableEmpleados.class)
                        .subMenu("Administrar Productos", FormTablaProductos.class)
                        .subMenu("Notificar Item de Almacen", FormNotificar.class),
                
                new Item("Area de ventas", "components.svg")
                        .subMenu("Ingreso Ventas", FormInput.class)
                        .subMenu("Vistas Ventas", FormInput.class)
                        .subMenu("Lista de reportes", FormInput.class)
                        .subMenu("No definido", FormInput.class)
                        .subMenu("No definido", FormInput.class)
                        .subMenu("No definido", FormInput.class),
                new Item("No definido", "undefined.svg")
                        .subMenu("No definido")
                        .subMenu(
                                new Item("No definido")
                                        .subMenu("No definido")
                                        .subMenu("No definido")
                                        .subMenu(
                                                new Item("No definido")
                                                        .subMenu("No definido")
                                                        .subMenu("No definido")
                                                        .subMenu("No definido")
                                                        .subMenu("No definido")
                                                        .subMenu("No definido")
                                                        .subMenu("No definido")
                                        )
                                        .subMenu("No definido")
                                        .subMenu("No definido")
                                        .subMenu("No definido")
                        )
                        .subMenu("No definido"),
                new Item("No definido", "undefined.svg"),
                new Item("No definido", "undefined.svg"),
                new Item.Label("OTROS"),
                new Item("No definido", "undefined.svg")
                        .subMenu("No definido")
                        .subMenu("No definido")
                        .subMenu("No definido"),
                new Item("Ajustes", "setting.svg", FormAjustes.class),
                new Item("About", "about.svg"),
                new Item("Logout", "logout.svg")
        };

        simpleMenuOption.setMenuStyle(new MenuStyle() {

            @Override
            public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
                boolean isTopLevel = index.length == 1;
                if (isTopLevel) {
                    // adjust item menu at the top level because it's contain icon
                    menu.putClientProperty(FlatClientProperties.STYLE, "" +
                            "margin:-1,0,-1,0;");
                }
            }

            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
            }
        });

        simpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
        simpleMenuOption.setMenuValidation(new MyMenuValidation());

        simpleMenuOption.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(MenuAction action, int[] index) {
                System.out.println("Drawer menu selected " + Arrays.toString(index));
                Class<?> itemClass = action.getItem().getItemClass();
                int i = index[0];
                if (i == 8) {
                    action.consume();
                    FormManager.showAbout();
                    return;
                } else if (i == 9) {
                    action.consume();
                    FormManager.logout();
                    return;
                }
                if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                    action.consume();
                    return;
                }
                Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
                FormManager.showForm(AllForms.getForm(formClass));
            }
        });

        simpleMenuOption.setMenus(items)
                .setBaseIconPath("CoolBoxApp/demo/drawer/icon")
                .setIconScale(0.45f);

        return simpleMenuOption;
    }

    @Override
    public int getDrawerWidth() {
        return 270 + SHADOW_SIZE;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80 + SHADOW_SIZE;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
    }

    private static String getDrawerBackgroundStyle() {
        return "" +
                "[light]background:tint($Panel.background,20%);" +
                "[dark]background:tint($Panel.background,5%);";
    }
}
