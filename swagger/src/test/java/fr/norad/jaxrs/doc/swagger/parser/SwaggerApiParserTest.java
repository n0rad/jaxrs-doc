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
package fr.norad.jaxrs.doc.swagger.parser;

import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiClass;
import fr.norad.jaxrs.doc.domain.ApiDefinition;

public class SwaggerApiParserTest {
    ApiDefinition api = new ApiDefinition();
    SwaggerApiParser parser = new SwaggerApiParser();

    @Test
    public void should_parse_api() throws Exception {
        @Api(value = "path", description = "description")
        class Test {
        }
        parser.parse(api, Test.class);

        assertThat(api.getDescription()).isEqualTo("description");
        assertThat(api.getPath()).isEqualTo("path");
    }

    @Test
    public void should_parse_api_class() throws Exception {
        @ApiClass(value = "summary", description = "description")
        class Test {
        }
        parser.parse(api, Test.class);

        assertThat(api.getDescription()).isEqualTo("description");
        assertThat(api.getSummary()).isEqualTo("summary");
    }
}
