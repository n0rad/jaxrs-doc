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
package fr.norad.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.ModelDefinition;

public class ModelJavaParserTest {

    private ModelDefinition model = new ModelDefinition(String.class);
    private ModelJavaParser parser = new ModelJavaParser();

    @Test
    public void should_ignore_basic_class() throws Exception {
        assertThat(parser.isModelToIgnore(String.class)).isTrue();
        assertThat(parser.isModelToIgnore(int.class)).isTrue();
        assertThat(parser.isModelToIgnore(void.class)).isTrue();
        assertThat(parser.isModelToIgnore(UUID.class)).isTrue();
        assertThat(parser.isModelToIgnore(InputStream.class)).isTrue();
        assertThat(parser.isModelToIgnore(URL.class)).isTrue();
        assertThat(parser.isModelToIgnore(URI.class)).isTrue();
    }

    @Test
    public void should_fill_deprecated() throws Exception {
        @Deprecated
        class Test {

        }
        parser.parse(model, Test.class);

        assertThat(model.getDeprecated()).isTrue();
    }
}
