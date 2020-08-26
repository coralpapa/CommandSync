package com.fuzzoland.CommandSyncServer;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class CommandThread implements Runnable {

	private ProxiedPlayer player;
	private String name;
	private List<String> commands;
	private ScheduledTask task;

	public CommandThread(ProxiedPlayer player) {
		
		this.player = player;
		this.name = player.getName();
		this.commands = CSS.getInstance().getPQ().get(name);
		this.task = ProxyServer.getInstance().getScheduler().schedule(CSS.getInstance(), this, 0, 10, TimeUnit.SECONDS);
	
	}

	public void run() {
		
		if(!player.isConnected()) {
			
			task.cancel();
			return;
			
		}
		
		try {
			
			for(String command : commands) {
				
				player.chat(command);
				Debugger.getInstance().Log(Locale.getInstance().getString("BungeeRanPlayerSingle", command, name));
				
			}
			
			CSS.getInstance().getPQ().remove(name);
			
			return;
			
		} catch(IllegalStateException e) {
			
			Debugger.getInstance().Log(Level.WARNING, e.getMessage(), e);
			
		}
		
	}
	
}