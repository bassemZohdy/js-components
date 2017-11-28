package io.js.component.util.function;

import java.util.function.Predicate;

public class JSPredicate<T> implements Predicate<T> {

  rivate final String scri pt;
  private final String functionName;
  
  private JSPredicate(String script, String functionName) {
		this.engine = new ScriptEngineManager().getEngineByName("nashorn");
		this.script = script;
		this.functionName = functionName;
  }
  
  public static <T> JSPredicate<T> of(String script, String functionName)
  throws ScriptException {
    JSPredicate<T> s = new JSPredicate<T>(script, functionName);
    s.engine.eval(s.script);
    return s;
  }

  @Override
  public boolean test(T t){
		Invocable invocable = (Invocable) engine;
		try {
			Object value = invocable.invokeFunction(functionName);
			if (value instanceof Boolean)
				return (Boolean)value;
			else
				return Boolean.valueOf(value.toString());
		} catch (NoSuchMethodException | ScriptException e) {
			throw new JSException(e);
		}
  }
}