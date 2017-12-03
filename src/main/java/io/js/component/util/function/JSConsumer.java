package io.js.component.util.function;

import java.util.function.Consumer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSConsumer<T> implements Consumer<T> {
	private final ScriptEngine engine;
	private final String functionName;

	private JSConsumer(String script, String functionName) throws ScriptException {
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
		this.engine.eval(script);
		this.functionName = functionName;
	}
	
	private JSConsumer(ScriptEngine engine, String functionName) {
		this.engine = engine;
		this.functionName = functionName;
	}

	public static <T> JSConsumer<T> of(String script, String functionName) throws ScriptException {
		JSConsumer<T> c = new JSConsumer<T>(script, functionName);
		return c;
	}
	public static <T> JSConsumer<T> of(ScriptEngine engine, String functionName) throws ScriptException {
		JSConsumer<T> c = new JSConsumer<T>(engine, functionName);
		return c;
	}

	@Override
	public void accept(T t) {
		Invocable invocable = (Invocable) engine;
		try {
			invocable.invokeFunction(functionName, t);
		} catch (NoSuchMethodException | ScriptException e) {
			throw new JSException(e);
		}
	}

}
