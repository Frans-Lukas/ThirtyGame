package frlo.umu.cs.se.thirty;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {
    private final int REQUEST_CODE_GAME = 0;
    private int score = 0;
    private final String SCORE_KEY = "score";
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Button mStartGameButton = findViewById(R.id.startGameButton);
        scoreTextView = findViewById(R.id.finalScoreTextView);
        mStartGameButton.setText(R.string.startGame);

        if(savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY);
            if(score != 0){
                scoreTextView.setText("Score: " + score);
            }
        }

        //findViewById(R.id.startScreenLayout).setBackground(getResources().getDrawable(R.drawable.startscreenimage));


        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(StartScreen.this, ThirtyGame.class);
                startActivityForResult(gameIntent, REQUEST_CODE_GAME);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GAME){
            if(resultCode == Activity.RESULT_OK){
                ThirtyModel model = data.getParcelableExtra("model");
                score = model.getScore();
                scoreTextView.setText("Score: " + score);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_KEY, score);
    }
}
