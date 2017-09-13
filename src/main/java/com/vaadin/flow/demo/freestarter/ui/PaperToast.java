package com.vaadin.flow.demo.freestarter.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;

/**
 * Wrapper for the paper-toast element.
 *
 * See https://www.webcomponents.org/element/PolymerElements/paper-toast/elements/paper-toast for details.
 */
@Tag("paper-toast")
@HtmlImport("context://bower_components/paper-toast/paper-toast.html")
public class PaperToast extends Component {
    /**
     * Sets the duration in milliseconds to show the toast.
     * Set to 0, a negative number, or Infinity, to disable the toast auto-closing.
     * @param duration the duration in milliseconds to show the toast
     */
    public void setDuration(int duration) {
        getElement().setProperty("duration", duration);
    }

    /**
     * Gets the duration in milliseconds to show the toast.
     * A value of 0, a negative number, or Infinity, mean that auto-closing is disabled.
     * @return the duration in milliseconds to show the toast
     */
    public int getDuration() {
        return getElement().getProperty("duration", 3000);
    }

    // TODO fitInto?

    /**
     * Sets the orientation against which to align the dropdown content horizontally
     * relative to <code>positionTarget</code>.
     * @param horizontalAlign the orientation against which to align the dropdown content horizontally
     *                        relative to <code>positionTarget</code>
     */
    public void setHorizontalAlign(String horizontalAlign) {
        getElement().setProperty("horizontalAlign", horizontalAlign);
    }

    /**
     * Gets the orientation against which to align the dropdown content horizontally
     * relative to <code>positionTarget</code>.
     * @return the orientation against which to align the dropdown content horizontally
     *         relative to <code>positionTarget</code>
     */
    public String getHorizontalAlign() {
        return getElement().getProperty("horizontalAlign", "left");
    }

    /**
     * Enables or disables auto-focusing the toast or child nodes with the autofocus attribute
     * when the overlay is opened.
     * Set to true to disable auto-focusing.
     * @param noAutoFocus true to disable auto-focusing or false to enable it
     */
    public void setNoAutoFocus(boolean noAutoFocus) {
        getElement().setProperty("noAutoFocus", noAutoFocus);
    }

    /**
     * Gets whether auto-focusing the toast or child nodes with the autofocus attribute
     * when the overlay is opened is enabled or disabled.
     * @return true if auto-focusing disabled or false if enabled
     */
    public boolean getNoAutoFocus() {
        return getElement().getProperty("noAutoFocus", true);
    }

    /**
     * Enables or disables closing of the toast by clicking outside it.
     * @param noCancelOnOutsideClick false to enable closing of the toast by clicking outside it or true to disable it
     */
    public void setNoCancelOnOutsideClick(boolean noCancelOnOutsideClick) {
        getElement().setProperty("noCancelOnOutsideClick", noCancelOnOutsideClick);
    }

    /**
     * Gets whether closing of the toast by clicking outside it is enabled or disabled.
     * @return false if closing of the toast by clicking outside it is enabled or true if disabled
     */
    public boolean getNoCancelOnOutsideClick() {
        return getElement().getProperty("noCancelOnOutsideClick", true);
    }

    /**
     * Sets the text to display in the toast.
     * @param text the text to display in the toast
     */
    public void setText(String text) {
        getElement().setProperty("text", text);
    }

    /**
     * Gets the text to display in the toast.
     * @return the text to display in the toast
     */
    public String getText() {
        return getElement().getProperty("text", "");
    }

    /**
     * Sets the orientation against which to align the dropdown content vertically
     * relative to <code>positionTarget</code>.
     * @param verticalAlign the orientation against which to align the dropdown content vertically
     *                      relative to <code>positionTarget</code>
     */
    public void setVerticalAlign(String verticalAlign) {
        getElement().setProperty("verticalAlign", verticalAlign);
    }

    /**
     * Gets the orientation against which to align the dropdown content vertically
     * relative to <code>positionTarget</code>.
     * @return the orientation against which to align the dropdown content vertically
     *         relative to <code>positionTarget</code>
     */
    public String getVerticalAlign() {
        return getElement().getProperty("verticalAlign", "bottom");
    }

    /**
     * Sets the paper-toast background-color.
     * @param backgroundColor the paper-toast background-color
     */
    public void setBackgroundColor(String backgroundColor) {
        getElement().getStyle().set("backgroundColor", backgroundColor);
    }

    /**
     * Gets the paper-toast background-color.
     * @return the paper-toast background-color
     */
    public String getBackgroundColor() {
        return getElement().getStyle().get("backgroundColor");
    }

    /**
     * Sets the paper-toast color.
     * @param color the paper-toast color
     */
    public void setColor(String color) {
        getElement().getStyle().set("color", color);
    }

    /**
     * Gets the paper-toast color.
     * @return the paper-toast color
     */
    public String getColor() {
        return getElement().getStyle().get("color");
    }

    /**
     * Shows the toast.
     */
    public void show() {
        getElement().callFunction("open");
    }

    /**
     * Shows the toast with the given text.
     * @param text the text to display
     */
    public void show(String text) {
        getElement().callFunction("show", text);
    }

    /**
     * Shows the toast with the given text for the given duration (in milliseconds).
     * @param text the text to display
     * @param duration the duration in milliseconds to show the toast
     */
    public void show(String text, int duration) {
        setDuration(duration);
        show(text);
    }

    /**
     * Hides the toast.
     */
    public void hide() {
        getElement().callFunction("hide");
    }

    /**
     * Adds a listener for the iron-announce event.
     * @param ironAnnounceListener  the listener to add
     * @return  the registration which allows the listener to be removed
     */
    public Registration addIronAnnounceListener(
            ComponentEventListener<IronAnnounceEvent> ironAnnounceListener) {
        return super.addListener(IronAnnounceEvent.class, ironAnnounceListener);
    }
}
