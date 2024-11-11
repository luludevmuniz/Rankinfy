package com.alpaca.rankify

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.alpaca.rankify.HiltTestApplication_Application
import dagger.hilt.android.testing.CustomTestApplication

// A custom runner to set up the instrumented application class for tests.
class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication_Application::class.java.name, context)
    }
}

@CustomTestApplication(MyApp::class)
interface HiltTestApplication
