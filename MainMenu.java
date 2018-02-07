import org.lwjgl.opengl.Display;

import Shapes.*;

public class MainMenu {
	boolean isActive = true;
	Game game;
	int height = Display.getHeight();
	int width = Display.getWidth();
	Text welcome = new Text(width/5, height-50, "Circle Game\n"
			+ "Select Mode");
	Text streakMode = new Text(width/15, height-height/4, "Timed Mode");
	Text survivalMode = new Text(width/3, height-(height/4)-25, "Survival Mode");
	Text help = new Text(width/10, height/4, "Press Q for help");
	Rectangle r = new Rectangle();
	MainMenu ctx = this;
	public MainMenu(final Game game) {
		// TODO Auto-generated constructor stub
		this.game = game;
		streakMode.hover = new Shape.onHoverListener() {
			
			@Override
			public void onHover(int x, int y) {
				// TODO Auto-generated method stub
				//System.out.println(streakMode.text);
				r.cx = (width/15)+(streakMode.text.length()*(streakMode.hw/2));
				r.cy = (height-height/4)+(streakMode.hw/2);
				r.sideLengths[0] = (streakMode.text.length()*streakMode.hw)+25;
				r.sideLengths[1] = streakMode.hw+25;//25 is border size
				game.em.spawnIntoScreen(r);
				game.em.remove(r);
			}
		};
		
		streakMode.click = new Shape.onClickListener() {
			
			@Override
			public void onClick(int x, int y) {
				// TODO Auto-generated method stub
				game.startGame("streak");
			}
		};
		
		survivalMode.click = new Shape.onClickListener() {
			
			@Override
			public void onClick(int x, int y) {
				// TODO Auto-generated method stub
				//System.out.println("Clickk");
				ctx.isActive = false;
				game.startGame("survival");
			}
		};
		survivalMode.hover = new Shape.onHoverListener() {
			
			@Override
			public void onHover(int x, int y) {
				// TODO Auto-generated method stub
				//System.out.println(survivalMode.text);
				r.cx = survivalMode.cx+(survivalMode.text.length()*(survivalMode.hw/2));
				r.cy = survivalMode.cy+(survivalMode.hw/2);
				r.sideLengths[0] = (survivalMode.text.length()*survivalMode.hw)+25;
				r.sideLengths[1] = survivalMode.hw+25;//25 is border size
				game.em.spawnIntoScreen(r);
				game.em.remove(r);
			}
		};
		
	}
	public void draw(){
		welcome.draw();
		streakMode.draw();
		survivalMode.draw();
		help.draw();
	}

}
