package com.monetware.demo.xml;

import java.util.ArrayList;
import java.util.Arrays;

public class SummaryStatCalculator{

    /**
     * according to data set of specific line, we can calculate 8 characteristic statistic values and return them
     * @param line
     * @param height
     * @param data
     * @return
     */
    public static double[] calculateSummaryStatistics(int line, int height, ArrayList<ArrayList<String>> data){
        //sample data
        Double[] x = new Double[height - 1];
        int i = 1;

        //convert a set of data from String type into double one
        for(; i < height; i++){
            try{
                x[i - 1] = Double.parseDouble(data.get(i).get(line));
            }catch (Exception e){
                //The catch & exception mechanism still have some problems:
                //if we find problems with data, we assign them with default value: 0.0
                x[i - 1] = 0.0;
                x[i] = 0.0;
                i++;
            }finally {
                continue;
            }
        }

        //8 characteristic statistic value stored in a double array
        //They are : ("mean", "medn", "mode", "vald", "invd", "min", "max", "stdev")
        // namely：(average，medium number，data type，valid number，invalid number，minimum，maximum，standard deviation)
        double[] nx = new double[8];

        //count invalid number
        int invalid = countInvalidValues(x);
        nx[4] = invalid;
        nx[3] = x.length - invalid;

        //remove invalid numbers
        double[] newx = prepareForSummaryStatsAlternative(x, x.length - invalid);

        //calculate mean
        nx[0] = calculateMean(newx);
        //calculate medium number
        nx[1] = calculateMedian(newx);
        //模式为0.0
        nx[2] = 0.0;
        //get minimum number
        nx[5] = getMin(newx);
        //get maximum number
        nx[6] = getMax(newx);
        //get standard deviation
        nx[7] = Math.sqrt(evaluate(newx));
        return nx;
    }

    //the first function for standard deviation calculation
    public static double evaluate(double[] values) {
        if (values == null) {
            System.out.println("ERROR!");
            return 0.0;
        } else {
            return evaluate(values, 0, values.length);
        }
    }

    //the first function for standard deviation calculation
    public static double evaluate(double[] values, int begin, int length) {
        double var = 0.0D / 0.0;
        //take some errors out of account
        if (!(values == null || begin < 0 || length < 0 || begin + length > values.length) && length != 0) {
            if (length == 1) {
                var = 0.0D;
            } else if (length > 1) {
                double m = ev(values, begin, length);
                var = ev2(values, m, begin, length);
            }
        }

        return var;
    }

    private static double ev(double[] values, int begin, int length) {
        if ((values == null || begin < 0 || length < 0 || begin + length > values.length) || length == 1) {
            return 0.0D / 0.0;
        } else {
            double sampleSize = (double)length;
            double xbar = deal(values, begin, length) / sampleSize;
            double correction = 0.0D;

            for(int i = begin; i < begin + length; ++i) {
                correction += values[i] - xbar;
            }

            return xbar + correction / sampleSize;
        }
    }

    private static double ev2(double[] values, double mean, int begin, int length) {
        double var = 0.0D / 0.0;
        if (!(values == null || begin < 0 || length < 0 || begin + length > values.length) && length != 0) {
            if (length == 1) {
                var = 0.0D;
            } else if (length > 1) {
                double accum = 0.0D;
                double dev = 0.0D;
                double accum2 = 0.0D;

                for(int i = begin; i < begin + length; ++i) {
                    dev = values[i] - mean;
                    accum += dev * dev;
                    accum2 += dev;
                }

                double len = (double)length;
//                if (isBiasCorrected) {
                    var = (accum - accum2 * accum2 / len) / (len - 1.0D);
//                } else {
//                var = (accum - accum2 * accum2 / len) / len;
//                }
            }
        }

        return var;
    }

    private static double deal(double[] values, int begin, int length) {
        double sum = 0.0D / 0.0;
        if (!(values == null || begin < 0 || length < 0 || begin + length > values.length) && length != 0) {
            sum = 0.0D;

            for(int i = begin; i < begin + length; ++i) {
                sum += values[i];
            }
        }

        return sum;
    }


    /**
     * Now we know how many numbers are valid (param length), we can remove those invalid ones from the data set
     * this function remove them and return the data set of valid ones
     * @param x
     * @param length
     * @return
     */
    private static double[] prepareForSummaryStatsAlternative(Double[] x, int length) {
        double[] retvector = new double[length];

        int c = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] != null) {
                double xvalue = x[i];
                if (!Double.isNaN(xvalue)) {
                    retvector[c++] = xvalue;
                }
            }
        }
        return retvector;
    }

    /**
     * Returns a new double array of nulls and non-Double.NaN values only
     *
     */
    // TODO:
    // implement this in some way that does not require allocating a new
    // ArrayList for the values of every vector. -- L.A. Aug. 11 2014
//    private variable double[] removeInvalidValues(Double[] x){
//        List<Double> dl = new ArrayList<Double>();
//        for (Double d : x){
//            if (d != null && !Double.isNaN(d)){
//                dl.add(d);
//            }
//        }
//        return ArrayUtils.toPrimitive(dl.toArray(new Double[dl.size()]));
//    }

    /**
     * Returns the number of Double.NaNs (or nulls) in a double-type array
     *
     */
    //calculate the invalid number
    private static int countInvalidValues(Double[] x){
        int counter=0;
        for (int i=0; i<x.length;i++){
            ////if ( x[i] == null || x[i].equals(Double.NaN) ) {
            //判断是不是数字值
            if (x == null || Double.isNaN(x[i])) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Returns the number of Double.NaNs in a double-type array
     *
     * TODO: figure out if this is actually necessary - to count NaNs and
     * nulls separately;
     *  -- L.A. 4.0 alpha 1
     */
//    private variable int countNaNs(double[] x){
//        int NaNcounter=0;
//        for (int i=0; i<x.length;i++){
//            if (Double.isNaN(x[i])){
//                NaNcounter++;
//            }
//        }
//        return NaNcounter;
//    }

    /**calculate medium number
     * @param values
     * @return
     */
    private static double calculateMedian(double[] values) {
        double[] sorted = new double[values.length];
        System.arraycopy(values, 0, sorted, 0, values.length);
        Arrays.sort(sorted);

        if (sorted.length == 0) {
            return Double.NaN;
        }
        if (sorted.length == 1) {
            return sorted[0]; // always return single value for n = 1
        }
        double n = sorted.length;
        double pos = (n + 1) / 2;
        double fpos = Math.floor(pos);
        int intPos = (int) fpos;
        double dif = pos - fpos;

        double lower = sorted[intPos - 1];
        double upper = sorted[intPos];

        return lower + dif * (upper - lower);
    }

    /**calculate mean of the data set
     *
     * @param values
     * @return
     */
    private static double calculateMean(double[] values) {
        int begin = 0, length = values.length;
        if (values == null || length == 0) {
            return Double.NaN;
        }

        double sampleSize = length;

        // Compute initial estimate using definitional formula
        double xbar = calculateSum(values) / sampleSize;

        // Compute correction factor in second pass
        double correction = 0;
        for (int i = begin; i < begin + length; i++) {
            correction += values[i] - xbar;
        }
        return xbar + (correction / sampleSize);
    }

    /**calculate sum
     * @param values
     * @return
     */
    private static double calculateSum(double[] values) {
        int begin = 0, length = values.length;
        if (values == null || length == 0) {
            return Double.NaN;
        }
        double sum = 0.0;
        for (int i = begin; i < begin + length; i++) {
            sum += values[i];
        }
        return sum;
    }

    /**calculate minimum
     * @param var
     * @return
     */
    private static double getMin(double[] var){
        double temp = var[0];
        for(double a : var){
            if(a < temp)
                temp = a;
        }
        return temp;
    }

    /**calculate maximum
     * @param var
     * @return
     */
    private static double getMax(double[] var){
        double temp = var[0];
        for(double a : var){
            if(a > temp)
                temp = a;
        }
        return temp;
    }
}
