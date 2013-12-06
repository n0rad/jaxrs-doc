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
import org.junit.Test;
import fr.norad.jaxrs.doc.api.Description;
import fr.norad.jaxrs.doc.api.Outdated;
import fr.norad.jaxrs.doc.domain.ModelDefinition;

public class ModelJaxrsDocParserTest {

    private ModelDefinition model = new ModelDefinition(String.class);
    private ModelJaxrsDocParser parser = new ModelJaxrsDocParser();

    @Test
    public void should_fill_oudated_part() throws Exception {
        @Outdated(since = "since", cause = "cause", willBeRemovedOn = "3.3")
        class Test {

        }
        parser.parse(null, model, Test.class);

        assertThat(model.getDeprecated()).isTrue();
        assertThat(model.getDeprecatedCause()).isEqualTo("cause");
        assertThat(model.getDeprecatedSince()).isEqualTo("since");
        assertThat(model.getDeprecatedWillBeRemovedOn()).isEqualTo("3.3");
    }

    @Test
    public void should_fill_description_part() throws Exception {
        @Description("yopla")
        class Test {

        }
        parser.parse(null, model, Test.class);

        assertThat(model.getDescription()).isEqualTo("yopla");
    }

}
