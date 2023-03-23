package comrade.test.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.Random;

public class GrpcBenchmark {
    static {
        System.setProperty("daemon", "jmh");
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        Random randomGenerator;
        ComradeGrpcClient grpcClient;

        @Setup(Level.Trial)
        public void setUp() {
            randomGenerator = new Random();
            grpcClient = new ComradeGrpcClient("127.0.0.1", 64004);
        }

        @TearDown(Level.Trial)
        public void tearDown() throws InterruptedException {
            grpcClient.shutdown();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(1)
//    @Warmup(time = 1, timeUnit = TimeUnit.HOURS)
    public String put(ExecutionPlan plan) throws Exception {
        int index = plan.randomGenerator.nextInt(65535);
        return plan.grpcClient.testComrade(String.format("Sasha_%s", index));
    }
}
