package frlo.umu.cs.se.thirty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Button mStartGameButton = findViewById(R.id.startGameButton);
        mStartGameButton.setText(R.string.startGame);

        //findViewById(R.id.startScreenLayout).setBackground(getResources().getDrawable(R.drawable.startscreenimage));


        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(StartScreen.this, ThirtyGame.class);
                startActivity(gameIntent);
            }
        });
    }
}
