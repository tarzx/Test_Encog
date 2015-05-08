import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.mathutil.randomize.ConsistentRandomizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;


public class GeneralisationCSV {
	
	private final static double MAX_LEARNING_RATE = 1.0;
	private final static double MAX_MOMENTUM = 1.0;
	private final static double STEP_PROPAGATION = 0.1;
	private final static double ERROR_THRESHOLD = 1E-5;
	private final static int MAX_EPOCH = 2000;
	private final static int STEP_EPOCH = 100;
	private final static int MAX_NEURONS = 10;
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            No arguments are used.
	 */
	public static void main(final String args[]) {
		String fileName = NeuralNetUtil.interventionNetCSV;
		
		double[] ep = new double[MAX_EPOCH/STEP_EPOCH];
		double[] er = new double[MAX_EPOCH/STEP_EPOCH];
		double[] v = new double[MAX_EPOCH/STEP_EPOCH];
		
		//set epoch data
		for(int e=STEP_EPOCH-1; e<=MAX_EPOCH; e+=STEP_EPOCH) {
			ep[e/STEP_EPOCH] = e+1;
		}
		
		
		// create training data
		NeuralNetUtil.trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
				fileName, true, NeuralNetUtil.NUM_INPUT_BITS, NeuralNetUtil.NUM_OUTPUT_BITS);
		
		CrossValidation.setTraining(fileName, NeuralNetUtil.netCSVset, NeuralNetUtil.netCSVValidationset);
		
		BasicNetwork best_network = new BasicNetwork();
		double best_validation_error = Double.MAX_VALUE;
		int best_epoch = 0;
		int best_trail = 0;
		int best_neurons = 0;
		for (int k=0; k<NeuralNetUtil.netCSVset.length; k++) {			
			BasicNetwork local_best_network = new BasicNetwork();
			BasicNetwork current_local_best_network = new BasicNetwork();
			double[] local_v = new double[MAX_EPOCH];
			double[] current_local_v = new double[MAX_EPOCH];
			double local_best_validation_error = Double.MAX_VALUE;
			double current_local_best_validation_error = Double.MAX_VALUE;
			int local_best_epoch = 0;
			int current_local_best_epoch = 0;
			int local_neurons = 0;
			int current_local_neurons = 0;
			while (current_local_neurons<MAX_NEURONS) {
				System.out.println("-----------------------------------------");

				current_local_neurons++;
				
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
					
							BasicNetwork network = createNetwork(current_local_neurons);
				
							final Backpropagation train = new Backpropagation(network, NeuralNetUtil.trainingSets[k], i, j);
							train.fixFlatSpot(false);
						
							int epoch = 0;
							do {
								train.iteration();
								epoch++;
							} while (epoch < e);
							
							double error = getValidationError(NeuralNetUtil.validationSets[k], network);
							if (error<back_best_validation_error) {
								back_best_validation_error = error;
								back_best_network = network;
							}
							
						}
					}
					
					System.out.println("Trail: " + (k+1) + 
									   " | Neurons: " + current_local_neurons + 
									   " | Epoch: " + (e+1) + 
									   " | Best Validation error: " + back_best_validation_error);
					
					previous_v = current_v;
					previous_epoch = current_epoch;
					current_v[e/STEP_EPOCH] = back_best_validation_error;
					current_epoch = (e+1);
					previous_network = current_network;
					current_network = back_best_network;
					previous_box_validation_error = box_validation_error.clone();
					box_validation_error[0] = box_validation_error[1];
					box_validation_error[1] = box_validation_error[2];
					box_validation_error[2] = back_best_validation_error;
					
					//System.out.println(getAvg(box_validation_error) + " " + getAvg(previous_box_validation_error));
					if (back_best_validation_error < ERROR_THRESHOLD || (e+STEP_EPOCH+1 > MAX_EPOCH)) {
						current_local_best_validation_error = box_validation_error[2];
						current_local_v = current_v;
						current_local_best_epoch = current_epoch;
						current_local_best_network = current_network;
						break;
					} else if (getAvg(box_validation_error)<getAvg(previous_box_validation_error)) {
						current_local_best_validation_error = box_validation_error[1];
						current_local_v = previous_v;
						current_local_best_epoch = previous_epoch;
						current_local_best_network = previous_network;
					} else {
						if ((e+1) > current_local_best_epoch + (3*STEP_EPOCH)) {
							break;
							//System.out.println("Stop..." + (current_local_best_epoch));
						}
					}
					
				}
				
				System.out.println("-----------------------------------------");
				System.out.println("Trail: " + (k+1) + 
								   " | Neurons: " + current_local_neurons + 
								   " | Best Epoch: " + current_local_best_epoch + 
								   " | Best Validation error: " + current_local_best_validation_error);
				
				if (current_local_best_validation_error<local_best_validation_error) {
					local_best_network = current_local_best_network;
					local_v = current_local_v;
					local_best_validation_error = current_local_best_validation_error;
					local_best_epoch = current_local_best_epoch;
					local_neurons = current_local_neurons;
					
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
			
					final Backpropagation train = new Backpropagation(network, NeuralNetUtil.trainingSet, i, j);
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
			
			System.out.println("Epoch: " + (e+1) + " | Training error: " + local_best_error);
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
	
	private static double getValidationError(MLDataSet validationSet, BasicNetwork network) {
		double avg_error = 0.0;
		double sum_square_error = 0.0;
		for (MLDataPair pair : validationSet) {
			final MLData output = network.compute(pair.getInput());
//			System.out.println(pair.getInput().getData(0) + ","
//				+ pair.getInput().getData(1) + ","
//				+ pair.getInput().getData(1) + ", actual="
//				+ output.getData(0) + ", ideal="
//				+ pair.getIdeal().getData(0));
			sum_square_error += Math.pow((output.getData(0))-(pair.getIdeal().getData(0)), 2);
		}
		avg_error = sum_square_error/validationSet.getInputSize();
		
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
		network.addLayer(new BasicLayer(null, true, NeuralNetUtil.NUM_INPUT_BITS));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, n));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, NeuralNetUtil.NUM_OUTPUT_BITS));
		network.getStructure().finalizeStructure();
		network.reset();
		
		//double[] weights = new double[network.getFlat().getWeights().length];
		//network.getFlat().setWeights(weights);
		
		new ConsistentRandomizer(-1, 1, 500).randomize(network);
		//System.out.println(network.dumpWeights());
		
		return network;
	}
}

