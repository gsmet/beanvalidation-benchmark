package org.apache.bval.bench;

import java.util.Objects;
import java.util.stream.Stream;

import org.apache.bval.bench.benchmarks.ParsingBeansSpeedBenchmark;
import org.apache.bval.bench.benchmarks.RawValidationSpeedBenchmark;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.CommandLineOptionException;
import org.openjdk.jmh.runner.options.CommandLineOptions;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Marko Bekhta
 * @author Guillaume Smet
 */
public final class BenchmarkRunner {

	private static final Stream<? extends Class<?>> DEFAULT_TEST_CLASSES = Stream.of(
			ParsingBeansSpeedBenchmark.class.getName(),
			RawValidationSpeedBenchmark.class.getName()
	).map( BenchmarkRunner::classForName ).filter( Objects::nonNull );

	private BenchmarkRunner() {
	}

	public static void main(String[] args) throws RunnerException, CommandLineOptionException {
		Options commandLineOptions = new CommandLineOptions( args );
		ChainedOptionsBuilder builder = new OptionsBuilder().parent( commandLineOptions );

		if ( !commandLineOptions.getResult().hasValue() ) {
			builder.result( "target/jmh-results.json" );
		}
		if ( !commandLineOptions.getResultFormat().hasValue() ) {
			builder.resultFormat( ResultFormatType.JSON );
		}
		if ( commandLineOptions.getIncludes().isEmpty() ) {
			DEFAULT_TEST_CLASSES.forEach( testClass -> builder.include( testClass.getName() ) );
		}

		Options opt = builder.build();
		new Runner( opt ).run();
	}

	private static Class<?> classForName(String qualifiedName) {
		try {
			return Class.forName( qualifiedName );
		}
		catch (ClassNotFoundException e) {
			// silently ignore the error
		}
		return null;
	}

}
