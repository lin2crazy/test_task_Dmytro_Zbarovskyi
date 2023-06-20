package org.test_task;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class EquationController {
    public static boolean validateEquation(String equation) {
        final String regex1 = "[(]*-?[()]*(?:[()]*\\d*[()]*\\.?\\d+[()]*|[()]*x[()]*)(?:[()]*([-+*/])[()]*-?[()]*(?:[()]*\\d*[()]*\\.?\\d+[()]*|[()]*x[()]*))*[)]*=\\d+.?\\d*[^x]";
        final String regex2 = "\\d+.?\\d*[^x]=[(]*-?[()]*(?:[()]*\\d*[()]*\\.?\\d+[()]*|[()]*x[()]*)(?:[()]*([-+*/])[()]*-?[()]*(?:[()]*\\d*[()]*\\.?\\d+[()]*|[()]*x[()]*))*[)]*";
        return (equation.matches(regex1) || equation.matches(regex2)) && validateBrackets(equation) && equation.contains("x");
    }

    public static boolean validateBrackets(String equation) {
        Deque<Character> stack = new LinkedList<>();

        for (char c : equation.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static boolean checkAndSaveRoot(String equation, double root) {
        double result = 0;
        String expression = "";
        if (equation.substring(equation.indexOf("=") + 1).matches("\\d+.?\\d*")) {
            result = Double.parseDouble(equation.substring(equation.indexOf("=") + 1));
            expression = equation.substring(0, equation.indexOf("="));
        } else {
            result = Double.parseDouble(equation.substring(0, equation.indexOf("=")));
            expression = equation.substring(equation.indexOf("=") + 1);
        }

        Expression e = new ExpressionBuilder(expression)
                .variables("x")
                .build()
                .setVariable("x", root);

        if (result - e.evaluate() >= 10e-9) {
            return false;
        }

        saveRoot(equation, root);
        return true;
    }

    public static void saveEquation(String equation) {
        try {
            DBController.insertIntoTableEquations(equation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveRoot(String equation, double root) {
        try {
            int id = DBController.getEquationIdByValue(equation);
            if (id != 0) {
                DBController.insertIntoTableEquationRoots(id, root);
            } else {
                System.out.println("There is no such equation in the DB");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getEquationsByRoot(double root) {
        try {
            return DBController.getEquationsByRoot(root);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getRootsByEquation(String equation) {
        try {
            return DBController.getRootsByEquation(equation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getEquationsAndRoots() {
        try {
            return DBController.getEquationsAndRoots();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEquationInDB(String equation) {
        try {
            DBController.createTableEquationRoots();
            boolean result = false;
            int id = DBController.getEquationIdByValue(equation);
            if (id != 0) {
                result = true;
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAll() {
        try {
            DBController.deleteTablesEquationsAndRoots();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTables() {
        try {
            DBController.createTableEquations();
            DBController.createTableEquationRoots();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createConnection() {
        try {
            DBController.createConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createStatement() {
        try {
            DBController.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        try {
            DBController.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeStatement() {
        try {
            DBController.closeStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
