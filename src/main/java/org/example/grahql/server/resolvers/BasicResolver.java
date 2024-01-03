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

@Controller
public class BasicResolver {

  private static final Logger log = LoggerFactory.getLogger(BasicResolver.class);

  private final AuthorRepository authorRepository;

  private final BookRepository bookRepository;

  @Autowired
  public BasicResolver(AuthorRepository authorRepository, BookRepository bookRepository) {
    this.authorRepository = authorRepository;
    this.bookRepository = bookRepository;
  }

  @QueryMapping
  public Author authorById(@Argument int id) {
    log.info("Fetching author with id: {}", id);
    return authorRepository.findById(id).orElse(null);
  }

  @QueryMapping
  public Iterable<Author> authors() {
    log.info("Fetching all authors");
    return authorRepository.findAll();
  }

  @MutationMapping
  public Author createAuthor(@Argument String firstName, @Argument String lastName) {
    log.info("Creating author with firstName: {} and lastName: {}", firstName, lastName);
    Author newAuthor = new Author();
    newAuthor.setFirstName(firstName);
    newAuthor.setLastName(lastName);
    return authorRepository.save(newAuthor);
  }

  @QueryMapping
  public Book bookById(@Argument int id) {
    log.info("Fetching book with id: {}", id);
    return bookRepository.findById(id).orElse(null);
  }

  @QueryMapping
  public Iterable<Book> books() {
    log.info("Fetching all books");
    return bookRepository.findAll();
  }

  @MutationMapping
  public Book createBook(@Argument String title, @Argument int publishedYear,
      @Argument int authorId) {
    log.info("Creating book with title: {}, publishedYear: {}, and authorId: {}",
        title, publishedYear, authorId);
    Author author = this.authorById(authorId);
    Book newBook = new Book();
    newBook.setTitle(title);
    newBook.setPublishedYear(publishedYear);
    newBook.setAuthor(author);
    return bookRepository.save(newBook);
  }
}
