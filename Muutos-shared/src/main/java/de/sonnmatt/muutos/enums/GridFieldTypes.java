package de.sonnmatt.muutos.enums;

public enum GridFieldTypes {
	Text("TEXT"), 
	Date("DATE"), 
	Integer("INTEGER"),
	Double("DOUBLE"), 
	Selection("SELECTION")
	;

    private final String text;

    /**
     * @param text
     */
    private GridFieldTypes(final String text) {
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
