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
import org.example.graphql.server.services.BasicPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller class for handling GraphQL queries and mutations related to {@link Author} instances.
 * <p>
 * This class provides methods to fetch an author by their ID, fetch all authors, create a new
 * author, and delete an author by their ID. It uses a {@link BasicPersistenceService} to interact
 * with the persistence layer and a {@link BasicFactory} to create new {@link Author} instances.
 * <p>
 * Each method is annotated with either {@link QueryMapping} or {@link MutationMapping} to indicate
 * whether it's a GraphQL query or mutation. The {@link Argument} annotation is used to specify the
 * arguments of the GraphQL query or mutation.
 *
 * @author Alexander Kombeiz
 * @version 1.02
 * @since 04-01-2024
 */
@Controller
public class AuthorResolver {

  private static final Logger log = LoggerFactory.getLogger(AuthorResolver.class);

  private final BasicPersistenceService basicPersistenceService;

  private final BasicFactory basicFactory;

  /**
   * Constructs a new {@link AuthorResolver} with the given {@link BasicPersistenceService} and
   * {@link BasicFactory}.
   *
   * @param basicPersistenceService the {@link BasicPersistenceService} to use for interacting with
   *                                the persistence layer.
   * @param basicFactory            the {@link BasicFactory} to use for creating new {@link Author}
   *                                instances.
   */
  @Autowired
  public AuthorResolver(BasicPersistenceService basicPersistenceService,
      BasicFactory basicFactory) {
    this.basicPersistenceService = basicPersistenceService;
    this.basicFactory = basicFactory;
  }

  /**
   * Fetches an author by their ID.
   *
   * @param id the ID of the author to fetch.
   * @return the author with the given ID, or null if no such author exists.
   */
  @QueryMapping
  public Author authorById(@Argument Long id) {
    log.info("Fetching author with id: {}", id);
    return basicPersistenceService.getAuthorById(id);
  }

  /**
   * Fetches all authors.
   *
   * @return an iterable of all authors.
   */
  @QueryMapping
  public List<Author> authors() {
    log.info("Fetching all authors");
    return basicPersistenceService.getAllAuthors();
  }

  /**
   * Creates a new author.
   *
   * @param firstName the first name of the author to create.
   * @param lastName  the last name of the author to create.
   * @return the created author.
   */
  @MutationMapping
  public Author createAuthor(@Argument String firstName, @Argument String lastName) {
    log.info("Creating author with firstName: {} and lastName: {}", firstName, lastName);
    Author newAuthor = basicFactory.createAuthor();
    newAuthor.setFirstName(firstName);
    newAuthor.setLastName(lastName);
    return basicPersistenceService.persistAuthor(newAuthor);
  }

  /**
   * Deletes an author with the specified ID and associated books.
   * <p>
   * This method removes the specified author and all books associated with them. The deletion
   * process includes removing each associated book from the persistence layer, ensuring the
   * maintenance of a consistent dependency relationship between authors and books.
   *
   * @param id the ID of the author to delete.
   * @return true if the author was deleted, false otherwise.
   */
  @MutationMapping
  public Boolean deleteAuthor(@Argument Long id) {
    log.info("Deleting author with id: {}", id);
    Author author = basicPersistenceService.getAuthorById(id);
    if (author != null) {
      author.getPublishedBookIds().forEach(bookId -> {
        log.info("Deleting book with id: {} associated with author id: {}", bookId, id);
        basicPersistenceService.deleteBookById(bookId);
      });
      basicPersistenceService.deleteAuthorById(id);
      return true;
    } else {
      log.warn("Author with id {} not found.", id);
      return false;
    }
  }
}
