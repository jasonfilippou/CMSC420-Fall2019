package demos.bags.clients;

/**A simple abstraction for a colored ball.
 * @author jason
 *
 */
public class Ball {

    private Color color;

    /** Simple constructor
     * @param color The color of the ball
     *
     */
    public Ball(Color color){
        this.color = color;
    }


    public Color getColor(){
        return color;
    }

    public void bounce(){
        System.out.println(this + " bounces!");
    }

    @Override
    public String toString(){
        return color + " ball.";
    }
}
