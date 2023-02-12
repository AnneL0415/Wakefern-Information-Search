import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String [] args) {

        /** getStoreDetails and getItemDetails URL for demo*/
        String getStoreDetailsURL = "https://apimdev.wakefern.com/mockexample/V1/getStoreDetails";
        String getItemDetailsURL = "https://apimdev.wakefern.com/mockexample/V1/getItemDetails";
        ArrayList<Store> stores = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();
        
/**
 * Get Request Template
 */
        // 1 - Create Client
        HttpClient clientStores = HttpClient.newHttpClient();

        // 2 - Build Request with headers
        HttpRequest requestStores = HttpRequest.newBuilder()
                .uri(URI.create(getStoreDetailsURL))
                .header("Ocp-Apim-Subscription-Key", "4ae9400a1eda4f14b3e7227f24b74b44")      //Set Other Headers
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpRequest requestProducts = HttpRequest.newBuilder()
                .uri(URI.create(getItemDetailsURL))
                .header("Ocp-Apim-Subscription-Key", "4ae9400a1eda4f14b3e7227f24b74b44")      //Set Other Headers
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        
        // 3 - Client sends request and saves the response in a variable
        try {
            HttpResponse responseStores = clientStores.send(requestStores, HttpResponse.BodyHandlers.ofString());
            HttpResponse responseProducts = clientStores.send(requestProducts, HttpResponse.BodyHandlers.ofString());

            // 4 - Print response in console
            /** Print Response Here */
            
            //Creating Files
            File storeFile = new File("StoreFile.txt");
            PrintWriter printStores = new PrintWriter(storeFile);
            printStores.print(responseStores.body());
            printStores.close(); 

            File productFile = new File("ProductFile.txt");
            PrintWriter printProducts = new PrintWriter(productFile);
            printProducts.print(responseProducts.body());
            printProducts.close();

            String readLine;
            String actualString;

            //Filling Store Array
            Scanner storeReader = new Scanner(storeFile);
            while(storeReader.hasNext()){
                readLine = storeReader.nextLine();
                readLine = storeReader.nextLine();

                if(readLine.compareTo("]") == 0){
                    break;
                }

                readLine = storeReader.nextLine();
                actualString = readLine.substring(13, readLine.length()-1);
                int storeNum = Integer.parseInt(actualString);

                readLine = storeReader.nextLine();
                String address = readLine.substring(16, readLine.length()-2);

                readLine = storeReader.nextLine();
                String city = readLine.substring(13, readLine.length()-2);

                readLine = storeReader.nextLine();
                String state = readLine.substring(14, readLine.length()-2);

                readLine = storeReader.nextLine();
                String zipcode = readLine.substring(16, readLine.length()-2);

                readLine = storeReader.nextLine();
                String phone = readLine.substring(14, readLine.length()-1);

                Store newStore = new Store(storeNum, address, city, state, zipcode, phone);
                stores.add(newStore);
            }
            storeReader.close();
            
            //Filling Product Array
            Scanner productReader = new Scanner(productFile);
            Double price = 0.0;
            Double disPrice = 0.0;
            Double loyalPrice = 0.0;
            Double coupon = 0.0;
            boolean thisIsAnAPIIssue = false;
            while(productReader.hasNext()){
                if(thisIsAnAPIIssue){
                    readLine = productReader.nextLine();
                    thisIsAnAPIIssue = false;
                }else{
                    readLine = productReader.nextLine();
                    readLine = productReader.nextLine();
                }
                
                if(readLine.compareTo("]") == 0){
                    break;
                }

                readLine = productReader.nextLine();
                String upc = readLine.substring(11, readLine.length()-1);
                if(upc.compareTo("1632972134587") == 0){
                    thisIsAnAPIIssue = true;
                }

                readLine = productReader.nextLine();
                String description = readLine.substring(20, readLine.length()-2);

                readLine = productReader.nextLine();
                String department = readLine.substring(17, readLine.length()-2);

                readLine = productReader.nextLine();
                actualString = readLine.substring(14, readLine.length()-2);
                if(actualString.substring(0,1).equals("$")){
                    price = Double.parseDouble(actualString.substring(1));
                }else{
                    price = 0.00;
                }

                readLine = productReader.nextLine();
                actualString = readLine.substring(23, readLine.length()-2);
                if((actualString.length()) != 0 && (actualString.substring(0,1).equals("$"))){
                    disPrice = Double.parseDouble(actualString.substring(1));
                }else{
                    disPrice = 0.00;
                }

                readLine = productReader.nextLine();
                actualString = readLine.substring(25, readLine.length()-2);
                if((actualString.length()) != 0 && (actualString.substring(0,1).equals("$"))){
                    loyalPrice = Double.parseDouble(actualString.substring(1));
                }else{
                    loyalPrice = 0.00;
                }

                readLine = productReader.nextLine();
                actualString = readLine.substring(23, readLine.length()-1);
                if((actualString.length()) != 0 && (actualString.substring(0,1).equals("$"))){
                    coupon = Double.parseDouble(actualString.substring(1));
                }else{
                    coupon = 0.00;
                }

                Product newProduct = new Product(upc, description, department, price, disPrice, loyalPrice, coupon);
                products.add(newProduct);
            }
            // 5 - Error Handling
            /**
             * Error Handling Conventions:
             * Errors are generally saved in a separate log file to be accessed in the future
             * In production, code generally should not print anything on the console
             * Console logging is okay for development/debugging purposes
             */
        } catch (IOException e) {
           /** Set Error Handling Here */
        } catch (InterruptedException e) {
            /** Set Error Handling Here */
        }

        boolean isRunning = true;
        Scanner input = new Scanner(System.in);
        boolean isRunningCatch = false;

        while(isRunning){
            System.out.println("1. Check store locations");
            System.out.println("2. Check prices");
            System.out.println("3. Exit");
            System.out.print("Please enter your option number: ");
            int option = 0;
            try{
                option = input.nextInt();
            }catch(Exception e){
                System.out.print("Please enter a valid number.\n\n");
                input.nextLine();
                isRunningCatch = true;
            }

            if(option == 1){ //Search for locations
                boolean isRunning1 = true;
                while(isRunning1){
                    System.out.println("Search by:");
                    System.out.println("1. City");
                    System.out.println("2. Zipcode");
                    System.out.println("3. Go back");
                    System.out.print("Please enter your option number: ");
                    try{
                        option = input.nextInt();
                        input.nextLine();
                        isRunning1 = false;
                    }catch(Exception e){
                        System.out.print("Please enter a valid number.\n\n");
                        input.nextLine();
                    }
                    if(option > 3 || option < 1){
                        System.out.print("Please enter a valid number.\n\n");
                        isRunning1 = true;
                    }
                }
                if(option == 1){ //Search by City
                    System.out.print("Please enter your city: ");
                    String city = input.nextLine();
                    int count = 0;
                    String response = "";
                    for(int i = 0; i < stores.size(); i++){
                        if(stores.get(i).getCity().compareToIgnoreCase(city) == 0){
                            count++;
                            response += stores.get(i).getFullAddress() + "\n" + stores.get(i).getContact() + "\n\n";
                        }
                    }
                    if(count == 0){
                        System.out.print("\nNo stores found in this city :(\n\n----------\n\n");
                    }else{
                        System.out.print("\n" + count + " stores found in your city:\n\n" + response + "----------\n\n");
                    }
                }else if(option == 2){ //Search by Zipcode
                    System.out.print("Please enter your zipcode: ");
                    String zipcode = input.nextLine();
                    int count = 0;
                    String response = "";
                    for(int i = 0; i < stores.size(); i++){
                        if(stores.get(i).getZipcode().compareToIgnoreCase(zipcode) == 0){
                            count++;
                            response += stores.get(i).getFullAddress() + "\n" + stores.get(i).getContact() + "\n\n";
                        }
                    }
                    if(count == 0){
                        System.out.print("\nNo stores found with this zipcode :(\n\n----------\n\n");
                    }else{
                        System.out.print("\n" + count + " stores found with this zipcode:\n" + response + "----------\n\n");
                    }
                }else{
                    System.out.println();
                }
            }   

            else if(option == 2){ //Search prices
                boolean hasLoyalty = false;
                boolean isRunning2 = true;
                while(isRunning2){
                    System.out.println("Do you have store loyalty?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.println("3. Go back");
                    System.out.print("Please enter your option number: ");
                    try{
                        option = input.nextInt();
                        input.nextLine();
                        isRunning2 = false;
                    }catch(Exception e){
                        System.out.print("Please enter a valid number.\n\n");
                        input.nextLine();
                    }
                    if(option > 3 || option < 1){
                        System.out.print("Please enter a valid number.\n\n");
                        isRunning2 = true;
                    }
                }

                if(option == 1){
                    hasLoyalty = true;
                }

                if(option == 1 || option == 2){
                    System.out.print("Enter a product: ");
                    String product = input.nextLine();
                    boolean isFound = false;
                    for(int i = 0; i < products.size(); i++){
                        if(products.get(i).getDescription().compareToIgnoreCase(product) == 0){
                            System.out.println("\nItem found:");
                            System.out.println(products.get(i).getDescription().toUpperCase());
                            System.out.println("Original Price: $" + products.get(i).getPrice());
                            if(hasLoyalty){
                                System.out.println("Current Price: $" + products.get(i).getPriceWithLoyalty() + "\n\n----------\n");
                                isFound = true;
                            }else{
                                System.out.println("Current Price: $" + products.get(i).getPriceWithoutLoyalty() + "\n\n----------\n");
                                isFound = true;
                            }
                            break;
                        }
                    }
                    if(!isFound){
                        System.out.println("\nItem not found.\n\n----------\n");
                    }

                }

            }
            
            else if(option == 3){
                isRunning = false;
            }

            else if (!isRunningCatch){
                System.out.println("Please enter a valid number.\n\n");
            }

        }
        input.close();

        

    }

}