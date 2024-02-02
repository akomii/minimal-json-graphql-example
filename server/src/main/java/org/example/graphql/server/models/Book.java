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

package org.example.graphql.server.models;

/**
 * Interface for {@link Book} instances.
 * <p>
 * This interface defines the contract for a Book entity in the system. It includes methods to get
 * and set the properties of a Book, such as its ID, title, published year, and author.
 * <p>
 * Each method should be implemented to interact with the corresponding property of the
 * {@link Book}.
 *
 * @author Alexander Kombeiz
 * @version 1.01
 * @since 08-01-2024
 */
public interface Book {

  Long getId();

  void setId(Long id);

  String getTitle();

  void setTitle(String title);

  int getPublishedYear();

  void setPublishedYear(int publishedYear);

  Author getAuthor();

  void setAuthor(Author author);
}

