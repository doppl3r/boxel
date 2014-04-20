package pack.boxel.main;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

	static SurfaceHolder surfaceHolder;
	static MainGamePanel gamePanel;
	static Canvas canvas;
	// flag to hold game state
	private boolean running;

	public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		MainThread.surfaceHolder = surfaceHolder;
		MainThread.gamePanel = gamePanel;
	}
	public void run() {
		while (running) {	
			try {
				synchronized (surfaceHolder) {
					Counter.StartFPSCounter(); //start counter
					canvas = MainThread.surfaceHolder.lockCanvas();
					gamePanel.draw(canvas);
					gamePanel.update();
					//surfaceHolder.unlockCanvasAndPost(canvas);
					//try {Thread.sleep(1);} 
					//catch (InterruptedException e) {e.printStackTrace();}
					Counter.StopAndPostFPS(); //calculate time
				}
			}
			finally {
				if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
}
