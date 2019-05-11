public class Color {
    int redValue;
    int greenValue;
    int blueValue;

    /**
     * The default constructor creates the color black
     */
    Color(){
        redValue = 0;
        greenValue = 0;
        blueValue = 0;

    }

    /**
     * Initializes a color with a set RGB value
     * @param red the value of red
     * @param green
     * @param blue
     */
    Color(int red, int green, int blue){
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    /**
     * Returns the red value of the color
     *
     * @return The redValue
     */
    public int getRed(){
        return redValue;
    }

    /**
     * Returns the green value of the color
     *
     * @return The greenValue
     */
    public int getGreen(){
        return greenValue;
    }

    /**
     * Returns the blue value of the color
     *
     * @return The blueValue
     */
    public int getBlue(){
        return blueValue;
    }

    /**
     *
     * @param newRed the new red value for the color. Automatically set it to be between
     */
    public void setRed(int newRed){
        if (newRed < 0){
            redValue = 0;

        }
        else if(newRed > 255){
            redValue  = 255;
        }
        else {
            redValue = newRed;
        }
    }

    public void setGreen(int newGreen) {
        if(newGreen < 0){
            greenValue = 0;
        }
        else if(newGreen >255) {
            greenValue = 255;
        }
        else {
            greenValue = newGreen;
        }
    }

    public void setBlue(int newBlue){
        if(newBlue < 0){
            blueValue = 0;
        }
        else if(newBlue >255) {
            blueValue = 255;
        }
        else {
            blueValue = newBlue;
        }
    }

    public void printColor(){
        System.out.print(this.getRed() + " " + this.getGreen() + " " + this.getBlue() + " ");
    }
}
