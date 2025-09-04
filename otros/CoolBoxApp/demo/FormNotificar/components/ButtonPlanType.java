package CoolBoxApp.demo.FormNotificar.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class ButtonPlanType extends JToggleButton {

    private final PlanType data;

    public ButtonPlanType(PlanType data) {
        this.data = data;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap 3", "[grow 0][fill][grow 0]", "[top]"));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;" +
                "selectedBackground:null;" +
                "pressedBackground:null;" +
                "borderWidth:1;");

        JRadioButton radioButton = new JRadioButton();

        radioButton.setModel(getModel());
        add(radioButton);

        // create text
        JPanel panel = new JPanel(new MigLayout("insets 0,nogrid"));
        JLabel lbName = new JLabel(data.getName());
        lbName.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold;");
        panel.add(lbName);
        JLabel lbRegion = new JLabel(data.getRegion());
        lbRegion.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:$TextField.background;" +
                "border:3,5,3,5,$Component.borderColor,1,15;");
        lbRegion.setIcon(new FlatSVGIcon("CoolboxApp/demo/icons/notificar/globe.svg", 0.9f));
        panel.add(lbRegion, "wrap");

        panel.add(new JLabel(data.getDescription()), "wrap");

        // create size
        if (data.getSize() == null) {
            // unlimited size
            JLabel lbLimit = new JLabel("Unlimited");
            lbLimit.setIcon(new FlatSVGIcon("CoolboxApp/demo/icons/notificar/infinity.svg", 0.9f));
            lbLimit.putClientProperty(FlatClientProperties.STYLE, "" +
                    "foreground:#22b65a");
            panel.add(lbLimit);
        } else {
            panel.add(new JLabel(data.getSize()));
        }
        JLabel lbSizeDescription = new JLabel(data.getSizeDescription());
        lbSizeDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        panel.add(lbSizeDescription);

        add(panel);


    }
}