package org.thesalutyt.storyverse.fs_environment.action;

import org.thesalutyt.storyverse.annotations.Documentate;

public class ActionPacketData {
    @Documentate(
            desc = "Is PlayAction keybinding pressed."
    )
    public boolean playKeyPressed = false;
    @Documentate(
            desc = "Returns sent message (if sent)."
    )
    public String messageSent = "";
//    @Documentate(
//            desc = "Current scene."
//    )
//    public ScriptScene scene;

    public ActionPacketData() {
    }
}
