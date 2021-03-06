import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
     * GuiTemplate.java
     * Purpose: this program
     * implements a Gui template that you can modify and adapt easily for any application
     * that need data visualization.
     * @author: Jeffrey Pallarés Núñez.
     * @version: 1.0 23/07/19
     */

public class GuiCA1D extends Frame implements ActionListener, FocusListener {

    private static final long serialVersionUID = 1L;

    private static JMenuBar nav_bar;
    private static String[] buttons_names;
    private static Map<String, JButton> gui_buttons = new LinkedHashMap<String, JButton>();
    public static Map<String, String> textfields_and_labels = new LinkedHashMap<>();
    private static JComboBox<String>  generator_list_combo_box;
    private static String[] engine_generator_names  = {
            "Basic","generator261a", "generator261b", "generator262", "generator263", "generatorFishmanAndMore1",
            "generatorFishmanAndMore2", "generatorRandu","generatorCombinedWXY",
    };
    private static String initializer_mode = "Basic";





    private JMenuBar createTopBar(Color color, Dimension dimension) {

        JMenuBar top_bar = new JMenuBar();
        top_bar.setOpaque(true);
        top_bar.setBackground(color);
        top_bar.setPreferredSize(dimension);

        return top_bar;
    }


    private JMenu createMenu( String menu_name, Font font, Color color) {

        JMenu menu = new JMenu(menu_name);
        menu.setFont(font);
        menu.setForeground(color);
        return menu;
    }


    private  Map<String, JMenu> createMenusItems( Map<String,String[]> items, Color color, Font font) {

        JMenuItem item;
        JMenu m;
        Map<String, JMenu> menus = new HashMap<>();

        for(Map.Entry<String,String[]> menu: items.entrySet()){
            String menu_name = menu.getKey();
            m = createMenu(menu_name, font , color);
            for(String item_name :menu.getValue()) {
                item = new JMenuItem(item_name);
                item.setFont(font);
                item.addActionListener(this);
                m.add(item);
            }
            menus.put(menu_name, m);
        }

        return menus;
    }

    private JMenuBar createNavBar() {

        Font menu_font = new Font("Dialog", Font.PLAIN, 20);
        Color menu_font_color = new Color(168, 168, 168);
        Color navbar_color = new Color(0,0,0);
        Dimension navbar_dimension = new Dimension(200,40);
        Map<String, String[] > menu_items = new HashMap<>();

        menu_items.put("File", new String[]{"Item menu 1", "Item menu 2"});
        menu_items.put("Plot", new String[]{"Population chart"});
        menu_items.put("Help", new String[]{"Help message"});
        menu_items.put("About", new String[]{"About message"});

        nav_bar = createTopBar(navbar_color, navbar_dimension);

        Map<String, JMenu> menus = createMenusItems(menu_items, menu_font_color, menu_font);

        nav_bar.add(menus.get("File"));
        nav_bar.add(menus.get("Plot"));
        nav_bar.add(Box.createHorizontalGlue());
        nav_bar.add(menus.get("Help"));
        nav_bar.add(menus.get("About"));

        return nav_bar;
    }

    String [] round_buttons_options = {"No", "Yes"};
    ButtonGroup cilindric_frontier = new ButtonGroup();
    Map<String, JRadioButton> cilindric_frontier_buttons = new HashMap<>();

    private Map<String, JRadioButton> createRadioButton(String[] round_buttons_options, ButtonGroup group){
        Map<String, JRadioButton> radio_button = new HashMap<>();
        for(String button_name: round_buttons_options){
            JRadioButton button = new JRadioButton(button_name);
            button.setFont(new Font(null, Font.PLAIN,20));
            button.setMnemonic(KeyEvent.VK_B);
            button.setActionCommand(button_name);
            button.setSelected(true);
            button.addActionListener(this);
            group.add(button);
            radio_button.put(button_name, button);

        }
        return radio_button;
    }

    private Map<String, JButton> createButtons(String[] button_names){

        Map<String, JButton> buttons_dict = new HashMap<String, JButton>();
        JButton button;

        for (String name: button_names) {
            button = new JButton(name);
            button.addActionListener(this);
            buttons_dict.put(name, button);
        }

        return buttons_dict;
    }

    private JPanel createButtonsPane(){

        gui_buttons = createButtons(buttons_names);
        JPanel buttons_pane = new JPanel();
        for(String button_name: buttons_names)
            buttons_pane.add(gui_buttons.get(button_name), BorderLayout.CENTER);

        buttons_pane.setPreferredSize(new Dimension(100, 5));
        buttons_pane.setMaximumSize(new Dimension(100, 5));
        buttons_pane.setMinimumSize(new Dimension(100, 5));
        buttons_pane.setOpaque(true);

        buttons_pane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Control"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));

        return buttons_pane;
    }

    private Object[] createTextFieldsAndLabels(Map<String, String> texts_labels){
        JLabel[] labels = new JLabel[texts_labels.size()];
        JTextField[] textFields = new JTextField[texts_labels.size()];
        int index = 0;

        for(Map.Entry<String, String> text_label: texts_labels.entrySet()){
            textFields[index] = new JTextField();
            textFields[index].setText(text_label.getValue());
            textFields[index].addFocusListener(this);
            labels[index] = new JLabel(text_label.getKey());
            labels[index].setLabelFor(textFields[index]);
            index++;
        }

        return new Object[]{labels, textFields};
    }

    private static JTextField[] input_variables_textfields;
    private static JLabel [] input_variables_labels;
    private static JLabel [] combobox_labels= {new JLabel("Initializer mode")};
    private static JLabel [] radio_button_labels = { new JLabel("Cilindric Frontier")};

    private static void initializeInputTextFieldsAndLabels(){
//        textfields_and_labels.put("Cells number (width): ", "300");//2
//        textfields_and_labels.put("Generations: ", "600");//3
        textfields_and_labels.put("States number: ", "2");//1
        textfields_and_labels.put("Neighborhood Range: ", "1");//4
        textfields_and_labels.put("Transition function: ", "90");//5
        textfields_and_labels.put("Seed: ", "1"); //0
        textfields_and_labels.put("Cell (temporal entropy): ","300");


        combobox_labels[0].setLabelFor(generator_list_combo_box);
    }
    private static void initializeButtonNames(){
        buttons_names = new String[]{"Initialize", "Start", "Stop"};
    }

    private JSplitPane createGuiPanels() {


        Object[]  labels_and_textfields_list = createTextFieldsAndLabels(textfields_and_labels);

        generator_list_combo_box = new JComboBox<>(engine_generator_names);
        generator_list_combo_box.addFocusListener(this);

        JComboBox[] combo_box_list = {generator_list_combo_box};

        JPanel input_variables_pane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();

        input_variables_pane.setLayout(gridbag);
        input_variables_pane.setPreferredSize(new Dimension(100, 900));
        input_variables_pane.setMinimumSize(new Dimension(100, 900));

        input_variables_labels = (JLabel[]) labels_and_textfields_list[0];
        input_variables_textfields = (JTextField[]) labels_and_textfields_list[1];

        cilindric_frontier_buttons = createRadioButton(round_buttons_options,cilindric_frontier);

        addLabelTextRows(input_variables_labels,input_variables_textfields, combobox_labels, combo_box_list,
                radio_button_labels, cilindric_frontier_buttons,
                input_variables_pane);

        input_variables_pane.setBorder(
                                   BorderFactory.createCompoundBorder(
                                                                      BorderFactory.createTitledBorder("Variables"),
                                                                      BorderFactory.createEmptyBorder(5,5,5,5)));
        input_variables_pane.setOpaque(true);
        JPanel buttons_pane = createButtonsPane();

        JSplitPane control_center_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              input_variables_pane,
                                              buttons_pane);
        control_center_pane.setMaximumSize(new Dimension(800,800));
        control_center_pane.setMinimumSize(new Dimension(800,800));
        input_variables_pane.setMaximumSize(new Dimension(800,800));
        input_variables_pane.setMinimumSize(new Dimension(800,800));

        control_center_pane.setOneTouchExpandable(true);
        control_center_pane.setOpaque(true);

        return control_center_pane;
    }

    private void addLabelTextRows(JLabel[] labels, JTextField[] textFields, JLabel[] combobox_labels,
                                  JComboBox<String>[] combo_box_list,JLabel[] radio_labels,
                                  Map<String, JRadioButton> radiobutton,
                                  Container container){

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        int numLabels = labels.length;
        int num_labels_combobox = combobox_labels.length;

        for (int i = 0; i < numLabels; i++){

        	labels[i].setFont(new Font(null, Font.PLAIN,20));
        	textFields[i].setFont(new Font(null, Font.PLAIN,20));
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 1.0;                       //reset to default
            container.add(labels[i], c);
 
            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.NONE;
            c.weightx = 1.0;
            textFields[i].setColumns(3);
            container.add(textFields[i], c);
        }

        for(int i =0; i < num_labels_combobox; i ++) {
            GuiCA1D.combobox_labels[i].setFont(new Font(null, Font.PLAIN,20));
            combo_box_list[i].setFont(new Font(null, Font.PLAIN,20));
            ((JLabel)combo_box_list[i].getRenderer()).setHorizontalAlignment(JLabel.CENTER);
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 1.0;                       //reset to default
            container.add(GuiCA1D.combobox_labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.NONE;
            c.weightx = 1.0;
            container.add(combo_box_list[i], c);
        }

        GuiCA1D.radio_button_labels[0].setFont(new Font(null, Font.PLAIN,20));
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 1.0;                       //reset to default
        container.add(GuiCA1D.radio_button_labels[0], c);
        c.gridwidth = GridBagConstraints.BASELINE_TRAILING;
        c.fill = GridBagConstraints.REMAINDER;
        c.weightx = 0;

        Box horizontal = Box.createHorizontalBox();


        for(Map.Entry<String,JRadioButton> button: radiobutton.entrySet()){
            horizontal.add(button.getValue());
        }
        container.add(horizontal,c);

    }

    private static  void createAndShowGUI(){

        chooseInputVariables(1,1,2);
        initializeButtonNames();
        initializeInputTextFieldsAndLabels();

        JFrame frame = new JFrame("CA1D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500,500));
        frame.setJMenuBar(new GuiCA1D().createNavBar());

        int xMax = cells_number;
        int yMax = generations;
        canvas_template = new MainCanvas(xMax, yMax);
        canvas_template.setOpaque(true);
        canvas_template.setDoubleBuffered(false);
        canvas_template.setPreferredSize(new Dimension(1000, 1000));

        JSplitPane buttons = new GuiCA1D().createGuiPanels();
        JSplitPane window = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, canvas_template, buttons);
        window.setOpaque(true);
        window.setOneTouchExpandable(true);
        frame.pack();
        frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setContentPane(window);

    }

    private static SwingWorker<Void, GuiCA1D> worker;

    private static MainCanvas canvas_template;

    private static double numeric_var = 33 ;
    private static String string_var = "Hello World";
    private static JLabel label_numeric_var_value;

    private static void chooseInputVariables(int n_string_variables, int n_numeric_variables, int n_label_variables){
        input_numeric_variables = new Double[n_numeric_variables];
        input_string_variables = new String[n_string_variables];
        input_label_variables = new JLabel[n_label_variables];
    }

    private static String[] input_string_variables;
    private static Double[] input_numeric_variables;
    private static JLabel[] input_label_variables;

    private static JLabel label_string_var_value;
    private static int value = 0;
    private static AnalyticsMultiChart population_chart;

    public void showURI(String uri){
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(uri));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void deleteCanvasLabels( JLabel[] labels){

        if(label_numeric_var_value != null) canvas_template.remove(label_numeric_var_value);
        if(label_string_var_value != null) canvas_template.remove(label_string_var_value);
    }

    private static int seed = 1;
    private static int states_number = 2;
    private static int neighborhood_range = 1;
    private static int transition_function = 90;
    private static int cfrontier = 0;
    private static int cells_number = 600;
    private static int generations = 600;
    private static int cell_temporal_entropy = 300;


    public void actionPerformed( ActionEvent e) {

        if(e.getSource() == nav_bar.getMenu(0).getItem(0)) {
//      frame.remove(window);
            value = 2;
            deleteCanvasLabels(input_variables_labels);
            MainCanvas.task.initializer(cells_number, generations, states_number,
                    neighborhood_range, transition_function, seed, cfrontier , initializer_mode, cell_temporal_entropy);
            canvas_template.updateCanvas();
        }

        if(e.getSource() == nav_bar.getMenu(0).getItem(1)) {
            value = 3;
            deleteCanvasLabels(input_variables_labels);
            MainCanvas.task.initializer(cells_number, generations, states_number,
                    neighborhood_range, transition_function, seed, cfrontier , initializer_mode, cell_temporal_entropy);
            canvas_template.updateCanvas();
        }

        if(e.getSource() == nav_bar.getMenu(1).getItem(0)){
            worker = new SwingWorker<Void, GuiCA1D>()
            {
                @Override
                protected Void doInBackground() {
                    try{
                        population_chart = new AnalyticsMultiChart("Population Chart",
                                "Generations", "Cells Number");
                        population_chart.setRef(MainCanvas.task);
                        population_chart.show();


                    }
                    catch(Exception ex){System.out.println("Worker exception");}
                    return null;
                }
            };
            worker.execute();
        }

        if(e.getSource()==nav_bar.getMenu(3).getItem(0)) {
            String uri = "https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html";
            showURI(uri);
        }

        if(e.getSource()==nav_bar.getMenu(4).getItem(0)) {
            String uri = "https://github.com/Jeffresh";
            showURI(uri);
        }

        if(e.getSource() == gui_buttons.get(buttons_names[0])) {

            if(cilindric_frontier_buttons.get("Yes").isSelected())
                cfrontier = 1;
            else cfrontier = 0;
            System.out.println("Cfrontier "+cfrontier);

            MainCanvas.task = new CellularAutomata1D();
            MainCanvas.task.plug(canvas_template);
            MainCanvas.task.initializer(cells_number, generations, states_number,
                    neighborhood_range, transition_function, seed, cfrontier , initializer_mode, cell_temporal_entropy);
            MainCanvas.setDimensions(cells_number, generations);
//            population_chart = new PopulationChart("Population Chart",
//                    "Generations", "Cells Number");
            if (population_chart != null){
                population_chart.setRef(MainCanvas.task);
                MainCanvas.task.plugPopulationChart(population_chart);
                population_chart.createSeries();
            }

            System.out.println("Cells number: "+cells_number);
            System.out.println("Generations: "+generations);
            System.out.println("State number: "+states_number);
            System.out.println("Neighborhood Range: "+ neighborhood_range);
            System.out.println("Transition_function: "+ transition_function);
            System.out.println("Seed: "+seed);
            System.out.println("Initializer mode: "+initializer_mode);
            System.out.println("Cell Spatial Entropy: "+ cell_temporal_entropy);


            canvas_template.updateCanvas();
        }

        if(e.getSource()== gui_buttons.get(buttons_names[1])) {
            worker = new SwingWorker<Void, GuiCA1D>()
            {
                @Override
                protected Void doInBackground() {
                    try{
                        MainCanvas.task.caComputation(generations);
                        MainCanvas.task.plugPopulationChart(population_chart);
                        population_chart.setRef(MainCanvas.task);
                        String message = "\"Temporal entropy of cell: "
                                + cell_temporal_entropy +"\n"
                                + "Temporal entropy value: "+MainCanvas.task.getTemporalEntropy();
                        JFrame dialog =  new JFrame();
                        dialog.setAlwaysOnTop(true);
                        JOptionPane.showMessageDialog(dialog, message, "Dialog",
                                JOptionPane.INFORMATION_MESSAGE);

                    }
                    catch(Exception ex){System.out.println("Worker exception");}
                    return null;
                }
            };
            worker.execute();


        }

        if(e.getSource()== gui_buttons.get(buttons_names[2])) {
            worker.cancel(true);
            worker.cancel(false);
            CellularAutomata1D.stop();
        }

    }


    public void focusGained(FocusEvent e) {
    	//nothing
	}
	public void focusLost(FocusEvent e) {
        String nump;
        if(e.getSource() == input_variables_textfields[0]) {
            nump = input_variables_textfields[0].getText();
            string_var = nump;
            states_number = Integer.parseInt(nump);
        }

        //Neighborhood Range
        try {
                double nump_value;
                if (e.getSource() == input_variables_textfields[1]) {
                    nump = input_variables_textfields[1].getText();
                    nump_value = Double.parseDouble(nump);
                    if (nump.equals("") || (nump_value < 0 || nump_value >=1000)) {
                        numeric_var = 0;
                        throw new Exception("Invalid Number");
                    }
                    neighborhood_range = Integer.parseInt(nump);
                }
            }
            catch (Exception ex){
                String message = "\"Invalid Number\"\n"
                        + "Enter a number between 0 and 1000\n"
                        + " setted 0 by default";
                JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                        JOptionPane.ERROR_MESSAGE);
            }
        //transition function
        if(e.getSource() == input_variables_textfields[2]) {
            nump = input_variables_textfields[2].getText();
            string_var = nump;
            transition_function = Integer.parseInt(nump);
        }

        //seed
        if(e.getSource() == input_variables_textfields[3]) {
            nump = input_variables_textfields[5].getText();
            seed = Integer.parseInt(nump);
        }

        if(e.getSource() == generator_list_combo_box) {
            JComboBox<String> cb = (JComboBox<String>)e.getSource();
            String op = (String)cb.getSelectedItem();
            assert op != null;
            initializer_mode = op;
        }

        if(e.getSource() == input_variables_textfields[0]) {
            nump = input_variables_textfields[0].getText();
            string_var = nump;
            states_number = Integer.parseInt(nump);
        }

        //cell temporal entropy
        try {
            double nump_value;
            if (e.getSource() == input_variables_textfields[4]) {
                nump = input_variables_textfields[4].getText();
                nump_value = Double.parseDouble(nump);
                if (nump.equals("") || (nump_value < 0 || nump_value >=600)) {
                    numeric_var = 0;
                    throw new Exception("Invalid Number");
                }
                cell_temporal_entropy = Integer.parseInt(nump);
            }
        }
        catch (Exception ex){
            String message = "\"Invalid Cell Number\"\n"
                    + "Enter a number between 0 and 599\n" ;
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args)
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.
                SwingUtilities.
                invokeLater(GuiCA1D::createAndShowGUI);


    }
}

