package com.pchudzik.blog.examples.batch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class DataInsertTest extends Specification {
	@Autowired
	EntityManager entityManager

	@Autowired
	DataInsert dataInsert

	def "should insert all products"() {
		given:
		final productsToInsert = (1..100).collect { index -> new Product("product " + index, index) }

		when:
		dataInsert.insertProducts(productsToInsert)

		then:
		productsToInsert.size() as Long == entityManager
				.createQuery("select count(id) from Product", Long.class)
				.getSingleResult()
	}
}
