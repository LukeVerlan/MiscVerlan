import java.util.*;

public class BlackJack {
    public static void main(String[] args){

        Scanner console = new Scanner(System.in);
        System.out.print("What is your table buy-in? "); 
        int[] money = new int[1]; 
        money[0] = console.nextInt();
        double startMoney = money[0]; 
        console.nextLine();
        int isPlay = 1;
        int handNumber = 0; 
        ArrayList<PlayingCard> player = new ArrayList<>();      
        ArrayList<PlayingCard> dealer = new ArrayList<>();  

        double finishMoney = 0; 
        

        while(isPlay == 1 && money[0] > 0 ) {
            handNumber++;
            int bet = setupHand(player, dealer, money, handNumber, console);
            money[0] -= bet; 
            playGame(player, dealer, money, console, bet);

            player.clear();
            dealer.clear();

            finishMoney = money[0];

            System.out.println("Current cash: " + money[0]); 
            System.out.println();

            System.out.print("Would you like to play again? (1 for yes, 2 for no) ");
            isPlay = console.nextInt();
        }   

        System.out.println("See you next time! \n");
        System.out.println("Your final cash was $" + money[0] + " Thats %" + ((finishMoney*100)/startMoney) 
                + " of what you started with!");

    }      
    
    public static int setupHand(ArrayList<PlayingCard> player, ArrayList<PlayingCard> dealer, int[] money, int handNumber, Scanner console) {
            
            int bet = 0; 

            player.add(new PlayingCard()); // Player Cards 
            player.add(new PlayingCard());

            dealer.add(new PlayingCard()); // Dealer Cards
            dealer.add(new PlayingCard());

            System.out.print("Place your bet: ");
            bet = console.nextInt();
            System.out.println();
            while (money[0] - bet < 0 || bet < 1){
                System.out.println("insufficient bet");
                System.out.print("re-enter bet: ");
                bet = console.nextInt();
            }
            System.out.println("Dealing hand " + handNumber);
            System.out.println("Your cards:"); 
            printHand(player,1);
            System.out.println("Dealers Upcard: ");
            System.out.println("  " + dealer.get(0).getFace()); 
            System.out.println();

            return bet;
    } 

    public static void playGame(ArrayList<PlayingCard> player, 
    ArrayList<PlayingCard> dealer, int[] money, Scanner console, int bet) {
        int splitFlag = 0;
        int choice = 0; 
        int bflag = 0;

        if(player.get(0).getNumber() + player.get(1).getNumber() == 21){
            PlayerWinState(money, 3, bet);
        } else if (player.get(0).getNumber() == player.get(1).getNumber() ) {
            System.out.println("What would you like to do? \n");
            System.out.print("1 for hit, 2 for stand, 3 for double down, 4 to split: "); 
            choice = console.nextInt();
            while(choice < 1 || choice > 4){
                System.out.println("Invalid Input, please re-enter");
                choice = console.nextInt();
            }

            if(choice == 4){ 
                money[0] -= bet;
                int[] numSplitHands = new int[1]; 
                numSplitHands[0] = 1; 
                ArrayList<ArrayList<PlayingCard>> splitStack = new ArrayList<ArrayList<PlayingCard>>();
                splitStack.add(player);
                int[] keepCount = new int[1];
                keepCount[0] = 0;  
                playHand(player, dealer, bet, money, choice, console, splitStack, keepCount, numSplitHands);
                splitFlag = 1; 
               
            }

        } else {
            System.out.println("What would you like to do? \n");
            System.out.print("1 for hit, 2 for stand, 3 for double down: ");
            choice = console.nextInt(); 
            while(choice < 1 || choice > 3 ){
                System.out.println("Invalid Input, please re-enter");
                choice = console.nextInt();
            } 

        }   

        if(choice == 3 || choice == 1){
            bet = playHand(player, dealer, bet, money, choice, console, null, null, null); 
        }        

        if(calculateTotal(player)== 21){
            bflag = 1;
        }
        
        if(splitFlag != 1){
            dealerPlays(player, dealer, money, bet, bflag, console, 0);
        }
        
    }

    public static void dealerPlays(ArrayList<PlayingCard> player, ArrayList<PlayingCard> dealer, 
    int[] money, int bet, int bflag, Scanner console, int splitflag) {

        if (splitflag == 0){
            System.out.println("Dealer Plays...");
            System.out.println();
            playHand(dealer, null, bet, money, 5, console, null, null, null);
            printHand(dealer,1);
        }

        if(bflag == 0){
            if(calculateTotal(player) > 21) {
                PlayerWinState(money, 1, bet);
            } else if(calculateTotal(dealer) > 21) {
                PlayerWinState(money, 7, bet);
            } else if(calculateTotal(player) > calculateTotal(dealer)){
                PlayerWinState(money, 2, bet);
            } else if (calculateTotal(player) == calculateTotal(dealer)){
                PlayerWinState(money, 4, bet);
            } else {
                PlayerWinState(money, 6, bet);
            }
        }
    }

    // Choice 5 - Dealer hit action 
    // Choice 4 - split action 
    // choice 3 - double down action 
    // choicd 1 - hit action 
    public static int playHand(ArrayList<PlayingCard> hand, ArrayList<PlayingCard> dealer,
      int bet, int[] money, int choice, Scanner console, ArrayList<ArrayList<PlayingCard>> splitStack, int[] keepCount, int[] numSplitHands) {
        if(choice == 5){

            if(calculateTotal(hand) == 21){
                System.out.println("Dealer BlackJack"); 
            }

            while(calculateTotal(hand) < 17 ) {
                hand.add(new PlayingCard());
                manageAces(hand);
            } 


        } else if (choice == 4) {
            
            handleSplit(hand, splitStack, bet, console, money, dealer, keepCount, numSplitHands);


        } else if (choice == 3) {

            if(money[0] >= bet){
                money[0] -= bet;
                bet = bet * 2;
                hand.add(new PlayingCard());
                printHand(hand,1);
                manageAces(hand);

                if (calculateTotal(hand) == 21){
                    PlayerWinState(money, 5, bet);  
                }
            }

        } else {

            int hit = 1; 
            System.out.println();
            while(hit == 1){
                hand.add(new PlayingCard());    
                printHand(hand,1);
                manageAces(hand);
                if(calculateTotal(hand) > 21) {
                    break; 
                } else if (calculateTotal(hand) == 21){
                    PlayerWinState(money, 5, bet); 
                    break; 
                }

                System.out.print("Hit again? (1 for yes, 2 for no) ");
                hit = console.nextInt(); 
                System.out.println();
            }
        }

        return bet;
    }

    public static void handleSplit (ArrayList<PlayingCard> hand, ArrayList<ArrayList<PlayingCard>> splitStack, int bet,
    Scanner console, int[] money, ArrayList<PlayingCard> dealer, int[] keepCount, int[] numSplitHands){
        int choice = 0;
        int currSize = 1;
        if (splitStack.size() != 0){
            currSize = splitStack.size();
        } 

        for (int i = 0; i < currSize; i++){
            if (splitStack.get(i).size() == 2){
                if (splitStack.get(i).get(0).getNumber() == splitStack.get(i).get(1).getNumber()){
                    PlayingCard temp = splitStack.get(i).get(1);
                    ArrayList<PlayingCard> tempList = new ArrayList<PlayingCard>();
                    tempList.add(temp);
                    splitStack.add(tempList);
                    splitStack.get(i).remove(1);
                    numSplitHands[0]++; 
                }
            }
        }


        for (int i = 0; i < splitStack.size(); i++){
            System.out.print("Hand " + (i + 1) + ":");
            printHand(splitStack.get(i), 0);
            System.out.print("   ");
        }
        System.out.println("\n");
        int originalbet = bet; 

        System.out.println("Hitting... \n ");

        for (int i = 0; i < splitStack.size(); i++){
            if (splitStack.get(i).size() == 1){
                splitStack.get(i).add(new PlayingCard()); 
                if(calculateTotal(splitStack.get(i)) == 21){
                    PlayerWinState(money, 5, bet);
                } else {
                    if(splitStack.size() < 5){
                        if (splitStack.get(i).get(0).getNumber() == splitStack.get(i).get(1).getNumber() && numSplitHands[0] < 4 ){
                            System.out.println("What would you like to do for hand " + (i + 1) + "? " 
                                + "(1 for hit, 2 for stand, 4 for split) ");
                            printHand(splitStack.get(i), 1);
                            choice = console.nextInt();
                            System.out.println();
                            while(choice != 1 || choice != 2 || choice != 4){
                                System.out.println("Invalid Input, please re-enter");
                                choice = console.nextInt();
                            } 
                        } else {
                            System.out.println("What would you like to do for hand " + (i + 1) + "?");
                            printHand(splitStack.get(i), 1);
                           System.out.print("(1 for hit, 2 for stand) ");
                            choice = console.nextInt();
                            System.out.println();
                            while(choice < 1 || choice > 2 ){
                                System.out.println("Invalid Input, please re-enter");
                                choice = console.nextInt();
                            } 
                        }   
                        if(choice !=2){
                            playHand(splitStack.get(i), dealer, bet, money, choice, console, splitStack, keepCount, numSplitHands);
                        }
                    }
                }
            }
       }

        
        if(keepCount[0] == 0){
            for (int i = 0; i < splitStack.size(); i++) {
                if (i == 0){
                    dealerPlays(splitStack.get(i), dealer, money, bet, 1, console, 0);
                }
                System.out.println("hand "  + (i + 1) + ":");
                if(calculateTotal(splitStack.get(i)) != 21){
                    dealerPlays(splitStack.get(i), dealer, money, bet, 0, console, 1);
                } else {
                    System.out.println("21!");
                }
                System.out.println();
            }
            keepCount[0]++;
        }    
    }

    public static void printHand(ArrayList<PlayingCard> hand, int space){
        for(int i = 0; i < hand.size(); i++){
            System.out.print("  " + hand.get(i).getFace());
        }

        if(space == 1){
            System.out.println("\n");
        }
    }

    public static void manageAces (ArrayList<PlayingCard> hand){

        for(int i = 0; i < hand.size(); i++){ 
            if(calculateTotal(hand) > 21){
                if(hand.get(i).getNumber() == 11){
                    hand.get(i).changeNumber(1);
                }
            }
        }
    }

    public static int numAces(ArrayList<PlayingCard> hand){
        int numAces = 0; 

        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i).getNumber() == 11){
                numAces++; 
            }
        }

        return numAces; 
    }

    public static int calculateTotal (ArrayList<PlayingCard> hand) {

        int total = 0;

        for(int i = 0; i < hand.size(); i++){
            total += hand.get(i).getNumber(); 
        }

        return total; 
    }
    
    // Win Conditions
    // Condition 7 - dealer bust 
    // Condiiton 6 - dealer win
    // Condition 5 - 21
    // Condition 4 - push
    // Condition 3 - blackjack 
    // Condition 2 - beat dealer 
    // Condition 1 - bust 
    public static void PlayerWinState (int[] money, int winCondition, int bet) {

        if(winCondition == 3){
            System.out.println("BlackJack! 3 to 1 Instant Payout"); 
            money[0] += bet * 3; 

        } else if (winCondition == 2) {
            System.out.println("Player Wins!");
            money[0] += bet * 2;
                
        } else if(winCondition == 5){
            System.out.println("21! 2 to 1 Instant Payout");
            money[0] += bet * 2; 

        } else if(winCondition == 6){
            System.out.println("Dealer Win.");

        } else if(winCondition == 4) {
            System.out.println("Push.");
            money[0] += bet;

        } else if(winCondition == 7) {
            System.out.println("Dealer Bust!");
            money[0] += bet * 2;

        } else { 
            System.out.println("Player Bust.");

        }   

        System.out.println();
        
    }
}