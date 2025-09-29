package org.ngengine.demo.son.gui;

import java.util.function.Consumer;

import org.ngengine.network.Lobby;
import org.ngengine.network.LobbyManager;
import org.ngengine.network.P2PChannel;

class JoinLockedMatchArg {

    public LobbyManager mng;
    public Lobby lobby;
    public Consumer<P2PChannel> onJoin;

    JoinLockedMatchArg(LobbyManager mng, Lobby lobby, Consumer<P2PChannel> onJoin) {
        this.mng = mng;
        this.lobby = lobby;
        this.onJoin = onJoin;
    }
}