package org.yy.opc;


import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.da.OPCSERVERSTATE;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;


@Service
public class OPCService {

    @Autowired
    private OPCConfig opcConfig;

    private static Server server = null;
    private static AccessBase accessBase = null;

    private static String Host;
    private static String User;
    private static String PassWord;
    private static String ClsId;

    private OPCService(){}

    @PostConstruct
    private void initConfig(){
        Host = opcConfig.getHost();
        User = opcConfig.getUser();
        PassWord = opcConfig.getPassword();
        ClsId = opcConfig.getClsId();
        openServer();
    }

    public synchronized static Server openServer() {

        if (server!=null&&server.getServerState()!=null&&server.getServerState().getServerState()==OPCSERVERSTATE.OPC_STATUS_RUNNING){
            return server;
        } else {
            final ConnectionInformation ci = new ConnectionInformation();
            ci.setHost(Host);         // 电脑IP
            ci.setDomain("");                  // 域，为空就行
            ci.setUser(User);             // 电脑上自己建好的用户名
            ci.setPassword(PassWord);
            ci.setClsid(ClsId);
            // 启动服务
            server = new org.openscada.opc.lib.da.Server(ci, Executors.newSingleThreadScheduledExecutor());
            AutoReconnectController controller = new AutoReconnectController(server);
            // 连接到服务
            // server.connect();
            controller.connect();

            return server;
        }
    }

    public static synchronized AccessBase getAccessBase() throws DuplicateGroupException, NotConnectedException, JIException, UnknownHostException {
        if (accessBase==null){
            accessBase = new SyncAccess(openServer(),1000);
            return accessBase;
        }else {
            return accessBase;
        }
    }

    public static void disposeServer(){
        server.dispose();
        server = null;
    }

}
