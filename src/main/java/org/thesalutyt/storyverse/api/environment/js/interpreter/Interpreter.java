//package org.thesalutyt.storyverse.api.environment.js.interpreter;
//
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.Scriptable;
//
//
//public class Interpreter {
//    private final EventLoop loop;
//    private Scriptable scope;
//    public Interpreter (String rootDir) {
//        loop = new EventLoop();
//
//        loop.runImmediate(() -> {
//            Context ctx = Context.enter();
//            scope = ctx.initStandardObjects();
//            ExternalFunctions.putIntoScope(scope, rootDir);
//            Asynchronous.putIntoScope(scope, loop);
//        });
//    }
//
//    public void close () {
//        loop.close();
//    }
//
//    public void executeString (String str) {
//        loop.runImmediate(() -> Context.getCurrentContext().evaluateString(
//                scope,
//                str,
//                "<cmd>",
//                1,
//                null
//        ));
//    }
//}