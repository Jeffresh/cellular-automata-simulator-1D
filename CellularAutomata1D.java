   /**
     * ClassNV.java
     * Purpose: generic Class that you can modify and adapt easily for any application
     * that need data visualization.
     * @author: Jeffrey Pallarés Núñez.
     * @version: 1.0 23/07/19
     */

interface ca1DSim{
       public static void nextGen() {

       }

       public static void caComputation(int nGen) {

       }
   }

public class CellularAutomata1D implements ca1DSim
{

    private static int[][] matrix;

    private static MainCanvas canvasTemplateRef;

    public int[][] getData() { return matrix; }

    public void plug(MainCanvas ref) { canvasTemplateRef = ref; }

    private static int width, height;

    private static int states_number = 2;
    private static int neighborhood_range = 1;
    private static int transition_function = 1;
    private static int cfrontier = 0;
    private static  RandomGenerator randomInitializer;
    private static EngineGenerator handler = new EngineGenerator();
    private static String random_engine;
    private static int seq_len;
    private static int seed;

    public void initializer(int seed, int states_number, int neighborhood_range,
                            int transition_function, int cfrontier) {
        width = 1000;
        height = 1000;
        matrix = new int[width][height];

        CellularAutomata1D.states_number = states_number;
        CellularAutomata1D.neighborhood_range = neighborhood_range;
        CellularAutomata1D.transition_function = transition_function;
        CellularAutomata1D.cfrontier = cfrontier;

        handler.createEngines();
        randomInitializer = new RandomGenerator(seed);


        for (int i = 0; i < width ; i++) {
            for (int j = 0; j < height ; j++) {
                matrix[i][j] = 0;

            }

        }
    }

    public static Boolean abort = false;

    public static void stop() {
        abort = true;
    }

    private static void caComputation(int nGen){

    }

    private static void nextGen(){

    }

    public void computeTask(int line)
    {
        abort = false;

        if(matrix[0][0]==2)
        {
            for (int j = 1; j < 1000; j++ ) {
                if(abort)
                    break;
                matrix[line][j] = 1;
                canvasTemplateRef.paintImmediately(0,0,1000,1000);

            }

        }
        else if(matrix[0][0]==3){
            for (int j = 1; j < 1000; j++ ) {
                if(abort)
                    break;
                matrix[j][line] = 1;
                canvasTemplateRef.paintImmediately(0,0,1000,1000);

            }
        }

    }
    
}