package io.js.component.util.function;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSFunction<I, O> implements Function<I, Optional<O>> {
	private final ScriptEngine engine;
	private final String script;
	private final String functionName;
	private final Function<Map<String, Object>, O> outFunc;

	private JSFunction(String script, String functionName, Function<Map<String, Object>, O> outFunc) {
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
		this.script = script;
		this.functionName = functionName;
		this.outFunc = outFunc;
	}

	public static <I, O> JSFunction<I, O> of(String script, String functionName, Function<Map<String, Object>, O> outFunc)
			throws ScriptException {
		JSFunction<I, O> func = new JSFunction<>(script, functionName, outFunc);
		func.engine.eval(func.script);
		return func;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<O> apply(I in) {
		Invocable invocable = (Invocable) engine;
		try {
			Object value = invocable.invokeFunction(functionName, in);
			if (value instanceof Map)
				return Optional.ofNullable(outFunc.apply((Map<String, Object>) value));
			else if (value instanceof Number || value instanceof Boolean || value instanceof Character|| value instanceof Void)
				return Optional.ofNullable((O) value);
			else
				return Optional.empty();
		} catch (NoSuchMethodException | ScriptException e) {
			throw new JSException(e);
		}
	}

}
