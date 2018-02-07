import org.lwjgl.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/*This is my first real "game". I am just starting to learn.
This is the run config start.
*/

/*
 * lwjgl.jar
 * lwjgl-util.jar
 * set library path with -D option in vm arguments in run config.
 */
public class Start
{
	//Gonna Make Main menu with Streak mode and survival mode.
	/*
	 * Streak Mode
	 * 	5 levels
	 * 	Unlimited Misses
	 * 	Timed, as many points as you can get in given time
	 * 	Points for streaks
	 * Survival Mode
	 * 	Unlimited time, but only 3 misses allowed
	 * 	Unlimited levels, but they get harder to the point where you ARE going to miss 4 times.
	 * 
	 * KEYBOARD SHORTCUTS
	 * 	KEY - FUNCTION
	 * 	Esc - Pause
	 * 	E	- Resume
	 * 	R	- Reset/Restart+
	 * 	F3	- Debug Info
	 * 	F11	- Toggle Fullscreen
	 * 	H	- Home+
	 * 	Q	- Help
	 * 
	 * 
	 * DEBUG SCREEN
	 * 	FPS
	 * 	Entities on screen & coords
	 * *=Must be paused to access this function.
	 * += Auto Pause game, then Confirmation Required
	 */
	static Game g;
	static String dir;
	public static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    public static void main(String[] args)
    {
    	if(args.length>0)Shapes.Text.dir=args[0].substring(0, args[0].length());
    	g = new Game();
    	g.start();
    }

}
