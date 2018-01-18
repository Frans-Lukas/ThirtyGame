package frlo.umu.cs.se.thirty;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Frans-Lukas on 2018-01-18.
 */

public class ThirtyModel {
    private int diceRoll[] = new int[6];
    private int mRoundCount;
    private int mRerollCount;
    private int score;
    private final int MAX_ROUND_COUNT = 10;
    private final int MAX_REROLL_COUNT = 3;
    private int scoreMode = 4;

    private Random rand;

    public ThirtyModel() {
        mRoundCount = 0;
        mRerollCount = 0;
        rand = new Random();
        rollDice();
    }

    public boolean rollDice(){
        for (int i = 0; i < diceRoll.length; i++) {
            diceRoll[i] = rand.nextInt(6) + 1;
        }
        mRerollCount++;
        if(mRerollCount <= MAX_REROLL_COUNT) {
            return true;
        } else{
            return false;
        }
    }

    public boolean endRound(){
        if(mRoundCount == MAX_ROUND_COUNT){
            return false;
        }
        calculateScore();
        mRerollCount = 0;
        mRoundCount++;
        return true;
    }

    private void calculateScore(){
        if(scoreMode == 3){
            for (int dice : diceRoll) {
                if(dice <= 3){
                    score += dice;
                }
            }
        } else{
            ArrayList<Integer> tempDice = new ArrayList<>();
            for (int dice : diceRoll) {
                tempDice.add(dice);
            }
            sum_up_recursive(tempDice, scoreMode,new ArrayList<Integer>());
        }
    }

    /**
     * https://stackoverflow.com/questions/4632322/finding-all-possible-combinations-of-numbers-to-reach-a-given-sum
     * @param numbers
     * @param target
     * @param partial
     */
    private void sum_up_recursive(ArrayList<Integer> numbers, int target, ArrayList<Integer> partial) {
        int s = 0;
        for (int x: partial) s += x;
        if (s == target)
            score += s;
        if (s >= target)
            return;
        for(int i = 0; i < numbers.size(); i++) {
            ArrayList<Integer> remaining = new ArrayList<Integer>();
            int n = numbers.get(i);
            for (int j=i+1; j<numbers.size();j++) remaining.add(numbers.get(j));
            ArrayList<Integer> partial_rec = new ArrayList<Integer>(partial);
            partial_rec.add(n);
            sum_up_recursive(remaining,target,partial_rec);
        }
    }

    public void setScoreMode(int scoreMode) throws InvalidParameterException{
        if(scoreMode < 13 && scoreMode > 2){
            this.scoreMode = scoreMode;
        } else{
            throw new InvalidParameterException("scoreMode must be between 3 and 13.");
        }
    }

    public int getScore() {
        return score;
    }

    public int getRollsLeft() {
        return MAX_REROLL_COUNT - mRerollCount + 1;
    }

    public int getmRoundCount() {
        return mRoundCount;
    }

    public int[] getDiceRoll() {
        return diceRoll;
    }

    public boolean isGameDone(){
        return mRoundCount >= MAX_ROUND_COUNT;
    }
}
