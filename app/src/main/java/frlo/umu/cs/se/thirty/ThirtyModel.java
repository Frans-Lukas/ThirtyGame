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
     * If the model is destroyed it can be reset in this constructor.
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


    /**
     * Rolls all the dice and sees if this is the final roll.
     */
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

    /**
     * Ends the current round and increases the roundcounter by 1.
     * @return if this is not the last round.
     */
    public boolean endRound(){
        if(mRoundCount == MAX_ROUND_COUNT){
            return false;
        }
        calculateScore();
        mRerollCount = 0;
        mRoundCount++;
        return true;
    }

    /**
     * Calculates the score depending on what score mode is selected.
     */
    public void calculateScore(){
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

            for (int i = 0; i < tempDice.size(); i++) {
                int value = tempDice.get(i);
                if(value == scoreMode){
                    score += value;
                    tempDice.remove(i);
                    i--;
                }
            }
            if(tempDice.size() >= 2){
                for (int i = 0; i < tempDice.size(); i++) {
                    for (int j = i + 1; j < tempDice.size(); j++) {
                        int value = tempDice.get(i);
                        value += tempDice.get(j);
                        if(value == scoreMode){
                            score += value;
                            tempDice.remove(i);
                            tempDice.remove(j - 1);
                            i--;
                            break;
                        }
                    }
                }
            }
            if(tempDice.size() >= 3){
                for (int i = 0; i < tempDice.size(); i++) {
                    for (int j = i + 1; j < tempDice.size(); j++) {
                        for (int k = j + 1; k < tempDice.size(); k++) {
                            int value = tempDice.get(i);
                            value += tempDice.get(j);
                            value += tempDice.get(k);

                            if(value == scoreMode){
                                score += value;
                                tempDice.remove(i);
                                tempDice.remove(j - 1);
                                tempDice.remove(k - 2);
                                i--;
                                break;
                            }

                        }
                    }
                }
            }
            if(tempDice.size() >= 4){
                for (int i = 0; i < tempDice.size(); i++) {
                    for (int j = i + 1; j < tempDice.size(); j++) {
                        for (int k = j + 1; k < tempDice.size(); k++) {
                            for (int l = k + 1; l < tempDice.size(); l++) {

                                int value = tempDice.get(i);
                                value += tempDice.get(j);
                                value += tempDice.get(k);
                                value += tempDice.get(l);

                                if(value == scoreMode){
                                    score += value;
                                    tempDice.remove(i);
                                    tempDice.remove(j - 1);
                                    tempDice.remove(k - 2);
                                    tempDice.remove(l - 3);
                                    i--;
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            if(tempDice.size() >= 5){
                for (int i = 0; i < tempDice.size(); i++) {
                    for (int j = i + 1; j < tempDice.size(); j++) {
                        for (int k = j + 1; k < tempDice.size(); k++) {
                            for (int l = k + 1; l < tempDice.size(); l++) {
                                for (int m = l + 1; m < tempDice.size(); m++) {
                                    int value = tempDice.get(i);
                                    value += tempDice.get(j);
                                    value += tempDice.get(k);
                                    value += tempDice.get(l);
                                    value += tempDice.get(m);

                                    if(value == scoreMode){
                                        score += value;
                                        tempDice.remove(i);
                                        tempDice.remove(j - 1);
                                        tempDice.remove(k - 2);
                                        tempDice.remove(l - 3);
                                        tempDice.remove(m - 4);
                                        i--;
                                        break;
                                    }
                                }
                            }

                        }
                    }
                }
            }

            if(tempDice.size() >= 6){
                for (int i = 0; i < tempDice.size(); i++) {
                    for (int j = i + 1; j < tempDice.size(); j++) {
                        for (int k = j + 1; k < tempDice.size(); k++) {
                            for (int l = k + 1; l < tempDice.size(); l++) {
                                for (int m = l + 1; m < tempDice.size(); m++) {
                                    for (int n = m + 1; n < tempDice.size(); n++) {
                                        int value = tempDice.get(i);
                                        value += tempDice.get(j);
                                        value += tempDice.get(k);
                                        value += tempDice.get(l);
                                        value += tempDice.get(m);
                                        value += tempDice.get(n);

                                        if(value == scoreMode){
                                            score += value;
                                            tempDice.remove(i);
                                            tempDice.remove(j - 1);
                                            tempDice.remove(k - 2);
                                            tempDice.remove(l - 3);
                                            tempDice.remove(m - 4);
                                            tempDice.remove(n - 5);
                                            i--;
                                            break;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * https://stackoverflow.com/questions/4632322/finding-all-possible-combinations-of-numbers-to-reach-a-given-sum
     * @param numbers
     */
    private void sum_up_recursive(ArrayList<Integer> numbers) {

    }

    public void setDiceRoll(int[] diceRoll) {
        this.diceRoll = diceRoll;
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
