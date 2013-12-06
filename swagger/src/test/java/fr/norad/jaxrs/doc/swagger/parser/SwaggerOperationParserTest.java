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
import com.wordnik.swagger.annotations.ApiError;
import com.wordnik.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.ApiOperation;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;

@SuppressWarnings("unused")
public class SwaggerOperationParserTest {

    SwaggerOperationParser parser = new SwaggerOperationParser();
    ApiDefinition api = new ApiDefinition();
    OperationDefinition operation = new OperationDefinition();

    @Test
    public void should_process_empty_api_operation() throws Exception {
        class Test {
            @ApiOperation("genre")
            public void method() {
            }
        }

        parser.parse(api, operation, Test.class.getDeclaredMethod("method"));

        assertThat(operation.getDescription()).isEqualTo("genre");
        assertThat(operation.getHttpMethod()).isNull();
        assertThat(operation.getResponseAsList()).isNull();
        assertThat((Object) operation.getResponseClass()).isNull();
    }

    @Test
    public void should_process_api_operation() throws Exception {
        class Test {
            @ApiOperation(value = "description", httpMethod = "GET", multiValueResponse = true,
                    responseClass = "java.lang.String")
            public void method() {
            }
        }

        parser.parse(api, operation, Test.class.getDeclaredMethod("method"));

        assertThat(operation.getDescription()).isEqualTo("description");
        assertThat(operation.getHttpMethod()).isEqualTo("GET");
        assertThat(operation.getResponseAsList()).isTrue();
        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
    }

    @Test
    public void should_process_api_errors() throws Exception {
        class Test {
            @ApiErrors(@ApiError(code = 404, reason = "not found"))
            public void method() {
            }
        }

        parser.parse(api, operation, Test.class.getDeclaredMethod("method"));

        assertThat(operation.getErrors()).hasSize(1);
//        assertThat(operation.getErrors().get(0).getHttpCode()).isEqualTo(404);
        assertThat(operation.getErrors().get(0).getReason()).isEqualTo("not found");
    }

    @Test
    public void should_process_api_error() throws Exception {
        class Test {
            @ApiError(code = 404, reason = "not found")
            public void method() {
            }
        }

        parser.parse(api, operation, Test.class.getDeclaredMethod("method"));

        assertThat(operation.getErrors()).hasSize(1);
//        assertThat(operation.getErrors().get(0).getHttpCode()).isEqualTo(404);
        assertThat(operation.getErrors().get(0).getReason()).isEqualTo("not found");
    }
}
