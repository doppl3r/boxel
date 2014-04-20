package pack.boxel.main;
import particle.ParticleHandler;
import physics.JumpAccelerator;
import physics.SprintAccelerator;
import textures.SpriteHandler;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {
	private double x, y;
	public double deathWall;
	private boolean jumping, sprinting, alive, doubleJump, finished;
	JumpAccelerator jump;
	SprintAccelerator sprint;
	ParticleHandler particles;
	SpriteHandler sprite;
	
	public Player(){
		x=62;
		y=0;
		sprite = new SpriteHandler(MainGamePanel.texture.player.getWidth(), 
				MainGamePanel.texture.player.getHeight(), 4, 1, 0.5);
		alive = true;
		jump = new JumpAccelerator(-16,0.5);
		sprint = new SprintAccelerator(6.0, 0.1, 4.5);
		particles = new ParticleHandler();
	}
	public void draw(Canvas canvas){
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		if (particles.isEmpty() || MainGamePanel.gui.hint.isOpen() || particles.getType()==1){
			canvas.drawBitmap(MainGamePanel.texture.player, sprite.getSprite(), sprite.getDestination(), null);
		}
		particles.draw(canvas);
		if (!MainGamePanel.isPaused()) particles.update();
	}
	public void check(){
		if (alive){
			//move right
			if (sprinting && particles.isEmpty() || particles.getType()==1){
				if (!finished) sprint.accelerate();
				else sprint.decelerate();
				x+=sprint.y;
			}
			//check for tiles
			if (y+jump.y >= 0 && y <= (MainGamePanel.level.getRows())*32){
				if (x+sprint.y >= 0 && x+sprint.y <= (MainGamePanel.level.getCols())*32){
					checkSpecialBlock();
					checkGravity();
					checkCeiling();
					checkWalls();
				}
				else sprint.stop();
			}
			else{
				if (y >= 600){ //reset to start
					alive = false;
					//particles.addParticles(0,599,1, 0);
					MainGamePanel.gui.fadeOut(15);
					MainGamePanel.audio.playSound(0);
					//respawn();
				}
				else{ 
					jumping = true;
					jump.accelerate(); //keep falling faster if the tiles no longer exist
					y+=jump.y;
				}
			}
			animate();
		}
		else{
			if (particles.isEmpty()) respawn();
		}
	}
	public void animate(){
		//animate according to the screen
		if (jumping) sprite.update(62+(int)deathWall, (int)(y-32), 32, 32);
		else resetAnimation();
	}
	public void resetAnimation(){
		sprite.reset();
		sprite.update(62+(int)deathWall, (int)(y-32), 32, 32);
	}
	public void checkGravity(){
		//check distance between player and platform
		jump.accelerate();
		double y1 = y;
		double y2 = y+jump.y;
		boolean fall = true;
		for (double i = y1; i < y2; i+=32){ //scan for tiles in between jumps. This corrects the fast falling issue
			if (getType(x+4,i+jump.y)!=0 || getType(x+28,i+jump.y)!=0){
				land(i);
				fall = false;
				break;
			}
		}
		if (fall){
			y+=jump.y;
			jumping = true;
		}
	}
	public void land(double i){
		y=((int)(i+jump.y)/32)*32;
		jumping = false;
		doubleJump = true;
		jump.reset();
		resetAnimation();
	}
	public void checkWalls(){
		if (y+jump.y-28>0){
			if (getType(x+sprint.y+28, y-28) != 0 || getType(x+sprint.y+28, y-2) != 0) {
				if (getType(x+4,y+jump.y)==0 || getType(x+28,y+jump.y)==0){ //if falling
					x=(((int)(x+sprint.y-4)/32)*32);
					sprint.stop();
				}	
				else x=(((int)(x+sprint.y-4)/32)*32)+4;
				sprint.stop();
				sprinting = false;
			} 
			else sprinting = true;
			//check for death wall
			if (getType(x+sprint.y+32, y-2) != 0) deathWall(true); 
			else deathWall(false);
		}
		else{ //ceiling fix
			if (getType(x+sprint.y+28, y-2) == 0 && getType(x+sprint.y+28, y-28) == 0){}
			else{
				if (getType(x+sprint.y+28, y) != 0){
					x=(((int)(x+sprint.y-4)/32)*32);
					sprint.stop();
				}
			}
		}
	}
	public void checkCeiling(){
		if (y+jump.y-32>0 && jump.x < 0){
			if (getType(x+4, y+jump.y-32) != 0 || getType(x+28, y+jump.y-32) != 0) { //the 8 represents the sprite's actual size from the ground
				y=((int)(y+jump.y+32)/32)*32;
				jump.reset();
			}
		}
	}
	public void checkSpecialBlock(){
		if (y+jump.y-32 > 0){
			if (getType(x+4,y+jump.y)==4){ //finish line
				MainGamePanel.gui.fadeIn(15);
				if (!finished) MainGamePanel.audio.playSound(1);
				finished = true;
				if (MainGamePanel.gui.fade >= 225){
					Counter.stopTimer();
					MainGamePanel.player.setY(0);
					MainGamePanel.gui.fadeIn = false;
					MainGamePanel.menu.setOpen(true);
					MainGamePanel.menu.setLevel(MainGamePanel.level.currentLevel);
					MainGamePanel.level.clearMap();
					MainGamePanel.level.checkHighestLevel();
					MainGamePanel.level.setTimer(MainGamePanel.level.currentLevel,
							(Counter.getFinalTime().toString()));
					if (MainGamePanel.level.currentLevel < 29) MainGamePanel.menu.setNext(true);
					else MainGamePanel.menu.complete();
				}
			}
			else if (getType(x+4,y+jump.y) > 8 || getType(x+28,y+jump.y) > 8){ //ground spikes
				if ((getType(x+4,y+jump.y)-1)%4==0 && getType(x+4,y+jump.y) > 8){
					alive = false;
					particles.addParticles(62,y,sprint.y, 0);
					MainGamePanel.gui.fadeOut(15);
					MainGamePanel.audio.playSound(0);
				}
				else if ((getType(x+28,y+jump.y)-1)%4==0 && getType(x+28,y+jump.y) > 8){
					alive = false;
					particles.addParticles(62,y,sprint.y, 0);
					MainGamePanel.gui.fadeOut(15);
					MainGamePanel.audio.playSound(0);
				}
			}
			else if (getType(x+33,y-1) > 8 || getType(x+33,y-28) > 8){ //spikes looking left
				if ((getType(x+33,y-1)-1)%4==3 && getType(x+33,y-1) > 8){
					alive = false;
					particles.addParticles(62,y,sprint.y, 0);
					MainGamePanel.gui.fadeOut(15);
					MainGamePanel.audio.playSound(0);
				}
				else if ((getType(x+33,y-28)-1)%4==3 && getType(x+33,y-28) > 8){
					alive = false;
					particles.addParticles(62,y,sprint.y, 0);
					MainGamePanel.gui.fadeOut(15);
					MainGamePanel.audio.playSound(0);
				}
			}
			else if (getType(x+4, y+jump.y-33) > 8 || getType(x+28, y+jump.y-33) > 8){ //ceiling spikes
				if (jumping){
					if ((getType(x+4, y+jump.y-33)-1)%4==2 || (getType(x+28, y+jump.y-33)-1)%4==2){
						alive = false;
						particles.addParticles(62,y,sprint.y, 0);
						MainGamePanel.gui.fadeOut(15);
						MainGamePanel.audio.playSound(0);
					}
				}
			}
			else if (getType(x+0, y+jump.y-33)==5 || getType(x+28, y+jump.y-33)==5){ //hint box
				double tempX = 0;
				Counter.pause();
				if (getType(x+0, y+jump.y-33)==5) tempX = 0;
				else if (getType(x+28, y+jump.y-33)==5) tempX = 28;
				particles.removeAll();
				particles.addParticles(96,y-32,0, 1);
				MainGamePanel.level.remove((int)((x+tempX)/32), (int)((y+jump.y-48)/32));
				MainGamePanel.gui.hint.setOpen(true);
				MainGamePanel.gui.fadeOut(15,0,148,255);
				MainGamePanel.audio.playSound(0);
				jump.reset();
			}
		}
	}
	public void respawn(){
		MainGamePanel.gui.fadeOut(15);
		Counter.startTimer();
		y=0;
		x=62;
		alive = true;
		jump.reset();
		sprint.stop();
		sprinting = false;
		doubleJump = true;
		deathWall = 0;
		particles.adjustAll(0,600);
		resetAnimation();
		finished = false;
		MainGamePanel.gui.fade = 0;
		//MainGamePanel.bg.stars.reset();
	}
	public int getType(double x1, double y1){
		if (y1 >= 0 && y1 <= (MainGamePanel.level.getRows())*32)
		return MainGamePanel.level.map[(int)(y1/32)][(int)(x1/32)].getType();
		else return 0;
	}
	public double getX(){return x;}
	public double getY(){return y;}
	public boolean getJumping(){ return jumping; }
	public boolean isSprinting(){ return sprinting; }
	public boolean isAlive(){ return alive; }
	public void setX(double x){ this.x=x; }
	public void setY(double y){ this.y=y; }
	public void jump(){
		if (alive){
			if (!finished){
				if (doubleJump){
					doubleJump = false;
					jumping = false;
				}
				if (!jumping){
					if (!sprinting){
						x=(((int)(x+sprint.y)/32)*32)-3;
					}
					y-=1;
					jumping=true; 
					jump.jump();
					//MainGamePanel.audio.playSound(0);
				}
			}
		}
	}
	public void deathWall(boolean wall){
		if (wall){
			if (deathWall < -96){
				alive = false;
				particles.addParticles(0,y,5, 0);
				MainGamePanel.gui.fadeOut(15);
				MainGamePanel.audio.playSound(0);
			}
			else deathWall-=3.5;
		}
		else{
			if (deathWall < 0) deathWall+=0.5;
		}
	}
}
