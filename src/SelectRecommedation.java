import java.util.Random;



public class SelectRecommedation {
	
	private Random rand;
	private final double RECOMMEND_RATE = 1.0;
	private final double RATE = 0.1;
	private final int MIN_FREQ = 1;
	private final int MAX_FREQ = 4;
	
	public SelectRecommedation() {
		rand = new Random();
	}
	
	public int selectFrequency(double lastCtlLv, int age, boolean isMale) {
		FrequencyInterventionNet fin = new FrequencyInterventionNet();
		
		double freq = fin.getRate(lastCtlLv, age, isMale, RECOMMEND_RATE) + getRandomRate();
		
		if ((int) Math.floor(freq*10 + 0.5) < MIN_FREQ) {
			return MIN_FREQ;
		} else if ((int) Math.floor(freq*10 + 0.5) > MAX_FREQ) {
			return MAX_FREQ;
		} else {
			return (int) Math.floor(freq*10 + 0.5);
		}
	}
	
	public int selectTimeSlot(double lastCtlLv, int age, boolean isMale) {
		TimeSlotInterventionNet tsin = new TimeSlotInterventionNet();
		
		double[] slotRate = tsin.getRate(lastCtlLv, age, isMale, RECOMMEND_RATE);

		return tsin.getSlot(getMaxRandomRate(slotRate));
	}
	
	public int selectSequence(double ctlLv, int age, boolean isMale) {
		SelectSequenceNet ssq = new SelectSequenceNet();
		
		double[] seqRate = ssq.getRate(ctlLv, age, isMale, RECOMMEND_RATE);

		return ssq.getSequence(getMaxRandomRate(seqRate));
	}
	
	public int selectReplayGoal(double ctlLv, int age, boolean isMale, int prevGroup) {
		SelectGQGoalNet sgqg = new SelectGQGoalNet();
		
		double[] rateGQGoal = sgqg.getRate(ctlLv, age, isMale, prevGroup);
		
		return sgqg.getGroupQuestion(getMaxRandomRate(rateGQGoal));
	}
	
	public int selectReflectBehaviour(double ctlLv, int age, boolean isMale, int prevGroup) {
		SelectGQBehaviourNet sgqb = new SelectGQBehaviourNet();
		
		double[] rateGQBehaviour = sgqb.getRate(ctlLv, age, isMale, prevGroup);
		
		return sgqb.getGroupQuestion(getMaxRandomRate(rateGQBehaviour));
	}
	
	private int getMaxRandomRate(double[] rate) {
		int maxIdx = 0;
		double max = Double.MIN_VALUE;
		for (int i=0; i<rate.length; i++) {
			double rndRate = rate[i] + getRandomRate();
			if (max<rndRate) {
				max = rndRate;
				maxIdx = i;
			}
		}
		return maxIdx;
	}
	
	private double getRandomRate() {
		switch (rand.nextInt(3)) {
		case 0:
			return 0;
		case 1:
			return RATE;
		default:
			return -RATE;
		}
	}

}

