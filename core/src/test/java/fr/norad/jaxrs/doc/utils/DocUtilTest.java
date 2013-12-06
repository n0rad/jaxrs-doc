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
import org.junit.Test;
import fr.norad.jaxrs.doc.api.Description;

public class DocUtilTest {
    @Test
    public void should_read_description_empty() throws Exception {
        @Description("")
        class TestClass {
        }
        Description annotation = TestClass.class.getAnnotation(Description.class);

        assertThat(DocUtils.getDescription(annotation)).isEqualTo(null);
    }

    @Test
    public void should_read_description_single() throws Exception {
        @Description("desc")
        class TestClass {
        }
        Description annotation = TestClass.class.getAnnotation(Description.class);

        assertThat(DocUtils.getDescription(annotation)).isEqualTo("desc");
    }

    @Test
    public void should_read_description_multi() throws Exception {
        @Description({"a", "b"})
        class TestClass {
        }
        Description annotation = TestClass.class.getAnnotation(Description.class);

        assertThat(DocUtils.getDescription(annotation)).isEqualTo("a b");
    }

    @Test
    public void should_read_description_multi_trim() throws Exception {
        @Description({" a ", " b "})
        class TestClass {
        }
        Description annotation = TestClass.class.getAnnotation(Description.class);

        assertThat(DocUtils.getDescription(annotation)).isEqualTo("a b");
    }

}
