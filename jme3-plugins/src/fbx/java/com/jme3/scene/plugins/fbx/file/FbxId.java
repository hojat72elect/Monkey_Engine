
package com.jme3.scene.plugins.fbx.file;

public abstract class FbxId {

    public static final FbxId ROOT = new LongFbxId(0);
    
    protected FbxId() { }
    
    private static final class StringFbxId extends FbxId {
        
        private final String id;
        
        public StringFbxId(String id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != StringFbxId.class) {
                return false;
            }
            return this.id.equals(((StringFbxId) obj).id);
        }

        @Override
        public String toString() {
            return id;
        }
        
        @Override
        public boolean isNull() {
            return id.equals("Scene\u0000\u0001Model");
        }
    }
    
    private static final class LongFbxId extends FbxId {
        
        private final long id;
        
        public LongFbxId(long id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return (int) (this.id ^ (this.id >>> 32));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != LongFbxId.class) {
                return false;
            }
            return this.id == ((LongFbxId) obj).id;
        }

        @Override
        public boolean isNull() {
            return id == 0;
        }

        @Override
        public String toString() {
            return Long.toString(id);
        }
    }
    
    public abstract boolean isNull(); 
    
    public static FbxId create(Object obj) {
        if (obj instanceof Long) {
            return new LongFbxId((Long)obj);
        } else if (obj instanceof String) {
            return new StringFbxId((String)obj);
        } else {
            throw new UnsupportedOperationException("Unsupported ID object type: " + obj.getClass());
        }
    }
    
    public static FbxId getObjectId(FbxElement el) {
        if (el.propertiesTypes.length == 2
                && el.propertiesTypes[0] == 'S'
                && el.propertiesTypes[1] == 'S') {
            return new StringFbxId((String) el.properties.get(0));
        } else if (el.propertiesTypes.length == 3
                && el.propertiesTypes[0] == 'L'
                && el.propertiesTypes[1] == 'S'
                && el.propertiesTypes[2] == 'S') {
            return new LongFbxId((Long) el.properties.get(0));
        } else {
            return null;
        }
    }
}
