package pack.boxel.main;
import levels.Hint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GUI {
	BitmapText text;
	public Hint hint;
	Bitmap map;
	int fade, speed, r, g, b;
	boolean fadeOut;
	public boolean fadeIn;
	public GUI() {
		text = new BitmapText();
		hint = new Hint();
	}
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		
		//draw hint
		if (hint.isOpen()){
			paint.setARGB(150, 0, 0, 0);
			canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
			canvas.drawBitmap(hint.getHintBitmap(), (canvas.getWidth()/2)-(hint.getWidth()/2),
					(canvas.getHeight()/2)-(hint.getHeight()/2)-4, null); //centers the hint
		}
		//if pause
		if (MainGamePanel.isPaused()){
			paint.setARGB(150, 0, 0, 0);
			canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
			canvas.drawRect(0,0,canvas.getWidth(),50,paint);
			canvas.drawRect(0,392,canvas.getWidth(),426,paint);
			//canvas.drawRect(122,16,200,50,paint);
			paint.setARGB(255,255,255,255);
			canvas.drawText("Game Paused - FPS: "+Counter.getFPS(), 100, 12, paint);
			canvas.drawBitmap(MainGamePanel.texture.back, 126, 396, null);
			canvas.drawBitmap(MainGamePanel.texture.exit, 126, 20, null);
		}
		else{
			//draw map
			canvas.drawBitmap(map,0,0,null);
			paint.setColor(Color.GREEN);
			canvas.drawRect((int)MainGamePanel.player.getX()/32, 0,
					(int)MainGamePanel.player.getX()/32+2, (int)(MainGamePanel.player.getY()/32)-3,paint);
			canvas.drawRect((int)MainGamePanel.player.getX()/32, (int)(MainGamePanel.player.getY()/32)+1,
					(int)MainGamePanel.player.getX()/32+2, 16,paint);
			//draw timer
			paint.setARGB(255,255,255,255);
			canvas.drawText("Time: "+Counter.getCurrentTime(), 2, 29, paint);
		}
		//render fade overlay
		if (fadeOut){
			if (fade - speed > 0){
				fade-=speed;
				paint.setARGB(fade,r,g,b);
				canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
			}
			else{
				fade = 0;
				fadeOut = false;
			}
		}
		if (fadeIn){
			if (fade + speed < 255){
				fade+=speed;
				paint.setARGB(fade,r,g,b);
				canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
			}
			else{
				fade = 255;
				fadeIn = false;
			}
		}
	}
	public void fadeOut(int i){ 
		if (!fadeOut){
			speed = i;
			fadeOut = true;
			fade = 255; 
		}
		r = g = b = 255;
	}
	public void fadeOut(int i, int r, int g, int b){
		if (!fadeOut){
			speed = i;
			fadeOut = true;
			fade = 255; 
		}
		this.r=r;
		this.g=g;
		this.b=b;
	}
	public void fadeOut(int i, int r, int g, int b, int amount){
		if (!fadeOut){
			speed = i;
			fadeOut = true;
			fade = amount; 
		}
		this.r=r;
		this.g=g;
		this.b=b;
	}
	public void fadeIn(int i){
		if (!fadeIn){
			speed = i;
			fadeIn = true;
			fade = 0;
		}
		r = g = b = 255;
	}
	public void fadeIn(int i, int r, int g, int b){
		if (!fadeIn){
			speed = i;
			fadeIn = true;
			fade = 0;
		}
		this.r=r;
		this.g=g;
		this.b=b;
	}
	public void drawMap(int width){
		int type;
		//String message = "Tap HERE to Select a Level";
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas();
		map = Bitmap.createBitmap(width,46, Bitmap.Config.ARGB_8888);
		canvas.setBitmap(map);
		paint.setARGB(75,0,0,0);
		canvas.drawRect(0, 0, width,16, paint);
		paint.setARGB(50,0,0,0);
		canvas.drawRect(0, 16, width,32, paint);
		//for (int i = 0; i < 8; i++){
		//	paint.setARGB(i*(50/8), 0,0,0);
		//	canvas.drawRect(0, (-i)+39, width, (-i)+40, paint);
		//}
		//paint.setARGB(150,0,0,0);
		//canvas.drawText(message, 85, 27, paint);
		//paint.setARGB(255,255,255,255);
		//canvas.drawText(message, 86, 28, paint);
		for (int row = 0; row < 16; row++){
			for (int col = 0; col < 312; col++){
				type = MainGamePanel.level.map[row][col].getType();
				if (type > 0){
					if (type > 8){ //show spikes
						paint.setARGB(255, 255,0,0);
						canvas.drawRect(col-1,row-2,col+2,row+1, paint);
					}
					else{ //show all else white
						paint.setARGB(100,255,255,255);
						canvas.drawRect(col,row,col+1,row+1, paint);
					}
				}
			}
		}
		paint.setARGB(75,0,0,0);
		canvas.drawRect(width-75, 0, width,32, paint);
		canvas.drawBitmap(MainGamePanel.texture.exit, (width)-72, 3, null);
	}
}