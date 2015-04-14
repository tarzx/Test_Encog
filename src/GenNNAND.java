import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;


public class GenNNAND {
	/**
	 * The input necessary for AND.
	 */
	public static double AND_INPUT[][] = { { 1.0, 0.0 }, { 0.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for AND.
	 */
	public static double AND_IDEAL[][] = { { 0.0 }, { 0.0 }, { 0.0 }, { 1.0 } };

	/**
	 * The main method.
	 * 
	 * @param args
	 *            No arguments are used.
	 */
	public static void main(final String args[]) {
	
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(AND_INPUT, AND_IDEAL);
		
		// train the neural network
		BasicNetwork bestNetwork = new BasicNetwork();
		int max_epoch = 100;
		double error_threshold = 1E-5;
		double lowest_error = Double.MAX_VALUE;
		double learning_rate = 0.0;
		double momentum = 0.0;
		int best_epoch = 0;
			
		for (double i=0.01; i<1.0; i+=0.01) {
			for (double j=0.01; j<1; j+=0.01) {
				BasicNetwork network = createNetwork();
		
				final Backpropagation train = new Backpropagation(network, trainingSet, i, j);
				train.fixFlatSpot(false);
				
				int epoch = 0;
				do {
					train.iteration();
					epoch++;
				} while (train.getError() > error_threshold && epoch < max_epoch);
				
				System.out.println("Learning Rate: " + i + " Momentum: " + j);
				System.out.println("Error:" + train.getError());
					
				if (train.getError() < lowest_error) {
					bestNetwork = (BasicNetwork) network.clone();
					lowest_error = train.getError();
					learning_rate = i;
					momentum = j;
					best_epoch = epoch;
				}
			}
		}
		
		System.out.println("The lowest error for epoch #" + best_epoch);
		System.out.println("Learning Rate: " + learning_rate + " Momentum: " + momentum);
		System.out.println("Error:" + lowest_error);
		System.out.println(bestNetwork.dumpWeights());
		
		double sum_error = 0.0;
		for (int i=0; i<AND_INPUT.length; i++) {
			double[][] inp = getCrossValidationSet(AND_INPUT, i);
			double[][] ideal = getCrossValidationSet(AND_IDEAL, i);
			
			BasicNetwork network = createNetwork();
			
			// create training data
			MLDataSet trainingCVSet = new BasicMLDataSet(inp, ideal);
			
			final Backpropagation train = new Backpropagation(network, trainingCVSet, learning_rate, momentum);
			train.fixFlatSpot(false);
			
			int epoch = 0;
			do {
				train.iteration();
				epoch++;
			} while (train.getError() > error_threshold && epoch < max_epoch);
			
			System.out.println("-----------------------------------------");
			System.out.println("The lowest error for epoch #" + epoch);
			System.out.println("Training Error: " + network.calculateError(trainingCVSet));
			System.out.println(network.dumpWeights());
			
			double avg_error = 0.0;
			double sum_square_error = 0.0;
			for (MLDataPair pair : trainingSet) {
				final MLData output = network.compute(pair.getInput());
				if (pair.getInput().getData(0) != AND_INPUT[i][0] && pair.getInput().getData(1) != AND_INPUT[i][1]) {
					System.out.println(pair.getInput().getData(0) + ","
						+ pair.getInput().getData(1) + ", actual="
						+ output.getData(0) + ",ideal="
						+ pair.getIdeal().getData(0));
					sum_square_error += Math.pow((output.getData(0))-(pair.getIdeal().getData(0)), 2);
				}
			}
			avg_error = sum_square_error/AND_INPUT.length;
			
			double[] weights = network.getFlat().getWeights();
			double weight_decay = 0.0;
			for (int j=0; j<weights.length; j++) {
				weight_decay += Math.pow(weights[j],2);
			}
			
			double learning_const = 1;
			while (weight_decay * learning_const > avg_error) {
				learning_const /= 10;
			}
			
			sum_error += (learning_const * weight_decay +  avg_error);
			
			System.out.println("Average Error: " + avg_error);
			System.out.println("Weight Decay: " + weight_decay);
			System.out.println("Learning Const: " + learning_const);
			System.out.println("Sum Error: " + (learning_const * weight_decay + avg_error));
		}

		// test the neural network
		System.out.println("-----------------------------------------");
		System.out.println("Neural Network Results: " + sum_error/AND_INPUT.length);
		Encog.getInstance().shutdown();
	}
	
	
	
	private static BasicNetwork createNetwork() {
		// create a neural network, without using a factory
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 2));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		network.getStructure().finalizeStructure();
		network.reset();
		
		//double[] weights = new double[network.getFlat().getWeights().length];
		//network.getFlat().setWeights(weights);
		
		new ConsistentRandomizer(-1, 1, 500).randomize(network);
		//System.out.println(network.dumpWeights());
		
		return network;
	}
	
	private static double[][] getCrossValidationSet(double[][] inp, int n) {
		double[][] cvSet = new double[inp.length-1][inp[0].length];
		int x = 0;
		for (int i=0; i<inp.length; i++) {
			if (i!=n) {
				for (int j=0; j<inp[0].length; j++) {
					cvSet[x][j] = inp[i][j];
				}	
				x++; 
			}
		}
		return cvSet;
	}
}
