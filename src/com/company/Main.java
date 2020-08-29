package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class Main {

    private static HashSet<User> findUsers(Set<User> distinctUsersWithSetOfEmails, String email) {
        return distinctUsersWithSetOfEmails.parallelStream().filter(
                v -> v.getEmailCollection().parallelStream().anyMatch(v1 -> v1.equals(email))
        ).collect(toCollection(HashSet::new));
    }

    private static void saveEmailsInResultCollection(
            Map<String, Set<String>> distinctUsersWithHashSetOfRelatedEmails,
            Set<String> setEmails,
            Set<Map.Entry<String, Set<String>>> resultCollection
    ){
        Optional<Map.Entry<String, Set<String>>> optMaxCollection = distinctUsersWithHashSetOfRelatedEmails.entrySet().parallelStream().filter(f -> f.getValue().containsAll(setEmails)).max((a, b) -> a.getValue().size() > b.getValue().size() ? 1 : -1);
        if(optMaxCollection.isPresent()){
            optMaxCollection.get().getValue().addAll(setEmails);
            resultCollection.add(optMaxCollection.get());
        }
    }

    private static Set<Map.Entry<String, Set<String>>> convert(Stream<String> stream){


        Set<User> distinctUsersWithSetOfEmails = stream
                .map((strLine) -> strLine.split(" -> ")).distinct()
                .filter((array) -> array.length == 2).map(User::new).collect(toCollection(HashSet::new));


        Map<String, HashSet<User>> distinctEmailsWithSetOfUsers =
                distinctUsersWithSetOfEmails//Определелили множество пользователей с их почтой
                        .parallelStream()//Определим уникальные email и вставим их в Map-коллекцию с пустыми значениями
                        .distinct()
                        .map(User::getEmailCollection)
                        .flatMap(Collection::parallelStream)
                        .distinct()
                        .collect(Collectors.toMap(i -> i, i -> findUsers(distinctUsersWithSetOfEmails, i), (i, j) -> i, HashMap::new));

        Map<
                String, //username
                Set<String> //emails
                > distinctUsersWithHashSetOfRelatedEmails = new HashMap<>();

        Set<Map.Entry<String, Set<String>>> resultCollection = new HashSet<>();


        distinctEmailsWithSetOfUsers.entrySet()
                .parallelStream()
                .map((entry) -> entry.getValue().parallelStream().findAny())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEachOrdered((user) -> {
                    user.getEmailCollection().addAll
                            (
                                    user.getEmailCollection().parallelStream()
                                            .map((strEmail) -> findUsers(distinctUsersWithSetOfEmails, strEmail)) //Находим связанных с текущей почтой пользователей
                                            .map(
                                                    (usersCollection) ->
                                                            usersCollection.parallelStream()
                                                                    .map(User::getEmailCollection)
                                                                    .flatMap(Collection::parallelStream)
                                                                    .collect(toCollection(HashSet::new))
                                            )
                                            .flatMap(Collection::stream)
                                            .collect(toCollection(HashSet::new))
                            );

                    Set<String> setEmails = user.getEmailCollection();

                    if(!distinctUsersWithHashSetOfRelatedEmails.containsValue(setEmails)){
                        distinctUsersWithHashSetOfRelatedEmails.put(user.getUserName(), setEmails);
                    } else {
                        saveEmailsInResultCollection(distinctUsersWithHashSetOfRelatedEmails, setEmails, resultCollection);
                    }

                    //Здесь мы гарантированно можем сказать, что второй раз пользователь с такой электронной почтой нам не встретится,
                    //соответственно сохраняем это значение
                    if(setEmails.size() == 1){
                        saveEmailsInResultCollection(distinctUsersWithHashSetOfRelatedEmails, setEmails, resultCollection);
                    }
                });
        return resultCollection;
    }

    public static void main(String[] args) {
        HashSet<String> hashSet = new HashSet<>();

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            String line;

            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                hashSet.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        long startTime = System.currentTimeMillis();
        Set<Map.Entry<String, Set<String>>> result = convert(hashSet.stream());
        long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime-startTime) + "ms;\n" + result);
    }
}
