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
package fr.norad.jaxrs.doc.utils;

import fr.norad.jaxrs.doc.annotations.Description;

public class DocUtils {

    public static String getDescription(Description description) {
        if (description == null || description.value() == null || description.value().length == 0) {
            return null;
        }
        String result = "";
        int i = 0;
        for (String line : description.value()) {
            if (i++ != 0) {
                result += ' ';
            }
            result += line.trim();
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }
}
