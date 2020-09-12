package com.fuzzoland.CommandSyncServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class ClientHandler implements Runnable {

	private Socket socket;
	private ScheduledTask t;
    private PrintWriter out;
    private BufferedReader in;
	private Boolean connected = false;
    private Integer heartbeat = 0;
    private String name;
    private String pass;

	public ClientHandler(Socket socket) {

		this.socket = socket;
		connect();
		t = CSS.getInstance().getProxy().getScheduler().schedule(CSS.getInstance(), this, 10, heartbeat, TimeUnit.MILLISECONDS);
		
	}

	public void run() {

		if(connected) {

			try {
				
				out.println("heartbeat");
				
				if(out.checkError()) {
					
					Debugger.getInstance().Log(Level.WARNING, Locale.getInstance().getString("Disconect", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), name));
					CSS.getInstance().getC().remove(name);
					connected = false;
					return;
					
				}
				
				while(in.ready()) {
					
					String input = in.readLine();
					
					if(!input.equals("heartbeat")) {
						
						Debugger.getInstance().Log(Locale.getInstance().getString("BungeeInput", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), name, input));
						
						String[] data = input.split(CSS.getInstance().getSpacer());
						
						if(data[0].equals("player")) {
							
							String command = "/" + data[2].replaceAll("\\+", " ");
							
							if(data[1].equals("single")) {
								
								String name = data[3];
								Boolean found = false;
								
								for(ProxiedPlayer player : CSS.getInstance().getProxy().getPlayers()) {
									
									if(name.equals(player.getName())){
										
										player.chat(command);
										Debugger.getInstance().Log(Locale.getInstance().getString("BungeeRanPlayerSingle", command, name));
										found = true;
										break;
										
									}
									
								}
								
								if(!found) {
									
									if(CSS.getInstance().getPQ().containsKey(name)) {
										
										List<String> commands = CSS.getInstance().getPQ().get(name);
										commands.add(command);
										CSS.getInstance().getPQ().put(name, commands);
										
									} else {
										
										CSS.getInstance().getPQ().put(name, new ArrayList<String>(Arrays.asList(command)));
										
									}
									
									Debugger.getInstance().Log(Locale.getInstance().getString("BungeeRanPlayerOffline", name, command));
									
								}
								
							} else if(data[1].equals("all")) {
								
								for(ProxiedPlayer player : CSS.getInstance().getProxy().getPlayers()) {
									
									player.chat(command);
									
								}
								
								Debugger.getInstance().Log(Locale.getInstance().getString("BungeeRanAll", command));
								
							}
							
						} else {
							
							if(data[1].equals("bungee")) {
								
								String command = data[2].replaceAll("\\+", " ");
								CSS.getInstance().getProxy().getPluginManager().dispatchCommand(CSS.getInstance().getProxy().getConsole(), command);
								Debugger.getInstance().Log(Locale.getInstance().getString("BungeeRanServer", command));
								
							} else {
								
								CSS.getInstance().getOQ().add(input);
								
							}
							
						}
						
					}
					
				}
				
				Integer size = CSS.getInstance().getOQ().size();
				Integer count = CSS.getInstance().getQC().get(name);
				
				if(size > count) {
					
					for(int i = count; i < size; i++) {
						
						count++;
						String output = CSS.getInstance().getOQ().get(i);
						String[] data = output.split(CSS.getInstance().getSpacer());
						
						if(data[1].equals("single")) {
						
							if(data[3].equals(name)) {
							
								out.println(output);
								Debugger.getInstance().Log(Locale.getInstance().getString("BungeeSentOutput", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), name, output));
							
							}
						
						} else {
						
							out.println(output);
							Debugger.getInstance().Log(Locale.getInstance().getString("BungeeSentOutput", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), name, output));
						
						}
					
					}
					
					CSS.getInstance().getQC().put(name, count);
				
				}
			
			} catch(Exception e) {
			
				CSS.getInstance().getC().remove(name);
				Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
			}
			
		} else {

			connect();
			
		}
		
	}
	
	public void connect() {

		try {
			
			ServerSocket server = CSS.getInstance().getServerSocker();
			
			if(server == null) {

				Debugger.getInstance().Log(Level.WARNING, "Unable to start Socket");
				return;
				
			}
			
			if(socket == null || socket.isClosed()) {

				this.socket = server.accept();
			
			}

			this.heartbeat = ConfigManager.getInstance().getHeartBeat();
			this.pass = ConfigManager.getInstance().getPassword();
			
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			name = in.readLine();

			Debugger.getInstance().Log(Locale.getInstance().getString("BungeeConnect", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort())));
			
			if(CSS.getInstance().getC().contains(name)) {
				
				Debugger.getInstance().Log(Level.WARNING, Locale.getInstance().getString("NameErrorBungee", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), name));
			    out.println("n");
			    socket.close();
			    return;
			    
			}
			
			out.println("y");
			
			if(!in.readLine().equals(this.pass)) {
				
				Debugger.getInstance().Log(Level.WARNING, Locale.getInstance().getString("PassError", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), name));
				out.println("n");
				socket.close();
				return;
				
			}
			
			out.println("y");
			String version = in.readLine();
			
			if(!version.equals(CSS.getInstance().getDescription().getVersion())) {
				
				Debugger.getInstance().Log(Level.WARNING, Locale.getInstance().getString("VersionErrorBungee", name, version, "" + CSS.getInstance().getDescription().getVersion()));
			    out.println("n");
			    out.println(CSS.getInstance().getDescription().getVersion());
			    socket.close();
			    return;
			    
			}
			
			out.println("y");
			
			if(!CSS.getInstance().getQC().containsKey(name)) {
				
				CSS.getInstance().getQC().put(name, 0);
			    
			}
			
			CSS.getInstance().getC().add(name);
			
			Debugger.getInstance().Log(Locale.getInstance().getString("ConnectFrom", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()), name));
			
		} catch(Exception e) {
			
			//Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			this.cancel();
			return;
			
		}
		
		connected = true;
		
	}
	
	public void cancel() {
		
		CSS.getInstance().getProxy().getScheduler().cancel(t);
				
	}
	
}