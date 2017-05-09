package com.pchudzik.blog.examples.batch;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Product {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private BigDecimal price;

	public Product(String name, double price) {
		this.name = name;
		this.price = new BigDecimal(price);
	}
}
