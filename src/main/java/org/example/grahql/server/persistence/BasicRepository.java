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

package org.example.grahql.server.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.grahql.server.models.Author;
import org.example.grahql.server.models.Book;
import org.springframework.stereotype.Service;

@Service
public class BasicRepository {


  private static final List<Author> authors = new ArrayList<>(Arrays.asList(
      new Author(1, "Joshua", "Bloch"),
      new Author(2, "Douglas", "Adams"),
      new Author(3, "Bill", "Bryson")
  ));

  private static final List<Book> books = new ArrayList<>(Arrays.asList(
      new Book(1, "Effective Java", 2017, authors.get(0)),
      new Book(2, "Hitchhiker's Guide to the Galaxy", 1979, authors.get(1)),
      new Book(3, "Down Under", 2000, authors.get(2))
  ));

  public Author getAuthor(int id) {
    return authors.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
  }

  public List<Author> getAuthors() {
    return authors;
  }

  public Author createAuthor(String firstName, String lastName) {
    Author author = new Author(authors.size() + 1, firstName, lastName);
    authors.add(author);
    return author;
  }

  public Book getBook(int id) {
    return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
  }

  public List<Book> getBooks() {
    return books;
  }

  public Book createBook(String title, int publishedYear, int authorId) {
    Author author = authors.stream().filter(a -> a.getId() == authorId).findFirst().orElse(null);
    if (author == null) {
      throw new IllegalArgumentException("Author not found");
    }
    Book book = new Book(books.size() + 1, title, publishedYear, author);
    books.add(book);
    return book;
  }
}
