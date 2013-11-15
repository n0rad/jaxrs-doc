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
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import org.junit.Test;
import fr.norad.jaxrs.doc.domain.ApiDefinition;
import fr.norad.jaxrs.doc.domain.OperationDefinition;

public class OperationJavaParserTest {

    private ApiDefinition api = new ApiDefinition();
    private OperationDefinition operation = new OperationDefinition();

    @Test
    public void should_fill_method_name() throws Exception {
        class Test {
            public void call() {

            }
        }
        new OperationJavaParser().parse(api, operation, Test.class.getMethod("call"));

        assertThat(operation.getMethodName()).isEqualTo("call");
    }

    @Test
    public void should_fill_source_class() throws Exception {
        class Test {
            public void call() {

            }
        }
        api.setApiClass(Test.class);
        new OperationJavaParser().parse(api, operation, Test.class.getMethod("call"));

        assertThat((Object) operation.getOperationClass()).isEqualTo(Test.class);
    }

    @Test
    public void should_fill_deprecated() throws Exception {
        class Test {
            @Deprecated
            public void call() {

            }
        }
        new OperationJavaParser().parse(api, operation, Test.class.getMethod("call"));

        assertThat(operation.getDeprecated()).isTrue();
    }

    @Test
    public void should_support_array_return_type() throws Exception {
        class Test {
            @GET
            public String[] getSomething() {
                return null;
            }
        }

        new OperationJavaParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat(operation.getResponseAsList()).isTrue();
    }

    @Test
    public void should_support_list_return_type() throws Exception {
        class Test {
            @GET
            public List<String> getSomething() {
                return null;
            }
        }

        new OperationJavaParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat(operation.getResponseAsList()).isTrue();
    }

    @Test
    public void should_support_map_return_type() throws Exception {
        class Test {
            @GET
            public Map<Integer, String> getSomething() {
                return null;
            }
        }

        new OperationJavaParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat((Object) operation.getResponseMapKeyClass()).isEqualTo(Integer.class);
    }

    @Test
    public void should_support_standard_return_type() throws Exception {
        class Test {
            @GET
            public String getSomething() {
                return null;
            }
        }

        new OperationJavaParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat((Object) operation.getResponseClass()).isEqualTo(String.class);
        assertThat(operation.getResponseAsList()).isNull();
    }

    @Test
    public void should_support_exception() throws Exception {
        class Test {
            @GET
            public String getSomething() throws IllegalArgumentException, ParseException {
                return null;
            }
        }

        new OperationJavaParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getErrors()).hasSize(2);
        assertThat((Object) operation.getErrors().get(0).getErrorClass()).isSameAs(IllegalArgumentException.class);
        assertThat((Object) operation.getErrors().get(1).getErrorClass()).isSameAs(ParseException.class);
    }

    @Test
    public void not_fill_void_response() throws Exception {
        class Test {
            @GET
            public void getSomething() {
            }
        }

        new OperationJavaParser().parse(api, operation, Test.class.getMethod("getSomething"));

        assertThat(operation.getResponseClass()).isNull();
    }
}
