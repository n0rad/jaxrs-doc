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
import javax.ws.rs.GET;
import org.junit.Test;
import fr.norad.jaxrs.doc.annotations.Description;
import fr.norad.jaxrs.doc.annotations.Outdated;
import fr.norad.jaxrs.doc.annotations.Summary;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;

public class OperationJaxrsDocParserTest {

    private ApiDefinition api = new ApiDefinition();
    private OperationDefinition operation = new OperationDefinition();

    @Test
    public void should_find_deprecated_with_outdated() throws Exception {
        class Test {
            @GET
            @Outdated(since = "since", cause = "cause", willBeRemovedOn = "2.2")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getDeprecated()).isTrue();
        assertThat(operation.getDeprecatedCause()).isEqualTo("cause");
        assertThat(operation.getDeprecatedSince()).isEqualTo("since");
        assertThat(operation.getDeprecatedWillBeRemovedOn()).isEqualTo("2.2");
    }

    @Test
    public void should_find_deprecated_with_outdated2() throws Exception {
        class Test {
            @GET
            @Outdated(cause = "cause")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getDeprecated()).isTrue();
        assertThat(operation.getDeprecatedCause()).isEqualTo("cause");
        assertThat(operation.getDeprecatedSince()).isNull();
    }

    @Test
    public void should_find_summary() throws Exception {
        class Test {
            @Summary("yop")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getSummary()).isEqualTo("yop");
    }

    @Test
    public void should_find_description() throws Exception {
        class Test {
            @Description("yop")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getDescription()).isEqualTo("yop");
    }
}
