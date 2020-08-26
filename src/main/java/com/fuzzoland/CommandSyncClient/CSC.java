package com.fuzzoland.CommandSyncClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CSC extends JavaPlugin {

	private static CSC instance;
	private ClientThread client;
	private List<String> oq = Collections.synchronizedList(new ArrayList<String>());
	private Integer qc = 0;
	private String spacer = "@#@";
	
	public void onEnable() {
		
		instance = this;
		
		ConfigManager.getInstance();
		
		Debugger.getInstance();
		
		Locale.getInstance();
		
		ClientThread.getInstance();
		
		try {
			
			workData();
			
		} catch (IOException e) {
			
			Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
		}
		
		getCommand("Sync").setExecutor(new CommandSynchronize(this));
		
	}
	
	public void onDisable() {
		
		saveData();
		ClientThread.getInstance().Cancel();
		
	}
	
	private void workData() throws IOException {
		
		File folder = getDataFolder();
		File data = new File(folder + File.separator + "data.txt");
        
        if (ConfigManager.getInstance().isDataRemoved()) {
        	
        	if(data.delete()){
        	
        		Bukkit.getConsoleSender().sendMessage(Locale.getInstance().getString("DataRemoved"));
        		
			} else {
				
				Bukkit.getConsoleSender().sendMessage(Locale.getInstance().getString("DataRemoveNotFound"));
			
			}
        	
        } else {
        	
    		loadData();
    		
        }
        
	}
	
	private void saveData() {
		
		try{
			
			OutputStream os = new FileOutputStream(new File(getDataFolder(), "data.txt"));
			PrintStream ps = new PrintStream(os);
			
			for(String s : oq) {
				
				ps.println("oq:" + s);
				
			}
			
			ps.println("qc:" + String.valueOf(qc));
			ps.close();
			
			Debugger.getInstance().Log(Locale.getInstance().getString("DataSaved"));
			
		}catch(IOException e){
			
			Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
		}
		
	}
	
	private void loadData() {
		
		try{
			
			File file = new File(getDataFolder(), "data.txt");
			
			if(file.exists()) {
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				try {
					
					String l = br.readLine();
					
					while(l != null) {
						
						if(l.startsWith("oq:")) {
							
							oq.add(new String(l.substring(3)));
							
						} else if(l.startsWith("qc:")) {
							
							qc = Integer.parseInt(new String(l.substring(3)));
							
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
	
	public static CSC getInstance() {
		
		return instance;
		
	}
	
	public String getSpacer() {
		
		return this.spacer;
		
	}
	
	public List<String> getOQ() {
		
		return this.oq;
		
	}
	
	public Integer getQC() {
		
		return this.qc;
		
	}
	
	public void setQC(Integer qc) {
		
		this.qc = qc;
		
	}
	
}