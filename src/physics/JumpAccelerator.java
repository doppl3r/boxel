package physics;


public class JumpAccelerator {
	public double y,x, startX, speed;
	public JumpAccelerator(double startX, double speed){
		this.startX=startX;
		this.speed=speed;
	}
	public void jump(){
		x=startX;
	}
	public void accelerate(){
		x+=speed;
		//removes the air time which occurs in cubic functions
		if (Math.abs(x)<(Math.abs(startX/(4f/2)))) x = (Math.abs(startX/(4f/2)));
		y=(Math.pow(x, 3)/(Math.pow(startX, 2)));
	}
	public void reset(){
		x = 0;
		y = 0;
	}
	public void set(double digit){
		x = digit;
	}
}
