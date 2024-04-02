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
import org.example.graphql.server.factories.AuthorFactory;
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
 * GraphQL controller for managing {@link Author} and related {@link Book} data.
 * <p>
 * Provides query and mutation operations for authors, including fetching, creating, and deleting authors and their books. Uses
 * {@link AuthorPersistenceService} and {@link BookPersistenceService} for persistence operations and {@link AuthorFactory} for author creation.
 * </p>
 * Each method is annotated with either {@link QueryMapping} or {@link MutationMapping} to indicate whether it's a GraphQL query or mutation. The
 * {@link Argument} annotation is used to specify the arguments of the GraphQL query or mutation.
 *
 * @author Alexander Kombeiz
 * @version 1.03
 * @since 04-01-2024
 */
@Controller
public class AuthorResolver {

  private static final Logger log = LoggerFactory.getLogger(AuthorResolver.class);

  private final AuthorPersistenceService authorPersistenceService;

  private final BookPersistenceService bookPersistenceService;

  private final AuthorFactory authorFactory;

  @Autowired
  public AuthorResolver(AuthorPersistenceService authorPersistenceService,
      BookPersistenceService bookPersistenceService,
      AuthorFactory authorFactory) {
    this.authorPersistenceService = authorPersistenceService;
    this.bookPersistenceService = bookPersistenceService;
    this.authorFactory = authorFactory;
  }

  @QueryMapping
  public Author authorById(@Argument Long id) {
    log.info("Fetching author with id: {}", id);
    return authorPersistenceService.getById(id);
  }

  @QueryMapping
  public List<Author> authors() {
    log.info("Fetching all authors");
    return authorPersistenceService.getAll();
  }

  @MutationMapping
  public Author createAuthor(@Argument String firstName, @Argument String lastName) {
    log.info("Creating author with firstName: {} and lastName: {}", firstName, lastName);
    Author newAuthor = authorFactory.create();
    newAuthor.setFirstName(firstName);
    newAuthor.setLastName(lastName);
    return authorPersistenceService.persist(newAuthor);
  }

  /**
   * Deletes an author by their ID and also deletes all books associated with that author.
   */
  @MutationMapping
  public Boolean deleteAuthor(@Argument Long id) {
    log.info("Deleting author with id: {}", id);
    Author author = authorPersistenceService.getById(id);
    if (author != null) {
      author.getPublishedBookIds().forEach(bookId -> {
        log.info("Deleting book with id: {} associated with author id: {}", bookId, id);
        bookPersistenceService.deleteById(bookId);
      });
      authorPersistenceService.getById(id);
      return true;
    } else {
      log.warn("Author with id {} not found.", id);
      return false;
    }
  }
}
