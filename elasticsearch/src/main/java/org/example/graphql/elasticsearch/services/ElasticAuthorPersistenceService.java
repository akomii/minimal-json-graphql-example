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

package org.example.graphql.elasticsearch.services;

import java.util.ArrayList;
import java.util.List;
import org.example.graphql.elasticsearch.models.ElasticAuthor;
import org.example.graphql.elasticsearch.persistence.ElasticAuthorRepository;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.services.AuthorPersistenceService;
import org.springframework.stereotype.Service;

/**
 * Service for persisting and retrieving {@link Author} entities in Elasticsearch.
 * <p>
 * Implements {@link AuthorPersistenceService} to interact with Elasticsearch using {@link ElasticAuthorRepository}. Handles conversion between
 * {@link Author} and {@link ElasticAuthor} when necessary.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 18-03-2024
 */
@Service
public class ElasticAuthorPersistenceService implements AuthorPersistenceService {

  private final ElasticAuthorRepository elasticAuthorRepository;

  public ElasticAuthorPersistenceService(ElasticAuthorRepository elasticAuthorRepository) {
    this.elasticAuthorRepository = elasticAuthorRepository;
  }

  @Override
  public Author getById(Long id) {
    return elasticAuthorRepository.findById(id).orElse(null);
  }

  @Override
  public List<Author> getAll() {
    Iterable<ElasticAuthor> authors = elasticAuthorRepository.findAll();
    List<Author> list = new ArrayList<>();
    authors.forEach(list::add);
    return list;
  }

  @Override
  public Author persist(Author author) {
    return elasticAuthorRepository.save((ElasticAuthor) author);
  }

  @Override
  public void deleteById(Long id) {
    elasticAuthorRepository.deleteById(id);
  }
}
