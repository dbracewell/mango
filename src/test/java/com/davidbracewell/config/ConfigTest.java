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

package com.davidbracewell.config;

import com.davidbracewell.Language;
import com.davidbracewell.io.Resources;
import com.davidbracewell.scripting.ScriptEnvironmentManager;
import com.davidbracewell.string.StringPredicates;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author David B. Bracewell
 */
public class ConfigTest {

  @Before
  public void setUp() throws Exception {
    Config.initializeTest();
    Config.loadConfig(Resources.fromClasspath("com/davidbracewell/testing.conf"));
    Config.setProperty("name", "David");
    Config.setProperty("age", "script[js]: 34+1;");
    Config.setProperty("named", "bean");
    Config.setProperty("object", "script[js]: 'Hello';");
    ScriptEnvironmentManager.getInstance().getEnvironment("js").eval("object = 'Hola';");
    Config.setProperty("objectAlias", "script[js,object]");
  }

  @Test
  public void testGet() throws Exception {
    assertEquals("David", Config.get("name").asString());
    assertEquals((Integer) 35, Config.get("age").asInteger());
    assertEquals("Hello", Config.get("object").asString());
    assertEquals("Hola", Config.get("objectAlias").asString());

    assertEquals(Config.get("org.alpha.omega").asInteger().intValue(), 120);
    assertEquals(Config.get("mys").asDouble(), (Double) 57.11);
    assertEquals(Config.get("welcomeText").asString(), "Hello there my name is david and I am 57.11 years olds!");
    assertEquals(Config.get("longComment").asString(), "This is really long \ndo you like it?");

    Config.setProperty("util", "default");
    Config.setProperty("util.en", "english");
    assertEquals("default", Config.get("util").asString());
    assertEquals("english", Config.get("util", Language.ENGLISH).asString());

    assertEquals((Integer) 134, Config.get("sectionName.testing.arg").asInteger());
    assertEquals(true, Config.get("the.other.property").asBoolean());

    assertEquals("espresso", ScriptEnvironmentManager.getInstance().getEnvironmentForExtension("js").getObject("libraryName"));


    assertEquals("HELLO, World", Config.get("keyA").asString());
  }

  @Test
  public void testGetPropertiesMatching() throws Exception {
    List<String> names = Config.getPropertiesMatching(StringPredicates.STARTS_WITH("name", true));
    assertTrue(names.contains("name"));
    assertTrue(names.contains("named"));
    assertEquals(names.size(), 2);
  }

  @Test
  public void testHasProperty() throws Exception {
    assertTrue(Config.hasProperty("name"));
    assertTrue(Config.hasProperty("age"));
  }

  @Test
  public void testValueIsScript() throws Exception {
    assertTrue(Config.valueIsScript("age"));
    assertFalse(Config.valueIsScript("name"));
  }

}
