package pack.boxel.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.util.Log;

public class Record {
	Context context;
	AlertDialog.Builder dlgAlert;
	
	public Record(Context context){this.context = context;}
	public void loadHighestLevel(){
		SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		Log.d("test", prefs.getInt("highestLevel", 0)+"");
		if (prefs.getInt("highestLevel", 0) <= 0) {
			displayMessage("A message from Doppler", "Thanks for choosing to download Boxel. If you " +
					"like the game, be sure to tell all your friends...and foes...\n\n"+"Have fun and good luck!");
		}
		MainGamePanel.level.setHighestLevel(prefs.getInt("highestLevel", 0)); //0 is the default value
		//MainGamePanel.level.setHighestLevel(30); //0 is the default value
	}
	public void saveHighestLevel(){
		MainGamePanel.level.setHighestLevel(MainGamePanel.level.getHighestLevel());
    	SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
    	Editor editor = prefs.edit();
    	editor.putInt("highestLevel", MainGamePanel.level.getHighestLevel());
    	editor.commit();
	}
	public void loadTimes(){
		SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		String tempString = prefs.getString("times", ""); //0 is the default value
		if (tempString.length() > 0){
			for (int i = 0; i < 30; i++){
				//Log.d(""+i,tempString.substring((i*5), (i*5)+5));
				MainGamePanel.level.setTimer(i, tempString.substring((i*5), (i*5)+5));
			}
		}
	}
	public void saveTimes(String[] times){
		String tempString = "";
		for (int i = 0; i < 30; i++){
			tempString+=times[i];
		}
    	SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
    	Editor editor = prefs.edit();
    	editor.putString("times", tempString);
    	editor.commit();
	}
	public void displayMessage(String title, String message){
		dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("Continue", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create();
        dlgAlert.show();
        dlgAlert.setPositiveButton("Continue",
        new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {Looper.prepare();}});
	}
}