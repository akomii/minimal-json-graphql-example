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

import java.util.List;

/**
 * Interface for {@link Author} instances.
 * <p>
 * This interface defines the contract for an Author entity in the system. It includes methods to
 * get and set the properties of an Author, such as its ID, first name, last name, and published
 * book IDs.
 * <p>
 * Each method should be implemented to interact with the corresponding property of the
 * {@link Author}.
 *
 * @author Alexander Kombeiz
 * @version 1.03
 * @since 08-01-2024
 */
public interface Author {

  Long getId();

  void setId(Long id);

  String getFirstName();

  void setFirstName(String firstName);

  String getLastName();

  void setLastName(String lastName);

  List<Long> getPublishedBookIds();

  void setPublishedBookIds(List<Long> publishedBookIds);

  void addPublishedBook(Long id);

  void removePublishedBook(Long id);
}
