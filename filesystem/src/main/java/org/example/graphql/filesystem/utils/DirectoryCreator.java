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

package org.example.graphql.filesystem.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectoryCreator {

  @Value("${filesystem.base-path:/tmp}")
  private String basePath;

  @Getter
  private Path workingDir;

  public DirectoryCreator(String dirName) {
    initWorkingDir(dirName);
  }

  private void initWorkingDir(String dirName) {
    Path dirPath = Paths.get(basePath, dirName);
    File dir = dirPath.toFile();
    if (!dir.exists() && !dir.mkdirs()) {
      throw new RuntimeException("Failed to create directory: " + dirPath);
    }
    workingDir = dirPath;
  }

  public String getAbsolutePathForWorkingDir() {
    return workingDir.toString();
  }

  public String getAbsoluteFilePathForEntity(Long entityId, String extension) {
    return workingDir.resolve(entityId + extension).toString();
  }
}
