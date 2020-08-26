package com.fuzzoland.CommandSyncServer;

import java.io.IOException;
import java.util.logging.Level;

public class ClientListener extends Thread {

	private Integer heartbeat;
	private String pass;

	public ClientListener(Integer heartbeat, String pass) {
		
		this.heartbeat = heartbeat;
		this.pass = pass;
		
	}

	public void run() {
		
		while(true) {
			
			try {
				
				new ClientHandler(CSS.getInstance().server.accept(), heartbeat, pass).start();
				
			} catch(IOException e) {
				
				Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
				
			}
			
		}
		
	}
	
}