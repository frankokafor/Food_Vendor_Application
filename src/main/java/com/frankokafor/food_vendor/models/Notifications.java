package com.frankokafor.food_vendor.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mac
 */

@Entity
@Table(name = "notifications")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Notifications.findAll", query = "SELECT n FROM Notifications n"),
		@NamedQuery(name = "Notifications.findById", query = "SELECT n FROM Notifications n WHERE n.id = :id"),
		@NamedQuery(name = "Notifications.findByDateCreated", query = "SELECT n FROM Notifications n WHERE n.dateCreated = :dateCreated"),
		@NamedQuery(name = "Notifications.findByMessage", query = "SELECT n FROM Notifications n WHERE n.message = :message"),
		@NamedQuery(name = "Notifications.findByReadStatus", query = "SELECT n FROM Notifications n WHERE n.readStatus = :readStatus"),
		@NamedQuery(name = "Notifications.findByType", query = "SELECT n FROM Notifications n WHERE n.type = :type"),
		@NamedQuery(name = "Notifications.findByDateUpdated", query = "SELECT n FROM Notifications n WHERE n.dateUpdated = :dateUpdated"),
		@NamedQuery(name = "Notifications.findByUrl", query = "SELECT n FROM Notifications n WHERE n.url = :url") })
public class Notifications implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Basic(optional = false)
	@Column(name = "id")
	private long id;

	@Column(name = "date_created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	@Size(max = 255)
	@Column(name = "message")
	private String message;

	@Column(name = "read_status")
	private Boolean readStatus = false;

	@Size(max = 255)
	@Column(name = "type")
	private String type;

	@Column(name = "date_updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateUpdated;

	@Size(max = 255)
	@Column(name = "url")
	private String url;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserEntity userDetails;

	public Notifications() {
	}

	public Notifications(UserEntity userDetails, String type, String message, Date created) {
		super();
		this.userDetails = userDetails;
		this.type = type;
		this.message = message;
		this.dateCreated = created;
		this.readStatus = false;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(Boolean readStatus) {
		this.readStatus = readStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

}
