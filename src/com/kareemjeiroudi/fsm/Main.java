package com.kareemjeiroudi.fsm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static String initData = "CLOSED: APP_PASSIVE_OPEN -> LISTEN\n" +
            "CLOSED: APP_ACTIVE_OPEN  -> SYN_SENT\n" +
            "LISTEN: RCV_SYN          -> SYN_RCVD\n" +
            "LISTEN: APP_SEND         -> SYN_SENT\n" +
            "LISTEN: APP_CLOSE        -> CLOSED\n" +
            "SYN_RCVD: APP_CLOSE      -> FIN_WAIT_1\n" +
            "SYN_RCVD: RCV_ACK        -> ESTABLISHED\n" +
            "SYN_SENT: RCV_SYN        -> SYN_RCVD\n" +
            "SYN_SENT: RCV_SYN_ACK    -> ESTABLISHED\n" +
            "SYN_SENT: APP_CLOSE      -> CLOSED\n" +
            "ESTABLISHED: APP_CLOSE   -> FIN_WAIT_1\n" +
            "ESTABLISHED: RCV_FIN     -> CLOSE_WAIT\n" +
            "FIN_WAIT_1: RCV_FIN      -> CLOSING\n" +
            "FIN_WAIT_1: RCV_FIN_ACK  -> TIME_WAIT\n" +
            "FIN_WAIT_1: RCV_ACK      -> FIN_WAIT_2\n" +
            "CLOSING: RCV_ACK         -> TIME_WAIT\n" +
            "FIN_WAIT_2: RCV_FIN      -> TIME_WAIT\n" +
            "TIME_WAIT: APP_TIMEOUT   -> CLOSED\n" +
            "CLOSE_WAIT: APP_CLOSE    -> LAST_ACK\n" +
            "LAST_ACK: RCV_ACK        -> CLOSED";

    private static Map<List<String>, String> transitions = new HashMap<>();

    public static void main(String[] args) {
        finishTransition();
//        String[] events = {
//                "APP_PASSIVE_OPEN",
//                "APP_ACTIVE_OPEN",
//                "APP_SEND",
//                "APP_CLOSE",
//                "APP_TIMEOUT",
//                "RCV_SYN",
//                "RCV_ACK",
//                "RCV_SYN_ACK",
//                "RCV_FIN",
//                "RCV_FIN_ACK"
//        };

//        String[] events = {"APP_PASSIVE_OPEN", "APP_SEND", "RCV_SYN_ACK"};
//        String[] events = {"APP_ACTIVE_OPEN"};
        String[] events = {"APP_ACTIVE_OPEN", "RCV_SYN_ACK", "APP_CLOSE", "RCV_FIN_ACK", "RCV_ACK"};
        String endState = traverseState(events);
        System.out.println(endState);
    }

    public static void finishTransition() {
        for (String line : initData.split("\n")) {
            String[] splitted = line.split("->");
            String endState =  splitted[1].trim();

            String[] combination = splitted[0].split(":");
            String initState = combination[0].trim();
            String transition = combination[1].trim();

            transitions.put(
                    List.of(initState, transition), endState
            );
        }
    }

    public static String traverseState(String[] events) {
        String currentState = "CLOSED";
        for (String event: events) {
            List<String> key = List.of(currentState, event);
            currentState = transitions.get(key);
            if (currentState == null) {
                return "ERROR";
            }
        }
        return currentState;
    }
}
