import java.awt.*;
import java.util.LinkedList;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import javax.swing.*;

import org.knowm.xchart.*;

/**
 * Creates a real-time chart using SwingWorker
 */
public class PopulationChart {

    public  JPanel sw;
    public  XYChart chart;
    public  JFrame chart_frame;
    public  CellularAutomata1D CA1Dref;
    private   String chart_title;
    private  LinkedList<Integer>[] fifo;


    PopulationChart(String chart_title, String x_axis_name, String y_axis_name){
        this.chart_title = chart_title;
        chart = new XYChartBuilder().title(chart_title).xAxisTitle(x_axis_name).yAxisTitle(y_axis_name).build();
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setXAxisTicksVisible(true);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

    }

    public void setRef(CellularAutomata1D ref){
        CA1Dref = ref;
    }

    public void getData () {

        fifo = CA1Dref.getPopulation();
        double[][] array = new double[CA1Dref.states_number][fifo[0].size()];

        for (int j = 0; j < CA1Dref.states_number; j++) {
            for (int i = 0; i < fifo[j].size(); i++)
                array[j][i] = fifo[j].get(i)+0.0;
            chart.updateXYSeries("state "+(j),null, array[j], null);
        }
    }

    public void create_series(){
        for (int i = 0; i < CA1Dref.states_number ; i++) {
            chart.addSeries("state "+(i),new double[] { 0 }, new double[] { 0 })
                    .setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        }
    }

    public void plot(){
        getData();
        sw.revalidate();
        sw.repaint();
    }

    public void show(){
            sw = new XChartPanel(chart);
            chart_frame = new JFrame("chart");
            chart_frame.add(sw);
            chart_frame.setAlwaysOnTop(true);
            chart_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            chart_frame.setTitle(chart_title);
            chart_frame.setOpacity(1);
            chart_frame.setBackground(Color.WHITE);
            chart_frame.setVisible(true);
            chart_frame.pack();
    }
}