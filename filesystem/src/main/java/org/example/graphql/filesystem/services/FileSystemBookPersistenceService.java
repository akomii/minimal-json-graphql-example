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

package org.example.graphql.filesystem.services;

import java.util.ArrayList;
import java.util.List;
import org.example.graphql.filesystem.models.FileSystemBook;
import org.example.graphql.filesystem.persistence.FileSystemStorage;
import org.example.graphql.server.models.Book;
import org.example.graphql.server.services.BookPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for persisting and retrieving {@link Book} entities using file system storage. Provides concrete implementation of
 * {@link BookPersistenceService} for {@link FileSystemBook}.
 *
 * @author Alexander Kombeiz
 * @version 1.01
 * @since 05-02-2024
 */
@Service
public class FileSystemBookPersistenceService implements BookPersistenceService {

  private final FileSystemStorage<FileSystemBook> bookStorage;

  @Autowired
  public FileSystemBookPersistenceService(FileSystemStorage<FileSystemBook> bookStorage) {
    this.bookStorage = bookStorage;
  }

  @Override
  public Book getById(Long id) {
    return bookStorage.getById(id);
  }

  @Override
  public List<Book> getAll() {
    return new ArrayList<>(bookStorage.getAll());
  }

  @Override
  public Book persist(Book author) {
    return bookStorage.save((FileSystemBook) author);
  }

  @Override
  public void deleteById(Long id) {
    bookStorage.deleteById(id);
  }
}
