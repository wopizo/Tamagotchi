package sample;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

public class Saver {

    private static String sep = File.separator;
    private static String path = "src" + sep + "sample" + sep + "resources" + sep + "save" + sep + "save.txt";

    public static Date dateCreate;
    public static Date dateFeed;
    public static String name;
    public static double grown;

    public static Date lastCheck = new Date(System.currentTimeMillis());

    public static void create(){
        try(PrintWriter pw = new PrintWriter(path))
        {
            dateCreate = new Date(System.currentTimeMillis());
            dateFeed = dateCreate;
            name = "Barsik";
            grown = 2;

            pw.println(dateCreate.getTime() + "");
            pw.println(dateFeed.getTime() + "");
            pw.println(name);
            pw.println(grown);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void read(){
        try(Scanner scanner = new Scanner( new File(path) ))
        {
            dateCreate = new Date( Long.parseLong( scanner.nextLine() ) );
            dateFeed = new Date( Long.parseLong( scanner.nextLine() ) );
            name = scanner.nextLine();
            grown = Double.parseDouble(scanner.nextLine());
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void changeDateFeed() {
        try(PrintWriter pw = new PrintWriter(path))
        {
            dateFeed = new Date(System.currentTimeMillis());
            pw.println(dateCreate.getTime() + "");
            pw.println(dateFeed.getTime() + "");
            pw.println(name);
            pw.println(grown);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static boolean isTimeToCheck(){
        Date now = new Date(System.currentTimeMillis());
        return ( now.getTime() - lastCheck.getTime() ) / (1*1000) >= 1;
    }

    public static String log() {
        return "CREATED = " + dateCreate.toString() +
                "\r\nFED = " + dateFeed.toString() +
                "\r\nNAME = " + name +
                "\r\nGROWN = " + grown;
    }
}
