package com.mq.keeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mq.logger.Logger;
import com.mq.server.Broker;
import com.mq.topic.Topic;

public class Zookeeper extends Thread {

	private Set<Broker> brokers;
	List<Broker> aliveBrokersList;
	private Map<Broker, Integer> aliveBrokers;
	private Map<Topic, Broker> tb;
	private static Logger log = new Logger(Zookeeper.class);

	public Zookeeper() {
		brokers = new HashSet<>();
		aliveBrokersList = new ArrayList<>();
		aliveBrokers = new HashMap<>();
		tb = new HashMap<>();
	}

	public void addBroker(Broker b) {
		if (!brokers.contains(b)) {
			log.info("Broker ID[" + b.hashCode() + "]" + " added");
			brokers.add(b);
		}
		synchronized (aliveBrokersList) {
			if (b.isAlive()) {
				boolean isAdded = aliveBrokersList.add(b);
				if (isAdded) {
					log.info("Broker ID[" + b.hashCode() + "]" + " added to running queue");
					aliveBrokers.put(b, aliveBrokersList.size() - 1);
				}
			}
		}
	}

	public boolean assignBroker(Topic topic) {
		if (tb.containsKey(topic))
			return false;
		Broker b = aliveBrokersList.get(topic.getId() % aliveBrokersList.size());
		if (b == null)
			throw new RuntimeException("No Broker is available to server");
		log.info("Broker ID[" + b.hashCode() + "]" + " is assined to Topic [" + topic.getKey() + "]");
		b.add(topic, 1);
		tb.put(topic, b);
		return true;

	}

	public boolean unAssignBroker(Topic topic) {
		if (!tb.containsKey(topic))
			return false;
		Broker b = tb.get(topic);
		b.remove(topic);
		tb.remove(topic);
		log.info("Topic [" + topic.getKey() + "]" + " is removed from Broker ID[" + b.hashCode() + "]");
		return true;

	}

	public Broker getBroker(Topic t) {
		return tb.get(t);
	}

	public boolean isAlive(Broker b) {
		return aliveBrokers.containsKey(b);
	}

	@Override
	public void run() {
		while (true) {

			try {
				for (Broker b : brokers) {
					synchronized (aliveBrokersList) {
						if (b.isAlive()) {
							if (!aliveBrokers.containsKey(b)) {
								aliveBrokersList.add(b);
								aliveBrokers.put(b, aliveBrokersList.size() - 1);
							}
						} else {
							int index = aliveBrokers.remove(b);
							Broker lastBroker = aliveBrokersList.get(aliveBrokersList.size() - 1);
							aliveBrokersList.set(index, lastBroker);
							aliveBrokers.put(lastBroker, index);
						}
					}

				}
			} catch (Exception e) {
				System.err.println("Zookeeper HeartBeat exception ::: " + e.getMessage());
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {

			}

		}
	}

}
