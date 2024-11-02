
package com.jme3.export.binary;

import java.util.HashMap;

class BinaryClassObject {

    // When exporting, use nameFields field, importing use aliasFields.
    HashMap<String, BinaryClassField> nameFields;
    HashMap<Byte, BinaryClassField> aliasFields;
    
    byte[] alias;
    String className;
    int[] classHierarchyVersions;
}
