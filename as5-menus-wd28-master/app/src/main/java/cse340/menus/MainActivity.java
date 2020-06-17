package cse340.menus;

import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AbstractMainActivity implements TrialListener{

    /**
     * Shows the menu given a trial.
     *
     * @param trial Current trial containing menu information.
     */
    @Override
    protected void showMenuForTrial(ExperimentTrial trial) {
        // Creates Menu based on trial (need to check what menu the current trial requires).
        // Sets the layout parameters for the menu and make it visible on screen
        super.showMenuForTrial(trial);

        // Indicate menu type in the task.
        final TextView instructionTextView = findViewById(R.id.instructionTextView);
        instructionTextView.setText(getString(R.string.trial_message, trial.getMenu(), trial.getItem()));

        // register a new listener with the menu:
        mMenuView.setTrialListener(this);
    }


    @Override
    public void onTrialCompleted(ExperimentTrial trial) {
        // When the user completes a trial, the menu listener should take care of the following:
        // 1. Write the result to your CSV file. This should be accomplished with
        //          ExperimentSession.recordResult().
        // show the menu for the next session, if there is another session available.
        // 3. If another session is not available, announce with a Toast and change the text in the
        //          instruction view to say the session is completed by using R.string.session_completed
        //          from strings.xml. For completeness, don’t forget to reset the session (to null)
        //          if you are done with all of the sessions.
        if (mSession != null) {
            mSession.recordResult();
            if (mSession.hasNext()) {
                showMenuForTrial(mSession.next());
            } else {
                TextView view = findViewById(R.id.instructionTextView);
                view.setText(R.string.session_completed);
                Toast.makeText(this, R.string.session_completed,
                        Toast.LENGTH_LONG).show();
                mSession = null;
                mMainLayout.removeView(mMenuView);
            }
        }
    }
}
