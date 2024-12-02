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
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6CssReference;
import my.project.wicket.initializr.beans.ArtifactVersionViewBean;
import my.project.wicket.initializr.behavors.BootstrapTextFieldValidatorBehavior;
import my.project.wicket.initializr.panels.ArtifactVersionDropDownChoice;
import my.project.wicket.initializr.services.IMavenCentralQueryService;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;
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
     * Constructor.
     */
    public HomePage() {
    }

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
        form.add(newPackaging());
        form.add(newProjectMetadataGroup());
        form.add(newProjectMetadataArtifact());
        form.add(newProjectMetadataName());
        form.add(newProjectMetadataDescription());
        form.add(newProjectMetadataPackageName());
        add(form);
    }

    /**
     * Create the project metadata group.
     *
     * @return The new project metadata group.
     */
    private Component newProjectMetadataGroup() {
        final String PATTERN = "^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$";
        final TextField<String> group = new TextField<>("group", Model.of(""));
        group.setRequired(true);
        group.validate();
        group.add(new AttributeModifier("pattern", PATTERN));
        group.add(new PatternValidator(PATTERN));
        group.add(new BootstrapTextFieldValidatorBehavior());
        return group;
    }

    /**
     * Create the project metadata artifact.
     *
     * @return The new project metadata artifact.
     */
    private Component newProjectMetadataArtifact() {
        final String PATTERN = "^[a-zA-Z0-9_\\-\\.]+$";
        final TextField<String> artifact = new TextField<>("artifact", Model.of(""));
        artifact.setRequired(true);
        artifact.validate();
        artifact.add(new AttributeModifier("pattern", PATTERN));
        artifact.add(new PatternValidator(PATTERN));
        artifact.add(new BootstrapTextFieldValidatorBehavior());
        return artifact;
    }

    /**
     * Create the project metadata package name.
     *
     * @return The new project metadata package name.
     */
    private Component newProjectMetadataPackageName() {
        final String PATTERN = "^[a-z]+(\\.[a-z][a-z0-9_]*)*$";
        final TextField<String> packageName = new TextField<>("packageName", Model.of(""));
        packageName.setRequired(true);
        packageName.validate();
        packageName.add(new AttributeModifier("pattern", PATTERN));
        packageName.add(new PatternValidator(PATTERN));
        packageName.add(new BootstrapTextFieldValidatorBehavior());
        return packageName;
    }

    /**
     * Create the project metadata name.
     *
     * @return The new project metadata name.
     */
    private Component newProjectMetadataName() {
        return new TextField<>("name", Model.of(""));
    }

    /**
     * Create the project metadata description.
     *
     * @return The new project metadata description.
     */
    private Component newProjectMetadataDescription() {
        return new TextArea<>("description", Model.of(""));
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
     * Create the packaging component.
     *
     * @return The new packaging component.
     */
    protected Component newPackaging() {
        // Prepare the project types
        final LinkedHashSet<Packaging> options = new LinkedHashSet<>(Arrays.stream(Packaging.values()).toList());
        final Model<Packaging> selectedOption = Model.of(options.stream().findFirst().orElse(null));
        final RadioGroup<Packaging> radioGroup = new RadioGroup<>("packagingRadioGroup", selectedOption);
        final ListView<Packaging> radioList = new ListView<>("packagingRadios", options.stream().toList()) {
            @Override
            protected void populateItem(ListItem<Packaging> item) {
                final Packaging packaging = item.getModelObject();
                final Radio<Packaging> packagingRadio = new Radio<>("packagingRadio", Model.of(packaging));
                packagingRadio.setOutputMarkupId(true);
                final Label packagingLabel = new Label("packagingLabel", packaging.getLabel());
                packagingLabel.add(AttributeModifier.append("for", packagingRadio.getMarkupId()));
                final WebMarkupContainer popoverIcon = new WebMarkupContainer("packagingPopover");
                popoverIcon.add(new AttributeAppender("data-bs-content", new StringResourceModel(packaging.getKey(), HomePage.this)));
                item.add(packagingRadio, packagingLabel, popoverIcon);
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
        response.render(CssHeaderItem.forReference(FontAwesome6CssReference.instance()));
        response.render(OnDomReadyHeaderItem.forScript("const popoverTriggerList = document.querySelectorAll('[data-bs-toggle=\"popover\"]');const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl, {trigger:'focus'}));"));
    }
}
