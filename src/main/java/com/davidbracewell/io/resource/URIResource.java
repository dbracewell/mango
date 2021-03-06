/*
 * (c) 2005 David B. Bracewell
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.davidbracewell.io.resource;

import com.davidbracewell.io.FileUtils;
import com.google.common.base.Throwables;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Resource that wraps a URI
 *
 * @author David B. Bracewell
 */
@EqualsAndHashCode(callSuper = false)
public class URIResource extends BaseResource {
   private static final long serialVersionUID = 1L;
   private final URI uri;

   /**
    * Instantiates a new Uri resource.
    *
    * @param uri the uri
    */
   public URIResource(@NonNull URI uri) {
      this.uri = uri;
   }

   @Override
   public Optional<File> asFile() {
      if (uri.getScheme().equalsIgnoreCase("file")) {
         return Optional.of(new File(uri.getPath()));
      }
      return super.asFile();
   }

   @Override
   public Resource append(byte[] byteArray) throws IOException {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean exists() {
      try (InputStream is = createInputStream()) {
         return true;
      } catch (IOException e) {
         return false;
      }
   }

   @Override
   public Resource getChild(String relativePath) {
      return new URIResource(uri.resolve("/" + relativePath));
   }

   @Override
   public Resource getParent() {
      try {
         return new URIResource(new URI(FileUtils.parent(uri.toString())));
      } catch (URISyntaxException e) {
         throw Throwables.propagate(e);
      }
   }

   @Override
   public String descriptor() {
      return uri.toString();
   }

   @Override
   protected OutputStream createOutputStream() throws IOException {
      OutputStream os = super.createOutputStream();
      if (os == null) {
         return uri.toURL().openConnection().getOutputStream();
      }
      return os;
   }

   @Override
   protected InputStream createInputStream() throws IOException {
      InputStream is = super.createInputStream();
      if (is == null) {
         return uri.toURL().openConnection().getInputStream();
      }
      return is;
   }

   @Override
   public Optional<URI> asURI() {
      return Optional.of(uri);
   }

   @Override
   public String path() {
      return uri.getPath();
   }
}//END OF URIResource
