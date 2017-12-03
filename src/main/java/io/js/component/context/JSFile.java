package io.js.component.context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.Function;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.js.component.util.function.JSConsumer;
import io.js.component.util.function.JSFunction;
import io.js.component.util.function.JSPredicate;
import io.js.component.util.function.JSSupplier;

public class JSFile {
	private final File file;
	private final ScriptEngine engine;

	private JSFile(File file) {
		this.file = file;
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
	}
	private JSFile(File file,ScriptEngine engine) {
		this.file = file;
		this.engine = engine;
	}

	public static JSFile of(File file) throws IOException, ScriptException {
		JSFile jsFile = new JSFile(file);
		jsFile.load();
		return jsFile;
	}
	public static JSFile of(File file,ScriptEngine engine) throws IOException, ScriptException {
		JSFile jsFile = new JSFile(file,engine);
		jsFile.load();
		return jsFile;
	}

	private void load() throws IOException, ScriptException {
		String script = Files.lines(this.file.toPath()).reduce("", (s, l) -> s += l);
		this.engine.eval(script);
	}

	public <T> JSConsumer<T> getConsumer(String functionName) throws ScriptException {
		return JSConsumer.of(this.engine, functionName);
	}

	public <T> JSSupplier<T> getSupplier(String functionName, Function<Map<String, Object>, T> outFunc)
			throws ScriptException {
		return JSSupplier.of(this.engine, functionName, outFunc);
	}

	public <I, O> JSFunction<I, O> getFunction(String functionName, Function<Map<String, Object>, O> outFunc)
			throws ScriptException {
		return JSFunction.of(this.engine, functionName, outFunc);
	}

	public <T> JSPredicate<T> getPredicate(String functionName) throws ScriptException {
		return JSPredicate.of(this.engine, functionName);
	}

}
