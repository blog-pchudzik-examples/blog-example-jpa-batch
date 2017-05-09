package com.pchudzik.blog.examples.batch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.transaction.Transactional

@Transactional
@SpringBootTest
class JpaIterableTest extends Specification {
	@Autowired
	EntityManager entityManager

	@Autowired
	JpaIterable jpaIterable

	def "should detect when no more elements available"() {
		when: "database is empty"
		final iterator = jpaIterable.iterator()

		then:
		!iterator.hasNext()
	}

	def "should throw exception when no data available and has next called"() {
		given: "database is empty"
		final iterator = jpaIterable.iterator()

		when:
		iterator.next()

		then:
		thrown NoSuchElementException
	}

	def "should find all products exactly one time"() {
		given:
		final p1 = entityManager.merge(new Product("a", 1.0))
		final p2 = entityManager.merge(new Product("b", 2.0))
		final p3 = entityManager.merge(new Product("c", 3.0))

		when:
		final iterator = jpaIterable.iterator(2)

		then:
		iterator.hasNext()
		iterator.next().id == p1.id

		and:
		iterator.hasNext()
		iterator.next().id == p2.id

		and:
		iterator.hasNext()
		iterator.next().id == p3.id

		and:
		!iterator.hasNext()
	}
}
