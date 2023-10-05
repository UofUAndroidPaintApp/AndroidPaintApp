package com.example.customviewdemo

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date


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
        var vm = SimpleViewModel()
        runBlocking {
            val lifeCycleOwner = TestLifecycleOwner()
            val before = vm.penColor.value!!
            var callBackFired = false

            lifeCycleOwner.run {
                withContext(Dispatchers.Main) {
                    vm.penColor.observe(lifeCycleOwner) {
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

//    @Test
//    fun splashIsDisplayed() {
//        onView(withId(R.id.splashFragId)).check(matches(isDisplayed()))
//    }
//
    @Test
    fun testTextViewString() {
        onView(isRoot()).perform(waitFor(3000))
        onView(withId(R.id.textView)).check(matches(withText("Let's Paint!")))
    }
//
//    @Test
//    fun isClickable() {
//        onView(isRoot()).perform(waitFor(3000));
//        onView(withId(R.id.startPainting)).perform(click())
//    }
//
//    @Test
//    fun checkInitialFragment() {
//        onView(isRoot()).perform(waitFor(3000));
//        onView(withId(R.id.clickFragId)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun switchToDrawFragment() {
//        onView(isRoot()).perform(waitFor(3000));
//        onView(withId(R.id.startPainting)).perform(click())
//        onView(withId(R.id.drawFragId)).check(matches(isDisplayed()))
//    }

    @RunWith(AndroidJUnit4::class)
    class SimpleEntityReadWriteTest {
        private lateinit var userDao: PaintingDAO
        private lateinit var db: PaintingDatabase

        @Before
        fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context, PaintingDatabase::class.java
            ).build()
            userDao = db.paintingDao()
        }

        @After
        @Throws(IOException::class)
        fun closeDb() {
            db.close()
        }

        suspend fun insertFilename(nameToInsert: String): Boolean {

            val data: PaintingData = PaintingData(Date(), nameToInsert+".jpg")

            //put data in database
            runBlocking {
                db.paintingDao().addPaintingData(data)
            }

            //grab the data from the database
            val query = db.paintingDao().latestPainting()
            var firstQuery = query.first()


            return firstQuery == data
        }

        @Test
        fun testDatabaseInsertion(){
            runBlocking {
                assert(insertFilename("lilBunk"))
                assert(insertFilename("winkledinkle"))
            }
        }
    }








}