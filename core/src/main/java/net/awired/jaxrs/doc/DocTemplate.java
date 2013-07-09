package net.awired.jaxrs.doc;

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
