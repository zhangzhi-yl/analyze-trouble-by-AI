package org.yy.opc.websocket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TestClient {

    public static Session session = null;

    static {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://172.16.30.154:11031/energy/survey";
        System.out.println("Connecting to " + uri);
        String token = "Hm_lvt_5f036dd99455cb8adc9de73e2f052f72=1633922533; JSESSIONID=DB30753B4413ABEEDABA9E253B0BA2AF";
        session = connection(uri,token);
    }

    //设置请求头
    public static Session connection(String uri, String cookieValue){
        Session session = null;

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig cec = ClientEndpointConfig.Builder
                    .create().configurator(new ClientEndpointConfig.Configurator() {
                        public void beforeRequest(Map<String, List<String>> headers) {
                            System.out.println("Setting user cookie in beforeRequest ...");
                            if(null != cookieValue && !cookieValue.isEmpty()){
                                ArrayList<String> value = new ArrayList<>();
                                value.add(cookieValue);
                                // 设置header，
                                headers.put("Cookie", value);
                            }

                        }
                    }).build();
            session = container.connectToServer(Client.class,cec, URI.create(uri));
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return session;
    }

    public static void main(String args[]) throws IOException {
        session.getBasicRemote().sendText("hahahaha");
    }
}
