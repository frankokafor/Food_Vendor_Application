package com.frankokafor.food_vendor.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "chat_message")
public class ChatMessages {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "chat_id", unique = true, nullable = false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	@ManyToOne
	@JoinColumn(name = "sender")
	private UserEntity sender;

	@Column(name = "type")
	private String type;

	@Column(name = "message")
	private String message;

	public ChatMessages(Date time, UserEntity sender, String message) {
		super();
		this.time = time;
		this.sender = sender;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public UserEntity getSender() {
		return sender;
	}

	public void setSender(UserEntity sender) {
		this.sender = sender;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
