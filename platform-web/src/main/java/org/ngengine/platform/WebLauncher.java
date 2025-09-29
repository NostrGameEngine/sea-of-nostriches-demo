package org.ngengine.platform;

import org.ngengine.NGEApplication.NGEAppRunner;
import org.ngengine.app.Main;
import org.ngengine.platform.NGEPlatform;
import org.ngengine.platform.teavm.TeaVMPlatform;
import org.ngengine.web.context.WebSystem;
import org.ngengine.web.filesystem.WebResourceLoader;
import org.ngengine.web.json.TeaJSONParser;

import com.jme3.plugins.json.Json;
import com.jme3.system.JmeSystem;
import com.jme3.util.BufferAllocator;
import com.jme3.util.BufferAllocatorFactory;
import com.jme3.util.BufferUtils;
import com.jme3.util.res.Resources;


public class WebLauncher {

    public static void main(String[] args)   {
        new Thread(()->{ // must be a thread to run in a suspendable context in teavm
            NGEPlatform.set(new TeaVMPlatform());
            JmeSystem.setSystemDelegate(new WebSystem());
    
            NGEAppRunner runner = Main.main(args);
            runner.start();
        }).start();
    }
}