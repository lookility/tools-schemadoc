package com.lookility.schemadoc.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Documentation of a node.
 *
 * @see Node
 */
public class Documentation {

    /**
     * Default language.
     * <p>
     * <p>The default language is used if no language is specified.</p>
     */
    public static final String DEFAULT_LANGUAGE = "";

    private Map<String, StringBuilder> languages = new HashMap<>();

    Documentation() {
    }

    /**
     * Appends a text to the documentation for the specified language.
     * <p>
     * The text is separated by a new line from the previous appended text.
     *
     * @param language language identifier (e.g. "de") or <i>null</i> if language is not specified
     * @param text     documentation
     */
    public void append(String language, String text) {
        if (language == null) language = DEFAULT_LANGUAGE;
        StringBuilder doc = this.languages.get(language);
        if (doc == null) {
            doc = new StringBuilder();
            this.languages.put(language, doc);
        }
        if (doc.length() > 0) {
            doc.append('\n');
        }

        doc.append(text);
    }

    /**
     * Returns the documentation text for the specified language.
     *
     * <p>The documentation for the specified language is returned. If not exists, the documentation of the default language is returned. If no documentation is available an empty string is returned.</p>
     *
     * @param language language identifier (e.g. "de") or <i>null</i> if language is not specified.
     * @return documentation or empty string if no documentation exists
     */
    public String getText(String language) {
        if (language == null) language = DEFAULT_LANGUAGE;
        StringBuilder doc = this.languages.get(language);
        if (doc == null) {
            if (isDefaultLanguage(language)) {
                return "";
            } else {
                doc = this.languages.get(DEFAULT_LANGUAGE);
                if (doc == null) return "";
            }
        }
        return doc.toString();
    }

    /**
     * Checks if the specified language is the default language.
     *
     * @param lang language to be checked
     * @return <i>true</i> if given language is the default language, <i>false</i> otherwise
     */
    public boolean isDefaultLanguage(String lang) {
        return lang == null || DEFAULT_LANGUAGE.equals(lang);
    }

    /**
     * Returns a set of languages which are available for the documentation.
     *
     * @return set of languages
     * @see #isDefaultLanguage(String)
     */
    public Set<String> getAvailableLanguages() {
        return this.languages.keySet();
    }
}
