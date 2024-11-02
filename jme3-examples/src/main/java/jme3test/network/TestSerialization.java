

package jme3test.network;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.*;

public class TestSerialization implements MessageListener<HostedConnection> {

    @Serializable
    public static class SomeObject {

        private int val;

        public SomeObject(){
        }

        public SomeObject(int val){
            this.val = val;
        }

        public int getVal(){
            return val;
        }

        @Override
        public String toString(){
            return "SomeObject[val="+val+"]";
        }
    }

    public enum Status {
        High,
        Middle,
        Low;
    }

    @Serializable
    public static class TestSerializationMessage extends AbstractMessage {

        private boolean z;
        private byte b;
        private char c;
        private short s;
        private int i;
        private float f;
        private long l;
        private double d;
        
        private int[] ia;
        private List<Object> ls;
        private Map<String, SomeObject> mp;

        private Status status1;
        private Status status2;

        private Date date;

        public TestSerializationMessage(){
            super(true);
        }

        public TestSerializationMessage(boolean initIt){
            super(true);
            if (initIt){
                z = true;
                b = -88;
                c = 'Y';
                s = 9999;
                i = 123;
                f = -75.4e8f;
                l = 9438345072805034L;
                d = -854834.914703e88;
                ia = new int[]{ 456, 678, 999 };

                ls = new ArrayList<Object>();
                ls.add("hello");
                ls.add(new SomeObject(-22));

                mp = new HashMap<String, SomeObject>();
                mp.put("abc", new SomeObject(555));

                status1 = Status.High;
                status2 = Status.Middle;

                date = new Date(System.currentTimeMillis());
            }
        }
    }

    @Override
    public void messageReceived(HostedConnection source, Message m) {
        TestSerializationMessage cm = (TestSerializationMessage) m;
        System.out.println(cm.z);
        System.out.println(cm.b);
        System.out.println(cm.c);
        System.out.println(cm.s);
        System.out.println(cm.i);
        System.out.println(cm.f);
        System.out.println(cm.l);
        System.out.println(cm.d);
        System.out.println(Arrays.toString(cm.ia));
        System.out.println(cm.ls);
        System.out.println(cm.mp);
        System.out.println(cm.status1);
        System.out.println(cm.status2);
        System.out.println(cm.date);
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        Serializer.registerClass(SomeObject.class);
        Serializer.registerClass(TestSerializationMessage.class);

        Server server = Network.createServer( 5110 );
        server.start();

        Client client = Network.connectToServer( "localhost", 5110 ); 
        client.start();

        server.addMessageListener(new TestSerialization(), TestSerializationMessage.class);
        client.send(new TestSerializationMessage(true));
        
        Thread.sleep(10000);
    }

}
