package org.ngengine.app;

import org.ngengine.NGEApplication;
import org.ngengine.NGEApplication.NGEAppRunner;
import org.ngengine.components.ComponentManager;
 
import org.ngengine.demo.son.BaseEnvironment;
import org.ngengine.demo.son.HelloGameState;
import org.ngengine.demo.son.LoadingGameState;
import org.ngengine.demo.son.LobbyGameState;
import org.ngengine.demo.son.PhysicsManager;
import org.ngengine.demo.son.PlayGameState;
import org.ngengine.demo.son.ocean.OceanAppState;
import org.ngengine.gui.win.NWindowManagerComponent;
import org.ngengine.player.PlayerManagerComponent;

import com.jme3.system.AppSettings;

public class Main {
    
    
    
    public static NGEAppRunner main(String arg[]){
        NGEAppRunner appBuilder = NGEApplication.createApp(
            app -> {
                ComponentManager mng = app.getComponentManager();
                mng.addAndEnableComponent(new BaseEnvironment());
                mng.addAndEnableComponent(new NWindowManagerComponent());
                mng.addAndEnableComponent(new PlayerManagerComponent());
                mng.addAndEnableComponent(new PhysicsManager());
                mng.addAndEnableComponent(new LoadingGameState());

                mng.addComponent(new OceanAppState());
                mng.addComponent(new LobbyGameState(), NWindowManagerComponent.class, PlayerManagerComponent.class);
                mng.addComponent(
                    new PlayGameState(),
                    NWindowManagerComponent.class,
                    PlayerManagerComponent.class,
                    OceanAppState.class,
                    PhysicsManager.class
                );
                mng.addComponent(
                    new HelloGameState(),
                    BaseEnvironment.class,
                    NWindowManagerComponent.class,
                    PlayerManagerComponent.class
                );

                mng.enableComponent(HelloGameState.class);
            }
        );
        return appBuilder;
    }
}
