package com.frankokafor.food_vendor.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Basic(optional = false)
	@Column(name = "id")
	private long id;

	@Size(max = 50)
	@Column(name = "name")
	private String name;

	@Size(max = 50)
	@Column(name = "ordered_by")
	private String orderedBy;

	@Column(name = "total_amount")
	private double totalAmount;
	
	@Column(name = "payment_status",nullable = false)
	private Boolean paymentStatus = false;

	@Column(name = "location")
	private String location;

	@Column(name = "date_created", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	@Column(name = "payment_type")
	private String paymentType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity userDetails;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_edited", nullable = true)
	private Date dateEdited;

	@Column(name = "edited_by", nullable = true)
	private long editedBy;

	@Column(name = "created_by", nullable = true)
	private long createdBy;

	@JoinTable(name = "food_orders", joinColumns = {
			@JoinColumn(name = "order_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "food_id", referencedColumnName = "id") })
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Food> foodList;

	@JoinTable(name = "drink_orders", joinColumns = {
			@JoinColumn(name = "order_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "food_id", referencedColumnName = "id") })
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Drinks> drinkList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

	public Date getDateEdited() {
		return dateEdited;
	}

	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}

	public List<Food> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<Food> foodList) {
		this.foodList = foodList;
	}

	public List<Drinks> getDrinkList() {
		return drinkList;
	}

	public void setDrinkList(List<Drinks> drinkList) {
		this.drinkList = drinkList;
	}

	public long getEditedBy() {
		return editedBy;
	}

	public void setEditedBy(long editedBy) {
		this.editedBy = editedBy;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public String getLocation() {
		return location;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public Boolean getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

}
