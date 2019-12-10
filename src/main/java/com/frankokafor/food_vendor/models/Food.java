package com.frankokafor.food_vendor.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "food")
public class Food implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Basic(optional = false)
	@Column(name = "id")
	private long id;

	@Size(max = 50)
	@Column(name = "name")
	private String name;

	@Size(max = 255)
	@Column(name = "photo_url")
	private String photoUrl;

	@Size(max = 255)
	@Column(name = "description")
	private String description;

	@Column(name = "amount")
	private double amount;

	@Column(name = "date_created", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_edited", nullable = true)
	private Date dateEdited;

	@Column(name = "edited_by", nullable = true)
	private long editedBy;

	@Column(name = "created_by", nullable = true)
	private long createdBy;

	@ManyToMany(mappedBy = "foodList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Order> orderList;
}
