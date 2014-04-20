package pack.boxel.main;

public class Block {
	int id, x, y;
	public Block(int id){
		this.id = id;
	}
	public int getType(){
		return id;
	}
	public void setType(int x1){
		id = x1;
	}
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	public boolean isActive(){
		if (id > 0) return true;
		else return false;
	}
}
