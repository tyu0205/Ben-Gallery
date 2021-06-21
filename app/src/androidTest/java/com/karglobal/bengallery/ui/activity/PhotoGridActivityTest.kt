package com.karglobal.bengallery.ui.activity

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karglobal.bengallery.R
import com.karglobal.bengallery.ui.adapters.PhotoGridAdapter
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoGridActivityTest {
    private lateinit var activity: ActivityScenario<PhotoGridActivity>

    @Before
    fun setUp() {
        activity = launch(PhotoGridActivity::class.java)
        activity.moveToState(Lifecycle.State.RESUMED)
    }

    /**
     * Check recycler view displayed
     */
    @Test
    fun gridDisplayTest() {
        Espresso.onView(withId(R.id.photo_grid_recycler))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check grids are scrollable
     */
    @Test
    fun gridsScrollTest() {
        var lastPosition = 0
        sleep(2000)
        activity.onActivity { activity ->
            val recyclerView = activity.binding.photoGridRecycler
            val itemCount = recyclerView.adapter?.itemCount
            lastPosition = itemCount?.minus(1) ?: 0
        }

        //scroll to the last element
        Espresso.onView(withId(R.id.photo_grid_recycler)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                lastPosition
            )
        )

        sleep(2000)

        //scroll to start
        Espresso.onView(withId(R.id.photo_grid_recycler)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        )

        //check view display
        Espresso.onView(withId(R.id.photo_grid_recycler))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        sleep(1000)
    }

    /**
     * Check click on photo and swipe left on the detail activity, then back to the main activity
     */
    @Test
    fun scrollClickAndBackOnResultTest() {
        sleep(2000)
        var itemCount = 0
        activity.onActivity { activity ->
            val recyclerView = activity.binding.photoGridRecycler
            itemCount = recyclerView.adapter?.itemCount!!
        }

        if (itemCount > 2) {
            Espresso.onView(withId(R.id.photo_grid_recycler))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<PhotoGridAdapter.ViewHolder>(
                        0,
                        OnClick
                    )
                )

            //check view display
            Espresso.onView(withId(R.id.view_pager))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            sleep(1000)

            Espresso.onView(withId(R.id.view_pager)).perform(ViewActions.swipeLeft())

            sleep(1000)

            Espresso.pressBack()

            sleep(1000)

            //check view display after back is pressed
            Espresso.onView(withId(R.id.photo_grid_recycler))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            var currentPosition = -1
            activity.onActivity { activity ->
                currentPosition = activity.getCurrentPosition()
            }

            assertTrue(currentPosition == 1)

            sleep(1000)
        }

    }

    //idle time before each task
    private fun sleep(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    //custom click action for recycler view
    object OnClick : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on specific button"
        }

        override fun perform(uiController: UiController?, view: View) {
            val card: View = view.findViewById(R.id.grid_photo_card_view)
            card.performClick()
        }
    }
}