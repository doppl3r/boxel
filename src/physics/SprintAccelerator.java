package physics;

public class SprintAccelerator {
	public double x, y, minX, maxX, speed, acceleration;
	public SprintAccelerator(double maxX, double speed, double acceleration){
		this.maxX = maxX;
		this.speed = speed;
		this.acceleration = acceleration;
	}
	public void accelerate(){
		if (x < maxX){
			x+=speed;
		}
		else x = maxX;
		y = Math.pow((acceleration)*x+(speed), 2) - Math.pow((acceleration)*x, 2);
	}
	public void decelerate(){
		if (x > minX) x-=speed*8; //decelerate slower}
		else x = 0;
		y = Math.pow((acceleration)*x+(speed), 2) - Math.pow((acceleration)*x, 2);
	}
	public void stop(){
		x = y = 0;
	}
}
