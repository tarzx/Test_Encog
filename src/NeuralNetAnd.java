
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class NeuralNetAnd {
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

		// create a neural network, without using a factory
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 2));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		network.getStructure().finalizeStructure();
		network.reset();
		
		double[] weights = new double[network.getFlat().getWeights().length];
		network.getFlat().setWeights(weights);
		
		//new ConsistentRandomizer(-1, 1, 500).randomize(network);
		//System.out.println(network.dumpWeights());

		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(AND_INPUT, AND_IDEAL);

//		// train the neural network
//		final Backpropagation train = new Backpropagation(network, trainingSet,
//				0.2, 0.8);
//		train.fixFlatSpot(false);
//
//		int epoch = 1;
//		do {
//			train.iteration();
//			System.out
//					.println("Epoch #" + epoch + " Error:" + train.getError());
//			epoch++;
//		} while (train.getError() > 0.0001);
		
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
				BasicNetwork processed_network = (BasicNetwork) network.clone();
				//System.out.println(processed_network.dumpWeights());
				
				final Backpropagation train = new Backpropagation(processed_network, trainingSet, i, j);
				train.fixFlatSpot(false);
				
				int epoch = 0;
				do {
					train.iteration();
					epoch++;
				} while (train.getError() > error_threshold && epoch < max_epoch);
				
				System.out.println("Learning Rate: " + i + " Momentum: " + j);
				System.out.println("Error:" + train.getError());
					
				if (train.getError() < lowest_error) {
					bestNetwork = (BasicNetwork) processed_network.clone();
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

		// test the neural network
		System.out.println("Neural Network Results:");
		for (MLDataPair pair : trainingSet) {
			final MLData output = bestNetwork.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + ","
					+ pair.getInput().getData(1) + ", actual="
					+ output.getData(0) + ",ideal="
					+ pair.getIdeal().getData(0));
		}

		Encog.getInstance().shutdown();
	}
}
