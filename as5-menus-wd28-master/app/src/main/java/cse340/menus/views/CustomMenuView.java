package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.List;

import cse340.menus.ExperimentTrial;

public class CustomMenuView extends MenuExperimentView {

    /** Class constant used to determine the size of the normal menu */
    private static final float CELL_WIDTH_RATIO = 0.220f;
    private static final int CELLS_PER_LEVEL = 3;

    /**
     * The width of each menu cell, in pixels. This is set to (CELL_WIDTH_RATIO) * the device's
     * smaller dimension.
     */
    private int CELL_WIDTH;

    /**
     * When adding text to your menu cells, TEXT_OFFSET should be added to both the X and Y
     * coordinates of the menu cell. This will ensure that text is "contained" by the menu.
     * For experimentation, try leaving this property off when drawing your menus.
     */
    private int TEXT_OFFSET;

    public CustomMenuView(Context context, ExperimentTrial trial) { super(context, trial); }
    public CustomMenuView(Context context, List<String> items) { super(context, items); }
    /**
     * Method that will be called from the constructor to complete any set up for the view.
     * Calls the parent class setup method for initialization common to all menus
     */
    public void setup () {
        // initialize any fields you need to (you may create whatever you need)
        super.setup();

        // Determine the dimensions of the normal menu
        CELL_WIDTH = (int) (CELL_WIDTH_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels));
        TEXT_OFFSET =  CELL_WIDTH / 3;
    }


    /**
     * Called when a touch event is dispatched to this view.
     *
     * @param e Motion event to compute geometry for, most likely a touch.
     * @return whether this event was used by the onTouchEvent or should it be passed on
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // you may implement this if you need to, otherwise we inherit the state machine
        // used by {Normal/Pie}MenuView

        return super.onTouchEvent(e);
    }

    /**
     * Calculates the essential geometry for the custom menu.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    @Override
    public int essentialGeometry(PointF p) {
        int numOfCells = getItems().size();
        int height = (numOfCells - 1)/ CELLS_PER_LEVEL + 1;
        int index =  (int) p.y / CELL_WIDTH * CELLS_PER_LEVEL + (int)p.x / CELL_WIDTH;
        if (getDistance(p) < MIN_DIST || p.x > CELL_WIDTH * CELLS_PER_LEVEL || p.x < 0 ||
        p.y > CELL_WIDTH * height || p.y < 0 || index >= numOfCells) {
            return -1;
        }
        return index;
    }

    /**
     * This must be menu specific so override it in your menu class for Pie, Normal, & Custom menus
     * In either case, you can assume (0,0) is the place the user clicked when you are drawing.
     *
     * @param canvas Canvas to draw on.
     */
    @Override
    protected void drawMenu(Canvas canvas) {
        List<String> items = getItems();
        int height = items.size() / CELLS_PER_LEVEL + 1;
        Paint border = getBorderPaint();
        border.setAlpha(50);
        canvas.drawRect(0, 0, CELL_WIDTH * CELLS_PER_LEVEL, CELL_WIDTH * height, getBorderPaint());
        border.setAlpha(255);
        float left = 0;
        float top = 0;
        for (int i = 0; i < items.size(); i ++) {
            canvas.drawRect(left, top, left + CELL_WIDTH, top + CELL_WIDTH, border);
            canvas.drawText(items.get(i), TEXT_OFFSET + left, (float)TEXT_OFFSET * 3 / 2 + top, getTextPaint());
            if (i % 3 == 2) {
                left = 0;
                top += CELL_WIDTH;
            } else {
                left += CELL_WIDTH;
            }
        }
        if (getCurrentIndex() >=0) {
            float hLeft = (float)(getCurrentIndex() % CELLS_PER_LEVEL) * CELL_WIDTH;
            float hTop = (float) (getCurrentIndex() / CELLS_PER_LEVEL) * CELL_WIDTH;
            canvas.drawRect(hLeft, hTop, hLeft + CELL_WIDTH, hTop + CELL_WIDTH, getHighlightPaint());
        }
    }
}
