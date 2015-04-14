
public class Transform {
	private static String[][] mapSubject = { {"i", "i'm", "i'd", "my", "mine", "myself"}, 
											 {"you", "you're", "you'd", "your", "yours", "yourself"} };
	
	public static String partialTransform(final String input) {
		if (input.indexOf(". ") > 0) {
			String sentence = input.substring(0, input.indexOf(". "));
			if (sentence.split(" ").length < 4) {
				return fullTransform(trimEnd(sentence)); 
			}
		} else {
			if (input.split(" ").length < 4) {
				return fullTransform(trimEnd(input)); 
			}
		}
		return "";
	}
	
	public static String fullTransform(final String input) {
		StringBuilder output = new StringBuilder();
		String[] component = input.toLowerCase().split(" ");
		for (int i=0; i<component.length; i++) {
			component[i] = grammarPersonal(negativeForm(component[i]));
		}
		for (int i=0; i<component.length-1; i++) {
			component[i+1] = componentSubject(component[i], component[i+1]);
		}
		for (int i=0; i<component.length; i++) {
			output.append(component[i] + " ");
		}
		
		return trimEnd(upper(concat(output.toString().trim())));
	}
	
	private static String concat(final String input) {
		if (input.contains(". ")) {
			return input.replace(". ", " and ");
		} else {
			return input;
		}
	}
	
	private static String trimEnd(final String input) {
		if (input.trim().endsWith(".")) {
			return input.substring(0, input.lastIndexOf("."));
		} else {
			return input;
		}
	}
	
	private static String upper(final String input) {
		String output = " " + input;
		output = output.replace(" i ", " I ");
		output = output.replace(" i'", " I'");
		return output.substring(1);
	}
	
	private static String negativeForm(final String input) {
		if (input.equals("can't")) {
			return "cannot";
		} else if (input.endsWith("n't")) {
			return input.replace("n't", " not");
		} else {
			return input;
		}
	}
	
	private static String grammarPersonal(final String input) {
		String output = input;
		for (int i=0; i<mapSubject[0].length; i++) {
			if (input.equals(mapSubject[0][i])) {
				output = mapSubject[1][i];
				break;
			} else if (input.equals(mapSubject[1][i])) {
				output = mapSubject[0][i];
				break;
			} 
		}
		return output;
	}
	
	private static String componentSubject(final String input1, final String input2) {
		String output = input2;
		if (input1.equals("i")) {
			if (input2.equals("were")) {
				output = "was";
			} else if (input2.equals("are")) {
				output = "am";
			}
		} else if (input1.equals("you")) {
			if (input2.equals("was")) {
				output = "were";
			} else if (input2.equals("am")) {
				output = "are";
			}
		}
		return output;
	}
	
	public static String replaceFull(String input, String prevAns) {
		return input.replace("%%%", Transform.fullTransform(prevAns));
	}
	
	public static String replacePartial(String input, String prevAns, String default_form) {
		String partial =  Transform.partialTransform(prevAns);
		if (!partial.equals("")) {
			return input.replace("###", partial);
		} else {
			return input.replace("###", default_form);
		}
	}
	
	public static String replaceQuestion(String input, String prevAns, String pattern, String default_pattern) {
		return input.replace("$$$", replacePartial(pattern, prevAns, default_pattern));
	}
	
	public static String replaceTopic(String input, String topic) {
		return input.replace("@@@", topic);
	}
}
