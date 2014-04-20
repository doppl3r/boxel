package levels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;

import pack.boxel.main.Block;
import pack.boxel.main.MainGamePanel;
import pack.boxel.main.R;
import pack.boxel.main.Record;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class LevelHandler {
	private Rect spriteRect, destRect;
	private int bitWidth, bitHeight,
		spriteWidth, spriteHeight;
	private int rows;
	private int cols;
	private int blockSize;
	public int currentLevel, maxRender;
	private int theme;
	private int highestLevel;
	private boolean newRecord;
	
	private BufferedReader br;
	private Bitmap tiles, spikes;
	public Record record;
	public Block[][] map;
	public LinkedList<String> levels;
	public String[] timeHolder;
	private Context context;
	
	public LevelHandler(Context context){
		this.context=context;
		maxRender = 9;
		rows = 16;
		cols = 312;
		blockSize = 32;
		theme = 0;
		map = new Block[rows][cols];
		levels = new LinkedList<String>();
		timeHolder = new String[30];
		//blank score
		for (int i = 0; i < 30; i++)
		timeHolder[i]="--.--";
		//build sprite limits
		spriteWidth = 16;
		spriteHeight = 16;
		highestLevel = 0;
		bitWidth = MainGamePanel.texture.tiles.getWidth();
		bitHeight = MainGamePanel.texture.tiles.getHeight();
		adjustSpriteQuality("LOW");
		spriteRect = new Rect(0,0,bitWidth,bitHeight);
		destRect = new Rect();
		//initiate record
		record = new Record(context);
		initMap();
		//load file
		try {loadFile(context);} 
		catch (IOException e) {e.printStackTrace();}
	}
	public void load(){
		try {loadFile(context);} 
		catch (IOException e) {e.printStackTrace();}
	}
	public void draw(Canvas canvas){
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		int type = 0;
		//Render style 2
		for (int row = 0; row < 16; row++){
			for (int col = -2; col < maxRender-(MainGamePanel.player.deathWall/32); col++){
				if (col+(int)(MainGamePanel.player.getX()/blockSize) >= 0){
					type = map[row][(int)(MainGamePanel.player.getX()/blockSize)+col].getType();
					//paint objects in range of the player
					if (type > 0){
						adjustTexture(type, (int)(col*blockSize-MainGamePanel.player.getX()%
							blockSize+64+MainGamePanel.player.deathWall), row*32); //adjust texture selection
						//optimize tile placement according to quality
						if (type <= 8) canvas.drawBitmap(tiles, spriteRect, destRect, null);
						else canvas.drawBitmap(spikes, spriteRect, destRect, null);
					}
				}
			}
		}
	}
	public void adjustTexture(int type, int x1, int y1){
		//sprite extraction points
		spriteRect.top = ((type-1)/4)*spriteHeight; //4 = texture columns
		spriteRect.bottom = spriteRect.top+spriteHeight;
		spriteRect.left = ((type-1)%4)*spriteWidth; //4 = texture columns
		spriteRect.right = spriteRect.left+spriteWidth;
		//placement adjustment
		destRect.top = y1;
		destRect.bottom = y1+blockSize;
		destRect.left = x1;
		destRect.right = destRect.left+blockSize;
	}
	public void adjustSpriteQuality(String quality){
		Canvas canvas1 = new Canvas();
		Canvas canvas2 = new Canvas();
		if (quality.equalsIgnoreCase("high")) tiles = Bitmap.createBitmap(bitWidth,bitHeight, Bitmap.Config.ARGB_8888);
		else if (quality.equalsIgnoreCase("medium"))  tiles = Bitmap.createBitmap(bitWidth,bitHeight, Bitmap.Config.ARGB_4444);
		else if (quality.equalsIgnoreCase("low"))  tiles = Bitmap.createBitmap(bitWidth,bitHeight, Bitmap.Config.RGB_565);
		else tiles = Bitmap.createBitmap(bitWidth,bitHeight, Bitmap.Config.RGB_565);
		spikes = Bitmap.createBitmap(bitWidth,bitHeight, Bitmap.Config.ARGB_4444);
		canvas1.setBitmap(tiles); canvas1.drawBitmap(MainGamePanel.texture.tiles, 0,0,null);	
		canvas2.setBitmap(spikes); canvas2.drawBitmap(MainGamePanel.texture.tiles, 0,0,null);
	}
	public void set(int type, int x, int y){
		if (x > -1 && x < cols && y > -1 & y < rows) map[y][x].setType(type);
	}
	public void addPlatform(int type, int x1, int y1, int x2, int y2, boolean centered){
		int offset=0;
		if (centered) offset = x2/2;
		for (int row = 0; row < y2; row++){
			for (int col = 0; col < x2; col++){
				set(type, (x1+col)-offset, (y1+row)-offset);
			}
		}
	}
	public void remove(int x, int y) { map[y][x].setType(0); }
	public void initMap(){
		for (int row = 0; row < rows; row++){
			for (int col = 0; col < cols; col++){
				map[row][col] = new Block(0); //new block
			}
		}
	}
	public void clearMap(){
		for (int row = 0; row < rows; row++){
			for (int col = 0; col < cols; col++){
				map[row][col].setType(0);
			}
		}
	}
	public void random(){
		clearMap();
		
		int x = 2;
		int y = 8;
		int size = 10;
		int type;
		if (theme == 0) type = 1;
		else if (theme == 1) type = 2;
		else type = 3;
		
		addPlatform(type,x,y,size,1,false);
		x+=(size+3);
		while (x < 150){
			size=(int)(Math.random()*5)+7;
			y+=(int)(Math.random()*5)-2;
			if (y < 3) y = 4;
			if (y > 8) y = 7;
			addPlatform(type,x,y,size,1,false);
			if ((int)(Math.random()*6)==0) set(type+4,x+size/2,y-1);
			if ((int)(Math.random()*4)==0) addPlatform(type,x+1,y-5,size-1,1,false);
			x+=(size+2);
		}
		addPlatform(4,x+1,y,size-2,1,false);
		set(type,x,y);
		set(type,x+size-1,y);
	}
	public int getRows(){ return rows; }
	public int getCols(){ return cols; }
	public int getTheme(){ return theme; }
	public int getCurrentLevel(){ return currentLevel; }
	public int getHighestLevel(){ return highestLevel; }
	public BufferedReader getBufferedReader() { return br; }
	public void setHighestLevel(int i) {highestLevel = i;}
	public void setRows(int i){ rows = i; }
	public void setCols(int i){ cols = i; }
	public void setTheme(int i){ 
		theme = i;
	}
	public void toggleTheme(){
		if (theme < 2) theme++;
		else theme = 0;
	}
	public void checkHighestLevel(){
		if (highestLevel < currentLevel+1) highestLevel = currentLevel+1;
	}
	public boolean setNextLevel(){
		if (currentLevel < 29){
			currentLevel++;
			return true;
		}
		else{
			currentLevel = 29;
			return false;
		}
	}
	public void loadFile(Context context, int i) throws IOException{
		clearMap();
		//if (i+1 > highestLevel) highestLevel++; //set highest level
		InputStream is = context.getResources().openRawResource(R.raw.levels);
		InputStreamReader ir = new InputStreamReader(is);
		br = new BufferedReader(ir);
		
		Scanner in = new Scanner(br);
		String tempString = "";
		int type = 0;
		int row = 0;
		//skip lines to level
		for (int j = 0; j < i*(rows+2); j++){
			if (in.hasNextLine()) in.nextLine();
		}
		if (in.hasNextLine()){
			in.nextLine(); //skip level separator
			MainGamePanel.gui.hint.setHint(in.nextLine()); //sets level hint
			while (row < rows){
				tempString=in.nextLine();
				for (int a = 0; a < tempString.length(); a+=2){
					type = Integer.parseInt(tempString.substring(a,a+2));
					set(type, a/2, row);
				}
				row++;
			}
		}
		in.close();
	}
	public void loadFile(Context context) throws IOException{
		//initiate vars
		int tempRows = 0;
		String line;
		InputStream is = context.getResources().openRawResource(R.raw.levels);
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		StringBuilder total = new StringBuilder();
		//separate levels
		while ((line = r.readLine()) != null) {
			total.append(line+"\n");
			tempRows++;
			if (tempRows >= 18){
				levels.add(total.toString());
				tempRows = 0;
				total.delete(0,total.length());
			}
		}
	}
	public void loadLevel(int i){
		//variables
		clearMap();
		String tempString = "";
		int type;
		
		if (i <= highestLevel){
			tempString = levels.get(i);
			tempString = tempString.substring(tempString.indexOf("\n")); //chop off level
			MainGamePanel.gui.hint.setHint(tempString.substring(1, tempString.substring(1).indexOf("\n")+1)); //set hint
			tempString = tempString.substring(tempString.indexOf("\n")+1); //chop off hint
			tempString = tempString.substring(tempString.indexOf("\n")+1); //chop off hint
			
			for (int row = 0; row < rows; row++){
				for (int col = 0; col < cols; col++){
					type = Integer.parseInt(tempString.substring(col*2,(col*2)+2));
					set(type, col, row);
				}
				tempString = tempString.substring(tempString.indexOf("\n")+1); //clip string
			}
		}
	}
	public void setTimer(int index, String time){
		double x1,x2;
		Log.d("timer: ",time);
		
		if (timeHolder[index].equals("--.--")){
			timeHolder[index] = time;
			newRecord = true;
		}
		else{
			x1 = Double.parseDouble(time);
			x2 = Double.parseDouble(timeHolder[index]);
			if (x1 < x2){
				timeHolder[index] = time;
				newRecord = true;
			}
			else newRecord = false;
		}
	}
	public boolean isNewRecord(){ return newRecord; }
	public void levelToString(int i){
		Log.d("level "+(i+1), levels.get(i));
	}
}
