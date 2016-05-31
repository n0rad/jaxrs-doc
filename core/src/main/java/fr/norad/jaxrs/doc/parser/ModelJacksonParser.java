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

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import fr.norad.jaxrs.doc.PropertyAccessor;
import fr.norad.jaxrs.doc.api.domain.LocalizationDefinition;
import fr.norad.jaxrs.doc.api.domain.ModelDefinition;
import fr.norad.jaxrs.doc.parserapi.ModelParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@JsonIgnoreType
public class ModelJacksonParser implements ModelParser {

    private final Logger log = Logger.getLogger(ModelJacksonParser.class.getName());

    private FakeSerializer fakeSerializer = new FakeSerializer();

    @Override
    public void parse(Map<Locale, LocalizationDefinition> localeDefinitions, ModelDefinition model, Class<?> modelClass) {

    }

    @Override
    public List<PropertyAccessor> findProperties(Class<?> modelClass) {
        List<PropertyAccessor> properties = new ArrayList<>();

        BasicBeanDescription beanDesc = fakeSerializer.getDescription(modelClass);
        List<BeanPropertyDefinition> findProperties = beanDesc.findProperties();
        for (BeanPropertyDefinition beanPropertyDefinition : findProperties) {
            if (modelClass.isEnum() && "declaringClass".equals(beanPropertyDefinition.getName())) {
                continue;
            }
            AnnotatedMethod getterJackson = beanPropertyDefinition.getGetter();
            AnnotatedMethod setterJackson = beanPropertyDefinition.getSetter();
            AnnotatedField fieldJackson = null;
            try {
                fieldJackson = beanPropertyDefinition.getField();
            } catch (Exception e) {
                log.warning("Name conflict on fields in bean : " + beanPropertyDefinition + " during doc generation"
                        + e.getMessage());
            }

            Method getter = getterJackson == null ? null : getterJackson.getAnnotated();
            Method setter = setterJackson == null ? null : setterJackson.getAnnotated();
            Field field = fieldJackson == null ? null : fieldJackson.getAnnotated();
            if (getter == null && setter == null && field == null) {
                log.warning("Cannot find valid property element for : " + beanPropertyDefinition + " on " +
                        modelClass);
                continue;
            }

            PropertyAccessor property = new PropertyAccessor();
            property.setField(field);
            property.setGetter(getter);
            property.setSetter(setter);
            property.setName(beanPropertyDefinition.getName());

            properties.add(property);
        }
        return properties;
    }

    ////////////////////////////////////////////////////////////

    class FakeSerializer extends BeanSerializerFactory {

        protected FakeSerializer() {
            super(null);
        }

        public BasicBeanDescription getDescription(Class<?> model) {
            if (Map.class.isAssignableFrom(model)) {
                throw new IllegalArgumentException("Can not construct SimpleType for a Map (class: "
                        + model.getName() + ")");
            }
            if (Collection.class.isAssignableFrom(model)) {
                throw new IllegalArgumentException("Can not construct SimpleType for a Collection (class: "
                        + model.getName() + ")");
            }
            if (model.isArray()) {
                throw new IllegalArgumentException("Can not construct SimpleType for an array (class: "
                        + model.getName() + ")");
            }

            JavaType origType = SimpleType.construct(model);
            BasicClassIntrospector basicClassIntrospector = new BasicClassIntrospector();
            AnnotationIntrospectorPair annotationIntrospector = new AnnotationIntrospectorPair(
                    new JacksonAnnotationIntrospector(),
                    new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));

            BaseSettings baseSettings = new BaseSettings(basicClassIntrospector, annotationIntrospector,
                    VisibilityChecker.Std.defaultInstance(), null, TypeFactory.defaultInstance(), null, null, null,
                    null, null, null);
            SerializationConfig config = new SerializationConfig(baseSettings, new StdSubtypeResolver(), null, null);
            BeanDescription beanDesc = config.introspect(origType);
            return (BasicBeanDescription) beanDesc;
        }
    }

    @Override
    public boolean isModelToIgnore(Class<?> modelClass) {
        return false;
    }
}
