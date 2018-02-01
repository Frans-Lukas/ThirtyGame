package frlo.umu.cs.se.thirty;

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

    private List<DiceImage> mDiceImages = new ArrayList<>();
    private TextView mRoundCountTextView;
    private TextView mRerollCountTextView;
    private TextView mScoreTextView;
    private Button mRollDiceBtn;
    private Button mEndRoundButton;
    private RadioGroup mScoringSystemRadioGroup;
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

        mScoreTextView.setText("Score: " + mModel.getScore());
        mRoundCountTextView.setText("Rounds left: " + (10 - mModel.getmRoundCount()));
        mRerollCountTextView.setText("Rerolls left: " + (mModel.getRollsLeft()));
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
            mRerollCountTextView.setText("Rerolls left: " + mModel.getRollsLeft());
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
            mRerollCountTextView.setText("Rerolls left: " + mModel.getRollsLeft());
            mRoundCountTextView.setText("Rounds left: " + (10 - mModel.getmRoundCount()));
            mScoreTextView.setText("Score: " + mModel.getScore());
            if(mModel.isGameDone()){
                Toast.makeText(ThirtyGame.this,
                        "Your score is: " + mModel.getScore(),
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("model", mModel);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        }
    }

    private void resetDiceImages() {
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
        mEndRoundButton = findViewById(R.id.calculateScoreButton);
        mEndRoundButton.setText("End Round");
        mEndRoundButton.setOnClickListener(new EndRound());

        mRollDiceBtn = findViewById(R.id.rollDiceButton);
        mRollDiceBtn.setText(R.string.rollDice);
        mRollDiceBtn.setOnClickListener(new RollDice());
        if(!mModel.isCanRollAgain()){
            mRollDiceBtn.setEnabled(false);
        }
    }

    /**
     * Sets up the radio group that decides the model scoreMode.
     */
    private void setUpRadioGroup(){
        mScoringSystemRadioGroup = findViewById(R.id.combinationRadioGroup);
        mScoringSystemRadioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rb = findViewById(((RadioGroup) view).getCheckedRadioButtonId());
                String scoreMode = rb.getText().toString();

                switch(scoreMode){
                    case "Low":
                        mModel.setScoreMode(3);
                        break;
                    case "Four":
                        mModel.setScoreMode(4);
                        break;
                    case "Five":
                        mModel.setScoreMode(5);
                        break;
                    case "Six":
                        mModel.setScoreMode(6);
                        break;
                    case "Seven":
                        mModel.setScoreMode(7);
                        break;
                    case "Eight":
                        mModel.setScoreMode(8);
                        break;
                    case "Nine":
                        mModel.setScoreMode(9);
                        break;
                    case "Ten":
                        mModel.setScoreMode(10);
                        break;
                    case "Eleven":
                        mModel.setScoreMode(11);
                        break;
                    case "Twelve":
                        mModel.setScoreMode(12);
                        break;
                    default:
                        mModel.setScoreMode(3);
                        break;
                }

            }
        });

        ((RadioButton)findViewById(R.id.lowRadioButton)).setText("Low");
        ((RadioButton)findViewById(R.id.fourRadioButton)).setText("Four");
        ((RadioButton)findViewById(R.id.fiveRadioButton)).setText("Five");
        ((RadioButton)findViewById(R.id.sixRadioButton)).setText("Six");
        ((RadioButton)findViewById(R.id.sevenRadioButton)).setText("Seven");
        ((RadioButton)findViewById(R.id.eightRadioButton)).setText("Eight");
        ((RadioButton)findViewById(R.id.nineRadioButton)).setText("Nine");
        ((RadioButton)findViewById(R.id.tenRadioButton)).setText("Ten");
        ((RadioButton)findViewById(R.id.elevenRadioButton)).setText("Eleven");
        ((RadioButton)findViewById(R.id.twelveRadioButton)).setText("Twelve");

        RadioButton lowButton = findViewById(R.id.lowRadioButton);
        lowButton.setChecked(true);
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
