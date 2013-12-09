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

import static fr.norad.jaxrs.doc.api.HttpStatus.FORBIDDEN_403;
import static org.fest.assertions.api.Assertions.assertThat;
import javax.ws.rs.GET;
import org.junit.Test;
import fr.norad.jaxrs.doc.api.*;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.ErrorOperationDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;

public class OperationJaxrsDocParserTest {


    private ApiDefinition api = new ApiDefinition();
    private OperationDefinition operation = new OperationDefinition();

    @Test
    public void should_override_from_class() throws Exception {
        @HttpStatus(FORBIDDEN_403)
        class NotFoundException extends Exception {
        }

        @OperationError(errorClass = NotFoundException.class, reason = "generic")
        class EventResource {
            @OperationError(errorClass = NotFoundException.class, reason = "Event not found")
            public void getEvent() throws NotFoundException {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, EventResource.class.getMethod("getEvent"));

        assertThat(operation.getErrors()).hasSize(1);
        ErrorOperationDefinition errorDefinition = operation.getErrors().get(0);
        assertThat((Object) errorDefinition.getErrorClass()).isEqualTo(NotFoundException.class);
        assertThat(errorDefinition.getReason()).isEqualTo("Event not found");
    }

    @Test
    public void should_find_2_error_annotation_on_method_and_class() throws Exception {
        @OperationError(errorClass = RuntimeException.class, reason = "bou")
        class Test {
            @OperationError(errorClass = Exception.class, reason = "bou")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getErrors()).hasSize(2);
    }

    @Test
    public void should_override_exception_on_method() throws Exception {
        class Test {
            @OperationErrors({
                    @OperationError(errorClass = Exception.class, reason = "boubou"),
            })
            @OperationError(errorClass = Exception.class, reason = "bou")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getErrors()).hasSize(1);
        ErrorOperationDefinition errorDefinition = operation.getErrors().get(0);
        assertThat((Object) errorDefinition.getErrorClass()).isEqualTo(Exception.class);
        assertThat(errorDefinition.getReason()).isEqualTo("boubou");
    }

    @Test
    public void should_find_2_error_annotation_on_method_for_list_and_single() throws Exception {
        class Test {
            @OperationErrors({
                    @OperationError(errorClass = RuntimeException.class, reason = "bou"),
            })
            @OperationError(errorClass = Exception.class, reason = "bou")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getErrors()).hasSize(2);
    }

    @Test
    public void should_find_2_error_annotation_on_method_for_list() throws Exception {
        class Test {
            @OperationErrors({
                    @OperationError(errorClass = RuntimeException.class, reason = "bou"),
                    @OperationError(errorClass = Exception.class, reason = "bou"),
            })
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getErrors()).hasSize(2);
    }

    @Test
    public void should_find_error_annotation_on_method() throws Exception {
        class Test {
            @OperationError(errorClass = Exception.class, reason = "bou")
            public void getSomething() {
            }
        }

        new OperationJaxrsDocParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getErrors()).hasSize(1);
        ErrorOperationDefinition errorDefinition = operation.getErrors().get(0);
        assertThat((Object) errorDefinition.getErrorClass()).isEqualTo(Exception.class);
        assertThat(errorDefinition.getReason()).isEqualTo("bou");
    }

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
