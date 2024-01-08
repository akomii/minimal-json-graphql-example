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
import org.example.graphql.server.factories.BasicFactory;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.example.graphql.server.services.BasicPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller class for handling GraphQL queries and mutations related to {@link Book} instances.
 * <p>
 * This class provides methods to fetch a book by its ID, fetch all books, create a new book, and
 * delete a book by its ID. It uses a {@link BasicPersistenceService} to interact with the
 * persistence layer and a {@link BasicFactory} to create new {@link Book} instances.
 * <p>
 * Each method is annotated with either {@link QueryMapping} or {@link MutationMapping} to indicate
 * whether it's a GraphQL query or mutation. The {@link Argument} annotation is used to specify the
 * arguments of the GraphQL query or mutation.
 *
 * @author Alexander Kombeiz
 * @version 1.1
 * @since 04-01-2024
 */
@Controller
public class BookResolver {

  private static final Logger log = LoggerFactory.getLogger(BookResolver.class);

  private final BasicPersistenceService basicPersistenceService;

  private final BasicFactory basicFactory;

  /**
   * Constructs a new {@link BookResolver} with the given {@link BasicPersistenceService} and
   * {@link BasicFactory}.
   *
   * @param basicPersistenceService the {@link BasicPersistenceService} to use for interacting with
   *                                the persistence layer.
   * @param basicFactory            the {@link BasicFactory} to use for creating new {@link Book}
   *                                instances.
   */
  @Autowired
  public BookResolver(BasicPersistenceService basicPersistenceService, BasicFactory basicFactory) {
    this.basicPersistenceService = basicPersistenceService;
    this.basicFactory = basicFactory;
  }

  /**
   * Fetches a book by its ID.
   *
   * @param id the ID of the book to fetch.
   * @return the book with the given ID, or null if no such book exists.
   */
  @QueryMapping
  public Book bookById(@Argument Long id) {
    log.info("Fetching book with id: {}", id);
    return basicPersistenceService.getBookById(id);
  }

  /**
   * Fetches all books.
   *
   * @return an iterable of all books.
   */
  @QueryMapping
  public List<Book> books() {
    log.info("Fetching all books");
    return basicPersistenceService.getAllBooks();
  }

  /**
   * Creates a new book.
   *
   * @param title         the title of the book to create.
   * @param publishedYear the year the book was published.
   * @param authorId      the ID of the author of the book to create.
   * @return the created book, or null if the author does not exist.
   */
  @MutationMapping
  public Book createBook(@Argument String title, @Argument int publishedYear,
      @Argument Long authorId) {
    log.info("Creating book with title: {}, publishedYear: {}, and authorId: {}",
        title, publishedYear, authorId);
    Author author = basicPersistenceService.getAuthorById(authorId);
    if (author != null) {
      Book newBook = basicFactory.createBook();
      newBook.setTitle(title);
      newBook.setPublishedYear(publishedYear);
      newBook.setAuthor(author);
      Book savedBook = basicPersistenceService.persistBook(newBook);
      author.getPublishedBookIds().add(savedBook.getId());
      basicPersistenceService.persistAuthor(author);
      return savedBook;
    } else {
      log.warn("Author with id {} not found. Cannot create book.", authorId);
      return null;
    }
  }

  /**
   * Deletes a book by its ID.
   *
   * @param id the ID of the book to delete.
   * @return true if the book was deleted, false otherwise.
   */
  @MutationMapping
  public Boolean deleteBook(@Argument Long id) {
    log.info("Deleting book with id: {}", id);
    Book book = basicPersistenceService.getBookById(id);
    if (book != null) {
      Author author = book.getAuthor();
      if (author != null) {
        log.info("Removing book with id: {} from author with id: {}", id, author.getId());
        author.getPublishedBookIds().remove(id);
        basicPersistenceService.persistAuthor(author);
      }
      basicPersistenceService.deleteBookById(id);
      return true;
    } else {
      log.warn("Book with id {} not found.", id);
      return false;
    }
  }
}
