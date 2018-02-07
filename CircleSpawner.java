import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Shapes.Circle;

public class CircleSpawner {
	public int spawnInterval = 1000;
	public double misFireChance = 0.15;
	
	private EntityManager em;
	private Timer t = new Timer();
	private TimerTask tt= new TimerTask(){
		public void run(){
			Runnable r = new Runnable(){
				public void run(){
					if(Math.random()>misFireChance){
						Circle c = new Circle();
						c.color[0] = (float)Math.random();
						c.color[1] = (float)Math.random();
						c.color[2] = (float)Math.random();
						c.cx = (int) Math.floor((Math.random()*500)+50);
						c.cy = 600;
						float sx = (float)Math.floor((Math.random()*4));
						float sy = (float)Math.floor((Math.random()*-6)-1);
						if(sx > 2){
							sx -= 4;
						}
						em.spawnIntoScreen(c, sx, sy);	
						em.gStats.addStat(GameStats.stats.ballsSpawned, 1);
					}
				}
			};
			Start.queue.add(r);
		}
	};
	public CircleSpawner(EntityManager em){
		this.em = em;
	}
	public boolean isRunning(){
		return (t != null);
	}
	
	public CircleSpawner start(){
		if(!isRunning()){
		t.scheduleAtFixedRate(tt, 0, spawnInterval);}
		else{
			System.out.println("Circle Spawner Already running");
			t.scheduleAtFixedRate(tt, 0, spawnInterval);
		}
		return this;
	}
	public void stop(){
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void pause(){
		t.cancel();
		t = null;
	}
	public void resume(){
		
		t = new Timer();
		tt= new TimerTask(){
			public void run(){
				Runnable r = new Runnable(){
					public void run(){
						if(Math.random()>misFireChance){
						Circle c = new Circle();
						c.color[0] = (float)Math.random();
						c.color[1] = (float)Math.random();
						c.color[2] = (float)Math.random();
						c.cx = (int) Math.floor((Math.random()*700)+50);
						c.cy = 500;
						float sx = (float)Math.floor((Math.random()*4));
						float sy = (float)Math.floor((Math.random()*-7));
						if(sx >= 2){
							sx -= 4;
						}
						em.spawnIntoScreen(c, sx, sy);
						em.gStats.addStat(GameStats.stats.ballsSpawned, 1);
						}
					}
				};
			
				Start.queue.add(r);
			}
		};
		t.scheduleAtFixedRate(tt, 500, spawnInterval);
	}

}
