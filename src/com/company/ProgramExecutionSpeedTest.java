package com.company;

import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertTrue;


//Just only for tick of program execution in some cases
public class ProgramExecutionSpeedTest {
    private Stream<String> stream = null;
    private HashSet<String> set = null;
    private final String USERNAME = "user%d";
    private Random rand = new Random();
    @BeforeEach
    void setUp() {
        set = new HashSet<>();
    }

    private void convertRandomQuantityOfUsers(int quantityOfUsers){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < quantityOfUsers; i++){
            String userName = String.format(USERNAME, i);
            final int quantityOfRandomValues = rand.nextInt(10);// Obtain a number between [0 - 9]
            for(int j = 0; j <quantityOfRandomValues; j++){
                set.add(String.format("%d", rand.nextInt(100)));
            }

            String joiningSetRandomValues = String.join(", ", set);

            result.append(userName).append(" -> ").append(joiningSetRandomValues).append("\n");

            set.clear();

        }

        String temp = result.toString();
        stream = Stream.of(
                (
                        temp
                )
                .split("\n")
        );


        long startTime = System.currentTimeMillis();
        Set<Map.Entry<String, Set<String>>> resultSet = Main.convert(stream);
        long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime) + "ms;\n" + resultSet);
    }

    @org.junit.jupiter.api.Test
    void checkExecutionSpeedIf5Users() {
        convertRandomQuantityOfUsers(5);
    }

    @org.junit.jupiter.api.Test
    void checkExecutionSpeedIf10Users(){
        convertRandomQuantityOfUsers(10);
    }


    @org.junit.jupiter.api.Test
    void checkExecutionSpeedIf100Users(){
        convertRandomQuantityOfUsers(100);
    }

    @org.junit.jupiter.api.Test
    void checkExecutionSpeedIf1000Users(){
        convertRandomQuantityOfUsers(1000);
    }

    @org.junit.jupiter.api.Test
    void checkExecutionSpeedIf10000Users(){
        convertRandomQuantityOfUsers(10000);
    }

    @org.junit.jupiter.api.Test
    void checkExecutionSpeedIf100000Users(){
        convertRandomQuantityOfUsers(100000);
    }

}
