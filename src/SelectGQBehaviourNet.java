import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;


public class SelectGQBehaviourNet {
	private final int[] GROUP = { 10, 11 };
	private final String fileName = "neuralNetSelectGQBehaviour.eg";
	private final int NUM_INPUT_BITS = 4;
	private final int NUM_OUTPUT_BITS = 2;
	private final double INPUT[][] = { { 0.1, 0.15, 1.0, 0.10 }, { 0.1, 0.50, 1.0, 0.10 }, { 0.3, 0.15, 1.0, 0.10 }, { 0.5, 0.15, 1.0, 0.10 }, 
									   { 0.5, 0.50, 1.0, 0.10 }, { 0.7, 0.15, 1.0, 0.10 }, { 0.9, 0.15, 1.0, 0.10 }, { 0.9, 0.50, 1.0, 0.10 },
									   { 0.1, 0.15, 1.0, 0.11 }, { 0.1, 0.50, 1.0, 0.11 }, { 0.3, 0.15, 1.0, 0.11 }, { 0.5, 0.15, 1.0, 0.11 },
									   { 0.5, 0.50, 1.0, 0.11 }, { 0.7, 0.15, 1.0, 0.11 }, { 0.9, 0.15, 1.0, 0.11 }, { 0.9, 0.50, 1.0, 0.11 }};
	private final double IDEAL[][] = { { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, 
									   { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 },
									   { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, 
									   { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }, { 0.5, 0.5 }};
	
	public boolean ready_to_parse = false;
	
	public SelectGQBehaviourNet() {
		createTrainingData();
	}
	
	public int getGroupQuestion(int idx) {
		if (idx<GROUP.length) {
			return GROUP[idx];
		} else { 
			return 0;
		}
	}
	
	public static void main(String[] args) {
		SelectGQBehaviourNet sgqb = new SelectGQBehaviourNet();
		sgqb.testoutput();
	}
	
	public double[] getRate(double controlLevel, int age, boolean isMale, int prevGroup) {
		//Refine
		double[] input = { controlLevel, ((double)age)/100, (isMale?0.0:1.0), ((double)prevGroup)/100 };
		double[] output = new double[1];
		
		BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fileName));
		net.compute(input, output);
		
		return output;
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
		
	    double[] output1 = new double[2];
	    double[] output2 = new double[2];
	    double[] output3 = new double[2];
	    double[] output4 = new double[2];
	    double[] output5 = new double[2];
	    double[] output6 = new double[2];
	    double[] output7 = new double[2];
	    double[] output8 = new double[2];
	    double[] output9 = new double[2];
	    double[] output10 = new double[2];
	    double[] output11 = new double[2];
	    double[] output12 = new double[2];
	    double[] output13 = new double[2];
	    double[] output14 = new double[2];
	    double[] output15 = new double[2];
	    double[] output16 = new double[2];
	    
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
	    
	    System.out.println("The output possibility for questions are: " + output1[0] + " "  + output1[2]);
	    System.out.println("The output possibility for questions are: " + output2[0] + " "  + output2[2]);
	    System.out.println("The output possibility for questions are: " + output3[0] + " "  + output3[2]);
	    System.out.println("The output possibility for questions are: " + output4[0] + " "  + output4[2]);
	    System.out.println("The output possibility for questions are: " + output5[0] + " "  + output5[2]);
	    System.out.println("The output possibility for questions are: " + output6[0] + " "  + output6[2]);
	    System.out.println("The output possibility for questions are: " + output7[0] + " "  + output7[2]);
	    System.out.println("The output possibility for questions are: " + output8[0] + " "  + output8[2]);
	    System.out.println("The output possibility for questions are: " + output9[0] + " "  + output9[2]);
	    System.out.println("The output possibility for questions are: " + output10[0] + " "  + output10[2]);
	    System.out.println("The output possibility for questions are: " + output11[0] + " "  + output11[2]);
	    System.out.println("The output possibility for questions are: " + output12[0] + " "  + output12[2]);
	    System.out.println("The output possibility for questions are: " + output13[0] + " "  + output13[2]);
	    System.out.println("The output possibility for questions are: " + output14[0] + " "  + output14[2]);
	    System.out.println("The output possibility for questions are: " + output15[0] + " "  + output15[2]);
	    System.out.println("The output possibility for questions are: " + output16[0] + " "  + output16[2]);
	    System.out.println();
   
	}
	
	public void writeFile(BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(new File(fileName), network);
		ready_to_parse = true;
	}
}
