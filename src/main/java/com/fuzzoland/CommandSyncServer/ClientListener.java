package com.fuzzoland.CommandSyncServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ClientListener implements Runnable {

	private CSS plugin;
	private Integer heartbeat;
	private String pass;

	public ClientListener(CSS plugin, Integer heartbeat, String pass) {
		this.plugin = plugin;
		this.heartbeat = heartbeat;
		this.pass = pass;
	}

	public void run() {
        try {
            this.plugin.getProxy().getScheduler().schedule(this.plugin, new ClientHandler(this.plugin, this.plugin.server.accept(), heartbeat, pass), 0, heartbeat, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
