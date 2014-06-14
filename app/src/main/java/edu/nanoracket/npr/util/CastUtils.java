package edu.nanoracket.npr.util;

public class CastUtils {
    private static final int HOUR = 1000*60*60;
    private static final int MINUTE = 1000 * 60;
    private static final int SECOND = 1000;
    private static final int TOTAL_PROGRESS = 100;
	
	public String milliSecondsToTimer(long milliseconds){
		int hours = (int)( milliseconds / HOUR);
		int minutes = (int)(milliseconds % HOUR) / MINUTE;
		int seconds = (int) ((milliseconds % HOUR) % MINUTE / SECOND);
        return String.format("%02d:02d:02d", hours, minutes, seconds);
	}
	
	public int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		long currentSeconds = (int) (currentDuration / SECOND);
		long totalSeconds = (int) (totalDuration / SECOND);
		percentage =(((double)currentSeconds)/totalSeconds) * TOTAL_PROGRESS;
		return percentage.intValue();
	}

	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / TOTAL_PROGRESS) * totalDuration);
		return currentDuration * SECOND;
	}
}
