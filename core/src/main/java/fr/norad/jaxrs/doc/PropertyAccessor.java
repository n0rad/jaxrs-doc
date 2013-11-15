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
package fr.norad.jaxrs.doc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.Data;

@Data
public class PropertyAccessor {
    private String name;
    private Field field;
    private Method getter;
    private Method setter;

    public PropertyAccessor() {
    }

    public PropertyAccessor(String name, Field field, Method getter, Method setter) {
        this.name = name;
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

}
