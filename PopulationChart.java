import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a real-time chart using SwingWorker
 */
public class PopulationChart {

    public SwingWorker mySwingWorker;
    public SwingWrapper<XYChart> sw;
    public XYChart chart;
    public JFrame chart_frame;
    public CellularAutomata1D CA1Dref;
    private LinkedList<Integer>[] fifo;



    public void setRef(CellularAutomata1D ref){
        CA1Dref = ref;
    }

    public void show(String chart_title){
        // Create Chart
        chart = QuickChart.getChart(chart_title, "Generations", "Cells Number", "state 0",
                new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setXAxisTicksVisible(true);

        for (int i = 1; i < CA1Dref.states_number ; i++) {
            chart.addSeries("state "+(i),new double[] { 0 }, new double[] { 0 });
        }



        fifo = CA1Dref.getPopulation();
        double [] datax = new double[CA1Dref.generations];
        double[][] array = new double[CA1Dref.states_number][fifo[0].size()];

        for (int j = 0; j < CA1Dref.states_number; j++) {
            for (int i = 0; i < fifo[j].size(); i++) {

                array[j][i] = fifo[j].get(i)+0.0;
            }
            chart.updateXYSeries("state "+(j),null, array[j], null);
        }

        // Show it
        sw = new SwingWrapper<XYChart>(chart);
        chart_frame = sw.displayChart();
        chart_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chart_frame.setTitle(chart_title);
        chart_frame.validate();
        chart_frame.repaint();


    }

    //TODO PAINT IN REAL TIME
//    public void go(int generations, int states) {
//        mySwingWorker = new MySwingWorker(generations,states);
//        mySwingWorker.execute();
//    }

//    private class MySwingWorker extends SwingWorker<Boolean, double[]> {
//
//        private LinkedList<Integer>[] fifo;
//        private int generations;
//        private int states;
//        private double [] array;
//
//        public MySwingWorker(int generations, int states) {
//            this.generations = generations;
//            this.states= states;
//            for (int i = 0; i < states ; i++) {
//                if(i>0)
//                    chart.addSeries("state "+(i+1),new double[] { 0 }, new double[] { 0 });
//            }
//
//            array = new double[generations];
//        }
//
//        @Override
//        protected Boolean doInBackground() throws Exception {
//
//            int point = 0;
//
//            while (point < generations) {
//
//                point++;
//                fifo = CA1Dref.nextGen(point);
//
//                double [] datax = new double[point];
//                double[][] array = new double[2][point];
//
//                for (int j = 0; j < states; j++) {
//                    for (int i = 0; i < fifo[j].size(); i++) {
//
//                        array[j][i] = fifo[j].get(i);
//                        datax[i] = i;
//                    }
//                    chart.updateXYSeries("state "+(j+1), datax, array[j], null);
//                }
//                sw.repaintChart();
//                CA1Dref.canvasTemplateRef.repaint(1000,1000,1000,1000);
//
//                long start = System.currentTimeMillis();
//                long duration = System.currentTimeMillis() - start;
//                try {
//                    Thread.sleep(40 - duration); // 40 ms ==> 25fps
//                    // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
//                } catch (InterruptedException e) {
//                }
//
//                try {
//                    Thread.sleep(5);
//                } catch (InterruptedException e) {
//                    // eat it. caught when interrupt is called
//                    System.out.println("MySwingWorker shut down.");
//                }
//
//            }
//
//            return true;
//        }
//
//    }
}