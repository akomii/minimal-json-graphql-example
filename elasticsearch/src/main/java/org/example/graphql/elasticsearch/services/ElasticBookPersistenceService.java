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
import org.example.graphql.elasticsearch.models.ElasticBook;
import org.example.graphql.elasticsearch.persistence.ElasticBookRepository;
import org.example.graphql.server.models.Book;
import org.example.graphql.server.services.BookPersistenceService;
import org.springframework.stereotype.Service;

/**
 * Service for persisting and retrieving {@link Book} entities in Elasticsearch.
 * <p>
 * Implements {@link BookPersistenceService} to interact with Elasticsearch using {@link ElasticBookRepository}. Handles conversion between
 * {@link Book} and {@link ElasticBook} when necessary.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 18-03-2024
 */
@Service
public class ElasticBookPersistenceService implements BookPersistenceService {

  private final ElasticBookRepository elasticBookRepository;

  public ElasticBookPersistenceService(ElasticBookRepository elasticBookRepository) {
    this.elasticBookRepository = elasticBookRepository;
  }

  @Override
  public Book getById(Long id) {
    return elasticBookRepository.findById(id).orElse(null);
  }

  @Override
  public List<Book> getAll() {
    Iterable<ElasticBook> books = elasticBookRepository.findAll();
    List<Book> list = new ArrayList<>();
    books.forEach(list::add);
    return list;
  }

  @Override
  public Book persist(Book book) {
    return elasticBookRepository.save((ElasticBook) book);
  }

  @Override
  public void deleteById(Long id) {
    elasticBookRepository.deleteById(id);
  }
}
