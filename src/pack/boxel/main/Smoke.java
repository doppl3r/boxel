package pack.boxel.main;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Smoke {
	LinkedList<Integer> y,aX;
	LinkedList<Double> x;
	int width, height;
	private Rect spriteRect, destRect;
	
	
	public Smoke(){
		width = 400;
		height = 540;
		
		y = new LinkedList<Integer>();
		aX = new LinkedList<Integer>();
		x = new LinkedList<Double>();
		
		spriteRect = new Rect(0, 0, 64, 16);
		destRect = new Rect();
		build();
	}
	public void draw(Canvas canvas){
		int size = size();
		
		for (int i = 0; i < size; i++){
			update(canvas, i);
			canvas.drawBitmap(MainGamePanel.texture.smoke, spriteRect, destRect, null);
		}
	}
	public void update(Canvas canvas, int i){
		if (width != canvas.getWidth()) width = canvas.getWidth();
		if (height != canvas.getHeight()) height = canvas.getHeight();
		
		spriteRect.top = 0;
		spriteRect.bottom = 16;
		spriteRect.left = aX.get(i);
		spriteRect.right = aX.get(i) + 16;
		
		destRect.top = y.get(i)-(int)(MainGamePanel.player.getY()/2);
		destRect.bottom = y.get(i)+64-(int)(MainGamePanel.player.getY()/2);
		destRect.left = (int)(x.get(i)*1);
		destRect.right = (int)(x.get(i)+64);
	}
	public void check(int i){
		x.set(i, x.get(i)-0.7);
		if (x.get(i)<-128){
			x.set(i, width*1.0+128);
			y.set(i, (int)(Math.random()*240));
			aX.set(i, (int)(Math.random()*4)*16);
		}
	}
	public void build(){
		for (int i = 0; i < 12; i++){
			add();
		}
	}
	public void add(){
		x.add(Math.random()*width);
		y.add((int)(Math.random()*height)/2+50);
		aX.add((int)(Math.random()*4)*16);
	}
	public void remove(int i){
		x.remove(i);
		y.remove(i);
		aX.remove(i);
	}
	public void removeAll(){
		int size = x.size();
		for (int i = 0; i < size; i++){
			x.remove(0);
			y.remove(0);
			aX.remove(0);
		}
	}
	public void reset(){
		removeAll();
		build();
	}
	public int size(){return x.size();}
}
