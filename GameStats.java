import Shapes.Text;

public class GameStats {
	public int strikes = 0;//misses
	public int ballsHit = 0;//streak
	public int score = 0;
	public int streak = 0;
	public int level = 1;
	public int ballsSpawned = 0;
	public Text ui = null;
	public StatChangeListener statChange = null;
	public static enum stats {
			STRIKES,
			ballsHit,
			STREAK,
			SCORE,
			LEVEL,
			ballsSpawned;
	}
	
	public void addStrike(){
		this.strikes++;
		if(statChange != null){
			statChange.onStatChange(stats.STRIKES, this.strikes);
		}
	}
	public void addStat(GameStats.stats key, int value){
		switch(key){
		case ballsHit:
			this.ballsHit += value;
			value = this.ballsHit;
			break;
		case SCORE:
			this.score += value;
			value = this.score;
			break;
		case LEVEL:
			this.level += value;
			value = this.level;
			break;
		case STREAK:
			this.streak += value;
			value = this.streak;
			break;
		case ballsSpawned:
			this.ballsSpawned += value;
			value = this.ballsSpawned;
			break;
		}
		if(statChange != null){
			statChange.onStatChange(key, value);
		}
	}
	
	public void setStat(GameStats.stats key, int value){
		switch(key){
		case ballsHit:
			this.ballsHit = value;
			value = this.ballsHit;
			break;
		case SCORE:
			this.score = value;
			value = this.score;
			break;
		case LEVEL:
			this.level = value;
			value = this.level;
			break;
		case STREAK:
			this.streak = value;
			break;
		case ballsSpawned:
			this.ballsSpawned += value;
			value = this.ballsSpawned;
			break;
		}
		if(statChange != null){
			statChange.onStatChange(key, value);
		}
	}
	public void reset(){
		strikes = 0;
		ballsHit = 0;
		score = 0;
		level =1;
		ballsSpawned = 0;
	}
	public void setStatChangeListener(StatChangeListener lis){
		this.statChange = lis;
	}
	public static interface StatChangeListener{
		public void onStatChange(GameStats.stats stat, int value);
	}

}
