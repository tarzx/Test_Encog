import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;


public class NeuralNetBack {
	
	// Variables for finding best net
	private static double best_error = 0.0;
	private static double best_learning = 0;
	private static double best_momentum = 0;
	
	public double getError() { return best_error; }
	public double getLearningRate() { return best_learning; }
	public double getMomentum() { return best_momentum; }
	
	public static void RunNet(String fileName, final int maxEpoch) {
		// Create training data
		NeuralNetUtil.trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
				fileName, true, NeuralNetUtil.NUM_INPUT_BITS, NeuralNetUtil.NUM_OUTPUT_BITS);
		// Retrieve net
		BasicNetwork network = calibrate(NeuralNetUtil.trainingSet, maxEpoch);
		NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetBackfile, network);
	}
	
	public BasicNetwork createTrainedNet(MLDataSet trainingSet, final int neurons, final int maxEpoch) {
		//long start = System.currentTimeMillis();
		BasicNetwork network = NeuralNetUtil.createNet(neurons);
		double[] current = backProp(trainingSet, network, maxEpoch);
		
		// Record info
		best_error = current[0];
		best_learning = current[1];
		best_momentum = current[2];

		//printDetail(network, maxEpoch);
		
		// Return the best ever network
		return network;
	}
	
	public BasicNetwork createTrainedNet(MLDataSet trainingSet, final int neurons, final double learning, final double momentum, final int maxEpoch) {
		//long start = System.currentTimeMillis();
		BasicNetwork network = NeuralNetUtil.createNet(neurons);
		double current_error = train(trainingSet, network, learning, momentum, maxEpoch);
		// Return the best ever network
		System.out.println(current_error);
		return network;
	}
	
	/** Find the best network */
	private static BasicNetwork calibrate(MLDataSet trainingSet, final int maxEpoch) {
		BasicNetwork bestNet = null;
		//long start = System.currentTimeMillis();
		// Iterate through a number of neurons, increasing each time
		for (int i = 1; i < NeuralNetUtil.MAX_NEURONS; i++) {
			BasicNetwork network = NeuralNetUtil.createNet(i);
			double[] current = backProp(trainingSet, network, maxEpoch);
			if (!Double.isNaN(best_error) && !Double.isNaN(current[0])) {
				// If it's the first run, or if the net has the best (lowest) error yet
				if (i == 1 || current[0] < best_error) {
					// Record info
					best_error = current[0];
					bestNet = network;
					best_learning = current[1];
					best_momentum = current[2];
				}
			}
		}
		printDetail(bestNet, maxEpoch);
		
		// Return the best ever network
		return bestNet;
	}
	private static double[] backProp(MLDataSet trainingSet, BasicNetwork network, int maxEpoch) {
		double[] netDetail = {0.0, 0.0, 0.0, 0.0};
		// Iterate through learning rates
		for (double j = 0.0; j < NeuralNetUtil.MAX_LEARNING; j = j + NeuralNetUtil.STEP) {
			// Iterate through momentums
			for (double k = 0.0; k < NeuralNetUtil.MAX_MOMENTUM; k = k + NeuralNetUtil.STEP) {
				// Create a net with i neurons
				// Train the net, with given learning rate and momentum
				// Get its error
				double current_error = train(trainingSet, network, j, k, maxEpoch);
				//System.out.println("Weight: " + network.getWeight(0, 0, 0));
				// Check everything went as expected
				if (!Double.isNaN(netDetail[0]) && !Double.isNaN(current_error)) {
					// If it's the first run, or if the net has the best (lowest) error yet
					if ((j == 0 && k == 0) || current_error < netDetail[0]) {
						// Record info
						netDetail[0] = current_error;
						netDetail[1] = j;
						netDetail[2] = k;
					}
				}
			}
		}
		return netDetail;
	}
	private static void printDetail(BasicNetwork bestNet, int maxEpoch) {
		System.out.println("Back-Propagation Done!");
		System.out.println("Best neurons: "+ bestNet.getLayerNeuronCount(1) +
				"\nEpoch: " + maxEpoch +
				"\nLowest error: " + best_error);
		System.out.println("Best learning rate: "+best_learning+
				"\nBest momentum: "+best_momentum);
		double weight_decay = 0;
		for (int j=0; j<bestNet.getFlat().getWeights().length; j++) {
			//System.out.println(j + ": " + bestNet.getFlat().getWeights()[j]);
			weight_decay += Math.pow(bestNet.getFlat().getWeights()[j],2);
		}
		System.out.println("Best Weight Decay: " + weight_decay);
	}
	
	/** Train a network with given parameters
	 * 
	 * @param network			The net
	 * @param learning_rate		Learning rate
	 * @param momentum			Momentum
	 * @return					Error
	 */
	private static double train(MLDataSet trainingSet, BasicNetwork network, final double learning_rate, final double momentum, final int maxEpoch) {
		// Using BackProp
		final Backpropagation train = new Backpropagation(network,
				trainingSet, learning_rate, momentum);
		int epoch = 0;
		// Train
		do {
			train.iteration();
			epoch++;
		} while (epoch < maxEpoch);
		return train.getError();
	}
	
}
