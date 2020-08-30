package com.company;

import org.junit.jupiter.api.BeforeEach;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {


    private Stream<String> stream = null;

    @BeforeEach
    void setUp() {
        stream = Stream.of(
                (
                        "user1 -> xxx@ya.ru, foo@gmail.com, lol@mail.ru\n" +
                                "user2 -> foo@gmail.com, ups@pisem.net\n" +
                                "user3 -> xyz@pisem.net, vasya@pupkin.com\n" +
                                "user4 -> ups@pisem.net, aaa@bbb.ru\n" +
                                "user5 -> xyz@pisem.net"
                )
                        .split("\n")
        );
    }


    @org.junit.jupiter.api.Test
    void checkIfLengthOfAllElementsOfSetIsEqual2() {
        HashSet<String[]> strings = stream.map((strLine) -> strLine.split(" -> ")).distinct().filter((array) -> array.length != 2).collect(toCollection(HashSet::new));

        assertTrue(strings.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void checkIf() {

        Set<Map.Entry<String, Set<String>>> resultSet = Main.convert(stream);

        Predicate<Set<String>> predicate1 = p ->
                p.contains("aaa@bbb.ru") &&
                p.contains("ups@pisem.net") &&
                p.contains("lol@mail.ru") &&
                p.contains("xxx@ya.ru") &&
                p.contains("foo@gmail.com");

        Predicate<Set<String>> predicate2 = p ->
                p.contains("xyz@pisem.net") &&
                p.contains("vasya@pupkin.com");

        boolean flgAllMatch1 = resultSet.stream().map(Map.Entry::getValue).anyMatch(predicate1);
        boolean flgAllMatch2 = resultSet.stream().map(Map.Entry::getValue).anyMatch(predicate2);

        assertTrue(flgAllMatch1);
        assertTrue(flgAllMatch2);
    }
}
