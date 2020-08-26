package com.fuzzoland.CommandSyncServer;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {
	
	@EventHandler
	public void onServerConnected(ServerConnectedEvent event) {
		
		ProxiedPlayer player = event.getPlayer();
		
		if(CSS.getInstance().getPQ().containsKey(player.getName())) {
			
			new CommandThread(player);
			
		}
		
	}
	
}