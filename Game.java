import java.awt.Container;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JWindow;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

import Shapes.Circle;
import Shapes.Rectangle;
import Shapes.Text;

public class Game extends Thread{
	public static int id = 0;
	GameStats stats;
    EntityManager em;
    CircleSpawner CS;
    boolean canPause = false;
    boolean f11 = false;
    boolean f3 = false;
    final boolean[] menuOpen = {false, false};//Keep track of debug and help menus, respectively.
    String gameMode = "";
    MainMenu mm;
    final Rectangle player = new Rectangle();
	public void start() {
        try {
            Display.setDisplayMode(Display.getAvailableDisplayModes()[0]);
           // Display.setFullscreen(true);
            Display.setResizable(true);
            Display.create();
            Mouse.create();          
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
         
        // init OpenGL here
              
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        //Playing with textures
        //https://github.com/mattdesl/lwjgl-basics/wiki/Textures
        stats = new GameStats();
        em = new EntityManager(stats);
        CS = new CircleSpawner(em);
            
        FPSCounter.StartTracking();
       // GL11.glEnable(GL11.GL_BLEND);
       // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //CS.start();
        Display.setTitle("CircleGame v1.1");
        mm = new MainMenu(this);
       // startGame();
        while (!Display.isCloseRequested()) {
        	//GL11.glDisable(GL11.GL_TEXTURE_2D);
        	//GL11.glEnable(GL11.GL_TEXTURE_2D);//If the debugTexture is uncommented, then this needs to be uncommented.
        	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
          //  Display.setTitle("FPS: " + FPSCounter.getFPS());
            
            if((Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Mouse.isButtonDown(0)) && player.cx > 9 && canPause){
            	player.cx -= 540/(FPSCounter.getFPS()+1);
            }
            if((Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Mouse.isButtonDown(1)) && player.cx < 800 && canPause){
            	player.cx += 540/(FPSCounter.getFPS()+1);
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && canPause){
            	pause();
            	canPause = false;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_F11)){
            	if(!f11){
            		try {
						Display.setFullscreen(!Display.isFullscreen());
					} catch (LWJGLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	f11 = true;
            }else{
            	f11=false;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_F3)){
            	if(!f3){
            		debugMenu();
            		if(this.canPause)
            		this.pause();
            	}
            	f3 = true;
            }else{
            	f3=false;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_Q) && !menuOpen[1]){
            	helpMenu();
            	if(this.canPause)
            	this.pause();
            }
            if(!this.canPause && Keyboard.isKeyDown(Keyboard.KEY_H)){
            	em.removeAllEntities();
            	CS.stop();
            	stats.ui = null;
            	this.canPause = false;
            	this.mm = new MainMenu(this);
            }
            
            if(stats.ui != null)
            stats.ui.draw();
            
            if(mm != null)
            	mm.draw();
        	//debugTexture(150, 150, twidth, theight);
            
            em.drawAllEntities();
        	FPSCounter.updateFPS();
               	
            Display.update();
            Display.sync(60);
        }
         
        Display.destroy();
        this.stop();
    }
	
	private void debugMenu() {
		JFrame window = openPopUp("Debug", 0);
		if(window != null){
		final JLabel message = new JLabel();
		new Thread(new Runnable(){
			public void run(){
				try {
					Thread.sleep(250);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while(menuOpen[0]){
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				String help = "<html><h1>Stats for Nerds</h1>FPS: "+FPSCounter.getFPS()+"<br>"
						+ "Entities: "+em.getNumberEntities()+"<br>"
						+ "<button onClick='expandList()'>More Details</button></html>";
				message.setText(help);
				}
			}
		}).start();
		window.add(message);
		window.revalidate();
		}
	}

	public void helpMenu(){
		JFrame window = openPopUp("Help", 1);
		if(window != null){
		String help = "<html><style>table{ border: 3px solid black;} td{border: 2px solid black;"
				+ "<h1>Help Menu</h1><p>There are two game modes:</p>"
				+ "<ol><li>Streak Mode:</li><ul><li>5 levels</li>"
				+ " <li>Unlimited Misses</li>"
				+ "<li>Get as many points as you can in the time allowed</li>"
				+ "<li>Points for streaks</li></ul>"
				+ "<li>Survival Mode:</li><ul>"
				+ "<li>Unlimited time, but only 3 misses allowed</li>"
				+ "<li>Unlimited levels, but they get harder to the point where you ARE going to miss 3 times.</li>"
				+ "<li>Points for Streaks</li></ul></ol>"
				+ "<h3>KEYBOARD SHORTCUTS</h3><table>"
				+ "<tr><td>KEY</td><td>FUNCTION</td></tr>"
				+ "<tr><td>Esc</td><td>Pause</td></tr>"
				+ "<tr><td>E</td><td>Resume</td></tr>"
				+ "<tr><td>R</td><td>Reset/Restart</td></tr>"
				+ "<tr><td>F3</td><td>Debug Info</td></tr>"
				+ "<tr><td>F11</td><td>Toggle Fullscreen</td></tr>"
				+ "<tr><td>H</td><td>Home</td></tr>"
				+ "<tr><td>Q</td><td>Help</td></tr></table>"
				+ "* DEBUG SCREEN"
				+ "* 	FPS"
				+ "* 	Entities on screen & coords</html>";
		JLabel message = new JLabel();
		message.setText(help);
		Container contain = window.getContentPane();
		//contain.setSize(window.getWidth(), window.getHeight());
		contain.add(message);
		window.revalidate();
		}
	}
	
	public JFrame openPopUp(String title, final int idx){
		// the int is the index of the boolean array that keeps track of which menus are open
		if(!menuOpen[idx]){
				JFrame mainFrame = new JFrame(title);
				mainFrame.setSize(450,450);
				mainFrame.setLocation(0,0);
				
				WindowListener wl = new WindowListener(){
					
					public void windowClosing(WindowEvent e) {
						// TODO Auto-generated method stub
						menuOpen[idx] = false;
						//System.out.println("Closed");
					}
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
					}
					@Override
					public void windowOpened(WindowEvent e) {
						// TODO Auto-generated method stub
						menuOpen[idx] = true;
						//System.out.println("Open");
					}
					@Override
					public void windowIconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void windowDeiconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void windowActivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void windowDeactivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}					
				};
				
				mainFrame.addWindowListener(wl);
				mainFrame.setVisible(true);
				return mainFrame;
		}else{
			return null;
		}
	}
	public void startGame(String mode){
		final Game ctx = this;
		this.canPause = true;
		this.gameMode = mode;
		mm = null;
        em.spawnIntoScreen(player);
        Text text = new Text(0, 250, "");
        stats.ui = text;
        stats.ui.setText("Points "+stats.score
				+"\nMisses "+stats.strikes);
        stats.setStatChangeListener(new GameStats.StatChangeListener() {
			
			@Override
			public void onStatChange(GameStats.stats name, int value) {
				String statText = "Points "+stats.score;
				if(ctx.gameMode=="survival")statText+="\nMisses "+stats.strikes;
				if(ctx.gameMode=="streak")statText+="\nStreak "+stats.streak;
				stats.ui.setText(statText);
				if(name == GameStats.stats.STRIKES){
					if(ctx.gameMode == "survival" && value == 4){
					gameOver();
					}
					if(ctx.gameMode == "streak"){
						stats.setStat(GameStats.stats.STREAK, 0);
					}
				}
				
				if(name == GameStats.stats.ballsSpawned && value == Math.ceil(10*Math.pow(stats.level, 1.25))){
					stats.addStat(GameStats.stats.LEVEL, 1);
					//stats.ui.setText("");
				}
				if(name == GameStats.stats.LEVEL){
					final Text t = new Text(Display.getWidth()/4, Display.getHeight()/2, "Level "+value);
					System.out.println("Level UP" +value);
					if(gameMode == "survival"){	
						em.spawnIntoScreen(t);
						CS.spawnInterval -= 5;
						CS.misFireChance -= 0.03;
						CS.pause();
						CS.resume();
						new Thread(new Runnable(){
							public void run(){
								try {
									Thread.sleep(1150);
									Start.queue.put(new Runnable(){
										public void run(){
											em.remove(t);
										}
									});
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();
					}
					if(gameMode == "streak"){
							if(value >8){
								//Game Over
								gameOver();
							}
					}
				}
			}
		});
        CS.start();
	}
	
	void gameOver(){
		//System.out.println("Game Over");
		canPause = false;
		CS.pause();
		em.pause();
		em.spawnIntoScreen(new Text(Display.getWidth()/15, Display.getHeight()/2, "Game Over Press R to Restart"));
		new Thread(new Runnable(){
			public void run(){
				while(!Keyboard.isKeyDown(Keyboard.KEY_R) && !Display.isCloseRequested()){
					try {
						Thread.sleep(10);//This is to prevent the CPU from revving up
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//System.out.println("Resume");
				em.removeAllEntities(Circle.class);
				em.removeAllEntities(Text.class);
				stats.reset();
				em.resume();
				CS.resume();
				canPause = true;
			}
		}).start();
	}
	
	private void pause(){
		CS.pause();
		em.pause();
		final Text t = new Text(Display.getWidth()/8, (Display.getHeight()/2)+150, "Game Paused\nPress E to Resume");
		em.spawnIntoScreen(t);
		new Thread(new Runnable(){
			public void run(){
				while(!Keyboard.isKeyDown(Keyboard.KEY_E) && !Display.isCloseRequested()){
					try{
						Thread.sleep(25);
					}catch(Exception e){}
				}
				//System.out.println("Resume");
				em.remove(t);
				em.resume();
				CS.resume();
				canPause = true;
				
			}
		}).start();
	}
}