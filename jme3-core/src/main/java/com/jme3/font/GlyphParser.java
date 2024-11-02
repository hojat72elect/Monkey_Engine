
package com.jme3.font;

import com.jme3.export.Savable;

/**
 * Used for selecting character shape in cursive bitmap text. In cursive scripts,
 * the appearance of a letter changes depending on its position:
 * isolated, initial (joined on the left), medial (joined on both sides)
 * and final (joined on the right) of a word.
 *
 * For an example implementation see: https://github.com/Ali-RS/JME-PersianGlyphParser
 *
 * @author Ali-RS
 */
public interface GlyphParser extends Savable {

    public CharSequence parse(CharSequence text);

}