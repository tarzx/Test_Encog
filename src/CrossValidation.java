import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class CrossValidation {
	
	
	public static void setTraining(String fileName, String[] fileSet) {
		CrossValidation.refineCSVset(fileName);
		for (int i=0; i<NeuralNetUtil.nSet; i++) {
			NeuralNetUtil.trainingSets[i] = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
					fileSet[i], true, NeuralNetUtil.NUM_INPUT_BITS, NeuralNetUtil.NUM_OUTPUT_BITS);
		}
	}
	
	public static void refineCSVset(String fileName){
		try {
			for (int i=0; i<NeuralNetUtil.nSet; i++) {
				CSVReader reader = new CSVReader(new FileReader(fileName));
				CSVWriter writer = null;
				try {
					writer = new CSVWriter(new FileWriter(NeuralNetUtil.netCSVset[i], false));
				
					String [] nextLine;
					int loopCounter = 0;
					while ((nextLine = reader.readNext()) != null) {
						if (loopCounter == 0 || loopCounter%NeuralNetUtil.nSet!=i) {
							//System.out.println(nextLine[0]+nextLine[1]+nextLine[2]+nextLine[3]);
							writer.writeNext(nextLine);
						}
						loopCounter++;
					}
					
					writer.close();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
