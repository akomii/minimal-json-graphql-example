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

package org.example.graphql.filesystem.models;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.graphql.server.models.Author;

/**
 * Represents an author in a file system-based storage mechanism. This class extends
 * {@link AbstractPersistentObject} to inherit common persistence properties and implements the
 * {@link Author} interface to fulfill author-specific behaviors.
 * <p>
 * This class manages the author's basic information and a list of IDs of published books,
 * supporting operations to add or remove books from an author's bibliography.
 * </p>
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 2024-02-02
 */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileSystemAuthor extends AbstractPersistentObject implements Author {

  String firstName;

  String lastName;

  List<Long> publishedBookIds = new ArrayList<>();

  @Override
  public void addPublishedBook(Long id) {
    publishedBookIds.add(id);
  }

  @Override
  public void removePublishedBook(Long id) {
    publishedBookIds.remove(id);
  }
}
