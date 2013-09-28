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
package fr.norad.jaxrs.doc.parser.jaxrs;

import static org.fest.assertions.api.Assertions.assertThat;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;
import org.junit.Test;
import fr.norad.jaxrs.doc.DocConfig;
import fr.norad.jaxrs.doc.parser.jaxrs.ModelParser;

public class ModelParserTest {

    private DocConfig config = new DocConfig(Arrays.asList("fr.norad"));

    @Test
    public void should_ignore_basic_class() throws Exception {
        ModelParser modelParser = new ModelParser(config);

        assertThat(modelParser.isIgnoreModel(String.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(int.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(void.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(UUID.class)).isTrue();
        assertThat(modelParser.isIgnoreModel(InputStream.class)).isTrue();
    }

}
