/// Copyright JProggy
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.

package org.jproggy.snippetory.groovy

import org.jproggy.snippetory.Syntaxes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateDynamizerTest {

  @BeforeEach
  void setUp() throws Exception {
    TemplateDynamizer.init();
  }

  @Test
  void test() {
    def tpl = Syntaxes.FLUYT.parse('$x{test}$')
    def y = tpl.x
    assert y.toString() == "test"
    tpl.x = "blah";
    assert tpl.toString() == "blah"
    tpl.x.render()
    assert tpl.toString() == "blahtest"
    assertEquals("test", tpl["x"].toString())
  }
}
