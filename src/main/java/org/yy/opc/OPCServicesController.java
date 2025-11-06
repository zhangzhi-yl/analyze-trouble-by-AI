package org.yy.opc;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yy.opc.utils.Browser;
import org.yy.opc.utils.DataItem;
import org.yy.opc.utils.JiVariantUtil;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/opc")
public class OPCServicesController {

    @Autowired
    OPCConfig opcConfig;
    
    private static final Logger log = LoggerFactory.getLogger(OPCServicesController.class);


    @GetMapping("items")
    public List<String> getAll(){
        Server server = OPCService.openServer();
        try {
            Collection<String> itemIds = server.getFlatBrowser().browse();
            List<String> result = itemIds.parallelStream().collect(Collectors.toList());
           // System.out.println(result);
            return result;
        } catch (final JIException e) {
            OPCService.disposeServer();
            log.error(e.getMessage());
            log.error(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    @GetMapping("dataitems")
    public List<DataItem> getDataItemsFromServer(){
        final Server server = OPCService.openServer();
        List<DataItem> dataItems = Browser.readSync(server);
        return dataItems;
    }

    @PostMapping("write")
    @ResponseBody
    public DataItem changOpcBool(@RequestBody Map<String,Object> map){
        final Server server = OPCService.openServer();
        try {
            final Group group = server.addGroup("write");
            // Add a new item to the group，
            // 将一个item加入到组，item名字就是MatrikonOPC Server或者KEPServer上面建的项的名字比如：u.u.TAG1，PLC.S7-300.TAG1
            final Item item = group.addItem(map.get("itemId").toString());
            //根据type做数据转型处理
            final JIVariant value = new JIVariant(map.get("value").toString());
            int result = item.write(value);
            DataItem res = JiVariantUtil.parseValue(item.getId(),item.read(false));
            server.removeGroup(group,true);
            return res;
        } catch (final JIException e) {
            log.error(e.getMessage());
            log.error(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (AddFailedException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        } catch (DuplicateGroupException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
