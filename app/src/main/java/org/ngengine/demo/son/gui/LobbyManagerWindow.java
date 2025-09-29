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
package org.ngengine.demo.son.gui;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.DynamicInsetsComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.GuiComponent;
import com.simsilica.lemur.event.MouseListener;
import com.simsilica.lemur.style.StyleAttribute;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.ngengine.gui.components.NIconButton;
import org.ngengine.gui.components.NTextInput;
import org.ngengine.gui.components.NVSpacer;
import org.ngengine.gui.components.containers.NMultiPageList;
import org.ngengine.gui.components.containers.NMultiPageList.LoadedItems;
import org.ngengine.gui.win.NWindow;
import org.ngengine.network.Lobby;
import org.ngengine.network.LobbyCursor;
import org.ngengine.network.LobbyManager;
import org.ngengine.network.P2PChannel;
import org.ngengine.platform.AsyncTask;
import org.ngengine.platform.NGEPlatform;
import org.ngengine.platform.NGEUtils;

public class LobbyManagerWindow extends NWindow<LobbyManagerWindowArg> {

    static final Logger logger = Logger.getLogger(LobbyManagerWindow.class.getName());

    private Lobby selectedLobby;
    private Container selectedLobbyEntry;
    private GuiComponent selectedLobbyEntryBackground;
    private GuiComponent selectionBackground;
    private LobbyCursor cursor;

    @StyleAttribute(value = "selectionBackground")
    public void setSelectionBackground(GuiComponent selectionBackground) {
        this.selectionBackground = selectionBackground;
    }

    @Override
    protected void compose(Vector3f size, LobbyManagerWindowArg arg) {
        LobbyManager mng = arg.mng;
        Consumer<P2PChannel> onJoin = arg.onJoin;

        setFitContent(false);
        setTitle("Find match");

        Container content = getContent();

        NMultiPageList<Lobby> mlist = new NMultiPageList<>();
        mlist.setRenderer(lobby -> {
            Container lobbyEntry = new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Last, FillMode.None));

            if(lobby==null) {
                Label lbl = new Label("-");
                lbl.setTextHAlignment(HAlignment.Center);
                lbl.setTextVAlignment(VAlignment.Center);
                lbl.setCullHint(CullHint.Always);
                lobbyEntry.addChild(lbl);
                return lobbyEntry;
            }

            String name = lobby.getDataOrDefault("name", "unnamed");
            int nPeers = NGEUtils.safeInt(lobby.getDataOrDefault("numPeers", "0"));


            if (lobby.isLocked()) {
                NIconButton lockIcon = new NIconButton("icons/outline/lock.svg");
                lobbyEntry.addChild(lockIcon);
            }

            Label lobbyName = new Label(name);
            lobbyName.setTextHAlignment(HAlignment.Left);
            lobbyName.setTextVAlignment(VAlignment.Center);
            lobbyEntry.addChild(lobbyName);

            lobbyEntry.addMouseListener(
                new MouseListener() {
                    @Override
                    public void mouseButtonEvent(MouseButtonEvent event, Spatial target, Spatial capture) {
                        if (event.isPressed()) {
                            if (event.getButtonIndex() == 0) {
                                if (selectedLobbyEntry != null) {
                                    selectedLobbyEntry.setBackground(selectedLobbyEntryBackground);
                                    selectedLobbyEntry = null;
                                    selectedLobbyEntryBackground = null;
                                }

                                selectedLobby = lobby;
                                logger.info("Selected lobby: " + lobby.getId());
                                selectedLobbyEntry = lobbyEntry;
                                selectedLobbyEntryBackground = lobbyEntry.getBackground();
                                if (selectionBackground != null) {
                                    lobbyEntry.setBackground(selectionBackground.clone());
                                }
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture) {}

                    @Override
                    public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {}

                    @Override
                    public void mouseMoved(MouseMotionEvent event, Spatial target, Spatial capture) {}
                }
            );

            return lobbyEntry;
        });

         

        content.addChild(mlist, BorderLayout.Position.Center);

        Container searchBar = new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.First, FillMode.None));
        content.addChild(searchBar, BorderLayout.Position.North);
        NTextInput search = new NTextInput();
        {
            search.setCopyAction(null);
            search.setPasteAction(null);
            search.setTextVAlignment(VAlignment.Center);
            searchBar.addChild(search);
        }



        Container pageNavigator = new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Even, FillMode.None));
        pageNavigator.setInsetsComponent(new DynamicInsetsComponent(1f, 0, 0, 0));

        {
            NIconButton prev = new NIconButton("icons/outline/chevron-left.svg");
            prev.addClickCommands(src -> {
                mlist.previousPage();
            });
            prev.setEnabled(false);
            pageNavigator.addChild(prev);

            Label pageLabel = new Label("0/0");
            pageNavigator.addChild(pageLabel);

            NIconButton next = new NIconButton("icons/outline/chevron-right.svg");
            next.addClickCommands(src -> {
                mlist.nextPage();
            });
            pageNavigator.addChild(next);
            content.addChild(pageNavigator, BorderLayout.Position.South);

            mlist.setPageChangeListener((p) -> {
                if (!mlist.hasPreviousPage()) {
                    prev.setEnabled(false);
                } else {
                    prev.setEnabled(true);
                }

                if (!mlist.hasNextPage()) {
                    next.setEnabled(false);
                } else {
                    next.setEnabled(true);
                }
                pageLabel.setText(""+(p + 1));
            });

            mlist.setPageLoadingHandler((num,res)->{
                String text = search.getText();
                int n = Math.min(num,2);
                // TODO tags
                mng.listLobbies(
                        text,
                        n,
                        null,
                        cursor,
                        (lobbies, err) -> {
                            if(err!=null){
                                getManager().showToast(err);
                                res.accept(null);
                                return;
                            }
                            this.cursor = lobbies;
                            LoadedItems<Lobby> loaded = new LoadedItems<>(lobbies.get(), lobbies.hasMore());
                            res.accept(loaded);
                        });
            
            });
        }

  


        {
           
            search.setTextChangeAction(src -> {
                this.cursor = null;
                mlist.clear();
            });

        }

        NIconButton searchBtn = new NIconButton("icons/outline/search.svg");
        {
            searchBar.addChild(searchBtn);
        }

        Container buttonPanel = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.None));
        content.addChild(buttonPanel, BorderLayout.Position.East);

        {
            Button newm = new Button("New match");
            newm.setTextHAlignment(HAlignment.Center);
            buttonPanel.addChild(newm);
            newm.addClickCommands(src -> {
                getManager().showWindow(NewMatchWindow.class, mng);
            });
        }

        {
            Button join = new Button("Join match");
            join.setTextHAlignment(HAlignment.Center);
            buttonPanel.addChild(join);
            join.addClickCommands(src -> {
                try {
                    if (selectedLobby != null) {
                        if (!selectedLobby.isLocked()) {
                            P2PChannel chan = mng.connectToLobby(selectedLobby, "");
                            onJoin.accept(chan);
                        } else {
                            getManager().showWindow(JoinLockedLobby.class, new JoinLockedMatchArg(mng, selectedLobby, onJoin));
                        }
                    }
                } catch (Exception e) {
                    getManager().showToast(e);
                }
            });
        }

        {
            Button refresh = new Button("Refresh list");
            refresh.setTextHAlignment(HAlignment.Center);
            refresh.addClickCommands(src -> {
                this.cursor = null;
                mlist.clear();
            });
            buttonPanel.addChild(refresh);
        }

        buttonPanel.addChild(new NVSpacer());
        buttonPanel.addChild(pageNavigator);

         
    }
}
