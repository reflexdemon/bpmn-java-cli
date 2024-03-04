# bpmn-java-cli
This is a simple utility to convert the BPMN xml files to PNG images.

# Build
Please use the below command to package the jar file

```shell
$ ./gradlew clean assemble
```

# Run

Please use the below command to run the uber jar

```shell
$ java -jar ./build/libs/bpmn-java-cli-1.0-SNAPSHOT.jar
08:13:27.903 [main] ERROR io.vpv.Main -- Problem while parsing arguments Missing required options: i, o
usage: bpmn-java-cli
 -i,--input <arg>    input file path
 -o,--output <arg>   output file
```