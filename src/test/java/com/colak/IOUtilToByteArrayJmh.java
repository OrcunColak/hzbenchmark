package com.colak;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


// Only one forked process
@Fork(value = 1)
// Only one thread
@Threads(1)
// use the same instance of this class for the whole benchmark,
// so it is OK to have some member variables
@State(Scope.Benchmark)
// calculate the average time of one call
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
// 5 iterations to warm-up, that may last 20 milliseconds each
@Warmup(iterations = 5, time = 20, timeUnit = TimeUnit.MILLISECONDS)
// 20 iterations to measure, that may last 200 milliseconds each
@Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class IOUtilToByteArrayJmh {

    private InputStream bufferedInputStream;

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(IOUtilToByteArrayJmh.class.getSimpleName())
                .build();

        new Runner(options).run();
    }

    @Setup(Level.Invocation)
    public void setup() throws IOException {
        bufferedInputStream = new FileInputStream("myfile.txt");
    }

    @TearDown(Level.Invocation)
    public void teardown() throws IOException {
        bufferedInputStream.close();
    }


    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public byte[] useIOUtil() throws IOException {
        return IOUtil.toByteArray(bufferedInputStream);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public byte[] useReadAllBytes() throws IOException {
        return bufferedInputStream.readAllBytes();
    }

}
