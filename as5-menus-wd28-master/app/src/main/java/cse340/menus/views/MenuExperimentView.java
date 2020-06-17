package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public abstract class MenuExperimentView extends AbstractMenuExperimentView {

    /**
     * Constructor
     *
     * @param context
     * @param trial Experiment trial (contains a list of items)
     */
    public MenuExperimentView(Context context, ExperimentTrial trial) { super(context, trial); }

    /**
     * Constructor
     *
     * @param context
     * @param items Items to display in menu
     */
    public MenuExperimentView(Context context, List<String> items) { super(context, items); }

    /**
     * Calculates the index of the menu item using the current finger position
     * This is specific to your menu's geometry, so override it in your Pie and Normal and Custom menu classes.
     *
     * Note that you should not be altering your menu's state within essentialGeometry.
     * This function should return a value to your touch event handler, and nothing more.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    public abstract int essentialGeometry(PointF p);

    /***
     * Handles user's touch input on the screen. It should follow the state machine specified
     * in the spec.
     *
     * @param event Event for touch.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int menuItem = essentialGeometry(event);

        /*
         * Implement the state machine for all of your views.
         * All of the state logic should be handled here, you won't need to change
         * this for Pie, Normal, and Custom menu to work. Use the state machine defined in the spec for reference.
         *
         * Below is the template for the state machine. You should use the state field to
         * fetch the menu's current state, and process it accordingly.
         */
        switch(mState) {
            case START:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startSelection(new PointF(event.getX(), event.getY()));
                    mState = State.SELECTING;
                    return true;
                }
                break;
            case SELECTING:
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    updateModel(menuItem);
                    invalidate();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    endSelection(menuItem, new PointF(event.getX(), event.getY()));
                    invalidate();
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    //////////////////////////////////////////////////
    // These methods are taken directly from the spec's description of the PPS
    //////////////////////////////////////////////////

    /**
     * Start the menu selection by recording the starting point and starting
     * a trial (if in experiment mode).
     * @param point The current position of the mouse
     */
    public void startSelection(PointF point) {
        // 0) record starting point
        setStartPoint(point);
        // 1) call trial.startTrial() (only if in experiment mode), passing it the position of the mouse
        if (experimentMode()){
            getTrial().startTrial(point);
        }
    }

    /**
     * Complete the menu selection and record the trial data if necessary
     * @param menuItem the menu item that was selected by the user
     * @param point The current position of the mouse
     */
    public void endSelection(int menuItem, PointF point) {
        //  0) Announce the selection using a Toast (or "Nothing Selected" if it is -1)
        if (menuItem == -1){
            announce("Nothing Selected");
        } else {
            announce("Selected " + getItem());
        }
        //  1) notify the menu trial listener
        //     a) call trial.endTrial(), passing it the pointer position and the currently selected item
        if (experimentMode() && getTrial() != null && getTrialListener() != null) {
            getTrial().endTrial(point, menuItem);
            //     b) call onTrialCompleted(trial)
            //  2) reset state machine
            getTrialListener().onTrialCompleted(getTrial());
        }
        reset();
    }

    /**
     * Change the model of the menu and force a redraw, if the current selection has changed.
     * @param menuItem the menu item that is currently selected by the user
     */
    public void updateModel(int menuItem) {
        //  check if the item selected has changed. If so
        //  1) update your menu's model
        if (menuItem != getCurrentIndex()) {
            setCurrentIndex(menuItem);
        }
    }

    /**
     * Reset all relevant variables to the start state (startPoint and currentIndex).
     */
    public void reset() {
        // think about what might need to be reset here. What
        // fields are used in your state machine?
        setStartPoint(new PointF(0,0));
        setCurrentIndex(-1);
        mState = State.START;
    }

    /**
     * Translate the canvas so that it is easier to draw relative to 0,0
     * Also only draw when the user has pressed (we are in the selecting state)
     *
     * @param canvas Canvas to draw on.
     */
    protected void onDraw(Canvas canvas) {
        /*
         * Translate the canvas so that your draw calls are relative to the starting point.
         * Call the relevant draw method of the menu if needed (based on the state machine).
         */
        if (mState == State.SELECTING) {
            canvas.translate(getStartPoint().x, getStartPoint().y);
            drawMenu(canvas);
        }
    }

    /**
     * This must be menu specific so override it in your menu class for Pie, Normal, & Custom menus
     * In either case, you can assume (0,0) is the place the user clicked when you are drawing.
     *
     * @param canvas Canvas to draw on.
     */
    protected abstract void drawMenu(Canvas canvas);
}
