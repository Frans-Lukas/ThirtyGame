package frlo.umu.cs.se.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
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
    private final int MAX_ROUND_COUNT = 10;
    private final int MAX_REROLL_COUNT = 3;
    private int scores[] = new int[MAX_ROUND_COUNT];
    private int scoreMode = 4;
    private boolean usedScoreModeChoice[] = new boolean[10];


    private Random rand;

    public ThirtyModel() {
        mRoundCount = 0;
        mRerollCount = 0;
        rand = new Random();
        rollDice();
    }

    /**
     * If the model is destroyed it can be reset in this constructor.
     * @param in
     */
    public ThirtyModel(Parcel in) {
        in.readIntArray(diceRoll);
        in.readIntArray(scores);
        in.readBooleanArray(usedScoreModeChoice);
        in.readBooleanArray(lockedDice);
        canRollAgain = in.readInt() != 0;
        mRoundCount = in.readInt();
        mRerollCount = in.readInt();
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

        //Disallow player from rolling when reached max rolls.
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
        //Calculate round score.
        calculateScore();

        //make sure user cant use same score mode again.
        usedScoreModeChoice[scoreMode - 3] = true;

        mRerollCount = 0;
        mRoundCount++;
        return true;
    }

    /**
     * Calculates the score depending on what score mode is selected.
     */
    public void calculateScore(){
        //If scoremode is three, select all dice that is three or lower.
        if(scoreMode == 3){
            for (int dice : diceRoll) {
                if(dice <= 3){
                    scores[mRoundCount] += dice;
                }
            }
        } else {
            /*Create templist for all the dice
            * So that the smartSumSelect method can remove and add dice.
            */
            ArrayList<Integer> tempDice = new ArrayList<>();
            for (int dice : diceRoll) {
                //Dont add dice that are higher than the selected mode.
                if(dice <= scoreMode) {
                    tempDice.add(dice);
                }
            }
            //Sort list in descending order.
            Collections.sort(tempDice, (dice1, dice2) -> dice2 - dice1);
            //calculate score.
            smartSumSelect(tempDice, 0, scoreMode);
        }

    }

    /**
     * This method chooses the most amount of numbers to make up the target value by
     * first selecting the value closest to the target value. After selecting the
     * value closest to the target, find the new value that makes the sum closer to the target.
     * If the sum is equal to the target, go back and make the sum zero.
     *
     * @param numbers A list of numbers sorted from biggest to smallest where no value should
     *                be bigger than the target.
     * @param sum the current sum of targets added.
     * @param target the target for the sum to reach.
     */
    private void smartSumSelect(ArrayList<Integer> numbers, int sum, int target) {

        if(sum == target){
            scores[mRoundCount] += sum;
            return;
        }

        for (int i = 0; i < numbers.size(); i++) {

            if(sum + numbers.get(i) <= target){
                sum += numbers.get(i);
                numbers.remove(i);
                smartSumSelect(numbers, sum, target);
                i = -1;
                sum = 0;
            }
        }
    }

    public void setDiceRoll(int[] diceRoll) {
        this.diceRoll = diceRoll;
    }

    public void setScoreMode(int scoreMode) throws InvalidParameterException{
        //Make sure illegal score modes can not be set.
        if(scoreMode < 13 && scoreMode > 2 && !usedScoreModeChoice[scoreMode - 3]){
            this.scoreMode = scoreMode;
        } else{
            throw new InvalidParameterException("scoreMode must be between 3 and 13 and not have " +
                    "been used before");
        }
    }

    /**
     * Lock the selected dice.
     * @param index the index of the dice to lock.
     */
    public void lockDice(int index){
        lockedDice[index] = true;
        }

    /**
     * Unlock the selected dice for rolling
     * @param index the index of the dice to unlock.
     */
    public void unlockDice(int index){
        lockedDice[index] = false;
    }

    /**
     * Iterate all scores and add them to a score int.
     * @return return the score
     */
    public int getScore() {
        int score = 0;
        for (int i : scores) {
            score += i;
        }
        return score;
    }

    /**
     * Return the first available score mode.
     * @return the index of an available score mode, or -1 if none.
     */
    public int getAvailableScoreMode(){
        for (int i = 0; i < usedScoreModeChoice.length; i++) {
            if(usedScoreModeChoice[i] == false){
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the score choice is disabled or not
     * @param index the index of the score choice.
     * @return
     */
    public boolean isDisabledScoreChoice(int index){
        return usedScoreModeChoice[index];
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

    /**
     * Make sure program is parcable, i.e. save all the data.
     */
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
     * Save data to parcel.
     * @param dest
     * @param i
     */
    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeIntArray(diceRoll);
        dest.writeIntArray(scores);
        dest.writeBooleanArray(usedScoreModeChoice);
        dest.writeBooleanArray(lockedDice);
        dest.writeInt(canRollAgain ? 1 : 0);
        dest.writeInt(mRoundCount);
        dest.writeInt(mRerollCount);
        dest.writeInt(scoreMode);
    }
}
