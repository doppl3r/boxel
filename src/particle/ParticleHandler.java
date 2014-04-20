package particle;
import java.util.LinkedList;

import pack.boxel.main.MainGamePanel;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ParticleHandler {
	LinkedList<Particle> particle;
	private int type;
	
	public ParticleHandler(){
		particle = new LinkedList<Particle>();
	}
	public void addParticles(double x1, double y1, double momentum, int type){
		removeAll();
		this.type=type;
		if (particle.size()<=0){ //only spawn x amount
			for (int i = 0; i < 50; i++){
				add(x1, y1, momentum);
				particle.get(i).jump.jump();
			}
		}
	}
	public void draw(Canvas canvas){
		Paint paint = new Paint();
		for (int i = 0; i < particle.size(); i++){
			if (type == 1) paint.setARGB(255, 0,148,255);
			else paint.setARGB(255, 0, 0, 0);
			canvas.drawRect((int)(particle.get(i).x+MainGamePanel.player.deathWall),
				(int)(particle.get(i).y),
				(int)(particle.get(i).x+MainGamePanel.player.deathWall)+6,
				(int)(particle.get(i).y)+6, paint);
		}
	}
	public void update(){
		for (int i = 0; i < particle.size(); i++){
			if (particle.get(i).y < 600){
				particle.get(i).update();
			}
			else remove(i);
		}
	}
	public void adjustAll(double x, double y){
		for (int i = 0; i < particle.size(); i++){
			particle.get(i).setX(x);
			particle.get(i).setY(y);
			particle.get(i).stop();
		}
	}
	public void add(double x1, double y1, double momentum){
		particle.add(new Particle(64, (int)y1, momentum));
	}
	public void remove(int i){
		particle.remove(i);
	}
	public void removeAll(){
		int size = particle.size();
		for (int i = 0; i < size; i++) remove(0);
	}
	public boolean isEmpty(){
		if (particle.size() <= 0) return true;
		else return false;
	}
	public int getType(){ return type; }
}
