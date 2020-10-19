/*
 Copyright (c) Prabuddha Banerjee
 *
 */
package javaai.ann.basic;

import javaai.ann.output.Ontology;
import javaai.util.Helper;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.mathutil.Equilateral;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Prabuddha Banerjee
 * @date 13 Oct 2020
 */
public class PrabuddhazIris {
    /** Error tolerance */
    public final static double TOLERANCE = 0.01;

    /** Data used for Training Purposes**/
    public final static double TRAINING_PERCENT = 0.80;

    /** The input necessary for XOR. */
    public static double XOR_INPUTS[][] = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
    };

    /** The ideal data necessary for XOR.*/
    public static double XOR_IDEALS[][] = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}};

    /** The high range index */
    public final static int HI = 1;

    /** The low range index */
    public final static int LO = 0;

    public static double[][] TRAINING_INPUTS;
    public static double[][] TRAINING_IDEALS;
    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String[] args) {
        //Initialize init method
        init();

        // Build the network
        BasicNetwork network = new BasicNetwork();

        // Input layer plus bias node
        // network.addLayer(new BasicLayer(null, true, 2));
        network.addLayer(new BasicLayer(null, true, 4));

        // Hidden layer plus bias node
        // network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 4));

        // Output layer
        // network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
        network.addLayer(new BasicLayer(new ActivationTANH(), false, 2));

        // No more layers to be added
        network.getStructure().finalizeStructure();

        // Randomize the weights
        network.reset();

        Helper.describe(network);

        // Create training data
        // MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUTS, XOR_IDEALS);
        MLDataSet trainingSet = new BasicMLDataSet(TRAINING_INPUTS, TRAINING_IDEALS);

        // Use a training object for the learning algorithm, in this case, an improved
        // backpropagation. For details on what this does see the javadoc.
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        // Set learning batch size: 0 = batch, 1 = online, n = batch size
        // See org.encog.neural.networks.training.BatchSize
        // train.setBatchSize(0);

        int epoch = 0;

        Helper.log(epoch, train,false);
        do {
            train.iteration();

            epoch++;

            Helper.log(epoch, train,false);

        } while (train.getError() > TOLERANCE && epoch < Helper.MAX_EPOCHS);

        train.finishTraining();

        Helper.log(epoch, train,true);
        Helper.report(trainingSet, network);
        Helper.describe(network);

        Encog.getInstance().shutdown();
    }

    protected static double[] getRange(List<Double> list) {
        // Initial low and high values
        double[] range = {Double.MAX_VALUE, -Double.MAX_VALUE};

        // Go through each value in the list
        for(Double value: list) {
            // Assign if value greater than range[HI], update range[HI].
            if( value > range[HI])
                range[HI] = value;

            // Assign if value less than range[LO], update range[LO].
            if( range[LO] > value)
                range[LO] = value;
        }

        return range;
    }


    protected static double[][] getInputs() {
        // Container for normalized data
        HashMap<String, List<Double>> normals = new HashMap<>( );

        //traverse all the headers of Helper.headers
        for(String title: Helper.headers) {

            //cast value to List<Double>
            List<Double> col = (List<Double>) Helper.data.get(title);

            //col value is not null
            if ((col == null || col.isEmpty()) || (!(col.get(0) instanceof Double))) {
                continue;
            }
            double[] range = getRange(col);

            // Create a NormalizedField instance using the hi-lo range.
            NormalizedField norm = new NormalizedField(NormalizationAction.Normalize,
                    null, range[HI], range[LO], 1, -1);

            //Container for normalized data
            List<Double> normalized = new ArrayList<>();

            // Data Normalized and added  to the list of normalized values.
            for (Double values : col) {
                normalized.add(norm.normalize(values));
            }
            normals.put(title, normalized);
        }
        // keySet returns an Object array, not a String array
        Object[] keys = normals.keySet().toArray();

        // Transfer header names to String array--we use it later
        String[] cols = new String[keys.length];
        System.arraycopy(keys, 0, cols, 0, keys.length);

        // Allocate the 2D storage
        int numRows = Helper.getRowCount();
        int numCols = cols.length;

        //initialize input
        double[][] inputs = new double[numRows][numCols];

        // Transfer data from normals to inputs
        for(int i=0; i<numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                List<Double> col = normals.get(keys[j]);
                inputs[i][j] = col.get(i);
            }
        }
        return inputs;
    }
    /***
     * This is the init method for initalizing data.
     * @return Nothing.
     *
     */
    protected static void init(){

        try {
            //Load data
            Helper.loadCsv("iris.csv", Ontology.parsers);

            // Create an instance of this class
            PrabuddhazIris pIris = new PrabuddhazIris();

            double[][] inputs = pIris.getInputs();
            double[][] ideals = pIris.getIdeals();


            // Print the column headers
            System.out.println("Iris normalized data inputs");
            System.out.println("---------------------------");
            System.out.printf("%3s "," ");

            for (int i = 0; i < Helper.headers.size()-1; i++){
                System.out.printf("%15s ",Helper.headers.get(i));
            }
            System.out.print("\n");

            // Loop through the data and print
            for(int i = 0; i < inputs.length; i++){
                System.out.printf("%3s ", i);
                for(int j = 0; j < inputs[i].length; j++) {
                    System.out.printf("%15.2f", inputs[i][j]);
                }
                System.out.print("\n");
            }
            //Call print method
            printIdeals(ideals);
            int row = getTrainingEndIndex() - getTrainingStartIndex()+1;
            assert(row == 120);
            int colTraining = inputs[0].length;
            System.out.println("inputs: "+ row+":"+colTraining);
            TRAINING_INPUTS = new double[row][colTraining];
            for(int i = 0; i<row; i++){
                for(int j = 0; j<colTraining; j++){
                    TRAINING_INPUTS[i][j] = inputs[i][j];
                }
            }

            int colTesting = ideals[0].length;
            System.out.println("ideals: "+ row+":"+colTesting);
            TRAINING_IDEALS = new double[row][colTesting];
            for(int i = 0; i<row; i++){
                for(int j = 0; j<colTesting; j++){
                    TRAINING_IDEALS[i][j] = ideals[i][j];
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    protected static double[][] getIdeals() throws Exception {

        //Initialize zero-based column index
        final int SPECIES = 4;

        List<String> subtypes = Helper.getNominalSubtypes(SPECIES);

        //Instantiate the 2D array to contain the ideals
        double[][] ideals = new double[Helper.getRowCount()][];

        //Map subtype to an index number to get the equilateral encoding
        HashMap<String,Integer> subtypeToNumber = new HashMap<>(); Integer number = 0;

        for(String subtype: subtypes) {
            subtypeToNumber.put(subtype,number);
            number++;
        }

        //Construct the equilateral encoder
        Equilateral eq = new Equilateral(subtypes.size(), 1.0, -1.0);

        //Get the species column name
        String col = Helper.headers.get(SPECIES);

        //Convert every species string name in that column to an equilateral encoding
        for(int rowno=0; rowno < Helper.getRowCount(); rowno++) {
            // Get the nominal as a string name
            String nominal = (String) Helper.data.get(col).get(rowno);

            // Convert the name to a subspecies index number
            number = subtypeToNumber.get(nominal);

            if(number == null)
                throw new Exception("bad nominal: "+nominal);

            // Encode the number as vertex in n-1 dimensions
            double[] encoding = eq.encode(number);

            // Save the vertex encoding as columns for this row
            ideals[rowno] = encoding;

        }
        return ideals;
    }
    private static void printIdeals(double[][] ideals){
        // Print the column headers
        System.out.println("Iris encoded data outputs");
        System.out.println("---------------------------");
        System.out.printf("%4s %8s %9s %3s %-12s", "Index", "y1", "y2", " ", "Decoding");

        System.out.print("\n");
        //Print data
        for(int i=0; i<ideals.length; i++){
            System.out.printf("%4s ", (i+1)+":");
            for(int j=0; j<ideals[0].length; j++){
                System.out.printf("%10.2f", ideals[i][j]);
            }
            System.out.printf("%3s %-12s"," ",Helper.data.get(Helper.headers.get(4)).get(i));
            System.out.print("\n");
        }
    }

    public static final int getTrainingStartIndex(){
        return 0;
    }

    public static final int getTrainingEndIndex(){
        return (int) (Helper.getRowCount()*TRAINING_PERCENT + 0.50-1);
    }

    public static final int getTestingStartIndex(){
        return (int) (Helper.getRowCount()*TRAINING_PERCENT + 0.50);
    }

    public static final int getTestingEndIndex(){
        return Helper.getRowCount()-1;
    }
}
