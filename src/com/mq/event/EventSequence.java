package com.mq.event;

public class EventSequence {

	static class Sequence {
		static int value;
	}

	public static int getSequence() {
		synchronized (Sequence.class) {
			return 1 + Sequence.value++;
		}
	}

}
