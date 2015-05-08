import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;


public class GenNNAndRes {
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
		int max_epoch = 1000;
		int step = 100;
		
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(AND_INPUT, AND_IDEAL);
		
		double[] ep = new double[max_epoch];
		double[] er = new double[max_epoch];
		double[] v = new double[max_epoch];
		
		for(int e=0; e<max_epoch; e++) {
			ep[e] = e;
		}
		
		BasicNetwork best_network = new BasicNetwork();
		double best_validation_error = Double.MAX_VALUE;
		int best_epoch = 0;
		int best_trail = 0;
		for (int k=0; k<AND_INPUT.length; k++) {
			double[][] inp = getCrossValidationSet(AND_INPUT, k);
			double[][] ideal = getCrossValidationSet(AND_IDEAL, k);
			
			// create training data
			MLDataSet trainingCVSet = new BasicMLDataSet(inp, ideal);
						
			BasicNetwork local_best_network = new BasicNetwork();
			double[] local_v = new double[max_epoch];
			double[] previous_v = new double[max_epoch];
			double[] current_v = new double[max_epoch];
			double[] box_validation_error = { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE };
			double local_best_validation_error = Double.MAX_VALUE;
			int local_best_epoch = 0;
			int previous_epoch = 0;
			int current_epoch = 0;
			int local_max_epoch = 0;
			for (int e=0; e<max_epoch; e+=step) {
				BasicNetwork network = createNetwork();
			
				final ResilientPropagation train = new ResilientPropagation(network, trainingCVSet);
				train.fixFlatSpot(false);
			
				int epoch = 0;
				do {
					train.iteration();
					epoch++;
				} while (epoch < e);
				
				double error = getValidationError(trainingSet, k, network);
				
				System.out.println("Trail: " + (k+1) + " | Epoch: " + e + " | Validation error: " + error);
				
				previous_v = current_v;
				previous_epoch = current_epoch;
				current_v[e] = error/AND_INPUT.length;
				current_epoch = e;
				box_validation_error[0] = box_validation_error[1];
				box_validation_error[1] = box_validation_error[2];
				box_validation_error[2] = error;
				if (getAvg(box_validation_error)<local_best_validation_error) {
					local_best_validation_error = box_validation_error[1];
					local_v = previous_v;
					local_best_epoch = previous_epoch;
					local_best_network = network;
				} else {
					if (e >= local_best_epoch + (3*step)) {
						local_max_epoch = e;
						break;
					}
				}
				
			}
			
			if (local_best_validation_error<best_validation_error) {
				v = local_v;
				best_validation_error = local_best_validation_error;
				best_epoch = local_best_epoch;
				best_network = local_best_network;
				best_trail = k+1;
				//max_epoch = local_max_epoch;
			}	

		}

		// test the neural network
		System.out.println("-----------------------------------------");
		System.out.println("Best Trail: " + best_trail + " | Best Epoch: " + best_epoch + " | Best Validation Error: " + best_validation_error);
		Encog.getInstance().shutdown();
		
		System.out.println("-----------------------------------------");
		
		for (int e=0; e<max_epoch; e+=step) {
			BasicNetwork network = createNetwork();
			
			final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
			train.fixFlatSpot(false);
			
			int epoch = 0;
			do {
				train.iteration();
				epoch++;
			} while (epoch < e);

			er[e] = train.getError();
			
			System.out.println("Epoch: " + e + " | Training error: " + train.getError());
		}

		plotGraph pg = new plotGraph("Error Graph", "Epoch", "Error rate", ep, er, v, (double)best_epoch);
		pg.plot();
	}
	
	private static double getAvg(double[] inp) {
		double sum = 0.0;
		for (int i=0; i<inp.length; i++) {
			if (sum != Double.MAX_VALUE) {
				sum += inp[i];
			} else {
				return Double.MAX_VALUE;
			}
		}
		return (sum/inp.length);
	}
	
	private static double getValidationError(MLDataSet trainingSet, int k, BasicNetwork network) {
		double avg_error = 0.0;
		double sum_square_error = 0.0;
		for (MLDataPair pair : trainingSet) {
			final MLData output = network.compute(pair.getInput());
			if (pair.getInput().getData(0) != AND_INPUT[k][0] && pair.getInput().getData(1) != AND_INPUT[k][1]) {
//				System.out.println(pair.getInput().getData(0) + ","
//					+ pair.getInput().getData(1) + ", actual="
//					+ output.getData(0) + ",ideal="
//					+ pair.getIdeal().getData(0));
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
		
//		System.out.println("Average Error: " + avg_error);
//		System.out.println("Weight Decay: " + weight_decay);
//		System.out.println("Learning Const: " + learning_const);
//		System.out.println("Sum Error: " + (learning_const * weight_decay + avg_error));
//		
		return (learning_const * weight_decay +  avg_error);
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

