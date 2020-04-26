package com.mq.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.mq.event.Event;
import com.mq.logger.Logger;
import com.mq.producer.ProducerRecord;
import com.mq.topic.Topic;

public class Broker extends Thread {

	private Queue<ProducerRecord> q;
	private Map<Topic, List<Partition>> tp;
	private static Logger log = new Logger(Broker.class);
	private boolean stop;

	public Broker() {
		tp = new HashMap<>();
		q = new LinkedList<>();
	}

	public boolean add(Topic t, int partition) {
		if (tp.containsKey(t)) {
			return false;
		}

		tp.put(t, new ArrayList<>(partition));
		return true;
	}

	public void close() {
		stop = true;
	}

	public boolean remove(Topic t) {
		if (!tp.containsKey(t)) {
			return false;
		}

		tp.remove(t);
		return true;
	}

	public void add(ProducerRecord record) {
		q.add(record);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(super.getId());
	}

	@Override
	public void run() {

		log.info("Broker ID[" + hashCode() + "]" + " started");
		while (!stop) {

			while (q.isEmpty() || !interrupted()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
			}

			while (!q.isEmpty()) {

				ProducerRecord record = q.poll();
				Topic t = record.getTopic();
				List<Partition> partitions = tp.get(t);

				int customHash = record.getCustomHashKey() != null ? record.getCustomHashKey() : t.hashCode();
				Integer partitionNumber = record.getPartitionNumber();

				if (partitionNumber == null || partitionNumber < 0 || partitionNumber >= partitions.size()) {
					partitionNumber = customHash % partitions.size();
				}

				Event e = new Event(record.getData());
				partitions.get(partitionNumber).add(e);
			}

		}
		log.info("Broker ID[" + hashCode() + "]" + " stopped");
	}

}
