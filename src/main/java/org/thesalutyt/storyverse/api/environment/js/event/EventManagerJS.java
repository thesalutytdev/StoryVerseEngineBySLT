package org.thesalutyt.storyverse.api.environment.js.event;

import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

public class EventManagerJS implements EnvResource, JSResource {
    @Override
    public String getResourceId() {
        return "EventManagerJS";
    }

    @Override
    public String getClassName() {
        return "EventManagerJS";
    }
}
