
package jme3tools.shader;

/**
 * Static tool box class for convenient methods to help debug shaders
 * @author Nehon
 */
public class ShaderDebug {

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private ShaderDebug() {
    }

    /**
     * Prepend line numbers to the source code of a shader, for output.
     *
     * @param source the source
     * @return the formatted source code
     */
    public static String formatShaderSource(String source) {      
        String[] sourceLines = source.split("\n");
        int lineNumber = 0;
        StringBuilder out = new StringBuilder();       
        for (String string : sourceLines) {
            lineNumber++;
            out.append(lineNumber).append("\t").append(string).append("\n");
        }
        return out.toString();
    }
}
