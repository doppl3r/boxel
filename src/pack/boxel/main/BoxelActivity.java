package pack.boxel.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class BoxelActivity extends Activity {
	static MainGamePanel game;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        		WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        game = new MainGamePanel(this);
        setContentView(game);
        MainGamePanel.level.load();
        //configure record
        MainGamePanel.level.record.loadHighestLevel();
        MainGamePanel.level.record.loadTimes();
    }
    public void onPause(){
    	//pause game
    	Counter.reset();
    	MainGamePanel.pause = false;
    	MainGamePanel.audio.stopAll();
    	MainGamePanel.level.record.saveTimes(MainGamePanel.level.timeHolder);
    	MainGamePanel.level.record.saveHighestLevel();
    	MainGamePanel.thread.setRunning(false);
    	finish();
    	super.onPause();
    }
    public void onResume(){
    	MainGamePanel.exit=false;
    	super.onResume();
    }
    public void onStop() {
    	finish();
		super.onStop();
	}
	public void onDestroy() {
		finish();
		super.onDestroy();
	}
}