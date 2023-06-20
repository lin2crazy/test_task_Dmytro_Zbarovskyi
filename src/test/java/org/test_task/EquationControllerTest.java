package org.test_task;

import org.junit.Assert;
import org.junit.Test;

public class EquationControllerTest {

    @Test
    public void validateEquationsTestValidParams() {
        String[] pass = new String[]{
                "2*x+-5=17",
                "17=-2*x+-5",
                "-1.3*5/x=1.2",
                "2.232*x=10",
                "2*x+5+x+5=10",
                "17=2*-x+-5",
                "(-2)*x+-5=17",
                "(-1.3*5)/x=1.2",
                "(2*x)=10",
                "((2*x)+5)+x+5=10",
                "17=(2*-x)+(-5)",
                "2+((-3.5*x))*4=14"
        };

        for (String s : pass) {
            Assert.assertTrue("Expected equations must pass the validation", EquationController.validateEquation(s));
        }
    }

    @Test
    public void validateEquationsTestInvalidParams() {
        String[] fail = new String[]{
                "*2*x+-5=17",
                "17x=-2*x+-5",
                "-1.3*5/x==1.2",
                "2.2.32*x=10",
                "2*x+5*+x+5=10",
                "17*x=2*-x+--5",
                "(-2))*x+-5=17",
                "(-1.3*5)/x=(1.2)",
                "(2*x)=10))",
                "))2*x(+5(+x+5=10"
        };

        for (String s : fail) {
            Assert.assertFalse("Expected equations mustn't pass the validation", EquationController.validateEquation(s));
        }
    }


    @Test
    public void checkAndSaveRootTest() {
        String[] equations = new String[]{
                "2*x+5=9", //2
                "15=2*x-5", //10
                "-1.5*5/x=1.5", //-5
                "2.33*x=10", //4.29185
                "2*x+5+x+5=10", //0
                "(-2)*x+-5=17", //-11
                "((2*x)+5)+x+5=10", //0
                "17=(2*-(x+5))", //-13.5
        };

        double[] roots = new double[]{2, 10, -5, 4.29185, 0, -11, 0, -13.5};

        EquationController.createConnection();
        EquationController.createStatement();

        for (int i = 0; i < equations.length; i++) {
            Assert.assertTrue(EquationController.checkAndSaveRoot(equations[i], roots[i]));
        }

        EquationController.closeConnection();
        EquationController.closeStatement();
    }

}
