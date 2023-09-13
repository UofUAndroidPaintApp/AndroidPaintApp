package com.example.customviewdemo

import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.allOf

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

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
    @Test
    fun testTextViewString() {
        onView(withId(R.id.textView)).check(matches(withText("Let's Paint!")))
    }
    @Test
    fun isClickable() {
    onView(withId(R.id.clickMe)).perform(click())
    }

    @Test
    fun checkInitialFragment() {
        onView(withId(R.id.clickFragId)).check(matches(isDisplayed()))
    }
    @Test
    fun switchToDrawFragment() {
        onView(withId(R.id.clickMe)).perform(click())
        onView(withId(R.id.drawFragId)).check(matches(isDisplayed()))
    }

    @Test
    fun backButtontoClickFragment() {
        onView(withId(R.id.clickMe)).perform(click())
        pressBack()
        onView(withId(R.id.clickFragId)).check(matches(isDisplayed()))
    }

    @Test
    fun testTest() {




    }

    //test does initialization work


}