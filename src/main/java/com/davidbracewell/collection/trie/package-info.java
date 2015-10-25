/*
 *  * Take from Apache commons
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
/**
 * This package contains implementations of the
 * {@link com.davidbracewell.collection.trie.Trie} interface.
 * <p>
 * The implementations are in the form of direct implementations and decorators.
 * A decorator wraps another implementation of the interface to add some
 * specific additional functionality.
 * <p>
 * The following implementations are provided in the package:
 * <ul>
 *   <li>PatriciaTrie - an implementation of a PATRICIA trie
 * </ul>
 * <p>
 * The following decorators are provided:
 * <ul>
 *   <li>Unmodifiable - ensures the collection cannot be altered
 * </ul>
 *
 * @version $Id: package-info.java 1493523 2013-06-16 15:56:35Z tn $
 */
package com.davidbracewell.collection.trie;