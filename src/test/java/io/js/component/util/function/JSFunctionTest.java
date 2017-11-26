package io.js.component.util.function;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSFunctionTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private static final String NUMBER_STRING = "42";
	private static final Integer NUMBER = 42;
	private static final String KEY = "value";

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@Test
	public void testPrimitive() throws ScriptException {
		JSFunction<String, Integer> jsFunction = JSFunction.of("function parse(s){return parseInt(s);}", "parse", null);
		Optional<Integer> value = jsFunction.apply(NUMBER_STRING);
		assertEquals(Optional.of(NUMBER), value);
	}

	@Test
	public void testObject() throws ScriptException {
		JSFunction<String, Integer> jsFunction = JSFunction
				.of("function parse(s){return {" + KEY + ":" + "parseInt(s)};}", "parse", m -> (Integer) m.get(KEY));
		Optional<Integer> value = jsFunction.apply(NUMBER_STRING);
		assertEquals(Optional.of(NUMBER), value);
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
	}
}
