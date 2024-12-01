package my.project.wicket.initializr.panels;

import my.project.wicket.initializr.beans.ArtifactVersionViewBean;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.LambdaChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

import java.util.List;

/**
 * A drop-down choice for selecting artifact versions.
 */
public class ArtifactVersionDropDownChoice extends DropDownChoice<ArtifactVersionViewBean> {

    /**
     * Constructor.
     *
     * @param id      The component id.
     * @param model   The model for the selected item.
     * @param choices The list of ArtifactVersionViewBean choices.
     */
    public ArtifactVersionDropDownChoice(String id, IModel<ArtifactVersionViewBean> model, List<ArtifactVersionViewBean> choices) {
        super(id, model, choices, new LambdaChoiceRenderer<>(ArtifactVersionViewBean::getVersion));
    }

    @Override
    protected CharSequence getDefaultChoice(String selectedValue) {
        // No default choice, it will be handled by the grouping mechanism
        return null;
    }

    @Override
    protected void appendOptionHtml(AppendingStringBuffer buffer, ArtifactVersionViewBean choice, int index, String selected) {
        int currentMajor = choice.getMajor();

        // If the current item is the first of a new major version, start a new <optgroup>
        if (isFirstOfGroup(index, currentMajor)) {
            // Close the previous group if not the first item
            if (index > 0) {
                buffer.append("</optgroup>");
            }

            // Open a new <optgroup>
            buffer.append(String.format("<optgroup label=\"%d.x\">", currentMajor));
        }

        // Add the individual <option>
        super.appendOptionHtml(buffer, choice, index, selected);

        // Close the last <optgroup> at the end of the list
        if (index == getChoices().size() - 1) {
            buffer.append("</optgroup>");
        }
    }

    /**
     * Determines if the current choice is the first in a group (based on major version).
     *
     * @param index The index of the current choice.
     * @param major The major version of the current choice.
     * @return true if it's the first of the group, false otherwise.
     */
    private boolean isFirstOfGroup(int index, int major) {
        if (index == 0) {
            return true;
        }

        // Compare the current item's major version with the previous item's major version
        ArtifactVersionViewBean previousChoice = getChoices().get(index - 1);
        return previousChoice.getMajor() != major;
    }
}
