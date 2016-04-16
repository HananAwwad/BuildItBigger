package com.provider;

import net.joningi.icndb.ICNDBClient;
import net.joningi.icndb.Joke;

import org.jsoup.Jsoup;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Joker {


    private static final Logger log = Logger.getLogger(Joker.class.getName());

    private static final ICNDBClient client = new ICNDBClient();


    public static void main(String[] args) {
        String joke = getJoke("New Joke..", "Chuck", "Norris");
        System.out.println("---> JOKE: " + joke);
    }

    public static String getJoke(String defaultJoke, String firstname, String lastname) {
        log.log(Level.INFO, "getJoke");
        //return "getJoke";
        final Random rand = new Random();
        int number = rand.nextInt(10000) + 1;
        String theJoke;

        theJoke = Jsoup.parse(getJokeFromInternet(firstname, lastname)).text();

        if (theJoke == null) theJoke = "Random Joke #" + number + ":\n\n" + defaultJoke;

        return theJoke;
    }

    // get a joke from the Internet Chuck Norris Database..
    private static String getJokeFromInternet(String firstname, String lastname) {
        log.log(Level.INFO, "GetJokeFromInternet");
        try {
            client.setFirstName(firstname);
            client.setLastName(lastname);
            Joke randomJoke = client.getRandom();
            return randomJoke.getJoke();
        } catch (Exception e) {
            log.log(Level.WARNING, "problem accessing the Internet Chuck Norris Database - e=" + e);
            return null;
        }
    }


}

