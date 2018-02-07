package Shapes;
import org.lwjgl.opengl.GL11;

public class Rectangle extends Shape{
	public final float z = 0.0f;
	//public int[] center = {400, 100}; //x, y
	public int cx = 400;
	public int cy = 100;
	public int[] sideLengths = {150, 75};//x, y
	public void draw(){
        // render OpenGL here
     	//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
         
     	// set the color of the quad (R,G,B,A)
     	GL11.glColor3f(0.5f,0.5f,1.0f);
     	     
     	// draw quad
     	GL11.glBegin(GL11.GL_QUADS);
     	    GL11.glVertex3f(cx-(sideLengths[0]/2), cy-(sideLengths[1]/2), z);
     	    GL11.glVertex3f(cx+(sideLengths[0]/2), cy-(sideLengths[1]/2), z);
     	    GL11.glVertex3f(cx+(sideLengths[0]/2), cy+(sideLengths[1]/2), z);
     	    GL11.glVertex3f(cx-(sideLengths[0]/2), cy+(sideLengths[1]/2), z);
     	GL11.glEnd();
	}
	
	public boolean isRectangle(){
		return true;
	}
}
