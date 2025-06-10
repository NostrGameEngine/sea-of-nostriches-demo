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

import com.jme3.system.AppSettings;
import org.ngengine.NGEApplication;
import org.ngengine.components.ComponentManager;
import org.ngengine.demo.son.ocean.OceanAppState;
import org.ngengine.gui.win.NWindowManagerComponent;
import org.ngengine.player.PlayerManagerComponent;

public class SonGame {

    // mac needs to start with -XstartOnFirstThread -Djava.awt.headless=true
    public static void main(String[] args) throws InterruptedException {
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setGammaCorrection(true);
        settings.setSamples(4);
        settings.setStencilBits(8);
        settings.setDepthBits(24);
        settings.setVSync(true);
        settings.setGraphicsDebug(false);
        settings.setTitle("Nostr Game Engine Demo");

        Runnable appBuilder = NGEApplication.createApp(
            settings,
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
        appBuilder.run();
    }
}
