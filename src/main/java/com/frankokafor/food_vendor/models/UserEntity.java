package com.frankokafor.food_vendor.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 8523746730771191759L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(unique = true, nullable = false)
	private long Id;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Column(nullable = false, length = 120)
	private String email;
	
	@Column(nullable = false, length = 120)
	private String username;

	@Column(nullable = true, length = 300, name = "photo_url")
	private String photoUrl;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String encryptedPassword;

	@Column(nullable = true, length = 300)
	private String address;

	@Column(nullable = true, length = 300)
	private String emailVerificationToken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_edited", nullable = true)
	private Date dateEdited;

	@Column(name = "edited_by", nullable = true)
	private long editedBy;

	@Column(name = "last_login", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;

	@Column(name = "date_created", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	@Column(nullable = false)
	private Boolean emailVerificationStatus = false;

	@Column(name = "phone_number", nullable = true, length = 12)
	private String phoneNumber;

	@OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL)
	private PasswordReset reset;

	@JoinTable(name = "user_roles", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", referencedColumnName = "id") })
	@ManyToOne
	private Roles role;

	@OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Order> orderList;

	@OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Notifications> notifications;
}
