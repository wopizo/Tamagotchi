package sample;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Saver {

    //Период проверки необходимости изменения заднего фона игрового поля, роста и настроения питомца
    private final static int CHECK_PERIOD = 60*1000;

    private static String sep = File.separator;
    private static String path = "src" + sep + "sample" + sep + "resources" + sep + "save" + sep + "save.txt";

    public static Date dateCreate;
    public static Date dateFeed;
    public static String name;
    public static String petClass;
    public static double grown;

    public static Date lastCheck = new Date(System.currentTimeMillis());

    public static void create(String name, String petClass){
        try(PrintWriter pw = new PrintWriter(path))
        {
            dateCreate = new Date(System.currentTimeMillis());
            dateFeed = dateCreate;
            Saver.name = name;
            Saver.petClass = petClass;
            grown = 2;
            if(petClass.equals("Rabbit")) grown = 1;

            pw.println(dateCreate.getTime() + "");
            pw.println(dateFeed.getTime() + "");
            pw.println(name);
            pw.println(petClass);
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
            petClass = scanner.nextLine();
            grown = Double.parseDouble(scanner.nextLine());
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }catch (NoSuchElementException | NumberFormatException ex){
            dateCreate = null;
            dateFeed = null;
            name = null;
            petClass = null;
            grown = 0;
        }
    }

    public static void changeDateFeed() {
        try(PrintWriter pw = new PrintWriter(path))
        {
            dateFeed = new Date(System.currentTimeMillis());
            pw.println(dateCreate.getTime() + "");
            pw.println(dateFeed.getTime() + "");
            pw.println(name);
            pw.println(petClass);
            pw.println(grown);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static boolean isTimeToCheck(){
        Date now = new Date(System.currentTimeMillis());
        return ( now.getTime() - lastCheck.getTime() ) / (CHECK_PERIOD) >= 1;
    }

    public static boolean isErrorOrTimeToNewGame(){
        if(dateFeed == null || dateCreate == null || name == null || petClass == null || grown == 0){
            return true;
        }
        Date now = new Date();
        boolean timeToNewGame = (now.getTime() - dateFeed.getTime())/(36*60*60*1000)>=1;
        return timeToNewGame;
    }

    public static void clearSaveFile(){
        try(PrintWriter pw = new PrintWriter(path))
        {
            pw.println("");
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static String log() {
        return "CREATED = " + dateCreate.toString() +
                "\r\nFED = " + dateFeed.toString() +
                "\r\nNAME = " + name +
                "\r\nPETCLASS = " + petClass +
                "\r\nGROWN = " + grown;
    }
}
