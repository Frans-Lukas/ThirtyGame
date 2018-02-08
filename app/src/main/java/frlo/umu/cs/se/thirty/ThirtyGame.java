package frlo.umu.cs.se.thirty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThirtyGame extends AppCompatActivity {
    private static final int DISABLED_COLOR = Color.DKGRAY;
    private static final int ENABLED_COLOR = Color.WHITE;

    private static final String M_MODEL_KEY = "mModel";
    public static final int SCORE_MODE_LOW = 3;
    public static final int SCORE_MODE_FOUR = 4;
    public static final int SCORE_MODE_FIVE = 5;
    public static final int SCORE_MODE_SIX = 6;
    public static final int SCORE_MODE_SEVEN = 7;
    public static final int SCORE_MODE_EIGHT = 8;
    public static final int SCORE_MODE_NINE = 9;
    public static final int SCORE_MODE_TEN = 10;
    public static final int SCORE_MODE_ELEVEN = 11;
    public static final int SCORE_MODE_TWELVE = 12;
    public static final int TOTAL_NUMBER_OF_ROUNDS = 10;

    private List<DiceImage> mDiceImages = new ArrayList<>();
    private List<RadioButton> scoreChoices = new ArrayList<>();

    private TextView mRoundCountTextView;
    private TextView mRerollCountTextView;
    private TextView mScoreTextView;
    private Button mRollDiceBtn;
    private ThirtyModel mModel;
    private Drawable[] mDieFaces = new Drawable[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mModel = savedInstanceState.getParcelable(M_MODEL_KEY);
        } else{
            mModel = new ThirtyModel();
        }

        setContentView(R.layout.activity_thirty_game);

        setUpDiceImages();
        setUpTextViews();
        setUpRadioGroup();
        setUpDiceImageButtons();
        setUpRollDiceButton();

    }

    private void setUpTextViews() {
        mScoreTextView = findViewById(R.id.scoreTextView);
        mRoundCountTextView = findViewById(R.id.roundNumberTextView);
        mRerollCountTextView = findViewById(R.id.rerollCountTextView);

        mScoreTextView.setText(getString(R.string.score) + mModel.getScore());
        mRoundCountTextView.setText(getString(R.string.roundsLeft) + (TOTAL_NUMBER_OF_ROUNDS - mModel.getmRoundCount()));
        mRerollCountTextView.setText(getString(R.string.rerollsLeft) + (mModel.getRollsLeft()));
    }

    /**
     * If a image is clicked, toggle if it is rollable.
     */
    class DiceImageListener implements View.OnClickListener{
        private DiceImage linkedDiceImage;
        private int indexInModel = 0;

        public DiceImageListener(DiceImage linkedDiceImage, int indexInModel) {
            this.linkedDiceImage = linkedDiceImage;
            this.indexInModel = indexInModel;
        }

        //Disable or enable dice buttons.
        @Override
        public void onClick(View view) {
            if(linkedDiceImage.isRollable()){
                linkedDiceImage.getmDiceImage().setBackgroundColor(DISABLED_COLOR);
                mModel.lockDice(indexInModel);
            } else{
                linkedDiceImage.getmDiceImage().setBackgroundColor(ENABLED_COLOR);
                mModel.unlockDice(indexInModel);
            }
            linkedDiceImage.setRollable(!linkedDiceImage.isRollable());
        }
    }

    /**
     * Roll all the enabled dice and update the dice images accordingly.
     */
    class RollDice implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            int index = 0;
            int value;
            mModel.rollDice();
            boolean canClickAgain = mModel.isCanRollAgain();

            //Update images.
            for (DiceImage mDiceImage : mDiceImages) {
                if (mDiceImage.isRollable()) {
                    value = mModel.getDiceRoll()[index] - 1;
                    mDiceImage.getmDiceImage().setImageDrawable(mDieFaces[value]);
                }
                index++;
            }

            //Update reroll count
            mRerollCountTextView.setText(getString(R.string.rerollsLeft) + mModel.getRollsLeft());
            if(!canClickAgain){
                view.setEnabled(false);
            }
        }
    }

    private void setUpDiceImages(){
        mDieFaces[0] = getResources().getDrawable(R.drawable.die_face_1_t);
        mDieFaces[1] = getResources().getDrawable(R.drawable.die_face_2_t);
        mDieFaces[2] = getResources().getDrawable(R.drawable.die_face_3_t);
        mDieFaces[3] = getResources().getDrawable(R.drawable.die_face_4_t);
        mDieFaces[4] = getResources().getDrawable(R.drawable.die_face_5_t);
        mDieFaces[5] = getResources().getDrawable(R.drawable.die_face_6_t);
    }

    /***
     * End round and if it is the final round go to score screen.
     */
    class EndRound implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            resetDiceImages();
            mModel.endRound();
            mRollDiceBtn.callOnClick();
            mRollDiceBtn.setEnabled(true);

            //change score, reroll and round left text.
            mRerollCountTextView.setText(getString(R.string.rerollsLeft) + mModel.getRollsLeft());
            mRoundCountTextView.setText(getString(R.string.roundsLeft )+ (TOTAL_NUMBER_OF_ROUNDS - mModel.getmRoundCount()));
            mScoreTextView.setText(getString(R.string.score )+ mModel.getScore());

            //check if Game is done, if so: return to start screen with score.
            if(mModel.isGameDone()){
                Toast.makeText(ThirtyGame.this,
                        getString(R.string.yourScore) + mModel.getScore(),
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("model", mModel);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            //disable current scorechoice button.
            if(mModel.getAvailableScoreMode() != -1) {
                 scoreChoices.get(mModel.getAvailableScoreMode()).performClick();
            }
            for (int i = 0; i < scoreChoices.size(); i++) {
                if(mModel.isDisabledScoreChoice(i)){
                    scoreChoices.get(i).setEnabled(false);
                }
            }
        }
    }

    private void resetDiceImages() {
        //If dice is rollabe set it as enabled.
        for (DiceImage mDiceImage : mDiceImages) {
            if(!mDiceImage.isRollable()){
                mDiceImage.getmDiceImage().setBackgroundColor(ENABLED_COLOR);
                mDiceImage.setRollable(!mDiceImage.isRollable());
            }
        }

    }

    private void setUpDiceImageButtons(){
        mDiceImages.add(new DiceImage((ImageButton) findViewById(R.id.diceImageButton1)));
        mDiceImages.add(new DiceImage((ImageButton) findViewById(R.id.diceImageButton2)));
        mDiceImages.add(new DiceImage((ImageButton) findViewById(R.id.diceImageButton3)));
        mDiceImages.add(new DiceImage((ImageButton) findViewById(R.id.diceImageButton4)));
        mDiceImages.add(new DiceImage((ImageButton) findViewById(R.id.diceImageButton5)));
        mDiceImages.add(new DiceImage((ImageButton) findViewById(R.id.diceImageButton6)));
        int index = 0;

        //Change dice images to display if enabled or not.
        for (DiceImage mDiceImage : mDiceImages) {
            mDiceImage.getmDiceImage().setBackgroundColor(ENABLED_COLOR);
            mDiceImage.getmDiceImage().setOnClickListener(new DiceImageListener(mDiceImage, index));
            mDiceImage.getmDiceImage().setImageDrawable(mDieFaces[mModel.getDiceRoll()[index] - 1]);
            if(mModel.isDiceLocked(index)){
                mDiceImage.setRollable(false);
                mDiceImage.getmDiceImage().setBackgroundColor(DISABLED_COLOR);
            }
            index++;
        }
    }

    private void setUpRollDiceButton(){
        Button mEndRoundButton = findViewById(R.id.calculateScoreButton);
        mEndRoundButton.setText(getString(R.string.endRound));
        mEndRoundButton.setOnClickListener(new EndRound());

        mRollDiceBtn = findViewById(R.id.rollDiceButton);
        mRollDiceBtn.setText(getString(R.string.rollDice));
        mRollDiceBtn.setOnClickListener(new RollDice());

        //If we can roll again, enable the dice button.
        if(!mModel.isCanRollAgain()){
            mRollDiceBtn.setEnabled(false);
        }
    }


    private void setUpRadioGroup(){
        setUpScoreChoices();

        //Start with low button checked.
        RadioButton lowButton = scoreChoices.get(mModel.getAvailableScoreMode());
        lowButton.performClick();
    }

    /**
     * Class for radioButtons to change the current scoremode.
     */
    class radioButtonListener implements View.OnClickListener{

        //Change the score mode
        @Override
        public void onClick(View view) {
            RadioButton rb = (RadioButton) view;
            String scoreMode = rb.getText().toString();
            setScoreMode(scoreMode);
        }
    }

    /**
     * Add all radio buttons to score choicecs list and set their text.
     */
    private void setUpScoreChoices() {
        //add to list
        scoreChoices.add(findViewById(R.id.lowRadioButton));
        scoreChoices.add(findViewById(R.id.fourRadioButton));
        scoreChoices.add(findViewById(R.id.fiveRadioButton));
        scoreChoices.add(findViewById(R.id.sixRadioButton));
        scoreChoices.add(findViewById(R.id.sevenRadioButton));
        scoreChoices.add(findViewById(R.id.eightRadioButton));
        scoreChoices.add(findViewById(R.id.nineRadioButton));
        scoreChoices.add(findViewById(R.id.tenRadioButton));
        scoreChoices.add(findViewById(R.id.elevenRadioButton));
        scoreChoices.add(findViewById(R.id.twelveRadioButton));

        //Set text
        scoreChoices.get(0).setText(getString(R.string.low));
        scoreChoices.get(1).setText(getString(R.string.four));
        scoreChoices.get(2).setText(getString(R.string.five));
        scoreChoices.get(3).setText(getString(R.string.six));
        scoreChoices.get(4).setText(getString(R.string.seven));
        scoreChoices.get(5).setText(getString(R.string.eight));
        scoreChoices.get(6).setText(getString(R.string.nine));
        scoreChoices.get(7).setText(getString(R.string.ten));
        scoreChoices.get(8).setText(getString(R.string.eleven));
        scoreChoices.get(9).setText(getString(R.string.twelve));

        for (RadioButton scoreChoice : scoreChoices) {
            scoreChoice.setOnClickListener(new radioButtonListener());
        }

        for (int i = 0; i < scoreChoices.size(); i++) {
            if(mModel.isDisabledScoreChoice(i)){
                scoreChoices.get(i).setEnabled(false);
            }
        }
    }


    private void setScoreMode(String scoreMode){
        System.out.println("Changing score mode: " + scoreMode + "Value: " + SCORE_MODE_FOUR);
        switch(scoreMode){
            case "Low":
                mModel.setScoreMode(SCORE_MODE_LOW);
                break;
            case "Four":
                mModel.setScoreMode(SCORE_MODE_FOUR);
                break;
            case "Five":
                mModel.setScoreMode(SCORE_MODE_FIVE);
                break;
            case "Six":
                mModel.setScoreMode(SCORE_MODE_SIX);
                break;
            case "Seven":
                mModel.setScoreMode(SCORE_MODE_SEVEN);
                break;
            case "Eight":
                mModel.setScoreMode(SCORE_MODE_EIGHT);
                break;
            case "Nine":
                mModel.setScoreMode(SCORE_MODE_NINE);
                break;
            case "Ten":
                mModel.setScoreMode(SCORE_MODE_TEN);
                break;
            case "Eleven":
                mModel.setScoreMode(SCORE_MODE_ELEVEN);
                break;
            case "Twelve":
                mModel.setScoreMode(SCORE_MODE_TWELVE);
                break;
            default:
                mModel.setScoreMode(SCORE_MODE_LOW);
                break;
        }
    }


    /**
     * On reset, remember parcable with all the model data.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(M_MODEL_KEY, mModel);
    }
}
