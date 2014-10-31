import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.io.IOException;


public class MyLogger{
	private Logger myLogger;
	private  FileHandler myHandler;
	private Formatter myFormatter;
	public MyLogger(){
		// get the global logger to configure it
		myLogger =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		// suppress the logging output to the console
	    Logger rootLogger = Logger.getLogger("");
	    Handler[] handlers = rootLogger.getHandlers();
	    if (handlers[0] instanceof ConsoleHandler) {
	      rootLogger.removeHandler(handlers[0]);
	    }
	    myLogger.setLevel(Level.ALL);

	    try {
			myHandler = new FileHandler("output.txt");
			myFormatter = new MyFormatter();
		    myHandler.setFormatter(myFormatter);
		    myLogger.addHandler(myHandler);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	public void log(String message){
		myLogger.info(message);
	}
	public class MyFormatter extends Formatter{
		@Override
		public String format(LogRecord record) {
			return record.getMessage()+"\n"; 
		}
	}
	/*
	public static void main(String args[]){
		
		MyLogger logger = new MyLogger();
		logger.log("avfc");
		logger.log("cv");
	}*/
}
