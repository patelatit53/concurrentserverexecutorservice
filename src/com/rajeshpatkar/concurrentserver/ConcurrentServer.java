package com.rajeshpatkar.concurrentserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ConcurrentServer {

    public static ArrayList<PrintWriter> al = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        System.out.println("Server signing On");
        ExecutorService e = Executors.newFixedThreadPool(3);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                ()->System.out.println("Jagte Raho..."),
                5,
                1,
                TimeUnit.SECONDS
        );
        ServerSocket ss = new ServerSocket(9081);
        for (int i = 0; i < 5; i++) {
            Socket soc = ss.accept();
            BufferedReader nis = new BufferedReader(
                    new InputStreamReader(
                            soc.getInputStream()
                    )
            );
            PrintWriter nos = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    soc.getOutputStream()
                            )
                    ), true
            );
            al.add(nos);
            e.execute(() -> {
                System.out.println("Conversation thread  "
                        + Thread.currentThread().getName()
                        + "   signing On"
                );
                try {
                    String str = nis.readLine();
                    while (!str.equals("End")) {
                        System.out.println("Server Recieved  " + str);
                        for (PrintWriter o : ConcurrentServer1.al) {
                            o.println(str);
                        }
                        str = nis.readLine();
                    }
                    nos.println("End");
                } catch (Exception e1) {
                    System.out.println(
                            "Client Seems to have abruptly closed the connection"
                    );
                }

                System.out.println("Conversation thread  "
                        + Thread.currentThread().getName()
                        + "   signing Off"
                );
            }
            );

        }
        e.shutdown();
        scheduler.shutdown();
        System.out.println("Server signing Off");
    }
}