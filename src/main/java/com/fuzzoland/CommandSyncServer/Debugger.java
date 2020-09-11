package com.fuzzoland.CommandSyncServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Debugger {

	
	private static Debugger instance;
    private Logger logger;
    
    public static Debugger getInstance() {
    	
    	if(instance == null) {
    		
    		instance = new Debugger();
    		instance.setup();
    		
    	}
    	
    	return instance;
    	
    }
    
    public void setup() {
    	
		logger = CSS.getInstance().getLogger();
    	
    }
    
    public void debug(String filename, String message, Exception exception) {
    	
    	logger.log(Level.INFO, "Writing to Log: '" + filename + ".txt'");
    	
        try {
        	
    		File directory = new File(CSS.getInstance().getDataFolder() + File.separator + "ErrorLogs");
    		
    		if(!directory.exists()) {
    			
    			directory.mkdirs();
    			
    		}
    		
    		File file = new File(CSS.getInstance().getDataFolder() + File.separator + "ErrorLogs" + File.separator + filename + ".txt");
    		
    		if(!file.exists()) {
    			
    			file.createNewFile();
    					
    		}
    		
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            
            ps.println("Error: " + message);
            
            for(int i= 2; i < elements.length; i++) { //2 to remove Thread#getStackTrace and this method
        	
            	ps.println(elements[i]);
        	
            }
            
            ps.close();
            
        } catch(IOException e) {
        	
        	Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
            
        }
        
    }
    
    public void Log(String message) {

    	Log(Level.INFO, message);
    	
    }

    public void Log(Level level, String message) {
    	
    	Log(level, message, null);
    	
    }
    
    public void Log(Level level, String message, Exception exception) {
    	
		if(exception == null) {
			
			logger.log(level, message);
			
		} else if(level.equals(Level.INFO)){

			logger.log(level, message);
			
		} else {
			
			String timestamp = new SimpleDateFormat("MM-dd-yy-HH-mm-ss").format(Calendar.getInstance().getTime());
			logger.log(level, "0" + message);
			debug(timestamp, message, exception);
		
		}
    	
    }
    
}