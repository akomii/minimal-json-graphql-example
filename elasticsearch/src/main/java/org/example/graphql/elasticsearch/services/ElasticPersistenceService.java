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
import org.example.graphql.elasticsearch.models.ElasticBook;
import org.example.graphql.elasticsearch.persistence.AuthorRepository;
import org.example.graphql.elasticsearch.persistence.BookRepository;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.example.graphql.server.services.BasicPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class ElasticPersistenceService implements BasicPersistenceService {

  private final AuthorRepository authorRepository;

  private final BookRepository bookRepository;

  /**
   * Constructs a new {@link ElasticPersistenceService} with the given {@link AuthorRepository} and {@link BookRepository}.
   *
   * @param authorRepository the repository to use for {@link ElasticAuthor} instances
   * @param bookRepository   the repository to use for {@link ElasticBook} instances
   */
  public ElasticPersistenceService(AuthorRepository authorRepository, BookRepository bookRepository) {
    this.authorRepository = authorRepository;
    this.bookRepository = bookRepository;
  }

  @Override
  public Author getAuthorById(Long id) {
    return authorRepository.findById(id).orElse(null);
  }

  @Override
  public List<Author> getAllAuthors() {
    Iterable<ElasticAuthor> authors = authorRepository.findAll();
    List<Author> list = new ArrayList<>();
    authors.forEach(list::add);
    return list;
  }

  @Override
  public Author persistAuthor(Author author) {
    return authorRepository.save((ElasticAuthor) author);
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
    Iterable<ElasticBook> books = bookRepository.findAll();
    List<Book> list = new ArrayList<>();
    books.forEach(list::add);
    return list;
  }

  @Override
  public Book persistBook(Book book) {
    return bookRepository.save((ElasticBook) book);
  }

  @Override
  public void deleteBookById(Long id) {
    bookRepository.deleteById(id);
  }
}
