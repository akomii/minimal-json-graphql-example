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
import org.example.graphql.redis.persistence.RedisAuthorRepository;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.services.AuthorPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for CRUD operations on {@link RedisAuthor} instances. Implements {@link AuthorPersistenceService} to manage {@link RedisAuthor} entities
 * using {@link RedisAuthorRepository}.
 *
 * @author Alexander Kombeiz
 * @version 1.01
 * @since 08-01-2024
 */
@Service
public class RedisAuthorPersistenceService implements AuthorPersistenceService {

  private final RedisAuthorRepository redisAuthorRepository;

  @Autowired
  public RedisAuthorPersistenceService(RedisAuthorRepository redisAuthorRepository) {
    this.redisAuthorRepository = redisAuthorRepository;
  }

  @Override
  public Author getById(Long id) {
    return redisAuthorRepository.findById(id).orElse(null);
  }

  @Override
  public List<Author> getAll() {
    List<RedisAuthor> redisAuthors = redisAuthorRepository.findAll();
    return new ArrayList<>(redisAuthors);
  }

  @Override
  public Author persist(Author author) {
    return redisAuthorRepository.save((RedisAuthor) author);
  }

  @Override
  public void deleteById(Long id) {
    redisAuthorRepository.deleteById(id);
  }
}
