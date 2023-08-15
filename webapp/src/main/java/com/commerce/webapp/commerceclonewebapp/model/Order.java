package com.commerce.webapp.commerceclonewebapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToOne(mappedBy = "order_detail_id")
	private OrderDetail orderDetail;

	@ManyToOne
	@JoinColumn(name = "shipment_detail_id")
	private ShipmentDetail shipmentDetail;

	@OneToOne
	@JoinColumn(name = "payment_id")
	private PaymentInformation paymentInformation;

	@JoinColumn(name = "supplier_id")
	private Supplier supplier;
}
