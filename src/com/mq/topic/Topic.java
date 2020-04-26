package com.mq.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Topic implements Cloneable {

	@JsonIgnore
	private int id;
	@JsonProperty("key")
	private String key;

	@JsonCreator
	public Topic(@JsonProperty("key") String key) {
		this.key = key;
		this.id = key.hashCode();
	}
	
	public int getId() {
		return id;
	}

	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {

		boolean isEqual = false;
		if (obj instanceof Topic) {
			Topic t = (Topic) obj;
			isEqual = this.key.equals(t.key);
		}

		return isEqual;
	}

	@Override
	public String toString() {
		return key;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Topic(key);
	}

}
