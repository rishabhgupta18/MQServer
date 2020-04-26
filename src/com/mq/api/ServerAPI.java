package com.mq.api;

import java.util.List;

import com.mq.keeper.Zookeeper;
import com.mq.producer.ProducerRecord;
import com.mq.server.Broker;
import com.mq.topic.Topic;

public class ServerAPI {

	private static Zookeeper keeper;
	private TopicAPI topicAPI;

	static {
		keeper = new Zookeeper();
		keeper.start();

	}

	public ServerAPI() {
		topicAPI = TopicAPI.getInstance();
	}

	public static ServerAPI getInstance() {
		return new ServerAPI();
	}

	public boolean assignBroker(Topic topic) {
		if (topicAPI.contains(topic)) {
			return keeper.assignBroker(topic);
		}
		throw new RuntimeException("Topic is not registered");
	}

	public boolean unAssignBroker(Topic topic) {
		if (topicAPI.contains(topic)) {
			return keeper.unAssignBroker(topic);
		}
		throw new RuntimeException("Topic is not registered");

	}

	public boolean registerTopic(Topic t) {
		return topicAPI.add(t);
	}

	public boolean unRegisterTopic(Topic t) {
		return topicAPI.remove(t);
	}

	public Broker getBroker(Topic t) {
		return keeper.getBroker(t);
	}

	public void addBroker(Broker broker) {
		keeper.addBroker(broker);
	}

	public void post(List<ProducerRecord> records) {
		for (ProducerRecord record : records) {
			Topic t = record.getTopic();
			Broker b = keeper.getBroker(t);
			b.add(record);

		}
	}
}
