/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.doc.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import fr.norad.jaxrs.doc.utils.ReflectionUtil;

public class ReflectionUtilTest {

    @Test
    public void should_find_generic_return_type() throws Exception {
        class Test {
            public List<UUID> getSomething() {
                return null;
            }
        }
        Class<?> type = ReflectionUtil.getSingleGenericReturnType(Test.class.getMethod("getSomething"));

        assertThat((Object) type).isEqualTo(UUID.class);
    }

    @Test
    public void should_not_find_generic_return_type() throws Exception {
        class Test {
            public String getSomething() {
                return null;
            }
        }
        Class<?> type = ReflectionUtil.getSingleGenericReturnType(Test.class.getMethod("getSomething"));

        assertThat((Object) type).isNull();
    }
}