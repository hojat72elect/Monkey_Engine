
package com.jme3.material.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class that parses a define condition in a GLSL language
 * style.
 *
 * extractDefines is able to get a list of defines in an expression and update
 * the formatter expression with uppercased defines
 *
 * @author Nehon
 */
public class ConditionParser {

    private String formattedExpression = "";

    public static void main(String argv[]) {
        ConditionParser parser = new ConditionParser();
        //List<String> defines = parser.extractDefines("(LightMap && SeparateTexCoord) || !ColorMap");
        List<String> defines = parser.extractDefines("RoughnessMap && MetallicRoughnessMap");
        for (String string : defines) {
            System.err.println(string);
        }
        System.err.println(parser.formattedExpression);

        defines = parser.extractDefines("#if (defined(LightMap) && defined(SeparateTexCoord)) || !defined(ColorMap)");

        for (String string : defines) {
            System.err.println(string);
        }
        System.err.println(parser.formattedExpression);


//        System.err.println(parser.getFormattedExpression());
//        
//        parser.parse("ShaderNode.var.xyz");
//        parser.parse("var.xyz");
//        parser.parse("ShaderNode.var");
//        parser.parse("var");
    }

    /**
     * Parses a condition and returns the list of defines of this condition.
     * Additionally, this method updates the formattedExpression with uppercased
     * defines names.
     *
     * supported expression syntax example:<br><br>
     * {@code "(LightMap && SeparateTexCoord) || !ColorMap"}<br><br>
     * {@code "#if (defined(LightMap) && defined(SeparateTexCoord)) || !defined(ColorMap)"}<br><br>
     * {@code "#ifdef LightMap"}<br><br>
     * {@code "#ifdef (LightMap && SeparateTexCoord) || !ColorMap"}<br>
     *
     * @param expression the expression to parse
     * @return the list of defines
     */
    public List<String> extractDefines(String expression) {
        List<String> defines = new ArrayList<>();
        expression = expression.replaceAll("#ifdef", "").replaceAll("#if", "").replaceAll("defined", "");
        Pattern pattern = Pattern.compile("(\\w+)");
        formattedExpression = expression;
        Matcher m = pattern.matcher(expression);
        while (m.find()) {
            String match = m.group();
            defines.add(match);
            formattedExpression = formattedExpression.replaceAll("\\b" + match + "\\b", "defined(" + match.toUpperCase() + ")");
        }
        return defines;
    }

    /**
     *
     * @return the formatted expression previously updated by extractDefines
     */
    public String getFormattedExpression() {
        return formattedExpression;
    }
}