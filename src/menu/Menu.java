package menu;

import levels.Hint;
import pack.boxel.main.BitmapText;
import pack.boxel.main.MainGamePanel;
import physics.JumpAccelerator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Menu {
	private boolean open, next, completed;
	public boolean selected;
	public boolean intro;
	private int selectX, selectY, level;
	public int fade, nextY;
	private int blink;
	public int width;
	private int r,g,b;
	private Context context;
	private BitmapText text;
	private Hint hint;
	JumpAccelerator jump;
	
	public Menu(Context context) {
		open = true; 
		intro = true;
		fade(255);
		hint = new Hint();
		this.context=context;
		text = new BitmapText();
		jump = new JumpAccelerator(-16,0.8);
		jump.reset();
	}
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		if (width != canvas.getWidth()) width = canvas.getWidth();
		if (intro){
			canvas.drawBitmap(MainGamePanel.texture.logo, 
					(width/2)-(MainGamePanel.texture.logo.getWidth()/2), 130, null);
		}
		else{
			paint.setARGB(150,255,0,0);
			//draw progress
			for (int row = 0; row < 10; row++){
				for (int col = 0; col < 3; col++){
					if ((row)+(col)*10 > MainGamePanel.level.getHighestLevel()){
						paint.setARGB(200,255,255,255);
						canvas.drawRect(col*64+82, row*48+12, col*64+82+32, row*48+22+22, paint);
					}
					else if ((row+1)+(col)*10 > MainGamePanel.level.getHighestLevel()){
						if (blink < 200) blink+=5;
						else blink = 0;
						paint.setARGB(blink,255,255,255);
						canvas.drawRect(col*64+82, row*48+12, col*64+82+32, row*48+22+22, paint);
					}
				}
			}
			//draw selection box
			if (selected){
				paint.setARGB(200,250,255,120);
				canvas.drawRect(selectX+2, selectY+2, selectX+34, selectY+34, paint);
			}
			//draw level selection
			canvas.drawBitmap(MainGamePanel.texture.levelSelect, 80, 10, null);
			if (selected || next){
				paint.setARGB(175,0,0,0);
				canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(), paint);
				if (!completed){
					paint.setARGB(100,0,0,0);
					canvas.drawRect(120,290,202,428,paint);
					if (selected || !MainGamePanel.level.isNewRecord()){
						text.draw("#1 Time:", 130, 340, 0, canvas);
						text.draw(MainGamePanel.level.timeHolder[level], 140, 364, 0, canvas);
					}
					if (!next){
						canvas.drawBitmap(MainGamePanel.texture.back, 126, 296, null);
						canvas.drawBitmap(MainGamePanel.texture.play, 126, 396, null);
					}
					else{
						if (MainGamePanel.level.isNewRecord()){
							text.draw("NEW", 148, 340, 0, canvas);
							text.draw("Record!", 134, 352, 0, canvas);
							text.draw(MainGamePanel.level.timeHolder[level], 140, 364, 0, canvas);
						}
						if (nextY + jump.y < 396){
							jump.accelerate();
							nextY+=jump.y;
						}
						else{
							jump.reset();
							nextY = 396;
						}
						canvas.drawBitmap(MainGamePanel.texture.back, 126, nextY-100, null);
						canvas.drawBitmap(MainGamePanel.texture.next, 126, nextY, null);
						
						canvas.drawRect(0,246, canvas.getWidth(), 262, paint);
						text.draw("Level Complete!", 102, 250, 0, canvas);
					}
				}
			}
		}
		if (completed){
			canvas.drawBitmap(hint.getHintBitmap(), (canvas.getWidth()/2)-(hint.getWidth()/2), 
					(canvas.getHeight()/2)-(hint.getHeight()/2), null);
		}
		//fader
		if (fade > 0){
			fade-=15;
			paint.setARGB(fade,r,g,b);
			canvas.drawRect(0,0,canvas.getWidth(), canvas.getHeight(), paint);
		}
	}
	public void fade(int i){
		fade = i;
		r = g = b = 0;
	}
	public void fade(int i, int r, int g, int b){
		fade = i;
		this.r=r;
		this.g=g;
		this.b=b;
	}
	public boolean isOpen(){ return open; }
	public void select(int x, int y){
		//check selection
		if (!intro){
			if (!selected){
				//select level
				if (!next){
					if (x >= 80 && x < 80+176 &&
							y >= 10 && y < 10+468){
						selectX = ((((x-8)/3)*3)/64)*64+16; 
						selectY = ((((y-8)/10)*10)/48)*48+10;
						level = ((selectY-10)/48)+((selectX-80)/64)*10;
						MainGamePanel.level.currentLevel = level;
						if (level < MainGamePanel.level.getHighestLevel()+1){
							selected = true;
							if (MainGamePanel.level.getTheme()!=level/10){
								fade(255);
								MainGamePanel.level.setTheme(level/10);
								MainGamePanel.bg.rerender();
							}
						}
					}
				}
				else{ //the menu with "next" will display
					if (completed){
						next = completed = selected = false;
					}
					else{
						if (y > 372 && y <= 446 &&
								x > 120 && x <= 210){
							if (MainGamePanel.level.setNextLevel()){
								MainGamePanel.level.setTheme(MainGamePanel.level.currentLevel/10);
								MainGamePanel.launchLevel(context, MainGamePanel.level.currentLevel);
								MainGamePanel.bg.rerender();
							}
							else fade(165);
							next = false;
							completed = false;
						}
						else {
							next = false;
							completed = false;
							fade(165);
						}
					}
				}
			}
			else{
				if (y > 372 && y <= 446 &&
						x > 120 && x <= 210){
					MainGamePanel.launchLevel(context, level);
					next = false;
					completed = false;
				}
				else{
					selected = false;
					fade(165);
				}
			}
		}
		else{
			fade(255);
			intro = false;
			MainGamePanel.audio.playSound(1);
		}
	}
	public void setOpen(boolean open){this.open=open;}
	public void setNext(boolean next){ 
		this.next=next; 
		nextY = 0;
	}
	public void complete(){
		completed = next = true;
		hint.setHint("Congratulations! You completed Boxel! Thanks for playing" +
				" and I hope you had lots of fun. Be sure to beat your best times!");
	}
	public void setLevel(int level){ this.level = level; }
}