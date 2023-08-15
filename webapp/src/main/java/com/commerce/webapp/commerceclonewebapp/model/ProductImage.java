package com.commerce.webapp.commerceclonewebapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ProductImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private byte[] image;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_description")
	private ProductDescription productDescription;
}
