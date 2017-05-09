package com.pchudzik.blog.examples.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
class JpaIterable implements Iterable<Product> {
	private final EntityManager entityManager;

	@Override
	public Iterator<Product> iterator() {
		return iterator(100);
	}

	public Iterator<Product> iterator(int batchSize) {
		return new ProductIterator(batchSize);
	}

	private class ProductIterator implements Iterator<Product> {
		private final int pageSize;

		private int firstResult = 0;
		private Iterator<Product> productsIterator;

		ProductIterator(int pageSize) {
			this.pageSize = pageSize;
		}

		@Override
		public boolean hasNext() {
			if(productsIterator == null || !productsIterator.hasNext()) {
				loadNextPage();
			}

			return productsIterator.hasNext();
		}

		private void loadNextPage() {
			final List<Product> productsPage = entityManager
					.createQuery("from Product order by id", Product.class)
					.setFirstResult(firstResult)
					.setMaxResults(pageSize)
					.getResultList();

			firstResult += productsPage.size();
			productsIterator = productsPage.iterator();
		}

		@Override
		public Product next() {
			if(!hasNext()) {
				throw new NoSuchElementException("No more elements");
			}
			return productsIterator.next();
		}
	}
}
