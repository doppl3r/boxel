package pack.boxel.main;
import android.graphics.Canvas;
import android.graphics.Rect;


public final class BitmapText{
	//private boolean rendered;
	private final int fontSize;
	private final int bitWidth, bitHeight,
		spriteWidth, spriteHeight;
	private final Rect spriteRect, destRect;
	//private Bitmap render;
	public BitmapText(){
		fontSize = 8;
		spriteWidth = 8;
		spriteHeight = 8;
		bitWidth = MainGamePanel.texture.text1.getWidth();
		bitHeight = MainGamePanel.texture.text1.getHeight();
		spriteRect = new Rect(0,0,bitWidth,bitHeight);
		destRect = new Rect();
	}
	public final void draw(String text, int x, int y, int type, Canvas canvas){
		for (int i = 0; i < text.length(); i++){
			update(text, x, y, i);
			if (type == 0) canvas.drawBitmap(MainGamePanel.texture.text1, spriteRect, destRect, null); //white
			else canvas.drawBitmap(MainGamePanel.texture.text2, spriteRect, destRect, null); //black
		}
	}
	public final void update(String text, int x, int y, int i){
		int a = ((int)(text.charAt(i)));
		//sprite extraction points
		spriteRect.top = getY(a);
		spriteRect.bottom = getY(a)+fontSize;
		spriteRect.left = getX(a);
		spriteRect.right = getX(a)+fontSize;
		//placement adjustment
		destRect.top = y;
		destRect.bottom = y+spriteHeight;
		destRect.left = x+(i*spriteWidth);
		destRect.right = destRect.left+spriteWidth;
	}
	public final int getX(int a){
		return ((a-32)%16)*8;
	}
	public final int getY(int a){
		return ((a-32)/16)*8;
	}
}
