package particle;
import physics.JumpAccelerator;

public class Particle {
	double x, y, direction;
	JumpAccelerator jump;
	public Particle(int x, int y, double momentum){
		this.x=x;
		this.y=y;
		direction=Math.random()*(Math.random()*9)-3.0+momentum;
		jump= new JumpAccelerator(-(Math.random()*8)-6,0.2);
	}
	public void update(){
		jump.accelerate();
		y+=jump.y;
		x+=direction;
	}
	public void setX(double x){ this.x=x; }
	public void setY(double y){ this.y=y; }
	public void stop(){ jump.reset(); }
}
