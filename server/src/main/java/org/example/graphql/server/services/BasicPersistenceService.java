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

package org.example.graphql.server.services;

import java.util.List;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;

/**
 * Interface for a service to interact with the persistence layer for {@link Author} and
 * {@link Book} instances.
 * <p>
 * This interface provides methods to fetch, persist, and delete {@link Author} and {@link Book}
 * instances by their ID. It also provides methods to fetch all authors and books.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 08-01-2024
 */
public interface BasicPersistenceService {

  Author getAuthorById(Long id);

  List<Author> getAllAuthors();

  Author persistAuthor(Author author);

  void deleteAuthorById(Long id);

  Book getBookById(Long id);

  List<Book> getAllBooks();

  Book persistBook(Book book);

  void deleteBookById(Long id);
}
