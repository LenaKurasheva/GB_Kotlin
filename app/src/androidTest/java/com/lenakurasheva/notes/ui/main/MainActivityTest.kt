package com.lenakurasheva.notes.ui.main

import com.lenakurasheva.notes.R
import com.lenakurasheva.notes.data.entity.Note
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.lenakurasheva.notes.ui.note.NoteActivity
import com.lenakurasheva.notes.ui.note.NoteData
import com.lenakurasheva.notes.ui.note.NoteViewModel
import io.mockk.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

class MainActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val EXTRA_NOTE = "extra.NOTE_ID"
    private val viewModel: MainViewModel = mockk(relaxed = true)
    private val noteViewModel: NoteViewModel = mockk<NoteViewModel>(relaxed = true)
    private val viewStateChannel = Channel<List<Note>?>()
    private val noteDataChannel = Channel<NoteData>()
    private val errorChannel = Channel<Throwable>()

    private val testNotes = listOf(
            Note("1", "title1", "text1"),
            Note("2", "title2", "text2"),
            Note("3", "title3", "text3"),
    )

    @Before
    fun setup() {
        loadKoinModules(
                listOf(
                        module {
                            viewModel { viewModel }
                            viewModel { noteViewModel }
                        }
                )
        )
        every { viewModel.getViewState() } returns viewStateChannel
        every { noteViewModel.getViewState() } returns noteDataChannel
        every { viewModel.getErrorChannel() } returns errorChannel
        every { noteViewModel.getErrorChannel() } returns errorChannel
        activityTestRule.launchActivity(null)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed(){
        GlobalScope.launch {viewStateChannel.send(testNotes)}

        onView(withId(R.id.rv_notes)).perform(RecyclerViewActions.scrollToPosition<NotesRVAdapter.NoteViewHolder>(1))
        onView(withText(testNotes[1].note)).check(matches(isDisplayed()))
    }

    @Test
    fun check_detail_activity_intent_sent() {
        GlobalScope.launch {
            viewStateChannel.send(testNotes)
            noteDataChannel.send(NoteData(testNotes[1]))
        }
        onView(withId(R.id.rv_notes))
                .perform(actionOnItemAtPosition<NotesRVAdapter.NoteViewHolder>(1, click()))
        intended(allOf(hasComponent(NoteActivity::class.java.name)))
    }
}