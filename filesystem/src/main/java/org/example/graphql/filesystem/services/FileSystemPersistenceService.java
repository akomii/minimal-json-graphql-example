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
import org.example.graphql.filesystem.models.FileSystemAuthor;
import org.example.graphql.filesystem.models.FileSystemBook;
import org.example.graphql.filesystem.persistence.FileSystemStorage;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.example.graphql.server.services.BasicPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for persisting and retrieving Author and Book entities using file system storage.
 * This class leverages {@link FileSystemStorage} for CRUD operations on {@link FileSystemAuthor}
 * and {@link FileSystemBook} entities, providing a concrete implementation of
 * {@link BasicPersistenceService}.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 2024-05-02
 */
@Service
public class FileSystemPersistenceService implements BasicPersistenceService {

  private final FileSystemStorage<FileSystemAuthor> authorStorage;
  private final FileSystemStorage<FileSystemBook> bookStorage;

  @Autowired
  public FileSystemPersistenceService(FileSystemStorage<FileSystemAuthor> authorStorage,
      FileSystemStorage<FileSystemBook> bookStorage) {
    this.authorStorage = authorStorage;
    this.bookStorage = bookStorage;
  }

  @Override
  public Author getAuthorById(Long id) {
    return authorStorage.getById(id);
  }

  @Override
  public List<Author> getAllAuthors() {
    return new ArrayList<>(authorStorage.getAll());
  }

  @Override
  public Author persistAuthor(Author author) {
    return authorStorage.save((FileSystemAuthor) author);
  }

  @Override
  public void deleteAuthorById(Long id) {
    authorStorage.deleteById(id);
  }

  @Override
  public Book getBookById(Long id) {
    return bookStorage.getById(id);
  }

  @Override
  public List<Book> getAllBooks() {
    return new ArrayList<>(bookStorage.getAll());
  }

  @Override
  public Book persistBook(Book book) {
    return bookStorage.save((FileSystemBook) book);
  }

  @Override
  public void deleteBookById(Long id) {
    bookStorage.deleteById(id);
  }
}
