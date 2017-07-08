# apache-beam


## Criando repositorio original 

Não é necessário para os demais exemplos, porém não consta na documentação oficial.
```
mvn archetype:generate \
      -DarchetypeGroupId=org.apache.beam \
      -DarchetypeArtifactId=beam-sdks-java-maven-archetypes-examples-java8 \
      -DarchetypeVersion=2.0.0 \
      -DgroupId=org.example \
      -DartifactId=java8-apache-beam2 \
      -Dversion="0.1" \
      -Dpackage=org.apache.beam.examples \
      -DinteractiveMode=false
```


## Criando repositorio da apresentacao

```
git clone https://github.com/klingerkrieg/apache-beam
```


## Executando o exemplo WordCount oficial

```
mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.WordCount \
     -Dexec.args="--inputFile=pom.xml --output=counts" -Pdirect-runner
```

## Rodando no spark 1.6.3

Iniciando o spark
```
cd sbin
./start-master.sh -h localhost -p 7077
./start-slave.sh spark://localhost:7077
```
Abra no browser http://localhost:8080

### Gereo o jar para o spark

```
mvn package -Pspark-runner -Dmaven.test.skip=true
ls target
```

### Execute
```
../../directors/spark-1.6.3-bin-hadoop2.6/bin/spark-submit --class org.apache.beam.examples.WordCount \
                --master spark://localhost:7077 target/java8-apache-beam-0.1.jar \
                --runner=SparkRunner \
                --output=output \
                --inputFile=pom.xml

```

Ao terminar finalize o spark
```
./stop-master.sh
./stop-slave.sh
```

## Executando no flink 1.2.0

```
./start-local.sh
localhost:8081
./taskmanager.sh start
```


### Gere o jar para o flink

```
mvn package -Pflink-runner
```

### Execute
```
mvn exec:java -Dexec.mainClass=org.apache.beam.examples.WordCount    \
   -Pflink-runner     \
   -Dexec.args="--runner=FlinkRunner \
      --inputFile=/media/alan/tudo/apache-beam-2/java8-apache-beam/pom.xml \
      --output=/media/alan/tudo/apache-beam-2/java8-apache-beam/output/flink \
      --flinkMaster=localhost:6123 \
      --filesToStage=target/java8-apache-beam-0.1.jar"
```



## Executando exemplos da apresentação


```
mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.GastosCount   \
      -Dexec.args="--inputFile=./data_files/201701_GastosDireitos.min.csv \
      --output=./output/gastos-count" -Pdirect-runner




mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.GastosSum   \
      -Dexec.args="--inputFile=./data_files/201701_GastosDireitos.min.csv \
      --output=./output/gastos-sum" -Pdirect-runner
```



## Executando no spark

```
../../directors/spark-1.6.3-bin-hadoop2.6/bin/spark-submit --class org.apache.beam.examples.GastosSum \
                --master spark://localhost:7077 target/java8-apache-beam-0.1.jar \
                --runner=SparkRunner \
                --output=./output/spark-gastos-sum \
                --inputFile=./data_files/201701_GastosDireitos.min.csv
```


## Executando no flink
```
mvn exec:java -Dexec.mainClass=org.apache.beam.examples.GastosSum    \
   -Pflink-runner     \
   -Dexec.args="--runner=FlinkRunner \
      --inputFile=/media/alan/tudo/apache-beam-2/java8-apache-beam/data_files/201701_GastosDireitos.min.csv \
      --output=/media/alan/tudo/apache-beam-2/java8-apache-beam/output/flink-gastos-sum \
      --flinkMaster=localhost:6123 \
      --filesToStage=target/java8-apache-beam-0.1.jar"
```