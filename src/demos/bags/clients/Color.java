package demos.bags.clients;

/** A simple enum for colors.
 * @author jason
 * @since  v1.1
 */
public enum Color {
    BLUE, RED, GREEN, YELLOW, WHITE, BLACK, CYAN, MAGENTA, GRAY, PINK, PURPLE;

    @Override
    public String toString(){
        switch(this.name()){
            case "Color.BLUE":
                return "Blue";
            case "Color.RED":
                return "Red";
            case "Color.GREEN":
                return "Green";
            case "Color.YELLOW":
                return "Yellow";
            case "Color.WHITE":
                return "White";
            case "Color.BLACK":
                return "Black";
            case "Color.CYAN":
                return "Cyan";
            case "Color.MAGENTA":
                return "Magenta";
            case "Color.GRAY":
                return "Gray";
            case "Color.PINK":
                return "Pink";
            case "Color.PURPLE":
                return "Purple";
            default:
                return "Unknown";
        }
    }
}
