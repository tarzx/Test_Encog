import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;


public class SelectReplayGoalNet {
	
	private static String fileName = "neuralNetSelectReplayGoal.eg";
	private static int NUM_INPUT_BITS = 4;
	private static int NUM_OUTPUT_BITS = 1;
	public static double INPUT[][] = { { 0.1, 0.15, 1.0, 0.10 }, { 0.1, 0.50, 1.0, 0.10 }, { 0.3, 0.15, 1.0, 0.10 }, { 0.5, 0.15, 1.0, 0.10 }, 
									   { 0.5, 0.50, 1.0, 0.10 }, { 0.7, 0.15, 1.0, 0.10 }, { 0.9, 0.15, 1.0, 0.10 }, { 0.9, 0.50, 1.0, 0.10 },
									   { 0.1, 0.15, 1.0, 0.11 }, { 0.1, 0.50, 1.0, 0.11 }, { 0.3, 0.15, 1.0, 0.11 }, { 0.5, 0.15, 1.0, 0.11 },
									   { 0.5, 0.50, 1.0, 0.11 }, { 0.7, 0.15, 1.0, 0.11 }, { 0.9, 0.15, 1.0, 0.11 }, { 0.9, 0.50, 1.0, 0.11 }};
	public static double IDEAL[][] = { { 0.03 }, { 0.03 }, { 0.03 }, { 0.03 }, { 0.04 }, { 0.04 }, { 0.04 }, { 0.04 },
									   { 0.04 }, { 0.04 }, { 0.04 }, { 0.04 }, { 0.03 }, { 0.03 }, { 0.03 }, { 0.03 }};
	
	static boolean ready_to_parse = false;
	
	public SelectReplayGoalNet() {
		createTrainingData();
	}
	
	public static void main(String[] args) {
		SelectReplayGoalNet srgn = new SelectReplayGoalNet();
		srgn.testoutput();
	}
	
	public int getQuestionGroup(double controlLevel, int age, boolean isMale, int prevGroup) {
		//Refine
		double[] input = { controlLevel, ((double)age)/100, (isMale?0.0:1.0), ((double)prevGroup)/100 };
		double[] output = new double[1];
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fileName));
		net.compute(input, output);
		
		return (int) Math.floor(output[0]*100 + 0.5);
	}
	
	public static void createTrainingData() {
		AutomaticTraining ath = new AutomaticTraining();
		
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(INPUT, IDEAL);
		
		// Retrieve net
		BasicNetwork network = ath.calibrate(trainingSet, NUM_INPUT_BITS, NUM_OUTPUT_BITS);
		writeFile(network);
	}
	
	
	
	public static void testoutput(){
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fileName));
		double[] input1 =  { 0.1, 0.15, 1.0, 0.10};
		double[] input2 =  { 0.1, 0.50, 1.0, 0.10};
		double[] input3 =  { 0.3, 0.15, 1.0, 0.10};
		double[] input4 =  { 0.5, 0.15, 1.0, 0.10};
		double[] input5 =  { 0.5, 0.50, 1.0, 0.10};
		double[] input6 =  { 0.7, 0.15, 1.0, 0.10};
		double[] input7 =  { 0.9, 0.15, 1.0, 0.10};
		double[] input8 =  { 0.9, 0.50, 1.0, 0.10};
		double[] input9 =  { 0.1, 0.15, 1.0, 0.11};
		double[] input10 =  { 0.1, 0.50, 1.0, 0.11};
		double[] input11 =  { 0.3, 0.15, 1.0, 0.11};
		double[] input12 =  { 0.5, 0.15, 1.0, 0.11};
		double[] input13 =  { 0.5, 0.50, 1.0, 0.11};
		double[] input14 =  { 0.7, 0.15, 1.0, 0.11};
		double[] input15 =  { 0.9, 0.15, 1.0, 0.11};
		double[] input16 =  { 0.9, 0.50, 1.0, 0.11};
		
	    double[] output1 = new double[1];
	    double[] output2 = new double[1];
	    double[] output3 = new double[1];
	    double[] output4 = new double[1];
	    double[] output5 = new double[1];
	    double[] output6 = new double[1];
	    double[] output7 = new double[1];
	    double[] output8 = new double[1];
	    double[] output9 = new double[1];
	    double[] output10 = new double[1];
	    double[] output11 = new double[1];
	    double[] output12 = new double[1];
	    double[] output13 = new double[1];
	    double[] output14 = new double[1];
	    double[] output15 = new double[1];
	    double[] output16 = new double[1];
	    
	    net.compute(input1, output1);
	    net.compute(input2, output2);
	    net.compute(input3, output3);
	    net.compute(input4, output4);
	    net.compute(input5, output5);
	    net.compute(input6, output6);
	    net.compute(input7, output7);
	    net.compute(input8, output8);
	    net.compute(input9, output9);
	    net.compute(input10, output10);
	    net.compute(input11, output11);
	    net.compute(input12, output12);
	    net.compute(input13, output13);
	    net.compute(input14, output14);
	    net.compute(input15, output15);
	    net.compute(input16, output16);
	    
	    System.out.println("The output question is: " + output1[0] +" Hey " +  (int) Math.floor(output1[0]*100 + 0.5));
	    System.out.println("The output question is: " + output2[0] +" Hey " +  (int) Math.floor(output2[0]*100 + 0.5));
	    System.out.println("The output question is: " + output3[0] +" Hey " +  (int) Math.floor(output3[0]*100 + 0.5));
	    System.out.println("The output question is: " + output4[0] +" Hey " +  (int) Math.floor(output4[0]*100 + 0.5));
	    System.out.println("The output question is: " + output5[0] +" Hey " +  (int) Math.floor(output5[0]*100 + 0.5));
	    System.out.println("The output question is: " + output6[0] +" Hey " +  (int) Math.floor(output6[0]*100 + 0.5));
	    System.out.println("The output question is: " + output7[0] +" Hey " +  (int) Math.floor(output7[0]*100 + 0.5));
	    System.out.println("The output question is: " + output8[0] +" Hey " +  (int) Math.floor(output8[0]*100 + 0.5));
	    System.out.println("The output question is: " + output9[0] +" Hey " +  (int) Math.floor(output9[0]*100 + 0.5));
	    System.out.println("The output question is: " + output10[0] +" Hey " +  (int) Math.floor(output10[0]*100 + 0.5));
	    System.out.println("The output question is: " + output11[0] +" Hey " +  (int) Math.floor(output11[0]*100 + 0.5));
	    System.out.println("The output question is: " + output12[0] +" Hey " +  (int) Math.floor(output12[0]*100 + 0.5));
	    System.out.println("The output question is: " + output13[0] +" Hey " +  (int) Math.floor(output13[0]*100 + 0.5));
	    System.out.println("The output question is: " + output14[0] +" Hey " +  (int) Math.floor(output14[0]*100 + 0.5));
	    System.out.println("The output question is: " + output15[0] +" Hey " +  (int) Math.floor(output15[0]*100 + 0.5));
	    System.out.println("The output question is: " + output16[0] +" Hey " +  (int) Math.floor(output16[0]*100 + 0.5));
	    System.out.println();
   
	}
	
	public static void writeFile(BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(new File(fileName), network);
		ready_to_parse = true;
	}

}

