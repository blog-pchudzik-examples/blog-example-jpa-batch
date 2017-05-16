package com.pchudzik.blog.examples.batch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class HibernateIterableTest extends Specification {
	@Autowired
	EntityManager entityManager

	@Autowired
	HibernateIterable hibernateIterable

	def "should detect when no more elements available"() {
		when: "database is empty"
		final iterator = hibernateIterable.iterator()

		then:
		!iterator.hasNext()

		cleanup:
		iterator.close()
	}

	def "should throw exception when no data available and has next called"() {
		given: "database is empty"
		final iterator = hibernateIterable.iterator()

		when:
		iterator.next()

		then:
		thrown NoSuchElementException

		cleanup:
		iterator.close()
	}

	def "should find all products exactly one time"() {
		given:
		final p1 = entityManager.merge(new Product("a", 1.0))
		final p2 = entityManager.merge(new Product("b", 2.0))
		final p3 = entityManager.merge(new Product("c", 3.0))

		when:
		final iterator = hibernateIterable.iterator()

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

		cleanup:
		iterator.close()
	}

	def "hasNext shouldn't go forward"() {
		given:
		final p1 = entityManager.merge(new Product("a", 1.0))
		final p2 = entityManager.merge(new Product("b", 2.0))
		final p3 = entityManager.merge(new Product("c", 3.0))

		when:
		final iterator = hibernateIterable.iterator()

		then:
		(0..3).each { assert iterator.hasNext() }

		and:
		iterator.next().id == p1.id

		cleanup:
		iterator.close()
	}
}
