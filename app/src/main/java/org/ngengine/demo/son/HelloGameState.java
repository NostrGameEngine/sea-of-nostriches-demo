/**
 * Copyright (c) 2025, Nostr Game Engine
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Nostr Game Engine is a fork of the jMonkeyEngine, which is licensed under
 * the BSD 3-Clause License. The original jMonkeyEngine license is as follows:
 */
package org.ngengine.demo.son;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import com.jme3.util.TempVars;
import org.ngengine.auth.AuthSelectionWindow;
import org.ngengine.auth.AuthStrategy;
import org.ngengine.components.Component;
import org.ngengine.components.ComponentManager;
import org.ngengine.components.fragments.AppFragment;
import org.ngengine.components.fragments.MainViewPortFragment;
import org.ngengine.gui.win.NWindowManagerComponent;
import org.ngengine.player.PlayerManagerComponent;
import org.ngengine.runner.Runner;
import org.ngengine.store.DataStoreProvider;

public class HelloGameState implements Component<Object>, AppFragment, MainViewPortFragment {

    private AppSettings settings;

    public HelloGameState() {}

    @Override
    public Object getSlot() {
        return "mainState";
    }

    @Override
    public void receiveApplication(Application app) {
        settings = app.getContext().getSettings();
    }

    @Override
    public void onEnable(
        ComponentManager fragmentManager,
        Runner runner,
        DataStoreProvider dataStoreProvider,
        boolean firstTime,
        Object slot
    ) {
        PlayerManagerComponent playerManager = fragmentManager.getComponent(PlayerManagerComponent.class);

        // create an authentication strategy that toggles the lobby app state when a signer is available
        AuthStrategy authStrategy = Settings.authStrategy(
            settings,
            signer -> {
                fragmentManager.enableComponent(LobbyGameState.class, signer);
            },
            playerManager
        );

        // open auth window
        NWindowManagerComponent windowManager = fragmentManager.getComponent(NWindowManagerComponent.class);
        windowManager.showWindow(AuthSelectionWindow.class, authStrategy);
    }

    @Override
    public void updateMainViewPort(ViewPort viewPort, float tpf) {
        TempVars vars = TempVars.get();
        try {
            float angles[] = vars.fADdU;
            Camera cam = viewPort.getCamera();
            cam.getRotation().toAngles(angles);
            angles[1] = FastMath.interpolateLinear(tpf, angles[1], FastMath.PI * 0.1f);
            angles[0] = FastMath.interpolateLinear(tpf, angles[0], 0f);

            cam.setRotation(cam.getRotation().fromAngles(angles));
        } finally {
            vars.release();
        }
    }

    @Override
    public void onDisable(ComponentManager mng, Runner runner, DataStoreProvider dataStoreProvider) {}

    @Override
    public void loadMainViewPortFilterPostprocessor(AssetManager assetManager, FilterPostProcessor fpp) {}

    @Override
    public void receiveMainViewPort(ViewPort viewPort) {}

    @Override
    public void receiveMainViewPortFilterPostProcessor(FilterPostProcessor fpp) {}
}
