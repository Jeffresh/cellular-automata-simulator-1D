import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a real-time chart using SwingWorker
 */
public class RealTimeChart {

    public SwingWorker mySwingWorker;
    public SwingWrapper<XYChart> sw;
    public XYChart chart;
    public JFrame chart_frame;

    public void show(){
        // Create Chart
        chart = QuickChart.getChart("Generic Chart", "Generations", "Cells Number", "randomWalk",
                new double[] { 0 }, new double[] { 0 });
        chart.addSeries("corona",new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setXAxisTicksVisible(true);
        // Show it
        sw = new SwingWrapper<XYChart>(chart);
        chart_frame = sw.displayChart();
        chart_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chart_frame.setTitle("Generic Chart");
        chart_frame.validate();
        chart_frame.repaint();


    }

    public void go(int generations, int states) {

        mySwingWorker = new MySwingWorker(generations,states);
        mySwingWorker.execute();
    }

    private class MySwingWorker extends SwingWorker<Boolean, double[]> {

        private LinkedList<Integer>[] fifo;
        private int generations;
        private int states;

        public MySwingWorker(int generations, int states) {
            this.generations = generations;
            this.states= states;
            fifo = new LinkedList[states];
            for (int i = 0; i < states ; i++) {
                fifo[i] = new LinkedList<Integer>();
            }
            fifo[0].add(0);
        }

        @Override
        protected Boolean doInBackground() throws Exception {

            int point = 0;

            while (point < generations) {

                point++;

                fifo[0].add(point);

                double[] array = new double[fifo[0].size()];
                double[] array1 = new double[fifo[0].size()];
                double [] datax = new double[fifo[0].size()];
                for (int i = 0; i < fifo[0].size(); i++) {
                    array[i] = fifo[0].get(i);
                    array1[i] = fifo[0].get(i)*4;
                    datax[i] = i;
                }
                chart.updateXYSeries("randomWalk", datax, array, null);
                chart.updateXYSeries("corona", datax, array1, null);
                sw.repaintChart();

                long start = System.currentTimeMillis();
                long duration = System.currentTimeMillis() - start;
                try {
                    Thread.sleep(40 - duration); // 40 ms ==> 25fps
                    // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
                } catch (InterruptedException e) {
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // eat it. caught when interrupt is called
                    System.out.println("MySwingWorker shut down.");
                }

            }

            return true;
        }

    }
}