package org.ngengine.demo.son;

import org.ngengine.components.Component;
import org.ngengine.components.ComponentManager;
import org.ngengine.gui.win.NWindowManagerComponent;
import org.ngengine.runner.Runner;
import org.ngengine.store.DataStoreProvider;

public class LoadingGameState implements Component<Object>{
    private Runnable closeWindow;
    @Override
    public Object getSlot() {
        return "mainState";
    }
    
    @Override
    public void onEnable(ComponentManager mng, Runner runner, DataStoreProvider dataStore, boolean firstTime, Object arg) {
        NWindowManagerComponent windowManager = mng.getComponent(NWindowManagerComponent.class);
        closeWindow = windowManager.showWindow(LoadingWindow.class, null);
    }

    @Override
    public void onDisable(ComponentManager mng, Runner runner, DataStoreProvider dataStore) {
        closeWindow.run();
    }
    
}
