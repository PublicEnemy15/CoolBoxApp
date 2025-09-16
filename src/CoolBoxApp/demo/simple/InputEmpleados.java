package CoolBoxApp.demo.simple;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

import javax.swing.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public class InputEmpleados extends JPanel {
    
    // Constantes
    private static final String LAYOUT_CONSTRAINTS = "fillx,wrap,insets 5 30 5 30,width 400";
    private static final String COLUMN_CONSTRAINTS = "[fill]";
    private static final String ROW_CONSTRAINTS = "";
    
    // Patrones de validacion
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern SALARY_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]{2})?$");
    
    // Componentes del formulario
    private final EmployeeFormFields formFields;
    private final ComboBoxManager comboBoxManager;
    
    // Clases internas para organizacion
    private static class EmployeeFormFields {
        final JTextField primerNombreField = new JTextField();
        final JTextField segundoNombreField = new JTextField();
        final JTextField apellidoPaternoField = new JTextField();
        final JTextField apellidoMaternoField = new JTextField();
        final JTextField emailField = new JTextField();
        final JTextField numeroField = new JTextField();
        final JTextField salarioField = new JTextField();
        final JFormattedTextField fechaEditor = new JFormattedTextField();
        
        EmployeeFormFields() {
            setupPlaceholders();
            setupFieldValidation();
        }
        
        private void setupPlaceholders() {
            setPlaceholder(primerNombreField, "Primer nombre");
            setPlaceholder(segundoNombreField, "Segundo nombre (opcional)");
            setPlaceholder(apellidoPaternoField, "Apellido paterno");
            setPlaceholder(apellidoMaternoField, "Apellido materno");
            setPlaceholder(emailField, "ejemplo@correo.com");
            setPlaceholder(numeroField, "999999999");
            setPlaceholder(salarioField, "s/.0000.00");
        }
        
        private void setPlaceholder(JTextField field, String placeholder) {
            field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        }
        
        private void setupFieldValidation() {
            // Validacion en tiempo real para email
            emailField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    validateEmail();
                }
            });
            
            // Validacion para numero de telefono
            numeroField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    validatePhoneNumber();
                }
            });
            
            // Validacion para salario
            salarioField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    validateSalary();
                }
            });
        }
        
        private void validateEmail() {
            String email = emailField.getText().trim();
            if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
                showValidationError(emailField, "Formato de email invalido");
            } else {
                clearValidationError(emailField);
            }
        }
        
        private void validatePhoneNumber() {
            String phone = numeroField.getText().trim();
            if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
                showValidationError(numeroField, "El numero debe tener 9 digitos");
            } else {
                clearValidationError(numeroField);
            }
        }
        
        private void validateSalary() {
            String salary = salarioField.getText().trim();
            if (!salary.isEmpty() && !SALARY_PATTERN.matcher(salary).matches()) {
                showValidationError(salarioField, "Formato de salario invalido (ej: s/.1500.00)");
            } else {
                clearValidationError(salarioField);
            }
        }
        
        private void showValidationError(JTextField field, String message) {
            field.putClientProperty(FlatClientProperties.OUTLINE, "error");
            field.setToolTipText(message);
        }
        
        private void clearValidationError(JTextField field) {
            field.putClientProperty(FlatClientProperties.OUTLINE, null);
            field.setToolTipText(null);
        }
        
        // Metodos de validacion publicos
        public boolean isFormValid() {
            return isRequiredFieldsFilled() && 
                   isEmailValid() && 
                   isPhoneValid() && 
                   isSalaryValid();
        }
        
        private boolean isRequiredFieldsFilled() {
            return !primerNombreField.getText().trim().isEmpty() &&
                   !apellidoPaternoField.getText().trim().isEmpty() &&
                   !emailField.getText().trim().isEmpty() &&
                   !numeroField.getText().trim().isEmpty() &&
                   !salarioField.getText().trim().isEmpty();
        }
        
        private boolean isEmailValid() {
            String email = emailField.getText().trim();
            return email.isEmpty() || EMAIL_PATTERN.matcher(email).matches();
        }
        
        private boolean isPhoneValid() {
            String phone = numeroField.getText().trim();
            return phone.isEmpty() || PHONE_PATTERN.matcher(phone).matches();
        }
        
        private boolean isSalaryValid() {
            String salary = salarioField.getText().trim();
            return salary.isEmpty() || SALARY_PATTERN.matcher(salary).matches();
        }
    }
    
    private static class ComboBoxManager {
        private final JComboBox<String> comboAlmacen = new JComboBox<>();
        private final JComboBox<String> comboPosicion = new JComboBox<>();
        
        private static final String[] ALMACENES = {
            "Lima Sur", "Lima Centro", "Lima Norte"
        };
        
        private static final String[] POSICIONES = {
            "Gerente General",
            "Gerente de Operaciones", 
            "Supervisor de Almacen",
            "Jefe de Inventario",
        };
        
        ComboBoxManager() {
            initializeComboBoxes();
        }
        
        private void initializeComboBoxes() {
            populateComboBox(comboAlmacen, ALMACENES);
            populateComboBox(comboPosicion, POSICIONES);
            
            // Configurar estilos
            configureComboBoxStyle(comboAlmacen);
            configureComboBoxStyle(comboPosicion);
        }
        
        private void populateComboBox(JComboBox<String> combo, String[] items) {
            Arrays.stream(items).forEach(combo::addItem);
        }
        
        private void configureComboBoxStyle(JComboBox<String> combo) {
            combo.setMaximumRowCount(8); // Limitar filas visibles
        }
        
        public JComboBox<String> getComboAlmacen() { return comboAlmacen; }
        public JComboBox<String> getComboPosicion() { return comboPosicion; }
    }
    
    // Constructor principal
    public InputEmpleados() {
        this.formFields = new EmployeeFormFields();
        this.comboBoxManager = new ComboBoxManager();

        // Inicializaciones de compatibilidad
        txtFirstName = formFields.primerNombreField;
        txtSecondName = formFields.segundoNombreField;
        dateEditor = formFields.fechaEditor;

        initializeForm();
    }
    
    private void initializeForm() {
        setupLayout();
        buildEmployeeForm();
    }
    
    private void setupLayout() {
        setLayout(new MigLayout(LAYOUT_CONSTRAINTS, COLUMN_CONSTRAINTS, ROW_CONSTRAINTS));
    }
    
    private void buildEmployeeForm() {
        createTitle("Informacion del empleado");
        
        addNameSection();
        addContactSection();
        addPositionSection();
        addSalarySection();
        addDateSection();
        addLocationSection();
    }
    
    private void addNameSection() {
        add(new JLabel("Nombres"), "gapy 5 0");
        add(formFields.primerNombreField, "split 2");
        add(formFields.segundoNombreField);
        
        add(new JLabel("Apellidos"), "gapy 5 0");
        add(formFields.apellidoPaternoField, "split 2");
        add(formFields.apellidoMaternoField);
    }
    
    private void addContactSection() {
        add(new JLabel("Email"), "gapy 5 0");
        add(formFields.emailField);
        
        add(new JLabel("Numero de telefono"), "gapy 5 0");
        add(formFields.numeroField);
    }
    
    private void addPositionSection() {
        add(new JLabel("Posicion"), "gapy 5 0");
        add(comboBoxManager.getComboPosicion());
    }
    
    private void addSalarySection() {
        add(new JLabel("Salario"), "gapy 5 0");
        add(formFields.salarioField);
        
        // Informacion adicional sobre salario
        JLabel salaryInfo = createInfoLabel("Ingrese el salario mensual en soles (ej: s/.1500.00)");
        add(salaryInfo, "gapy 2 0");
    }
    
    private void addDateSection() {
        DatePicker datePicker = new DatePicker();
        datePicker.setEditor(formFields.fechaEditor);
        
        add(new JLabel("Fecha de registro del empleado"), "gapy 5 0");
        add(formFields.fechaEditor);
    }
    
    private void addLocationSection() {
        add(new JLabel("Almacén"), "gapy 5 0");
        add(comboBoxManager.getComboAlmacen());
    }
    
    private void createTitle(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        add(titleLabel, "gapy 5 0");
        
        JSeparator separator = new JSeparator();
        add(separator, "height 2!,gapy 0 0");
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel("<html><i>" + text + "</i></html>");
        label.putClientProperty(FlatClientProperties.STYLE, 
            "foreground:$Label.disabledForeground;font:-1");
        return label;
    }
    
    // Metodos publicos para validacion y acceso a datos
    public void formOpen() {
        formFields.primerNombreField.grabFocus();
    }
    
    public boolean validateForm() {
        if (!formFields.isFormValid()) {
            showValidationMessage();
            return false;
        }
        return true;
    }
    
    private void showValidationMessage() {
        JOptionPane.showMessageDialog(this,
            "Por favor, complete todos los campos obligatorios correctamente.",
            "Validación de formulario",
            JOptionPane.WARNING_MESSAGE);
    }
    
    // Getters para obtener los datos del formulario
    public String getPrimerNombre() {
        return formFields.primerNombreField.getText().trim();
    }
    
    public Optional<String> getSegundoNombre() {
        String segundoNombre = formFields.segundoNombreField.getText().trim();
        return segundoNombre.isEmpty() ? Optional.empty() : Optional.of(segundoNombre);
    }
    
    public String getApellidoPaterno() {
        return formFields.apellidoPaternoField.getText().trim();
    }
    
    public String getApellidoMaterno() {
        return formFields.apellidoMaternoField.getText().trim();
    }
    
    public String getEmail() {
        return formFields.emailField.getText().trim();
    }
    
    public String getNumeroTelefono() {
        return formFields.numeroField.getText().trim();
    }
    
    public String getPosicion() {
        return (String) comboBoxManager.getComboPosicion().getSelectedItem();
    }
    
    public String getSalario() {
        return formFields.salarioField.getText().trim();
    }
    
    public String getAlmacen() {
        return (String) comboBoxManager.getComboAlmacen().getSelectedItem();
    }
    
    public JFormattedTextField getFechaEditor() {
        return formFields.fechaEditor;
    }
    
    // Metodo para obtener el nombre completo
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder();
        nombreCompleto.append(getPrimerNombre());
        
        getSegundoNombre().ifPresent(segundo -> 
            nombreCompleto.append(" ").append(segundo));
        
        nombreCompleto.append(" ").append(getApellidoPaterno());
        nombreCompleto.append(" ").append(getApellidoMaterno());
        
        return nombreCompleto.toString();
    }
    
    // Metodo para limpiar el formulario
    public void clearForm() {
        formFields.primerNombreField.setText("");
        formFields.segundoNombreField.setText("");
        formFields.apellidoPaternoField.setText("");
        formFields.apellidoMaternoField.setText("");
        formFields.emailField.setText("");
        formFields.numeroField.setText("");
        formFields.salarioField.setText("");
        formFields.fechaEditor.setText("");
        
        comboBoxManager.getComboAlmacen().setSelectedIndex(0);
        comboBoxManager.getComboPosicion().setSelectedIndex(0);
        
        // Limpiar errores de validacion
        Arrays.asList(
            formFields.primerNombreField,
            formFields.segundoNombreField,
            formFields.apellidoPaternoField,
            formFields.apellidoMaternoField,
            formFields.emailField,
            formFields.numeroField,
            formFields.salarioField
        ).forEach(field -> {
            field.putClientProperty(FlatClientProperties.OUTLINE, null);
            field.setToolTipText(null);
        });
    }
    
    // Metodo para establecer datos en el formulario
    public void setEmployeeData(String primerNombre, String segundoNombre, 
                              String apellidoPaterno, String apellidoMaterno,
                              String email, String numero, String posicion, 
                              String salario, String almacen) {
        formFields.primerNombreField.setText(primerNombre);
        formFields.segundoNombreField.setText(segundoNombre != null ? segundoNombre : "");
        formFields.apellidoPaternoField.setText(apellidoPaterno);
        formFields.apellidoMaternoField.setText(apellidoMaterno);
        formFields.emailField.setText(email);
        formFields.numeroField.setText(numero);
        formFields.salarioField.setText(salario);
        
        comboBoxManager.getComboPosicion().setSelectedItem(posicion);
        comboBoxManager.getComboAlmacen().setSelectedItem(almacen);
    }
    
    // Campos de instancia para compatibilidad
    private JTextField txtFirstName;
    private JTextField txtSecondName;
    private JFormattedTextField dateEditor; 
    
}