/*
Copyright (c) 2005, Pete Bevin.
<http://markdownj.petebevin.com>

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

* Redistributions of source code must retain the above copyright notice,
  this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in the
  documentation and/or other materials provided with the distribution.

* Neither the name "Markdown" nor the names of its contributors may
  be used to endorse or promote products derived from this software
  without specific prior written permission.

This software is provided by the copyright holders and contributors "as
is" and any express or implied warranties, including, but not limited
to, the implied warranties of merchantability and fitness for a
particular purpose are disclaimed. In no event shall the copyright owner
or contributors be liable for any direct, indirect, incidental, special,
exemplary, or consequential damages (including, but not limited to,
procurement of substitute goods or services; loss of use, data, or
profits; or business interruption) however caused and on any theory of
liability, whether in contract, strict liability, or tort (including
negligence or otherwise) arising in any way out of the use of this
software, even if advised of the possibility of such damage.

*/

package com.petebevin.markdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


/**
 * Mutable String with common operations used in Markdown processing.
 */
public class TextEditor {
    private StringBuffer text;

    /**
     * Create a new TextEditor based on the contents of a String or
     * StringBuffer.
     *
     * @param text
     */
    public TextEditor(CharSequence text) {
        this.text = new StringBuffer(text.toString());
    }

    /**
     * Give up the contents of the TextEditor.
     * @return
     */
    @Override
    public String toString() {
        return text.toString();
    }

    /**
     * Replace all occurrences of the regular expression with the replacement.  The replacement string
     * can contain $1, $2 etc. referring to matched groups in the regular expression.
     *
     * @param regex
     * @param replacement
     * @return
     */
    public TextEditor replaceAll(String regex, String replacement) {
        if (text.length() > 0) {
            final String r = replacement;
            Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
            Matcher m = p.matcher(text);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, r);
            }
            m.appendTail(sb);
            text = sb;
        }
        return this;
    }

    /**
     * Same as replaceAll(String, String), but does not interpret
     * $1, $2 etc. in the replacement string.
     * @param regex
     * @param replacement
     * @return
     */
    public TextEditor replaceAllLiteral(String regex, final String replacement) {
        return replaceAll(Pattern.compile(regex, Pattern.MULTILINE), new Replacement() {
            public String replacement(Matcher m) {
                return replacement;
            }
        });
    }

    /**
     * Replace all occurrences of the Pattern.  The Replacement object's replace() method is
     * called on each match, and it provides a replacement, which is placed literally
     * (i.e., without interpreting $1, $2 etc.)
     *
     * @param pattern
     * @param replacement
     * @return
     */
    public TextEditor replaceAll(Pattern pattern, Replacement replacement) {
        Matcher m = pattern.matcher(text);
        int lastIndex = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            sb.append(text.subSequence(lastIndex, m.start()));
            sb.append(replacement.replacement(m));
            lastIndex = m.end();
        }
        sb.append(text.subSequence(lastIndex, text.length()));
        text = sb;
        return this;
    }

    /**
     * Remove all occurrences of the given regex pattern, replacing them
     * with the empty string.
     *
     * @param pattern Regular expression
     * @return
     * @see java.util.regex.Pattern
     */
    public TextEditor deleteAll(String pattern) {
        return replaceAll(pattern, "");
    }

    /**
     * Convert tabs to spaces given the default tab width of 4 spaces.
     * @return
     */
    public TextEditor detabify() {
        return detabify(4);
    }

    /**
     * Convert tabs to spaces.
     *
     * @param tabWidth  Number of spaces per tab.
     * @return
     */
    public TextEditor detabify(final int tabWidth) {
        replaceAll(Pattern.compile("(.*?)\\t"), new Replacement() {
            public String replacement(Matcher m) {
                String lineSoFar = m.group(1);
                int width = lineSoFar.length();
                StringBuffer replacement = new StringBuffer(lineSoFar);
                do {
                    replacement.append(' ');
                    ++width;
                } while (width % tabWidth != 0);
                return replacement.toString();
            }
        });
        return this;
    }

    /**
     * Remove a number of spaces at the start of each line.
     * @param spaces
     * @return
     */
    public TextEditor outdent(int spaces) {
        return deleteAll("^(\\t|[ ]{1," + spaces + "})");
    }

    /**
     * Remove one tab width (4 spaces) from the start of each line.
     * @return
     */
    public TextEditor outdent() {
        return outdent(4);
    }

    /**
     * Remove leading and trailing space from the start and end of the buffer.  Intermediate
     * lines are not affected.
     * @return
     */
    public TextEditor trim() {
        text = new StringBuffer(text.toString().trim());
        return this;
    }

    /**
     * Introduce a number of spaces at the start of each line.
     * @param spaces
     * @return
     */
    public TextEditor indent(int spaces) {
        StringBuffer sb = new StringBuffer(spaces);
        for (int i = 0; i < spaces; i++) {
            sb.append(' ');
        }
        return replaceAll("^", sb.toString());
    }

    /**
     * Add a string to the end of the buffer.
     * @param s
     */
    public void append(CharSequence s) {
        text.append(s);
    }

    /**
     * Parse HTML tags, returning a Collection of HTMLToken objects.
     * @return
     */
    public Collection<HTMLToken> tokenizeHTML() {
        List<HTMLToken> tokens = new ArrayList<HTMLToken>();
        String nestedTags = nestedTagsRegex(6);

        Pattern p = Pattern.compile("" +
                "(?s:<!(--.*?--\\s*)+>)" +
                "|" +
                "(?s:<\\?.*?\\?>)" +
                "|" +
                nestedTags +
                "", Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(text);
        int lastPos = 0;
        while (m.find()) {
            if (lastPos < m.start()) {
                tokens.add(HTMLToken.text(text.substring(lastPos, m.start())));
            }
            tokens.add(HTMLToken.tag(text.substring(m.start(), m.end())));
            lastPos = m.end();
        }
        if (lastPos < text.length()) {
            tokens.add(HTMLToken.text(text.substring(lastPos, text.length())));
        }

        return tokens;
    }

    /**
     * Regex to match a tag, possibly with nested tags such as <a href="<MTFoo>">.
     *
     * @param depth - How many levels of tags-within-tags to allow.  The example <a href="<MTFoo>"> has depth 2.
     */
    private String nestedTagsRegex(int depth) {
        if (depth == 0) {
            return "";
        } else {
            return "(?:<[a-z/!$](?:[^<>]|" + nestedTagsRegex(depth - 1) + ")*>)";
        }
    }

    /**
     * Add a string to the start of the first line of the buffer.
     * @param s
     */
    public void prepend(CharSequence s) {
        StringBuffer newText = new StringBuffer();
        newText.append(s);
        newText.append(text);
        text = newText;
    }

    /**
     * Find out whether the buffer is empty.
     * @return
     */
    public boolean isEmpty() {
        return text.length() == 0;
    }
}
