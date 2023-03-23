**This is repo with two grpc servers for compare performance spring vs vanilla grpc servers** 

***Vanilla package based on pure grpc and netty server.***

***Spring package based on springboot with [grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter) and [reactor-grpc-stub](https://github.com/salesforce/reactive-grpc). I tasted it without spring reactor result was same***

***Jmh package contains simple [Java Microbenchmark Harness](https://github.com/openjdk/jmh) application for measuring grpc speed. But i will use [ghz](https://github.com/bojand/ghz).***

How to run
----

1) start grpc servers from [VanillaServiceLauncher.java](vanilla%2Fsrc%2Fmain%2Fjava%2Fcomrade%2Ftest%2Fvanilla%2FVanillaServiceLauncher.java) or/and [SpringbootGrpcServer.java](spring%2Fsrc%2Fmain%2Fjava%2Fcomrade%2Ftest%2Fspring%2FSpringbootGrpcServer.java)
2) start [behch.sh](behch.sh) with scope vanilla/spring ```./behch.sh vanilla```

**My results:**

1) **Vanilla**:
```
Run ghz in scope vanilla with port 62004 ---- 
Benchmark will be stopped after 1 minute ---- 

Summary:
  Name:         vanilla
  Count:        974523
  Total:        60.00 s
  Slowest:      60.05 ms
  Fastest:      0.04 ms
  Average:      0.05 ms
  Requests/sec: 16241.78

Response time histogram:
  0.036 [1]     |
  6.037 [974520]        |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  12.039 [0]    |
  18.040 [0]    |
  24.042 [0]    |
  30.043 [0]    |
  36.045 [0]    |
  42.046 [0]    |
  48.048 [0]    |
  54.049 [0]    |
  60.050 [1]    |

Latency distribution:
  10 % in 0.04 ms 
  25 % in 0.04 ms 
  50 % in 0.04 ms 
  75 % in 0.05 ms 
  90 % in 0.05 ms 
  95 % in 0.06 ms 
  99 % in 0.09 ms 

Status code distribution:
  [OK]            974522 responses   
  [Unavailable]   1 responses        

Error distribution:
  [1]   rpc error: code = Unavailable desc = transport is closing 
```

2) **Spring:**
```
Run ghz in scope spring with port 61004 ---- 
Benchmark will be stopped after 1 minute ---- 

Summary:
  Name:         spring
  Count:        625721
  Total:        60.00 s
  Slowest:      75.30 ms
  Fastest:      0.06 ms
  Average:      0.08 ms
  Requests/sec: 10428.52

Response time histogram:
  0.064 [1]     |
  7.588 [625719]        |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  15.111 [0]    |
  22.635 [0]    |
  30.158 [0]    |
  37.682 [0]    |
  45.206 [0]    |
  52.729 [0]    |
  60.253 [0]    |
  67.776 [0]    |
  75.300 [1]    |

Latency distribution:
  10 % in 0.07 ms 
  25 % in 0.07 ms 
  50 % in 0.08 ms 
  75 % in 0.08 ms 
  90 % in 0.10 ms 
  95 % in 0.10 ms 
  99 % in 0.12 ms 

Status code distribution:
  [OK]   625721 responses   
```