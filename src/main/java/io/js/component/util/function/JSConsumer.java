package io.js.component.util.function;

import java.util.function.Consumer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSConsumer<T> implements Consumer<T> {
	private final ScriptEngine engine;
	private final String script;
	private final String functionName;

	private JSConsumer(String script, String functionName) {
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
		this.script = script;
		this.functionName = functionName;
	}

	public static <T> JSConsumer<T> of(String script, String functionName) throws ScriptException {
		JSConsumer<T> c = new JSConsumer<T>(script, functionName);
		c.engine.eval(c.script);
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
