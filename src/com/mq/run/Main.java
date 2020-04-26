package com.mq.run;

import com.mq.api.ServerAPI;
import com.mq.listner.Listener;
import com.mq.server.Broker;
import com.mq.topic.Topic;

public class Main {

	public static void main(String[] args) throws Exception {

		ServerAPI api = ServerAPI.getInstance();
		Broker b1 = new Broker();
		b1.start();
		Broker b2 = new Broker();
		b2.start();
		api.addBroker(b1);
		api.addBroker(b2);
		Topic t1 = new Topic("Test_t1");
		Topic t2 = new Topic("Test_t2");
		api.registerTopic(t1);
		api.registerTopic(t2);
		api.assignBroker(t1);
		api.assignBroker(t2);

		Listener app = new Listener(null);
		app.start();

	}

}
