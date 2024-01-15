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

package org.example.graphql.filesystem.factories;

import org.example.graphql.filesystem.models.FileSystemAuthor;
import org.example.graphql.filesystem.models.FileSystemBook;
import org.example.graphql.server.factories.BasicFactory;
import org.example.graphql.server.models.Author;
import org.example.graphql.server.models.Book;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating {@link FileSystemAuthor} and {@link FileSystemBook} instances.
 * <p>
 * This class implements the {@link BasicFactory} interface and overrides its methods to create
 * {@link FileSystemAuthor} and {@link FileSystemBook} instances. It is annotated with
 * {@link Component} to indicate that it is a Spring component.
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 15-01-2024
 */
@Component
public class FileSystemFactory implements BasicFactory {

  @Override
  public Author createAuthor() {
    return new FileSystemAuthor();
  }

  @Override
  public Book createBook() {
    return new FileSystemBook();
  }
}
