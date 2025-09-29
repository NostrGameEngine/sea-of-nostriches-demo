package org.ngengine.demo.son.gui;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.ngengine.gui.components.NTextInput;
import org.ngengine.gui.components.NVSpacer;
import org.ngengine.gui.win.NWindow;
import org.ngengine.network.LobbyManager;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;

public class NewMatchWindow extends NWindow<LobbyManager> {

    @Override
    protected void compose(Vector3f size, LobbyManager mng) {
        setTitle("New match");

        Container content = getContent().addCol();

        NTextInput lobbyName = new NTextInput();
        lobbyName.setLabel("Name");
        lobbyName.setCopyAction(null);
        lobbyName.setPasteAction(null);
        content.addChild(lobbyName);

        NTextInput password = new NTextInput();
        password.setLabel("Password");
        password.setIsSecretInput(true);
        content.addChild(password);

        content.addChild(new NVSpacer());
        Button create = new Button("Create");
        create.setTextHAlignment(HAlignment.Center);
        content.addChild(create);
        create.addClickCommands(src -> {
            LobbyManagerWindow.logger.info("Creating lobby: " + lobbyName.getText());
            Map<String, String> data = new HashMap<>();
            data.put("name", lobbyName.getText());
            Duration expiration = Duration.ofDays(1);
            mng.createLobby(
                password.getText(),
                data,
                expiration,
                (r, err) -> {
                    if (err != null) {
                        getManager().showToast(err);
                        return;
                    }
                    LobbyManagerWindow.logger.info("Lobby created: " + r.getId());
                    close();
                }
            );
        });
    }
}