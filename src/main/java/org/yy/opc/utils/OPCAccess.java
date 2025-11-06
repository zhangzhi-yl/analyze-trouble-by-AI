package org.yy.opc.utils;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Server;

import java.net.UnknownHostException;

public class OPCAccess extends AccessBase {
    public OPCAccess(Server server, int period) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
        super(server, period);
    }
}
