import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class NNANDManual {
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
		
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(AND_INPUT, AND_IDEAL);

		// train the neural network
		int max_epoch = 1;
			
				final Backpropagation train = new Backpropagation(network, trainingSet, 0.5, 0.5);
				train.fixFlatSpot(false);
				
				int epoch = 1;
				do {
					System.out.println(network.dumpWeights());
					train.iteration();
					
					epoch++;
				} while (epoch <= max_epoch);
				
				System.out.println(network.dumpWeights());
				System.out.println("Learning Rate: " + 0.5 + " Momentum: " + 0.5);
				System.out.println("Error: " + train.getError());
				System.out.println("Error: " + network.calculateError(trainingSet));
				
		// test the neural network
		System.out.println("Neural Network Results:");
		for (MLDataPair pair : trainingSet) {
			final MLData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + ","
					+ pair.getInput().getData(1) + ", actual="
					+ output.getData(0) + ",ideal="
					+ pair.getIdeal().getData(0));
		}

		Encog.getInstance().shutdown();
	}
}
