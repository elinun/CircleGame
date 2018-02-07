import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

import Shapes.Circle;
import Shapes.Rectangle;
import Shapes.Shape;

public class EntityManager {
	int maxEntities = 16;
	private Rectangle player = null;
	private List<Shape> entitiesOnScreen = new CopyOnWriteArrayList<Shape>();
	public GameStats gStats;
	private boolean isPaused = false;
	public EntityManager(GameStats stats){
		this.gStats = stats;
	}
	public int getNumberEntities(){
		return entitiesOnScreen.size();
	}
	public void removeAllEntities(){
		Iterator i = entitiesOnScreen.iterator();
		while(i.hasNext()){
			entitiesOnScreen.remove(i.next());
		}
	}
	
	public void removeAllEntities(Class s){
		Iterator i = entitiesOnScreen.iterator();
		while(i.hasNext()){
			Shape e = (Shape)i.next();
			if(s == e.getClass()){
				entitiesOnScreen.remove(e);
			}
		}
	}
	public void pause(){
		isPaused = true;
	}
	public void resume(){
		isPaused = false;
	}
	public void spawnIntoScreen(Shape s){
		if(!(entitiesOnScreen.size()<maxEntities)){
			System.err.println("Error Spawning entity "+s+". The maximum number of entites has already been reached.");
			return;
		}
		s.draw();
		entitiesOnScreen.add(s);
	}
	
	public void spawnIntoScreen(Shape s, float speedX, float speedY){
		if(entitiesOnScreen.size()<maxEntities){
		s.speedX = speedX;
		s.speedY = speedY;
		s.draw();
		entitiesOnScreen.add(s);
		}else{
			System.err.println("Error Spawning entity "+s+". The maximum number of entites has already been reached.");
			return;
		}
	}
	public void remove(Shape s){
		entitiesOnScreen.remove(s);
	}
	public void drawAllEntities(){
		try{
			if(Start.queue.size()>0){
				Start.queue.poll().run();
			}
		}catch(Exception e){
			System.out.println("No Circle");
		}
		
		Iterator i = entitiesOnScreen.iterator();
		while(i.hasNext()){
			Shape e = (Shape)i.next();
			if(e.isRectangle()) this.player = (Rectangle)e;
			
			if((Math.abs(e.speedX)>0.0f || Math.abs(e.speedY)>0.0f) && isPaused == false){
				if(e.isRectangle()){
					Rectangle r = (Rectangle)e;
					r.cx += e.speedX;
					r.cy += e.speedY;
					if(r.cx>Display.getWidth() || r.cy >Display.getHeight()){
						entitiesOnScreen.remove(e);
					}
				}
				else{
					Circle c = (Circle)e;
					c.cx += e.speedX;
					c.cy += e.speedY;
					if(c.cx>Display.getWidth() || c.cx < 0 ||c.cy<0){
						if(c.cx < 900 && c.cx > 0)//The player let it go out the bottom
						{
//							/System.out.println(Display.getDisplayMode().getWidth());
							this.gStats.addStrike();
							this.gStats.setStat(GameStats.stats.STREAK, 0);
						}
						entitiesOnScreen.remove(e);
					}
					
					if(haveEntitiesCollided((Circle)e, player)){
						//System.out.println("Collision!");
						entitiesOnScreen.remove(e);
						this.gStats.addStat(GameStats.stats.ballsHit, 1);
						this.gStats.addStat(GameStats.stats.STREAK, 1);
						this.gStats.addStat(GameStats.stats.SCORE, this.gStats.streak*2);
					}
				}
			}
			e.draw();
			}
	}
	public boolean haveEntitiesCollided(Circle one, Rectangle two){
		if(one != null && two != null){
			//System.out.println("Test");
			if(one.cy-one.radius <= (two.cy+(two.sideLengths[1]/2)) && one.cx >= (two.cx-(two.sideLengths[0]/2)) && one.cx <= (two.cx+(two.sideLengths[0]/2))){
				return true;
			}
		}
		return false;
	}
}
