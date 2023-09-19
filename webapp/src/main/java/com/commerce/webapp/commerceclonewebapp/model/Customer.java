package com.commerce.webapp.commerceclonewebapp.model;

import flexjson.JSON;
import flexjson.JSONDeserializer;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer" , uniqueConstraints = {
		@UniqueConstraint(columnNames = "email")
})
public class Customer implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

//	@Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")
	private String email;

	private String password;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "is_Account_NonExpired")
	private boolean isAccountNonExpired;
	@Column(name = "is_Account_NonLocked")
	private boolean isAccountNonLocked;
	@Column(name = "is_Credentials_NonExpired")
	private boolean isCredentialsNonExpired;
	@Column(name = "is_Enabled")
	private boolean isEnabled;


	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority>authorityList = List.of( new SimpleGrantedAuthority("ROLE_User"));
		return authorityList;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JSON(include = false)
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		isAccountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		isAccountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		isCredentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public static Customer fromJsonToCustomer(String json){
		return new JSONDeserializer<Customer>().deserialize(json,Customer.class);
	}

}
