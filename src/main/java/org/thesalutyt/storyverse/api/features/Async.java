package org.thesalutyt.storyverse.api.features;

import org.thesalutyt.storyverse.api.environment.resource.EnvResource;

public class Async implements EnvResource {
    public Async(Runnable action) {
        new Thread(() -> {action.run();}).start();
    }

    @Override
    public String getResourceId() {
        return "Async";
    }
}
