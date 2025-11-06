package org.yy.opc.websocket;

import javax.websocket.*;

@ClientEndpoint
public class Client extends Endpoint{

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }
}
