package pack.boxel.main;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Background {
	private boolean rendered;
	private double bg1_x, bg2_x, bg3_x,
		bg1_speed, bg2_speed, bg3_speed;
	private int	bg1_size, bg2_size, bg3_size, repeat, bgM;
	private Bitmap bg1,bg2,bg3;
	private Rect rect1, rect2;
	Stars stars;
	Clouds clouds;
	Smoke smoke;
	
	public Background() {
		//construct the map data
		build();
		repeat = 4;
	}
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		
		if (MainGamePanel.level.getTheme() == 0){
			canvas.drawColor(Color.rgb(80, 74, 98));
			paint.setARGB(255,179,171,201);
		}
		else if (MainGamePanel.level.getTheme() == 1){
			canvas.drawColor(Color.rgb(175, 226, 234));
			paint.setARGB(255,105,239,154);
		}
		else{
			canvas.drawColor(Color.rgb(0,70, 84));
			paint.setARGB(255,0,179,219);
		}
		//render here so that it can run easier
		if (!rendered){			
			Canvas singleUseCanvas1 = new Canvas();
			Canvas singleUseCanvas2 = new Canvas();
			Canvas singleUseCanvas3 = new Canvas();
			
			//render bg1
			bg1 = Bitmap.createBitmap((bgM*bg1_size)*repeat,
					(MainGamePanel.texture.bg1.getHeight()*bgM), Bitmap.Config.ARGB_8888);
			singleUseCanvas1.setBitmap(bg1);
			for (int i = 0; i < repeat; i++){
				rect2.left = (i*bg1_size*bgM);
				rect2.right = (i*bg1_size*bgM)+(bg1_size*bgM);
				if (MainGamePanel.level.getTheme() == 0)	singleUseCanvas1.drawBitmap(MainGamePanel.texture.bg1, rect1,rect2, null);
				else if (MainGamePanel.level.getTheme() == 1)	singleUseCanvas1.drawBitmap(MainGamePanel.texture.bg4, rect1,rect2, null);
				else singleUseCanvas1.drawBitmap(MainGamePanel.texture.bg7, rect1,rect2, null);
			}
			//render bg2
			bg2 = Bitmap.createBitmap((bgM*bg2_size)*repeat,
					(MainGamePanel.texture.bg2.getHeight()*bgM), Bitmap.Config.ARGB_8888);
			singleUseCanvas2.setBitmap(bg2);
			for (int i = 0; i < repeat; i++){
				rect2.left = (i*bg2_size*bgM);
				rect2.right = (i*bg2_size*bgM)+(bg2_size*bgM);
				if (MainGamePanel.level.getTheme() == 0) singleUseCanvas2.drawBitmap(MainGamePanel.texture.bg2, rect1,rect2, null);
				else if (MainGamePanel.level.getTheme() == 1) singleUseCanvas2.drawBitmap(MainGamePanel.texture.bg5, rect1,rect2, null);
				else singleUseCanvas2.drawBitmap(MainGamePanel.texture.bg8, rect1,rect2, null);
			}
			//render bg3
			bg3 = Bitmap.createBitmap((bgM*bg3_size)*repeat,
					(MainGamePanel.texture.bg3.getHeight()*bgM), Bitmap.Config.ARGB_8888);
			singleUseCanvas3.setBitmap(bg3);
			for (int i = 0; i < repeat; i++){
				rect2.left = (i*bg3_size*bgM);
				rect2.right = (i*bg3_size*bgM)+(bg3_size*bgM);
				if (MainGamePanel.level.getTheme() == 0) singleUseCanvas3.drawBitmap(MainGamePanel.texture.bg3,rect1,rect2, null);
				else if (MainGamePanel.level.getTheme() == 1) singleUseCanvas3.drawBitmap(MainGamePanel.texture.bg6,rect1,rect2, null);
				else singleUseCanvas3.drawBitmap(MainGamePanel.texture.bg9,rect1,rect2, null);
			}
			rendered = true;
		}
		else{ 
			//draw features
			if (MainGamePanel.level.getTheme() == 0) stars.draw(canvas);
			else if (MainGamePanel.level.getTheme() == 1) clouds.draw(canvas);
			else smoke.draw(canvas);
			//draw the prerendered background :D
			canvas.drawBitmap(bg1,(int)(bg1_x+MainGamePanel.player.sprint.y),
					434+(int)(-MainGamePanel.player.getY()/2),null);
			canvas.drawBitmap(bg2,(int)(bg2_x+MainGamePanel.player.sprint.y),
					434+(int)(-MainGamePanel.player.getY()/2),null);
			canvas.drawBitmap(bg3,(int)(bg3_x+MainGamePanel.player.sprint.y),
					434+(int)(-MainGamePanel.player.getY()/2),null);
			canvas.drawRect(0,(int)(-MainGamePanel.player.getY()/2+498),
					canvas.getWidth(), canvas.getHeight(), paint);
		}
	}
	public void check(){
		if (MainGamePanel.player.isAlive() && MainGamePanel.player.isSprinting() || MainGamePanel.menu.isOpen()){
			//move layer 1
			bg1_x-=bg1_speed;
			if (bg1_x < -(bg1_size*bgM)) bg1_x=0;
			//move layer 2
			bg2_x-=bg2_speed;
			if (bg2_x < -(bg2_size*bgM)) bg2_x=0;
			//move layer 3
			bg3_x-=bg3_speed;
			if (bg3_x < -(bg3_size*bgM)) bg3_x=0;
			//check effects
			for (int i = 0; i < stars.size(); i++) stars.check(i);
			for (int i = 0; i < clouds.size(); i++) clouds.check(i);
			for (int i = 0; i < smoke.size(); i++) smoke.check(i);
		}
	}
	public void build(){
		//set speeds
		bg1_speed = 0.5;
		bg2_speed = 1.0;
		bg3_speed = 1.5;
		//set image size
		bgM = 4;
		bg1_size = MainGamePanel.texture.bg1.getWidth();
		bg2_size = MainGamePanel.texture.bg2.getWidth();
		bg3_size = MainGamePanel.texture.bg3.getWidth();
		rect1 = new Rect(0,0,32,16);
		rect2 = new Rect(0,0,bg1_size*bgM,MainGamePanel.texture.bg1.getHeight()*bgM);
		//construct features
		stars = new Stars();
		clouds = new Clouds();
		smoke = new Smoke();
	}
	public void rerender(){
		rendered = false;
	}
}
