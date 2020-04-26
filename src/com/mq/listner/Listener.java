package com.mq.listner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mq.logger.Logger;

public class Listener {

	private ServerSocket server;
	private ThreadPoolExecutor connectionPool;
	private static Logger log = new Logger(Listener.class);

	public Listener(String ipAddress) throws Exception {
		log.info("Opening a socket connection");
		if (ipAddress != null && !ipAddress.isEmpty())
			this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
		else
			this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());

		log.info("Server Listening at IP[" + getSocketAddress().getHostAddress() + "], Port[" + getPort() + "]");

		connectionPool = new ThreadPoolExecutor(5000, 20000, 120, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.AbortPolicy());
	}

	public void start() throws IOException {
		Socket client = this.server.accept();
		log.info("A new connection request came. IP[" + client.getInetAddress().getHostAddress() + "] port["
				+ client.getPort() + "]");
		ConnectionListener listener = new ConnectionListener(client);
		connectionPool.execute(listener);
	}

	public InetAddress getSocketAddress() {
		return this.server.getInetAddress();
	}

	public int getPort() {
		return this.server.getLocalPort();
	}
}
