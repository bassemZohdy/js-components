package io.js.component.util.function;

import java.util.function.Predicate;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSPredicate<T> implements Predicate<T> {

	private final ScriptEngine engine;
	private final String functionName;

	private JSPredicate(String script, String functionName) throws ScriptException {
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
		this.engine.eval(script);
		this.functionName = functionName;
	}
	
	private JSPredicate(ScriptEngine engine, String functionName) {
		this.engine = engine;
		this.functionName = functionName;
	}

	public static <T> JSPredicate<T> of(String script, String functionName) throws ScriptException {
		JSPredicate<T> s = new JSPredicate<T>(script, functionName);
		return s;
	}
	
	public static <T> JSPredicate<T> of(ScriptEngine engine, String functionName) throws ScriptException {
		JSPredicate<T> s = new JSPredicate<T>(engine, functionName);
		return s;
	}

	@Override
	public boolean test(T t) {
		Invocable invocable = (Invocable) engine;
		try {
			Object value = invocable.invokeFunction(functionName,t);
			if (value instanceof Boolean)
				return (Boolean) value;
			else
				return Boolean.valueOf(value.toString());
		} catch (NoSuchMethodException | ScriptException e) {
			throw new JSException(e);
		}
	}
}