import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Conversation {
	public static void main(String[] args) {
		int age = 15;
		boolean isMale = false;
		boolean isRecommened = true;
		
		GroupQuestion gq = new GroupQuestion();
		SelectRecommedation sr = new SelectRecommedation();
		
		Scanner sc = new Scanner(System.in);
        while (true) {
        	System.out.println("Enter Mode (C/T) or 0 to exit:");
        	char mode = sc.next().charAt(0);
        	if (mode=='0') {
        		System.exit(0);
        	} else {
        		double preCtlLv = getControlLevel(sc, gq, true);
        		int seq = sr.selectSequence(preCtlLv, age, isMale);
        		SequenceQuestion seqQ;
        		if (mode=='T') {
        			seqQ = getTranscript(gq, seq, sr, preCtlLv, age, isMale);
        		} else {
        			seqQ = startChatting(sc, gq, seq, sr, preCtlLv, age, isMale);
        		}
        		double postCtlLv = getControlLevel(sc, gq, true);
        		//Record Data
        		recordControlLevel(preCtlLv, postCtlLv);
        		SeqRespond(sc, gq, seqQ);
        		if (isRecommened) { 
        			RecommendedRespond(sc, gq); 
        		}
        		trainNet();
        	}
    	}
	}
	
	private static double getControlLevel(Scanner sc, GroupQuestion gq, boolean isPre) {
		if (isPre) {
			System.out.println(gq.getPreControl());
		} else {
			System.out.println(gq.getPostControl());
		}
		
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
	
	private static SequenceQuestion startChatting(Scanner sc, GroupQuestion gq, int seq,
		SelectRecommedation sr, double ctlLv, int age, boolean isMale) {
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader keyboard = new BufferedReader(in);
		SequenceQuestion seqQ = new SequenceQuestion();
		Question q1, q2, q3, q4;
		String answer = "";
		String target_thought = "";
		String target_behaviour = "";
		String target_goal = "";
		try {
			System.out.println("---------------------------------------------------");
			switch (seq) {
				case 1:
					System.out.println("Sequence 1:");
					q1 = gq.getQuestionG1(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					target_goal = keyboard.readLine();
					break;
				case 2:
					System.out.println("Sequence 2:");
					q1 = gq.getQuestionG1(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q4 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q3.getGroup(), target_thought, "", true);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					target_goal = keyboard.readLine();;
					break;
				case 3:
					System.out.println("Sequence 3:");
					q1 = gq.getQuestionG9(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					q3 = q1.getNextQuestion(q2.getGroup(), target_thought, answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					target_goal = keyboard.readLine();
					break;
				case 4:
					System.out.println("Sequence 4:");
					q1 = gq.getQuestionG6(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					target_goal = keyboard.readLine();
					break;
				case 5:
					System.out.println("Sequence 5:");
					q1 = gq.getQuestionG6(0);
					System.out.println("C: " + q1);	
					System.out.print("P: " + q1.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					answer = keyboard.readLine();
					q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
					System.out.println("C: " + q3);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q4 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q3.getGroup(), target_thought, "", true);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					target_goal = keyboard.readLine();
					break;
				case 6:
					System.out.println("Sequence 6:");
					q1 = gq.getQuestionG5(0, "", false);
					System.out.println("C: " + q1);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q2 = gq.getQuestionG8(q1.getGroup(), "", target_behaviour, false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					target_goal = keyboard.readLine();
					q3 = gq.getQuestionG7(q2.getGroup(), "");
					System.out.println("C: " + q3);	
					System.out.print("P: " + q3.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q4 = gq.getQuestionG2(q3.getGroup(), target_thought, "", false);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					answer = keyboard.readLine();
					break;
				case 7:
					System.out.println("Sequence 7:");
					q1 = gq.getQuestionG10(0, "", false, false);
					System.out.println("C: " + q1);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q2 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q1.getGroup(), "", target_behaviour, false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					target_goal = keyboard.readLine();
					q3 = gq.getQuestionG7(q2.getGroup(), "");
					System.out.println("C: " + q3);	
					System.out.print("P: " + q3.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q4 = gq.getQuestionG2(q3.getGroup(), target_thought, "", false);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					answer = keyboard.readLine();
					break;
				case 8:
					System.out.println("Sequence 8:");
					q1 = gq.getQuestionG11(0, "", false, false);
					System.out.println("C: " + q1);
					System.out.print("P: ");
					target_behaviour = keyboard.readLine();
					q2 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q1.getGroup(), "", target_behaviour, false);
					System.out.println("C: " + q2);
					System.out.print("P: ");
					target_goal = keyboard.readLine();
					q3 = gq.getQuestionG7(q2.getGroup(), "");
					System.out.println("C: " + q3);	
					System.out.print("P: " + q3.getAnswerPrefix());
					target_thought = keyboard.readLine();
					q4 = gq.getQuestionG2(q3.getGroup(), target_thought, "", false);
					System.out.println("C: " + q4);
					System.out.print("P: ");
					answer = keyboard.readLine();
					break;
				default:
					System.out.println("Sequence number is not match. try again!");
					q1 = null; q2 = null; q3 = null; q4 = null;
					break;
			}
			System.out.println("C: " + gq.getSummary(target_thought, target_behaviour, target_goal));
			System.out.println("---------------------------------------------------");
			seqQ.setQuestion(q1, 1);
			seqQ.setQuestion(q2, 2);
			seqQ.setQuestion(q3, 3);
			seqQ.setQuestion(q4, 4);
			seqQ.setAnswer(answer);
			seqQ.setThought(target_thought);
			seqQ.setBehaviour(target_behaviour);
			seqQ.setGoal(target_goal);
			
		} catch (Exception ex) { 
			ex.printStackTrace(); 
		}
		return seqQ;
	}
	
	private static SequenceQuestion getTranscript(GroupQuestion gq, int seq,
			SelectRecommedation sr, double ctlLv, int age, boolean isMale) {
			SequenceQuestion seqQ = new SequenceQuestion();
			Question q1, q2, q3, q4;
			String answer = "";
			String target_thought = "";
			String target_behaviour = "";
			String target_goal = "";
		System.out.println("---------------------------------------------------");
		switch (seq) {
			case 1:
				System.out.println("Sequence 1:");
				q1 = gq.getQuestionG1(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I am waiting for the feedback and I haven't talked to my supervisor about it yet.";
				System.out.println("P: " + target_behaviour);
				q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true);
				System.out.println("C: " + q4);
				target_goal = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + target_goal);
				break;
			case 2:
				System.out.println("Sequence 2:");
				q1 = gq.getQuestionG1(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				q2 = gq.getQuestionG2(1, target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + target_behaviour);
				q4 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q3.getGroup(), target_thought, "", true);
				System.out.println("C: " + q4);
				target_goal = "I concern about my grade and whether I graduate.";
				System.out.println("P: " + target_goal);
				break;
			case 3:
				System.out.println("Sequence 3:");
				q1 = gq.getQuestionG9(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				q2 = gq.getQuestionG2(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I feel unhappy about it. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				q3 = q1.getNextQuestion(q2.getGroup(), target_thought, answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I aware of the feedback of my last presentation.";
				System.out.println("P: " + target_behaviour);
				q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true);
				System.out.println("C: " + q4);
				target_goal = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + target_goal);
				break;
			case 4:
				System.out.println("Sequence 4:");
				q1 = gq.getQuestionG6(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				q3 = gq.getQuestionG5(q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I am waiting for the feedback and I haven’t talked to my supervisor about it yet.";
				System.out.println("P: " + target_behaviour);
				q4 = gq.getQuestionG8(q3.getGroup(), target_thought, "", true);
				System.out.println("C: " + q4);
				target_goal = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + target_goal);
				break;
			case 5:
				System.out.println("Sequence 5:");
				q1 = gq.getQuestionG6(0);
				System.out.println("C: " + q1);	
				target_thought = "my work.";
				System.out.println("P: " + q1.getAnswerPrefix() + target_thought);
				q2 = q1.getNextQuestion(q1.getGroup(), target_thought, "", false);
				System.out.println("C: " + q2);
				answer = "I think I feel unhappy with my last presentation. I was too nervous and not confident.";
				System.out.println("P: " + answer);
				q3 = gq.getGroup10OR11(sr, ctlLv, age, isMale, q2.getGroup(), answer, true);
				System.out.println("C: " + q3);
				target_behaviour = "I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + target_behaviour);
				q4 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q3.getGroup(), target_thought, "", true);
				System.out.println("C: " + q4);
				target_goal = "I concern about my grade and whether I graduate.";
				System.out.println("P: " + target_goal);
				break;
			case 6:
				System.out.println("Sequence 6:");
				q1 = gq.getQuestionG5(0, "", false);
				System.out.println("C: " + q1);
				target_behaviour = "I am waiting for the feedback of my presentation and I haven’t talked to my supervisor about it yet.";
				System.out.println("P: " + target_behaviour);
				q2 = gq.getQuestionG8(q1.getGroup(), "", target_behaviour, false);
				System.out.println("C: " + q2);
				target_goal = "I think I want to take course about presentation for academic and I think it would help me to improve my presentation skill.";
				System.out.println("P: " + target_goal);
				q3 = gq.getQuestionG7(q2.getGroup(), "");
				System.out.println("C: " + q3);	
				target_thought = "my work. I feel I was too nervous and not confident on my last presentation and I should try to way to improve myself.";
				System.out.println("P: " + q3.getAnswerPrefix() + target_thought);
				q4 = gq.getQuestionG2(q3.getGroup(), target_thought, "", false);
				System.out.println("C: " + q4);
				answer = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + answer);
				break;
			case 7:
				System.out.println("Sequence 7:");
				q1= gq.getQuestionG10(0, "", false, false);
				System.out.println("C: " + q1);
				target_behaviour = "I worry about my last presentation. I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + target_behaviour);
				q2 = gq.getGroup3OR4(sr, ctlLv, age, isMale, q1.getGroup(), "", target_behaviour, false);
				System.out.println("C: " + q2); //
				target_goal = "I want to graduate with a good grade.";
				System.out.println("P: " + target_goal);
				q3 = gq.getQuestionG7(q2.getGroup(), "");
				System.out.println("C: " + q3);	
				target_thought = "my work and I feel I was too nervous and not confident on my last presentation and I might not get a good grade on it.";
				System.out.println("P: " + q3.getAnswerPrefix() + target_thought);
				q4 = gq.getQuestionG2(q3.getGroup(), target_thought, "", false);
				System.out.println("C: " + q4);
				answer = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + answer);
				break;
			case 8:
				System.out.println("Sequence 8:");
				q1 = gq.getQuestionG11(0, "", false, false);
				System.out.println("C: " + q1);
				target_behaviour = "I worry about my last presentation. I cannot stop thinking about it even though I feel I am too serious about it.";
				System.out.println("P: " + target_behaviour);
				q2 = gq.getGroup3OR4(sr, ctlLv, age, isMale, 11, "", target_behaviour, false);
				System.out.println("C: " + q2); //
				target_goal = "I want to graduate with a good grade.";
				System.out.println("P: " + target_goal);
				q3 = gq.getQuestionG7(q2.getGroup(), "");
				System.out.println("C: " + q3);	
				target_thought = "my work. I feel I was too nervous and not confident on my last presentation and I might not get a good grade on it.";
				System.out.println("P: " + q3.getAnswerPrefix() + target_thought);
				q4 = gq.getQuestionG2(q3.getGroup(), target_thought, "", false);
				System.out.println("C: " + q4);
				answer = "I feel unhappy about it and want to do it better.";
				System.out.println("P: " + answer);
				break;
			default:
				System.out.println("Sequence number is not match. try again!");
				q1 = null; q2 = null; q3 = null; q4 = null;
				break;
		}
		System.out.println("C: " + gq.getSummary(target_thought, target_behaviour, target_goal));
		System.out.println("---------------------------------------------------");
		seqQ.setQuestion(q1, 1);
		seqQ.setQuestion(q2, 2);
		seqQ.setQuestion(q3, 3);
		seqQ.setQuestion(q4, 4);
		seqQ.setAnswer(answer);
		seqQ.setThought(target_thought);
		seqQ.setBehaviour(target_behaviour);
		seqQ.setGoal(target_goal);
		return seqQ;
	}

	private static void SeqRespond(Scanner sc, GroupQuestion gq, SequenceQuestion seqQ) {
		boolean isFirst = true;
		for (int i=2; i<seqQ.getQNo()+1; i++) {
			if (seqQ.getQuestion(i).isAmbiguious()) {
				System.out.println(gq.getAmbiguous(seqQ.getQuestion(i-1).getQuestion(), seqQ.getQuestion(i).getQuestion(), isFirst));
				isFirst = false;
				int group = seqQ.getQuestion(i).getGroup();
				double rate = getLikeRate(sc);
				//Record Data
				switch (group) {
					case 3:
						record(rate);
						break;
					case 4:
						record(rate);
						break;
					case 10:
						record(rate);
						break;
					case 11:
						record(rate);
						break;
					default:
						break;
				}
			}
		}
	}

	private static void RecommendedRespond(Scanner sc, GroupQuestion gq) {
		System.out.println(gq.getIntvention(true));
		double slotRate = getLikeRate(sc);
		//Record Data
		record(slotRate);
		System.out.println(gq.getIntvention(false));
		double freqRate = getLikeRate(sc);
		//Record Data
		record(freqRate);
	}
	
	private static double getLikeRate(Scanner sc) {
		double rate = 0.0;
		char lv = sc.next().charAt(0);
		if (lv=='1') {
			rate = 1.0;
		} else if (lv=='2') {
			rate = 0.5;
		} else {
			rate = 0.0;
		}
		
		return rate;
	}
	
	private static void record(double inp) {
		
	}
	
	private static void recordControlLevel(double preCtlLv, double postCtlLv) {
		
	}
	
	private static void trainNet() {
		//Train
	}
}
