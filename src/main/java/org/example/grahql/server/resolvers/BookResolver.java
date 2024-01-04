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

import java.util.Optional;
import org.example.grahql.server.models.Author;
import org.example.grahql.server.models.Book;
import org.example.grahql.server.persistence.AuthorRepository;
import org.example.grahql.server.persistence.BookRepository;
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
 * This class uses the {@link org.springframework.stereotype.Controller} annotation to indicate that
 * it's a controller. It also uses the
 * {@link org.springframework.graphql.data.method.annotation.QueryMapping} and
 * {@link org.springframework.graphql.data.method.annotation.MutationMapping} annotations to map
 * GraphQL queries and mutations to methods.
 * <p>
 * The {@link org.springframework.beans.factory.annotation.Autowired} annotation is used to inject
 * dependencies.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 04-01-2024
 */
@Controller
public class BookResolver {

  private static final Logger log = LoggerFactory.getLogger(BookResolver.class);

  private final BookRepository bookRepository;

  private final AuthorRepository authorRepository;

  /**
   * Constructs a new BookResolver with the given repositories.
   *
   * @param bookRepository   the repository for accessing books.
   * @param authorRepository the repository for accessing authors.
   */
  @Autowired
  public BookResolver(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
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
    return bookRepository.findById(id).orElse(null);
  }

  /**
   * Fetches all books.
   *
   * @return an iterable of all books.
   */
  @QueryMapping
  public Iterable<Book> books() {
    log.info("Fetching all books");
    return bookRepository.findAll();
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
    Author author = authorRepository.findById(authorId).orElse(null);
    if (author != null) {
      Book newBook = new Book();
      newBook.setTitle(title);
      newBook.setPublishedYear(publishedYear);
      newBook.setAuthor(author);
      Book savedBook = bookRepository.save(newBook);
      author.getPublishedBookIds().add(savedBook.getId());
      authorRepository.save(author);
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
    Optional<Book> bookOptional = bookRepository.findById(id);
    if (bookOptional.isPresent()) {
      Book book = bookOptional.get();
      Author author = book.getAuthor();
      if (author != null) {
        log.info("Removing book with id: {} from author with id: {}", id, author.getId());
        author.getPublishedBookIds().remove(id);
        authorRepository.save(author);
      }
      bookRepository.delete(book);
      return true;
    } else {
      log.warn("Book with id {} not found.", id);
      return false;
    }
  }
}
