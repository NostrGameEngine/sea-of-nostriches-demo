package org.ngengine.demo.son.gui;

import org.ngengine.gui.components.NTextInput;
import org.ngengine.gui.components.NVSpacer;
import org.ngengine.gui.win.NWindow;
import org.ngengine.network.Lobby;
import org.ngengine.network.LobbyManager;
import org.ngengine.network.P2PChannel;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;

public class JoinLockedLobby extends NWindow<JoinLockedMatchArg> {

    @Override
    protected void compose(Vector3f size, JoinLockedMatchArg arg) {
        LobbyManager appState = arg.mng;
        Lobby lobby = arg.lobby;

        setTitle("Join locked match");

        Container content = getContent().addCol();

        NTextInput password = new NTextInput();
        password.setLabel("Password");
        password.setIsSecretInput(true);
        content.addChild(password);

        content.addChild(new NVSpacer());
        Button create = new Button("Join");
        create.setTextHAlignment(HAlignment.Center);
        content.addChild(create);
        create.addClickCommands(src -> {
            try {
                P2PChannel chan = appState.connectToLobby(lobby, password.getText());
                arg.onJoin.accept(chan);
                close();
            } catch (Exception e) {
                getManager().showToast(e);
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setTextHAlignment(HAlignment.Center);
        content.addChild(cancel);
        cancel.addClickCommands(src -> {
            close();
        });
    }
}