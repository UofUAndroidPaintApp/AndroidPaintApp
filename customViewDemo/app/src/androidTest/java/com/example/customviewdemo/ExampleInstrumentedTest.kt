package com.example.customviewdemo

import android.view.View
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {

        val vm = SimpleViewModel()
        runBlocking{
            val lifeCycleOwner = TestLifecycleOwner()
            val before = vm.penColor.value!!
            var callBackFired = false

            lifeCycleOwner.run{
                withContext(Dispatchers.Main){
                    vm.penColor.observe(lifeCycleOwner){
                }
                    callBackFired = true
                }
            }
            assertTrue(callBackFired)
        }
    }

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    @Test
    fun splashIsDisplayed() {
        onView(withId(R.id.splashFragId)).check(matches(isDisplayed()))
    }
    @Test
    fun testTextViewString() {
        onView(isRoot()).perform(waitFor(3000))
        onView(withId(R.id.textView)).check(matches(withText("Let's Paint!")))
    }
    @Test
    fun isClickable() {
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.clickMe)).perform(click())
    }

    @Test
    fun checkInitialFragment() {
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.clickFragId)).check(matches(isDisplayed()))
    }
    @Test
    fun switchToDrawFragment() {
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.clickMe)).perform(click())
        onView(withId(R.id.drawFragId)).check(matches(isDisplayed()))
    }

    @Test
    fun backButtontoClickFragment() {
        onView(isRoot()).perform(waitFor(3000));
        onView(withId(R.id.clickMe)).perform(click())
        pressBack()
        onView(withId(R.id.clickFragId)).check(matches(isDisplayed()))
    }



}