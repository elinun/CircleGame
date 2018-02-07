
public class FPSCounter {
	private static int FPS = 0;
	private static int prevFPS = 0;
	private static long lastFPS = 0;
	
	public static void StartTracking(){
		lastFPS = getTime();
	}
	public static void updateFPS(){
		if (getTime() - lastFPS > 1000) { 
			prevFPS = FPS;
	        FPS = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
		FPS++;
	}
	public static int getFPS(){
		return prevFPS;
	}
	static long getTime() {
	    return System.nanoTime() / 1000000;
	}
}
