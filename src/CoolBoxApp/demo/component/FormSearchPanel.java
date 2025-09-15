package CoolBoxApp.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatMenuArrowIcon;
import net.miginfocom.swing.MigLayout;
import SantaCruzDept.modal.Drawer;
import SantaCruzDept.modal.ModalDialog;
import SantaCruzDept.component.ModalContainer;

import CoolBoxApp.demo.icons.SVGIconUIColor;
import CoolBoxApp.demo.menu.MyMenuValidation;
import CoolBoxApp.demo.system.Form;
import CoolBoxApp.demo.system.FormSearch;
import CoolBoxApp.demo.utils.DemoPreferences;
import CoolBoxApp.demo.utils.SystemForm;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FormSearchPanel extends JPanel {

    // Constants
    private static final int SEARCH_MAX_LENGTH = 50;
    
    // Functional interfaces for callbacks
    @FunctionalInterface
    private interface ItemEventHandler {
        void handle(Item item);
    }
    
    @FunctionalInterface
    private interface SearchValidator {
        boolean validate(SystemForm form, String searchTerm);
    }
    
    // Immutable fields
    private final LookAndFeel oldTheme = UIManager.getLookAndFeel();
    private final Map<SystemForm, Class<? extends Form>> formsMap;
    private final List<Item> listItems;
    private final SearchValidator searchValidator;
    
    // UI Components
    private JTextField textSearch;
    private JPanel panelResult;

    public FormSearchPanel(Map<SystemForm, Class<? extends Form>> formsMap) {
        this.formsMap = Collections.unmodifiableMap(new HashMap<>(formsMap));
        this.listItems = new ArrayList<>();
        this.searchValidator = this::matchesSearch;
        init();
    }

    private void init() {
        setupLayout();
        setupSearchField();
        setupResultPanel();
        setupScrollPane();
        installSearchField();
    }

    private void setupLayout() {
        setLayout(new MigLayout("fillx,insets 0,wrap", "[fill,500]"));
    }

    private void setupSearchField() {
        textSearch = new JTextField();
        configureSearchFieldProperties();
        add(textSearch, "gap 17 17 0 0");
        add(new JSeparator(), "height 2!");
    }

    private void configureSearchFieldProperties() {
        textSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Búsqueda...");
        textSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, 
            new FlatSVGIcon("CoolBoxApp/demo/icons/search.svg", 0.4f));
        textSearch.putClientProperty(FlatClientProperties.STYLE, 
            "border:3,3,3,3;background:null;showClearButton:true;");
    }

    private void setupResultPanel() {
        panelResult = new JPanel(new MigLayout("insets 3 10 3 10,fillx,wrap", "[fill]"));
    }

    private void setupScrollPane() {
        JScrollPane scrollPane = createConfiguredScrollPane();
        add(scrollPane);
    }

    private JScrollPane createConfiguredScrollPane() {
        JScrollPane scrollPane = new JScrollPane(panelResult);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        configureScrollBarStyle(scrollPane);
        return scrollPane;
    }

    private void configureScrollBarStyle(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
            "trackArc:$ScrollBar.thumbArc;thumbInsets:0,3,0,3;trackInsets:0,3,0,3;width:12;");
    }

    public void formCheck() {
        Optional.of(UIManager.getLookAndFeel())
            .filter(currentTheme -> !currentTheme.equals(oldTheme))
            .ifPresent(theme -> SwingUtilities.updateComponentTreeUI(this));
    }

    private void installSearchField() {
        setupDocumentLengthLimit();
        setupSearchDocumentListener();
        setupKeyboardNavigation();
    }

    private void setupDocumentLengthLimit() {
        textSearch.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                Optional.ofNullable(str)
                    .filter(s -> getLength() + s.length() <= SEARCH_MAX_LENGTH)
                    .ifPresent(s -> {
                        try {
                            super.insertString(offs, s, a);
                        } catch (BadLocationException e) {
                            // Log error in production
                        }
                    });
            }
        });
    }

    private void setupSearchDocumentListener() {
        textSearch.getDocument().addDocumentListener(new DocumentListener() {
            private String previousText = "";

            @Override
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { performSearch(); }

            private void performSearch() {
                String currentText = normalizeSearchText(textSearch.getText());
                
                if (!currentText.equals(previousText)) {
                    previousText = currentText;
                    executeSearch(currentText);
                }
            }
        });
    }

    private String normalizeSearchText(String text) {
        return Optional.ofNullable(text)
            .map(String::trim)
            .map(String::toLowerCase)
            .orElse("");
    }

    private void executeSearch(String searchTerm) {
        clearResults();
        
        if (searchTerm.isEmpty()) {
            showRecentResults();
        } else {
            performFormSearch(searchTerm);
        }
    }

    private void clearResults() {
        panelResult.removeAll();
        listItems.clear();
    }

    private void performFormSearch(String searchTerm) {
        List<Item> searchResults = createSearchResults(searchTerm);
        
        if (searchResults.isEmpty()) {
            showNoResults(searchTerm);
        } else {
            displaySearchResults(searchResults);
        }
        
        refreshUI();
    }

    private List<Item> createSearchResults(String searchTerm) {
        return formsMap.entrySet().stream()
            .filter(entry -> searchValidator.validate(entry.getKey(), searchTerm))
            .filter(entry -> MyMenuValidation.validation(entry.getValue()))
            .map(entry -> createSearchItem(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    private Item createSearchItem(SystemForm systemForm, Class<? extends Form> formClass) {
        return new Item(systemForm, formClass, false, false, 
            this::handleItemShow, this::handleItemRemove, this::handleItemFavorite);
    }

    private boolean matchesSearch(SystemForm form, String searchTerm) {
        return Stream.of(
                form.name().toLowerCase(),
                form.description().toLowerCase()
            )
            .anyMatch(field -> field.contains(searchTerm)) ||
            Arrays.stream(form.tags())
                .map(String::toLowerCase)
                .anyMatch(tag -> tag.contains(searchTerm));
    }

    private void showNoResults(String searchTerm) {
        panelResult.add(createNoResultComponent(searchTerm));
    }

    private void displaySearchResults(List<Item> results) {
        results.forEach(panelResult::add);
        listItems.addAll(results);
        setSelectedIndex(0);
    }

    private void refreshUI() {
        panelResult.repaint();
        updateLayout();
    }

    private void setupKeyboardNavigation() {
        textSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyNavigation(e.getKeyCode());
            }
        });
    }

    private void handleKeyNavigation(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> moveSelection(true);
            case KeyEvent.VK_DOWN -> moveSelection(false);
            case KeyEvent.VK_ENTER -> showSelectedForm();
        }
    }

    private void updateLayout() {
        Optional.ofNullable(SwingUtilities.getAncestorOfClass(ModalContainer.class, this))
            .ifPresent(Container::revalidate);
    }

    private void showSelectedForm() {
        getSelectedIndex()
            .map(listItems::get)
            .ifPresent(Item::showForm);
    }

    private void setSelectedIndex(int index) {
        IntStream.range(0, listItems.size())
            .forEach(i -> listItems.get(i).setSelected(i == index));
    }

    private OptionalInt getSelectedIndex() {
        return IntStream.range(0, listItems.size())
            .filter(i -> listItems.get(i).isSelected())
            .findFirst();
    }

    private void moveSelection(boolean up) {
        if (listItems.isEmpty()) return;
        
        int currentIndex = getSelectedIndex().orElse(-1);
        int newIndex = calculateNewIndex(currentIndex, up, listItems.size());
        setSelectedIndex(newIndex);
    }

    private int calculateNewIndex(int currentIndex, boolean up, int size) {
        if (currentIndex == -1) {
            return up ? size - 1 : 0;
        }
        
        return up ? 
            (currentIndex == 0 ? size - 1 : currentIndex - 1) :
            (currentIndex == size - 1 ? 0 : currentIndex + 1);
    }

    private void showRecentResults() {
        List<Item> recentItems = getRecentSearchItems(false);
        List<Item> favoriteItems = getRecentSearchItems(true);
        
        clearResults();
        
        addSectionIfNotEmpty("Reciente", recentItems);
        addSectionIfNotEmpty("Favorito", favoriteItems);
        
        if (listItems.isEmpty()) {
            panelResult.add(new NoRecentResult());
        } else {
            setSelectedIndex(0);
        }
        
        updateLayout();
    }

    private void addSectionIfNotEmpty(String title, List<Item> items) {
        if (!items.isEmpty()) {
            panelResult.add(createSectionLabel(title));
            items.forEach(panelResult::add);
            listItems.addAll(items);
        }
    }

    private JLabel createSectionLabel(String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, "font:bold +1;border:5,15,5,15;");
        return label;
    }

    private List<Item> getRecentSearchItems(boolean favorite) {
        return Optional.ofNullable(DemoPreferences.getRecentSearch(favorite))
            .map(Arrays::stream)
            .orElse(Stream.empty())
            .map(name -> createRecentItem(name, favorite))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(this::isValidItem)
            .collect(Collectors.toList());
    }

    private boolean isValidItem(Item item) {
        return MyMenuValidation.validation(item.getFormClass());
    }

    private Optional<Class<? extends Form>> getFormClass(String name) {
        return formsMap.entrySet().stream()
            .filter(entry -> entry.getKey().name().equals(name))
            .map(Map.Entry::getValue)
            .findFirst();
    }

    private Optional<Item> createRecentItem(String name, boolean favorite) {
        return formsMap.entrySet().stream()
            .filter(entry -> entry.getKey().name().equals(name))
            .map(entry -> new Item(entry.getKey(), entry.getValue(), true, favorite,
                this::handleItemShow, this::handleItemRemove, this::handleItemFavorite))
            .findFirst();
    }

    private Component createNoResultComponent(String searchText) {
        JPanel panel = new JPanel(new MigLayout("insets 15 5 15 5,al center,gapx 1"));
        
        JLabel prefixLabel = createDisabledLabel("No hay resultados para \"");
        JLabel searchLabel = new JLabel(searchText);
        JLabel suffixLabel = createDisabledLabel("\"");
        
        Stream.of(prefixLabel, searchLabel, suffixLabel)
            .forEach(panel::add);
        
        return panel;
    }

    private JLabel createDisabledLabel(String text) {
        JLabel label = new JLabel(text);
        label.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        return label;
    }

    // Event handlers
    private void handleItemShow(Item item) {
        item.showForm();
    }

    private void handleItemRemove(Item item) {
        item.removeRecent();
    }

    private void handleItemFavorite(Item item) {
        item.addFavorite();
    }

    public void clearSearch() {
        if (!textSearch.getText().isEmpty()) {
            textSearch.setText("");
        } else {
            showRecentResults();
        }
    }

    public void searchGrabFocus() {
        textSearch.requestFocusInWindow();
    }

    // Inner Classes
    private static class NoRecentResult extends JPanel {
        public NoRecentResult() {
            setLayout(new MigLayout("insets 15 5 15 5,al center"));
            JLabel label = new JLabel("No hay búsquedas recientes");
            label.putClientProperty(FlatClientProperties.STYLE, 
                "foreground:$Label.disabledForeground;font:bold;");
            add(label);
        }
    }

    private class Item extends JButton {
        private final SystemForm data;
        private final Class<? extends Form> formClass;
        private final boolean isRecent;
        private final boolean isFavorite;
        private final ItemEventHandler onShow;
        private final ItemEventHandler onRemove;
        private final ItemEventHandler onFavorite;
        private Component itemSource;

        public Item(SystemForm data, Class<? extends Form> formClass, boolean isRecent, 
                   boolean isFavorite, ItemEventHandler onShow, ItemEventHandler onRemove, 
                   ItemEventHandler onFavorite) {
            this.data = data;
            this.formClass = formClass;
            this.isRecent = isRecent;
            this.isFavorite = isFavorite;
            this.onShow = onShow;
            this.onRemove = onRemove;
            this.onFavorite = onFavorite;
            init();
        }

        private void init() {
            configureButton();
            setupLayout();
            addComponents();
            setupActionListener();
        }

        private void configureButton() {
            setFocusable(false);
            setHorizontalAlignment(JButton.LEADING);
            putClientProperty(FlatClientProperties.STYLE,
                "background:null;arc:10;borderWidth:0;focusWidth:0;innerFocusWidth:0;" +
                "[light]selectedBackground:lighten($Button.selectedBackground,9%)");
        }

        private void setupLayout() {
            setLayout(new MigLayout("insets 3 3 3 0,filly,gapy 2", "[]push[]"));
        }

        private void addComponents() {
            add(new JLabel(data.name()), "cell 0 0");
            add(createDescriptionLabel(), "cell 0 1");
            add(createActionComponent(), "cell 1 0,span 1 2");
        }

        private JLabel createDescriptionLabel() {
            JLabel label = new JLabel(data.description());
            label.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
            return label;
        }

        private Component createActionComponent() {
            return isRecent ? createRecentOptions() : new JLabel(new FlatMenuArrowIcon());
        }

        private void setupActionListener() {
            addActionListener(e -> handleAction());
        }

        private void handleAction() {
            Optional.ofNullable(itemSource)
                .map(Component::getName)
                .ifPresentOrElse(this::handleNamedAction, this::handleDefaultAction);
        }

        private void handleNamedAction(String actionName) {
            switch (actionName) {
                case "remover" -> onRemove.handle(this);
                case "favoritos" -> onFavorite.handle(this);
            }
        }

        private void handleDefaultAction() {
            clearAllSelections();
            setSelected(true);
            onShow.handle(this);
        }

        private void clearAllSelections() {
            Stream.of(getParent().getComponents())
                .filter(JButton.class::isInstance)
                .map(JButton.class::cast)
                .forEach(button -> button.setSelected(false));
        }

        protected void showForm() {
            ModalDialog.closeModal(FormSearch.ID);
            Drawer.setSelectedItemClass(formClass);
            
            if (!isFavorite) {
                DemoPreferences.addRecentSearch(data.name(), false);
            }
        }

        protected Component createRecentOptions() {
            JPanel panel = new JPanel(new MigLayout("insets n 0 n 0,fill,gapx 2", "", "[fill]"));
            panel.setOpaque(false);
            
            addFavoriteComponent(panel);
            panel.add(new JSeparator(JSeparator.VERTICAL), "gapy 5 5");
            panel.add(createActionButton("remover", "clear.svg", 0.35f, "Label.foreground", 0.9f));
            
            return panel;
        }

        private void addFavoriteComponent(JPanel panel) {
            if (isFavorite) {
                panel.add(createFavoriteIndicator());
            } else {
                panel.add(createActionButton("favoritos", "favorite.svg", 0.4f, "Component.accentColor", 0.9f));
            }
        }

        private JLabel createFavoriteIndicator() {
            JLabel label = new JLabel(new SVGIconUIColor(
                "CoolBoxApp/demo/icons/favorite_filled.svg", 0.4f, "Component.accentColor", 0.8f));
            label.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3;");
            return label;
        }

        private JButton createActionButton(String name, String icon, float scale, String hoverKey, float alpha) {
            SVGIconUIColor svgIcon = new SVGIconUIColor(
                "CoolBoxApp/demo/icons/" + icon, scale, "Label.disabledForeground", alpha);
            
            JButton button = new JButton(svgIcon);
            configureActionButton(button, name, svgIcon, hoverKey);
            
            return button;
        }

        private void configureActionButton(JButton button, String name, SVGIconUIColor svgIcon, String hoverKey) {
            button.setName(name);
            button.setFocusable(false);
            button.setContentAreaFilled(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setModel(getModel());
            button.putClientProperty(FlatClientProperties.STYLE, "margin:3,3,3,3;");
            
            setupButtonHoverEffects(button, svgIcon, hoverKey);
        }

        private void setupButtonHoverEffects(JButton button, SVGIconUIColor svgIcon, String hoverKey) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    svgIcon.setColorKey(hoverKey);
                    itemSource = (Component) e.getSource();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    svgIcon.setColorKey("Label.disabledForeground");
                    itemSource = null;
                }
            });
        }

        protected void removeRecent() {
            DemoPreferences.removeRecentSearch(data.name(), isFavorite);
            removeFromParent();
            
            if (listItems.isEmpty()) {
                showEmptyState();
            } else {
                removeEmptySectionIfNeeded();
            }
            
            updateLayout();
        }

        private void removeFromParent() {
            panelResult.remove(this);
            listItems.remove(this);
        }

        private void showEmptyState() {
            panelResult.removeAll();
            panelResult.add(new NoRecentResult());
        }

        private void removeEmptySectionIfNeeded() {
            if (countItemsOfType(isFavorite) == 0) {
                int labelIndex = isFavorite ? panelResult.getComponentCount() - 1 : 0;
                panelResult.remove(labelIndex);
            }
        }

        protected void addFavorite() {
            DemoPreferences.addRecentSearch(data.name(), true);
            removeFromParent();
            
            Item favoriteItem = new Item(data, formClass, isRecent, true, onShow, onRemove, onFavorite);
            insertFavoriteItem(favoriteItem);
            
            removeRecentSectionIfEmpty();
            updateLayout();
        }

        private void insertFavoriteItem(Item favoriteItem) {
            getFirstFavoriteIndex()
                .ifPresentOrElse(
                    index -> insertAt(favoriteItem, index),
                    () -> appendAsFavorite(favoriteItem)
                );
        }

        private void insertAt(Item item, int[] indices) {
            panelResult.add(item, indices[1] - 1);
            listItems.add(indices[0] - 1, item);
        }

        private void appendAsFavorite(Item item) {
            panelResult.add(createSectionLabel("Favorito"));
            panelResult.add(item);
            listItems.add(item);
        }

        private void removeRecentSectionIfEmpty() {
            if (countItemsOfType(false) == 0) {
                panelResult.remove(0);
            }
        }

        private long countItemsOfType(boolean favorite) {
            return listItems.stream()
                .mapToInt(item -> item.isFavorite == favorite ? 1 : 0)
                .sum();
        }

        private Optional<int[]> getFirstFavoriteIndex() {
            return IntStream.range(0, listItems.size())
                .filter(i -> listItems.get(i).isFavorite)
                .mapToObj(i -> new int[]{i, panelResult.getComponentZOrder(listItems.get(i))})
                .findFirst();
        }

        // Getters
        public Class<? extends Form> getFormClass() { return formClass; }
        public SystemForm getData() { return data; }
        public boolean isRecent() { return isRecent; }
        public boolean isFavorite() { return isFavorite; }
    }
}