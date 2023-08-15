package com.commerce.webapp.commerceclonewebapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	@OneToOne
	@JoinColumn(name = "product_description")
	private ProductDescription productDescription;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	private Double pricePerUnit;

	private Integer units;
}
