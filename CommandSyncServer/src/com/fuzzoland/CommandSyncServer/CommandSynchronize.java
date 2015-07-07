package com.fuzzoland.CommandSyncServer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by David on 7/7/2015.
 *
 * @author David
 */
@SuppressWarnings("deprecation")
public class CommandSynchronize extends Command {
    public CommandSynchronize() {super("bsync", "bsync.use", "bungeesync");}

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length >= 0) {
            if(args.length <= 2) {
                sender.sendMessage(ChatColor.BLUE + "CommandSync by YoFuzzy3");
                if(args.length >= 1) {
                    if(args[0].equalsIgnoreCase("console")){
                        sender.sendMessage(ChatColor.GREEN + "/bsync console <server> <command args...>");
                        sender.sendMessage(ChatColor.GREEN + "/bsync console all <command args...>");
                    } else if(args[0].equalsIgnoreCase("player")) {
                        sender.sendMessage(ChatColor.GREEN + "/bsync player <player> <command args...>");
                        sender.sendMessage(ChatColor.GREEN + "/bsync player all <command args...>");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Type /bsync for help.");
                    }
                } else {
                    sender.sendMessage(ChatColor.GREEN + "/bsync console");
                    sender.sendMessage(ChatColor.GREEN + "/bsync player");
                    sender.sendMessage(ChatColor.BLUE + "Type the command for more info.");
                }
                sender.sendMessage(ChatColor.BLUE + "Visit www.spigotmc.org/resources/commandsync.115 for help.");
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
                    if(args[1].equalsIgnoreCase("all")) {
                        makeData(newArgs, false, sender);
                    } else {
                        makeData(newArgs, true, sender);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Type /bsync for help!");
                }
            }
        }
    }

    private void makeData(String[] args, Boolean single, CommandSender sender) {
        String data;
        String message = ChatColor.GREEN + "Syncing command /" + args[2].replaceAll("\\+", " ") + " to " + args[0];
        if(single) {
            data = args[0].toLowerCase() + CSS.spacer + "single" + CSS.spacer + args[2] + CSS.spacer + args[1];
            message = message + " [" + args[1] + "]...";
        } else {
            data = args[0].toLowerCase() + CSS.spacer + args[1].toLowerCase() + CSS.spacer + args[2];
            message = message + " [All]...";
        }
        CSS.oq.add(data);
        sender.sendMessage(message);
    }
}
