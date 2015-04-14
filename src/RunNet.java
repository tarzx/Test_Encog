
public class RunNet {

	public static void main(String[] args) {
		//NeuralNetBack.RunNet(NeuralNetUtil.interventionNetCSV, NeuralNetUtil.MAX_EPOCH);
		//System.out.println("---------------------------");
		//NeuralNetResilient.RunNet(NeuralNetUtil.subQuestionNetCSV, NeuralNetUtil.MAX_EPOCH);
		//System.out.println("---------------------------");
		Generalisation.RunNet(NeuralNetUtil.interventionNetCSV);
		
	}
	
}
