/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MainPackage;
import Jama.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 *  Joseph Zhong
 *  ASSIGNMENT_NUMBER
 *  PROGRAM_DESCRIPTION
 *  PROGRAM_TITLE
 *  DATE
 *
 **/

public class mainMethods
{
    private static Matrix XMatrix;
    private static Matrix YMatrix;
    private static Matrix InverseMatrix;

    private static double[] xInputValues;
    private static double[] yInputValues;

    private static Matrix CoefficientMatrix;
    private static int d;

    public static void main(String[] args) throws FileNotFoundException
    {
        //File input = new File("C:/Users/Joseph/Downloads/GitHub/PolynomialRegression5/src/MainPackage/bestfit_dataset_example.txt");

        // Set up reader
        Scanner scn = new Scanner(new File("C:/Users/Joseph/Downloads/GitHub/PolynomialRegression5/src/MainPackage/bestfit_dataset_example.txt"));
        Scanner userScn = new Scanner(System.in);
        // Set up containers
        /*
        double xValues[];
        double yValues[];
        * */
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();

        // X and Y Value input
        while(scn.hasNextDouble())
        {
            xValues.add(scn.nextDouble());
            yValues.add(scn.nextDouble());

            scn.nextLine();
        }

        // d Input sanitation
        d = 0;
        while(!(d <= 4 && d >= 1))
        {
            input(userScn);
        }

        // Populate X
        /*
        double[][] asdf = populateXMatrix(xValues); // populated correctedly
        for(int c = 0; c <= asdf.length; c++)
        {
            for(int r = 0; r <= asdf.length; r++)
            {
                System.out.print(asdf[r][c]);
            }
            System.out.println();
        }*/
        XMatrix = new Matrix(populateXMatrix(xValues));

        // Populate Y
        /*
        double[] asdf = populateYMatrix(yValues);
        for(int r = 0; r < asdf.length; r++)
        {
            System.out.print(asdf[r]);
        }
        System.out.println();
        * */
        YMatrix = new Matrix(populateYMatrix(yValues), d + 1);

        // Inverse XMatrix
        InverseMatrix = XMatrix.inverse();

        // Multiply YMatrix by InverseMatrix
        Matrix ProductMatrix = YMatrix.times(InverseMatrix);
        //InverseMatrix.times(YMatrix);
        int a = 97;
        for(int i = 0; i < d + 1; i++)
        {
            System.out.print((char) (a + i) + " = " + ProductMatrix.get(0, i));
        }
    }

    /**
     *
     * @param userScn
     */
    private static void input(Scanner userScn)
    {
        System.out.print("Degree value of polynomial? (1-4): ");

        if(!userScn.hasNextInt())
        {
            userScn.next();
            inputError();
        }
        else if((d = userScn.nextInt()) > 4 || d < 1)
        {
            inputError();
        }
        else
        {
            //d = userScn.nextInt();
            System.out.println("Your polynomial degree is: " + d);
        }
        /*
        while(!userScn.hasNextInt())
        {
            inputError();
        }*/

    }

    private static void inputError()
    {
        System.out.println("Please enter an integer value from 1 to 4.");
        //input(userScn);
    }

    private static double[][] populateXMatrix(ArrayList<Double> _xValues)
    {
        xInputValues = new double[_xValues.size()];
        Iterator<Double> iterator = _xValues.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            xInputValues[i] = iterator.next().doubleValue();
            i++;
        }

        double[][] XArray = new double[d+1][d+1];
        for(int c = 0; c < XArray.length; c++)
        {
            for(int r = 0; r < XArray[c].length; r++)
            {
                XArray[c][r] = SumX(2*d-r-c, xInputValues);
                //XArray[r][c] = SumX(2*d-r-c, _xValues);
            }
        }
        return XArray;
    }

    private static double SumX(int degree, double[] _x)
    {
        double sum = 0.;
        for(int i = 0; i < _x.length; i++)
        {
            sum += Math.pow(_x[i], degree);
        }
        return sum;
    }

    private static double[] populateYMatrix(ArrayList<Double> _yValues)
    {
        yInputValues = new double[_yValues.size()];
        Iterator<Double> iterator = _yValues.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            yInputValues[i] = iterator.next().doubleValue();
            i++;
        }

        double[] YArray = new double[d+1];
        for(int r = 0; r < YArray.length; r++)
        {
            YArray[r] = SumY(d-r, yInputValues, xInputValues);
            //XArray[r][c] = SumX(2*d-r-c, _xValues);
        }
        return YArray;
    }

    private static double SumY(int degree, double[] _y, double[] _x)
    {
        double sum = 0.;
        for(int i = 0; i < _y.length; i++)
        {
            sum += _y[i] * Math.pow(_x[i], degree);
        }
        return sum;
    }

    private static void MultiplyYbyInverse()
    {

    }
}
