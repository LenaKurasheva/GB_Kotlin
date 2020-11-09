package com.lenakurasheva.notes.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.errors.NoAuthException
import com.lenakurasheva.notes.data.model.NoteResult
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreDataProviderTest{

    @get:Rule
    // InstantTaskExecutorRule is a JUnit Test Rule that swaps the background
    // executor used by the Architecture Components with a different one which
    // executes each task synchronously.
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb= mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockResultCollection = mockk<CollectionReference>()

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    private val provider = FirestoreDataProvider(mockDb, mockAuth)

    @Before
    fun setup(){
        clearAllMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every { mockDb.collection("users").document(any()).collection("notes") } returns mockResultCollection

        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw NoAuthException if no auth`(){
        var result: Throwable? = null
        every { mockAuth.currentUser } returns null
        val channel = provider.subscribeToNotes()
        result = (channel.poll() as NoteResult.Error)?.error
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToNotes should return notes`() {
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        val channel = provider.subscribeToNotes()
        slot.captured.onEvent(mockSnapshot, null)
        result = (channel.poll() as NoteResult.Success<List<Note>>)?.data

        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToNotes should return error`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        val channel =  provider.subscribeToNotes()
        slot.captured.onEvent(null, testError)
        result = (channel.poll() as NoteResult.Error)?.error

        assertEquals(testError, result)
    }

    @Test
    fun `saveNote calls set`() {
        val mockDocumentReference = mockk<DocumentReference>()
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<OnFailureListener>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(any()).addOnFailureListener(capture(slot)) } returns mockk()

        GlobalScope.launch {
            provider.saveNote(testNotes[0])
            verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
            slot.captured.onFailure(testError)
        }
    }

    @Test
    fun `saveNote returns note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)).addOnFailureListener(any()) } returns mockk()

        GlobalScope.launch {
            val note = provider.saveNote(testNotes[0])
            slot.captured.onSuccess(null)
            result = (note as NoteResult.Success<Note>)?.data
            assertEquals(testNotes[0], result)
        }
    }

    //TODO
    @Test
    fun `saveNote returns error`() {
        var result: Throwable? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<OnFailureListener>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(any()).addOnFailureListener(capture(slot)) } returns mockk()

        GlobalScope.launch {
            val error = provider.saveNote(testNotes[0])
            slot.captured.onFailure(testError)
            result = (error as NoteResult.Error)?.error
            assertEquals(testError, result)
        }
    }

    @Test
    fun `deleteNote calls delete`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        GlobalScope.launch {
            provider.deleteNote(testNotes[0].id)
            verify(exactly = 1) { mockDocumentReference.delete() }
        }
    }

    //TODO
    @Test
    fun `deleteNote returns NoteResult Success`(){
        var result: Any? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.delete().addOnSuccessListener(capture(slot)).addOnFailureListener(any()) } returns mockk()

        GlobalScope.launch {
            result = provider.deleteNote(testNotes[0].id)
            slot.captured.onSuccess(null)
            assertEquals(null, result)
        }
    }

    //TODO
    @Test
    fun `deleteNote returns NoteResult Error`(){
        var result: Throwable? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<OnFailureListener>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.delete().addOnSuccessListener(any()).addOnFailureListener(capture(slot)) } returns mockk()

        GlobalScope.launch {
            val error = provider.deleteNote(testNotes[0].id)
            slot.captured.onFailure(testError)
            result = (error as NoteResult.Error)?.error
            assertEquals(testError, result)
        }
    }

    //TODO
    @Test
    fun `getNoteById calls get`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        GlobalScope.launch {
            provider.getNoteById(testNotes[0].id)
            verify(exactly = 1) { mockDocumentReference.get() }
        }
    }

    //TODO
    @Test
    fun `getNoteById returns note`(){
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val mockSnapshot = mockk<DocumentSnapshot>()
        val slot = slot<OnSuccessListener<DocumentSnapshot>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.get().addOnSuccessListener(capture(slot)).addOnFailureListener(any()) } returns mockk()
        every { mockSnapshot.toObject(Note::class.java) as Note} returns testNotes[0]

        GlobalScope.launch {
            val note = provider.getNoteById(testNotes[0].id)
            slot.captured.onSuccess(mockSnapshot)
            result = (note as NoteResult.Success<Note>)?.data
            assertEquals(testNotes[0], result)
        }
    }

    //TODO
    @Test
    fun `getNoteById returns error`(){
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val mockDocumentReference = mockk<DocumentReference>()
        val mockSnapshot = mockk<DocumentSnapshot>()
        val slot = slot<OnFailureListener>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.get().addOnSuccessListener(any()).addOnFailureListener(capture(slot)) } returns mockk()

        GlobalScope.launch {
            val error = provider.getNoteById(testNotes[0].id)
            slot.captured.onFailure(testError)
            result = (error as NoteResult.Error)?.error
            assertEquals(testError, result)
        }
    }
}