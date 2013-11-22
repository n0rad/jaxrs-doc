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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserHolder<T> {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Set<T> holder = new HashSet<>();

    public ParserHolder() {
    }

    public ParserHolder(String[] parsers) {
        addAll(parsers);
    }

    public boolean addAll(String[] classAsString) {
        for (String clazz : classAsString) {
            try {
                addSimpleInstantiation((Class<? extends T>) getClass().getClassLoader().loadClass(clazz));
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Cannot found parser class : " + clazz);
            }
        }
        return true;
    }

    public boolean add(T parser) {
        return holder.add(parser);
    }

    public boolean addSimpleInstantiation(Class<? extends T> clazz) {
        try {
            T parser = clazz.newInstance();
            return holder.add(parser);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot build parser instance for class" + clazz);
        }
    }

    public boolean addOnlyIfLoadable(T clazz, String... classesToFindToBeLoadable) {
        for (String classToFind : classesToFindToBeLoadable) {
            try {
                getClass().getClassLoader().loadClass(classToFind);
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        return add(clazz);
    }

    public Set<T> get() {
        return Collections.unmodifiableSet(holder);
    }


}
