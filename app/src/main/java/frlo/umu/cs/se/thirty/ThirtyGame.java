package frlo.umu.cs.se.thirty;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThirtyGame extends AppCompatActivity {
    private List<DiceImage> mDiceImages = new ArrayList<>();
    private int[] mDiceValues = new int[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirty_game);

        setUpDiceImageButtons();
        setUpRollDiceButton();
        setUpRadioGroup();
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
        private Drawable[] mDieFaces = new Drawable[6];
        private Random mRand;

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
            for (DiceImage mDiceImage : mDiceImages) {
                if(mDiceImage.isRollable()) {
                    value = mRand.nextInt(6);
                    mDiceValues[index] = value;
                    mDiceImage.getmDiceImage().setImageDrawable(mDieFaces[value]);
                    index++;
                }
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
        Button rollDiceBtn = findViewById(R.id.rollDiceButton);
        rollDiceBtn.setText(R.string.rollDice);
        rollDiceBtn.setOnClickListener(new RollDice());
        rollDiceBtn.callOnClick();

        TextView textView = findViewById(R.id.scoreTextView);
        textView.setText("Score: 0");
    }

    private void setUpRadioGroup(){
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
}
