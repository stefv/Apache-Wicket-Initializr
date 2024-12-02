/*
 * Copyright 2024-2025 stefv
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package my.project.wicket.initializr;

import com.giffing.wicket.spring.boot.starter.app.WicketBootStandardWebApplication;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6CssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeSettings;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import org.apache.wicket.csp.CSPDirective;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * The initializr application.
 */
@SpringBootApplication
public class InitializrApplication extends WicketBootStandardWebApplication {

    /**
     * The main method.
     *
     * @param args The arguments.
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(InitializrApplication.class).run(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() {
        super.init();

        // Add the CSP rules for the Google Fonts
        getCspSettings().blocking().add(CSPDirective.STYLE_SRC, "'self'", "fonts.googleapis.com");
        getCspSettings().blocking().add(CSPDirective.FONT_SRC, "fonts.gstatic.com");
        getCspSettings().blocking().add(CSPDirective.IMG_SRC, "'self'", "data:");

        // Prepare the FontAwesome settings
        FontAwesomeSettings.get(InitializrApplication.get()).setCssResourceReference(FontAwesome6CssReference.instance());

        // Prepare the Bootstrap settings
        final BootstrapSettings settings = new BootstrapSettings();
        settings.setThemeProvider(new BootswatchThemeProvider(BootswatchTheme.Zephyr));
        Bootstrap.install(this, settings);
    }

    /**
     * The template for the REST calls to Maven Central.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
