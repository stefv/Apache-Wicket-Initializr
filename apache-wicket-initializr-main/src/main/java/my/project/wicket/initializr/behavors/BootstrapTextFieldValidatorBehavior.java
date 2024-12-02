package my.project.wicket.initializr.behavors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;

/**
 * A behavior that adds Bootstrap validation classes to a text field.
 */
public class BootstrapTextFieldValidatorBehavior extends Behavior {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        super.onComponentTag(component, tag);
        if (((TextField<?>) component).isValid()) {
            tag.put("class", tag.getAttribute("class").replaceAll("\\s+is-(in)?valid", "") + " is-valid");
        } else {
            tag.put("class", tag.getAttribute("class").replaceAll("\\s+is-(in)?valid", "") + " is-invalid");
        }
    }
}
