

package com.jme3.network.service.serializer;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.message.SerializerRegistrationsMessage;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.service.AbstractClientService;
import com.jme3.network.service.ClientServiceManager;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 *
 *  @author    Paul Speed
 */
public class ClientSerializerRegistrationsService extends AbstractClientService 
                                                  implements MessageListener<Client> {
                                                  
    private static final Logger log = Logger.getLogger(SerializerRegistrationsMessage.class.getName());

    @Override
    protected void onInitialize( ClientServiceManager serviceManager ) {

        // Make sure our message type is registered if it isn't already
        if( Serializer.getExactSerializerRegistration(SerializerRegistrationsMessage.class) == null ) {
            // This is the minimum we'd need just to be able to register
            // the rest... otherwise we can't even receive this message.
            Serializer.registerClass(SerializerRegistrationsMessage.class);
            Serializer.registerClass(SerializerRegistrationsMessage.Registration.class);
        } else {
            log.log(Level.INFO, "Skipping registration of SerializerRegistrationsMessage.");
        }
        
        // Add our listener for that message type
        serviceManager.getClient().addMessageListener(this, SerializerRegistrationsMessage.class); 
    }

    @Override
    public void messageReceived( Client source, Message m ) {
        // We only wait for one kind of message...
        SerializerRegistrationsMessage msg = (SerializerRegistrationsMessage)m;
        msg.registerAll();
    }    
}
