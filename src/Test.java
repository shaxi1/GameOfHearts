public class Test {
    public static void main(String[] args) {
        Player[] players = new Player[] {
                new Player("Player 1", 0),
                new Player("Player 2", 1),
                new Player("Player 3", 2),
                new Player("Player 4", 3)
        };
        
        VirtualDealer dealer = new VirtualDealer();
        dealer.dealCards(players, -1);
        //dealer.dealCards(players, 2);

        System.out.println("test");
    }

}
