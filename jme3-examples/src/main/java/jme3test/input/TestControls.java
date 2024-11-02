

package jme3test.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;

public class TestControls extends SimpleApplication {
    
    final private ActionListener actionListener = new ActionListener(){
        @Override
        public void onAction(String name, boolean pressed, float tpf){
            System.out.println(name + " = " + pressed);
        }
    };
    final private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            System.out.println(name + " = " + value);
        }
    };

    public static void main(String[] args){
        TestControls app = new TestControls();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Test multiple inputs per mapping
        inputManager.addMapping("My Action",
                new KeyTrigger(KeyInput.KEY_SPACE),
                new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));

        // Test multiple listeners per mapping
        inputManager.addListener(actionListener, "My Action");
        inputManager.addListener(analogListener, "My Action");
    }

}
