package com.commerce.webapp.commerceclonewebapp.model;

import com.commerce.webapp.commerceclonewebapp.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ShipmentDetail {

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long shipmentDetailId;

	private String shipmentVia;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;

	private OrderStatus shipmentStatus;

}
