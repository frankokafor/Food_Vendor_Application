package com.frankokafor.food_vendor.models;



import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "roles")
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r")
    , @NamedQuery(name = "Roles.findById", query = "SELECT r FROM Roles r WHERE r.id = :id")
    , @NamedQuery(name = "Roles.findByName", query = "SELECT r FROM Roles r WHERE r.name = :name")
    , @NamedQuery(name = "Roles.findByPrivelege", query = "SELECT r FROM Roles r WHERE r.privelege = :privelege")})
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private long id;
    
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    
    @Size(max = 100)
    @Column(name = "privelege")
    private String privelege;
    
	@OneToMany(mappedBy = "role", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	private List<UserEntity> userDetails;

    public Roles() {
    }

	public long getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrivelege() {
		return privelege;
	}

	public void setPrivelege(String privelege) {
		this.privelege = privelege;
	}

	public List<UserEntity> getUserDetails() {
		return userDetails;
	}
	@XmlTransient
	public void setUserDetails(List<UserEntity> userDetails) {
		this.userDetails = userDetails;
	}

	public void setId(long id) {
		this.id = id;
	}

}