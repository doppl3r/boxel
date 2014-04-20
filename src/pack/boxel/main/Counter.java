package pack.boxel.main;

final public class Counter { 
    private static int FPSStartTime;  
    private static int FPSEndTime; 
    private static int frameTimes = 0;
    private static int maxFrames = 0;
    private static short frames = 0;
    private static double startTimer, endTimer, startPause;
    private static boolean pause;
    
    public final static void StartFPSCounter() {  
        FPSStartTime = (int) System.currentTimeMillis();  
    }   
    public final static void StopAndPostFPS() {  
        FPSEndTime = (int) System.currentTimeMillis();   
        frameTimes += FPSEndTime - FPSStartTime;  
        frames++;  
        
        if(frameTimes >= 1000) { 
        	maxFrames = frames;
            frames = 0;  
            frameTimes = 0;  
        }  
    }  
    public final static String getFPS(){ 
    	return maxFrames+"" ;
    }
    public final static void startTimer(){
    	startTimer = System.currentTimeMillis();
    }
    public final static void stopTimer(){
    	endTimer = System.currentTimeMillis();
    }
    public final static void pause(){
    	pause=true; 
    	startPause = System.currentTimeMillis();
    }
    public final static void resume(){
    	if (pause){
    		startTimer += (System.currentTimeMillis() - startPause);
    		pause = false;
    	}
    }
    public final static void reset(){ startPause = startTimer = endTimer = 0; }
    public final static String getCurrentTime(){
    	double currentTime;
    	if (pause) currentTime = (startPause - startTimer)/1000;
    	else currentTime = ((System.currentTimeMillis() - startTimer)/1000);
    	String tempString = currentTime+"";
    	if ((int)currentTime < 10) tempString = "0"+tempString;
    	while (tempString.length() < 5) tempString+="0";
    	return tempString.substring(0,5); //up to 2 decimals
    }
    public final static String getFinalTime(){
    	double currentTime = ((endTimer - startTimer)/1000);
    	String tempString = currentTime+"";
    	if ((int)currentTime < 10) tempString = "0"+tempString;
    	while (tempString.length() < 5) tempString+="0";
    	return tempString.substring(0,5); //up to 2 decimals
    }
}  