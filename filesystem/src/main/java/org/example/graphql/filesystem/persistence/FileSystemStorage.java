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

package org.example.graphql.filesystem.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.graphql.filesystem.models.AbstractPersistentObject;
import org.example.graphql.filesystem.utils.DirectoryManager;
import org.example.graphql.filesystem.utils.IdGenerator;

/**
 * Provides generic file system storage capabilities for entities extending {@link AbstractPersistentObject}. Utilizes {@link ObjectMapper} for JSON
 * serialization and deserialization, supporting basic CRUD operations.
 * <p>
 * Entities are stored as individual JSON files within a specified directory, managed by {@link DirectoryManager}, with unique IDs generated by
 * {@link IdGenerator}.
 * </p>
 *
 * @param <T> The type of entity this storage handles. Must extend {@link AbstractPersistentObject}.
 * @author Alexander Kombeiz
 * @version 1.0
 * @since 05-02-2024
 */
public class FileSystemStorage<T extends AbstractPersistentObject> {

  private static final Logger logger = Logger.getLogger(FileSystemStorage.class.getName());
  private static final String FILE_EXTENSION = ".json";

  private final ObjectMapper objectMapper;
  private final DirectoryManager dirCreator;
  private final IdGenerator idGenerator;
  private final Class<T> typeParameterClass;

  /**
   * Constructs a new {@code FileSystemStorage} instance with specified dependencies.
   *
   * @param objectMapper       The {@link ObjectMapper} for JSON processing.
   * @param dirCreator         The {@link DirectoryManager} for directory operations.
   * @param idGenerator        The {@link IdGenerator} for generating unique entity IDs.
   * @param typeParameterClass The class object of T for deserialization purposes.
   */
  public FileSystemStorage(ObjectMapper objectMapper,
      DirectoryManager dirCreator,
      IdGenerator idGenerator,
      Class<T> typeParameterClass) {
    this.objectMapper = objectMapper;
    this.dirCreator = dirCreator;
    this.idGenerator = idGenerator;
    this.typeParameterClass = typeParameterClass;
  }

  public T getById(Long id) {
    try {
      String filePath = dirCreator.getAbsoluteFilePathForEntity(id, FILE_EXTENSION);
      return objectMapper.readValue(new File(filePath), typeParameterClass);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error getting entity by ID", e);
      return null;
    }
  }

  public List<T> getAll() {
    List<T> entities = new ArrayList<>();
    File folder = new File(dirCreator.getAbsolutePathForWorkingDir());
    if (folder.exists() && folder.isDirectory()) {
      File[] files = folder.listFiles((dir, name) -> name.endsWith(FILE_EXTENSION));
      if (files != null) {
        for (File file : files) {
          try {
            T entity = objectMapper.readValue(file, typeParameterClass);
            entities.add(entity);
          } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while reading entity from file", e);
          }
        }
      }
    }
    return entities;
  }

  public T save(T entity) {
    // Generate ID if not present
    if (entity.getId() == null) {
      entity.setId(idGenerator.generateId());
    }
    try {
      String filePath = dirCreator.getAbsoluteFilePathForEntity(entity.getId(), FILE_EXTENSION);
      objectMapper.writeValue(new File(filePath), entity);
      return entity;
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error saving entity", e);
      return null;
    }
  }

  public void deleteById(Long id) {
    String filePath = dirCreator.getAbsoluteFilePathForEntity(id, FILE_EXTENSION);
    File file = new File(filePath);
    if (file.exists() && !file.delete()) {
      logger.warning("Error while deleting entity: " + filePath);
    }
  }
}
