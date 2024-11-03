
package com.jme3.material.plugins;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.ShaderNodeDefinitionKey;
import com.jme3.util.blockparser.BlockLanguageParser;
import com.jme3.util.blockparser.Statement;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * ShaderNodeDefinition file loader (.j3sn)
 *
 * a j3sn file is a block style file like j3md or j3m. It must contain one
 * ShaderNodeDefinition{} block that contains several ShaderNodeDefinition{}
 * blocks
 *
 * @author Nehon
 */
public class ShaderNodeDefinitionLoader implements AssetLoader {

    private ShaderNodeLoaderDelegate loaderDelegate;

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        AssetKey k = assetInfo.getKey();
        if (!(k instanceof ShaderNodeDefinitionKey)) {
            throw new IOException("ShaderNodeDefinition file must be loaded via ShaderNodeDefinitionKey");
        }
        ShaderNodeDefinitionKey key = (ShaderNodeDefinitionKey) k;
        loaderDelegate = new ShaderNodeLoaderDelegate();

        InputStream in = assetInfo.openStream();
        List<Statement> roots = BlockLanguageParser.parse(in);

        if (roots.size() == 2) {
            Statement exception = roots.get(0);
            String line = exception.getLine();
            if (line.startsWith("Exception")) {
                throw new AssetLoadException(line.substring("Exception ".length()));
            } else {
                throw new MatParseException("In multi-root shader node definition, expected first statement to be 'Exception'", exception);
            }
        } else if (roots.size() != 1) {
            throw new MatParseException("Too many roots in J3SN file", roots.get(0));
        }

        return loaderDelegate.readNodesDefinitions(roots.get(0).getContents(), key);

    }
}
