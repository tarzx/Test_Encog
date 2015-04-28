import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;


public class TimeSlotInterventionNet {
	private final int[] SLOT = { 1, 2, 3, 4, 5, 6 };
	private final String fileName = "neuralNetTimeSlotIntervention.eg";
	private final int NUM_INPUT_BITS = 4;
	private final int NUM_OUTPUT_BITS = 6;
	public double INPUT[][] = { { 0.1, 0.15, 1.0, 1.0 }, { 0.1, 0.15, 1.0, 1.0 }, { 0.3, 0.15, 1.0, 1.0 }, { 0.5, 0.15, 1.0, 1.0 }, 
									   { 0.5, 0.15, 1.0, 1.0 }, { 0.7, 0.15, 1.0, 1.0 }, { 0.9, 0.15, 1.0, 1.0 }, { 0.9, 0.15, 1.0, 1.0 } };
	public double IDEAL[][] = { { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, 
									   { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, 
									   { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, 
									   { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }, { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 } };
	
	static boolean ready_to_parse = false;
	
	public TimeSlotInterventionNet() {
		createTrainingData();
	}
	
	public int getSlot(int idx) {
		if (idx<SLOT.length) {
			return SLOT[idx];
		} else { 
			return 0;
		}
	}
	
	public static void main(String[] args) {
		TimeSlotInterventionNet tsin = new TimeSlotInterventionNet();
		tsin.testoutput();
	}
	
	public double[] getRate(double controlLevel, int age, boolean isMale, double likeRate) {
		//Refine
		double[] input = { controlLevel, ((double)age)/100, (isMale?0.0:1.0), likeRate };
		double[] output = new double[6];
		
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
		double[] input1 =  { 0.1, 0.15, 1.0, 1.0};
		double[] input2 =  { 0.1, 0.15, 1.0, 1.0};
		double[] input3 =  { 0.3, 0.15, 1.0, 1.0};
		double[] input4 =  { 0.5, 0.15, 1.0, 1.0};
		double[] input5 =  { 0.5, 0.15, 1.0, 1.0};
		double[] input6 =  { 0.7, 0.15, 1.0, 1.0};
		double[] input7 =  { 0.9, 0.15, 1.0, 1.0};
		double[] input8 =  { 0.9, 0.15, 1.0, 1.0};
		
	    double[] output1 = new double[6];
	    double[] output2 = new double[6];
	    double[] output3 = new double[6];
	    double[] output4 = new double[6];
	    double[] output5 = new double[6];
	    double[] output6 = new double[6];
	    double[] output7 = new double[6];
	    double[] output8 = new double[6];
	    
	    net.compute(input1, output1);
	    net.compute(input2, output2);
	    net.compute(input3, output3);
	    net.compute(input4, output4);
	    net.compute(input5, output5);
	    net.compute(input6, output6);
	    net.compute(input7, output7);
	    net.compute(input8, output8);
	    
	    System.out.println("The output question is: " + output1[0] + " " + output1[1] + " " + output1[2] + " " + output1[3]
	    										+ " " + output1[4] + " " + output1[5]);
	    System.out.println("The output question is: " + output2[0] + " " + output2[1] + " " + output2[2] + " " + output2[3]
												+ " " + output2[4] + " " + output2[5]);
	    System.out.println("The output question is: " + output3[0] + " " + output3[1] + " " + output3[2] + " " + output3[3]
												+ " " + output3[4] + " " + output3[5]);
	    System.out.println("The output question is: " + output4[0] + " " + output4[1] + " " + output4[2] + " " + output4[3]
												+ " " + output4[4] + " " + output4[5]);
	    System.out.println("The output question is: " + output5[0] + " " + output5[1] + " " + output5[2] + " " + output5[3]
												+ " " + output5[4] + " " + output5[5]);
	    System.out.println("The output question is: " + output6[0] + " " + output6[1] + " " + output6[2] + " " + output6[3]
												+ " " + output6[4] + " " + output6[5]);
	    System.out.println("The output question is: " + output7[0] + " " + output7[1] + " " + output7[2] + " " + output7[3]
												+ " " + output7[4] + " " + output7[5]);
	    System.out.println("The output question is: " + output8[0] + " " + output8[1] + " " + output8[2] + " " + output8[3]
												+ " " + output8[4] + " " + output8[5]);
	    System.out.println();
   
	}
	
	public void writeFile(BasicNetwork network) {
		EncogDirectoryPersistence.saveObject(new File(fileName), network);
		ready_to_parse = true;
	}
}
