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
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

public class ReflectionUtilsTest {

    interface ITest {
        public void getSomething() throws ParseException;

    }

    @Test
    public void should_find_exception() throws Exception {
        class Test {
            public void getSomething() throws IllegalArgumentException {
            }
        }
        List<Class<?>> type = ReflectionUtils.getExceptions(Test.class.getMethod("getSomething"));

        assertThat(type).hasSize(1);
        assertThat(type.toArray()[0]).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    public void should_find_exception_from_interface() throws Exception {
        class Test implements ITest {
            @Override
            public void getSomething() throws IllegalArgumentException {
            }
        }
        List<Class<?>> type = ReflectionUtils.getExceptions(Test.class.getMethod("getSomething"));

        assertThat(type).hasSize(2);
        assertThat(type).containsExactly(ParseException.class, IllegalArgumentException.class);
    }

    @Test
    public void should_find_exception_from_abstract() throws Exception {
        class TestAbstract implements ITest {
            @Override
            public void getSomething() throws IllegalStateException {
            }

        }
        class Test extends TestAbstract {
            @Override
            public void getSomething() throws IllegalArgumentException {
            }
        }
        List<Class<?>> type = ReflectionUtils.getExceptions(Test.class.getMethod("getSomething"));

        assertThat(type).hasSize(3);
        assertThat(type).containsExactly(IllegalArgumentException.class, ParseException.class,
                IllegalStateException.class);
    }

    @Test
    public void should_find_generic_return_type() throws Exception {
        class Test {
            public List<UUID> getSomething() {
                return null;
            }
        }
        Class<?> type = ReflectionUtils.getSingleGenericReturnType(Test.class.getMethod("getSomething"));

        assertThat((Object) type).isEqualTo(UUID.class);
    }

    @Test
    public void should_not_find_generic_return_type() throws Exception {
        class Test {
            public String getSomething() {
                return null;
            }
        }
        Class<?> type = ReflectionUtils.getSingleGenericReturnType(Test.class.getMethod("getSomething"));

        assertThat((Object) type).isNull();
    }
}
