package com.commerce.webapp.commerceclonewebapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderDetailId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;

	private Integer unit;

	private Double perUnitPrice;

	@JoinColumn(name = "product_id")
	private Product product;
}
