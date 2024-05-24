package org.thesalutyt.storyverse.api.environment.js.event;

import org.mozilla.javascript.*;
import org.thesalutyt.storyverse.SVEngine;
import org.thesalutyt.storyverse.api.environment.events.EventManager;
import org.thesalutyt.storyverse.api.environment.js.interpreter.EventLoop;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;
import org.thesalutyt.storyverse.api.features.Script;
import org.thesalutyt.storyverse.api.special.FadeScreen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManagerJS extends ScriptableObject implements EnvResource, JSResource {
    private final EventLoop loop;
    private final HashMap<String, ArrayList<BaseFunction>> handlers = new HashMap<>();
    public EventManagerJS(EventLoop eventLoop) {
        this.loop = eventLoop;
    }
    public static void putIntoScope (Scriptable scope, EventLoop loop) {
        EventManagerJS em = new EventManagerJS(loop);

        ArrayList<Method> methodsToAdd = new ArrayList<>();

        try {
            Method addEventListener = EventManagerJS.class.getMethod("addEventListener", String.class, BaseFunction.class);
            methodsToAdd.add(addEventListener);
            Method removeEventListener = EventManagerJS.class.getMethod("removeEventListener", String.class, BaseFunction.class);
            methodsToAdd.add(removeEventListener);
            Method runEvent = EventManagerJS.class.getMethod("runEvent", String.class, NativeArray.class);
            methodsToAdd.add(runEvent);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, em);
            em.put(m.getName(), em, methodInstance);
        }

        scope.put("event", scope, em);
    }

    public void addEventListener (String eventName, BaseFunction handler) {
        /*
         *   Здесь добавляется хендлер. Используй это как пример вообще любого сохранения
         *   JS функции в java объекте. И кажется я понял как их запускать из джавы
         *   Обрати внимание на то, что я запускаю все события в loop. Это важно.
         *   Если ты запустишь их не из loop, то получишь огромное количество невнятных багов
         *   Самый очевидный - это то что все может модифицироваться в разных потоках.
         * */
        loop.runImmediate(() -> {
            if (!handlers.containsKey(eventName)) {
                handlers.put(eventName, new ArrayList<>());
            }

            ArrayList<BaseFunction> arr = handlers.get(eventName);
            arr.add(handler);
        });
    }

    public void removeEventListener (String eventName, BaseFunction handler) {
        // это чтобы убрать конкретный хендлер
        loop.runImmediate(() -> {
            if (handlers.containsKey(eventName)) {
                if (handler == null) {
                    // если не сказали какую - удаляем все
                    handlers.remove(eventName);
                } else {
                    ArrayList<BaseFunction> arr = handlers.get(eventName);
                    for (int i=0; i<arr.size(); i++) {
                        BaseFunction fn = arr.get(i);
                        if (fn.equals(handler)) {
                            arr.remove(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    public void runEvent (String eventName, NativeArray eventParameters) {
        // этот метод запускает событие
        // ох, надеюсь это сработает
        loop.runImmediate(() -> {
            if (handlers.containsKey(eventName)) {
                ArrayList<BaseFunction> arr = handlers.get(eventName);
                Context ctx = Context.getCurrentContext();
                for (int i=0; i<arr.size(); i++) {
                    arr.get(i).call(ctx, this, this, eventParameters.toArray());
                }
            }
        });
    }

    @Override
    public String getResourceId() {
        return "EventManagerJS";
    }

    @Override
    public String getClassName() {
        return "EventManagerJS";
    }
}
