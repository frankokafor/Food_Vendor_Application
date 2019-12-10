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

import lombok.Data;

@Data
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
}
