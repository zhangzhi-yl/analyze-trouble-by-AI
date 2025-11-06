package org.yy.opc;

import org.springframework.context.annotation.Configuration;
import org.yy.util.Const;

@Configuration
//@ConfigurationProperties(prefix = "opc")
public class OPCConfig {
	private String host= Const.OPC_HOST;
    private String domain=Const.OPC_DOMAIN;
    private String user=Const.OPC_USER;
    private String password=Const.OPC_PASWORD;
    private String progId=Const.PROG_ID;
    private String clsId=Const.CLSZ_ID;
    private String timeout=Const.TIME_OUT;
    private String heartbeat=Const.HEART_BEAT;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProgId() {
        return progId;
    }

    public void setProgId(String progId) {
        this.progId = progId;
    }

    public String getClsId() {
        return clsId;
    }

    public void setClsId(String clsId) {
        this.clsId = clsId;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }
}
