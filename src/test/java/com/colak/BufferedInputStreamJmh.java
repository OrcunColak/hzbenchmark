package com.colak;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// for /l %i in (1,1,3000) do echo "This is line %i" >> myfile.txt

// Only one forked proce
@Fork(value = 1)
// Only one thread
@Threads(1)
// use the same instance of this class for the whole benchmark,
// so it is OK to have some fix member variables
@State(Scope.Benchmark)
// calculate the average time of one call
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
// 5 iterations to warm-up, that may last 10 seconds each
@Warmup(iterations = 5, time = 20, timeUnit = TimeUnit.MILLISECONDS)
// 20 iterations to measure, that may last 200 milliseconds each
@Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class BufferedInputStreamJmh {

    @Param({"100", "1000", "10000"})
    private int bufferSize;

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
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
    public void useBufferedInputStream() throws IOException {
        String filePath = "myfile.txt";
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             BufferedInputStream inputStream = new BufferedInputStream(fileInputStream, 65536)) {
            int numberOfLines = countNewLinesManually(inputStream, bufferSize);
            assertThat(numberOfLines).isEqualTo(30_000);
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void useBufferingInputStream() throws IOException {
        String filePath = "myfile.txt";
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             BufferingInputStream inputStream = new BufferingInputStream(fileInputStream, 65536)) {
            int numberOfLines = countNewLinesManually(inputStream, bufferSize);
            assertThat(numberOfLines).isEqualTo(30_000);
        }
    }
}
