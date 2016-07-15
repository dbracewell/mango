package com.davidbracewell.stream;

import com.davidbracewell.config.Config;
import com.davidbracewell.string.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author David B. Bracewell
 */
public abstract class BaseDoubleStreamTest {

  StreamingContext sc;

  @Test
  public void streamOps() throws Exception {
    MDoubleStream stream = sc.doubleStream(1.0, 2.0, 3, 4);
    assertEquals(sc, stream.getContext());
    AtomicBoolean closed = new AtomicBoolean(false);
    stream.cache();
    stream.repartition(10);
    stream.onClose(() -> {
      closed.set(true);
    });
    stream.close();
    assertTrue(closed.get());
  }

  @Test
  public void sum() throws Exception {
    assertEquals(
      10.0,
      sc.doubleStream(1.0, 2.0, 3, 4).sum(),
      0.0
    );
    assertEquals(
      0.0,
      sc.emptyDouble().sum(),
      0.0
    );
  }

  @Test
  public void min() throws Exception {
    assertEquals(
      1.0,
      sc.doubleStream(1.0, 2.0, 3, 4).min().orElse(Double.NaN),
      0.0
    );
    assertFalse(sc.emptyDouble().min().isPresent());
  }

  @Test
  public void max() throws Exception {
    assertEquals(
      4.0,
      sc.doubleStream(1.0, 2.0, 3, 4).max().orElse(Double.NaN),
      0.0
    );
    assertFalse(sc.emptyDouble().max().isPresent());
  }

  @Test
  public void stddev() throws Exception {
    assertEquals(
      1.2,
      sc.doubleStream(1.0, 2.0, 3, 4).stddev(),
      0.1
    );

    assertEquals(
      Double.NaN,
      sc.emptyDouble().stddev(),
      0.0
    );

  }

  @Test
  public void mean() throws Exception {
    assertEquals(
      2.5,
      sc.doubleStream(1.0, 2.0, 3, 4).mean(),
      0.1
    );

    assertEquals(
      0.0,
      sc.emptyDouble().mean(),
      0.0
    );

  }


  @Test
  public void count() throws Exception {
    assertEquals(
      4,
      sc.doubleStream(1.0, 2.0, 3, 4).count(),
      0
    );

    assertEquals(
      0.0,
      sc.emptyDouble().count(),
      0.0
    );

  }

  @Test
  public void first() throws Exception {
    assertEquals(
      1.0,
      sc.doubleStream(1.0, 2.0, 3, 4).first().orElse(Double.NaN),
      0
    );

    assertFalse(sc.emptyDouble().first().isPresent());
  }

  @Test
  public void mapToObj() throws Exception {
    List<String> strings = sc.doubleStream(1, 2, 1).mapToObj(d -> StringUtils.randomHexString((int) d)).collect();
    assertEquals(1, strings.get(0).length());
    assertEquals(2, strings.get(1).length());
    assertEquals(1, strings.get(2).length());
  }


  @Test
  public void map() throws Exception {
    assertEquals(
      14.0,
      sc.doubleStream(1.0, 2.0, 3.0).map(d -> d * d).sum(),
      0.1
    );
  }

  @Test
  public void matches() throws Exception {
    assertTrue(sc.doubleStream(1.0, 2.0, 3.0).allMatch(Double::isFinite));
    assertTrue(sc.doubleStream(1.0, 2.0, 3.0, Double.NaN).anyMatch(Double::isNaN));
    assertTrue(sc.doubleStream(1.0, 2.0, 3.0).noneMatch(Double::isNaN));
    assertTrue(sc.emptyDouble().noneMatch(Double::isNaN));
    assertFalse(sc.emptyDouble().anyMatch(Double::isNaN));
    assertTrue(sc.emptyDouble().allMatch(Double::isNaN));
  }

  @Test
  public void distinctArray() throws Exception {
    assertArrayEquals(
      new double[]{1, 2, 3},
      sc.doubleStream(1, 1, 2, 2, 3, 3).distinct().sorted().toArray(),
      0.1
    );
  }


  @Test
  public void filter() throws Exception {
    assertEquals(
      1,
      sc.doubleStream(Double.NaN, Double.POSITIVE_INFINITY, 10).filter(Double::isFinite).count()
    );
  }

  @Test
  public void union() throws Exception {
    MDoubleStream d1 = sc.doubleStream(1, 2, 3, 4);
    MDoubleStream d2 = sc.doubleStream(5);
    assertEquals(
      5,
      d1.union(d2).count()
    );

    d1 = sc.doubleStream(1, 2, 3, 4);
    if( sc instanceof SparkStreamingContext ){
      Config.setProperty("spark.master", "local[*]");
      d2 = StreamingContext.distributed().doubleStream(5);
    } else {
      d2 = StreamingContext.local().doubleStream(5);
    }

    assertEquals(
      5,
      d1.union(d2).count()
    );
  }
}// END OF BaseDoubleStreamTest
