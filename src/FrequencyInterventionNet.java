import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;


public class FrequencyInterventionNet {
	private static String fileName = "neuralNetFreqIntervention.eg";
	private static int NUM_INPUT_BITS = 4;
	private static int NUM_OUTPUT_BITS = 1;
	public static double INPUT[][] = { { 0.1, 0.15, 1.0, 1.0 }, { 0.1, 0.15, 1.0, 1.0 }, { 0.3, 0.15, 1.0, 1.0 }, { 0.5, 0.15, 1.0, 1.0 }, 
									   { 0.5, 0.15, 1.0, 1.0 }, { 0.7, 0.15, 1.0, 1.0 }, { 0.9, 0.15, 1.0, 1.0 } };
	public static double IDEAL[][] = { { 0.1 }, { 0.2 }, { 0.3 }, { 0.4 }, { 0.5 }, { 0.6 }, { 0.7 } };
	
	static boolean ready_to_parse = false;
	
	public FrequencyInterventionNet() {
		createTrainingData();
	}
	
	public static void main(String[] args) {
		FrequencyInterventionNet fin = new FrequencyInterventionNet();
		fin.testoutput();
	}
	
	public double getRate(double controlLevel, int age, boolean isMale, double likeRate) {
		//Refine
		double[] input = { controlLevel, ((double)age)/100, (isMale?0.0:1.0), likeRate };
		double[] output = new double[0];
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fileName));
		net.compute(input, output);
		
		return output[0];
	}
	
	public void createTrainingData() {
		AutomaticTraining ath = new AutomaticTraining();
		
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(INPUT, IDEAL);
		
		// Retrieve net
		BasicNetwork network = ath.calibrate(trainingSet, NUM_INPUT_BITS, NUM_OUTPUT_BITS);
		writeFile(network);
	}
	
	public void testoutput(){
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fileName));
		double[] input1 =  { 0.1, 0.15, 1.0, 1.0 };
		double[] input2 =  { 0.1, 0.15, 1.0, 1.0 };
		double[] input3 =  { 0.3, 0.15, 1.0, 1.0 };
		double[] input4 =  { 0.5, 0.15, 1.0, 1.0 };
		double[] input5 =  { 0.5, 0.15, 1.0, 1.0 };
		double[] input6 =  { 0.7, 0.15, 1.0, 1.0 };
		double[] input7 =  { 0.9, 0.15, 1.0, 1.0 };
		
	    double[] output1 = new double[1];
	    double[] output2 = new double[1];
	    double[] output3 = new double[1];
	    double[] output4 = new double[1];
	    double[] output5 = new double[1];
	    double[] output6 = new double[1];
	    double[] output7 = new double[1];
	    
	    net.compute(input1, output1);
	    net.compute(input2, output2);
	    net.compute(input3, output3);
	    net.compute(input4, output4);
	    net.compute(input5, output5);
	    net.compute(input6, output6);
	    net.compute(input7, output7);
	    
	    System.out.println("The output frequency is: " + output1[0]);
	    System.out.println("The output frequency is: " + output2[0]);
	    System.out.println("The output frequency is: " + output3[0]);
	    System.out.println("The output frequency is: " + output4[0]);
	    System.out.println("The output frequency is: " + output5[0]);
	    System.out.println("The output frequency is: " + output6[0]);
	    System.out.println("The output frequency is: " + output7[0]);
	    System.out.println();
   
	}
	
	public static void writeFile(BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(new File(fileName), network);
		ready_to_parse = true;
	}
}
