package io.js.component.util.function;

import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Assertions;

class JSPredicateTest {

    @Test
    public void testSimpleExpression() {
        Predicate<Integer> predicate = JSPredicate.of("input > 10");
        Assertions.assertTrue(predicate.test(15));
        Assertions.assertFalse(predicate.test(5));
    }

    @Test
    public void testExpressionWithReturn() {
        Predicate<Integer> predicate = JSPredicate.of("if (input >= 30) {\n  return true;\n} else {\n  return false;\n}");
        Assertions.assertTrue(predicate.test(35));
        Assertions.assertFalse(predicate.test(25));
    }

    @Test
    public void testExpressionWithSingleQuotes() {
        Predicate<String> predicate = JSPredicate.of("input === 'hello'");
        Assertions.assertTrue(predicate.test("hello"));
        Assertions.assertFalse(predicate.test("world"));
    }

    @Test
    public void testExpressionWithDoubleQuotes() {
        Predicate<String> predicate = JSPredicate.of("input === \"hello\"");
        Assertions.assertTrue(predicate.test("hello"));
        Assertions.assertFalse(predicate.test("world"));
    }

    @Test
    public void testExpressionWithNewline() {
        Predicate<Integer> predicate = JSPredicate.of("input > 10\n&& input < 20");
        Assertions.assertTrue(predicate.test(15));
        Assertions.assertFalse(predicate.test(5));
    }

    @Test
    public void testExpressionWithCarriageReturn() {
        Predicate<Integer> predicate = JSPredicate.of("input > 10\r&& input < 20");
        Assertions.assertTrue(predicate.test(15));
        Assertions.assertFalse(predicate.test(5));
    }

    @Test
    public void testExpressionWithMixedNewlineAndCarriageReturn() {
        Predicate<Integer> predicate = JSPredicate.of("input > 10\r\n&& input < 20");
        Assertions.assertTrue(predicate.test(15));
        Assertions.assertFalse(predicate.test(5));
    }

    @Test
    public void testExpressionWithInvalidSyntax() {
        Assertions.assertThrows(PolyglotException.class, () -> {
            Predicate<Integer> predicate = JSPredicate.of("input >");
        });
    }

    @Test
    public void testExpressionWithNonBooleanResult() {
        Assertions.assertThrows(ClassCastException.class, () -> {
            Predicate<Integer> predicate = JSPredicate.of("input + 10");
            predicate.test(5);
        });
    }

    @Test
    public void testExpressionWithInjectedCode() {
        Assertions.assertThrows(ClassCastException.class, () -> {
            Predicate<Integer> predicate = JSPredicate.of("input + 10; java.lang.System.exit(0);");
            predicate.test(5);
        });
    }

    @Test
    public void testExpressionWithInfiniteLoop() {
        Assertions.assertThrows(PolyglotException.class, () -> {
            Predicate<Integer> predicate = JSPredicate.of("while (true) {}");
            predicate.test(5);
        });
    }

    @Test
    public void testExpressionWithLargeMemoryUsage() {
        Assertions.assertThrows(PolyglotException.class, () -> {
            String script = "var arr = []; while (true) { arr.push('a'); }";
            Predicate<String> predicate = JSPredicate.of(script);
            predicate.test("test");
        });
    }
}