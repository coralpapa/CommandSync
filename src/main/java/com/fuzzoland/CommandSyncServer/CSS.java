package com.fuzzoland.CommandSyncServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import net.md_5.bungee.api.plugin.Plugin;

public class CSS extends Plugin {

	private static CSS instance;
	private ClientListener clientlistener = null;
	private ServerSocket server;
	private Set<String> c = Collections.synchronizedSet(new HashSet<String>());
	private List<String> oq = Collections.synchronizedList(new ArrayList<String>());
	private Map<String, List<String>> pq = Collections.synchronizedMap(new HashMap<String, List<String>>());
	private Map<String, Integer> qc = Collections.synchronizedMap(new HashMap<String, Integer>());
	private String spacer = "@#@";

	public void onEnable() {
		
		this.instance = this;
		
		ConfigManager.getInstance();
		
		Debugger.getInstance();
		
		Locale.getInstance();
		
		clientlistener = new ClientListener(ConfigManager.getInstance().getHeartBeat());
		
		try {
			
			workData();
			
		} catch (IOException e1) {

			Debugger.getInstance().Log(Level.WARNING, e1.getMessage(), e1);
			
		}

		getProxy().getPluginManager().registerListener(this, new EventListener());
		
		Debugger.getInstance().Log(Level.INFO, "Enabled");

	}
	
	private void workData() throws IOException {
		
		File folder = getDataFolder();
		File data = new File(folder + File.separator + "data.txt");
		
        if(ConfigManager.getInstance().isDataRemoved()) {
        	
        	if(data.delete()) {
        		
        		Debugger.getInstance().Log(Locale.getInstance().getString("DataRemoved"));
    			
			} else {
				
				Debugger.getInstance().Log(Locale.getInstance().getString("DataRemoveNotFound")); 
			
			}
        	
        } else {
        	
    		loadData();
        
        }
        
	}
	
	public void onDisable() {
		
		saveData();
	
	}
	
	private void saveData() {
		
		try {
			
			OutputStream os = new FileOutputStream(new File(getDataFolder(), "data.txt"));
			PrintStream ps = new PrintStream(os);
			
			for(String s : oq) {
				
				ps.println("oq:" + s);
				
			}
			
			for(Entry<String, List<String>> e : pq.entrySet()) {
				
				String name = e.getKey();
				
				for(String command : e.getValue()) {
					
					ps.println("pq:" + name + spacer + command);
					
				}
				
			}
			
			for(Entry<String, Integer> e : qc.entrySet()) {
				
				ps.println("qc:" + e.getKey() + spacer + String.valueOf(e.getValue()));
				
			}
			
			ps.close();
			
			Debugger.getInstance().Log(Locale.getInstance().getString("DataSaved"));
			
		}catch(IOException e){
			
			Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
		}
		
	}
	
	private void loadData() {
		
		try {
			
			File file = new File(getDataFolder(), "data.txt");
			
			if(file.exists()) {
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				try {
					
					String l = br.readLine();
					
					while(l != null) {
						
						if(l.startsWith("oq:")) {
							
							oq.add(new String(l.substring(3)));
							
						} else if(l.startsWith("pq:")) {
							
							String[] parts = new String(l.substring(3)).split(spacer);
							
							if(pq.containsKey(parts[0])) {
								
								List<String> commands = pq.get(parts[0]);
								
								commands.add(parts[1]);
								
								pq.put(parts[0], commands);
								
							} else {
								
								List<String> commands = new ArrayList<String>(Arrays.asList(parts[1]));
								pq.put(parts[0], commands);
								
							}
							
						} else if(l.startsWith("qc:")) {
							
							String[] parts = new String(l.substring(3)).split(spacer);
							qc.put(parts[0], Integer.parseInt(parts[1]));
							
						}
						
						l = br.readLine();
						
					}
					
					Debugger.getInstance().Log(Locale.getInstance().getString("DataLoaded"));
					
				} finally {
					
					br.close();
					
				}
				
			} else {
				
				Debugger.getInstance().Log(Locale.getInstance().getString("DataNotfound"));
				
			}
			
		} catch(IOException e) {
			
			Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
		}
		
	}
	
	public ServerSocket getServerSocker() {
		
		if(server == null || server.isClosed()) {
			
			try {
				
				server = new ServerSocket(ConfigManager.getInstance().getPort(), 50, InetAddress.getByName(ConfigManager.getInstance().getIP()));
				
				Debugger.getInstance().Log(Locale.getInstance().getString("OpenOn", ConfigManager.getInstance().getIP(), ConfigManager.getInstance().getPort() + ""));

			} catch(Exception e) {
				
				Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
				return null;
				
			}
		
		}
		
		return this.server;
		
	}
	
	public static CSS getInstance() {
		
		return instance;
		
	}
	
	public String getSpacer() {
		
		return this.spacer;
		
	}
	
	public Map<String, List<String>> getPQ(){
		
		return this.pq;
		
	}
	
	public Set<String> getC() {
		
		return this.c;
		
	}
	
	public List<String> getOQ() {
		
		return this.oq;
				
	}
	
	public Map<String, Integer> getQC() {
		
		return this.qc;
		
	}
	
}