package org.ngengine.demo.son;


import org.ngengine.gui.components.NLabel;
import org.ngengine.gui.win.NWindow;

import com.jme3.math.Vector3f;

public class LoadingWindow extends NWindow<Object>{

    @Override
    protected void compose(Vector3f size, Object args) throws Throwable {
        setWithTitleBar(false);
        
        NLabel label = new NLabel("Loading...");
        label.setFontSize(size.y*0.3f);
        getContent().addCol().addChild(label);
        
    }
    
}
