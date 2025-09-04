package CoolBoxApp.demo.component.chart.themes;

import java.awt.*;

public enum ColorThemes {
    DEFAULT(
            Color.decode("#fd7f6f"),
            Color.decode("#7eb0d5"),
            Color.decode("#b2e061"),
            Color.decode("#bd7ebe"),
            Color.decode("#ffb55a"),
            Color.decode("#ffee65"),
            Color.decode("#beb9db"),
            Color.decode("#fdcce5"),
            Color.decode("#8bd3c7")
    );

    private Color[] colors;

    ColorThemes(Color... colors) {
        this.colors = colors;
    }

    public Color[] getColors() {
        return colors;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
