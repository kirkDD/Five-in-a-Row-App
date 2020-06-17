package cse340.menus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cse340.menus.views.*;


public class TestActivity extends AbstractMainActivity {

    /** List of items in the menu that will be displayed in the menu for this activity */
    protected List<String> mMenuItems;

    /** The normal menu that will be tested */
    private MenuExperimentView mNormalMenu;

    /** The pie menu that will be tested */
    private MenuExperimentView mPieMenu;

    /** The student's custom menu that will be tested */
    private MenuExperimentView mCustomMenu;

    /** The which menu is currently being tested */
    private MenuExperimentView mCurrentMenu = null;

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> testItems = new ArrayList<>();
        Collections.addAll(testItems, "1", "2", "4", "8", "16");
        mMenuItems = testItems;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mNormalMenu = new NormalMenuView(this, mMenuItems);
        mNormalMenu.setLayoutParams(params);
        mPieMenu = new PieMenuView(this, mMenuItems);
        mPieMenu.setLayoutParams(params);
        mCustomMenu = new CustomMenuView(this, mMenuItems);
        mCustomMenu.setLayoutParams(params);
    }

    /**
     * This uses inflate to create a menu with options for the session.
     *
     * @param menu The menu resource to inflate for this app
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    /**
     * Do an action based on the item that was selected from the menu
     *
     * @param item The item selected from the m enu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        mMenuView.setCurrentIndex(-1);

        // Inspect the item to determine the menu type
        switch (item.getItemId()) {
            case R.id.action_open_normal: showMenu(mNormalMenu); break;
            case R.id.action_open_pie: showMenu(mPieMenu); break;
            case R.id.action_open_custom: showMenu(mCustomMenu); break;
            default: return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Show the given menu on the screen by first removing all previous menus
     * then displaying the current menu. Only do this if the new menu is not
     * the same as the current menu being used.
     *
     * @param menu the new menu to show on the screen.
     */
    public void showMenu(@NonNull MenuExperimentView menu) {
        if (menu != mCurrentMenu) {
            mMainLayout.removeAllViews();
            mMainLayout.addView(menu);
            mCurrentMenu = menu;
        }
    }
}
