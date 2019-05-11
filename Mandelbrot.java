/**
 * This program creates a PPM P6 image (https://en.wikipedia.org/wiki/Netpbm_format) of a certain area of the mandelbrot set.
 * PPM images are not very space efficient; Images above a certain number of pixels will be very large in size. Early versions
 * of this program were using text documents (P3) instead of binary (P6), and file sizes were much larger.
 *
 * This requires the class Color.java for functions relating to getting and defining colors in the mandelbrot image.
 *
 *
 *
 *
 * Jonathan Roos
 */

import java.io.*;
public class Mandelbrot {
    public static void  main(String[] args) throws java.io.IOException{
        /**
         * Set up for the mandelbrot generation.
         *
         * Width is how wide the image will be in pixels.
         * Height is how tall an image will be in pixels.
         *
         * xMin is the lower bound of the image along the x-axis(real axis).
         * xMax is the upper bound of the image along the x-axis(real axis).
         *
         * yMin is the lower bound of the image along the y-axis(imaginary axis).
         * yMax is the upper bound of the image along the y-axis(imaginary axis).
         *
         * maxIterations is the maximum number of times that the
         *
         * ----------Locations Reference---------
         * Full Mandelbrot: xMin: -1.5, xMax: 0.5, yMin: -1, yMax: 1
         * Seahorse: xMin: -0.75, xMax: -0.74, yMin: 0.1, yMax: 0.11
         * Spiral(kinda lame imo): xMin: -0.748, xMax:-0.746, yMin:0.105 yMax:0.11
         *
         */
        int width = 10000;  // width of the PPM image
        int height = 10000; // height of the PPM image
        double xMin = -0.749; // The minimum value along the x-axis (real axis) of the mandelbrot set to be viewed.
        double xMax = -0.745; // The maximum value along the x-axis (real axis) of the mandelbrot set to be viewed.
        double yMin = 0.105; // The minimum value along the y-axis (imaginary axis) of the mandelbrot set to be viewed.
        double yMax = 0.109; // The maximum value along the y-axis (imaginary axis) of the mandelbrot set to be viewed.
        final int maxIterations = 500; // The number of iterations
        final String imageName = "mandelbrot"; // The name of the file that will be created. DO NOT ADD FILE EXTENSION.

        /**
         * This is a list of colors to choose from. Some may or not be used. This is just for easily changing what colors
         * show up in the image without retyping the RGB values each time.
         */
        Color white = new Color (255, 255, 255);
        Color pink = new Color (238, 73, 232);
        Color red =new Color(255,0,0);
        Color maroon = new Color(87, 12, 33);
        Color orange = new Color (255, 69, 3); // This is not a perfect orange.
        Color yellow = new Color (255, 201, 32);
        Color green = new Color (0, 255, 100);
        Color blue = new Color (6, 41, 160);
        Color darkBlue = new Color (9, 4, 71);
        Color purple = new Color (60, 2, 73);
        Color black = new Color (0, 0, 0);

        Color aStart = yellow;
        Color aEnd = orange;

        Color bStart = blue;
        Color bEnd = green;

        Color cStart = white;
        Color cEnd = purple;

        Color dStart = black;
        Color dEnd = white;

        double pixelWidth = (xMax - xMin) / width;
        double pixelHeight = (yMax - yMin) / width;
        DataOutputStream output =  new DataOutputStream(new BufferedOutputStream( new FileOutputStream(imageName + ".ppm"), 4096));
        long startTime = System.currentTimeMillis();// used to track how long the image takes to generate.

        output.writeBytes("P6 "+ width + " " + height + " " + "255 ");
        for (int yPixel = 0; yPixel < height; yPixel++) {
            System.out.println("Y: " + yPixel);
            for(int xPixel = 0; xPixel < height; xPixel++){
                //System.out.println("X: " + xPixel + ", Y: "+ yPixel);
                double x0 = xMin + (xPixel * pixelWidth);
                double y0 = yMin + (yPixel * pixelHeight);
                int currentIteration = iterateMandelbrot(0.0, 0.0, x0, y0, 0, maxIterations);
                output.write(PrintPixelColor(aStart.getRed(), aEnd.getRed(), bStart.getRed(), bEnd.getRed(), cStart.getRed(), cEnd.getRed(), dStart.getRed(), dEnd.getRed(), currentIteration, maxIterations));
                output.write(PrintPixelColor(aStart.getGreen(), aEnd.getGreen(), bStart.getGreen(), bEnd.getGreen(),cStart.getGreen(),cEnd.getGreen() ,dStart.getGreen(), dEnd.getGreen() , currentIteration, maxIterations));
                output.write(PrintPixelColor(aStart.getBlue(), aEnd.getBlue(), bStart.getBlue(), bEnd.getBlue(),cStart.getBlue(), cEnd.getBlue(), dStart.getBlue(), dEnd.getBlue() , currentIteration, maxIterations));
            }
        }
        output.close();
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        printTime(timeElapsed);

    }

    /**
     * Print pixel color is used to find correct color for the pixel. This current version of PrintPixelColor divides the
     * number of iterations into four sections, allowing there to be four different color blocks.
     * This method divides the number of iterations up equally between all four color sections. This approach does not
     * take into account the fact that some numbers of iterations are more common, and this results in a few color blocks
     * being used more in some areas. Using a histogram to choose the colors would be a better approach to have a more even
     * color distribution, but I couldn't be bothered to do this.
     *
     * @param aStart The starting color for the first color block.
     * @param aEnd The end color for the first color block.
     * @param bStart The starting color for the second color block.
     * @param bEnd the end color for the second color block.
     * @param cStart The starting color for the third color block.
     * @param cEnd The end color for the third color block.
     * @param dStart The starting color for the fourth/final color block.
     * @param dEnd The end color for the fourth/final color block.
     * @param currentIteration The number of iterations it took for the pixel to escape.
     * @param maxIterations The max number of iterations allowed by the program.
     * @return returns a value either the Red Green or Blue part of a pixel based upon the number of iterations it took to escape.
     */
    public static int PrintPixelColor(int aStart, int aEnd, int bStart, int bEnd, int cStart, int cEnd, int dStart, int dEnd, int currentIteration, int maxIterations){
        if(currentIteration < maxIterations*0.25) {
            return getValue(aStart, aEnd, currentIteration, maxIterations/4);
        }
        else if (currentIteration < maxIterations*0.5) {
            return getValue(bStart, bEnd, currentIteration-(int)(0.25*maxIterations), maxIterations/4);
        }
        else if (currentIteration < maxIterations*0.75) {
            return getValue(cStart, cEnd, currentIteration-(int)(0.5*maxIterations), maxIterations/4);
        }
        else {
            return getValue(dStart, dEnd, currentIteration-(int)(0.75*maxIterations), maxIterations/4);
        }

    }

    /**
     * Takes in a value for a color and then finds the average color based on its percentage of iterations to maxiIterations
     *
     * @param valueAOne Either the R G or B value for the first color
     * @param valueATwo Either the R  G or B value for the second color.
     * @param iteration The number of iterations that it took for the image to escape
     * @param maxIterations Blah blah blah
     * @return
     */
    public static int getValue(int valueAOne, int valueATwo, int iteration, int maxIterations){
        return (int)(((((double)valueATwo - valueAOne)/(maxIterations)) * iteration) + valueAOne);
    }

    /**
     * Recursively finds how many iterations it takes for the pixel to escape. Uses the escape time algorithm.
     * https://en.wikipedia.org/wiki/Mandelbrot_set#Escape_time_algorithm
     *
     * @param x current real value of the pixel.
     * @param y current imaginary value of the pixel.
     * @param x0 The x coordinate of the pixel.
     * @param y0 The y coordinate of hte pixel.
     * @param iterations The number of iterations that have occurred.
     * @param maxIterations The
     * @return If the pixel escapes, it will return the number of iterations. Otherwise iterate mandelbrot is recursively
     * called until the pixel value escapes.
     */
    public static int iterateMandelbrot(double x, double y, double x0, double y0,int iterations, int maxIterations){
        double xSquared = x * x;
        double ySquared = y * y;
        if((xSquared + ySquared) >= 4 ||(iterations > maxIterations)) {
            return iterations;
        }
        return iterateMandelbrot(xSquared - ySquared + x0, 2*x*y + y0, x0, y0,iterations + 1, maxIterations);
    }

    /**
     * Prints a time given in milliseconds in minutes.
     *
     * @param Timeothy Time in milliseconds.
     */
    public static void printTime(long Timeothy) {
        double seconds = Timeothy/1000;
        int minutes = (int)(seconds/60);
        System.out.println("Time in milliseconds: " + Timeothy + " milliseconds");
        System.out.println("Time in seconds: " + seconds + " seconds");
        System.out.print("Time in minutes: " +  minutes);
        if (minutes == 1) {
            System.out.print(" minute");
        }
        else {
            System.out.print(" minutes");
        }
        System.out.println(" and " + (seconds % 60) + " seconds");
    }
}

