import java.io.*;
import java.util.*; 

class Product{

    String name ;
    int ID ;
    int stock ;
    double price ;

    Product(String name , int ID , int stock , double price ){
        this.name =  name;
        this.ID = ID;
        this.stock = stock;
        this.price = price;

    }

    void displayProduct(){
        System.out.printf("ID : %d | Name : %s | Price : %.2f | Stock : %d\n" , ID, name, price, stock);
    }
}

class FileHandler {

     static void saveProducts(List<Product> products){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products_info.txt"))){
            for(Product p : products){
               writer.write(p.ID + "," + p.name + "," + p.price + "," + p.stock); 
               writer.newLine();

            }
        } catch(IOException e){
            System.out.println("Error in saving products");
        }
        }

        static List<Product> loadproducts(){
            List<Product> products = new ArrayList<>();
            try(BufferedReader reader = new BufferedReader(new FileReader("products_info.txt"))){
                String line;
                while ((line = reader.readLine())!= null) {
                    String[] data = line.split(",");
                    Product p = new Product(
                            data[1],
                            Integer .parseInt(data[0]),
                            Integer.parseInt(data[3]),
                            Double.parseDouble(data[2])

                          );
                    products.add(p);
                }
            } catch(IOException e){
                System.out.println("products_info file not found");
            }
            return products;
        }
        
     }

     interface Payment{
        void pay(double amount);
     }
     class CashonDelivery implements Payment{
        public void pay(double amount){
            System.out.println("Payment Method : Cash On Delivery | Amount : " + amount);
        }
     }
     class OnlinePayment implements Payment{
        public void pay(double amount){
            System.out.println("Payment Method : Online Payment | Amount : " + amount);
        }
     }
     class Cart {
        List<Product> products = new ArrayList<>();

        void addProduct(Product p){
            products.add(p);
            System.out.println("Product added to cart : " + p.name);
        }
 
        void removeProduct(int id){
            products.removeIf(p -> p.ID == id);
        }
         
        double calculateTotal(){
           double total = 0;
           for(Product p : products){
            total += p.price;
           } 
           return total;
        }

        void displayCart(){
            if(products.isEmpty()){
                System.out.println("Cart is Empty");
            }
            else{
                System.out.println("----- YOUR CART -----");
                for(Product p : products){
                    System.out.printf("ID : %d | Name : %s | Price : %.2f\n", p.ID, p.name, p.price);
                }
                System.out.printf("Total %.2f\n",calculateTotal());
            }
        }
     }

     class Admin {
        List<Product> products;

        Admin(List<Product> products){
            this.products = products;
        }

        void addProduct(Product p){
            products.add(p);
            FileHandler.saveProducts(products);
            System.out.println("Product added : " + p.name);
        }

        void removeProduct(int ID){
            boolean removed = products.removeIf(p -> p.ID == ID);
            if(removed){
                System.out.println(" Product Removed Successfuly");
            }
            else{
                System.out.println("Product not found");
            }
            FileHandler.saveProducts(products);
        }

        void updateProduct(int ID,String name,double price,int stock){

            for(Product p : products){
                if(p.ID == ID){
                    p.name = name;
                    p.price = price;
                    p.stock = stock;
                    System.out.println("Product Updated :");
                    FileHandler.saveProducts(products);
                    return;
                }
            }
            System.out.println("Product not found");

        }

        void viewProducts(){
            if(products.isEmpty()){
                System.out.println("No product available");
            }
            else{
                for(Product p : products){
                    p.displayProduct();
                }
            }
        }

        Product searchProducts(int ID){
            for(Product p : products){
                if(p.ID == ID){
                    return p;
                }
            }
            return null;
        }
     }


public class JavaProject {

   static Scanner sc = new Scanner(System.in);
   static List<Product> products =  FileHandler.loadproducts();
   static Admin admin = new Admin(products);
   static Cart cart = new Cart();
    public static void main(String args[]){
        System.out.println();
        System.out.println("===== WELCOME TO E-COMMERSE PROJECT =====");
        mainMenu();
    }
    static void mainMenu(){
        while(true){
        System.out.println("\n1 . AdminLogin\n2 . User\n3 . Exit ");
        System.out.println();
        System.out.println("Enter choice :");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                adminLogin();
                break;
            case 2:
                userMenu();
                break;
            case 3:
                System.out.println("Goodbye!");
                return;
            default:
                System.out.println("Invalid Choice !");
                break;
        }
    }

}

    static void adminLogin(){
        sc.nextLine();
        System.out.println("Enter Username :");
        String user = sc.nextLine();
        System.out.println("Enter password :");
        String pass = sc.nextLine();
        if(user.equals("admin") && pass.equals("12345")){
            System.out.println("Admin logged Successfully");
            adminMenu();
        }
        else{
            System.out.println("Invalid Credentials!");
        }
}

     static void adminMenu(){
        while(true){
          System.out.println("---- AdminMenu ----");
          System.out.println("1 . Add Product\n2 . Remove Product\n3 . Update Product\n4 . View product\n5 . Logout");
          System.out.println("Enter choice :");
          int choice = sc.nextInt();

          switch (choice) {
            case 1:
                addProductFlow();
                break;
            case 2:
                removeProductFlow();
                break;
            case 3:
                updateProductFlow();
                break;
            case 4:
                admin.viewProducts();
                break;
            case 5:
                System.out.println("Logout Successfull !");
                return;
            default:
                System.out.println("Invalid Choice!");
          }
     }
   }

    static void addProductFlow(){
           
        int nextID = 1; 
        if (!products.isEmpty()) {
             int maxID = 0;
            for (Product p : products) {
                if (p.ID > maxID) {
                    maxID = p.ID;
                }
            }
            nextID = maxID + 1;
        }

    
        sc.nextLine(); 
       System.out.println("Assigned Product ID: " + nextID);

       System.out.println("Enter product name :");
       String name = sc.nextLine();
       System.out.println("Enter product stock :");
       int stock = sc.nextInt();
       System.out.println("Enter product price :");
       double price = sc.nextDouble();
       sc.nextLine();

       admin.addProduct(new Product(name,nextID,stock,price));

    }

    static void removeProductFlow(){
        System.out.println("Enter ID of product which you want to remove :");
        int ID = sc.nextInt();
        admin.removeProduct(ID);
    }

    static void updateProductFlow(){
        System.out.println("Enter Product ID to update :");
        int ID = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter name :");
        String name = sc.nextLine();
        System.out.println("Enter stock :");
        int stock = sc.nextInt();
        System.out.println("Enter Price :");
        double price = sc.nextDouble();
        admin.updateProduct(ID, name, price, stock);

    }

    static void userMenu(){

        while(true){
        System.out.println("--- USER MENU ----");
        System.out.println("1. View Product\n2. Search Product\n3. Add to Cart\n4. View Cart\n5. Checkout\n6. Exit ");
        System.out.println("Enter Choice :");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                admin.viewProducts();
                break;
            case 2:
                searchProductFlow();
                break;
            case 3:
                addTocartFlow();
                break;
            case 4:
               cart.displayCart();
               break;
            case 5:
                checkOutFlow();
                break; 
            case 6:
                return;
            default:
                System.out.println("Invalid Choice!");  
        }
    }
}

    static void searchProductFlow(){
        System.out.println("Enter product ID you want to search  :");
        int ID = sc.nextInt();
        Product p = admin.searchProducts(ID);
        if(p != null){
            p.displayProduct();
        }
        else{
            System.out.println("Product Not Found!");
        }


    } 

    static void addTocartFlow(){
        System.out.println("Enter Product ID , you want to add in cart :");
        int ID = sc.nextInt();
        Product p = admin.searchProducts(ID);
        if(p != null && p.stock > 0){
            cart.addProduct(p);
        }
        else{
            System.out.println("Product not Found or Out of stock!");
        }
    }

    static void checkOutFlow(){
        if(cart.products.isEmpty()){
            System.out.println("Cart is Empty!");
            return;
        }
        cart.displayCart();
        double total = cart.calculateTotal();
        total = applyDiscount(total);

        System.out.println("Choose Payment method\n1 . Cash on  Delivery\n2 . Online Payment");
        System.out.println("Enter choice :");
        int payChoice = sc.nextInt();
        
         for (Product cartProduct : cart.products) {
        
            for (Product storeProduct : products) {
                if (storeProduct.ID == cartProduct.ID) {
                     storeProduct.stock--; 
                 }
             }
         }
   
         FileHandler.saveProducts(products);
   
         cart.products.clear(); 
    

        Payment payment = (payChoice == 1)? new CashonDelivery(): new OnlinePayment(); 
        payment . pay(total);
        System.out.println("Order Placed Successfully!");
    }

    static double applyDiscount(double total){
        if(total > 100000){
            double discounted = total * 0.9;
            System.out.println("Dicount applied! New total is : " + discounted);
            return discounted;
        }
        return total;
        


    }




} 

    

