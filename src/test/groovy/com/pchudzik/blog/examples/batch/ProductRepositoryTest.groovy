package com.pchudzik.blog.examples.batch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.transaction.Transactional

import static java.util.stream.Collectors.toList

@SpringBootTest
@Transactional
class ProductRepositoryTest extends Specification {
	@Autowired
	EntityManager entityManager

	@Autowired
	ProductRepository productrepository

	def "should find all products exactly one time"() {
		given:
		final p1 = entityManager.merge(new Product("a", 1.0))
		final p2 = entityManager.merge(new Product("b", 2.0))
		final p3 = entityManager.merge(new Product("c", 3.0))

		when:
		final stream = productrepository.findProducts()

		then:
		[p1.id, p2.id, p3.id] == stream.map({ product -> product.id }).collect(toList())
	}
}
