
public class Question {
	private int _questionGroup;
	private int _prevGroup;
	private Question _nextQuestion = null;
	private String _question = "";
	private String _answerPrefix = "";
	private String _echo = "";
	private String _changeTopic = "";
	private String _defaultTopic = "";
	private String _target = "";
	private String _prevAnswer = "";
	private String _echoAnswer = "";
	private String _pattern = "";
	private String _defaultPattern = "";
	private String _link = "";
	private boolean _isEcho;
	private boolean _isChangeTopic;
	
	public Question(int group, String question) {
		this._questionGroup = group;
		this._question = question;
	}
	
	public int getGroup() {
		return this._questionGroup;
	}
	public int getPrevGroup() {
		return this._prevGroup;
	}
	public void setPrevGroup(int prevGroup) {
		this._prevGroup = prevGroup;
	}
	
	public String getAnswerPrefix() {
		return this._answerPrefix;
	}
	public void setAnswerPrefix(String answerPrefix) {
		
		this._answerPrefix = answerPrefix;
	}
	
	public Question getNextQuestion(String prevAns) {
		this._nextQuestion.setPrevAnswer(prevAns);
		return this._nextQuestion;
	}
	public void setNextQuestion(Question nextQuestion) {
		this._nextQuestion = nextQuestion;
	}
	
	public void setEcho(String echo) {
		this._echo = echo;
	}
	public void setChangeTopic(String changeTopic) {
		this._changeTopic = changeTopic;
	}
	public void setDefaultTopic(String defaultTopic) {
		this._defaultTopic = defaultTopic;
	}
	public void setTarget(String target) {
		this._target = target;
	}
	public void setPrevAnswer(String prevAnswer) {
		this._prevAnswer = prevAnswer;
	}
	public void setPrevEcho(String echoAnswer) {
		this._echoAnswer = echoAnswer;
	}
	public void setPattern(String pattern) {
		this._pattern = pattern;
	}
	public void setDefaultPattern(String defaultPattern) {
		this._defaultPattern = defaultPattern;
	}
	public void setLink(String link) {
		this._link = link;
	}
	public void setIsEcho(boolean isEcho) {
		this._isEcho = isEcho;
	}
	public void setIsChangeTopic(boolean isChangeTopic) {
		this._isChangeTopic = isChangeTopic;
	}
	
	private String defineQuestion() {
		StringBuilder sb = new StringBuilder();
		switch (this._questionGroup) {
		case 1:
			sb.append(this._question);
			break;
		case 2:
			if (this._isEcho) {
				sb.append(Transform.replaceFull(this._echo, this._echoAnswer));
				sb.append(this._link);
			}	
			sb.append(Transform.replaceQuestion(this._question, this._prevAnswer, this._pattern, this._defaultPattern));
			break;
		case 3:
			if (this._isChangeTopic) {
				sb.append(Transform.replaceTopic(
							Transform.replacePartial(this._changeTopic, this._prevAnswer, this._defaultTopic), 
							this._target));
			} else {
				sb.append(Transform.replaceFull(this._echo, this._echoAnswer));
				sb.append(this._link);
			}	
			sb.append(this._question);
			break;
		case 4:
			if (this._isChangeTopic) {
				sb.append(Transform.replaceTopic(
							Transform.replacePartial(this._changeTopic, this._prevAnswer, this._defaultTopic), 
							this._target));
			} else {
				sb.append(Transform.replaceFull(this._echo, this._echoAnswer));
				sb.append(this._link);
			}	
			sb.append(this._question);
			break;
		case 5:
			if (this._isEcho) {
				sb.append(Transform.replaceFull(this._echo, this._echoAnswer));
				sb.append(this._link);
			}	
			sb.append(this._question);
			break;
		case 61:
			sb.append(this._question);
			break;
		case 62:
			sb.append(this._link);
			sb.append(Transform.replaceQuestion(this._question, this._prevAnswer, this._pattern, this._defaultPattern));
			break;
		case 7:
			sb.append(Transform.replaceTopic(
					Transform.replacePartial(this._changeTopic, this._prevAnswer, this._defaultTopic), 
					this._target));	
			sb.append(this._question);
			break;
		case 8:
			if (this._isChangeTopic) {
				sb.append(Transform.replaceTopic(
							Transform.replacePartial(this._changeTopic, this._prevAnswer, this._defaultTopic), 
							this._target));
			} else {
				sb.append(Transform.replaceFull(this._echo, this._echoAnswer));
				sb.append(this._link);
			}	
			sb.append(this._question);
			break;
		case 91:
			sb.append(this._question);
			break;
		case 92:
			sb.append(this._link);
			sb.append(this._question);
			break;
		case 10:
			if (this._isEcho) {
				sb.append(Transform.replaceFull(this._echo, this._echoAnswer));
				sb.append(this._link);
			}	
			sb.append(this._question);
			break;
		case 11:
			if (this._isEcho) {
				sb.append(Transform.replaceFull(this._echo, this._echoAnswer));
				sb.append(this._link);
			}	
			sb.append(this._question);
			break;
		default:
			break;
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return defineQuestion();
	}
}
