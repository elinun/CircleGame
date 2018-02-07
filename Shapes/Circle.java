package Shapes;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Circle extends Shape{
	public final float z = 0.0f;
	//public int[] center = {400, 450}; //x, y
	public int cx = 400;
	public int cy = 450;
	public int radius = 25;//x, y
	public float[] color = {0.5f, 0.5f, 1.0f};
	public void draw(){
        // render OpenGL here
     	//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);  
         
     	// set the color of the quad (R,G,B,A)
     	GL11.glColor3f(color[0], color[1], color[2]);


     	
     	float DEG2RAD = (float)3.14159/180;
     	GL11.glBegin(GL11.GL_LINE_LOOP);
     	 for (int i=0; i <= 360; i++)
     	   {
     		 float degInRad = i*DEG2RAD;
     	      GL11.glVertex3d((Math.cos(degInRad)*radius)+ cx,(Math.sin(degInRad)*radius)+cy, z);
     	   }
     	 
     	   GL11.glEnd();
	}
}
