/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.examples;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

//mvn compile exec:java -Dexec.mainClass=org.apache.beam.examples.LinkCount -Dexec.args="--inputFile=index.html --output=links"

public class LinkCount {

  
  static class ExtractLinksFn extends DoFn<String, String> {
    
	private final Counter emptyLines = Metrics.counter(ExtractLinksFn.class, "emptyLines");

    @ProcessElement
    public void processElement(ProcessContext c) {
      if (c.element().trim().isEmpty()) {
        emptyLines.inc();
      }

      String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
      Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
      Matcher urlMatcher = pattern.matcher(c.element());

      while (urlMatcher.find()) {
          String url = c.element().substring(urlMatcher.start(0),
                  urlMatcher.end(0));
          c.output(url);
      }
    }
  }

  /** A SimpleFunction that converts a Word and Count into a printable string. */
  public static class FormatAsTextFn extends SimpleFunction<KV<String, Long>, String> {
    
	@Override
    public String apply(KV<String, Long> input) {
      return input.getKey() + ": " + input.getValue();
    }
  }

  
  public static class CountLinks extends PTransform<PCollection<String>,
      PCollection<KV<String, Long>>> {
    
	@Override
    public PCollection<KV<String, Long>> expand(PCollection<String> lines) {

      // Convert lines of text into individual words.
      PCollection<String> links = lines.apply(
          ParDo.of(new ExtractLinksFn()));

      // Count the number of times each word occurs.
      PCollection<KV<String, Long>> linksCounts =
          links.apply(Count.<String>perElement());

      return linksCounts;
    }
  }

  
  public interface LinkCountOptions extends PipelineOptions {

    
    @Description("Path of the file to read from")
    @Default.String("pom.xml")
    String getInputFile();
    void setInputFile(String value);

    
    @Description("Path of the file to write to")
    @Required
    String getOutput();
    void setOutput(String value);
  }

  public static void main(String[] args) {
    LinkCountOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
      .as(LinkCountOptions.class);
    Pipeline p = Pipeline.create(options);

    
    p.apply("ReadLines", TextIO.read().from(options.getInputFile()))
     .apply(new CountLinks())
     .apply(MapElements.via(new FormatAsTextFn()))
     .apply("WriteCounts", TextIO.write().to(options.getOutput()));

    p.run().waitUntilFinish();
  }
}
