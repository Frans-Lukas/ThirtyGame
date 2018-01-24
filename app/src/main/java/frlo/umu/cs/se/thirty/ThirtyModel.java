package frlo.umu.cs.se.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Frans-Lukas on 2018-01-18.
 */

public class ThirtyModel implements Parcelable{
    private int diceRoll[] = new int[6];
    private boolean lockedDice[] = new boolean[6];
    private boolean canRollAgain = true;
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
        for (boolean dice : lockedDice) {
            dice = false;
        }
        rollDice();
    }

    /**
     *
     * @param in
     */
    public ThirtyModel(Parcel in) {
        in.readIntArray(diceRoll);
        in.readBooleanArray(lockedDice);
        canRollAgain = in.readInt() != 0;
        mRoundCount = in.readInt();
        mRerollCount = in.readInt();
        score = in.readInt();
        scoreMode = in.readInt();
    }


    public void rollDice(){
        for (int i = 0; i < diceRoll.length; i++) {
            diceRoll[i] = rand.nextInt(6) + 1;
        }
        mRerollCount++;
        if(mRerollCount <= MAX_REROLL_COUNT) {
            canRollAgain =  true;
        } else{
            canRollAgain = false;
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

    public void lockDice(int index){
        lockedDice[index] = true;
        }

    public void unlockDice(int index){
        lockedDice[index] = false;
    }

    public int getScore() {
        return score;
    }

    public boolean isCanRollAgain() {
        return canRollAgain;
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

    public boolean isDiceLocked(int index){
        return lockedDice[index];
    }

    public boolean isGameDone(){
        return mRoundCount >= MAX_ROUND_COUNT;
    }

    public static final Parcelable.Creator<ThirtyModel> CREATOR
            = new Parcelable.Creator<ThirtyModel>() {
        public ThirtyModel createFromParcel(Parcel in) {
            return new ThirtyModel(in);
        }

        public ThirtyModel[] newArray(int size) {
            return new ThirtyModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param dest
     * @param i
     */

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeIntArray(diceRoll);
        dest.writeBooleanArray(lockedDice);
        dest.writeInt(canRollAgain ? 1 : 0);
        dest.writeInt(mRoundCount);
        dest.writeInt(mRerollCount);
        dest.writeInt(score);
        dest.writeInt(scoreMode);
    }
}
