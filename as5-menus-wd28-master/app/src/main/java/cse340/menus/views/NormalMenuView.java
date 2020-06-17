package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.List;

import cse340.menus.ExperimentTrial;

public class NormalMenuView extends MenuExperimentView {

    /** Class constant used to determine the size of the normal menu */
    private static final float CELL_HEIGHT_RATIO = 0.104f;
    private static final float CELL_WIDTH_RATIO = 0.277f;
    private static final float TEXT_OFFSET_RATIO = 0.055f;

    /**
     * The height of each menu cell, in pixels. This is set to (CELL_HEIGHT_RATIO) * the device's
     * smaller dimension.
     */
    private int CELL_HEIGHT;

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

    public NormalMenuView(Context context, List<String> items) {
        super(context, items);
    }

    public NormalMenuView(Context context, ExperimentTrial trial) {
        super(context, trial);
    }

    /**
     * Method that will be called from the constructor to complete any set up for the view.
     * Calls the parent class setup method for initialization common to all menus
     */
    @Override
    public void setup() {
        super.setup();

        // Determine the dimensions of the normal menu
        CELL_HEIGHT = (int) (CELL_HEIGHT_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels));
        CELL_WIDTH = (int) (CELL_WIDTH_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels));
        TEXT_OFFSET = (int) (TEXT_OFFSET_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels));
    }

    /**
     * Calculates the index of the menu item using the current finger position
     * This is specific to your menu's geometry, so override it in your Pie and Normal menu classes
     * If the finger has moved less than MIN_DIST, or is outside the bounds of the menu,
     * return -1.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    @Override
    public int essentialGeometry(PointF p) {
        /*
         * Complete the essentialGeometry function for the normal menu.
         * Remember: you should not be altering the state of your application in this function --
         * you should only return the result.
         */
        int numberOfCells = this.getItems().size();
        if (p.x > CELL_WIDTH || p.y > CELL_HEIGHT * numberOfCells || p.x < 0 || p.y < 0
        || this.getDistance(p) < MIN_DIST) {
            return -1;
        }
        return (int) (p.y / CELL_HEIGHT);
    }

    /**
     * This must be menu specific so override it in your menu class for Pie, Normal, & Custom menus
     * In either case, you can assume (0,0) is the place the user clicked when you are drawing.
     *
     * @param canvas Canvas to draw on.
     */
    @Override
    protected void drawMenu(Canvas canvas) {
        /*
         *  Draw the menu.
         * If an option is currently selected, that option should be highlighted.
         *
         * You may change the paint properties for the menu if desired.
         * You can also choose to draw the text horizontally instead of vertically.
         */
        List<String> items = getItems();
        float top = 0;
        for (int i = 0; i < items.size(); i++) {
            canvas.drawRect(0,top, CELL_WIDTH, top + CELL_HEIGHT, getBorderPaint());
            canvas.drawText (items.get(i), TEXT_OFFSET, top + TEXT_OFFSET, getTextPaint());
            top += CELL_HEIGHT;
        }
        if (getCurrentIndex() >= 0) {
            canvas.drawRect(0, CELL_HEIGHT * getCurrentIndex(), CELL_WIDTH,
                    CELL_HEIGHT * getCurrentIndex() + CELL_HEIGHT, getHighlightPaint());
        }
    }
}