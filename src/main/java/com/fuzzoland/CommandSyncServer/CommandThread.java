package com.fuzzoland.CommandSyncServer;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class CommandThread implements Runnable {

  private CSS plugin;
  private ProxiedPlayer player;
  private String name;
  private List<String> commands;

  public CommandThread(CSS plugin, ProxiedPlayer player) {
    this.plugin = plugin;
    this.player = player;
    this.name = player.getName();
    this.commands = plugin.pq.get(name);
  }

  public void run() {
    try {
      for (String command : commands) {
        player.chat(command);
        plugin.debugger.debug("Ran command " + command + " for player " + name + ".");
      }
      plugin.pq.remove(name);
      return;
    } catch (IllegalStateException e1) {
      e1.printStackTrace();
    }
  }
}
