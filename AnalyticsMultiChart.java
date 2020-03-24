import java.awt.*;
import java.util.LinkedList;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import javax.swing.*;

import org.knowm.xchart.*;

/**
 * Creates a real-time chart using SwingWorker
 */
public class AnalyticsMultiChart {



    public  JPanel population_chart_panel;
    public  JPanel hamming_chart_panel;
    public  JPanel entropy_chart_panel;
    public  XYChart population_chart;
    public  XYChart hamming_chart;
    public  XYChart entropy_chart;
    public  JFrame chart_frame;
    public  CellularAutomata1D CA1Dref;
    private  String chart_title;
    private  LinkedList<Double>[] fifo_population;
    private  LinkedList<Double>[] fifo_hamming;
    private  LinkedList<Double>[] fifo_entropy;


    AnalyticsMultiChart(String chart_title, String x_axis_name, String y_axis_name){
        this.chart_title = chart_title;
        population_chart = new XYChartBuilder().title(chart_title).xAxisTitle(x_axis_name).yAxisTitle(y_axis_name).build();
        population_chart.getStyler().setLegendVisible(true);
        population_chart.getStyler().setXAxisTicksVisible(true);
        population_chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        hamming_chart = new XYChartBuilder().title("Hamming Distance").xAxisTitle(x_axis_name).yAxisTitle(y_axis_name).build();
        hamming_chart.getStyler().setLegendVisible(true);
        hamming_chart.getStyler().setXAxisTicksVisible(true);
        hamming_chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        entropy_chart = new XYChartBuilder().title("Spatial Entropy").xAxisTitle(x_axis_name).yAxisTitle(y_axis_name).build();
        entropy_chart.getStyler().setLegendVisible(true);
        entropy_chart.getStyler().setXAxisTicksVisible(true);
        entropy_chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
    }

    public void setRef(CellularAutomata1D ref){
        CA1Dref = ref;
    }

    public void getData () {

        fifo_population = CA1Dref.getPopulation();
        double[][] array = new double[CA1Dref.states_number][fifo_population[0].size()];

        for (int j = 0; j < CA1Dref.states_number; j++) {
            for (int i = 0; i < fifo_population[j].size(); i++)
                array[j][i] = fifo_population[j].get(i)+0.0;
            population_chart.updateXYSeries("state "+(j),null, array[j], null);
        }
    }

    public void create_series(){
        for (int i = 0; i < CA1Dref.states_number ; i++) {
            population_chart.addSeries("state "+(i),new double[] { 0 }, new double[] { 0 })
                    .setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        }

        hamming_chart.addSeries("Hamming distance",new double[] { 0 }, new double[] { 0 })
                .setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);



        entropy_chart.addSeries("Spatial Entropy",new double[] { 0 }, new double[] { 0 })
                .setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

    }

    public void getDataHamming(){
        fifo_hamming = new LinkedList[1];
        fifo_hamming[0] = CA1Dref.getHammingDistance();
        double[]array = new double[fifo_hamming[0].size()];
        for (int i = 0; i < fifo_hamming[0].size(); i++){
            array[i] = fifo_hamming[0].get(i)+0.0;
            hamming_chart.updateXYSeries("Hamming distance",null, array, null);
        }
    }

    public void getDataSpatialEntropy(){
        fifo_entropy = new LinkedList[1];
        fifo_entropy[0] = CA1Dref.getEntropy();
        double[]array = new double[fifo_entropy[0].size()];
        for (int i = 0; i < fifo_entropy[0].size(); i++){
            array[i] = fifo_entropy[0].get(i)+0.0;
            entropy_chart.updateXYSeries("Spatial Entropy",null, array, null);
        }
    }

    public void plot(){
        getData();
        getDataHamming();
        getDataSpatialEntropy();

        population_chart_panel.revalidate();
        population_chart_panel.repaint();
        hamming_chart_panel.revalidate();
        hamming_chart_panel.repaint();
        entropy_chart_panel.revalidate();
        entropy_chart_panel.repaint();
    }

    public void show(){
        population_chart_panel = new XChartPanel(population_chart);
        hamming_chart_panel = new XChartPanel(hamming_chart);
        entropy_chart_panel = new XChartPanel(entropy_chart);
        chart_frame = new JFrame("Charts");
        GridLayout layout = new GridLayout(3,1);
        chart_frame.setLayout(layout);
        chart_frame.add(population_chart_panel);
        chart_frame.add(hamming_chart_panel);
        chart_frame.add(entropy_chart_panel);
        chart_frame.setSize(600,600);
        chart_frame.setMaximumSize(new Dimension(100,600));
        chart_frame.setMaximumSize(new Dimension(100,600));


        chart_frame.setAlwaysOnTop(true);
        chart_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chart_frame.setTitle(chart_title);
        chart_frame.setOpacity(1);
        chart_frame.setBackground(Color.WHITE);
        chart_frame.setVisible(true);
        chart_frame.pack();
    }
}