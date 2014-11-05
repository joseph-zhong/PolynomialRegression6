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
 *  @author Joseph Zhong
 *  Polynomial Regression
 *  This program provides the coefficient values for the best fit line
 *  Polynomial Regression Solver
 *  4 Nov. 2014
 *
 **/

public class mainMethods
{
    /**
     * Maximum degree (^143...before something something infinity and NaN shows
     * up everywhere...)
     */
    private static final int MaxDegree = 143;

    /**
     * Minimum degree (linear)
     */
    private static final int MinDegree = 1;

    /**
     * X Matrix with the sum of X Values that represent the partial derivatives
     * for each coefficient
     */
    private static Matrix XMatrix;

    /**
     * Y Matrix with the sum of Y Values that represent the output of X*k, where
     * k represents the matrix of coefficients to be found that leads to the
     * best fit line
     */
    private static Matrix YMatrix;

    /**
     * Inverse Matrix that can be used to solve the coefficients
     * Eventually will become the inverse of the X Matrix, and can be multiplied
     * by Y to find the coefficients
     * <b>NOT IMPLEMENTED<b>
     */
    private static Matrix InverseMatrix;

    /**
     * Coefficient matrix that represents the coefficients of the best fit line
     * that minimizes deviation distance
     */
    private static Matrix CoefficientMatrix;

    /**
     * Input array of the X values from the text file
     */
    private static double[] xInputValues;

    /**
     * Input array of the Y values from the text file
     */
    private static double[] yInputValues;

    /**
     * Intended Degree value input by the user
     */
    private static int d;

    /**
     * String of FilePath URL to take x and y value input from
     * I split it to make it neater on two lines, but also because /GitHub/ won't
     *  change for me...
     * Might not necessarily be the case for other users however (Hi, Mr. Kresser)
     */
    private static final String InputFilePath = "C:/Users/Joseph/Downloads/GitHub/"
            + "PolynomialRegression6/src/MainPackage/bestfit_dataset_example.txt";

    /**
     * Main entry.
     *
     * Basic Logical Progression as follows: <br>
     *  Take Input (Self-implemented Sanitation) <br>
     *  Build XMatrix (Math library used along with self-implemented algorithm) <br>
     *  Build YMatrix (Math library used along with self-implemented algorithm) <br>
     *  Solve for Coefficients (Matrix library used) <br>
     *  Return Coefficients (Self-implemented algorithm) <br>
     *
     * <h4> taking input </h4>
     *  Input includes the values taken from the text file as x and y values.
     *  Input also requires User input for the d-value. This is sanitized.
     *
     * <h4> building the X matrix </h4>
     *  When you solve for the coefficients by hand using the partial derivatives
     *  a specific pattern can be found in the construction of the X Matrix:
     *      <br>First, there is a specific order in which the x values line up: Decending.
     *      <br>Second, one can see that the degree of the X values corresponds directly
     *          to what the user selected d value is
     *      <br>Thirdly, rather than using a completely double for-loop system to
     *          build the array, one can take advantage of the fact that the matrix
     *          is symmetrical, and thus you only need to calculate half the values
     *
     * <h4> building the Y matrix </h4>
     *  Building the Y matrix is much simpler than the X Matrix, it is not
     *      multidimensional, and the correlation of values and d-value is less tricky
     *
     * <h4> solving for coefficients </h4>
     *  Thank you JAMA.
     *
     * <h4> returning coefficients </h4>
     *  I actually developed a pretty efficient algorithm to dynamically return
     *      all the values. It involves dynamically changing the ascii value and
     *      simultaneously returning the values from the ProductMatrix
     *
     * @param args CMD Commands
     * @throws FileNotFoundException to catch the input text file not found
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        // Set up readers
        // File Reader
        Scanner scn = new Scanner(new File(InputFilePath));
        Scanner userScn = new Scanner(System.in);
        // Set up containers
        /*
        double xValues[];
        double yValues[];
        * I can never predict the input file... Although I'd like to use arrays,
        * ArrayLists are always easy easy easy.
        * */
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();

        // X and Y Value input from text file
        while(scn.hasNextDouble())
        {
            xValues.add(scn.nextDouble());
            yValues.add(scn.nextDouble());

            scn.nextLine(); // read next line of values
        }

        // d Input sanitation
        d = 0;
        while(!(/*d <= 4 && */d >= 1))
        {
            input(userScn);
        }

        // Populate X
        /*
        double[][] test = populateXMatrix(xValues); // populated correctedly
        for(int c = 0; c < test.length; c++)
        {
            for(int r = 0; r < test.length; r++)
            {
                System.out.print(test[r][c]);
            }
            System.out.println();
        }
        * // Uncomment to test input of XMatrix... I keep getting an index out
        *   of bounds, but regardless, the building of the matrix is correct
        *   The For-loop just keeps going even though it's clearly not supposed to
        */
        XMatrix = new Matrix(populateXMatrix(xValues));

        // Populate Y
        /*
        double[] test2 = populateYMatrix(yValues);
        for(int r = 0; r < test2.length; r++)
        {
            System.out.print(test2[r]);
        }
        System.out.println();
        * */
        YMatrix = new Matrix(populateYMatrix(yValues), d + 1);

        /*
        // Inverse XMatrix      // DOES NOT WORK
        InverseMatrix = XMatrix.inverse();

        // Multiply YMatrix by InverseMatrix // DOES NOT WORK
        Matrix ProductMatrix = YMatrix.times(InverseMatrix);
        //InverseMatrix.times(YMatrix);
        int a = 97;
        for(int i = 0; i < d + 1; i++)
        {
            System.out.print((char) (a + i) + " = " + ProductMatrix.get(0, i));
        }
        * */

        // Solve for Coefficients
        // This is pathetic, but extremely efficient. I have the Source if you
        // want to see...
        // SOLVES IN SPECIFIED ORDER: X*K = Y and returns K
        Matrix SolutionMatrix = XMatrix.solve(YMatrix);

        // Return Coefficients
        // I dynamically change the coefficient returned based on the index
        // Essentially I have a program that has a maximum solving capacity of 255
        // degrees if you wish... But you're limiting till 4 :P
        for(int i = 0; i < d + 1; i++)
        {
            System.out.print((char)((int)'a' + i) + " = " + SolutionMatrix.get(i, 0) + " ");
        }

        // Graph Results
        // TODO: IMPLEMENT LATER
    }

    /**
     * Input method that both sanitizes and saves input.
     * @param userScn Scanner to read System.in
     */
    private static void input(Scanner userScn)
    {
        // Prompt
        //System.out.print("Degree value of polynomial? (1-4): ");
        Prompt();

        // Sanitation logic... .next() should catch all
        if(!userScn.hasNextInt())
        {
            userScn.next();
            Prompt();
        }
        else if((d = userScn.nextInt()) > MaxDegree || d < MinDegree) // super efficient
        {
            Prompt();
        }
        else // exits with valid input
        {
            System.out.println("Your polynomial degree is: " + d);
        }
    }

    /**
     * Input Error Message
     */
    private static void Prompt()
    {
        System.out.println("Please enter an integer value from " + MinDegree + " to " + MaxDegree + ":");
    }

    /**
     * Populate X Matrix Method.
     * First, takes the input Double x values and converts them to double <br>
     * Secondly, each value is calculated by a sums helper method <br>
     * Thirdly, subsequent values continue based on pattern
     * @param _xValues ArrayList of Double values from the text file
     * @return double[][] to build a Matrix object
     */
    private static double[][] populateXMatrix(ArrayList<Double> _xValues)
    {
        // convert from Double to double
        xInputValues = new double[_xValues.size()];
        Iterator<Double> iterator = _xValues.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            xInputValues[i] = iterator.next().doubleValue();
            i++;
        }

        // double[][] to use to construct Matrix Object
        double[][] XArray = new double[d+1][d+1];
        for(int c = 0; c < XArray.length; c++)
        {
            for(int r = c; r < XArray[c].length; r++) // Using r = c doubles efficiency
            {
                XArray[r][c] = XArray[c][r] = SumX(2*d-r-c, xInputValues);
            }
        }
        return XArray;
    }

    /**
     * Sigma helper method.
     * @param degree Degree order to scale the values
     * @param _x array of values to sum
     * @return double value to put into the matrix
     */
    private static double SumX(int degree, double[] _x)
    {
        double sum = 0.;
        for(int i = 0; i < _x.length; i++)
        {
            sum += Math.pow(_x[i], degree);
        }
        return sum;
    }

    /**
     * Populate Y matrix.
     * First, convert the Double values to double <br>
     * Second, use a sigma helper method to calculate the values needed <br>
     * Third, subsequent values progress linearly
     * @param _yValues
     * @return
     */
    private static double[] populateYMatrix(ArrayList<Double> _yValues)
    {
        // Convert from Double to double
        yInputValues = new double[_yValues.size()];
        Iterator<Double> iterator = _yValues.iterator();
        int i = 0;
        while(iterator.hasNext())
        {
            yInputValues[i] = iterator.next().doubleValue();
            i++;
        }

        // double[] to use to make Matrix Object
        double[] YArray = new double[d+1];
        for(int r = 0; r < YArray.length; r++)
        {
            YArray[r] = SumY(d-r, yInputValues, xInputValues);
            // X degree is based on d-r
        }
        return YArray;
    }

    /**
     * Sigma Y helper method.
     * Calculates needed value to populate the Y Matrix
     * @param degree to scale X Value with
     * @param _y values of Y to multiply with scaled x values
     * @param _x values of X to scale multiply with y
     * @return double value to populate Y Matrix
     */
    private static double SumY(int degree, double[] _y, double[] _x)
    {
        double sum = 0.;
        for(int i = 0; i < _y.length; i++)
        {
            sum += _y[i] * Math.pow(_x[i], degree);
        }
        return sum;
    }
}
