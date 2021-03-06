package com.davidbracewell.collection;

import com.davidbracewell.io.resource.Resource;
import com.davidbracewell.io.resource.StringResource;
import org.junit.Test;

import java.io.InputStream;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.davidbracewell.collection.list.Lists.list;
import static org.junit.Assert.*;

/**
 * @author David B. Bracewell
 */
public class StreamsTest {


   @Test
   public void inputStream() throws Exception {
      Resource r = new StringResource("This is a test");
      InputStream is = r.inputStream();
      try (Stream<String> stream = Streams.asStream(is)) {
         assertEquals("This is a test", stream.collect(Collectors.joining()));
      }
      try {
         int i = is.read();
      } catch (Exception e) {
         assertEquals("Stream closed", e.getMessage());
      }
   }

   @Test
   public void reader() throws Exception {
      Resource r = new StringResource("This is a test");
      Reader reader = r.reader();
      try (Stream<String> stream = Streams.asStream(reader)) {
         assertEquals("This is a test", stream.collect(Collectors.joining()));
      }
      try {
         int i = reader.read();
      } catch (Exception e) {
         assertEquals("Stream closed", e.getMessage());
      }
   }

   @Test
   public void iterator() throws Exception {
      Iterator<String> it = null;
      assertEquals(0, Streams.asStream(it).count());
      assertEquals("A, B, C",
                   Streams.asStream(Arrays.asList("A", "B", "C").iterator()).collect(Collectors.joining(", "))
                  );
      assertEquals("A, B, C",
                   Streams.asParallelStream(Arrays.asList("A", "B", "C").iterator()).collect(Collectors.joining(", "))
                  );

   }

   @Test
   public void iterable() throws Exception {
      Iterable<String> it = null;
      assertEquals(0, Streams.asStream(it).count());
      assertEquals("A, B, C",
                   Streams.asStream(Arrays.asList("A", "B", "C")).collect(Collectors.joining(", "))
                  );
      assertEquals("A, B, C",
                   Streams.asParallelStream(Arrays.asList("A", "B", "C")).collect(Collectors.joining(", "))
                  );
   }


   @Test
   public void zip() throws Exception {
      assertEquals(0L, Streams.zip(null, null).count());
      assertEquals(list(new AbstractMap.SimpleEntry<>("A", 1),
                        new AbstractMap.SimpleEntry<>("B", 2),
                        new AbstractMap.SimpleEntry<>("C", 3)
                       ),
                   Streams.zip(Stream.of("A", "B", "C"), Stream.of(1, 2, 3, 4)).collect(Collectors.toList())
                  );
      assertEquals(0L, Streams.zip(Stream.empty(), Stream.of(1, 2, 3, 4)).count());
   }

   @Test
   public void zipWithIndex() throws Exception {
      assertEquals(0L, Streams.zipWithIndex(null).count());
      assertEquals(list(new AbstractMap.SimpleEntry<>("A", 0),
                        new AbstractMap.SimpleEntry<>("B", 1),
                        new AbstractMap.SimpleEntry<>("C", 2)
                       ),
                   Streams.zipWithIndex(Stream.of("A", "B", "C")).collect(Collectors.toList())
                  );
      assertEquals(0L, Streams.zipWithIndex(Stream.empty()).count());
   }
}