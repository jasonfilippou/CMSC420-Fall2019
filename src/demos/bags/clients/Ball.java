package demos.bags.clients;

/**A simple abstraction for a colored ball.
 * @author jason
 * @since 1.1
 */
public class Ball {

    private Color color;

    /** Simple constructor
     * @param color The color of the ball
     * @since 1.1
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
