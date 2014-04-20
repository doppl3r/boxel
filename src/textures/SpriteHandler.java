package textures;

import android.graphics.Rect;

public class SpriteHandler {
	private int bitWidth, bitHeight, hFrames, frames;
	private boolean done;
	private double currentFrame, rate;
	private Rect spriteRect, destRect;
	public SpriteHandler(int spriteWidth, int spriteHeight, int hFrames, int vFrames, double rate){
		this.hFrames=hFrames;
		this.rate=rate;
		bitWidth=spriteWidth/hFrames;
		bitHeight=spriteHeight/vFrames;
		frames = (int)(hFrames*vFrames); //good time to start
		spriteRect = new Rect(0,0,bitWidth,bitHeight);
		destRect = new Rect();
	}
	public void update(double x, double y, int xSize, int ySize){
		if (currentFrame+rate < frames) currentFrame+=rate;
		else{
			done = true;
			currentFrame = 0;
		}
		//adjust sprite location
		spriteRect.top = (((int)currentFrame)/hFrames)*bitHeight;
		spriteRect.bottom = spriteRect.top + bitHeight;
		spriteRect.left = (((int)currentFrame)%hFrames)*bitWidth;
		spriteRect.right = spriteRect.left + bitWidth;
		//texture placement
		destRect.top = (int)y;
		destRect.bottom = destRect.top + xSize;
		destRect.left = (int)x;
		destRect.right = destRect.left + ySize;
	}
	public Rect getDestination(){ return destRect; }
	public Rect getSprite(){ return spriteRect; }
	public void reset(){currentFrame=0;}
	public boolean isDone(){ return done; }
}