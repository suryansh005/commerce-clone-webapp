package com.commerce.webapp.commerceclonewebapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	private String receiverName;

	private String apartmentNumber;

	@Length(max = 10)
	private String contactNumber;

	private String street;

	private String landMark;

	private String city;

	private String state;

	@Length(max =  6)
	private Integer pinCode;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	private boolean defaultAddress;
}
