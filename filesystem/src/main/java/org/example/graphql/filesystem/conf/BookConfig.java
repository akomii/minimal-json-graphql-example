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

package org.example.graphql.filesystem.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.graphql.filesystem.models.FileSystemBook;
import org.example.graphql.filesystem.persistence.FileSystemStorage;
import org.example.graphql.filesystem.utils.DirectoryManager;
import org.example.graphql.filesystem.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for {@link FileSystemBook}-related beans. This class configures the
 * beans necessary for managing books within the filesystem, including their storage, ID generation,
 * and directory management.
 * <p>
 * The working directory for book data storage is configurable through application properties,
 * allowing for flexibility in the storage location.
 * </p>
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 05-02-2024
 */
@Configuration
public class BookConfig {

  @Value("${filesystem.books.working-dir:/tmp/books}")
  private String workingDir;

  /**
   * Creates and configures a {@link DirectoryManager} bean for managing the directory where book
   * data is stored. The working directory path is determined by application properties, with a
   * default fallback to "/tmp/books".
   *
   * @return A {@link DirectoryManager} instance dedicated to book data management.
   */
  @Bean
  public DirectoryManager bookDirManager() {
    return new DirectoryManager(workingDir);
  }

  /**
   * Provides a {@link IdGenerator} bean specifically for book entity ID generation. Utilizes the
   * {@link DirectoryManager} bean for directory management to ensure unique ID generation is
   * aligned with the file system storage strategy for books.
   *
   * @return An {@link IdGenerator} instance for book entities.
   */
  @Bean
  public IdGenerator bookIdGenerator() {
    return new IdGenerator(bookDirManager());
  }

  /**
   * Configures a {@link FileSystemStorage} bean for {@link FileSystemBook} entities, integrating
   * directory management and ID generation services for comprehensive storage management. This bean
   * is essential for persisting book data within the filesystem.
   *
   * @return A {@link FileSystemStorage} instance tailored for {@link FileSystemBook} entities.
   */
  @Bean
  public FileSystemStorage<FileSystemBook> bookStorage() {
    return new FileSystemStorage<>(new ObjectMapper(), bookDirManager(), bookIdGenerator(),
        FileSystemBook.class);
  }
}
