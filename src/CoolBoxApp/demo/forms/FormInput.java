package CoolBoxApp.demo.forms;

import net.miginfocom.swing.MigLayout;
import CoolBoxApp.demo.system.Form;
import CoolBoxApp.demo.utils.SystemForm;

import javax.swing.*;

@SystemForm(name = "Form Input", description = "input form not yet update")
public class FormInput extends Form {

    public FormInput() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("al center center"));
        JLabel text = new JLabel("Input");
        add(text);
    }
}
