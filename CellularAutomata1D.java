import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
     * ClassNV.java
     * Purpose: generic Class that you can modify and adapt easily for any application
     * that need data visualization.
     * @author: Jeffrey Pallarés Núñez.
     * @version: 1.0 23/07/19
     */



public class CellularAutomata1D
{

    private static int[][] matrix;

    public static MainCanvas canvasTemplateRef;

    public int[][] getData() { return matrix; }

    public void plug(MainCanvas ref) { canvasTemplateRef = ref; }

    private static int width, height;

    public static int states_number = 2;
    private static int neighborhood_range = 1;
    private static int transition_function = 1;
    private static int cfrontier = 0;
    private static  RandomGenerator randomInitializer;
    private static EngineGenerator handler = new EngineGenerator();
    private static String random_engine;
    private static int seq_len;
    private static int seed;
    private static int cells_number;
    public static int generations;
    private static LinkedList<Integer>[] population;
    private static int[] binary_rule;
    private static int rules_number;
    private static int[] population_counter;

    private int[] compute_rule(){

        int decimal_rule = transition_function;
        int size_binary_rule = (int)Math.pow(states_number,2*neighborhood_range+1);
        binary_rule  = new int[size_binary_rule];
        int index = 0;
        while( decimal_rule != 0 && index <= size_binary_rule) {
            binary_rule[index] = decimal_rule % states_number;
            decimal_rule = decimal_rule / states_number;
            index ++;
        }

        StringBuilder cout= new StringBuilder(new String());
        cout.append("| ");
        for (int value : binary_rule) {
            cout.append(value);
            cout.append(" | ");
        }
        System.out.println(cout);
        System.out.println(Arrays.toString(binary_rule));
        return binary_rule;
    }

    private void initializeState(ArrayList<BigInteger> random_generated){
        for(BigInteger num: random_generated){
            matrix[num.intValue()%width][0] = num.intValue()%states_number;
        }
    }

    public LinkedList<Integer>[] getPopulation(){return population;}

    public void initializer (int cells_number, int generations, int states_number,
                             int neighborhood_range, int transition_function, int seed,
                             int cfrontier , String random_engine){
        width = cells_number;
        height = generations;
        matrix = new int[1000][1000];

        CellularAutomata1D.cells_number = cells_number;
        CellularAutomata1D.generations = generations;
        CellularAutomata1D.states_number = states_number;
        CellularAutomata1D.neighborhood_range = neighborhood_range;
        CellularAutomata1D.transition_function = transition_function;
        CellularAutomata1D.cfrontier = cfrontier;
        CellularAutomata1D.random_engine = random_engine;
        CellularAutomata1D.seed = seed;

        population = new LinkedList[states_number];
        for (int i = 0; i < states_number; i++) {
            population[i] = new LinkedList<Integer>();
        }
        rules_number = (int)Math.pow(states_number,((2*neighborhood_range +1)*(states_number-1))+1) -1;
        compute_rule();
        handler.createEngines();
        randomInitializer = new RandomGenerator(seed);

        if (random_engine.equals("Basic"))
            matrix[width / 2][0] = 1;
        else if(!random_engine.equals("generatorCombinedWXY")) {
            ArrayList<BigInteger> random_generated = randomInitializer.
                    getRandomSequence(handler.engines.get(random_engine), seed, width);
            initializeState(random_generated);
        }
        else {
            ArrayList<BigInteger> random_generated = randomInitializer.
                    getRandomSequenceCombined(handler.combined_engines.get(random_engine),
                            handler.engines.get("generatorCombinedW"), handler.engines.get("generatorCombinedY"),
                            handler.engines.get("generatorCombinedX"),
                            seed, seed, seed, width);
            initializeState(random_generated);
        }
    }

    public static Boolean abort = false;

    public static void stop() {
        abort = true;
    }

    public static LinkedList<Integer>[]caComputation(int nGen){
        abort = false;
        for (int i = 0; i < nGen ; i++) {
            if(abort)
                break;
            nextGen(i);
        }


        return population;
    }

    public static LinkedList<Integer>[] nextGen(int actual_gen){
        population_counter = new int[states_number];
        if (cfrontier==0){
            for (int i = 0; i < width; i++) {
                if(abort)
                    break;
                int j =(i + neighborhood_range) % width;
                int k = (i - neighborhood_range <0 ) ? i - neighborhood_range + width : i - neighborhood_range;
                int irule = 0;
                int exp = 0;

                while(exp < neighborhood_range *2 +1){
                    irule = irule + matrix[j][actual_gen];
                    exp ++;
                    j = ( j== 0) ? ( j - 1 + width) : j - 1;
                }

                if( irule >= binary_rule.length)
                    matrix[i][actual_gen+1] = 0;
                else
                    matrix[i][actual_gen+1] = binary_rule[irule];

                population_counter[matrix[i][actual_gen+1]]++;
                canvasTemplateRef.paintImmediately(i,actual_gen+1,500-width/2,500-height/2);
            }

            for (int i = 0; i < states_number; i++) {
                population[i].add(population_counter[i]);
            }


        }
        else{
            // TODO cilindric frontier
        }
        return population;

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