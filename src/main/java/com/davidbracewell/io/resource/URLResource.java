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

import com.davidbracewell.io.QuietIO;
import com.davidbracewell.string.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * <p> A <code>Resource</code> wrapper for a URL. </p>
 *
 * @author David B. Bracewell
 */
public class URLResource extends Resource {

  private static final long serialVersionUID = -5874490341557934277L;
  private URL url;
  private String userAgent = "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0";
  private int connectionTimeOut = 30000;

  /**
   * Instantiates a new uRL resource.
   *
   * @param url the url
   * @throws java.net.MalformedURLException the malformed url exception
   */
  public URLResource(String url) throws MalformedURLException {
    Preconditions.checkNotNull(url);
    this.url = new URL(url);
  }

  @Override
  public File asFile() {
    if (url.getProtocol().equalsIgnoreCase("file")) {
      return new File(url.getFile());
    }
    return super.asFile();
  }

  /**
   * Instantiates a new uRL resource.
   *
   * @param url the url
   */
  public URLResource(URL url) {
    Preconditions.checkNotNull(url);
    this.url = url;
  }

  @Override
  public String path() {
    return url.getPath();
  }

  @Override
  public String baseName() {
    return url.getFile();
  }

  @Override
  public String resourceDescriptor() {
    return url.toString();
  }


  @Override
  public URL asURL() throws MalformedURLException {
    return url;
  }

  @Override
  public Resource getChild(String relativePath) {
    try {
      return new URLResource(new URL(url, relativePath));
    } catch (MalformedURLException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public boolean canWrite() {
    return true;
  }

  @Override
  public boolean canRead() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    } else if (o instanceof URLResource) {
      return url.equals(((URLResource) o).url);
    }
    return false;
  }

  @Override
  public boolean exists() {
    boolean exists = true;
    InputStream is = null;
    try {
      URLConnection connection = createConnection();
      connection.setConnectTimeout(5 * 1000);
      is = connection.getInputStream();
    } catch (Exception e) {
      exists = false;
    } finally {
      QuietIO.closeQuietly(is);
    }
    return exists;
  }

  @Override
  public int hashCode() {
    return url.hashCode();
  }

  @Override
  public InputStream createInputStream() throws IOException {
    return createConnection().getInputStream();
  }

  private URLConnection createConnection() throws IOException {
    URLConnection connection = url.openConnection();
    if (!StringUtils.isNullOrBlank(userAgent)) {
      connection.setRequestProperty("User-Agent", userAgent);
    }
    connection.setConnectTimeout(connectionTimeOut);
    return connection;
  }

  @Override
  public OutputStream createOutputStream() throws IOException {
    return createConnection().getOutputStream();
  }

  @Override
  public String toString() {
    return url.toString();
  }

  /**
   * @return The user agent string to pass along to the web server
   */
  public String getUserAgent() {
    return userAgent;
  }

  /**
   * Sets The user agent string to pass along to the web server
   *
   * @param userAgent the user agent
   */
  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  /**
   * @return the amount of time to wait in connecting to the host before giving up
   */
  public int getConnectionTimeOut() {
    return connectionTimeOut;
  }

  /**
   * Sets the amount of time to wait in connecting to the host before giving up
   *
   * @param connectionTimeOut The connectionTimeOut
   */
  public void setConnectionTimeOut(int connectionTimeOut) {
    this.connectionTimeOut = connectionTimeOut;
  }


}//END OF URLResource
