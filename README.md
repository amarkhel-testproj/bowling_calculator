# **Bowling Score Calculator**

This project was developed as test coding exercise and should be discussed at team interview.

# **Business requirements**

Write a program to score a game of Ten-Pin Bowling.

**Input:** string (described below) representing a bowling game

**Ouput:** integer score

**The scoring rules:**

Each game, or "line" of bowling, includes ten turns,or "frames" for the bowler. In each frame, the bowler gets up to two tries to knock down all ten pins.

If the first ball in a frame knocks down all ten pins,this is called a "strike". The frame is over. The score for the frame is ten plus the total of the pins knocked down in the next two balls.

If the second ball in a frame knocks down all ten pins,this is called a "spare". The frame is over. The score for the frame is ten plus the number of pins knocked down in the next ball.

If, after both balls, there is still at least one of the ten pins standing the score for that frame is simply the total number of pins knocked down in those two balls.

If you get a spare in the last (10th) frame you get one more bonus ball. 

If you get a strike in the last (10th)frame you get two more bonus balls.

These bonus throws are taken as part of the same turn.

If a bonus ball knocks down all the pins, the process does not repeat.
 
The bonus balls are only used to calculate the score of the final frame.

The game score is the total of all frame scores.

**Examples:**

_'X'_ indicates a strike _'/'_ indicates a spare _'-'_ indicates a miss _'|'_ indicates a frame boundary. The characters after the _'||'_ indicate bonus balls.

**X|X|X|X|X|X|X|X|X|X||XX** ---- Ten strikes on the first ball of all ten frames. Two bonus balls, both strikes. Score for each frame == 10 + score for next two balls == 10 + 10 + 10 == 30. Total score == 10 frames x 30 == **300**

**9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||** ---- Nine pins hit on the first ball of all ten frames. Second ball of each frame misses last remaining pin. No bonus balls. Score for each frame == 9. Total score == 10 frames x 9 == **90**

**5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5** ---- Five pins on the first ball of all ten frames. Second ball of each frame hits all five remaining pins, a spare. One bonus ball, hits five pins. Score for each frame == 10 + score for next one ball == 10 + 5 == 15. Total score == 10 frames x 15 == **150**

**X|7/|9-|X|-8|8/|-6|X|X|X||81** ---- Total score == **167**

# **Implementation**

Design of software is always a trade-off. Same application can be designed differently according non-functional requirements(development time, cost of maintaining or performance). There is also difference when code should be reused as library(or can be extended and customised) or intended to use only inside team. There is no requirements about validation, but it looks like a public service, so I implemented it with all possible validations that I can imagine(can be looked as paranoidal for someone:)). However, I introduced 'Validator' abstraction, and one of the implementation is simple no-op, so it can be easily switched off.

Recently I switched to Scala language and Functional Programming paradigm, so it was interesting for me to implement algorithms in 2 ways(classical OOP with mutable data and FP without mutability and absence of side-effects). Then I decide that it will be interesting to implement it in both Scala and Java and even compare it both visually and in terms of performance. To do that I created very basic benchmark(will be discussed later). So, this project contains 4 versions of ccode to solve the problem:

_Java: Classic way_ (entrypoint is **com.amarkhel.java.GameService.classic**)

_Java: Functional programming way_ (entrypoint is **com.amarkhel.java.GameService.fp**)

_Scala: Classic way_ (entrypoint is **com.amarkhel.ClassicGameService**)

_Scala: Functional programming way_ (entrypoint is **com.amarkhel.FPGameService**)

All implementations can be customized further(it can run with or without validation), so finally it is 8 versions to measure:)

All tests are implemented in Scala and located here - src/test/scala/

# **Benchmarks**

Code located here - com.amarkhel.benchmark.BowlingBenchmark  It have 8 methods(each implementation run with and without validations)

Measurements was calculated in one thread on my Laptop(Core i7 12 GB) and using different versions of javac.

**Results:**

VM version: JDK 1.8.0_141, Java HotSpot(TM) 64-Bit Server VM, 25.141-b15

|Benchmark         |(size)            |Mode   |Cnt   |Score    Error  |Units|
| -----------------|:----------------:| -----:|-----:|---------------:|----:|
|BowlingBenchmark.classicNoValidator                                     | 10000   |thrpt  | 15 | 21.734 ±  2.194  |ops/s|
|BowlingBenchmark.classicNoValidatorJava                                 | 10000   |thrpt  | 15 | 42.649 ±  3.878  |ops/s|
|BowlingBenchmark.classicValidator                                       | 10000   |thrpt  | 15 | 20.178 ±  1.017  |ops/s|
|BowlingBenchmark.classicValidatorJava                                   | 10000   |thrpt  | 15 |  7.720 ±  0.426  |ops/s|
|BowlingBenchmark.fpNoValidator                                          | 10000   |thrpt  | 15 |  9.991 ±  0.165  |ops/s|
|BowlingBenchmark.fpNoValidatorJava                                      | 10000   |thrpt  | 15 | 18.500 ±  0.280  |ops/s|
|BowlingBenchmark.fpValidator                                            | 10000   |thrpt  | 15 |  8.458 ±  0.425  |ops/s|
|BowlingBenchmark.fpValidatorJava                                        | 10000   |thrpt  | 15 |  6.300 ±  0.127  |ops/s|
|BowlingBenchmark.classicNoValidator                                     | 10000    |avgt  | 15 | 0.048 ±  0.005   |s/op|
|BowlingBenchmark.classicNoValidatorJava                                 | 10000    |avgt  | 15 |  0.024 ±  0.001   |s/op|
|BowlingBenchmark.classicValidator                                       | 10000    |avgt  | 15 |  0.051 ±  0.003   |s/op|
|BowlingBenchmark.classicValidatorJava                                   | 10000    |avgt  | 15 |  0.132 ±  0.014   |s/op|
|BowlingBenchmark.fpNoValidator                                          | 10000    |avgt  | 15 |  0.100 ±  0.002   |s/op|
|BowlingBenchmark.fpNoValidatorJava                                      | 10000    |avgt  | 15 |  0.055 ±  0.001   |s/op|
|BowlingBenchmark.fpValidator                                            | 10000    |avgt  | 15 |  0.122 ±  0.010   |s/op|
|BowlingBenchmark.fpValidatorJava                                        | 10000    |avgt  | 15 |  0.161 ±  0.008   |s/op|


VM version: JDK 9.0.4, Java HotSpot(TM) 64-Bit Server VM, 9.0.4+11

|Benchmark         |(size)            |Mode   |Cnt   |Score    Error  |Units|
| -----------------|:----------------:| -----:|-----:|---------------:|----:|
|BowlingBenchmark.classicNoValidator                                      |10000   |thrpt  | 15  | 19.609 ± 0.725  |ops/s|
|BowlingBenchmark.classicNoValidatorJava                                  |10000   |thrpt  | 15  | 38.807 ± 2.302  |ops/s|
|BowlingBenchmark.classicValidator                                        |10000   |thrpt  | 15  | 18.005 ± 0.540  |ops/s|
|BowlingBenchmark.classicValidatorJava                                    |10000   |thrpt  | 15  | 7.165 ± 0.267  |ops/s|
|BowlingBenchmark.fpNoValidator                                           |10000   |thrpt  | 15  | 10.416 ± 0.270  |ops/s|
|BowlingBenchmark.fpNoValidatorJava                                       |10000   |thrpt  | 15  | 15.931 ± 0.648  |ops/s|
|BowlingBenchmark.fpValidator                                             |10000   |thrpt  | 15  | 8.289 ± 0.248  |ops/s|
|BowlingBenchmark.fpValidatorJava                                         |10000   |thrpt  | 15  | 5.139 ± 0.519  |ops/s|
|BowlingBenchmark.classicNoValidator                                      |10000    |avgt  | 15  | 0.050 ± 0.001   |s/op|
|BowlingBenchmark.classicNoValidatorJava                                  |10000    |avgt  | 15  | 0.025 ± 0.001   |s/op|
|BowlingBenchmark.classicValidator                                        |10000    |avgt  | 15  | 0.056 ± 0.002   |s/op|
|BowlingBenchmark.classicValidatorJava                                    |10000    |avgt  | 15  | 0.140 ± 0.006   |s/op|
|BowlingBenchmark.fpNoValidator                                           |10000    |avgt  | 15  | 0.103 ± 0.011   |s/op|
|BowlingBenchmark.fpNoValidatorJava                                       |10000    |avgt  | 15  | 0.064 ± 0.003   |s/op|
|BowlingBenchmark.fpValidator                                             |10000    |avgt  | 15  | 0.120 ± 0.008   |s/op|
|BowlingBenchmark.fpValidatorJava                                         |10000    |avgt  | 15  | 0.187 ± 0.009   |s/op|


VM version: JDK 10.0.2, Java HotSpot(TM) 64-Bit Server VM, 10.0.2+13

|Benchmark         |(size)            |Mode   |Cnt   |Score    Error  |Units|
| -----------------|:----------------:| -----:|-----:|---------------:|----:|
|BowlingBenchmark.classicNoValidator                                      |10000   |thrpt  | 15  | 19.282 ± 0.479  |ops/s|
|BowlingBenchmark.classicNoValidatorJava                                  |10000   |thrpt  | 15  | 38.501 ± 1.751  |ops/s|
|BowlingBenchmark.classicValidator                                        |10000   |thrpt  | 15  | 17.300 ± 0.427  |ops/s|
|BowlingBenchmark.classicValidatorJava                                    |10000   |thrpt  | 15  | 7.031 ± 0.256  |ops/s|
|BowlingBenchmark.fpNoValidator                                           |10000   |thrpt  | 15  | 9.636 ± 0.450  |ops/s|
|BowlingBenchmark.fpNoValidatorJava                                       |10000   |thrpt  | 15  | 15.595 ± 0.558  |ops/s|
|BowlingBenchmark.fpValidator                                             |10000   |thrpt  | 15  | 8.597 ± 0.481  |ops/s|
|BowlingBenchmark.fpValidatorJava                                         |10000   |thrpt  | 15  | 5.295 ± 0.141  |ops/s|
|BowlingBenchmark.classicNoValidator                                      |10000    |avgt  | 15  | 0.051 ± 0.001   |s/op|
|BowlingBenchmark.classicNoValidatorJava                                  |10000    |avgt  | 15  | 0.026 ± 0.001   |s/op|
|BowlingBenchmark.classicValidator                                        |10000    |avgt  | 15  | 0.057 ± 0.001   |s/op|
|BowlingBenchmark.classicValidatorJava                                    |10000    |avgt  | 15  | 0.142 ± 0.007   |s/op|
|BowlingBenchmark.fpNoValidator                                           |10000    |avgt  | 15  | 0.112 ± 0.005   |s/op|
|BowlingBenchmark.fpNoValidatorJava                                       |10000    |avgt  | 15  | 0.070 ± 0.007   |s/op|
|BowlingBenchmark.fpValidator                                             |10000    |avgt  | 15  | 0.138 ± 0.013   |s/op|
|BowlingBenchmark.fpValidatorJava                                         |10000    |avgt  | 15  | 0.189 ± 0.016   |s/op|

**Insights:**

1) Obviously all versions with validation working slower then without it

2) FP versions is slower than classic imperative(also no big surprises here)

3) Scala versions is slower then Java if running without validation but faster when validation is on. I looked throw Java validator and didn't found obvious reason, need to deep in later.

4) Java 8 version is faster then 9 and 10. Java 9 comparable with Java 10 only little faster. That result was surprise for me.


# Conclusion

If I need most performant version I'll choose Imperative version, if maintainability - FP version. Chosen language highly dependent on other team members, personally I prefer Scala, but Java also ok.

# Build Instructions

To build project just use **mvn clean install** It will compile project, run tests, and create application and benchmarks.jars. To run benchmarks use command **java -jar /target/benchmarks.jar** from the root project folder.

Prerequisites: Maven 3, Java 8+ 