package com.colak;

import org.apache.commons.io.input.UnsynchronizedBufferedInputStream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.io.BufferedInputStream;
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
public class BufferedInputStreamJmh {

    @Param({"100", "1000", "10000"})
    private int readBufferSize;

    private BufferedInputStream bufferedInputStream;

    private BufferingInputStream bufferingInputStream;

    private UnsynchronizedBufferedInputStream unsynchronizedBufferedInputStream;


    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Setup(Level.Invocation)
    public void setup() throws IOException {
        final int streamInternalBufferSize = 1 << 16;
        bufferedInputStream = new BufferedInputStream(new FileInputStream("myfile.txt"), streamInternalBufferSize);
        bufferingInputStream = new BufferingInputStream(new FileInputStream("myfile.txt"), streamInternalBufferSize);
        unsynchronizedBufferedInputStream = new UnsynchronizedBufferedInputStream.Builder()
                .setInputStream(new FileInputStream("myfile.txt"))
                .setBufferSize(streamInternalBufferSize)
                .get();
    }

    @TearDown(Level.Invocation)
    public void teardown() throws IOException {
        bufferedInputStream.close();
        bufferingInputStream.close();
        unsynchronizedBufferedInputStream.close();
    }

    private int countNewLinesManually(InputStream inputStream, int customBytesToBuffer) throws IOException {
        byte[] buff = new byte[customBytesToBuffer];
        int count = 1;
        int bytesRead;
        while ((bytesRead = inputStream.read(buff)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                if (buff[i] == '\n') {
                    count++;
                }
            }
        }
        return count;
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int useBufferingInputStream() throws IOException {
        return countNewLinesManually(bufferingInputStream, readBufferSize);
        //assertThat(numberOfLines).isEqualTo(30_000);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int useBufferedInputStream() throws IOException {
        return countNewLinesManually(bufferedInputStream, readBufferSize);
        //assertThat(numberOfLines).isEqualTo(30_000);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int useUnsynchronizedBufferedInputStream() throws IOException {
        return countNewLinesManually(unsynchronizedBufferedInputStream, readBufferSize);
        //assertThat(numberOfLines).isEqualTo(30_000);
    }
}
