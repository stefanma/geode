/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.geode.management;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.test.dunit.rules.MBeanServerConnectionRule;
import org.apache.geode.test.dunit.rules.ServerStarterRule;
import org.apache.geode.test.junit.categories.IntegrationTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTest.class)
public class DataCommandMBeanTest {
  private MemberMXBean bean;

  @ClassRule
  public static ServerStarterRule server =
      new ServerStarterRule().withJMXManager().withRegion(RegionShortcut.REPLICATE, "testRegion");

  @Rule
  public MBeanServerConnectionRule mBeanConnector = new MBeanServerConnectionRule();

  @Before
  public void before() throws Exception {
    mBeanConnector.connect(server.getJmxPort());
    bean = mBeanConnector.getProxyMBean(MemberMXBean.class);
  }

  @Test
  public void testQueryWithStepAll() throws Exception {
    String result = bean.processCommand("query --query='SELECT * FROM /testRegion'");
    assertThat(result).contains("Only Remote command can be executed");
  }

  @Test
  public void testQueryWithStepExec() throws Exception {
    String result =
        bean.processCommand("query --query='SELECT * FROM /testRegion' --step-name=SELECT_EXEC");
    assertThat(result).contains("Result     : true");
  }
}
