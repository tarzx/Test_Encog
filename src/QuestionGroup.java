import java.util.Random;


public class QuestionGroup {
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
								  "Which you are you being at the moment?" };
			 
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
									"It means %%%.\n" };
	private final String link = "So, ";
	
	private Random rand;
	
	public QuestionGroup() {
		rand = new Random();
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
	
	public Question getQuestionG3(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic) {
		Question q = new Question(3, g3[getRandom(g3.length)]);
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
	
	public Question getQuestionG4(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic) {
		Question q = new Question(4, g4[getRandom(g4.length)]);
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
		qs.setLink(link);
		
		q.setNextQuestion(qs);
		return q;
	}
	
	public Question getQuestionG10(int prevGroup, String echoAns, boolean isEcho) {
		Question q = new Question(10, g10[getRandom(g10.length)]);
		q.setPrevGroup(prevGroup);
		q.setIsEcho(isEcho);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setLink(link);
		return q;
	}
	
	public Question getQuestionG11(int prevGroup, String echoAns, boolean isEcho) {
		Question q = new Question(11, g11[getRandom(g11.length)]);
		q.setPrevGroup(prevGroup);
		q.setIsEcho(isEcho);
		q.setEcho(echo[getRandom(echo.length)]);
		q.setPrevEcho(echoAns);
		q.setLink(link);
		return q;
	}
	
	private int getRandom(int length) {
		return rand.nextInt(length);
	}
	
	public Question getGroup3OR4(SelectReplayGoalNet srgn, double ctlLv, int age, boolean isMale, int prevGroup, 
								 String prevAns, String echoAns, boolean isChangeTopic) {
		int group = srgn.getQuestionGroup(ctlLv, age, isMale, prevGroup);
		if (group==3) {
			return getQuestionG3(prevGroup, prevAns, echoAns, isChangeTopic);
		} else {
			return getQuestionG4(prevGroup, prevAns, echoAns, isChangeTopic);
		}
	}
	
	public Question getGroup10OR11(SelectReflectBehaviourNet srbn, double ctlLv, int age, boolean isMale, int prevGroup, 
								   String echoAns, boolean isEcho) {
		int group = srbn.getQuestionGroup(ctlLv, age, isMale, prevGroup);
		if (group==10) {
			return getQuestionG10(prevGroup, echoAns, isEcho);
		} else {
			return getQuestionG11(prevGroup, echoAns, isEcho);
		}
	}
	
}
