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

import static fr.norad.jaxrs.doc.parser.SecuringSomething.TheScopes.SCOPE_A;
import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;
import fr.norad.jaxrs.doc.api.domain.OperationDefinition;

public class OperationOauth2ParserTest {
    private OperationOauth2Parser operationParser = new OperationOauth2Parser();

    @Test
    public void should_not_fill_secured_for_not_annotated() throws Exception {
        class Test {
            public void getString() {
            }
        }
        OperationDefinition operation = new OperationDefinition();
        operationParser.parse(null, operation, Test.class.getMethod("getString"));

        assertThat(operation.getSecuredInfo()).isNull();
    }

    @Test
    public void should_fill_secured_for_annotated() throws Exception {
        class Test {
            @SecuringSomething(SCOPE_A)
            public void getString() {
            }
        }
        OperationDefinition operation = new OperationDefinition();
        operationParser.parse(null, operation, Test.class.getMethod("getString"));

        assertThat(operation.getSecuredInfo()).isNotNull();
        assertThat(operation.getSecuredInfo().getScopes()).containsOnly(SCOPE_A);
    }

}
