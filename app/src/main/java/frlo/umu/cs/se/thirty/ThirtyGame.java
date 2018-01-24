package frlo.umu.cs.se.thirty;

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
    private List<DiceImage> mDiceImages = new ArrayList<>();

    private TextView mRoundCountTextView;
    private TextView mRerollCountTextView;
    private TextView mScoreTextView;
    private Button mRollDiceBtn;
    private Button mEndRoundButton;
    private RadioGroup mScoringSystemRadioGroup;
    private ThirtyModel mModel;
    private String mGameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            //mModel = savedInstanceState.get("");
        }

        setContentView(R.layout.activity_thirty_game);

        mModel = new ThirtyModel();

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

    class DiceImageListener implements View.OnClickListener{
        private DiceImage linkedDiceImage;

        public DiceImageListener(DiceImage linkedDiceImage) {
            this.linkedDiceImage = linkedDiceImage;
        }

        @Override
        public void onClick(View view) {
            if(linkedDiceImage.isRollable()){
                linkedDiceImage.getmDiceImage().setBackgroundColor(Color.DKGRAY);
            } else{
                linkedDiceImage.getmDiceImage().setBackgroundColor(Color.WHITE);
            }
            linkedDiceImage.setRollable(!linkedDiceImage.isRollable());
        }
    }

    class RollDice implements View.OnClickListener{
        private Random mRand;
        private Drawable[] mDieFaces = new Drawable[6];

        public RollDice() {
            mDieFaces[0] = getResources().getDrawable(R.drawable.die_face_1_t);
            mDieFaces[1] = getResources().getDrawable(R.drawable.die_face_2_t);
            mDieFaces[2] = getResources().getDrawable(R.drawable.die_face_3_t);
            mDieFaces[3] = getResources().getDrawable(R.drawable.die_face_4_t);
            mDieFaces[4] = getResources().getDrawable(R.drawable.die_face_5_t);
            mDieFaces[5] = getResources().getDrawable(R.drawable.die_face_6_t);
            mRand = new Random();
        }

        @Override
        public void onClick(View view) {
            int index = 0;
            int value;
            boolean canClickAgain = mModel.rollDice();

            for (DiceImage mDiceImage : mDiceImages) {
                if (mDiceImage.isRollable()) {
                    value = mModel.getDiceRoll()[index] - 1;
                    mDiceImage.getmDiceImage().setImageDrawable(mDieFaces[value]);
                }
                index++;
            }
            mRerollCountTextView.setText("Rerolls left: " + mModel.getRollsLeft());
            if(!canClickAgain){
                view.setEnabled(false);
            }
        }
    }

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
                mEndRoundButton.setEnabled(false);
                mRollDiceBtn.setEnabled(false);
                Toast.makeText(ThirtyGame.this,
                        "You Got The Score: " + mModel.getScore(),
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    private void resetDiceImages() {
        for (DiceImage mDiceImage : mDiceImages) {
            if(!mDiceImage.isRollable()){
                mDiceImage.getmDiceImage().setBackgroundColor(Color.WHITE);
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

        for (DiceImage mDiceImage : mDiceImages) {
            mDiceImage.getmDiceImage().setBackgroundColor(Color.WHITE);
            mDiceImage.getmDiceImage().setOnClickListener(new DiceImageListener(mDiceImage));
        }
    }

    private void setUpRollDiceButton(){
        mEndRoundButton = findViewById(R.id.calculateScoreButton);
        mEndRoundButton.setText("End Round");
        mEndRoundButton.setOnClickListener(new EndRound());

        mRollDiceBtn = findViewById(R.id.rollDiceButton);
        mRollDiceBtn.setText(R.string.rollDice);
        mRollDiceBtn.setOnClickListener(new RollDice());
        mRollDiceBtn.callOnClick();
    }

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
