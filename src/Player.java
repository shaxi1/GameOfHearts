import java.util.ArrayList;
import java.util.List;

public class Player {
    public String name;
    /*  indexes go as follows:
            2
           1 3
            0
    */
    public int playerIndex;
    public int score;

    public List<Card> hand;
    Boolean starts;

    public Player(String name, int playerIndex) {
        this.name = name;
        this.score = 0;
        this.playerIndex = playerIndex;

        this.hand = new ArrayList<>();
    }
}
