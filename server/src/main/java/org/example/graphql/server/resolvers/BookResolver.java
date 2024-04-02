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

package org.example.graphql.server.resolvers;

import java.util.List;
import org.example.graphql.server.factories.BookFactory;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.example.graphql.server.services.AuthorPersistenceService;
import org.example.graphql.server.services.BookPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL controller for managing {@link Book} and related {@link Author} data.
 * <p>
 * Provides query and mutation operations for authors, including fetching, creating, and deleting authors and their books. Uses
 * {@link BookPersistenceService} and {@link AuthorPersistenceService} for persistence operations and {@link BookFactory} for author creation.
 * </p>
 * Each method is annotated with either {@link QueryMapping} or {@link MutationMapping} to indicate whether it's a GraphQL query or mutation. The
 * {@link Argument} annotation is used to specify the arguments of the GraphQL query or mutation.
 *
 * @author Alexander Kombeiz
 * @version 1.03
 * @since 04-01-2024
 */
@Controller
public class BookResolver {

  private static final Logger log = LoggerFactory.getLogger(BookResolver.class);

  private final BookPersistenceService bookPersistenceService;

  private final AuthorPersistenceService authorPersistenceService;

  private final BookFactory bookFactory;

  @Autowired
  public BookResolver(BookPersistenceService bookPersistenceService,
      AuthorPersistenceService authorPersistenceService,
      BookFactory bookFactory) {
    this.bookPersistenceService = bookPersistenceService;
    this.authorPersistenceService = authorPersistenceService;
    this.bookFactory = bookFactory;
  }

  @QueryMapping
  public Book bookById(@Argument Long id) {
    log.info("Fetching book with id: {}", id);
    return bookPersistenceService.getById(id);
  }

  @QueryMapping
  public List<Book> books() {
    log.info("Fetching all books");
    return bookPersistenceService.getAll();
  }

  /**
   * Creates a new book with the specified title, published year, and author ID. Establishes a dependency relationship between the {@link Author} and
   * {@link Book} entities. The author, identified by the provided {@code authorId}, is associated with the newly created book.
   */
  @MutationMapping
  public Book createBook(@Argument String title, @Argument int publishedYear,
      @Argument Long authorId) {
    log.info("Creating book with title: {}, publishedYear: {}, and authorId: {}", title,
        publishedYear, authorId);
    Author author = authorPersistenceService.getById(authorId);
    if (author != null) {
      Book newBook = bookFactory.create();
      newBook.setTitle(title);
      newBook.setPublishedYear(publishedYear);
      newBook.setAuthor(author);
      Book savedBook = bookPersistenceService.persist(newBook);
      author.addPublishedBook(savedBook.getId());
      authorPersistenceService.persist(author);
      return savedBook;
    } else {
      log.warn("Author with id {} not found. Cannot create book.", authorId);
      return null;
    }
  }

  /**
   * Deletes a book with the specified ID. Also removes the specified book from the associated {@link Author}'s list of books, ensuring the
   * maintenance of a consistent dependency relationship between authors and books.
   */
  @MutationMapping
  public Boolean deleteBook(@Argument Long id) {
    log.info("Deleting book with id: {}", id);
    Book book = bookPersistenceService.getById(id);
    if (book != null) {
      Author author = book.getAuthor();
      log.info("Removing book with id: {} from author with id: {}", id, author.getId());
      author.removePublishedBook(id);
      authorPersistenceService.persist(author);
      bookPersistenceService.deleteById(id);
      return true;
    } else {
      log.warn("Book with id {} not found.", id);
      return false;
    }
  }
}
