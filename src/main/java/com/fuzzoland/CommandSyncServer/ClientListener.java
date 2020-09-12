package com.fuzzoland.CommandSyncServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ClientListener implements Runnable {

	public ClientListener(Integer heartbeat) {

		CSS.getInstance().getProxy().getScheduler().schedule(CSS.getInstance(), this, 10, heartbeat, TimeUnit.MILLISECONDS);
		
	}

	public void run() {
		
		ServerSocket server = CSS.getInstance().getServerSocker();
		
		if(server == null) {

			return;
			
		}

		
		try {

			Socket socket = server.accept();
		
			if(socket != null) {

				new ClientHandler(socket);
				
			}

		
		} catch(IOException e) {

			Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
		}
		
	}
	
}
