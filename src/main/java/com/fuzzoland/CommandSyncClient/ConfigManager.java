package com.fuzzoland.CommandSyncClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {

	private static ConfigManager instance;
	private SpigotYaml config;
	private String ip = "localhost";
	private Integer port = 9190;
	private Integer heartbeat = 100;
	private String name = "ClientName";
	private String pass = "Password";
	private Boolean debug = false;
	private Boolean removedata = true;
	private String lang = "en_US";
	
	public static ConfigManager getInstance() {
		
		if(instance == null) {
			
			instance = new ConfigManager();
			instance.setup();
			
		}
		
		return instance;
		
	}
	
	public void setup() {
		
		config = new SpigotYaml(CSC.getInstance().getDataFolder().toString(), "config.yml");
		
		loadConfig();
		
		if(getName().equalsIgnoreCase("ClientName")) {
			
			try {
				
				File file = new File("server.properties");
				BufferedReader read;
				read = new BufferedReader(new FileReader(file));
				String line;
				
				while ((line = read.readLine()) != null) {
					
					if(line.contains("server-name")) {
						
						setName(line.replace("server-name=", ""));
						
					}
					
				}
				
				read.close();
				
				Debugger.getInstance().Log(Level.INFO, "Default Client Name. Setting to " + getName());
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		saveConfig();
		
	}
	
	
	public void loadConfig() {
		
		setIP(config.getString("Settings.IP", getIP()));
		setPort(config.getInteger("Settings.Port", getPort()));
		setHeartBeat(config.getInteger("Settings.Heartbeat", getHeartBeat()));
		setName(config.getString("Settings.Name", getName()));
		setPassword(config.getString("Settings.Password", getPassword()));
		setDebug(config.getBoolean("Settings.Debug", sendDebug()));
		setDataRemove(config.getBoolean("Settings.DataRemoval", isDataRemoved()));
		setLanguage(config.getString("Settings.Language", getLanauge()));

	}
	
	public void saveConfig() {
		
		config.set("Settings.IP", getIP());
		config.set("Settings.Port", getPort());
		config.set("Settings.Heartbeat", getHeartBeat());
		config.set("Settings.Name", getName());
		config.set("Settings.Password", getPassword());
		config.set("Settings.Debug", sendDebug());
		config.set("Settings.DataRemoval", isDataRemoved());
		config.set("Settings.Language", getLanauge());
		config.save();
		
	}
	
	public String getIP() {
		
		return this.ip;
		
	}
	
	public void setIP(String ip) {
		
		this.ip = ip;
		
	}
	
	public Integer getPort() {
		
		return this.port;
		
	}
	
	public void setPort(Integer port) {
		
		this.port = port;
		
	}
	
	public Integer getHeartBeat() {
		
		return this.heartbeat;
		
	}
	
	public void setHeartBeat(Integer heartbeat) {
		
		this.heartbeat = heartbeat;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public void setName(String name) {
		
		this.name = name;
		
	}
	
	public String getPassword() {
		
		return this.pass;
		
	}
	
	public void setPassword(String pass) {
		
		this.pass = pass;
		
	}
	
	public Boolean sendDebug() {
		
		return this.debug;
		
	}
	
	public void setDebug(Boolean debug) {
		
		this.debug = debug;
		
	}
	
	public Boolean isDataRemoved() {
		
		return this.removedata;
		
	}

	public void setDataRemove(Boolean removedata) {
		
		this.removedata = removedata;
		
	}
	
	public String getLanauge() {
		
		return this.lang;
		
	}
	
	public void setLanguage(String lang) {
		
		this.lang = lang;
		
	}
	
}
