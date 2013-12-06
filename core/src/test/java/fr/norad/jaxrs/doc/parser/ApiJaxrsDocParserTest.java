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
import javax.ws.rs.Path;
import org.junit.Test;
import fr.norad.jaxrs.doc.api.Description;
import fr.norad.jaxrs.doc.api.Outdated;
import fr.norad.jaxrs.doc.api.Summary;
import fr.norad.jaxrs.doc.domain.ApiDefinition;

public class ApiJaxrsDocParserTest {
    @Test
    public void should_support_outdated() throws Exception {
        @Path("/")
        @Outdated(cause = "sux", since = "since")
        class Test {
        }

        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsDocParser().parse(api, Test.class);

        assertThat(api.getDeprecated()).isTrue();
        assertThat(api.getDeprecatedCause()).isEqualTo("sux");
        assertThat(api.getDeprecatedSince()).isEqualTo("since");
    }

    @Test
    public void should_fill_summary() throws Exception {
        @Summary("summary")
        class Test {
        }

        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsDocParser().parse(api, Test.class);

        assertThat(api.getSummary()).isEqualTo("summary");
    }

    @Test
    public void should_fill_description() throws Exception {
        @Description("description")
        class Test {
        }

        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsDocParser().parse(api, Test.class);

        assertThat(api.getDescription()).isEqualTo("description");
    }

    @Test
    public void should_support_description_with_multiline() throws Exception {
        @Description({"very long description that needs to use 2 lines to be able to say everything we want to",
                      "say and keep the formater able to do what we want to do"})
        class Test {
        }

        ApiDefinition api = new ApiDefinition();
        new ApiJaxrsDocParser().parse(api, Test.class);

        assertThat(api.getDescription()).startsWith("very").endsWith("do").contains("to say and keep");
    }
}
