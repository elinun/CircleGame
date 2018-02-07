package Shapes;

import org.lwjgl.input.Mouse;

import javafx.scene.input.MouseButton;

public abstract class Shape {
	public float speedX = 0.0f;
	public float speedY = 0.0f;
	public int cx;//center x
	public int cy;//center y
	public onHoverListener hover = null;
	public onClickListener click = null;
	public void draw(){
		//TODO take care of listeners
		if(this.isText()){
			int x = Mouse.getX();
			int y = Mouse.getY();
			Text that = (Text)this;
			//System.out.println("Checking");
			if(x>that.cx&&x<(that.cx+(that.hw*that.text.length())*2)&&y>that.cy&&y<(that.cy+that.hw)){
				if(hover != null){
					this.hover.onHover(x, y);
				}
				if(Mouse.isButtonDown(0) && click != null){
					this.click.onClick(x, y);
				}
			}
		}
	}
	public boolean isRectangle(){
		if(this instanceof Rectangle){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean isText(){
		if(this instanceof Text){
			return true;
		}
		else{
			return false;
		}
	}
	public static interface onHoverListener{
		public void onHover(int x, int y);
	}
	public static interface onClickListener{
		public void onClick(int x, int y);
	}
}
