/*
Android instrumented test class named ExampleInstrumentedTest. 
Instrumented tests are designed to run on an actual Android device or emulator and are used to 
test the behavior of an Android app in a real environment. 
These tests interact with the app's components and simulate user interactions to validate the app's functionality.

The class is annotated with @RunWith(AndroidJUnit4.class). 
This annotation indicates that the test class will be executed using the AndroidJUnit4 test runner, 
which is specifically designed for Android instrumented tests.

The class contains a single test method named useAppContext(), annotated with @Test. 
This method will be executed when running the instrumented test.

Inside the useAppContext() method:

The Context of the app under test is obtained using InstrumentationRegistry.getInstrumentation().getTargetContext(). 
This Context represents the target application's context, which is the context of the app being tested.
The test checks whether the package name of the target app is equal to "com.stmn.pdfmerger" using assertEquals(). 
This test is a simple check to verify that the correct app context is obtained, 
ensuring that the test is running in the correct application environment.
*/

package com.stmn.pdfmerger;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.stmn.pdfmerger", appContext.getPackageName());
    }
}
