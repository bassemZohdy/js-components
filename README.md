# JavaScript Component
This library is giving you functionality of having basic `java.util.function` functional components like `Consumer<T>`, `Supplier<T>`, and `Function<I,O>`, to be implemented using java script and to run on java (Nashorn).
	 
## usage
* JSConsumer<T>
This class is represent implementation of Java `Consumer<T>` interface with java script code as:

	JSConsumer<String> jsConsumer = JSConsumer.of("function printX(x){print(x);}", "printX");
	jsConsumer.accept("message");
  
* JSSupplier<T>
This class is represent implementation of Java `Supplier<T>` interface with java script code as:

	JSSupplier<Integer> jsSupplier = JSSupplier.of("function getNumber(){return {" + KEY + ":" + NUMBER + "};}",
				"getNumber", m -> (Integer) m.get(KEY));
	Optional<Integer> opt = jsSupplier.get();

* JSFunction<I,O>
This class is represent implementation of Java `Supplier<T>` interface with java script code as:

	JSFunction<String, Integer> jsFunction = JSFunction
				.of("function parse(s){return {value:parseInt(s)};}", "parse", m -> (Integer) m.get("value"));
	Optional<Integer> value = jsFunction.apply("42");
    
> for more examples check test cases.
## issues
* Junit error
While there is no need for System.out to be initialized in JS Supplier and Function if you remove or comment it will get `java.lang.NullPointerException` exception