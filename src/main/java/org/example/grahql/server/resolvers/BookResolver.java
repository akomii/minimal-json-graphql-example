/*
 * MIT License
 *
 * Copyright (c) 2024 Alexander Kombeiz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.grahql.server.resolvers;

import graphql.schema.DataFetcher;
import java.util.ArrayList;
import java.util.List;
import org.example.grahql.server.models.Author;
import org.example.grahql.server.models.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BookResolver {

  private static final Logger log = LoggerFactory.getLogger(BookResolver.class);

  private final List<Book> books = new ArrayList<>();
  private final List<Author> authors = new ArrayList<>();

  public BookResolver() {
    Author author1 = new Author(1, "John", "Doe");
    authors.add(author1);
    books.add(new Book(1, "Book1", 2000, author1));
    Author author2 = new Author(2, "Jane", "Doe");
    authors.add(author2);
    books.add(new Book(2, "Book2", 2001, author2));
  }

  public DataFetcher<Book> getBook() {
    return env -> {
      int id = env.getArgument("id");
      log.info("Fetching book with id: {}", id);
      return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    };
  }

  public DataFetcher<List<Book>> getBooks() {
    return env -> {
      log.info("Fetching all books");
      return books;
    };
  }

  public DataFetcher<Book> createBook() {
    return env -> {
      String title = env.getArgument("title");
      int publishedYear = env.getArgument("publishedYear");
      int authorId = env.getArgument("authorId");
      log.info("Creating book with title: {}, publishedYear: {}, and authorId: {}",
          title, publishedYear, authorId);
      Author author = authors.stream().filter(a -> a.getId() == authorId).findFirst().orElse(null);
      if (author == null) {
        throw new IllegalArgumentException("Author not found");
      }
      Book book = new Book(books.size() + 1, title, publishedYear, author);
      books.add(book);
      return book;
    };
  }
}
