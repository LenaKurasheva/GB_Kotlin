package com.lenakurasheva.notes.ui.note

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.lenakurasheva.notes.R
import com.lenakurasheva.notes.common.getColorFromRes
import com.lenakurasheva.notes.data.entity.Note
import io.mockk.*
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

class NoteActivityTest{
    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val viewModel: NoteViewModel = mockk(relaxed = true)
    private val noteDataChannel = Channel<NoteData>()
    private val errorChannel = Channel<Throwable>()

    private val testNote = Note("333", "hello", "world")

    @Before
    fun setup() {
        clearAllMocks()
        loadKoinModules(listOf(
                module {
                    viewModel { viewModel }
                }))
        every { viewModel.getViewState() } returns noteDataChannel
        every { viewModel.getErrorChannel() } returns errorChannel
        every { viewModel.loadNote(any()) } returns mockk()
        every { viewModel.saveChanges(any()) } just runs
        every { viewModel.deleteNote() } returns mockk()

        // Запустим Activity, передав в Intent созданную заметку:
        Intent().apply {
            putExtra("note", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_call_viewModel_loadNote() {
        verify(exactly = 1) { viewModel.loadNote(testNote.id) }
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())

        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.palette)).perform(click()).perform(click())

        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(R.color.color_blue))).perform(click())

        val colorInt = R.color.color_blue.getColorFromRes(activityTestRule.activity)

        onView(withId(R.id.toolbar)).check { view, _ ->
            assertTrue("toolbar background color does not match",
                    (view.background as? ColorDrawable)?.color == colorInt)
        }
    }

    @Test
    fun should_show_note()  {
        activityTestRule.launchActivity(null)
        GlobalScope.launch {
            noteDataChannel.send(NoteData(testNote))
            onView(withId(R.id.et_title)).check(matches(withText(testNote.title)))
            onView(withId(R.id.et_body)).check(matches(withText(testNote.note)))
        }
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.et_title)).perform(typeText(testNote.title))
        verify { viewModel.saveChanges(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        onView(withId(R.id.delete)).perform(click())
        onView(withText(R.string.note_delete_ok)).perform(click())
        verify { viewModel.deleteNote() }
    }

}