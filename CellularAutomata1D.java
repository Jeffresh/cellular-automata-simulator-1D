import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
     * ClassNV.java
     * Purpose: generic Class that you can modify and adapt easily for any application
     * that need data visualization.
     * @author: Jeffrey Pallarés Núñez.
     * @version: 1.0 23/07/19
     */



public class CellularAutomata1D implements Runnable
{

    private static int[][] matrix;
    private static int [] gen, next_gen;

    private static AtomicIntegerArray population_counter;
    private int [] local_population_counter;

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
    private int task_number;
    private static int total_tasks;
    private static CyclicBarrier barrier = null;
    public void run()
    {

        for (int i = 0; i < generations-1 ; i++) {
            if(abort)
                break;
            nextGen(i);




            try
            {
                int l = barrier.await();
                for (int j = 0; j < states_number; j++) {
                    population_counter.getAndAdd(j,this.local_population_counter[j]);
                }

                if(task_number==1) {
                    canvasTemplateRef.paintImmediately(0, 0, 1000, 1000);

                    for (int j = 0; j < states_number; j++) {
                        population[j].add(population_counter.get(j));
                    }
                    population_counter = new AtomicIntegerArray(states_number);

                }

                if(barrier.getParties() == 0)
                    barrier.reset();
            }catch(Exception e){}
        }



    }

    public CellularAutomata1D(){}

    private int in;
    private int fn;

    public CellularAutomata1D(int i)
    {
        task_number = i;

        int paso = cells_number /total_tasks;


        fn = paso * task_number;
        in = fn - paso;

        if( total_tasks == task_number)
            fn =cells_number;

        System.out.println(in+" "+fn);

    }

    private static int gens;
    private static int size_pool;
    private static ThreadPoolExecutor myPool;
    public static void next_gen_concurrent(int nt,int g)
    {
        gens =g;


        size_pool =nt;

        barrier = new CyclicBarrier (size_pool);
        total_tasks = size_pool;

        myPool = new ThreadPoolExecutor(
                size_pool, size_pool, 60000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        CellularAutomata1D[] tareas = new  CellularAutomata1D[nt];

        for(int t = 0; t < nt; t++)
        {
            tareas[t] = new CellularAutomata1D(t+1);
            myPool.execute(tareas[t]);

        }


        myPool.shutdown();
        try{
            myPool.awaitTermination(10, TimeUnit.HOURS);}catch(Exception e){}


    }

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
        matrix = new int[height][width];


        population_counter = new AtomicIntegerArray(states_number);

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
        generations = nGen;
        next_gen_concurrent(4,nGen);

        return population;
    }

    public  LinkedList<Integer>[] nextGen(int actual_gen){

        local_population_counter = new int[states_number];
        for (int i = 0; i < states_number; i++) {
            this.local_population_counter[i]=0;

        }
        if (cfrontier==0){
            for (int i = in; i < fn; i++) {
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


                if (irule >= binary_rule.length)
                    matrix[i][actual_gen + 1] = 0;
                else
                    matrix[i][actual_gen + 1] = binary_rule[irule];

                local_population_counter[matrix[i][actual_gen + 1]]++;

            }






        }
        else{
            // TODO cilindric frontier
        }
        return population;

    }

}