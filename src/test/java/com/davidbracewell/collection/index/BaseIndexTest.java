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

package com.davidbracewell.collection.index;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * @author David B. Bracewell
 */
public abstract class BaseIndexTest {

   public Index<String> getIndex() {
      return Indexes.newIndex("A", "B", "C", "D", "E");
   }


   @Test
   public void testSize() throws Exception {
      Index<String> index = getIndex();
      assertEquals(5, index.size());
      assertFalse(index.isEmpty());
      index.clear();
      assertTrue(index.isEmpty());
   }

   @Test
   public void testContains() throws Exception {
      Index<String> index = getIndex();
      assertTrue(index.contains("A"));
      assertTrue(index.contains("E"));
      assertFalse(index.contains("F"));
      assertFalse(index.contains("Z"));
   }

   @Test
   public void testIterator() throws Exception {
      Index<String> index = getIndex();
      Iterator<String> itr = index.iterator();
      assertEquals("A", itr.next());
   }

   @Test
   public void testAdd() throws Exception {
      Index<String> index = getIndex();
      assertEquals(5, index.add("G"));
      assertEquals(0, index.add("A"));
   }

   @Test
   public void testAddAll() throws Exception {
      Index<String> index = getIndex();
      assertFalse(index.contains("Z"));
      assertFalse(index.contains("Y"));
      index.addAll(Arrays.asList("Z", "Y"));
      assertTrue(index.contains("Z"));
      assertTrue(index.contains("Y"));
   }


   @Test
   public void testGet() throws Exception {
      Index<String> index = getIndex();
      assertEquals("A", index.get(0));
      assertNull(index.get(-100));
      assertNull(index.get(100));
   }

   @Test
   public void testRemove() throws Exception {
      Index<String> index = getIndex();
      assertEquals("A", index.remove(0));
      assertEquals(0, index.remove("B"));
   }

   @Test
   public void testIndexOf() throws Exception {
      Index<String> index = getIndex();
      assertEquals(0, index.getId("A"));
      assertEquals(-1, index.getId("Z"));
   }


   @Test
   public void parallelStream() throws Exception {
      Index<String> index = getIndex();
      assertEquals("A", index.parallelStream().filter(k -> k.equalsIgnoreCase("a")).findFirst().orElse(null));
   }

   @Test
   public void stream() throws Exception {
      Index<String> index = getIndex();
      assertEquals("A", index.stream().filter(k -> k.equalsIgnoreCase("a")).findFirst().orElse(null));
   }


}//END OF HashMapIndexTest
