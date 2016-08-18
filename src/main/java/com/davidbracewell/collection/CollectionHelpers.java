package com.davidbracewell.collection;

import com.davidbracewell.conversion.Cast;
import lombok.NonNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.davidbracewell.tuple.Tuples.$;

/**
 * The interface Collection helpers.
 *
 * @author David B. Bracewell
 */
public interface CollectionHelpers {


  /**
   * As map map.
   *
   * @param <K>     the type parameter
   * @param <V>     the type parameter
   * @param entries the entries
   * @return the map
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <K, V> Map<K, V> asMap(Map.Entry<K, V>... entries) {
    return createMap(HashMap::new, entries);
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1) {
    return createMap(HashMap::new, $(key1, value1));
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2) {
    return createMap(HashMap::new, $(key1, value1), $(key2, value2));
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3) {
    return createMap(HashMap::new, $(key1, value1), $(key2, value2), $(key3, value3));
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
    return createMap(HashMap::new, $(key1, value1), $(key2, value2), $(key3, value3), $(key4, value4));
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
    return createMap(HashMap::new, $(key1, value1), $(key2, value2), $(key3, value3), $(key4, value4), $(key5, value5));
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
    return createMap(HashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6)
    );
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
    return createMap(HashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7)
    );
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @param key8   the key 8
   * @param value8 the value 8
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8) {
    return createMap(HashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8)
    );
  }

  /**
   * As map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @param key8   the key 8
   * @param value8 the value 8
   * @param key9   the key 9
   * @param value9 the value 9
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9) {
    return createMap(HashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8),
                     $(key9, value9)
    );
  }

  /**
   * As map map.
   *
   * @param <K>     the type parameter
   * @param <V>     the type parameter
   * @param key1    the key 1
   * @param value1  the value 1
   * @param key2    the key 2
   * @param value2  the value 2
   * @param key3    the key 3
   * @param value3  the value 3
   * @param key4    the key 4
   * @param value4  the value 4
   * @param key5    the key 5
   * @param value5  the value 5
   * @param key6    the key 6
   * @param value6  the value 6
   * @param key7    the key 7
   * @param value7  the value 7
   * @param key8    the key 8
   * @param value8  the value 8
   * @param key9    the key 9
   * @param value9  the value 9
   * @param key10   the key 10
   * @param value10 the value 10
   * @return the map
   */
  static <K, V> Map<K, V> asMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10) {
    return createMap(HashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8),
                     $(key9, value9),
                     $(key10, value10)
    );
  }


  /**
   * As tree map map.
   *
   * @param <K>     the type parameter
   * @param <V>     the type parameter
   * @param entries the entries
   * @return the map
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <K, V> Map<K, V> asTreeMap(Map.Entry<K, V>... entries) {
    return createMap(TreeMap::new, entries);
  }


  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1) {
    return createMap(TreeMap::new, $(key1, value1));
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2) {
    return createMap(TreeMap::new, $(key1, value1), $(key2, value2));
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3) {
    return createMap(TreeMap::new, $(key1, value1), $(key2, value2), $(key3, value3));
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
    return createMap(TreeMap::new, $(key1, value1), $(key2, value2), $(key3, value3), $(key4, value4));
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
    return createMap(TreeMap::new, $(key1, value1), $(key2, value2), $(key3, value3), $(key4, value4), $(key5, value5));
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
    return createMap(TreeMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6)
    );
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
    return createMap(TreeMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7)
    );
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @param key8   the key 8
   * @param value8 the value 8
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8) {
    return createMap(TreeMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8)
    );
  }

  /**
   * As tree map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @param key8   the key 8
   * @param value8 the value 8
   * @param key9   the key 9
   * @param value9 the value 9
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9) {
    return createMap(TreeMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8),
                     $(key9, value9)
    );
  }

  /**
   * As tree map map.
   *
   * @param <K>     the type parameter
   * @param <V>     the type parameter
   * @param key1    the key 1
   * @param value1  the value 1
   * @param key2    the key 2
   * @param value2  the value 2
   * @param key3    the key 3
   * @param value3  the value 3
   * @param key4    the key 4
   * @param value4  the value 4
   * @param key5    the key 5
   * @param value5  the value 5
   * @param key6    the key 6
   * @param value6  the value 6
   * @param key7    the key 7
   * @param value7  the value 7
   * @param key8    the key 8
   * @param value8  the value 8
   * @param key9    the key 9
   * @param value9  the value 9
   * @param key10   the key 10
   * @param value10 the value 10
   * @return the map
   */
  static <K, V> Map<K, V> asTreeMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10) {
    return createMap(TreeMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8),
                     $(key9, value9),
                     $(key10, value10)
    );
  }


  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1) {
    return createMap(LinkedHashMap::new, $(key1, value1));
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2) {
    return createMap(LinkedHashMap::new, $(key1, value1), $(key2, value2));
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3) {
    return createMap(LinkedHashMap::new, $(key1, value1), $(key2, value2), $(key3, value3));
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
    return createMap(LinkedHashMap::new, $(key1, value1), $(key2, value2), $(key3, value3), $(key4, value4));
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
    return createMap(LinkedHashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5)
    );
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
    return createMap(LinkedHashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6)
    );
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
    return createMap(LinkedHashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7)
    );
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @param key8   the key 8
   * @param value8 the value 8
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8) {
    return createMap(LinkedHashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8)
    );
  }

  /**
   * As linked hash map map.
   *
   * @param <K>    the type parameter
   * @param <V>    the type parameter
   * @param key1   the key 1
   * @param value1 the value 1
   * @param key2   the key 2
   * @param value2 the value 2
   * @param key3   the key 3
   * @param value3 the value 3
   * @param key4   the key 4
   * @param value4 the value 4
   * @param key5   the key 5
   * @param value5 the value 5
   * @param key6   the key 6
   * @param value6 the value 6
   * @param key7   the key 7
   * @param value7 the value 7
   * @param key8   the key 8
   * @param value8 the value 8
   * @param key9   the key 9
   * @param value9 the value 9
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9) {
    return createMap(LinkedHashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8),
                     $(key9, value9)
    );
  }

  /**
   * As linked hash map map.
   *
   * @param <K>     the type parameter
   * @param <V>     the type parameter
   * @param key1    the key 1
   * @param value1  the value 1
   * @param key2    the key 2
   * @param value2  the value 2
   * @param key3    the key 3
   * @param value3  the value 3
   * @param key4    the key 4
   * @param value4  the value 4
   * @param key5    the key 5
   * @param value5  the value 5
   * @param key6    the key 6
   * @param value6  the value 6
   * @param key7    the key 7
   * @param value7  the value 7
   * @param key8    the key 8
   * @param value8  the value 8
   * @param key9    the key 9
   * @param value9  the value 9
   * @param key10   the key 10
   * @param value10 the value 10
   * @return the map
   */
  static <K, V> Map<K, V> asLinkedHashMap(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10) {
    return createMap(LinkedHashMap::new,
                     $(key1, value1),
                     $(key2, value2),
                     $(key3, value3),
                     $(key4, value4),
                     $(key5, value5),
                     $(key6, value6),
                     $(key7, value7),
                     $(key8, value8),
                     $(key9, value9),
                     $(key10, value10)
    );
  }


  /**
   * As linked hash map map.
   *
   * @param <K>     the type parameter
   * @param <V>     the type parameter
   * @param entries the entries
   * @return the map
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <K, V> Map<K, V> asLinkedHashMap(Map.Entry<K, V>... entries) {
    return createMap(LinkedHashMap::new, entries);
  }


  /**
   * As map map.
   *
   * @param <K>         the type parameter
   * @param <V>         the type parameter
   * @param mapSupplier the map supplier
   * @param entries     the entries
   * @return the map
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <K, V> Map<K, V> createMap(@NonNull Supplier<Map<K, V>> mapSupplier, Map.Entry<K, V>... entries) {
    if (entries == null) {
      return Collections.emptyMap();
    }
    final Map<K, V> map = mapSupplier.get();
    asStream(entries).forEach(e -> map.put(e.getKey(), e.getValue()));
    return map;
  }


  /**
   * As stream stream.
   *
   * @param <T>    the type parameter
   * @param values the values
   * @return the stream
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T> Stream<T> asStream(T... values) {
    if (values == null) {
      return Stream.empty();
    }
    return Stream.of(values);
  }

  /**
   * As stream stream.
   *
   * @param <T>   the type parameter
   * @param value the value
   * @return the stream
   */
  static <T> Stream<T> asStream(T value) {
    if (value == null) {
      return Stream.empty();
    }
    return Stream.of(value);
  }

  /**
   * As stream stream.
   *
   * @param <T>      the type parameter
   * @param iterator the iterator
   * @return the stream
   */
  static <T> Stream<T> asStream(Iterator<? extends T> iterator) {
    if (iterator == null) {
      return Stream.empty();
    }
    return asStream(iterator, false);
  }

  /**
   * As parallel stream stream.
   *
   * @param <T>      the type parameter
   * @param iterator the iterator
   * @return the stream
   */
  static <T> Stream<T> asParallelStream(Iterator<? extends T> iterator) {
    if (iterator == null) {
      return Stream.empty();
    }
    return asStream(iterator, true);
  }

  /**
   * As stream stream.
   *
   * @param <T>      the type parameter
   * @param iterator the iterator
   * @param parallel the parallel
   * @return the stream
   */
  static <T> Stream<T> asStream(Iterator<? extends T> iterator, boolean parallel) {
    if (iterator == null) {
      return Stream.empty();
    }
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), parallel);
  }

  /**
   * As stream stream.
   *
   * @param <T>      the type parameter
   * @param iterable the iterable
   * @return the stream
   */
  static <T> Stream<T> asStream(Iterable<? extends T> iterable) {
    if (iterable == null) {
      return Stream.empty();
    }
    return asStream(iterable, false);
  }

  /**
   * As parallel stream stream.
   *
   * @param <T>      the type parameter
   * @param iterable the iterable
   * @return the stream
   */
  static <T> Stream<T> asParallelStream(Iterable<? extends T> iterable) {
    if (iterable == null) {
      return Stream.empty();
    }
    return asStream(iterable, true);
  }

  /**
   * As stream stream.
   *
   * @param <T>      the type parameter
   * @param iterable the iterable
   * @param parallel the parallel
   * @return the stream
   */
  static <T> Stream<T> asStream(Iterable<? extends T> iterable, boolean parallel) {
    if (iterable == null) {
      return Stream.empty();
    }
    return StreamSupport.stream(Cast.as(iterable.spliterator()), parallel);
  }

  /**
   * As list list.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param first  the first
   * @param others the others
   * @return the list
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> List<T> list(Y first, Y... others) {
    return createList(ArrayList::new, first, others);
  }

  /**
   * As linked list list.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param first  the first
   * @param others the others
   * @return the list
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> List<T> linkedList(Y first, Y... others) {
    return createList(LinkedList::new, first, others);
  }

  /**
   * As sorted list list.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param first  the first
   * @param others the others
   * @return the list
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> List<T> sortedList(Y first, Y... others) {
    return createList(SortedArrayList::new, first, others);
  }

  /**
   * As list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param supplier the supplier
   * @param first    the first
   * @param others   the others
   * @return the list
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> List<T> createList(@NonNull Supplier<List<T>> supplier, Y first, Y... others) {
    if (first == null) {
      return Collections.emptyList();
    }
    if (others == null) {
      return Collections.singletonList(first);
    }
    return createList(supplier, Stream.concat(asStream(first), asStream(others)));
  }

  /**
   * As array list list.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param stream the stream
   * @return the list
   */
  static <T, Y extends T> List<T> asArrayList(Stream<Y> stream) {
    return createList(ArrayList::new, stream);
  }

  /**
   * As linked list list.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param stream the stream
   * @return the list
   */
  static <T, Y extends T> List<T> asLinkedList(Stream<Y> stream) {
    return createList(LinkedList::new, stream);
  }

  /**
   * As sorted list list.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param stream the stream
   * @return the list
   */
  static <T, Y extends T> List<T> asSortedList(Stream<Y> stream) {
    return createList(SortedArrayList::new, stream);
  }

  /**
   * As array list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterator the iterator
   * @return the list
   */
  static <T, Y extends T> List<T> asArrayList(Iterator<Y> iterator) {
    return createList(ArrayList::new, asStream(iterator));
  }

  /**
   * As linked list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterator the iterator
   * @return the list
   */
  static <T, Y extends T> List<T> asLinkedList(Iterator<Y> iterator) {
    return createList(LinkedList::new, asStream(iterator));
  }

  /**
   * As sorted list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterator the iterator
   * @return the list
   */
  static <T, Y extends T> List<T> asSortedList(Iterator<Y> iterator) {
    return createList(SortedArrayList::new, asStream(iterator));
  }

  /**
   * As array list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterable the iterable
   * @return the list
   */
  static <T, Y extends T> List<T> asArrayList(Iterable<Y> iterable) {
    return createList(ArrayList::new, asStream(iterable));
  }

  /**
   * As linked list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterable the iterable
   * @return the list
   */
  static <T, Y extends T> List<T> asLinkedList(Iterable<Y> iterable) {
    return createList(LinkedList::new, asStream(iterable));
  }

  /**
   * As sorted list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterable the iterable
   * @return the list
   */
  static <T, Y extends T> List<T> asSortedList(Iterable<Y> iterable) {
    return createList(SortedArrayList::new, asStream(iterable));
  }

  /**
   * Create list list.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param supplier the supplier
   * @param stream   the stream
   * @return the list
   */
  static <T, Y extends T> List<T> createList(@NonNull Supplier<List<T>> supplier, Stream<Y> stream) {
    if (stream == null) {
      return Collections.emptyList();
    }
    return stream.collect(Collectors.toCollection(supplier));
  }


  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> Set<T> set(Y... others) {
    return createSet(HashSet::new, others);
  }


  /**
   * As sorted set set.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param first  the first
   * @param others the others
   * @return the set
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> Set<T> treeSet(Y first, Y... others) {
    return createSet(TreeSet::new, first, others);
  }

  /**
   * As linked hash set set.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param first  the first
   * @param others the others
   * @return the set
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> Set<T> linkedHashSet(Y first, Y... others) {
    return createSet(LinkedHashSet::new, first, others);
  }

  /**
   * As set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param supplier the supplier
   * @param first    the first
   * @param others   the others
   * @return the set
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> Set<T> createSet(@NonNull Supplier<Set<T>> supplier, Y first, Y... others) {
    if (first == null) {
      return Collections.emptySet();
    }
    if (others == null) {
      return Collections.singleton(first);
    }
    return createSet(supplier, Stream.concat(asStream(first), asStream(others)));
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  static <T, Y extends T> Set<T> createSet(@NonNull Supplier<Set<T>> supplier, Y... others) {
    if (others == null) {
      return Collections.emptySet();
    }
    return createSet(supplier, Stream.of(others));
  }

  /**
   * As set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterator the iterator
   * @return the set
   */
  static <T, Y extends T> Set<T> asSet(Iterator<Y> iterator) {
    return createSet(HashSet::new, asStream(iterator));
  }

  /**
   * As tree set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterator the iterator
   * @return the set
   */
  static <T, Y extends T> Set<T> asTreeSet(Iterator<Y> iterator) {
    return createSet(TreeSet::new, asStream(iterator));
  }

  /**
   * As linked hash set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterator the iterator
   * @return the set
   */
  static <T, Y extends T> Set<T> asLinkedHashSet(Iterator<Y> iterator) {
    return createSet(LinkedHashSet::new, asStream(iterator));
  }

  /**
   * As set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterable the iterable
   * @return the set
   */
  static <T, Y extends T> Set<T> asSet(Iterable<Y> iterable) {
    return createSet(HashSet::new, asStream(iterable));
  }

  /**
   * As tree set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterable the iterable
   * @return the set
   */
  static <T, Y extends T> Set<T> asTreeSet(Iterable<Y> iterable) {
    return createSet(TreeSet::new, asStream(iterable));
  }

  /**
   * As linked hash set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param iterable the iterable
   * @return the set
   */
  static <T, Y extends T> Set<T> asLinkedHashSet(Iterable<Y> iterable) {
    return createSet(LinkedHashSet::new, asStream(iterable));
  }

  /**
   * As set set.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param stream the stream
   * @return the set
   */
  static <T, Y extends T> Set<T> asSet(Stream<Y> stream) {
    return createSet(HashSet::new, stream);
  }

  /**
   * As tree set set.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param stream the stream
   * @return the set
   */
  static <T, Y extends T> Set<T> asTreeSet(Stream<Y> stream) {
    return createSet(TreeSet::new, stream);
  }

  /**
   * As linked hash set set.
   *
   * @param <T>    the type parameter
   * @param <Y>    the type parameter
   * @param stream the stream
   * @return the set
   */
  static <T, Y extends T> Set<T> asLinkedHashSet(Stream<Y> stream) {
    return createSet(LinkedHashSet::new, stream);
  }

  /**
   * Create set set.
   *
   * @param <T>      the type parameter
   * @param <Y>      the type parameter
   * @param supplier the supplier
   * @param stream   the stream
   * @return the set
   */
  static <T, Y extends T> Set<T> createSet(@NonNull Supplier<Set<T>> supplier, Stream<Y> stream) {
    if (stream == null) {
      return Collections.emptySet();
    }
    return stream.collect(Collectors.toCollection(supplier));
  }

}//END OF CollectionHelpers
