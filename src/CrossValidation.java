import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class CrossValidation {
	
	
	public static void setTraining(String fileName, String[] fileSet, String[] fileValidationSet) {
		CrossValidation.refineCSVset(fileName, fileSet, fileValidationSet);
		for (int i=0; i<NeuralNetUtil.nSet; i++) {
			NeuralNetUtil.trainingSets[i] = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
					fileSet[i], true, NeuralNetUtil.NUM_INPUT_BITS, NeuralNetUtil.NUM_OUTPUT_BITS);
			NeuralNetUtil.validationSets[i] = TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, 
					fileValidationSet[i], true, NeuralNetUtil.NUM_INPUT_BITS, NeuralNetUtil.NUM_OUTPUT_BITS);
		}
	}
	
	public static void refineCSVset(String fileName, String[] fileSet, String[] fileValidationSet){
		try {
			for (int i=0; i<NeuralNetUtil.nSet; i++) {
				CSVReader reader = new CSVReader(new FileReader(fileName));
				CSVWriter writer, writerVal = null;
				try {
					writer = new CSVWriter(new FileWriter(fileSet[i], false));
					writerVal = new CSVWriter(new FileWriter(fileValidationSet[i], false));
				
					String [] nextLine;
					int loopCounter = 0;
					while ((nextLine = reader.readNext()) != null) {
						//System.out.println(nextLine[0]+nextLine[1]+nextLine[2]+nextLine[3]);
						if (loopCounter == 0) {
							writer.writeNext(nextLine);
							writerVal.writeNext(nextLine);
						} else if (loopCounter%NeuralNetUtil.nSet!=i) {
							writer.writeNext(nextLine);
						} else {
							writerVal.writeNext(nextLine);
						}
						loopCounter++;
					}
					
					writer.close();
					writerVal.close();
					reader.close();
					
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
