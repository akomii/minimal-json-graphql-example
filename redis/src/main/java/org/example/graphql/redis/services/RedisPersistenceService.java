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
import org.example.graphql.redis.persistence.RedisAuthorRepository;
import org.example.graphql.redis.persistence.RedisBookRepository;
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
 * {@link RedisAuthorRepository} and {@link RedisBookRepository} to interact with the database. It is
 * annotated with {@link Service} to indicate that it is a Spring service.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 08-01-2024
 */
@Service
public class RedisPersistenceService implements BasicPersistenceService {

  private final RedisAuthorRepository redisAuthorRepository;

  private final RedisBookRepository redisBookRepository;

  /**
   * Constructs a new {@link RedisPersistenceService} with the given {@link RedisAuthorRepository} and
   * {@link RedisBookRepository}.
   *
   * @param redisAuthorRepository the repository to use for {@link RedisAuthor} instances
   * @param redisBookRepository   the repository to use for {@link RedisBook} instances
   */
  @Autowired
  public RedisPersistenceService(RedisAuthorRepository redisAuthorRepository, RedisBookRepository redisBookRepository) {
    this.redisAuthorRepository = redisAuthorRepository;
    this.redisBookRepository = redisBookRepository;
  }

  @Override
  public Author getAuthorById(Long id) {
    return redisAuthorRepository.findById(id).orElse(null);
  }

  @Override
  public List<Author> getAllAuthors() {
    List<RedisAuthor> redisAuthors = redisAuthorRepository.findAll();
    return new ArrayList<>(redisAuthors);
  }

  @Override
  public Author persistAuthor(Author author) {
    return redisAuthorRepository.save((RedisAuthor) author);
  }

  @Override
  public void deleteAuthorById(Long id) {
    redisAuthorRepository.deleteById(id);
  }

  @Override
  public Book getBookById(Long id) {
    return redisBookRepository.findById(id).orElse(null);
  }

  @Override
  public List<Book> getAllBooks() {
    List<RedisBook> redisBooks = redisBookRepository.findAll();
    return new ArrayList<>(redisBooks);
  }

  @Override
  public Book persistBook(Book book) {
    return redisBookRepository.save((RedisBook) book);
  }

  @Override
  public void deleteBookById(Long id) {
    redisBookRepository.deleteById(id);
  }
}
