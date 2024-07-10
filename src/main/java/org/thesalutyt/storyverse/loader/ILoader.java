package org.thesalutyt.storyverse.loader;

import java.io.IOException;

public interface ILoader {
    public void init() throws IOException;
    public LoaderType getType();
    public void load() throws IOException;
}
