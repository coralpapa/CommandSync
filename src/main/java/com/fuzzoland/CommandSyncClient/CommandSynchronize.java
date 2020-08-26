package com.fuzzoland.CommandSyncClient;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSynchronize implements CommandExecutor {

	private CSC plugin;
	
	public CommandSynchronize(CSC plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("sync.use")) {
			
			if(args.length >= 0) {
				
				if(args.length <= 2) {
					
					sender.sendMessage(Locale.getInstance().getString("HelpAuthors"));
					
					if(args.length >= 1) {
						
						if(args[0].equalsIgnoreCase("console")){
							
							sender.sendMessage(Locale.getInstance().getString("HelpCommands8"));
							sender.sendMessage(Locale.getInstance().getString("HelpCommands7"));
							sender.sendMessage(Locale.getInstance().getString("HelpCommands6"));
							
						} else if(args[0].equalsIgnoreCase("player")) {
							
							sender.sendMessage(Locale.getInstance().getString("HelpCommands5"));
							sender.sendMessage(Locale.getInstance().getString("HelpCommands4"));
							
						} else {
							
							sender.sendMessage(Locale.getInstance().getString("HelpCommands9"));
							
						}
						
					} else {
						
						sender.sendMessage(Locale.getInstance().getString("HelpCommands3"));
						sender.sendMessage(Locale.getInstance().getString("HelpCommands2"));
						sender.sendMessage(Locale.getInstance().getString("HelpCommands1"));
						
					}
					
					sender.sendMessage(Locale.getInstance().getString("HelpLink"));
					
				} else if(args.length >= 3) {
					
					if(args[0].equalsIgnoreCase("console") || args[0].equalsIgnoreCase("player")) {
						
					    String[] newArgs = new String[3];
					    newArgs[0] = args[0];
					    newArgs[1] = args[1];
					    StringBuilder sb = new StringBuilder();
					    
					    for(int i = 2; i < args.length; i++) {
					    	
					        sb.append(args[i]);
					        
					        if(i < args.length - 1) {
					        	
					            sb.append("+");
					            
					        }
					        
					    }
					    
					    newArgs[2] = sb.toString();
					    
						if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("bungee")) {
							
							makeData(newArgs, false, sender);
							
						} else {
							
						    makeData(newArgs, true, sender);
						    
						}
						
					} else {
						
						sender.sendMessage(Locale.getInstance().getString("HelpCommands9"));
						
					}
					
				}
				
			}
			
		} else {
			
			sender.sendMessage(Locale.getInstance().getString("NoPerm"));
			
		}
		
		return true;
		
	}
	
	private void makeData(String[] args, Boolean single, CommandSender sender) {
		
		String data;
		String message;
		
		if(args[0].equalsIgnoreCase("console")) {
			
			if(args[1].equalsIgnoreCase("all")) {
				
				message = Locale.getInstance().getString("SyncingCommand", args[2].replaceAll("\\+", " "),  Locale.getInstance().getString("SyncConsoleAll"));
				
			} else {
				
				message = Locale.getInstance().getString("SyncingCommand", args[2].replaceAll("\\+", " "),  Locale.getInstance().getString("SyncConsole", args[1]));
				
			}
			
		} else if(args[0].equalsIgnoreCase("bungee")) {
			
			message = Locale.getInstance().getString("SyncingCommand", args[2].replaceAll("\\+", " "),  Locale.getInstance().getString("SyncConsole", args[1]));
			
		} else {
			
			if(args[1].equalsIgnoreCase("all")) {
				
				message = Locale.getInstance().getString("SyncingCommand", args[2].replaceAll("\\+", " "),  Locale.getInstance().getString("SyncPlayerAll"));
				
			} else {
				
				message = Locale.getInstance().getString("SyncingCommand", args[2].replaceAll("\\+", " "),  Locale.getInstance().getString("SyncPlayer", args[1]));
				
			}
			
		}
		
		if(single) {
			
		    data = args[0].toLowerCase() + CSC.getInstance().getSpacer() + "single" + CSC.getInstance().getSpacer() + args[2] + CSC.getInstance().getSpacer() + args[1];
		    
		} else {
			
		    data = args[0].toLowerCase() + CSC.getInstance().getSpacer() + args[1].toLowerCase() + CSC.getInstance().getSpacer() + args[2];
		    
		}
		
		CSC.getInstance().getOQ().add(data);
		sender.sendMessage(message);
		
	}
	
}