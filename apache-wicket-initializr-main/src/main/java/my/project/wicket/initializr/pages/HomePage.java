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
package my.project.wicket.initializr.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import my.project.wicket.initializr.beans.ArtifactVersionViewBean;
import my.project.wicket.initializr.panels.ArtifactVersionDropDownChoice;
import my.project.wicket.initializr.services.IMavenCentralQueryService;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * The home page.
 */
@WicketHomePage
@MountPath(value = "/", alt = {"index"})
public class HomePage extends WebPage {

    /**
     * The Maven Central query service to get the version of Apache Wicket.
     */
    @SpringBean
    private IMavenCentralQueryService mavenCentralQueryService;

    /**
     * The selected version of Apache Wicket.
     */
    private ArtifactVersionViewBean wicketVersion;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        // The Apache Wicket logo
        add(newApacheWicketLogo());

        // The form
        final Form<Void> form = new StatelessForm<>("form");
        form.add(newProject());
        form.add(newWicketVersion());
        add(form);
    }

    /**
     * Create the Wicket version component.
     *
     * @return The new Wicket version component.
     */
    protected Component newWicketVersion() {
        final List<ArtifactVersionViewBean> versions = mavenCentralQueryService.fetchWicketCoreVersions();
        if (wicketVersion == null) {
            wicketVersion = versions.stream().findFirst().orElse(null);
        }
        return new ArtifactVersionDropDownChoice("wicketVersions", Model.of(wicketVersion), versions);
    }

    /**
     * Create the project type component.
     *
     * @return The new project type component.
     */
    protected Component newProject() {
        // Prepare the project types
        final LinkedHashSet<ProjectType> options = new LinkedHashSet<>(Arrays.stream(ProjectType.values()).toList());
        final Model<ProjectType> selectedOption = Model.of(options.stream().findFirst().orElse(null));
        final RadioGroup<ProjectType> radioGroup = new RadioGroup<>("projectRadioGroup", selectedOption);
        final ListView<ProjectType> radioList = new ListView<>("projectRadios", options.stream().toList()) {
            @Override
            protected void populateItem(ListItem<ProjectType> item) {
                final ProjectType projectType = item.getModelObject();
                final Radio<ProjectType> projectRadio = new Radio<>("projectRadio", Model.of(projectType));
                if (projectType.isDisabled()) {
                    projectRadio.add(AttributeModifier.append("disabled", "disabled"));
                }
                projectRadio.setOutputMarkupId(true);
                final Label projectLabel = new Label("projectLabel", projectType.getLabel());
                projectLabel.add(AttributeModifier.append("for", projectRadio.getMarkupId()));
                item.add(projectRadio, projectLabel);
            }
        };
        radioGroup.add(radioList);
        return radioGroup;
    }

    /**
     * Create the Apache Wicket logo in the header.
     *
     * @return The Apache Wicket logo.
     */
    protected Image newApacheWicketLogo() {
        return new Image("apacheWicketLogo", new ResourceReference("apache-wicket-logo") {
            @Override
            public IResource getResource() {
                return new PackageResourceReference(HomePage.class, "wicket-logo.svg").getResource();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssContentHeaderItem.forReference(new CssResourceReference(HomePage.class, "style.css")));
    }
}
