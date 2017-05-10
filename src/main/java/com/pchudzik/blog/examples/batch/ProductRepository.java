package com.pchudzik.blog.examples.batch;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

interface ProductRepository extends Repository<Product, Long> {
	@Query("from Product order by id")
	Stream<Product> findProducts();
}
