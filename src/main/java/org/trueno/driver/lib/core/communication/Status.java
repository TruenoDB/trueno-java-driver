package org.trueno.driver.lib.core.communication;

/**
 * Created by victor on 9/6/16.
 */
public enum Status {
    SUCCESS("success"),
    ERROR("error")
    ;

    private final String text;

    /**
     * @param text
     */
    private Status(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}