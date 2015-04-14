import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Conversation {
	public static void main(String[] args) {
		int age = 15;
		boolean isMale = false;
		
		QuestionGroup q = new QuestionGroup();
		SelectSequenceNet sqn = new SelectSequenceNet();
		SelectReplayGoalNet srgn = new SelectReplayGoalNet();
		SelectReflectBehaviourNet srbn = new SelectReflectBehaviourNet();
		
		Scanner sc = new Scanner(System.in);
        while (true) {
        	System.out.println("Enter Mode (C/T) or 0 to exit:");
        	char mode = sc.next().charAt(0);
        	if (mode=='0') {
        		System.exit(0);
        	} else {
        		double ctlLv = getControlLevel(sc);
        		int seq = sqn.getSequence(ctlLv, age, isMale);
        		if (mode=='T') {
        			getTranscript(q, seq, srgn, srbn, ctlLv, age, isMale);
        		} else {
        			startChatting(sc, q, seq, srgn, srbn, ctlLv, age, isMale);
        		}
        	}
    	}
	}
	
	private static double getControlLevel(Scanner sc) {
		System.out.println("To what extent do you feel you are in control of your life recently?");
		System.out.println("(1) Not all all");
		System.out.println("(2) To little extent");
		System.out.println("(3) To some extent");
		System.out.println("(4) To a moderate extent");
		System.out.println("(5) To a large extent");
		char lv = sc.next().charAt(0);
		if (lv=='1') {
			return 0.1;
		} else if (lv=='2') {
			return 0.3;
		} else if (lv=='3') {
			return 0.5;
		} else if (lv=='4') {
			return 0.7;
		} else if (lv=='5') {
			return 0.9;
		} else {
			return 0.1;
		}
	}
	
	private static void startChatting(Scanner sc, QuestionGroup q, int seq,
			SelectReplayGoalNet srgn, SelectReflectBehaviourNet srbn, double ctlLv, int age, boolean isMale) {
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader keyboard = new BufferedReader(in);
		try {
			switch (seq) {
				case 1:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 1:");
					Question q11 = q.getQuestionG1(0);
					System.out.println("C: " + q11);	
					System.out.print("P: " + q11.getAnswerPrefix());
					String ans11 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG2(1, ans11, "", false));
					System.out.print("P: ");
					String ans12 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG5(2, ans12, true));
					System.out.print("P: ");
					String ans13 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG8(5, ans11, "", true));
					System.out.print("P: ");
					String ans14 = keyboard.readLine();
					System.out.println("---------------------------------------------------");
					break;
				case 2:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 2:");
					Question q21 = q.getQuestionG1(0);
					System.out.println("C: " + q21);	
					System.out.print("P: " + q21.getAnswerPrefix());
					String ans21 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG2(1, ans21, "", false));
					System.out.print("P: ");
					String ans22 = keyboard.readLine();
					Question q21011 = q.getGroup10OR11(srbn, ctlLv, age, isMale, 2, ans22, true);
					System.out.println("C: " + q21011);
					System.out.print("P: ");
					String ans23 = keyboard.readLine();;
					System.out.println("C: " + q.getGroup3OR4(srgn, ctlLv, age, isMale, q21011.getGroup(), ans21, "", true));
					System.out.print("P: ");
					String ans24 = keyboard.readLine();;
					System.out.println("---------------------------------------------------");
					break;
				case 3:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 3:");
					Question q39 = q.getQuestionG9(0);
					System.out.println("C: " + q39);	
					System.out.print("P: " + q39.getAnswerPrefix());
					String ans31 = keyboard.readLine();
					System.out.println("C: " + q39.getNextQuestion(ans31));
					System.out.print("P: ");
					String ans32 = keyboard.readLine();;
					System.out.println("C: " + q.getQuestionG2(9, ans31, ans32, true));
					System.out.print("P: ");
					String ans33 = keyboard.readLine();;
					System.out.println("C: " + q.getQuestionG8(7, ans31, "", true));
					System.out.print("P: ");
					String ans34 = keyboard.readLine();;
					System.out.println("---------------------------------------------------");
					break;
				case 4:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 4:");
					Question q46 = q.getQuestionG6(0);
					System.out.println("C: " + q46);	
					System.out.print("P: " + q46.getAnswerPrefix());
					String ans41 = keyboard.readLine();
					System.out.println("C: " + q46.getNextQuestion(ans41));
					System.out.print("P: ");
					String ans42 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG5(6, ans42, true));
					System.out.print("P: ");
					String ans43 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG8(5, ans41, "", true));
					System.out.print("P: ");
					String ans44 = keyboard.readLine();
					System.out.println("---------------------------------------------------");
					break;
				case 5:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 5:");
					Question q56 = q.getQuestionG6(0);
					System.out.println("C: " + q56);	
					System.out.print("P: " + q56.getAnswerPrefix());
					String ans51 = keyboard.readLine();
					System.out.println("C: " + q56.getNextQuestion(ans51));
					System.out.print("P: ");
					String ans52 = keyboard.readLine();
					Question q51011 = q.getGroup10OR11(srbn, ctlLv, age, isMale, 6, ans52, true);
					System.out.println("C: " + q51011);
					System.out.print("P: ");
					String ans53 = keyboard.readLine();
					System.out.println("C: " + q.getGroup3OR4(srgn, ctlLv, age, isMale, q51011.getGroup(), ans51, "", true));
					System.out.print("P: ");
					String ans54 = keyboard.readLine();
					System.out.println("---------------------------------------------------");
					break;
				case 6:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 6:");
					System.out.println("C: " + q.getQuestionG5(0, "", false));
					System.out.print("P: ");
					String ans61 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG8(5, "", ans61, false));
					System.out.print("P: ");
					String ans62 = keyboard.readLine();
					Question q67 = q.getQuestionG7(8, "");
					System.out.println("C: " + q67);	
					System.out.print("P: " + q67.getAnswerPrefix());
					String ans63 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG2(7, ans63, "", false));
					System.out.print("P: ");
					String ans64 = keyboard.readLine();
					System.out.println("---------------------------------------------------");
					break;
				case 7:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 7:");
					System.out.println("C: " + q.getQuestionG10(0, "", false));
					System.out.print("P: ");
					String ans71 = keyboard.readLine();
					Question q734 = q.getGroup3OR4(srgn, ctlLv, age, isMale, 10, "", ans71, false);
					System.out.println("C: " + q734);
					System.out.print("P: ");
					String ans72 = keyboard.readLine();
					Question q77 = q.getQuestionG7(q734.getGroup(), "");
					System.out.println("C: " + q77);	
					System.out.print("P: " + q77.getAnswerPrefix());
					String ans73 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG2(7, ans73, "", false));
					System.out.print("P: ");
					String ans74 = keyboard.readLine();
					System.out.println("---------------------------------------------------");
					break;
				case 8:
					System.out.println("---------------------------------------------------");
					System.out.println("Sequence 8:");
					System.out.println("C: " + q.getQuestionG11(0, "", false));
					System.out.print("P: ");
					String ans81 = keyboard.readLine();
					Question q834 = q.getGroup3OR4(srgn, ctlLv, age, isMale, 11, "", ans81, false);
					System.out.println("C: " + q834);
					System.out.print("P: ");
					String ans82 = keyboard.readLine();
					Question q87 = q.getQuestionG7(q834.getGroup(), "");
					System.out.println("C: " + q87);	
					System.out.print("P: " + q87.getAnswerPrefix());
					String ans83 = keyboard.readLine();
					System.out.println("C: " + q.getQuestionG2(7, ans83, "", false));
					System.out.print("P: ");
					String ans84 = keyboard.readLine();
					System.out.println("---------------------------------------------------");
					break;
				default:
					System.out.println("Sequence number is not match. try again!");
					break;
			}
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		}
	}
	
	private static void getTranscript(QuestionGroup q, int seq,
			SelectReplayGoalNet srgn, SelectReflectBehaviourNet srbn, double ctlLv, int age, boolean isMale) {
		switch (seq) {
			case 1:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 1:");
				Question q11 = q.getQuestionG1(0);
				System.out.println("C: " + q11);	
				String ans11 = "my work.";
				System.out.println("P: " + q11.getAnswerPrefix() + ans11);
				System.out.println("C: " + q.getQuestionG2(1, ans11, "", false));
				String ans12 = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + ans12);
				System.out.println("C: " + q.getQuestionG5(2, ans12, true));
				String ans13 = "I am waiting for the feedback and I haven't talked to my supervisor about it yet.";
				System.out.println("P: " + ans13);
				System.out.println("C: " + q.getQuestionG8(5, ans11, "", true));
				String ans14 = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + ans14);
				System.out.println("---------------------------------------------------");
				break;
			case 2:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 2:");
				Question q21 = q.getQuestionG1(0);
				System.out.println("C: " + q21);	
				String ans21 = "my work.";
				System.out.println("P: " + q21.getAnswerPrefix() + ans21);
				System.out.println("C: " + q.getQuestionG2(1, ans21, "", false));
				String ans22 = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + ans22);
				Question q21011 = q.getGroup10OR11(srbn, ctlLv, age, isMale, 2, ans22, true);
				System.out.println("C: " + q21011);
				String ans23 = "I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + ans23);
				System.out.println("C: " + q.getGroup3OR4(srgn, ctlLv, age, isMale, q21011.getGroup(), ans21, "", true));
				String ans24 = "I concern about my grade and whether I graduate.";
				System.out.println("P: " + ans24);
				System.out.println("---------------------------------------------------");
				break;
			case 3:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 3:");
				Question q39 = q.getQuestionG9(0);
				System.out.println("C: " + q39);	
				String ans31 = "my work.";
				System.out.println("P: " + q39.getAnswerPrefix() + ans31);
				System.out.println("C: " + q39.getNextQuestion(ans31));
				String ans32 = "I aware of the feedback of my last presentation.";
				System.out.println("P: " + ans32);
				System.out.println("C: " + q.getQuestionG2(9, ans31, ans32, true));
				String ans33 = "I feel unhappy about it. I was too nervous and not confident.";
				System.out.println("P: " + ans33);
				System.out.println("C: " + q.getQuestionG8(2, ans31, "", true));
				String ans34 = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + ans34);
				System.out.println("---------------------------------------------------");
				break;
			case 4:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 4:");
				Question q46 = q.getQuestionG6(0);
				System.out.println("C: " + q46);	
				String ans41 = "my work.";
				System.out.println("P: " + q46.getAnswerPrefix() + ans41);
				System.out.println("C: " + q46.getNextQuestion(ans41));
				String ans42 = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + ans42);
				System.out.println("C: " + q.getQuestionG5(6, ans42, true));
				String ans43 = "I am waiting for the feedback and I haven’t talked to my supervisor about it yet.";
				System.out.println("P: " + ans43);
				System.out.println("C: " + q.getQuestionG8(5, ans41, "", true));
				String ans44 = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + ans44);
				System.out.println("---------------------------------------------------");
				break;
			case 5:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 5:");
				Question q56 = q.getQuestionG6(0);
				System.out.println("C: " + q56);	
				String ans51 = "my work.";
				System.out.println("P: " + q56.getAnswerPrefix() + ans51);
				System.out.println("C: " + q56.getNextQuestion(ans51));
				String ans52 = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + ans52);
				Question q51011 = q.getGroup10OR11(srbn, ctlLv, age, isMale, 5, ans52, true);
				System.out.println("C: " + q51011);
				String ans53 = "I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + ans53);
				System.out.println("C: " + q.getGroup3OR4(srgn, ctlLv, age, isMale, q51011.getGroup(), ans51, "", true));
				String ans54 = "I concern about my grade and whether I graduate.";
				System.out.println("P: " + ans54);
				System.out.println("---------------------------------------------------");
				break;
			case 6:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 6:");
				System.out.println("C: " + q.getQuestionG5(0, "", false));
				String ans61 = "I am waiting for the feedback of my presentation and I haven’t talked to my supervisor about it yet.";
				System.out.println("P: " + ans61);
				System.out.println("C: " + q.getQuestionG8(5, "", ans61, false));
				String ans62 = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + ans62);
				Question q67 = q.getQuestionG7(8, "");
				System.out.println("C: " + q67);	
				String ans63 = "my work. I feel I was too nervous and not confident on my last presentation and I should try to way to improve myself.";
				System.out.println("P: " + q67.getAnswerPrefix() + ans63);
				System.out.println("C: " + q.getQuestionG2(7, ans63, "", false));
				String ans64 = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + ans64);
				System.out.println("---------------------------------------------------");
				break;
			case 7:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 7:");
				System.out.println("C: " + q.getQuestionG10(0, "", false));
				String ans71 = "I worry about my last presentation. I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + ans71);
				Question q734 = q.getGroup3OR4(srgn, ctlLv, age, isMale, 10, "", ans71, false);
				System.out.println("C: " + q734); //
				String ans72 = "I want to graduate with a good grade.";
				System.out.println("P: " + ans72);
				Question q77 = q.getQuestionG7(q734.getGroup(), "");
				System.out.println("C: " + q77);	
				String ans73 = "my work and I feel I was too nervous and not confident on my last presentation and I might not get a good grade on it.";
				System.out.println("P: " + q77.getAnswerPrefix() + ans73);
				System.out.println("C: " + q.getQuestionG2(7, ans73, "", false));
				String ans74 = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + ans74);
				System.out.println("---------------------------------------------------");
				break;
			case 8:
				System.out.println("---------------------------------------------------");
				System.out.println("Sequence 8:");
				System.out.println("C: " + q.getQuestionG11(0, "", false));
				String ans81 = "I worry about my last presentation. I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + ans81);
				Question q834 = q.getGroup3OR4(srgn, ctlLv, age, isMale, 11, "", ans81, false);
				System.out.println("C: " + q834); //
				String ans82 = "I want to graduate with a good grade.";
				System.out.println("P: " + ans82);
				Question q87 = q.getQuestionG7(q834.getGroup(), "");
				System.out.println("C: " + q87);	
				String ans83 = "my work. I feel I was too nervous and not confident on my last presentation and I might not get a good grade on it.";
				System.out.println("P: " + q87.getAnswerPrefix() + ans83);
				System.out.println("C: " + q.getQuestionG2(7, ans83, "", false));
				String ans84 = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + ans84);
				System.out.println("---------------------------------------------------");
				break;
			default:
				System.out.println("Sequence number is not match. try again!");
				break;
		}
		
	}
}
