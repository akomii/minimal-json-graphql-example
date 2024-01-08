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

package org.example.graphql.redis.services;

import java.util.ArrayList;
import java.util.List;
import org.example.graphql.redis.models.RedisAuthor;
import org.example.graphql.redis.models.RedisBook;
import org.example.graphql.redis.persistence.AuthorRepository;
import org.example.graphql.redis.persistence.BookRepository;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.example.graphql.server.services.BasicPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for performing CRUD operations on {@link RedisAuthor} and {@link RedisBook}
 * instances.
 * <p>
 * This class implements the {@link BasicPersistenceService} interface and overrides its methods to
 * perform CRUD operations on {@link RedisAuthor} and {@link RedisBook} instances. It uses
 * {@link AuthorRepository} and {@link BookRepository} to interact with the database. It is
 * annotated with {@link Service} to indicate that it is a Spring service.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 08-01-2024
 */
@Service
public class RedisPersistenceService implements BasicPersistenceService {

  private final AuthorRepository authorRepository;

  private final BookRepository bookRepository;

  /**
   * Constructs a new {@link RedisPersistenceService} with the given {@link AuthorRepository} and
   * {@link BookRepository}.
   *
   * @param authorRepository the repository to use for {@link RedisAuthor} instances
   * @param bookRepository   the repository to use for {@link RedisBook} instances
   */
  @Autowired
  public RedisPersistenceService(AuthorRepository authorRepository, BookRepository bookRepository) {
    this.authorRepository = authorRepository;
    this.bookRepository = bookRepository;
  }

  @Override
  public Author getAuthorById(Long id) {
    return authorRepository.findById(id).orElse(null);
  }

  @Override
  public List<Author> getAllAuthors() {
    List<RedisAuthor> redisAuthors = authorRepository.findAll();
    return new ArrayList<>(redisAuthors);
  }

  @Override
  public Author persistAuthor(Author author) {
    return authorRepository.save((RedisAuthor) author);
  }

  @Override
  public void deleteAuthorById(Long id) {
    authorRepository.deleteById(id);
  }

  @Override
  public Book getBookById(Long id) {
    return bookRepository.findById(id).orElse(null);
  }

  @Override
  public List<Book> getAllBooks() {
    List<RedisBook> redisBooks = bookRepository.findAll();
    return new ArrayList<>(redisBooks);
  }

  @Override
  public Book persistBook(Book book) {
    return bookRepository.save((RedisBook) book);
  }

  @Override
  public void deleteBookById(Long id) {
    bookRepository.deleteById(id);
  }
}
