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

package com.davidbracewell.reflection;

import com.davidbracewell.config.Config;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * A cache of {@link BeanDescriptor} to speed up performance of using {@link BeanMap}s. The cache specification be set
 * using <code>com.davidbracewell.reflection.BeanDescriptorCache.spec</code> and follows the {@link com.google.common.cache.LoadingCache} spec
 * format found in
 * <a href= "http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/CacheBuilder.html" >the
 * Guava documentation.By defaul it set to
 * <p/>
 * <pre>
 * maximumSize=100,expireAfterWrite=10m
 * </pre>
 * <p/>
 * </a>
 *
 * @author David B. Bracewell
 */
public class BeanDescriptorCache {

  private static volatile BeanDescriptorCache INSTANCE = null;

  private final LoadingCache<Class<?>, BeanDescriptor> cache;

  /**
   * @return An instance of the {@link BeanDescriptorCache}.
   */
  public static BeanDescriptorCache getInstance() {
    if (INSTANCE == null) {
      synchronized (BeanDescriptorCache.class) {
        if (INSTANCE == null) {
          INSTANCE = new BeanDescriptorCache();
        }
      }
    }
    return INSTANCE;
  }

  protected BeanDescriptorCache() {
    String spec = Config.get(BeanDescriptorCache.class, "spec").asString("maximumSize=100,expireAfterWrite=10m");
    cache = CacheBuilder.from(spec).build(new CacheLoader<Class<?>, BeanDescriptor>() {
      public BeanDescriptor load(Class<?> key) {
        return new BeanDescriptor(key);
      }
    });
  }

  /**
   * Gets a {@link BeanDescriptor} for a <code>Class</code>
   *
   * @param clazz The class for which a {@link BeanDescriptor} is to be created.
   * @return A {@link BeanDescriptor} for the given class
   */
  public synchronized BeanDescriptor get(Class<?> clazz) {
    try {
      return cache.get(clazz);
    } catch (ExecutionException e) {
      throw Throwables.propagate(e);
    }
  }

}// END OF CLASS BeanDescriptorCache