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

package com.davidbracewell.collection.counter;

import com.davidbracewell.Math2;
import com.davidbracewell.collection.Sets;
import com.davidbracewell.tuple.Tuple3;
import org.junit.Test;

import java.util.*;

import static com.davidbracewell.tuple.Tuples.$;
import static org.junit.Assert.*;

/**
 * @author David B. Bracewell
 */
public abstract class BaseMultiCounterTest {

   public MultiCounter<String, String> getEmptyCounter() {
      return MultiCounters.newMultiCounter();
   }

   public MultiCounter<String, String> getEntryCounter() {
      return MultiCounters.newMultiCounter($("A", "B"),
                                           $("A", "C"),
                                           $("A", "D"),
                                           $("B", "E"),
                                           $("B", "G"),
                                           $("B", "H")
                                          );
   }


   @Test
   public void clear() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      assertEquals(6, mc.size(), 0);
      assertFalse(mc.isEmpty());
      mc.clear();
      assertEquals(0, mc.size(), 0);
      assertTrue(mc.isEmpty());
   }

   @Test
   public void contains() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      assertTrue(mc.contains("A"));
      assertTrue(mc.contains("A", "B"));
      assertTrue(mc.contains("B"));
      assertFalse(mc.contains("D"));
   }

   @Test
   public void items() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      assertEquals(Sets.set("A", "B"), mc.firstKeys());
      assertEquals(6, mc.sum(), 0);
      mc.firstKeys().remove("A");
      assertEquals(Sets.set("B"), mc.firstKeys());
      assertEquals(3, mc.sum(), 0);
   }

   @Test
   public void counts() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      assertEquals(6, Math2.sum(mc.values()), 0);
      Iterator<Double> itr = mc.values().iterator();
      itr.next();
      itr.remove();
      assertEquals(5, Math2.sum(mc.values()), 0);
   }

   @Test
   public void entries() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      Set<Tuple3<String, String, Double>> entries = mc.entries();
      assertEquals(6, entries.stream().mapToDouble(Tuple3::getV3).sum(), 0);
      entries.removeIf(t -> t.v1.equals("A") && t.v2.equals("B"));
      assertEquals(2, mc.get("A").sum(), 0);
      assertEquals(5, entries.stream().mapToDouble(Tuple3::getV3).sum(), 0);
   }

   @Test
   public void get() throws Exception {
      MultiCounter<String, String> mc = getEmptyCounter();
      assertEquals(0, mc.sum(), 0);

      Counter<String> z = mc.get("Z");
      assertEquals(0, z.sum(), 0);
      assertEquals(0, z.remove("e"), 0);

      z.increment("e", 5);
      assertEquals(5, mc.sum(), 0);
      assertEquals(5, z.sum(), 0);

      z.set("a", 1);
      assertEquals(6, mc.sum(), 0);
      assertEquals(5, mc.get("Z", "e"), 0);
      assertEquals(6, z.sum(), 0);
      assertEquals(1, mc.get("Z", "a"), 0);

      z.clear();
      assertEquals(0, mc.sum(), 0);
      assertEquals(0, z.sum(), 0);
      assertTrue(mc.firstKeys().isEmpty());
   }


   @Test
   public void incrementAndSet() throws Exception {
      MultiCounter<String, String> mc = getEmptyCounter();
      mc.increment("A", "B");
      mc.increment("A", "B", 100);
      mc.decrement("B", "B", 100);
      mc.decrement("B", "A");
      assertEquals(0, mc.sum(), 0);
      assertEquals(101, mc.get("A").sum(), 0);
      assertEquals(-101, mc.get("B").sum(), 0);

      mc.set("B", "B", 0);
      assertEquals(0, mc.get("B", "B"), 0);
      mc.clear();
      mc.set("A", Counters.newCounter("B", "C", "D", "E"));
      assertEquals(4, mc.get("A").sum(), 0);
   }


   @Test
   public void remove() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      assertEquals(1, mc.remove("A", "B"), 0);
      assertEquals(1, mc.remove("A", "C"), 0);
      mc.remove("A", "D");
      assertEquals(Sets.set("B"), mc.firstKeys());
      mc.increment("A", "B");
      mc.remove("A");
      assertEquals(Sets.set("B"), mc.firstKeys());
      mc.removeAll(Collections.singleton("B"));
      assertTrue(mc.isEmpty());
   }


   @Test
   public void adjustValues() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      mc.adjustValuesSelf(d -> d * d);
      assertEquals(6, mc.sum(), 0);
      MultiCounter<String, String> mc2 = mc.adjustValues(d -> d + 1);
      assertEquals(6, mc.sum(), 0);
      assertEquals(12, mc2.sum(), 0);
   }

   @Test
   public void stats() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      assertEquals(1, mc.average(), 0);
      assertEquals(0, mc.standardDeviation(), 0);
      assertEquals(Math.sqrt(6), mc.magnitude(), 0);
      assertEquals(1, mc.maximumCount(), 0);
      assertEquals(1, mc.minimumCount(), 0);
   }


   @Test
   public void filter() throws Exception {
      MultiCounter<String, String> mc = getEntryCounter();
      MultiCounter<String, String> mc2 = mc.filterByFirstKey(k -> k.equalsIgnoreCase("a"));
      assertEquals(Sets.set("A"), mc2.firstKeys());
      assertEquals(3, mc2.sum(), 0);
      mc2 = mc.filterBySecondKey(k -> k.equalsIgnoreCase("H"));
      assertEquals(Sets.set("B"), mc2.firstKeys());
      assertEquals(1, mc2.sum(), 0);
      mc2 = mc.filterByValue(d -> d > 1);
      assertTrue(mc2.isEmpty());
   }

   @Test
   public void itemsByCount() throws Exception {
      MultiCounter<String, String> mc = getEmptyCounter();
      mc.merge(MultiCounters.newMultiCounter($("A", "B")));
      mc.merge(MultiCounters.newMultiCounter($("A", "B")));
      mc.merge(MultiCounters.newMultiCounter($("A", "B")));
      mc.merge(MultiCounters.newMultiCounter($("A", "C"),
                                             $("A", "C"),
                                             $("A", "A")
                                            ));

      List<Map.Entry<String, String>> items = mc.itemsByCount(false);
      assertEquals(3, items.size(), 0);
      assertEquals($("A", "B"), items.get(0));
      assertEquals($("A", "C"), items.get(1));
      assertEquals($("A", "A"), items.get(2));
   }

}//END OF BaseMultiCounterTest
