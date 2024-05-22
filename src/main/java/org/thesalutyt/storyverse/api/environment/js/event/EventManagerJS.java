package org.thesalutyt.storyverse.api.environment.js.event;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.special.FadeScreen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManagerJS extends ScriptableObject implements EnvResource, JSResource {

    @Override
    public String getResourceId() {
        return "EventManagerJS";
    }

    @Override
    public String getClassName() {
        return "EventManagerJS";
    }
}
