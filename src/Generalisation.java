import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.RPROPType;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;

import au.com.bytecode.opencsv.CSVReader;


public class Generalisation {
    private static double[] outputFirstQuestion = new double[1];

    public static void RunNet(String fileName) {
    	// Create training data
		NeuralNetUtil.trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
				fileName, true, NeuralNetUtil.NUM_INPUT_BITS, NeuralNetUtil.NUM_OUTPUT_BITS);
		CrossValidation.setTraining(NeuralNetUtil.interventionNetCSV, NeuralNetUtil.netCSVset);
    	
    	// Retrieve net
		System.out.println("Back Propagation---------------------------");
		BasicNetwork network = incrementNeuron();
		NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetBackfile, network);
		System.out.println("Resilient RPROPp---------------------------");
		BasicNetwork networkRp = incrementNeuron(RPROPType.RPROPp);
		NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileRp, networkRp);
		//System.out.println("---------------------------");
		//BasicNetwork networkRp = incrementNeuron(RPROPType.RPROPm);
		//NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileRm, networkRm);
		//System.out.println("---------------------------");
		// Retrieve net - iRPROP+
		//BasicNetwork networkRp = incrementNeuron(RPROPType.iRPROPp);
		//NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileiRp, networkiRp);
		//System.out.println("---------------------------");
		// Retrieve net - iRPROP-
		//BasicNetwork networkRp = incrementNeuron(RPROPType.iRPROPm);
		//NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileiRm, networkiRm);

	}
    
    private static BasicNetwork incrementNeuron() {
    	BasicNetwork bestNet = null;
    	int neurons = 0;
    	double best_error = Double.MAX_VALUE;
    	double last_avg_error = 0.0;
    	double[] last_weights = null;
    	  	
    	while (best_error > NeuralNetUtil.THRESHOLD) {
    		neurons++;
    		int epoch = 100;
    		double previous_error = Double.MAX_VALUE;
    		while(epoch < NeuralNetUtil.MAX_EPOCH) {
    			NeuralNetBack nnback = new NeuralNetBack();
    			BasicNetwork network = nnback.createTrainedNet(NeuralNetUtil.trainingSet, neurons, epoch);
    			double learning = nnback.getLearningRate();
    			double momentum = nnback.getMomentum();
    			
    			double sum_error = 0.0;
    			for (int i=0; i<NeuralNetUtil.nSet; i++) {
    				BasicNetwork subNet = nnback.createTrainedNet(NeuralNetUtil.trainingSets[i], neurons, learning, momentum, epoch);
    				NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetset[i], subNet);
    				
    				sum_error += checkError(NeuralNetUtil.netCSVset[i], i);
    			}
    			double avg_error = sum_error/NeuralNetUtil.nSet;
    			double[] weights = network.getFlat().getWeights();
    			double weight_decay = 0.0;
    			double sum_weight = 0.0;
    			for (int j=0; j<weights.length; j++) {
    				//System.out.println(j + ": " + bestNet.getFlat().getWeights()[j]);
    				weight_decay += Math.pow(weights[j],2);
    				sum_weight += Math.abs(weights[j]);
    			}
    			
    			if (weight_decay + avg_error > previous_error) {
    				best_error = previous_error;
    				bestNet = network;
    				break;
    			} else {
    				previous_error = (weight_decay/sum_weight) + avg_error;
    				last_weights = weights;
    				last_avg_error = avg_error;
    			}
    			
    			epoch += 100;
    		}
    		printDetail(bestNet, best_error, last_weights, last_avg_error, epoch);
    	}
    	
    	return bestNet;
    }
    
    private static BasicNetwork incrementNeuron(RPROPType r) {
    	BasicNetwork bestNet = null;
    	int neurons = 0;
    	double best_error = Double.MAX_VALUE;
    	double last_avg_error = 0.0;
    	double[] last_weights = null;
 
    	while (best_error > NeuralNetUtil.THRESHOLD) {
    		neurons++;
    		int epoch = 100;
    		double previous_error = Double.MAX_VALUE;
    		while(epoch < NeuralNetUtil.MAX_EPOCH) {
    			BasicNetwork network = NeuralNetResilient.createTrainedNet(NeuralNetUtil.trainingSet, r, neurons, epoch);
    			
    			double sum_error = 0.0;
    			for (int i=0; i<NeuralNetUtil.nSet; i++) {
    				BasicNetwork subNet = NeuralNetResilient.createTrainedNet(NeuralNetUtil.trainingSets[i], r, neurons, epoch);
    				NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetset[i], subNet);
    				
    				sum_error += checkError(NeuralNetUtil.netCSVset[i], i);
    			}
    			double avg_error = sum_error/NeuralNetUtil.nSet;
    			double[] weights = network.getFlat().getWeights();
    			double weight_decay = 0.0;
    			double sum_weight = 0.0;
    			for (int j=0; j<weights.length; j++) {
    				//System.out.println(j + ": " + bestNet.getFlat().getWeights()[j]);
    				weight_decay += Math.pow(weights[j],2);
    				sum_weight += Math.abs(weights[j]);
    			}
    			
    			if ((weight_decay/sum_weight) + avg_error > previous_error) {
    				best_error = previous_error;
    				bestNet = network;
    				break;
    			} else {
    				previous_error = (weight_decay/sum_weight) + avg_error;
    				last_weights = weights;
    				last_avg_error = avg_error;
    			}
    			
    			epoch += 100;
    		}
    		printDetail(bestNet, best_error, last_weights, last_avg_error, epoch);
    	}
    	
    	return bestNet;
    }
    
	private static void printDetail(BasicNetwork bestNet, double best_error, double[] weights, double avg, int epoch) {
		System.out.println("Generalisation Done!");
		System.out.println("Best neurons: "+ bestNet.getLayerNeuronCount(1) +
				"\nEpoch: " + epoch +
				"\nLowest error: " + best_error);
		System.out.print("Weight: ");
		double weight_decay = 0.0;
		double sum_weight = 0.0;
		for (int i=0; i<weights.length; i++) {
			weight_decay += Math.pow(weights[i], 2);
			sum_weight += Math.abs(weights[i]);
			System.out.print(weights[i] + ", ");
		}
		System.out.println("\nWeight Decay: " + weight_decay +
				"\nWeight Sum: " + sum_weight +
				"\nWeight Decay/Sum: " + weight_decay/sum_weight +
				"\nAverage error: " + avg);
		System.out.println("--------------------------------------");
	}
    
    private static double checkError(String csvSet, int set) {
    	int n=0;
    	double sum_square_error = 0.0;
    	
    	try {
			CSVReader reader = new CSVReader(new FileReader(csvSet));
			try {
				String [] nextLine;
				int loopCounter = 0;
				while ((nextLine = reader.readNext()) != null) {
					if (loopCounter > 0 && loopCounter%NeuralNetUtil.nSet==set) {
						double[] input = {Double.valueOf(nextLine[0]), Double.valueOf(nextLine[1]), Double.valueOf(nextLine[2])};
						double target = Double.valueOf(nextLine[3]);
						double output = computeFirstQuestion(NeuralNetUtil.neuralNetset[set], input);
						sum_square_error += Math.pow(output-target, 2);
						n++;
					}
					loopCounter++;
				}
				
				reader.close();
					
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return sum_square_error / n;
    }

    
    protected static double computeFirstQuestion(File inpNet, double[] input){
        loadNet(inpNet, input);
        return Math.floor(outputFirstQuestion[0]*10 + 0.5)/10  ;
    }

    public static void loadNet(File inpNet, double[] input){
    	BasicNetwork netWork = (BasicNetwork) EncogDirectoryPersistence.loadObject(inpNet);
        netWork.compute(input, outputFirstQuestion);
    }
}
