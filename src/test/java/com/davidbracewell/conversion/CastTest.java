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

package com.davidbracewell.conversion;

import org.junit.Test;

import java.util.*;

import static com.davidbracewell.collection.Sets.*;
import static com.davidbracewell.collection.list.Lists.asArrayList;
import static com.davidbracewell.collection.list.Lists.list;
import static com.davidbracewell.conversion.Cast.as;
import static org.junit.Assert.*;

public class CastTest {

   @Test
   public void testUnsafeAs() throws Exception {
      assertEquals(1d, Cast.<Double>as(1d), 0d);
   }

   @Test(expected = ClassCastException.class)
   public void testBadUnsafeAs() throws Exception {
      assertEquals(1d, Cast.<Double>as(1L), 0d);
   }

   @Test
   public void testUnsafeNull() throws Exception {
      assertNull(Cast.<Double>as(null));
   }

   @Test
   public void testSafeAs() throws Exception {
      assertEquals(1d, as(1d, Double.class), 0d);
   }

   @Test
   public void testBadSafeAs() throws Exception {
      assertNull(as(1L, Double.class));
   }

   @Test
   public void testSafeNull() throws Exception {
      assertNull(as(null, Double.class));
   }


   @Test
   public void testAs() throws Exception {
      Double d = as(1.0);
      assertEquals(1d, d, 0);

      d = as("no", Double.class);
      assertNull(d);
   }

   @Test(expected = ClassCastException.class)
   public void testAsBad() throws Exception {
      Double d = Cast.<Double>as("no");
      assertFalse(d == 0);
   }

   @Test
   public void testCollections() throws Exception {
      List<?> l = Arrays.asList(1.0, 2.0, 3.0);
      Collection<?> c = l;
      Iterable<?> i = l;
      Set<?> s = asSet(l);

      assertEquals(list(1d, 2d, 3d), Cast.<Double>cast(l));
      assertEquals(list(1d, 2d, 3d), asArrayList(Cast.<Double>cast(l.iterator())));
      assertEquals(list(1d, 2d, 3d), asArrayList(Cast.<Double>cast(c)));
      assertEquals(list(1d, 2d, 3d), asArrayList(Cast.<Double>cast(i)));
      assertEquals(treeSet(1d, 2d, 3d), asTreeSet(Cast.<Double>cast(s)));

   }

   @Test
   public void testMap() throws Exception {
      Map<Object, Object> m = new HashMap<>();
      m.put(1, 2d);
      Map<Integer, Double> m2 = Cast.cast(m);
      for (Map.Entry<Integer, Double> e : m2.entrySet()) {
         assertEquals((Integer) 1, e.getKey());
         assertEquals(2d, e.getValue(), 0);
      }


   }

}//END OF CastTest
