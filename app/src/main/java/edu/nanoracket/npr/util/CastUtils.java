package edu.nanoracket.npr.util;

import android.util.Log;

public class CastUtils {
	
	public String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString;
		
		// Convert total duration into time
		int hours = (int)( milliseconds / (1000*60*60));
		int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		// Add hours if there
		if(hours > 0){
			   finalTimerString = hours + ":";
		}
		if(seconds < 10){
			   secondsString = "0" + seconds;
		}else{
			   secondsString = "" + seconds;
        }
		finalTimerString = finalTimerString + minutes + ":" + secondsString;
		return finalTimerString;
	}
	
	public int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		// calculating percentage
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		// return percentage
		return percentage.intValue();
	}

	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);
		// return current duration in milliseconds
		return currentDuration * 1000;
	}

	public long TimerToMilliSeconds(String timeString) {
		String[] timeArray = timeString.split(":");
		Log.i("Java", "Minute number" + timeArray);
		int mins = Integer.parseInt(timeArray[0]);
		Log.i("Java", "Minute number" + mins);
		int secs = Integer.parseInt(timeArray[1]);
		return (long)(mins*1000*60 + secs*1000);
	}
}
