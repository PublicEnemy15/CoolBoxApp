package CoolBoxApp.demo.component.chart.utils;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import CoolBoxApp.demo.component.ToolBarSelection;

public class ToolBarCategoryOrientation extends ToolBarSelection<String> {

    public ToolBarCategoryOrientation(JFreeChart chart) {
        super(new String[]{"Vertical", "Horizontal"}, orientation -> {
            chart.getCategoryPlot().setOrientation(orientation == "Horizontal" ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL);
        });
    }
}
