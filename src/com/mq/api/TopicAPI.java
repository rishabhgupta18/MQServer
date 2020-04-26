package com.mq.api;

import java.util.List;

import com.mq.logger.Logger;
import com.mq.topic.Topic;
import com.mq.topic.TopicMeta;

public class TopicAPI {

	private static final int DEFAULT_LIMIT = 200;
	private static Logger log = new Logger(TopicAPI.class);

	public List<Topic> get(int page) {
		int start = page == 1 ? 0 : (page - 1) * DEFAULT_LIMIT;
		int end = start + DEFAULT_LIMIT;
		return TopicMeta.getInstance().get(start, end);
	}
	
	public boolean contains(Topic t) {
		return TopicMeta.getInstance().contains(t);
	}

	public boolean add(Topic t) {
		boolean status = TopicMeta.getInstance().add(t);
		if (status) {
			log.info("Topic [" + t.getKey() + "]" + " added");
			ServerAPI.getInstance().assignBroker(t);
		}
		return status;
	}

	public boolean remove(Topic t) {
		boolean status = TopicMeta.getInstance().remove(t);
		if (status) {
			log.info("Topic [" + t.getKey() + "]" + " removed");
			ServerAPI.getInstance().unAssignBroker(t);
		}
		return status;
	}

	public static TopicAPI getInstance() {
		return new TopicAPI();
	}

}
