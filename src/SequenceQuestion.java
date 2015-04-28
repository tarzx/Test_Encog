
public class SequenceQuestion {
	private int NUMBER_OF_QUESTION = 4;
	private Question[] qList;
	private String answer = "";
	private String target_thought = "";
	private String target_behaviour = "";
	private String target_goal = "";
	
	public SequenceQuestion() {
		qList = new Question[NUMBER_OF_QUESTION];
	}
	
	public SequenceQuestion(int nq) {
		qList = new Question[nq];
	}
	
	public int getQNo() {
		return qList.length;
	}
	
	public void setQuestion(Question q, int no) {
		if (no-1<qList.length) {
			qList[no-1] = q;
		}
	}
	
	public Question getQuestion(int no) {
		if (no-1<qList.length) {
			return qList[no-1];
		} else {
			return null;
		}
	}
	
	public void setThought(String input) {
		target_thought = input;
	}
	public void setBehaviour(String input) {
		target_behaviour = input;
	}
	public void setGoal(String input) {
		target_goal = input;
	}
	public void setAnswer(String input) {
		answer = input;
	}
	
	public String getThought() {
		return target_thought;
	}
	public String getBehaviour() {
		return target_behaviour;
	}
	public String getGoal() {
		return target_goal;
	}
	public String getAnswer() {
		return answer;
	}
}
