package io.js.component.util.function;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSSupplierTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private static final Integer NUMBER = 42;
	private static final String KEY = "value";

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}


	@Test
	public void testPrimitive() throws ScriptException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
		JSSupplier<Integer> jsSupplier = JSSupplier.of("function getNumber(){return " + NUMBER + ";}", "getNumber",
				null);
		assertEquals(NUMBER, jsSupplier.get().get());
	}

	@Test
	public void testObject() throws ScriptException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
		JSSupplier<Integer> jsSupplier = JSSupplier.of("function getNumber(){return {" + KEY + ":" + NUMBER + "};}",
				"getNumber", m -> (Integer) m.get(KEY));
		assertEquals(NUMBER, jsSupplier.get().get());
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
	}
}
