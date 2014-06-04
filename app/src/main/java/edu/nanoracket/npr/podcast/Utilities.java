package edu.nanoracket.npr.podcast;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
	
	/**
	 * Function to convert milliseconds time to
	 * Timer Format
	 * Hours:Minutes:Seconds
	 * */
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
		   // Prepending 0 to seconds if it is one digit
		   if(seconds < 10){ 
			   secondsString = "0" + seconds;
		   }else{
			   secondsString = "" + seconds;}
		   finalTimerString = finalTimerString + minutes + ":" + secondsString;
		// return timer string
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

	/**
	 * Function to change progress to timer
	 * @param progress - 
	 * @param totalDuration
	 * returns current duration in milliseconds
	 * */
	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);
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

    public static String convertString(String str){
        String regexPat = "#([0-9]+)(:|((\\s)-(\\s)))";

        Pattern pattern = Pattern.compile(regexPat);
        Matcher matcher = pattern.matcher(str);

        if(matcher.find()){
            return matcher.replaceAll("");
        }else {
            return str;
        }
    }
}
