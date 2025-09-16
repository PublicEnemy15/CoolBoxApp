package CoolBoxApp.demo.simple;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import SantaCruzDept.component.ModalBorderAction;
import SantaCruzDept.component.SimpleModalBorder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import javaswingdev.picturebox.PictureBox;

public class InputProductos extends JPanel {
    
    // Constantes
    private static final String[] SUPPORTED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png"};
    private static final Dimension IMAGE_PANEL_SIZE = new Dimension(350, 200);
    private static final Dimension BUTTON_SIZE = new Dimension(120, 30);
    private static final int TEXTAREA_HEIGHT = 150;
    private static final int SCROLL_UNIT_INCREMENT = 16;
    
    // Predicados funcionales
    private static final Predicate<File> IS_IMAGE_FILE = file -> {
        String name = file.getName().toLowerCase();
        return Arrays.stream(SUPPORTED_IMAGE_EXTENSIONS)
                .anyMatch(name::endsWith);
    };
    
    // Componentes de UI
    private final FormFields formFields;
    private final ImageManager imageManager;
    private final ComboBoxManager comboBoxManager;
    
    // Clases internas 
    private static class FormFields {
        final JTextField nombreField = new JTextField();
        final JTextField idField = new JTextField();
        final JTextField precioField = new JTextField();
        final JTextField cantidadField = new JTextField();
        final JTextField cantMaxField = new JTextField();
        final JTextField cantMinField = new JTextField();
        final JFormattedTextField dateEditor = new JFormattedTextField();
        final JTextArea descripcionArea = new JTextArea();
        
        FormFields() {
            setupPlaceholders();
            setupTextArea();
        }
        
        private void setupPlaceholders() {
            setPlaceholder(nombreField, "Nombre");
            setPlaceholder(idField, "ID");
            setPlaceholder(precioField, "Precio");
            setPlaceholder(cantidadField, "Cantidad");
            setPlaceholder(cantMaxField, "Cantidad Maxima");
            setPlaceholder(cantMinField, "Cantidad Minima");
        }
        
        private void setupTextArea() {
            descripcionArea.setWrapStyleWord(true);
            descripcionArea.setLineWrap(true);
        }
        
        private void setPlaceholder(JTextField field, String placeholder) {
            field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        }
    }
    
    private static class ImageManager {
        private final PictureBox pictureBox = new PictureBox();
        private final JButton btnBuscar = new JButton("Buscar Imagen");
        private final JButton btnBorrar = new JButton("Borrar Imagen");
        private final JPanel imagePanel = new JPanel(new BorderLayout());
        private final List<File> imageFiles = new ArrayList<>();
        private int currentImageIndex = -1;
        
        ImageManager() {
            setupImagePanel();
            setupButtons();
        }
        
        private void setupImagePanel() {
            imagePanel.setPreferredSize(IMAGE_PANEL_SIZE);
            imagePanel.putClientProperty(FlatClientProperties.STYLE,
                "border:1,1,1,1,$Component.borderColor,,10");
            imagePanel.add(pictureBox, BorderLayout.CENTER);
            pictureBox.setImage(null);
        }
        
        private void setupButtons() {
            configurarBoton(btnBuscar, this::buscarImagen);
            configurarBoton(btnBorrar, this::borrarImagen);
            btnBorrar.setEnabled(false);
        }
        
        private void configurarBoton(JButton button, Runnable action) {
            button.setPreferredSize(BUTTON_SIZE);
            button.addActionListener(e -> action.run());
        }
        
        private void buscarImagen() {
            Optional<File> selectedFile = showFileChooser();
            selectedFile.ifPresent(this::processSelectedImage);
        }
        
        private Optional<File> showFileChooser() {
            JFileChooser fileChooser = createFileChooser();
            return fileChooser.showOpenDialog(imagePanel) == JFileChooser.APPROVE_OPTION
                ? Optional.of(fileChooser.getSelectedFile())
                : Optional.empty();
        }
        
        private JFileChooser createFileChooser() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imagen");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Archivos de Imagen", SUPPORTED_IMAGE_EXTENSIONS));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            return fileChooser;
        }
        
        private void processSelectedImage(File selectedFile) {
            if (!selectedFile.exists() || !IS_IMAGE_FILE.test(selectedFile)) {
                showErrorMessage("El archivo seleccionado no es una imagen valida.");
                return;
            }
            
            try {
                addImageFile(selectedFile);
                displayCurrentImage();
                btnBorrar.setEnabled(true);
            } catch (Exception ex) {
                showErrorMessage("Error al cargar la imagen: " + ex.getMessage());
            }
        }
        
        private void addImageFile(File file) {
            if (!imageFiles.contains(file)) {
                imageFiles.add(file);
                currentImageIndex = imageFiles.size() - 1;
            } else {
                currentImageIndex = imageFiles.indexOf(file);
            }
        }
        
        private void displayCurrentImage() {
            getCurrentImageFile().ifPresent(file -> {
                ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
                pictureBox.setImage(imageIcon);
                imagePanel.repaint();
            });
        }
        
        private void borrarImagen() {
            if (!isValidImageIndex()) return;
            
            if (confirmDeletion()) {
                removeCurrentImage();
                updateImageDisplay();
            }
        }
        
        private boolean isValidImageIndex() {
            return currentImageIndex >= 0 && currentImageIndex < imageFiles.size();
        }
        
        private boolean confirmDeletion() {
            return JOptionPane.showConfirmDialog(imagePanel,
                "Esta seguro de que desea borrar esta imagen?",
                "Confirmar borrado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
        }
        
        private void removeCurrentImage() {
            imageFiles.remove(currentImageIndex);
            adjustCurrentIndex();
        }
        
        private void adjustCurrentIndex() {
            if (imageFiles.isEmpty()) {
                currentImageIndex = -1;
            } else if (currentImageIndex >= imageFiles.size()) {
                currentImageIndex = imageFiles.size() - 1;
            }
        }
        
        private void updateImageDisplay() {
            if (currentImageIndex == -1) {
                pictureBox.setImage(null);
                btnBorrar.setEnabled(false);
            } else {
                displayCurrentImage();
            }
            imagePanel.repaint();
        }
        
        private void showErrorMessage(String message) {
            JOptionPane.showMessageDialog(imagePanel, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Metodos publicos
        public List<File> getImageFiles() {
            return new ArrayList<>(imageFiles);
        }
        
        public Optional<File> getCurrentImageFile() {
            return isValidImageIndex() 
                ? Optional.of(imageFiles.get(currentImageIndex))
                : Optional.empty();
        }
        
        public JPanel getImagePanel() { return imagePanel; }
        public JButton getBtnBuscar() { return btnBuscar; }
        public JButton getBtnBorrar() { return btnBorrar; }
    }
    
    private static class ComboBoxManager {
        private final JComboBox<String> comboAlmacen = new JComboBox<>();
        private final JComboBox<String> comboCategory = new JComboBox<>();
        
        private static final String[] ALMACENES = {"Lima Sur", "Lima Centro", "Lima Norte"};
        private static final String[] CATEGORIAS = {"Electronica", "Accesorios", "Videojuegos", "Hogar", "Oficina"};
        
        ComboBoxManager() {
            initializeComboBoxes();
        }
        
        private void initializeComboBoxes() {
            populateComboBox(comboAlmacen, ALMACENES);
            populateComboBox(comboCategory, CATEGORIAS);
        }
        
        private void populateComboBox(JComboBox<String> combo, String[] items) {
            Arrays.stream(items).forEach(combo::addItem);
        }
        
        public JComboBox<String> getComboAlmacen() { return comboAlmacen; }
        public JComboBox<String> getComboCategory() { return comboCategory; }
    }
    
    // Constructor y metodos principales
    public InputProductos() {
        this.formFields = new FormFields();
        this.imageManager = new ImageManager();
        this.comboBoxManager = new ComboBoxManager();
        initializeForm();
    }
    
    private void initializeForm() {
        JPanel mainPanel = createMainPanel();
        setupScrollPane(mainPanel);
        buildForm(mainPanel);
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 5 30 5 30,width 400", "[fill]", ""));
        return panel;
    }
    
    private void setupScrollPane(JPanel mainPanel) {
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        configureScrollPane(scrollPane);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void configureScrollPane(JScrollPane scrollPane) {
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INCREMENT);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
    }
    
    private void buildForm(JPanel mainPanel) {
        addItemInformationSection(mainPanel);
        addSpecificInformationSection(mainPanel);
        addImageSection(mainPanel);
    }
    
    private void addItemInformationSection(JPanel panel) {
        createTitle(panel, "Registrar informacion del item");
        
        // Primera fila: Nombre e ID
        panel.add(formFields.nombreField, "split 2, gapy 5 0");
        panel.add(formFields.idField);
        
        // Segunda fila: Precio y Cantidad
        panel.add(formFields.precioField, "gapy 10 0, split 2");
        panel.add(formFields.cantidadField);
        
        // Categoria
        panel.add(new JLabel("Categoría"), "gapy 5 0");
        panel.add(comboBoxManager.getComboCategory());
        
        // Fecha
        DatePicker datePicker = new DatePicker();
        datePicker.setEditor(formFields.dateEditor);
        panel.add(new JLabel("Fecha de registro del item"), "gapy 5 0");
        panel.add(formFields.dateEditor);
        
        // Cantidades min/max
        panel.add(new JLabel("Cantidad de items (min/max)"), "gapy 5 0");
        panel.add(formFields.cantMaxField, "split 2");
        panel.add(formFields.cantMinField);
        
        // Descripcion informativa
        JLabel description = createDescriptionLabel(
            "La cantidad max cambiara según lo registrado en el campo,<br>" +
            "la cantidad min es 10 items, se cambiara automáticamente");
        panel.add(description, "gapy 5 0");
        
        // Almacen
        panel.add(new JLabel("Almacen"), "gapy 5 0");
        panel.add(comboBoxManager.getComboAlmacen());
    }
    
    private void addSpecificInformationSection(JPanel panel) {
        createTitle(panel, "Informacion especifica del item");
        
        panel.add(new JLabel("Descripcion del item"), "gapy 5 0");
        JScrollPane scroll = new JScrollPane(formFields.descripcionArea);
        panel.add(scroll, "height " + TEXTAREA_HEIGHT + ",grow,pushy");
        
        // Agregar listener funcional para Ctrl+Enter
        formFields.descripcionArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                handleControlEnter(e);
            }
        });
    }
    
    private void handleControlEnter(KeyEvent e) {
        if (e.isControlDown() && e.getKeyChar() == 10) {
            Optional.ofNullable(ModalBorderAction.getModalBorderAction(this))
                .ifPresent(action -> action.doAction(SimpleModalBorder.YES_OPTION));
        }
    }
    
    private void addImageSection(JPanel panel) {
        createTitle(panel, "Imagen del item");
        
        // Panel de imagen
        panel.add(imageManager.getImagePanel(), "height 200!,gapy 5 0, growx");
        
        // Panel de botones
        JPanel buttonPanel = createImageButtonPanel();
        panel.add(buttonPanel, "gapy 5 0, growx");
        
        // Informacion
        JLabel info = createDescriptionLabel(
            "Selecciona una imagen para el item.<br>Formatos soportados: JPG, PNG");
        panel.add(info, "gapy 5 0");
    }
    
    private JPanel createImageButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(imageManager.getBtnBuscar(), BorderLayout.WEST);
        buttonPanel.add(imageManager.getBtnBorrar(), BorderLayout.EAST);
        return buttonPanel;
    }
    
    private void createTitle(JPanel panel, String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, "font:+2");
        panel.add(label, "gapy 5 0");
        panel.add(new JSeparator(), "height 2!,gapy 0 0");
    }
    
    private JLabel createDescriptionLabel(String text) {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
        return label;
    }
    
    // Metodos publicos para acceso a datos
    public void formOpen() {
        formFields.nombreField.grabFocus();
    }
    
    public List<File> getImageFiles() {
        return imageManager.getImageFiles();
    }
    
    public Optional<File> getCurrentImage() {
        return imageManager.getCurrentImageFile();
    }
    
    // Getters para campos del formulario
    public String getNombre() { return formFields.nombreField.getText().trim(); }
    public String getId() { return formFields.idField.getText().trim(); }
    public String getPrecio() { return formFields.precioField.getText().trim(); }
    public String getCantidad() { return formFields.cantidadField.getText().trim(); }
    public String getCantidadMaxima() { return formFields.cantMaxField.getText().trim(); }
    public String getCantidadMinima() { return formFields.cantMinField.getText().trim(); }
    public String getDescripcion() { return formFields.descripcionArea.getText().trim(); }
    public String getAlmacen() { return (String) comboBoxManager.getComboAlmacen().getSelectedItem(); }
    public String getCategoria() { return (String) comboBoxManager.getComboCategory().getSelectedItem(); }
    public JFormattedTextField getDateEditor() { return formFields.dateEditor; }
}