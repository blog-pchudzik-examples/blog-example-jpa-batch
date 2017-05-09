package com.pchudzik.blog.examples.batch;

import lombok.RequiredArgsConstructor;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
class HibernateIterable implements Iterable<Product> {
	private final EntityManager entityManager;

	@Override
	public ProductIterator iterator() {
		return new ProductIterator();
	}

	private class ProductIterator implements Iterator<Product>, Closeable {
		private final ScrollableResults scrollableResults;

		ProductIterator() {
			final Session session = entityManager.unwrap(Session.class);

			this.scrollableResults = session
					.createQuery("from Product order by id")
					.scroll(ScrollMode.FORWARD_ONLY);
		}

		@Override
		public boolean hasNext() {
			return scrollableResults.next();
		}

		@Override
		public Product next() {
			final Object [] productRow = scrollableResults.get();

			if(productRow == null) {
				throw new NoSuchElementException("No more results");
			}

			return (Product) productRow[0];
		}

		@Override
		public void close() throws IOException {
			scrollableResults.close();
		}
	}
}
