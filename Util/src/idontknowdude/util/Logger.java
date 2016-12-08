package idontknowdude.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author jobota
 * Logger is a largely static class that sends messages to various
 * fixed log files located in /var/log/idontknowdude folder. This
 * class also applies timestamps to messages based on system time.
 */
public class Logger {
	
	private static final String ERROR_FAILED_CREATE = "Failed to create ";
	private static final String ERROR_FAILED_WRITE = "Failed to write to ";
	
	private static File loggerDir = new File(Constants.LOGGER_DIR);
	private static File generalLog = new File(Constants.LOGGER_GENERAL_LOG);
	private static File errorLog = new File(Constants.LOGGER_ERROR_LOG);
	
	/**
	 * Prints some message to the console. Will also write the message
	 * to the /var/log/idontknowdudde/general.log file. Appends a time-
	 * stamp to the time at which toConsole() was called.
	 * @param String messageToConsole
	 * @return Boolean success
	 */
	public static boolean toConsole(String message) {
		return outputTerminal(message);
	}
	
	/**
	 * Prints some message  to /var/log/idontknowdude/error.log. Appends
	 * a timestamp to the time at which toErrorLog() was called.
	 * @param String messageToErrorLog
	 * @return Boolean success
	 */
	public static boolean toErrorLog(String message) {
		return outputToLogFile(message, errorLog);
	}
	
	/**
	 * Prints some message  to /var/log/idontknowdude/general.log. Appends
	 * a timestamp to the time at which toGeneralLog() was called.
	 * @param String messageToGeneralLog
	 * @return Boolean success
	 */
	public static boolean toGeneralLog(String message) {
		return outputToLogFile(message, generalLog);
	}
	
	
	private static boolean outputTerminal(String message) {
		boolean retVal = true;

		String output = getTimestamp() + message;
		System.out.println(output);
		retVal = outputToLogFile(output, generalLog);
		
		return retVal;
	}
	
	private static boolean outputToLogFile(String message, File logFile) {
		boolean retVal = true;
		
		if(!logFile.isFile()) {
			retVal = createLogFile(logFile);
		}
		
		if(!retVal) {
			return retVal;
		}
		
		try(FileWriter fileWriter = new FileWriter(logFile, true);
			    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			    PrintWriter printWriter = new PrintWriter(bufferedWriter))
		{
			printWriter.println(message);
		} catch (IOException e) {
			e.printStackTrace();
			if (!logFile.equals(errorLog)) {
				outputToLogFile(ERROR_FAILED_WRITE + logFile.toString(), errorLog);
			}
			retVal = false;
		}
		
		return retVal;
	}
	
	private static boolean createLogFile(File logFile) {
		boolean retVal = true;
		
		if(!loggerDir.isDirectory()) {
			retVal = loggerDir.mkdir();
		}
		
		if(!retVal) {
			return retVal;
		}
		
		try{
		    PrintWriter printWriter = new PrintWriter(logFile);
		    printWriter.println(getTimestamp() + logFile.toString() + " created.");
		    printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			if (!logFile.equals(errorLog)) {
				outputToLogFile(ERROR_FAILED_CREATE + logFile.toString(), errorLog);
			}
		    retVal = false;
		}
		
		return retVal;
	}
	
	private static String getTimestamp() {
		String retVal = "";
		
	    DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss : ");
	    Calendar calendar = Calendar.getInstance();
	    retVal = df.format(calendar.getTime());
		
		return retVal;
	}
}
