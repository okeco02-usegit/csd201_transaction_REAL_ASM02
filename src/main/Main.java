package main;

import service.MainController;
import model.Account;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MainController bankController = new MainController();
        int choice = -1;

        System.out.println("==================================================");
        System.out.println("     DIGITAL BANKING SYSTEM (OOP PROJECT)         ");
        System.out.println("==================================================");
        
        bankController.loadData();

        while (choice != 0) {
            System.out.println("\n------------------- MAIN MENU -------------------");
            System.out.println("1. Create New Account");
            System.out.println("2. Lookup Account Info");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. View Transaction History");
            System.out.println("7. Save Data to File");
            System.out.println("8. Generate Sorted Monthly Report");
            System.out.println("9. Search Transaction by ID");
            System.out.println("0. Save & Exit System");
            System.out.print(">> Please select an option (0-9): ");

            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("[ERROR] Invalid input. Please enter a valid number!");
                scanner.nextLine(); 
                continue;
            }
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.println("\n--- 1. CREATE NEW ACCOUNT ---");
                    System.out.print("Enter Account ID: ");
                    String newId = scanner.nextLine();
                    System.out.print("Enter Customer Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter Initial Balance ($): ");
                    double initBal = scanner.nextDouble();
                    
                    bankController.createAccount(newId, newName, initBal);
                    break;

                case 2:
                    System.out.println("\n--- 2. ACCOUNT LOOKUP ---");
                    System.out.print("Enter Account ID to search: ");
                    String searchId = scanner.nextLine();
                    Account foundAcc = bankController.findAccount(searchId);
                    
                    if (foundAcc != null) {
                        System.out.println("[ACCOUNT DETAILS]");
                        System.out.println("- Account ID: " + foundAcc.getAccountId());
                        System.out.println("- Owner Name: " + foundAcc.getAccountName());
                        System.out.println("- Current Balance: $" + foundAcc.getBalance());
                    } else {
                        System.out.println("[FAILED] Account [" + searchId + "] not found.");
                    }
                    break;

                case 3:
                    System.out.println("\n--- 3. DEPOSIT ---");
                    System.out.print("Enter Beneficiary Account ID: ");
                    String depId = scanner.nextLine();
                    System.out.print("Enter deposit amount ($): ");
                    double depAmount = scanner.nextDouble();
                    scanner.nextLine(); 
                    System.out.print("Enter description: ");
                    String depDesc = scanner.nextLine();
                    
                    String depTxId = "TX-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
                    bankController.processDeposit(depId, depTxId, depAmount, depDesc);
                    break;

                case 4:
                    System.out.println("\n--- 4. WITHDRAW ---");
                    System.out.print("Enter Account ID: ");
                    String withId = scanner.nextLine();
                    System.out.print("Enter withdrawal amount ($): ");
                    double withAmount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    String withDesc = scanner.nextLine();
                    
                    String withTxId = "TX-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
                    bankController.processWithdraw(withId, withTxId, withAmount, withDesc);
                    break;

                case 5:
                    System.out.println("\n--- 5. TRANSFER ---");
                    System.out.print("Enter SENDER Account ID: ");
                    String senderId = scanner.nextLine();
                    System.out.print("Enter RECEIVER Account ID: ");
                    String receiverId = scanner.nextLine();
                    System.out.print("Enter transfer amount ($): ");
                    double transAmount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter description: ");
                    String transDesc = scanner.nextLine();
                    
                    String transTxId = "TX-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
                    bankController.processTransfer(senderId, receiverId, transTxId, transAmount, transDesc);
                    break;

                case 6:
                    System.out.println("\n--- 6. TRANSACTION HISTORY ---");
                    System.out.print("Enter Account ID: ");
                    String historyId = scanner.nextLine();
                    Account historyAcc = bankController.findAccount(historyId);
                    
                    if (historyAcc != null) {
                        System.out.println("\n[TRANSACTION LEDGER FOR " + historyAcc.getAccountName() + "]");
                        System.out.println("Current Balance: $" + historyAcc.getBalance());
                        System.out.println("---------------------------------------------------------");
                        historyAcc.displayHistory(); 
                        System.out.println("---------------------------------------------------------");
                    } else {
                        System.out.println("[FAILED] Account [" + historyId + "] not found.");
                    }
                    break;

                case 7:
                    System.out.println("\n--- 7. SAVE DATA TO FILE ---");
                    bankController.saveData(); 
                    break;

                case 8:
                    System.out.println("\n--- 8. SORTED MONTHLY FINANCIAL REPORT ---");
                    System.out.print("Enter Account ID: ");
                    String reportId = scanner.nextLine();
                    System.out.print("Enter Month (1-12): ");
                    int month = scanner.nextInt();
                    System.out.print("Enter Year (e.g., 2024): ");
                    int year = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    bankController.generateFinancialReport(reportId, month, year);
                    break;

                case 9:
                    System.out.println("\n--- 9. SEARCH TRANSACTION (LINEAR SEARCH) ---");
                    System.out.print("Enter Account ID: ");
                    String searchAccId = scanner.nextLine();
                    System.out.print("Enter Transaction ID to find (e.g., TX-A1B2C): ");
                    String searchTxId = scanner.nextLine();
                    
                    bankController.searchTransaction(searchAccId, searchTxId);
                    break;

                case 0:
                    System.out.println("\n[INFO] Saving data before exit...");
                    bankController.saveData(); 
                    System.out.println("Thank you for using the Digital Banking System. Goodbye!");
                    break;

                default:
                    System.out.println("[ERROR] Invalid choice! Please select an option from 0 to 9.");
                    break;
            }
        }
        scanner.close();
    }
}