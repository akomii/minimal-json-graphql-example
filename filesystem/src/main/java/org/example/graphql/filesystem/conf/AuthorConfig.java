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
import org.example.graphql.filesystem.models.FileSystemAuthor;
import org.example.graphql.filesystem.persistence.FileSystemStorage;
import org.example.graphql.filesystem.utils.DirectoryManager;
import org.example.graphql.filesystem.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for {@link FileSystemAuthor}-related beans. This class is responsible for configuring and provisioning beans specific to
 * author management, including storage, ID generation, and directory management within the filesystem.
 * <p>
 * It uses application properties to configure the working directory for author data storage.
 * </p>
 *
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 05-02-2024
 */
@Configuration
public class AuthorConfig {

  @Value("${filesystem.authors.working-dir:/tmp/authors}")
  private String workingDir;

  /**
   * Creates and configures a {@link DirectoryManager} bean for managing the directory where author data is stored. The working directory path is
   * determined by application properties, with a default fallback to "/tmp/authors".
   *
   * @return A {@link DirectoryManager} instance for author data.
   */
  @Bean
  public DirectoryManager authorDirManager() {
    return new DirectoryManager(workingDir);
  }

  /**
   * Creates a {@link IdGenerator} bean that relies on the {@link DirectoryManager} bean for managing unique ID generation for authors. This setup
   * ensures that ID generation is consistent and based on the file system storage specifics for authors.
   *
   * @return An {@link IdGenerator} instance for generating unique IDs for authors.
   */
  @Bean
  public IdGenerator authorIdGenerator() {
    return new IdGenerator(authorDirManager());
  }

  /**
   * Configures and provides a {@link FileSystemStorage} bean specialized for {@link FileSystemAuthor} entities. This bean integrates with the
   * directory management and ID generation facilities to provide persistent storage capabilities.
   *
   * @return A {@link FileSystemStorage} instance configured for storing and managing {@link FileSystemAuthor} entities.
   */
  @Bean
  public FileSystemStorage<FileSystemAuthor> authorStorage() {
    return new FileSystemStorage<>(new ObjectMapper(), authorDirManager(),
        authorIdGenerator(), FileSystemAuthor.class);
  }
}
