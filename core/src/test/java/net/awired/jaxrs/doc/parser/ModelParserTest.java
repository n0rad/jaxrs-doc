/**
 *
 *     Copyright (C) Awired.net
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
package net.awired.jaxrs.doc.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;
import net.awired.jaxrs.doc.DocConfig;
import org.junit.Test;

public class ModelParserTest {

    private DocConfig config = new DocConfig(Arrays.asList("net.awired"));

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
