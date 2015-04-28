import java.util.Random;


public class GroupQuestion {
	//Target
	private final String target_thought = "your thought";
	//private final String target_behaviour = "your behaviour";
	private final String target_goal = "your goal";
	
	//Group 1
	private final String[] g1 = { "What’s going through your mind just at the minute?",
								  "What would you say about your state of mind just now?",
								  "What thought is running through your mind?",
								  "What are you most aware of right now?",
								  "How would you describe your frame of mind just now?",
								  "Where's your attention just now?",
								  "What are you thinking about as you answer these questions?",
								  "What are your thoughts doing just now?",
								  "How would you describe the inside of your mind at the moment?",
								  "What’s your mind playing with just now?",
								  "What’s the main thing in your frame of mind?" };
	private final String[] g1_prefix = { "My thought at the minute is ",
										 "I am thinking about ",
										 "My current thought is ",
										 "I am thinking about ",
										 "I am thinking about ",
										 "My attention now is ",
										 "I am thinking about ",
										 "My thought now is ",
										 "I am thinking about ",
										 "My mind is playing with ",
										 "I am thinking about " };
			 
	//Group 2
	private final String[] g2 = { "How would you comment on $$$ just now?" };
	private final String g2_default = "last thought";
	private final String g2_pattern = "this thought about ###";
			 
	//Group 3
	private final String[] g3 = { "What goal are you heading for right now?",
								"What’s your mind up to right now?" };
			 
	//Group 4
	private final String[] g4 = { "What’s important to you at the moment?",
								  "What’s uppermost in your mind at the moment?" };
			 
	//Group 5
	private final String[] g5 = { "How are things sitting for you at the moment?",
								  "What do you think about yourself at the moment?" };
			 
	//Group 6-1
	private final String[] g61 = { "Replay the thought that just went through your mind.",
								  "What is your background thought?",
								  "What is going through your mind at this very minute?",
								  "What are you thinking about just now?",
								  "What’s the main thought running through your mind just now?",
								  "Where’s your mind at just now?",
								  "How are you travelling just now?",
								  "What’s in your mind right now?" };
	//Group 6-prefix
	private final String[] g61_prefix = { "My thought is ",
										 "My background thought is ",
										 "My thought is ",
										 "I am thinking about ",
										 "My thought is ",
										 "My mind now is at ",
										 "I am thinking about ",
										 "I am thinking about " };
	//Group 6-2
	private final String[] g62 = { "How does it sound?",
							       "What does it mean?",
							       "What do you make of $$$?",
							       "What do you think about $$$?",
							       "What do you make of $$$?",
							       "What do you make of $$$?",
							       "What's running through your mind as you think about $$$?",
							       "How do things look to you just now?" };
	private final String g6_default = "that";
	private final String g6_pattern = "###";

	//Group 7
	private final String[] g7 = { "How would you describe the state your mind is in right now? What goals do you connect with them?",
								  "What’s occurring to you right now? Can you relate that to an important goal you have?",
								  "What crossed your mind just then? What goal does that relate to?" };
	//Group 7-prefix
	private final String[] g7_prefix = { "I am thinking about ",
										 "I am thinking about ",
										 "My current thought is " };
			
	//Group 8
	private final String[] g8 = { "What are you going for right now? What do you think about that?" };
			 
	//Group 9-1
	private final String[] g91 = { "Where’s your mind at just now?",
								   "What stands out in your mind just now?",
								   "What thought is going through your mind right now?" };
	//Group 9-prefix
	private final String[] g91_prefix = { "I am thinking about ",
										  "I am thinking about ",
										  "My thought right now is " };
	//Group 9-2
	private final String[] g92 = { "What aspect of yourself are you most aware of at this moment?",
							   	   "What are you most aware of at the moment?",
								   "What feelings are you most aware of?" };
			 
	//Group 10
	private final String[] g10 = { "Where's your life at just now? How do you feel about that?" };
	
	//Group 11
	private final String[] g11 = { "What’s sitting at the front of your mind just now? How are you travelling overall?",
								   "What’s the activity of your mind just now? How do you feel about that?",
								   "What’s occupying your mind at the moment? How does that sit with you?",
								   "What’s your perspective on things right now? What s the thought that stands out the most?" };
			 
	//Linking
	private final String[] change_topic = { "Now I understand about ###, then we move to the topic about @@@.\n",
											"I understand about ###, now let's talk about @@@.\n" };
	private final String change_topic_default = "that";
	private final String[] echo = { "You said that %%%.\n",
									"It sounds that %%%.\n" };
	private final String link = "So, ";
	
	//Summary
	private final String summary = "Overall, you have said that your current thought is about ###.\n"
			+ "Your current state is that %%%.\n"
			+ "In addition, your goal is that &&&.";
	private final String summary_default = "yourself";
	
	private Random rand;
	
	public GroupQuestion() {
		rand = new Random();
	}
	
	//ControlLevel
	private final String preControl = "To what extent do you feel you are in control of your life recently?\n";
	private final String postControl = "After answering the question, I hope you feel better.\n"
			+ "So, let us check again. To what extent do you feel you are in control of your life at the moment?\n";
	private final String controlChoice = "(1) Not at all\n"
			+ "(2) To little extent\n"
			+ "(3) To some extent\n"
			+ "(4) To a moderate extent\n"
			+ "(5) To a large extent";
	
	//Response
	private String likeChoice = "(1) Like\n"
			+ "(2) Neutral\n"
			+ "(3) Unlike";
	
	//Ambiguous
	private String ambPreQuestion = "Now I would like to ask you about your likes and dislikes about the series of question.\n";
	private String ambQuestion = "Do you like that the question '%%%' is followed by '&&&'?\n";
	//Intervention
	private String preIntvQuestion = "OK. I would now like to ask about the interventions to see if they are good for you.\n";
	private String slotIntvQuestion = "Do you like the present timing frequency of interventions?\n";
	private String freqIntvQuestion = "Do you like the present timing slot of interventions?\n";
	
	public String getPreControl() {
		return preControl + controlChoice;
	}
	public String getPostControl() {
		return postControl + controlChoice;
	}
	
	public Question getQuestionG1(int prevGroup) {
		int n = getRandom(g1.length);
		Question q = new Question(1, g1[n]);
		q.setPrevGroup(prevGroup);
		q.setAnswerPrefix(g1_prefix[n]);
		return q;
	}
	
	public Question getQuestionG2(int prevGroup, String prevAns, String echoAns, boolean isEcho) {
		Question q = new Question(2, g2[getRandom(g2.length)]);
		q.setPrevGroup(prevGroup);
		q.setIsEcho(isEcho);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setPrevAnswer(prevAns);
		q.setPattern(g2_pattern);
		q.setDefaultPattern(g2_default);
		q.setLink(link);
		return q; 
	}
	
	public Question getQuestionG3(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isAmbiguious) {
		Question q = new Question(3, g3[getRandom(g3.length)]);
		q.setIsAmbiguious(isAmbiguious);
		q.setPrevGroup(prevGroup);
		q.setIsChangeTopic(isChangeTopic);
		q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
		q.setDefaultTopic(change_topic_default);
		q.setTarget(target_goal);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setPrevAnswer(prevAns);
		q.setLink(link);
		return q;
	}
	
	public Question getQuestionG4(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isAmbiguious) {
		Question q = new Question(4, g4[getRandom(g4.length)]);
		q.setIsAmbiguious(isAmbiguious);
		q.setPrevGroup(prevGroup);
		q.setIsChangeTopic(isChangeTopic);
		q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
		q.setDefaultTopic(change_topic_default);
		q.setTarget(target_goal);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setPrevAnswer(prevAns);
		q.setLink(link);
		return q;
	}
	
	public Question getQuestionG5(int prevGroup, String echoAns, boolean isEcho) {
		Question q = new Question(5, g5[getRandom(g5.length)]);
		q.setPrevGroup(prevGroup);
		q.setIsEcho(isEcho);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setLink(link);
		return q;
	}
	
	public Question getQuestionG6(int prevGroup) {
		int n = getRandom(g61.length);
		Question q = new Question(61, g61[n]);
		q.setPrevGroup(prevGroup);
		q.setAnswerPrefix(g61_prefix[n]);
		
		Question qs = new Question(62, g62[n]);
		qs.setPrevGroup(prevGroup);
		qs.setPattern(g6_pattern);
		qs.setDefaultPattern(g6_default);
		qs.setLink(link);
		
		q.setNextQuestion(qs);
		return q;
	}
	
	public Question getQuestionG7(int prevGroup, String prevAns) {
		int n = getRandom(g7.length);
		Question q = new Question(7, g7[n]);
		q.setPrevGroup(prevGroup);
		q.setAnswerPrefix(g7_prefix[n]);
		q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
		q.setDefaultTopic(change_topic_default);
		q.setTarget(target_thought);
		q.setPrevAnswer(prevAns);
		return q;
	}
	
	public Question getQuestionG8(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic) {
		Question q = new Question(8, g8[getRandom(g8.length)]);
		q.setPrevGroup(prevGroup);
		q.setIsChangeTopic(isChangeTopic);
		q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
		q.setDefaultTopic(change_topic_default);
		q.setTarget(target_goal);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setPrevAnswer(prevAns);
		q.setLink(link);
		return q;
	}
	
	public Question getQuestionG9(int prevGroup) {
		int n = getRandom(g91.length);
		Question q = new Question(91, g91[n]);
		q.setPrevGroup(prevGroup);
		q.setAnswerPrefix(g91_prefix[n]);
		
		Question qs = new Question(92, g92[n]);
		qs.setEcho(echo[getRandom(echo.length)]);
		qs.setLink(link);
		
		q.setNextQuestion(qs);
		return q;
	}
	
	public Question getQuestionG10(int prevGroup, String echoAns, boolean isEcho, boolean isAmbiguous) {
		Question q = new Question(10, g10[getRandom(g10.length)]);
		q.setIsAmbiguious(isAmbiguous);
		q.setPrevGroup(prevGroup);
		q.setIsEcho(isEcho);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setLink(link);
		return q;
	}
	
	public Question getQuestionG11(int prevGroup, String echoAns, boolean isEcho, boolean isAmbiguous) {
		Question q = new Question(11, g11[getRandom(g11.length)]);
		q.setIsAmbiguious(isAmbiguous);
		q.setPrevGroup(prevGroup);
		q.setIsEcho(isEcho);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setLink(link);
		return q;
	}
	
	public String getSummary(String thought, String behaviour, String goal) {
		return Transform.replaceTransformPattern("&&&",
				Transform.replaceTransformPattern("%%%",
				Transform.replacePartial(summary, thought, summary_default), behaviour, false), goal, false);
	}
	
	public String getAmbiguous(String prevQ, String Q, boolean isFirst) {
		return Transform.replacePattern("&&&",
				Transform.replacePattern("%%%", (isFirst ? ambPreQuestion : "") + ambQuestion + likeChoice, prevQ), Q);
	}
	
	public String getIntvention(boolean isSlot) {
		if (isSlot) {
			return preIntvQuestion + slotIntvQuestion + likeChoice;
		} else {
			return freqIntvQuestion + likeChoice;
		}
	}
	
	private int getRandom(int length) {
		return rand.nextInt(length);
	}
	
	public Question getGroup3OR4(SelectRecommedation sgq, double ctlLv, int age, boolean isMale, int prevGroup, 
								 String prevAns, String echoAns, boolean isChangeTopic) {
		int group = sgq.selectReplayGoal(ctlLv, age, isMale, prevGroup);
		if (group==3) {
			return getQuestionG3(prevGroup, prevAns, echoAns, isChangeTopic, true);
		} else {
			return getQuestionG4(prevGroup, prevAns, echoAns, isChangeTopic, true);
		}
	}
	
	public Question getGroup10OR11(SelectRecommedation sgq, double ctlLv, int age, boolean isMale, int prevGroup, 
								   String echoAns, boolean isEcho) {
		int group = sgq.selectReflectBehaviour(ctlLv, age, isMale, prevGroup);
		if (group==10) {
			return getQuestionG10(prevGroup, echoAns, isEcho, true);
		} else {
			return getQuestionG11(prevGroup, echoAns, isEcho, true);
		}
	}
	
}
