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

package com.davidbracewell.cache.impl;

import com.davidbracewell.cache.Cache;
import com.davidbracewell.cache.CacheSpec;
import com.davidbracewell.cache.NoOptCache;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author David B. Bracewell
 */
public class NoOptCacheTest {

  private Cache<String, String> cache;

  @Before
  public void setUp() throws Exception {
    cache = new NoOptCache<>(new CacheSpec<String, String>());
    cache.put("var", "element");
  }

  @Test
  public void testCache() throws Exception {
    assertEquals(0, cache.size());
    cache.clear();
    cache.invalidateAll(Arrays.asList("A"));
    cache.invalidate("A");
    assertFalse(cache.containsKey("var"));
    assertNull(cache.get("A"));
  }

}
