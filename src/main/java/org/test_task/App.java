package org.test_task;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try {
            EquationController.createConnection();
            EquationController.createStatement();
            EquationController.createTables();

            String option = "";
            while (!option.equals("q")) {
                System.out.println("Options: \n1 - Input an equation\n2 - Find an equation in the DB by value\n3 - Find an equations in the DB by root\n4 - View all equations and their roots\n5 - Delete all data\nanother symbol - exit\n-----------------------");
                System.out.print("Your option: ");
                Scanner scanner = new Scanner(System.in);
                option = scanner.nextLine();

                switch (option) {
                    case "1": {
                        System.out.print("Equation: ");
                        String equation = scanner.nextLine();

                        if (EquationController.validateEquation(equation)) {
                            EquationController.saveEquation(equation);
                            System.out.println("The equation is correct and saved in the DB.\n1 - Add a root of the equation\nanother symbol - exit\n-----------------------");
                            System.out.print("Your option: ");
                            String option2 = scanner.nextLine();

                            if (option2.equals("1")) {
                                System.out.print("Input a root: ");
                                int root = scanner.nextInt();

                                if (EquationController.checkAndSaveRoot(equation, root)) {
                                    System.out.println("This number is the root, so it's successfully saved to the DB");
                                } else {
                                    System.out.println("This number is not a root");
                                }
                            }

                        } else {
                            System.out.println("The equation is not correct, check the correctness of the entered expression");
                        }
                        break;
                    }
                    case "2": {
                        System.out.print("Equation: ");
                        String equation = scanner.nextLine();
                        if (EquationController.isEquationInDB(equation)) {
                            System.out.println("This equation is in DB\n1 - Add root of the equation\n2 - View roots of the equation\nAnother symbol - exit");
                            String option2 = scanner.nextLine();
                            switch (option2) {
                                case "1": {
                                    System.out.print("Input a root: ");
                                    double root = scanner.nextDouble();

                                    if (EquationController.checkAndSaveRoot(equation, root)) {
                                        System.out.println("This number is the root, so it's successfully saved to the DB");
                                    } else {
                                        System.out.println("This number is not a root");
                                    }
                                    break;
                                }
                                case "2":
                                    System.out.println("Roots: " + EquationController.getRootsByEquation(equation));
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            System.out.println("There is no such equation");
                        }
                        break;
                    }
                    case "3": {
                        System.out.print("Input a root: ");
                        double root = 0;
                        try {
                            root = scanner.nextDouble();
                        } catch (Exception e) {
                            System.out.println("Invalid root");
                            break;
                        }
                        List<String> equations = EquationController.getEquationsByRoot(root);
                        if (equations.isEmpty()) {
                            System.out.println("There are no equations with this root");
                        } else {
                            System.out.println("Equations: " + equations);
                        }
                        break;
                    }
                    case "4": {
                        List<String> list = EquationController.getEquationsAndRoots();
                        if (list.isEmpty()) {
                            System.out.println("There are no equations in the DB");
                        } else {
                            System.out.println(list);
                        }
                        break;
                    }
                    case "5": {
                        EquationController.deleteAll();
                        System.out.println("All data has been deleted");
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        } finally {
            EquationController.closeStatement();
            EquationController.closeConnection();
        }
    }
}

