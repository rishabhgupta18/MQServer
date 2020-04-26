package com.mq.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopicMeta {

	private Map<Topic, Integer> index;
	private List<Topic> topics;

	private static TopicMeta tm;

	private TopicMeta() {
		if (tm != null)
			throw new IllegalAccessError("Instance of TopicMeta already exists");

		this.index = new HashMap<>();
		this.topics = new ArrayList<>();
	}

	public static TopicMeta getInstance() {

		if (tm == null) {
			// Double check for multithreading environment
			synchronized (TopicMeta.class) {
				if (tm == null) {
					tm = new TopicMeta();
				}
			}
		}
		return tm;

	}
	
	public java.util.List<Topic> get(int start, int end){
		
		if(start < 0 || start >= end) {
			throw new IllegalAccessError("index is out of bound");
		}
		end = end > topics.size() ? topics.size() : end;
		List<Topic> topic = new ArrayList<>(end-start+1);
		for(int i = start; i < end; i++) {
			topic.add(topics.get(i));
		}
		return topic;
	}
	
	public boolean contains(Topic t) {
		return index.containsKey(t);
	}

	public boolean add(Topic t) {
		if (contains(t))
			return false;
		topics.add(t);
		index.put(t, topics.size() - 1);
		return true;
	}

	public boolean remove(Topic t) {
		if (!contains(t))
			return false;
		topics.remove(t);
		index.remove(t);
		return true;
	}

}
