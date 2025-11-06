package org.yy.opc.utils;

public class ServerInfo {
    private String progId;
    private String clsId;
    private String description;

    public ServerInfo(String progId,String clsId,String description){
        this.progId = progId;
        this.clsId = clsId;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
