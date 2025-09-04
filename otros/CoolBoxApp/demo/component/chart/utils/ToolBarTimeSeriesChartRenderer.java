package CoolBoxApp.demo.component.chart.utils;

import org.jfree.chart.renderer.xy.XYItemRenderer;
import CoolBoxApp.demo.component.ToolBarSelection;
import CoolBoxApp.demo.component.chart.TimeSeriesChart;
import CoolBoxApp.demo.component.chart.renderer.*;

public class ToolBarTimeSeriesChartRenderer extends ToolBarSelection<XYItemRenderer> {

    public ToolBarTimeSeriesChartRenderer(TimeSeriesChart chart) {
        super(getRenderers(), renderer -> {
            chart.setRenderer(renderer);
        });
    }

    private static XYItemRenderer[] getRenderers() {
        XYItemRenderer[] renderers = new XYItemRenderer[]{
                new ChartXYCurveRenderer(),
                new ChartXYLineRenderer(),
        };
        return renderers;
    }
}
