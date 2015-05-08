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


public class GenNNXOR {
	/**
	 * The input necessary for AND.
	 */
	public static double AND_INPUT[][] = { { 1.0, 0.0 }, { 0.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for AND.
	 */
	public static double AND_IDEAL[][] = { { 1.0 }, { 0.0 }, { 1.0 }, { 0.0 } };

	/**
	 * The main method.
	 * 
	 * @param args
	 *            No arguments are used.
	 */
	public static void main(final String args[]) {
		double MAX_LEARNING_RATE = 1.0;
		double MAX_MOMENTUM = 1.0;
		double STEP_PROPAGATION = 0.1;
		double ERROR_THRESHOLD = 1E-50;
		int MAX_EPOCH = 10000;
		int STEP_EPOCH = 1000;
		int MAX_NEURONS = 4;
		
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(AND_INPUT, AND_IDEAL);
		
		double[] ep = new double[MAX_EPOCH/STEP_EPOCH];
		double[] er = new double[MAX_EPOCH/STEP_EPOCH];
		double[] v = new double[MAX_EPOCH/STEP_EPOCH];
		
		for(int e=STEP_EPOCH-1; e<=MAX_EPOCH; e+=STEP_EPOCH) {
			ep[e/STEP_EPOCH] = e+1;
		}
		
		BasicNetwork best_network = new BasicNetwork();
		double best_validation_error = Double.MAX_VALUE;
		int best_epoch = 0;
		int best_trail = 0;
		int best_neurons = 0;
		for (int k=0; k<AND_INPUT.length; k++) {
			double[][] inp = getCrossValidationSet(AND_INPUT, k);
			double[][] ideal = getCrossValidationSet(AND_IDEAL, k);
			
			// create training data
			MLDataSet trainingCVSet = new BasicMLDataSet(inp, ideal);
						
			BasicNetwork local_best_network = new BasicNetwork();
			BasicNetwork previous_local_best_network = new BasicNetwork();
			double[] local_v = new double[MAX_EPOCH];
			double[] previous_local_v = new double[MAX_EPOCH];
			double local_best_validation_error = Double.MAX_VALUE;
			double previous_local_best_validation_error = Double.MAX_VALUE;
			int local_best_epoch = 0;
			int previous_local_best_epoch = 0;
			int local_neurons = 0;
			int previous_local_neurons = 0;
			while (local_best_validation_error>ERROR_THRESHOLD && local_neurons<MAX_NEURONS) {
				System.out.println("-----------------------------------------");
				
				previous_local_best_network = local_best_network;
				previous_local_v = local_v;
				previous_local_best_validation_error = local_best_validation_error;
				previous_local_best_epoch = local_best_epoch;
				previous_local_neurons = local_neurons;
				
				local_neurons+=2;
				
				BasicNetwork previous_network = new BasicNetwork();
				BasicNetwork current_network = new BasicNetwork();
				double[] previous_v = new double[MAX_EPOCH];
				double[] current_v = new double[MAX_EPOCH];
				double[] box_validation_error = { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE };
				double[] previous_box_validation_error;
				int previous_epoch = 0;
				int current_epoch = 0;
				for(int e=STEP_EPOCH-1; e<=MAX_EPOCH; e+=STEP_EPOCH) {
					BasicNetwork back_best_network = new BasicNetwork();
					double back_best_validation_error = Double.MAX_VALUE;
					for (double i=0.0; i<MAX_LEARNING_RATE; i+=STEP_PROPAGATION) {
						for (double j=0.0; j<MAX_MOMENTUM; j+=STEP_PROPAGATION) {
					
							BasicNetwork network = createNetwork(local_neurons);
				
							final Backpropagation train = new Backpropagation(network, trainingCVSet, i, j);
							train.fixFlatSpot(false);
						
							int epoch = 0;
							do {
								train.iteration();
								epoch++;
							} while (epoch < e);
							
							double error = getValidationError(trainingSet, k, network);
							if (error<back_best_validation_error) {
								back_best_validation_error = error;
								back_best_network = network;
							}
							
						}
					}
					
					System.out.println("Trail: " + (k+1) + 
									   " | Neurons: " + local_neurons + 
									   " | Epoch: " + (e+1) + 
									   " | Best Validation error: " + back_best_validation_error);
					
					previous_v = current_v;
					previous_epoch = current_epoch;
					current_v[e/STEP_EPOCH] = back_best_validation_error/AND_INPUT.length;
					current_epoch = (e+1);
					previous_network = current_network;
					current_network = back_best_network;
					previous_box_validation_error = box_validation_error.clone();
					box_validation_error[0] = box_validation_error[1];
					box_validation_error[1] = box_validation_error[2];
					box_validation_error[2] = back_best_validation_error;
					
					if (back_best_validation_error < ERROR_THRESHOLD || (e+STEP_EPOCH+1 > MAX_EPOCH)) {
						local_best_validation_error = box_validation_error[2];
						local_v = current_v;
						local_best_epoch = current_epoch;
						local_best_network = current_network;
						break;
					} else if (getAvg(box_validation_error)<getAvg(previous_box_validation_error)) {
						local_best_validation_error = box_validation_error[1];
						local_v = previous_v;
						local_best_epoch = previous_epoch;
						local_best_network = previous_network;
					} else {
						if (e >= local_best_epoch + (3*STEP_EPOCH)) {
							break;
						}
					}
					
				}
				
				System.out.println("-----------------------------------------");
				System.out.println("Trail: " + (k+1) + 
								   " | Neurons: " + local_neurons + 
								   " | Best Epoch: " + local_best_epoch + 
								   " | Best Validation error: " + local_best_validation_error);
				
				if (previous_local_best_validation_error<local_best_validation_error) {
					local_best_network = previous_local_best_network;
					local_v = previous_local_v;
					local_best_validation_error = previous_local_best_validation_error;
					local_best_epoch = previous_local_best_epoch;
					local_neurons = previous_local_neurons;
					break;
				}
			}
			
			System.out.println("-----------------------------------------");
			System.out.println("Trail: " + (k+1) + 
							   " | Best Neurons: " + local_neurons + 
							   " | Best Epoch: " + local_best_epoch + 
							   " | Best Validation error: " + local_best_validation_error);
				
			if (local_best_validation_error<best_validation_error) {
				best_network = local_best_network;
				v = local_v;
				best_validation_error = local_best_validation_error;
				best_epoch = local_best_epoch;
				best_neurons = local_neurons;
				best_trail = k+1;
			}	

		}

		// test the neural network
		System.out.println("-----------------------------------------");
		System.out.println("Best Trail: " + best_trail +
							" | Best Network Neurons: " + best_network.getLayerNeuronCount(1) + 
							" | Best Epoch: " + best_epoch + 
							" | Best Validation Error: " + best_validation_error);
		
		Encog.getInstance().shutdown();
		
		System.out.println("-----------------------------------------");
		
		for(int e=STEP_EPOCH-1; e<=MAX_EPOCH; e+=STEP_EPOCH) {
			double local_best_error = Double.MAX_VALUE;
			for (double i=0.1; i<MAX_LEARNING_RATE; i+=STEP_PROPAGATION) {
				for (double j=0.1; j<MAX_MOMENTUM; j+=STEP_PROPAGATION) {
					
					BasicNetwork network = createNetwork(best_neurons);
			
					final Backpropagation train = new Backpropagation(network, trainingSet, i, j);
					train.fixFlatSpot(false);
					
					int epoch = 0;
					do {
						train.iteration();
						epoch++;
					} while (epoch < e);
					
					if (train.getError()<local_best_error) {
						local_best_error = train.getError();
					}
				}
			}
			er[e/STEP_EPOCH] = local_best_error;
			
			System.out.println("Epoch: " + e + " | Training error: " + local_best_error);
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
	
	
	
	private static BasicNetwork createNetwork(int n) {
		// create a neural network, without using a factory
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 2));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, n));
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
