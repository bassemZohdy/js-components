package io.js.component.util.function;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.function.Predicate;

public class JSPredicate<T> implements Predicate<T> {
	private final Value function;

	public JSPredicate(String script) {
		Context context = Context.newBuilder("js").build();
		script = script.replaceAll("'", "\\\\'").replaceAll("[\r\n]", "\\\\n");
		Value compiledFunction = script.contains("return ") ?
				context.eval("js", "new Function('input', '" + script + "')")
				:
				context.eval("js", "new Function('input', 'return " + script + "')");
		this.function = compiledFunction;
	}

	public static <T> JSPredicate<T> of(String script) {
		return new JSPredicate<>(script);
	}

	@Override
	public boolean test(T t) {
		Value result = function.execute(t);
		return result.asBoolean();
	}
}