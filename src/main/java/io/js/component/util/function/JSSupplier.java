package io.js.component.util.function;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSSupplier<T> implements Supplier<Optional<T>> {

	private final ScriptEngine engine;
	private final String functionName;
	private final Function<Map<String, Object>, T> outFunc;

	private JSSupplier(String script, String functionName, Function<Map<String, Object>, T> outFunc) throws ScriptException {
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
		this.engine.eval(script);
		this.functionName = functionName;
		this.outFunc = outFunc;
	}
	
	private JSSupplier(ScriptEngine engine, String functionName, Function<Map<String, Object>, T> outFunc) throws ScriptException {
		this.engine = engine;
		this.functionName = functionName;
		this.outFunc = outFunc;
	}

	public static <T> JSSupplier<T> of(String script, String functionName, Function<Map<String, Object>, T> outFunc)
			throws ScriptException {
		JSSupplier<T> s = new JSSupplier<T>(script, functionName, outFunc);
		return s;
	}
	
	public static <T> JSSupplier<T> of(ScriptEngine engine, String functionName, Function<Map<String, Object>, T> outFunc)
			throws ScriptException {
		JSSupplier<T> s = new JSSupplier<T>(engine, functionName, outFunc);
		return s;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<T> get() {
		Invocable invocable = (Invocable) engine;
		try {
			Object value = invocable.invokeFunction(functionName);
			if (value instanceof Map)
				return Optional.ofNullable(outFunc.apply((Map<String, Object>) value));
			else if (value instanceof Number || value instanceof Boolean || value instanceof Character|| value instanceof Void)
				return Optional.ofNullable((T) value);
			else
				return Optional.empty();
		} catch (NoSuchMethodException | ScriptException e) {
			throw new JSException(e);
		}
	}

}
