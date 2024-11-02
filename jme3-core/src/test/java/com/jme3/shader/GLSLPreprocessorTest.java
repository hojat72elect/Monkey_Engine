
package com.jme3.shader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.system.TestUtil;

import org.junit.Test;

import jme3tools.shader.Preprocessor;


public class GLSLPreprocessorTest {

    String readAllAsString(InputStream is) throws Exception{
        String output = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            String l = reader.readLine();
            if (l == null) break;
            if (output != "") output += "\n";
            output += l;
        }
        reader.close();
        return output;
    }
    
    @Test
    public void testFOR() throws Exception{
        String source = "#for i=0..2 (#ifdef IS_SET$i $0 #endif)\n" +
                "  uniform float m_Something$i;\n" +
                "#endfor";
        String processedSource= readAllAsString(Preprocessor.apply(new ByteArrayInputStream(source.getBytes("UTF-8"))));

        AssetInfo testData = TestUtil.createAssetManager().locateAsset(new AssetKey("GLSLPreprocessorTest.testFOR.validOutput"));
        assertNotNull(testData);
        String sourceCheck=readAllAsString(testData.openStream());
        assertEquals(sourceCheck, processedSource);                  
    }
}
