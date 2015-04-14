import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.RPROPType;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;


public class NeuralNetResilient {
	
	// Variables for finding best net
	private static double best_error = 0.0;

	public static void RunNet(String fileName, final int maxEpoch) {
		// Create training data
		NeuralNetUtil.trainingSet = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
				fileName, true, NeuralNetUtil.NUM_INPUT_BITS, NeuralNetUtil.NUM_OUTPUT_BITS);
		
		// Retrieve net - RPROP+
		BasicNetwork networkRp = calibrate(NeuralNetUtil.trainingSet, RPROPType.RPROPp, maxEpoch);
		NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileRp, networkRp);
		System.out.println("---------------------------");
		// Retrieve net - RPROP-
		//BasicNetwork networkRm = calibrate(RPROPType.RPROPm, maxEpoch);
		//NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileRm, networkRm);
		//System.out.println("---------------------------");
		// Retrieve net - iRPROP+
		//BasicNetwork networkiRp = calibrate(RPROPType.iRPROPp, maxEpoch);
		//NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileiRp, networkiRp);
		//System.out.println("---------------------------");
		// Retrieve net - iRPROP-
		//BasicNetwork networkiRm = calibrate(RPROPType.iRPROPm, maxEpoch);
		//NeuralNetUtil.writeFile(NeuralNetUtil.neuralNetResilientfileiRm, networkiRm);

	}
	
	public static BasicNetwork createTrainedNet(MLDataSet trainingSet, RPROPType r, final int neurons, final int maxEpoch) {
		//long start = System.currentTimeMillis();
		BasicNetwork network = NeuralNetUtil.createNet(neurons);
		best_error = ResilientProp(trainingSet, network, r, maxEpoch);
		//printDetail(network, r, maxEpoch);
		
		// Return the best ever network
		return network;
	}
	
	/** Find the best network */
	private static BasicNetwork calibrate(MLDataSet trainingSet, RPROPType r, final int maxEpoch) {
		BasicNetwork bestNet = null;
		//long start = System.currentTimeMillis();
		// Iterate through a number of neurons, increasing each time
		for (int i = 1; i < NeuralNetUtil.MAX_NEURONS; i++) {
			BasicNetwork network = NeuralNetUtil.createNet(i);
			double current = ResilientProp(trainingSet, network, r, maxEpoch);
			if (!Double.isNaN(best_error) && !Double.isNaN(current)) {
				// If it's the first run, or if the net has the best (lowest) error yet
				if (i == 1 || current < best_error) {
					// Record info
					best_error = current;
					bestNet = network;
				}
			}
		}
		printDetail(bestNet, r, maxEpoch);
		
		// Return the best ever network
		return bestNet;
	}
	private static double ResilientProp(MLDataSet trainingSet, BasicNetwork network, RPROPType r, int maxEpoch) {
		double error = 0.0;
		// Train the net, with given learning rate and momentum
		// Get its error
		double current_error = train(trainingSet, network, r, maxEpoch);
		//System.out.println("Weight: " + network.getWeight(0, 0, 0));
		// Check everything went as expected
		if (!Double.isNaN(error) && !Double.isNaN(current_error)) {
			// If it's the first run, or if the net has the best (lowest) error yet
			if (current_error < error) {
				// Record info
				error = current_error;
			}
		}
		return error;
	}
	private static void printDetail(BasicNetwork bestNet, RPROPType r, int maxEpoch) {
		System.out.println("Resilient-Propagation Done!");
		System.out.println("RPROP Type: " + r.name());
		System.out.println("Best neurons: "+ bestNet.getLayerNeuronCount(1)+
				"\nLowest error: " + best_error);
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
	 * @return					Error
	 */
	private static double train(MLDataSet trainingSet, BasicNetwork network, RPROPType r, final int maxEpoch) {
		// Using BackProp
		final ResilientPropagation train = new ResilientPropagation(network,
				NeuralNetUtil.trainingSet);
		int epoch = 1;
		train.setRPROPType(r);
		// Train
		do {
			train.iteration();
			epoch++;
		} while (epoch < maxEpoch);
		return train.getError();
	}
	
}
