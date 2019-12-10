package com.frankokafor.food_vendor.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "password_reset")
public class PasswordReset implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long Id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private UserEntity userDetails;

	@Column(nullable = false)
	private String token;
}
