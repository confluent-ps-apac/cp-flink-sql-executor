package org.example;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<String> parseFileToList(String filePath) throws IOException {
        System.out.println("Parsing SQL from file [" + filePath + "]");
        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        // Join lines into a single string separated by new lines
        StringBuilder contentBuilder = new StringBuilder();
        for (String line : lines) {
            contentBuilder.append(line).append("\n");
        }
        String content = contentBuilder.toString();

        // Split the content by the delimiter '-----'
        String[] segments = content.split("\n-----\n");

        // Process each segment: trim and filter out empty ones
        List<String> parsedSegments = new ArrayList<>();
        for (String segment : segments) {
            String trimmedSegment = segment.trim();
            if (!trimmedSegment.isEmpty()) {
                parsedSegments.add(trimmedSegment);
            }
        }

        return parsedSegments;
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Please specify SQL file to execute as first argument. Multiple SQLs must separate by a new line with \\n-----\\n");
            System.exit(1);
        }
        List<String> queries = parseFileToList(args[0]);
        System.out.println("Loaded " + queries.size() + " queries from [" + args[0] + "]");
        if(queries == null || queries.size() == 0) {
            System.out.println("Error: Queries are empty");
            System.exit(1);
        }

        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        // Set up the Flink environment
        TableEnvironment env = TableEnvironment.create(settings);
        for(int i = 0; i < queries.size(); i++) {
            int index = i + 1;
            String theQuery = queries.get(i);
            theQuery = theQuery.trim();
            if(theQuery.endsWith(";")) {
                theQuery = theQuery.substring(0, theQuery.length() - 1);
            }
            System.out.println("Executing query [" + index + "] -> [" + theQuery + "]");
            TableResult result = env.executeSql(theQuery);
            System.out.println("Done for query [" + index + "] -> " + result);
        }

        System.out.println("All queries submitted. System should continue to run.");
    }
}
