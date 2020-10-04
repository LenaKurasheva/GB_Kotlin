package ru.geekbrains.gb_kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        push.setOnClickListener {
            viewModel.buttonClicked()
        }

        // Create the observer which updates the UI.
        val mainObserver = Observer<String> { value ->
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        val liveData = viewModel.getDataLiveData()
        liveData.observe(this, mainObserver)
    }
}