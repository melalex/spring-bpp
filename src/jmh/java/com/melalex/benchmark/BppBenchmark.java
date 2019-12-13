package com.melalex.benchmark;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import com.melalex.bpp.BppApplication;
import com.melalex.bpp.aapi.service.AapiPingService;
import com.melalex.bpp.annotation.service.AnnotatedPingService;
import com.melalex.bpp.gof.service.GofPingService;
import com.melalex.bpp.spring.service.SpringPingService;
import com.melalex.bpp.web.dto.PingDto;
import com.melalex.bpp.web.session.UserContext;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G", "-XX:+PrintCompilation", "-verbose:gc"})
@Warmup(iterations = 40)
public class BppBenchmark {

  private Random random;
  private AnnotatedPingService annotatedPingService;
  private GofPingService gofPingService;
  private SpringPingService springPingService;
  private AapiPingService aapiPingService;
  private BaseLinePingService baseLinePingService;
  private UserContext userContext;

  public static void main(final String[] args) throws RunnerException {
    final var opt = new OptionsBuilder()
        .include(BppBenchmark.class.getSimpleName())
        .forks(2)
        .build();

    new Runner(opt).run();
  }

  @Setup(Level.Trial)
  public void setUp() {
    System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "benchmark");

    final var context = new AnnotationConfigApplicationContext(BppApplication.class);

    this.annotatedPingService = context.getBean(AnnotatedPingService.class);
    this.gofPingService = context.getBean(GofPingService.class);
    this.springPingService = context.getBean(SpringPingService.class);
    this.aapiPingService = context.getBean(AapiPingService.class);
    this.userContext = context.getBean(UserContext.class);
    this.random = new Random();
    this.baseLinePingService = new BaseLinePingService();
  }

  @Setup(Level.Invocation)
  public void setUpSession() {
    this.userContext.setCassandra(this.random.nextBoolean());
  }

  @Benchmark
  public void annotatedPingService(final Blackhole blackhole) {
    blackhole.consume(this.annotatedPingService.ping());
  }

  @Benchmark
  public void gofPingService(final Blackhole blackhole) {
    blackhole.consume(this.gofPingService.ping());
  }

  @Benchmark
  public void springPingService(final Blackhole blackhole) {
    blackhole.consume(this.springPingService.ping());
  }

  @Benchmark
  public void aapiPingService(final Blackhole blackhole) {
    blackhole.consume(this.aapiPingService.ping());
  }

  @Benchmark
  public void baseLinePingService(final Blackhole blackhole) {
    blackhole.consume(this.baseLinePingService.ping());
  }

  private static class BaseLinePingService {

    public PingDto ping() {
      return new PingDto("BaseLinePingService");
    }
  }
}
