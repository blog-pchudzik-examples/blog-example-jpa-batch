package com.pchudzik.blog.examples.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
class DataInsert {
	private final EntityManager entityManager;

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
