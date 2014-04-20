package pack.boxel.main;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Stars {
	LinkedList<Integer> y,aX;
	LinkedList<Double> aTime, x;
	int width, height;
	private Rect spriteRect, destRect;
	
	
	public Stars(){
		width = 312;
		height = 540;
		
		y = new LinkedList<Integer>();
		aX = new LinkedList<Integer>();
		aTime = new LinkedList<Double>();
		x = new LinkedList<Double>();
		
		spriteRect = new Rect(0, 0, 32, 8);
		destRect = new Rect();
		build();
	}
	public void draw(Canvas canvas){
		int size = size();
		
		for (int i = 0; i < size; i++){
			update(canvas, i);
			canvas.drawBitmap(MainGamePanel.texture.star, spriteRect, destRect, null);
		}
	}
	public void update(Canvas canvas, int i){
		if (width != canvas.getWidth()) width = canvas.getWidth();
		if (height != canvas.getHeight()) height = canvas.getHeight();
		
		spriteRect.top = 0;
		spriteRect.bottom = 8;
		spriteRect.left = aX.get(i);
		spriteRect.right = aX.get(i) + 8;
		
		destRect.top = y.get(i)-(int)(MainGamePanel.player.getY()/2);
		destRect.bottom = y.get(i)+8-(int)(MainGamePanel.player.getY()/2);
		destRect.left = (int)(x.get(i)*1);
		destRect.right = (int)(x.get(i)+8);
		
		animate(i);
	}
	public void check(int i){
		x.set(i, x.get(i)-0.3);
		if (x.get(i)<-16){
			x.set(i, width*1.0);
			y.set(i, (int)(Math.random()*height));
		}
	}
	public void animate(int i){
		if (aTime.get(i) > 0){
			aTime.set(i, aTime.get(i)-0.2);
			if (aTime.get(i) > 15) aX.set(i, 8);
			else if (aTime.get(i) > 10 && aTime.get(i) <= 15) aX.set(i, 16);
			else if (aTime.get(i) > 05 && aTime.get(i) <= 10) aX.set(i, 24);
			else aX.set(i, 0);
		}
		else aTime.set(i, 20.0);
	}
	public void build(){
		for (int i = 0; i < 15; i++){
			add();
		}
	}
	public void add(){
		x.add(Math.random()*width);
		y.add((int)(Math.random()*height));
		aTime.add((Math.random()*20));
		aX.add(0);
	}
	public void remove(int i){
		x.remove(i);
		y.remove(i);
		aTime.remove(i);
		aX.remove(i);
	}
	public void removeAll(){
		int size = x.size();
		for (int i = 0; i < size; i++){
			x.remove(0);
			y.remove(0);
			aTime.remove(0);
			aX.remove(0);
		}
	}
	public void reset(){
		removeAll();
		build();
	}
	public int size(){return x.size();}
}
