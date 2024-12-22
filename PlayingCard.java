import java.util.*;

public class PlayingCard {
    
    private int number;
    private String face; 
    private String suit; 

    public PlayingCard() {
        Random r = new Random(); 
        this.number = r.nextInt(1,14);

        if(number == 1) {
            this.face = "Ace";
        } else if (number > 1 && number < 11){
            this.face = "" + number; 
        } else if (number == 11) {
            this.face = "Jack";
        } else if (number == 12) {
            this.face = "Queen";
        } else {
            this.face = "King";
        }

        if (number > 10){ // default face to be 10
            number = 10; 
        }

        if(number == 1){ // default ace to be 11 
            number = 11;
        }

        int suitNum = r.nextInt(1,5);

        if(suitNum == 1){
            this.suit = "Spades";
        } else if (suitNum == 2){
            this.suit = "Clubs";
        } else if(suitNum == 3){
            this.suit = "Hearts";
        } else if(suitNum == 4){
            this.suit = "Diamonds";
        }

    }

    public int getNumber(){
        return number;
    }

    public String getSuit() {
        return suit;
    }

    public String getFace(){
        return face; 
    }

    public void changeNumber(int number){
        this.number = number; 
    }
    
}