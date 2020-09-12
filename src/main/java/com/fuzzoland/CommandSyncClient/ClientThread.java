package com.fuzzoland.CommandSyncClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class ClientThread extends BukkitRunnable {

	private static ClientThread instance;
	private InetAddress ip;
	private Boolean connected = false;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public static ClientThread getInstance() {
		
		if(instance == null) {
			
			instance = new ClientThread();
			instance.setup();
			
		}
		
		return instance;
		
	}
	
	public void setup() {
		
		try {
			
			ip = InetAddress.getByName(ConfigManager.getInstance().getIP());
			
		} catch (UnknownHostException e) {
			
			Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
		}
		
		connect();
		
		this.runTaskTimer(CSC.getInstance(), 0, ConfigManager.getInstance().getHeartBeat());
		
	}
	
	public void run() {
			
		if(connected) {
			
			out.println("heartbeat");
			
			if(out.checkError()) {
				
				connected = false;
				Debugger.getInstance().Log(Locale.getInstance().getString("ConnectLost"));
				
			} else {
				
				try {
					
					Integer size = CSC.getInstance().getOQ().size();
					Integer count = CSC.getInstance().getQC();
					
					if(size > count) {
						
						for(int i = count; i < size; i++) {
							
							count++;
							String output = CSC.getInstance().getOQ().get(i);
							
							out.println(output);
							
							Debugger.getInstance().Log(Locale.getInstance().getString("SentOutput", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), output));
						
						}
						
						CSC.getInstance().setQC(count);
						
					}
					
					while(in.ready()) {
						
						String input = in.readLine();
						
						if(!input.equals("heartbeat")) {
							
							Debugger.getInstance().Log(Locale.getInstance().getString("ReceivedInput", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), input));
							String[] data = input.split(CSC.getInstance().getSpacer());
							
							if(data[0].equals("console")) {
								
								String command = data[2].replaceAll("\\+", " ");
								safePerformCommand(Bukkit.getServer().getConsoleSender(), command, CSC.getInstance());
								Debugger.getInstance().Log(Locale.getInstance().getString("RanCommand", command));
								
							}
							
						}
						
					}
					
				} catch(IOException e) {
					
					Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
					
				}	
				
			}
			
		} else {
			
			connect();
			
		}
		
	}
	
	private void connect() {
		
		try {
			
			socket = new Socket(ip, ConfigManager.getInstance().getPort());
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.println(ConfigManager.getInstance().getName());
			
			String name = in.readLine();
            
			if(name.equals("n")) {
				
				Debugger.getInstance().Log(Locale.getInstance().getString("NameError", ConfigManager.getInstance().getName()));
			    socket.close();
			    return;
			    
			}
			
			out.println(ConfigManager.getInstance().getPassword());
			
			String password = in.readLine();
			
			if(password.equals("n")) {
				
				Debugger.getInstance().Log(Locale.getInstance().getString("InvalidPassword"));
			    socket.close();
				return;
				
			}
			
            out.println(CSC.getInstance().getDescription().getVersion());
            
            String version = in.readLine();
            
            if(version.equals("n")) {
            	
            	Debugger.getInstance().Log(Locale.getInstance().getString("VersionError", CSC.getInstance().getDescription().getVersion(), in.readLine()));
                socket.close();
                return;
                
            }
            
			connected = true;
			
			Debugger.getInstance().Log(Locale.getInstance().getString("ConnectInfo", ip.getHostName(), ConfigManager.getInstance().getPort() + "", ConfigManager.getInstance().getName()));
			
		} catch(IOException e) {
			
			Debugger.getInstance().Log(Level.WARNING, Locale.getInstance().getString("NoConnect"), e);
			
		}
		
	}
	
    public static void safePerformCommand(final CommandSender sender, final String command, CSC plugin) {
    	
	  // PaperSpigot will complain about async command execution without this. See http://bit.ly/1oSiM6C
    	
      if (Bukkit.getServer().isPrimaryThread()){
    	  
    	  Bukkit.getServer().dispatchCommand(sender, command);
    	  
      } else {
    	  
    	  Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().dispatchCommand(sender, command)); // This uses lambdas, in Java 8+ only
    	  
      }
      
    }
    
    public void Cancel() {
    	
    	if(this.isCancelled()) return;
    	
    	this.cancel();
    
    }
    
}