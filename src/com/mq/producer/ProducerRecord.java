package com.mq.producer;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mq.topic.Topic;

public class ProducerRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty("topic")
	private Topic topic;
	@JsonProperty("customHashKey")
	private int customHashKey;
	@JsonProperty("partitionNumber")
	@JsonIgnore
	private Integer partitionNumber;
	@JsonProperty("data")
	private String data;

	@JsonCreator
	public ProducerRecord(@JsonProperty("topic") Topic topic) {
		this(topic, null);
	}

	public ProducerRecord(Topic topic, Integer partitionNumber) {
		this(topic, topic.getId(), partitionNumber);
	}

	public ProducerRecord(Topic topic, Integer customHashKey, Integer partitionNumber) {
		this.topic = topic;
		this.customHashKey = customHashKey;
		this.partitionNumber = partitionNumber;
	}
	
	@JsonProperty("customHashKey")
	public Integer getCustomHashKey() {
		return customHashKey;
	}

	@JsonProperty("customHashKey")
	public void setCustomHashKey(int customHashKey) {
		this.customHashKey = customHashKey;
	}

	@JsonProperty("partitionNumber")
	public Integer getPartitionNumber() {
		return partitionNumber;
	}

	@JsonProperty("partitionNumber")
	public void setPartitionNumber(int partitionNumber) {
		this.partitionNumber = partitionNumber;
	}

	@JsonProperty("topic")
	public Topic getTopic() {
		return topic;
	}

	@JsonProperty("data")
	public String getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(String data) {
		this.data = data;
	}
	
}
