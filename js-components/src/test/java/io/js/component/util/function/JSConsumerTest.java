/**
 * 
 */
package io.js.component.util.function;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSConsumerTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@Test
	public void test() throws ScriptException {
		String message = "Test: \t new Test";
		JSConsumer<String> jsConsumer = JSConsumer.of("function printX(x){print(x);}", "printX");
		jsConsumer.accept(message);
		String actual = outContent.toString();
		assertEquals(message + System.getProperty("line.separator"), actual);
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
	}
}
