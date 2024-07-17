package org.thesalutyt.storyverse.api.quests.goal;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.thesalutyt.storyverse.api.environment.js.minecraft.item.JSItem;
import org.thesalutyt.storyverse.api.environment.resource.EnvResource;
import org.thesalutyt.storyverse.api.environment.resource.JSResource;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class IGoalBuilder extends ScriptableObject implements EnvResource, JSResource {

    public static ItemGoal itemGoal(String jsStack, String quest) {
        return new ItemGoal().itemGoal(JSItem.getStack(jsStack), quest);
    }

    public static NPCItem npcItem(String jsStack, String quest) {
        return new NPCItem(JSItem.getStack(jsStack), quest);
    }

    public static InteractGoal interactGoal(String questId) {
        return new InteractGoal(questId);
    }

    public static ArrayList<Method> methodsToAdd = new ArrayList<>();

    public static void putIntoScope(Scriptable scope) {
        IGoalBuilder ef = new IGoalBuilder();
        ef.setParentScope(scope);

        try {
            Method itemGoal = IGoalBuilder.class.getMethod("itemGoal", String.class, String.class);
            methodsToAdd.add(itemGoal);
            Method interactGoal = IGoalBuilder.class.getMethod("interactGoal", String.class);
            methodsToAdd.add(interactGoal);
            Method npcItem = IGoalBuilder.class.getMethod("npcItem", String.class, String.class);
            methodsToAdd.add(npcItem);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Method m : methodsToAdd) {
            FunctionObject methodInstance = new FunctionObject(m.getName(),
                    m, ef);
            ef.put(m.getName(), ef, methodInstance);
        }
        scope.put("goal", scope, ef);
    }

    @Override
    public String getClassName() {
        return "IGoalBuilder";
    }

    @Override
    public String getResourceId() {
        return "IGoalBuilder";
    }
}
