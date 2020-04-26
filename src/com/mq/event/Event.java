package com.mq.event;

public class Event {

	private int id;
	private Object data;

	public Event(Object data) {
		this.id = EventSequence.getSequence();
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public Object getData() {
		return data;
	}

}
