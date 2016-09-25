package com.davidbracewell.stream;

import com.davidbracewell.collection.map.Maps;
import com.davidbracewell.config.Config;
import com.davidbracewell.stream.accumulator.*;
import com.davidbracewell.string.StringUtils;
import com.davidbracewell.tuple.Tuple2;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.davidbracewell.collection.list.Lists.asArrayList;
import static com.davidbracewell.collection.list.Lists.list;
import static org.junit.Assert.*;

/**
 * @author David B. Bracewell
 */
public abstract class BaseMStreamTest {

   StreamingContext sc;

   @Test
   public void stream() throws Exception {
      assertEquals(1, sc.stream("A").count());
      assertEquals(2, sc.stream(Arrays.asList("A", "B")).count());
      assertEquals(2, sc.stream(Arrays.asList("A", "B").iterator()).count());
      assertEquals(0, sc.stream(Stream.empty()).count());
   }

   @Test
   public void map() throws Exception {
      assertEquals(
         Arrays.asList("a", "b", "c"),
         sc.stream("A", "B", "C").map(String::toLowerCase).collect()
                  );

      assertEquals(
         Arrays.asList("a", "b", "c"),
         sc.stream(Collections.singletonList("A"), Collections.singletonList("B"), Collections.singletonList("C"))
           .map(c -> c.get(0).toLowerCase())
           .collect()
                  );
   }

   @Test
   public void minMax() throws Exception {
      assertEquals(1,
                   sc.range(1, 100).min().orElse(0).intValue()
                  );
      assertEquals(99,
                   sc.range(1, 100).max().orElse(0).intValue()
                  );
      assertEquals("A",
                   sc.stream("A", "B", "C", "a", "b", "c").min().orElse("")
                  );
      assertEquals("c",
                   sc.stream("A", "B", "C", "a", "b", "c").max().orElse("")
                  );
      assertEquals("A",
                   sc.stream("A", "B", "C", "a", "b", "c").min(String::compareToIgnoreCase).orElse("")
                  );
      assertEquals("C",
                   sc.stream("A", "B", "C", "a", "b", "c").max(String::compareToIgnoreCase).orElse("")
                  );
   }

   @Test(expected = IllegalArgumentException.class)
   public void limitError() throws Exception {
      assertEquals(Collections.emptyList(),
                   sc.range(1, 6).limit(-1).collect()
                  );
   }

   @Test
   public void limit() throws Exception {
      assertEquals(Arrays.asList(1, 2, 3, 4, 5),
                   sc.range(1, 100).limit(5).collect()
                  );

      assertEquals(Arrays.asList(1, 2, 3, 4, 5),
                   sc.range(1, 6).limit(100).collect()
                  );

      assertEquals(Collections.emptyList(),
                   sc.range(1, 6).limit(0).collect()
                  );
   }

   @Test(expected = IllegalArgumentException.class)
   public void takeError() throws Exception {
      assertEquals(Collections.emptyList(),
                   sc.range(1, 100).take(-1)
                  );
   }

   @Test
   public void take() throws Exception {
      assertEquals(Arrays.asList(1, 2, 3, 4, 5),
                   sc.range(1, 100).take(5)
                  );

      assertEquals(Arrays.asList(1, 2, 3, 4, 5),
                   sc.range(1, 6).take(100)
                  );

      assertEquals(Collections.emptyList(),
                   sc.range(1, 6).take(0)
                  );

   }


   @Test
   public void flatMap() throws Exception {
      assertEquals(Arrays.asList("A", "B", "C"),
                   sc.stream(Collections.singletonList("A"), Collections.singletonList("B"),
                             Collections.singletonList("C"))
                     .flatMap(List::stream)
                     .collect()
                  );
   }

   @Test
   public void iterator() throws Exception {
      Iterator<String> itr = sc.stream("A", "B", "C").iterator();
      assertTrue(itr.hasNext());
      assertEquals("A", itr.next());
      assertTrue(itr.hasNext());
      assertEquals("B", itr.next());
      assertTrue(itr.hasNext());
      assertEquals("C", itr.next());
      assertFalse(itr.hasNext());
   }

   @Test
   public void groupBy() throws Exception {
      Map<Character, Iterable<String>> target = new TreeMap<>(Maps.map(
         'A', Arrays.asList("Abb", "Abc"),
         'B', Arrays.asList("Bbb", "Bbc"),
         'C', Arrays.asList("Cbb", "Cbb")
                                                                      ));

      Map<Character, Iterable<String>> calc = new TreeMap<>(
                                                              sc.stream("Abb", "Abc", "Bbb", "Bbc", "Cbb", "Cbb")
                                                                .groupBy(s -> s.charAt(0))
                                                                .collectAsMap()
      );

      assertEquals(target.keySet(), calc.keySet());
      target.keySet().forEach(k -> {
         assertEquals(asArrayList(target.get(k)), asArrayList(calc.get(k)));
      });
   }

   @Test
   public void groupByError() throws Exception {
      Map<Character, List<String>> gold = Maps.treeMap(
         'A', list("Abb", "Abc"),
         'B', list("Bbb", "Bbc"),
         'C', list("Cbb", "Cbb")
                                                      );
      Map<Character, Iterable<String>> streamed = sc.stream("Abb", "Abc", "Bbb", "Bbc", "Cbb", "Cbb", null)
                                                    .filter(Objects::nonNull)
                                                    .groupBy(s -> s.charAt(0))
                                                    .collectAsMap();
      assertEquals(gold.get('A'), asArrayList(streamed.get('A')));
      assertEquals(gold.get('B'), asArrayList(streamed.get('B')));
      assertEquals(gold.get('C'), asArrayList(streamed.get('C')));
   }

   @Test
   public void countByValue() throws Exception {
      assertEquals(
         Maps.map(
            "A", 3L,
            "B", 1L,
            "C", 2L
                 ),
         sc.stream(Arrays.asList("A", "A", "A", "B", "C", "C"))
           .countByValue()
                  );
   }

   @Test
   public void sorted() throws Exception {
      assertEquals(
         Arrays.asList(1, 2, 3, 4, 5),
         sc.stream(5, 4, 2, 1, 3).sorted(true).collect()
                  );
      assertEquals(
         Arrays.asList(5, 4, 3, 2, 1),
         sc.stream(5, 4, 2, 1, 3).sorted(false).collect()
                  );
   }

   @Test
   public void zipWithIndex() throws Exception {
      List<Map.Entry<String, Long>> result = sc.stream("A", "B", "C")
                                               .zipWithIndex()
                                               .collectAsList();
      assertEquals("A", result.get(0).getKey());
      assertEquals("B", result.get(1).getKey());
      assertEquals("C", result.get(2).getKey());
      assertEquals(0L, result.get(0).getValue().longValue());
      assertEquals(1L, result.get(1).getValue().longValue());
      assertEquals(2L, result.get(2).getValue().longValue());
   }

   @Test
   public void zip() throws Exception {
      List<Map.Entry<String, String>> list = sc.stream("A", "B", "C").zip(sc.stream("B", "C", "D")).collectAsList();
      assertEquals(Tuple2.of("A", "B"), list.get(0));
      assertEquals(Tuple2.of("B", "C"), list.get(1));
      assertEquals(Tuple2.of("C", "D"), list.get(2));
   }

   @Test
   public void union() throws Exception {
      assertEquals(
         Arrays.asList("A", "B", "C"),
         sc.stream("A").union(sc.stream("B", "C")).collect()
                  );
      assertEquals(
         Arrays.asList("A"),
         sc.stream("A").union(sc.empty()).collect()
                  );

      StreamingContext other;
      if (sc instanceof LocalStreamingContext) {
         Config.setProperty("spark.master", "local[*]");
         other = StreamingContext.distributed();
      } else {
         other = StreamingContext.local();
      }

      assertEquals(
         Arrays.asList("A", "B", "C"),
         sc.stream("A").union(other.stream("B", "C")).collect()
                  );
      assertEquals(
         Arrays.asList("A"),
         sc.stream("A").union(other.empty()).collect()
                  );

   }


   @Test
   public void shuffle() throws Exception {
      List<String> orig = Arrays.asList("A", "B", "C", "D", "E");
      sc.stream(orig).shuffle();
   }

   @Test
   public void context() throws Exception {
      assertEquals(sc, sc.stream("A").getContext());
   }

   @Test
   public void parallel() throws Exception {
      assertEquals("A", sc.stream("A").parallel().collect().get(0));
   }


   @Test
   public void first() throws Exception {
      assertEquals("A", sc.stream("A").first().get());
      assertFalse(sc.empty().first().isPresent());
   }


   @Test
   public void sample() throws Exception {
      assertEquals(10, sc.range(0, 100).sample(false, 10).count());
      assertEquals(100, sc.range(0, 100).sample(false, 200).count());
      assertEquals(10, sc.range(0, 100).sample(true, 10).count());
      assertEquals(200, sc.range(0, 100).sample(true, 200).count());
   }

   @Test(expected = IllegalArgumentException.class)
   public void sampleError() throws Exception {
      assertEquals(0, sc.range(0, 100).sample(false, -1).count());
   }

   @Test
   public void isEmpty() throws Exception {
      assertTrue(sc.empty().isEmpty());
   }

   @Test
   public void skip() throws Exception {
      assertEquals("A", sc.stream("B", "C", "A").skip(2).first().get());
      assertFalse(sc.stream("B", "C", "A").skip(5).first().isPresent());
   }

   @Test
   public void distinct() throws Exception {
      assertEquals("A", sc.stream("A", "A", "A").distinct().collect().get(0));
      assertEquals("A", sc.stream("A", "A", "A").distinct().collect().get(0));
   }


   @Test
   public void filter() throws Exception {
      List<String> filtered = sc.stream("A", "B", "c", "e").filter(s -> Character.isLowerCase(s.charAt(0))).collect();
      assertEquals("c", filtered.get(0));
      assertEquals("e", filtered.get(1));
   }

   @Test
   public void reduce() throws Exception {
      assertEquals(10, sc.stream(1, 2, 3, 4).reduce((x, y) -> x + y).orElse(0).intValue());
      assertEquals("ABC", sc.stream("A", "B", "C").reduce((x, y) -> x + y).orElse(""));
   }


   @Test
   public void fold() throws Exception {
      assertEquals(10, sc.stream(1, 2, 3, 4).fold(0, (x, y) -> x + y).intValue());
      assertEquals("ABC", sc.stream("A", "B", "C").fold("", (x, y) -> x + y));
   }


   @Test
   public void partitionAndSplit() throws Exception {
      MStream<String> stream = sc.stream("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
      List<Iterable<String>> partitions = stream.split(3).collect();
      assertEquals(3, partitions.size());
      assertEquals(list("A", "B", "C"), asArrayList(partitions.get(0)));
      assertEquals(list("D", "E", "F"), asArrayList(partitions.get(1)));
      assertEquals(list("G", "H", "I", "J"), asArrayList(partitions.get(2)));

      partitions = new LocalStream<>(stream.javaStream()).split(3).collect();
      assertEquals(3, partitions.size());
      assertEquals(list("A", "B", "C"), asArrayList(partitions.get(0)));
      assertEquals(list("D", "E", "F"), asArrayList(partitions.get(1)));
      assertEquals(list("G", "H", "I", "J"), asArrayList(partitions.get(2)));


      partitions = stream.partition(3).collect();
      assertEquals(4, partitions.size());
      assertEquals(list("A", "B", "C"), asArrayList(partitions.get(0)));
      assertEquals(list("D", "E", "F"), asArrayList(partitions.get(1)));
      assertEquals(list("G", "H", "I"), asArrayList(partitions.get(2)));
      assertEquals(list("J"), asArrayList(partitions.get(3)));

      partitions = new LocalStream<>(stream.javaStream()).partition(3).collect();
      assertEquals(4, partitions.size());
      assertEquals(list("A", "B", "C"), asArrayList(partitions.get(0)));
      assertEquals(list("D", "E", "F"), asArrayList(partitions.get(1)));
      assertEquals(list("G", "H", "I"), asArrayList(partitions.get(2)));
      assertEquals(list("J"), asArrayList(partitions.get(3)));
   }

   @Test
   public void streamOps() throws Exception {
      MStream<String> stream = sc.stream("A", "V", "D");

      AtomicBoolean closed = new AtomicBoolean(false);
      stream.cache();
      stream.repartition(10);
      stream.onClose(() -> closed.set(true));
      stream.close();

      assertTrue(closed.get());
   }

   @Test
   public void flatMapToPair() throws Exception {
      Map<String, Boolean> g = sc.stream("AB", "BC", "Aa").flatMapToPair(s -> {
                                                                            List<Map.Entry<String, Boolean>> result = new ArrayList<>();
                                                                            for (char c : s.toCharArray()) {
                                                                               result.add(Tuple2.of(Character.toString(c), Character.isUpperCase(c)));
                                                                            }
                                                                            return result.stream();
                                                                         }
                                                                        ).collectAsMap();

      assertTrue(g.get("A"));
      assertTrue(g.get("B"));
      assertTrue(g.get("C"));
      assertFalse(g.get("a"));
   }


   @Test
   public void mapToPair() throws Exception {
      Map<String, Boolean> g = sc.stream("AB", "BC", "Aa")
                                 .mapToPair(s -> Tuple2.of(s, StringUtils.isUpperCase(s)))
                                 .collectAsMap();
      assertTrue(g.get("AB"));
      assertTrue(g.get("BC"));
      assertFalse(g.get("Aa"));
   }


   @Test
   public void accumulators() throws Exception {
      MDoubleAccumulator dA = sc.doubleAccumulator(0d);
      MLongAccumulator iA = sc.longAccumulator(0);
      MListAccumulator<String> sA = sc.listAccumulator();
      MCounterAccumulator<String> cA = sc.counterAccumulator();
      MMapAccumulator<String, Integer> mA = sc.mapAccumulator();
      sc.stream("A", "B", "CC", "A").forEach(s -> {
         dA.add((double) s.length());
         iA.add(s.length());
         sA.add(s);
         mA.put(s, s.length());
         cA.add(s);
      });

      assertEquals(5.0, dA.value(), 0.0);
      assertEquals(5, iA.value().intValue());
      assertEquals(4, sA.value().size());
      assertEquals(1, mA.value().get("A").intValue());
      assertEquals(1, mA.value().get("B").intValue());
      assertEquals(2, mA.value().get("CC").intValue());
      assertEquals(2.0, cA.value().get("A"), 0.0);
      assertEquals(1.0, cA.value().get("B"), 0.0);
      assertEquals(1.0, cA.value().get("CC"), 0.0);
   }

   @Test
   public void mapToDouble() throws Exception {
      assertEquals(
         6.0,
         sc.stream("1.0", "2.0", "3.0").mapToDouble(Double::parseDouble).sum(),
         0.0
                  );
   }
}//END OF BaseMStreamTest
