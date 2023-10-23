package io.js.component.util.function;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import javax.script.ScriptException;
import java.util.function.BiPredicate;

public class JSBiPredicate<T, U> implements BiPredicate<T, U> {
    private final Value function;

    private JSBiPredicate(String script) {
        Context context = Context.newBuilder("js").build();
        script = script.replaceAll("'", "\\\\'").replaceAll("[\r\n]", "\\\\n");
        Value compiledFunction = script.contains("return ") ?
                context.eval("js", "new Function('input1', 'input2', '" + script + "')")
                :
                context.eval("js", "new Function('input1', 'input2', 'return " + script + "')");
        this.function = compiledFunction;
    }

    public static <T, U> JSBiPredicate<T, U> of(String script) {
        return new JSBiPredicate<>(script);
    }

    @Override
    public boolean test(T t, U u) {
        Value result = function.execute(t, u);
        return result.asBoolean();
    }
}
