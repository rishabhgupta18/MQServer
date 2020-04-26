package com.mq.listner;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.function.Predicate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mq.api.ServerAPI;
import com.mq.logger.Logger;
import com.mq.monitor.Monitoring;
import com.mq.producer.ProducerRecord;
import com.mq.serialize.Deserialize;
import com.mq.serialize.IDeserializtion;

public class ConnectionListener extends Monitoring implements Runnable {

	protected Socket clientSocket = null;
	private static Logger log = new Logger(ConnectionListener.class);
	private final static int CONNECTION_TIMEOUT_SECONDS = 120;
	private boolean stop = false;
	private ServerAPI api;

	public ConnectionListener(Socket clientSocket) {
		super(ConnectionListener.class.getSimpleName(),
				(Predicate<Integer>) (Integer t) -> t > CONNECTION_TIMEOUT_SECONDS);
		this.clientSocket = clientSocket;
		this.api = ServerAPI.getInstance();
	}

	@Override
	public void operation() {
		stop();
		this.stop = true;
		log.info("No new Request from client in " + getTimer().getTime() + " secounds. Closing Connection");
	}

	@Override
	public void close() {
		try {
			super.close();
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			log.error("Error while closing connection Exception[" + e.getMessage() + "]");
		}
	}

	@Override
	public void run() {

		IDeserializtion deserialize = new Deserialize();
		log.info("A new connection established. IP[" + clientSocket.getInetAddress().getHostAddress() + "] port["
				+ clientSocket.getPort() + "]");
		DataInputStream is = null;
		while (!stop) {

			while (is == null) {
				log.info("No message from Client");
				try {
					is = new DataInputStream(clientSocket.getInputStream());
				} catch (IOException e1) {
				}

				try {
					if (is == null || is.available() == 0) {
						Thread.sleep(1000);
						is = null;
					}
				} catch (IOException | InterruptedException e) {
				}
			}

			try {
				log.info("New message from Client");
				String data = is.readUTF();
				List<ProducerRecord> records = deserialize.deserializeString(data, new TypeReference<List<ProducerRecord>>() {
				});
				api.post(records);
			} catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				if (is != null) {
					try {
						is.close();
						is = null;
					} catch (IOException e) {
						log.error("Error while closing the stream ::: " + e.getMessage());
					}
				}
			}
		}

	}

}
