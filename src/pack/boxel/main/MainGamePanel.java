package pack.boxel.main;

import levels.LevelHandler;
import menu.Menu;
import textures.Textures;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import audio.AudioHandler;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

	static boolean pause;
	public static boolean exit;
	public static MainThread thread;
	public static Context context; //MIGHT NEED TO BE REMOVED
	public static Textures texture;
	public static Player player;
	public static LevelHandler level;
	public static Background bg;
	public static GUI gui;
	public static Menu menu;
	public static AudioHandler audio;
	

	public MainGamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		//initiate objects
		audio = new AudioHandler(context);
		audio.playMusic(R.raw.theme1); //play music
		texture = new Textures(context);
		menu = new Menu(context);
		level = new LevelHandler(context);
		bg = new Background();
		player = new Player();
		gui = new GUI();	
		
		//start game
		thread = new MainThread(getHolder(), this);
		setFocusable(true);
	}
	public void draw(Canvas canvas) {
		//things will be drawn here		
		if (menu.isOpen()){
			bg.draw(canvas);
			menu.draw(canvas);
		}
		else {
			bg.draw(canvas);
			level.draw(canvas);
			player.draw(canvas);
			gui.draw(canvas);
		}
	}
	public void update(){
		if (!pause){
			if (menu.isOpen()){
				bg.check();
			}
			else {
				if (!gui.hint.isOpen()){
					bg.check();
					player.check();
				}
			}
		}
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {}
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {thread.join();	retry = false;
			} catch (InterruptedException e) {}
		}
	}
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (menu.isOpen()) menu.select((int)event.getX(), (int)event.getY());
			
			else{
				if (event.getY() > 128){
					if (pause){
						pause = false;
						gui.fadeOut(15,0,0,0,150);
						MainGamePanel.audio.toggleMusic();
						if (!gui.hint.isOpen()) Counter.resume();
					}
					else{
						if (gui.hint.isOpen()){
							Counter.resume();
							gui.hint.setOpen(false);
							gui.fadeOut(15,0,0,0,150);
						}
						else{
							if (!player.isAlive()) player.particles.adjustAll(0,600);
							else player.jump();
						}
					}
				}
				else{
					if (!pause){
						pause = true;
						MainGamePanel.audio.toggleMusic();
						if (!gui.hint.isOpen()) Counter.pause();
					}
					else{
						MainGamePanel.audio.toggleMusic();
						player.setY(0);
						Counter.stopTimer();
						Counter.resume();
						gui.hint.setOpen(false);
						menu.setOpen(true);
						menu.fade = 255;
						pause = false;
					}
				}
			}
			try {MainThread.sleep(25);} 
			catch (InterruptedException e) {e.printStackTrace();}
		} 
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// constantly updated	
			try {MainThread.sleep(100);} 
			catch (InterruptedException e) {e.printStackTrace();}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {}
		return true;
	}
	public static void launchLevel(Context context, int i){
		player.respawn();
		MainGamePanel.audio.playSound(1);
		level.loadLevel(i);
		MainGamePanel.gui.drawMap(menu.width);
		menu.setOpen(false);
		menu.selected = false;
		MainGamePanel.gui.fadeOut(15);
	}
	public void setPause(boolean pause){ MainGamePanel.pause=pause; }
	public static boolean isPaused(){ return pause; }
}
