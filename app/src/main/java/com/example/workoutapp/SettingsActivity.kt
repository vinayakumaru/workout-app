package com.example.workoutapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.workoutapp.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private var binding : ActivitySettingsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarSettings)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Settings"
        }
        binding?.toolbarSettings?.setNavigationOnClickListener{
            onBackPressed()
        }

        val sharedPref = getSharedPreferences(R.string.settingsData.toString(), MODE_PRIVATE)
        val restTime = sharedPref.getInt(R.string.RestTime.toString(),10)
        val exerciseTime = sharedPref.getInt(R.string.ExerciseTime.toString(),30)
        val scSpeakOut = sharedPref.getBoolean(R.string.speak_out.toString(),true)
        val scMediaSound = sharedPref.getBoolean(R.string.media_sound.toString(),true)

        binding?.slRestTime?.value = restTime.toFloat()
        binding?.slExerciseTime?.value = exerciseTime.toFloat()
        binding?.scSpeakOut?.isChecked = scSpeakOut
        binding?.scExerciseCompletionSound?.isChecked = scMediaSound
    }

    override fun onStop() {
        super.onStop()

        val sharedPref = getSharedPreferences(R.string.settingsData.toString(), MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putInt(R.string.RestTime.toString(),binding?.slRestTime?.value!!.toInt())
        editor.putInt(R.string.ExerciseTime.toString(),binding?.slExerciseTime?.value!!.toInt())
        editor.putBoolean(R.string.speak_out.toString(),binding?.scSpeakOut?.isChecked!!)
        editor.putBoolean(R.string.media_sound.toString(),binding?.scExerciseCompletionSound?.isChecked!!)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}