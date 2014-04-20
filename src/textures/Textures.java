package textures;
import pack.boxel.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;

public class Textures extends View {
	public Bitmap bg1, bg2, bg3, bg4, bg5, bg6, bg7, bg8, bg9; //backgrounds
	public Bitmap player, tiles, star, cloud, smoke; //effects
	public Bitmap text1, text2; //text
	public Bitmap levelSelect, play, exit, next, back, icon, logo; //menu
	public Textures(Context context){
		super(context);
		//player
		player = BitmapFactory.decodeResource(getResources(),  R.drawable.player);
		//terrain
		bg1 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg1);
		bg2 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg2);
		bg3 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg3);
		bg4 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg4);
		bg5 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg5);
		bg6 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg6);
		bg7 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg7);
		bg8 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg8);
		bg9 = BitmapFactory.decodeResource(getResources(),  R.drawable.bg9);
		tiles = BitmapFactory.decodeResource(getResources(),  R.drawable.tiles);
		//text
		text1 = BitmapFactory.decodeResource(getResources(),  R.drawable.text1);
		text2 = BitmapFactory.decodeResource(getResources(),  R.drawable.text2);
		//extra
		star = BitmapFactory.decodeResource(getResources(),  R.drawable.star);
		cloud = BitmapFactory.decodeResource(getResources(),  R.drawable.cloud);
		smoke = BitmapFactory.decodeResource(getResources(),  R.drawable.smoke);
		//menu
		levelSelect = BitmapFactory.decodeResource(getResources(),  R.drawable.levelselect);
		play = BitmapFactory.decodeResource(getResources(),  R.drawable.play);
		exit = BitmapFactory.decodeResource(getResources(),  R.drawable.exit);
		next = BitmapFactory.decodeResource(getResources(),  R.drawable.next);
		back = BitmapFactory.decodeResource(getResources(),  R.drawable.back);
		icon = BitmapFactory.decodeResource(getResources(),  R.drawable.icon);
		logo = BitmapFactory.decodeResource(getResources(),  R.drawable.logo);
	}
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		//use once, it's a heavier process
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
	public Bitmap crop(Bitmap bitmapOrg, int x1, int y1, int x2, int y2){
		return Bitmap.createBitmap(bitmapOrg, x1, y1, x2-x1, y2-y1);
	}
}
