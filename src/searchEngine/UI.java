package searchEngine;

import java.util.List;
import java.util.Scanner;

public class UI {

    public static void main(String[] args){

        SearchEngine searchEngine= new SearchEngine();

        Scanner sc = new Scanner(System.in);

        //keep working while the user wants to do other operations
        boolean isWorking = true;
        while(isWorking) {

            System.out.println("what operation do you want ot do: ");
            System.out.println("1- read document(s) file");
            System.out.println("2- read all files in directory");
            System.out.println("3- search for document with specific word");
            System.out.println("4- search for documents containing many words");
            System.out.println("5- delete file from the engine");
            System.out.print("enter number of your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("enter the full path of your file please: ");
                    String filePath = sc.nextLine();
                    searchEngine.indexWebPage(filePath);
                }

                case 2 -> {
                    System.out.print("enter full path for the directory please: ");
                    String directoryPath = sc.nextLine();
                    searchEngine.indexDirectory(directoryPath);
                }

                case 3 -> {
                    if(searchEngine.webPageIndices.size() == 0){
                        System.out.println("the engine has no documents to search in it");
                        System.exit(-1);
                    }
                    System.out.print("enter the word you want to search for: ");
                    String word = sc.nextLine();
                    List<ISearchResult> result = searchEngine.searchByWordWithRanking(word.toLowerCase());
                    for (ISearchResult i: result){
                        System.out.println("id is: " + i.getId() + " rank is: "+ i.getRank());
                    }
                }

                case 4 -> {
                    if(searchEngine.webPageIndices.size() == 0){
                        System.out.println("the engine has no documents to search in it");
                        System.exit(-1);
                    }
                    System.out.print("enter a sentence of the words to search for: ");
                    String sent = sc.nextLine();
                    List<ISearchResult> result = searchEngine.searchByMultipleWordWithRanking(sent.toLowerCase());
                    for (ISearchResult i: result){
                        System.out.println("id is: " + i.getId() + " rank is: "+ i.getRank());
                    }
                }

                case 5 -> {
                    if(searchEngine.webPageIndices.size() == 0){
                        System.out.println("the engine has no documents to delete");
                        System.exit(-2);
                    }
                    System.out.print("enter the full path for the file do delete: ");
                    String deletePath = sc.nextLine();
                    int sizeBeforeDeleting = searchEngine.webPageIndices.size();
                    searchEngine.deleteWebPage(deletePath);
                    int sizeAfterDeleting = searchEngine.webPageIndices.size();
                    System.out.println("the system had: " + sizeBeforeDeleting + " documents");
                    System.out.println("we have removed {" + (sizeBeforeDeleting - sizeAfterDeleting) + "} documents");
                    System.out.println("search engine now contains: " + sizeAfterDeleting + " documents");
                }
            }

            //check if the user want to exit the program
            System.out.print("do you want to do any other operation(y/n): ");
            String cont = sc.nextLine();
            //exit the program
            if(cont.equalsIgnoreCase("no") || cont.equalsIgnoreCase("n"))
                isWorking = false;
        }
    }
}