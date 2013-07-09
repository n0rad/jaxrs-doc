package net.awired.jaxrs.doc.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.awired.jaxrs.doc.DocConfig;
import net.awired.jaxrs.doc.annotations.Description;
import net.awired.jaxrs.doc.domain.ProjectDefinition;
import net.awired.jaxrs.doc.domain.PropertyDefinition;
import net.awired.jaxrs.doc.utils.AnnotationUtil;

public class PropertyParser {

    private final DocConfig config;

    public PropertyParser(DocConfig config) {
        this.config = config;
    }

    public PropertyDefinition parse(ProjectDefinition project, Field field, Method getter, Method setter) {
        PropertyDefinition property = new PropertyDefinition();

        if (field != null) {
            property.setPropertyClass(field.getType());
        }
        if (getter != null) {
            property.setPropertyClass(getter.getReturnType());
        }

        property.setAsList(property.getPropertyClass().isArray() ? true : null);

        processGetterSetter(property, getter);
        processGetterSetter(property, setter);

        if (field != null) {
            Description desc = field.getAnnotation(Description.class);
            if (property.getDescription() == null) {
                property.setDescription(desc == null ? null : desc.value());
            }
        }

        //        property.setAsList(asList)

        //        if (model.getProperties() == null) {
        //            model.setProperties(new HashMap<String, ModelDefinition>());
        //        }
        //        Class<?> propertyType = property.getPropertyType();
        //        if (!model.getProperties().containsKey(propertyType)) {
        //            parse(project, propertyType);
        //        }
        //        model.getProperties().put(property.getName(), null);

        return property;
    }

    private void processGetterSetter(PropertyDefinition property, Method method) {
        if (method == null) {
            return;
        }
        Description desc = AnnotationUtil.findAnnotation(method, Description.class);
        if (property.getDescription() == null) {
            property.setDescription(desc == null ? null : desc.value());
        }

    }

}
