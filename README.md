# apache-beam


mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.GastosCount   \
      -Dexec.args="--inputFile=./data_files/201701_GastosDireitos.min.csv \
      --output=./output/gastosCount" -Pdirect-runner




mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.GastosSum   \
      -Dexec.args="--inputFile=./data_files/201701_GastosDireitos.min.csv \
--output=./output/gastosSum" -Pdirect-runner
