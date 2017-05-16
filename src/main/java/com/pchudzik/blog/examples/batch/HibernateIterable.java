package com.pchudzik.blog.examples.batch;

import lombok.RequiredArgsConstructor;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

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

		private Object[] nextRow;

		ProductIterator() {
			final Session session = entityManager.unwrap(Session.class);

			this.scrollableResults = session
					.createQuery("from Product order by id")
					.scroll(ScrollMode.FORWARD_ONLY);
		}

		@Override
		public boolean hasNext() {
			if (!hasNextRow()) {
				return goToNextRow();
			}

			return hasNextRow();
		}

		@Override
		public Product next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more results");
			}

			try {
				return (Product) nextRow[0];
			} finally {
				goToNextRow();
			}
		}

		@Override
		public void close() throws IOException {
			scrollableResults.close();
		}

		private boolean goToNextRow() {
			scrollableResults.next();
			nextRow = scrollableResults.get();

			return hasNextRow();
		}

		private boolean hasNextRow() {
			return nextRow != null;
		}
	}
}
