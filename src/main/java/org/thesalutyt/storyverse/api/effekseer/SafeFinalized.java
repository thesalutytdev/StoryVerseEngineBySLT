package org.thesalutyt.storyverse.api.effekseer;

import net.minecraft.client.Minecraft;

import java.io.Closeable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

abstract class SafeFinalized<T> implements Closeable {
    private static final Set<Object> KEEPER = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final AtomicReference<T> kept;
    private final Consumer<T> closer;

    protected SafeFinalized(T kept, Consumer<T> closer) {
        this.kept = new AtomicReference<>(kept);
        this.closer = closer;
        KEEPER.add(kept);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        try {
            T kept = this.kept.get();
            if (kept != null) {
                Minecraft.getInstance().tell(this::close);
            }
        } finally {
            super.finalize();
        }
    }

    @Override
    public void close() {
        T removed = this.kept.getAndSet(null);
        if (removed == null) {
            return;
        }
        try {
            closer.accept(removed);
        } finally {
            KEEPER.remove(removed);
        }
    }
}
