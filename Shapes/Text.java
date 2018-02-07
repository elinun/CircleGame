package Shapes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Display;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLUtessellator;

public class Text extends Shape {
	public String text = "Hellow World";
	int width = 250;
	int height = 250;
	public final int hw = 24;//height and width of letters
	public int cx = 0;
	int px = 0;
	int py = 0;
	public int cy = 0;
	int bpp = 4;
	public static String dir;
	private int[] ids;
	

	
	public void setText(String text){
		this.text = text;
		for(int id: ids){
			GL11.glDeleteTextures(id);
		}
		init();
		
	}
	public void setPosition(int x, int y){
		this.cx = x;
		this.cy = y;
		this.px = x;
		this.py = y;
	}
	public void draw(){
		for(int i=0;i<this.ids.length;i++){
			if(ids[i] == -1){//new line
				cy-=hw;
				cx-=(i*hw)+hw;
				//System.out.println(x+","+y);
			}else{
			this.debugTexture(ids[i], cx+(i*hw), cy, hw, hw);
			}
			//System.out.println(ids[i]);
		}
		cy = py;
		cx = px;
		super.draw();		
	}
	
	public Text(int x, int y, String text){
		this.text = text;
		this.cx = x;
		this.cy = y;
		this.px = x;
		this.py = y;
		init();
	}
	
	private void init(){
		this.ids = new int[this.text.length()];
		char[] chars = this.text.toUpperCase().toCharArray();
		for(int i = 0; i<this.text.length();i++){
			if(chars[i] != ' ' && chars[i] != '\n'){
			ids[i] = this.generateTexture(chars[i]);
			}else{
				ids[i] = 0;
				if(chars[i] == '\n'){
					ids[i] = -1;
				}
			}
		}
	}
	
	private int generateTexture(char letter){
		
		//put image into byte array
		String sletter = new StringBuilder().append(letter).toString();
		switch(letter){
		case '?':
			sletter = "qm";
		}
		BufferedImage bi;
		try {
			//System.out.println(dir+"\\letters\\"+sletter+".png");
			bi = ImageIO.read(new File(dir+"\\letters\\"+sletter+".png"));	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.toString()+dir+"\\letters\\"+sletter+".png");
			bi = null;
			System.exit(hw);
		}
		byte[] data = ((DataBufferByte) bi.getData().getDataBuffer()).getData();
		//generate texture
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	        int id = GL11.glGenTextures();
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	        //GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
	        ByteBuffer buf = BufferUtils.createByteBuffer(bpp * hw * hw);
	        buf.put(data);
	        buf.flip();//we have to do this when we are done editing it and when people need to start using it.
	        GL11.glTexParameter(GL11.GL_TEXTURE_2D, 1, buf.asIntBuffer());
	      //Setup filtering, i.e. how OpenGL will interpolate the pixels when scaling up or down
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

	        //Setup wrap mode, i.e. how OpenGL will handle pixels outside of the expected range
	        //Note: GL_CLAMP_TO_EDGE is part of GL12
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
	        
	        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, hw, hw, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
	       
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        return id;
	}
	
	private void debugTexture(int id, float x, float y, float mwidth, float mheight) {
		//bind the texture before rendering it, although it should already be bound.
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

		//setup our texture coordinates
		//(u,v) is another common way of writing (s,t)
		float u = 0f;
		float v = 0f;
		float u2 = 1f;
		float v2 = 1f;

		//immediate mode is deprecated -- we are only using it for quick debugging
		GL11.glColor4f(1f, 1f, 1f, 0f);
		GL11.glBegin(GL11.GL_QUADS);
		
		
		GL11.glTexCoord2f(u, v2);
		GL11.glVertex2f(x, y);
		
		GL11.glTexCoord2f(u2, v2);
		GL11.glVertex2f(x + mwidth, y);
		
		GL11.glTexCoord2f(u2, v);
		GL11.glVertex2f(x + mwidth, y +mheight);	
		
		GL11.glTexCoord2f(u, v);
		GL11.glVertex2f(x, y + mheight);
		GL11.glEnd();
	
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

}
