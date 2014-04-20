package levels;

import java.util.LinkedList;

import pack.boxel.main.BitmapText;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Hint {
	private String hint;
	private Bitmap hintBit;
	private BitmapText text;
	private int width;
	private int height;
	private int textLimit;
	private boolean open;
	
	public Hint(){
		text = new BitmapText();
		textLimit = 32; //text wrap
	}
	public String getHint(){ 
		if (hint.length() <= 0) return " ";
		else return hint;
	}
	public void setHint(String hint){ 
		int spaceCount = -1;	
		//protect against huge words by space divisions
		for (int i = 0; i < hint.length()-1; i++){
			if (hint.substring(i,i+1).equals(" ")) spaceCount = 0;
			else{
				spaceCount++;
				if (spaceCount >= textLimit - 1){
					hint = hint.substring(0, i-2) + "- "+hint.substring(i);
					spaceCount = 0;
				}
			}
		}
		this.hint=hint; 
		renderHint();
	} 
	public void setTextLimit(int textLimit){ this.textLimit=textLimit; }
	public void renderHint(){
		int padding = 8;
		int lineX = 0;
		int lineY = 0;
		//add words to a container
		int nextWord = 0;
		LinkedList<String> words = new LinkedList<String>();
		for (int i = 0; i < hint.length()-1; i++){
			if (hint.substring(i, i+1).equals(" ")){
				words.add(hint.substring(nextWord, i)+" "); //space is for wrapping
				nextWord = i+1;
			}
		}
		if (nextWord <= hint.length()-1){
			words.add(hint.substring(nextWord)+" "); //adds final word
		}
		//initialize image width
		if (hint.length() > textLimit) width = (textLimit*8);
		else width = ((hint.length()+2)*8);
		
		//initialize image height
		for (int i = 0; i < words.size(); i++){
			if (lineX + words.get(i).length() < textLimit){
				lineX+=words.get(i).length();
			}
			else{
				lineY++; lineX=0;
				lineX+=words.get(i).length();
			}
		}
		height = (lineY+1)*10+padding*2;
		lineX=0; //reset x
		lineY=0; //reset y
		
		//color system
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas();
		
		//set up background
		hintBit = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
		canvas.setBitmap(hintBit);
		paint.setARGB(150,0,148,255);
		canvas.drawRect(0, 4, width, height-4, paint);
		canvas.drawRect(4, 0, width-4, height, paint);
		
		//add text
		for (int i = 0; i < words.size(); i++){
			if (lineX + words.get(i).length() < textLimit){
				//border
				text.draw(words.get(i), lineX*8+padding+1, lineY*10+padding+1, 1, canvas);
				//text
				text.draw(words.get(i), lineX*8+padding-1, lineY*10+padding, 0, canvas);
				text.draw(words.get(i), lineX*8+padding, lineY*10+padding, 0, canvas);
				lineX+=words.get(i).length();
			}
			else{
				lineY++; lineX=0;
				//border
				text.draw(words.get(i), lineX*8+padding+1, lineY*10+padding+1, 1, canvas);
				//text
				text.draw(words.get(i), lineX*8+padding-1, lineY*10+padding, 0, canvas);
				text.draw(words.get(i), lineX*8+padding, lineY*10+padding, 0, canvas);
				lineX+=words.get(i).length();
			}
		}
	}
	public boolean isOpen(){ return open; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public void setOpen(boolean open){ this.open=open; }
	public Bitmap getHintBitmap(){
		return hintBit;
	}
}
