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

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DocTemplate {

    public static final DocTemplate GITHUB = new DocTemplate("/template/github", "github-template.html") //
            .file("github-template.html") //
            .file("960.css") //
            .file("documentation.css") //
            .file("documentation.js") //
            .file("feed-icon-28x28.png") //
            .file("github-template.html") //
            .file("jquery.js") //
            .file("logo_developer.png") //
            .file("pygments.css") //
            .file("reset.css") //
            .file("status-icon-good.png") //
            .file("uv_active4d.css") //

            .file("expand-arrows.png") //
            .file("octicons-regular-webfont.svg") //
            .file("octicons-regular-webfont.woff") //

            .file("active-arrow.png") //
            .file("background-v2.png") //
            .file("background-white.png") //
            .file("crud-sprite.png") //
            .file("dropdown_sprites.jpg") //
            .file("nav-rule.png") //
            .file("qmark.png") //

    ;

    private String resourcePath;
    private String homePageName;
    private List<String> files = new ArrayList<>();
    private String fileResourcePath;

    public DocTemplate(String fileResourcePath, String homePageName) {
        this.fileResourcePath = fileResourcePath;
        this.homePageName = homePageName;
    }

    public DocTemplate file(String file) {
        files.add(file);
        return this;
    }

}
