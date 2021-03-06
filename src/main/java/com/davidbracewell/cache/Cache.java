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
package com.davidbracewell.cache;


import com.davidbracewell.function.SerializableSupplier;
import lombok.NonNull;

import java.util.concurrent.ExecutionException;

/**
 * <p>A generic cache interface that allows multiple implementations, definition through specification, management, and
 * auto cached interfaces.</p>
 *
 * @param <K> the Key parameter
 * @param <V> the Value parameter
 * @author David B. Bracewell
 */
public interface Cache<K, V> {

   /**
    * Determines if a key is in the cache or not
    *
    * @param key The key to check
    * @return True if the key is in the cache, False if not
    */
   boolean containsKey(K key);

   /**
    * Gets the value associated with a key
    *
    * @param key The key
    * @return The value associated with the key or null
    */
   V get(K key);

   /**
    * Gets name.
    *
    * @return The name of the cache
    */
   String getName();

   /**
    * Removes a single key
    *
    * @param key The key to remove
    */
   void invalidate(K key);

   /**
    * Clears the cache of all given keys
    *
    * @param keys The keys to remove
    */
   void invalidateAll(@NonNull Iterable<? extends K> keys);


   /**
    * Clears the cache
    */
   void invalidateAll();

   /**
    * Adds a key value pair to the cache overwriting any value that is there
    *
    * @param key   The key
    * @param value The value
    */
   void put(K key, V value);

   /**
    * Gets the value associated with the given key when available and if not available calculates and stores the value
    * using the given supplier.
    *
    * @param key      The key
    * @param supplier The supplier to use to generate the value
    * @return The old value if put, null if not
    */
   V get(K key, SerializableSupplier<? extends V> supplier) throws ExecutionException;

   /**
    * The number of items cached.
    *
    * @return The current size of the cache
    */
   long size();

   /**
    * Determines if the cache is empty or not
    *
    * @return True if empty, False if not
    */
   default boolean isEmpty() {
      return size() == 0;
   }


}//END OF Cache
