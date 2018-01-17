package frlo.umu.cs.se.thirty;

import android.widget.ImageButton;

/**
 * Created by Frans-Lukas on 2018-01-17.
 */

public class DiceImage {
    private boolean rollable = true;
    private ImageButton mDiceImage;

    public DiceImage(ImageButton mDiceImage) {
        this.mDiceImage = mDiceImage;
    }

    public boolean isRollable() {
        return rollable;
    }

    public void setRollable(boolean rollable) {
        this.rollable = rollable;
    }

    public ImageButton getmDiceImage() {
        return mDiceImage;
    }

    public void setmDiceImage(ImageButton mDiceImage) {
        this.mDiceImage = mDiceImage;
    }
}
