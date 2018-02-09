package frlo.umu.cs.se.thirty;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StartScreen extends AppCompatActivity {
    private final int REQUEST_CODE_GAME = 0;
    private int score = 0;
    private int scores[];
    private final String SCORE_KEY = "score";
    private final String SCORE_ARRAY_KEY = "scoreArray";
    private TextView scoreTextView;

    /**
     * Create the button that goes to the game activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Button mStartGameButton = findViewById(R.id.startGameButton);
        scoreTextView = findViewById(R.id.finalScoreTextView);
        mStartGameButton.setText(R.string.startGame);


        restoreOnSavedInstance(savedInstanceState);

        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(StartScreen.this, ThirtyGame.class);
                startActivityForResult(gameIntent, REQUEST_CODE_GAME);
            }
        });
    }

    private void restoreOnSavedInstance(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY);
            scores = savedInstanceState.getIntArray(SCORE_ARRAY_KEY);

            if (scores != null) {
                ArrayList<Integer> scoreArrayList = new ArrayList<>();

                for (int i : scores) {
                    scoreArrayList.add(i);
                }

                ListView scoreList = findViewById(R.id.scoreChoicesListView);
                ArrayAdapter<Integer> scoreListAdapter = new ArrayAdapter<Integer>(
                        this,
                        android.R.layout.simple_list_item_1,
                        scoreArrayList
                );

                scoreList.setAdapter(scoreListAdapter);

                scoreTextView.setText("Score: " + score);

            }
        }
    }

    /**
     * Get result from game and display the score.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GAME){
            if(resultCode == Activity.RESULT_OK){
                ThirtyModel model = data.getParcelableExtra("model");
                score = model.getScore();
                scores = model.getScores();
                scoreTextView.setText("Score: " + score);
            }
        }
    }

    /**
     * If score is rotated, remember the score for recreation.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_KEY, score);
        outState.putIntArray(SCORE_ARRAY_KEY, scores);
    }
}
