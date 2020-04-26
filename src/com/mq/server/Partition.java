package com.mq.server;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.mq.event.Event;

public class Partition {

	private Queue<Event> queue;
	private int capacity;
	private final static int DEFAULT = 1000;

	public Partition() {
		this(DEFAULT);
	}

	public Partition(int capacity) {
		this.capacity = capacity;
		this.queue = new ArrayBlockingQueue<>(capacity);
	}

	public boolean add(Event e) {
		if (hasMoreSpace()) {
			queue.add(e);
		}
		return false;
	}

	public Event poll() {
		return queue.poll();
	}

	public boolean hasMoreSpace() {
		return queue.size() < capacity;
	}

}
