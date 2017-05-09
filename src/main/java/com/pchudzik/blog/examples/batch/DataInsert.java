package com.pchudzik.blog.examples.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
class DataInsert {
	@Autowired
	EntityManager entityManager;

	public void insertProducts(Iterable<Product> products) {
		int batch = 0;

		for (Product product : products) {
			entityManager.persist(product);

			batch++;

			if (batch % 100 == 0) {
				flushAndClear();
			}
		}

		flushAndClear();
	}

	private void flushAndClear() {
		entityManager.flush();
		entityManager.clear();
	}
}
