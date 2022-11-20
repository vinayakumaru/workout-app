package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.databinding.ActivityExerciseBinding
import com.example.workoutapp.databinding.DialogCustomBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding : ActivityExerciseBinding? = null

    private var restTimer : CountDownTimer? = null
    private var exerciseTimer : CountDownTimer? = null
    private var restProgress = 0
    private var exerciseProgress = 0
    private var restTimerDuration : Long = 0
    private var exerciseTimerDuration : Long = 0
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var ttsOn: Boolean = true
    private var playerOn: Boolean = true
    private var tts: TextToSpeech? = null
    private var player : MediaPlayer? = null

    private var exerciseAdaptor : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()
        }

        exerciseList = Constants.defaultExerciseList()
        tts = TextToSpeech(this,this)
        val sharedPref = getSharedPreferences(R.string.settingsData.toString(), MODE_PRIVATE)
        restTimerDuration = sharedPref.getInt(R.string.RestTime.toString(),10).toLong()
        exerciseTimerDuration = sharedPref.getInt(R.string.ExerciseTime.toString(),30).toLong()
        ttsOn = sharedPref.getBoolean(R.string.speak_out.toString(),true)
        playerOn = sharedPref.getBoolean(R.string.media_sound.toString(),true)
        binding?.progressBar?.max = restTimerDuration.toInt()
        binding?.exerciseProgressBar?.max = exerciseTimerDuration.toInt()


        setupExerciseStatusRecyclerView()
        setupRestView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
        //super.onBackPressed()
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBinding.inflate(layoutInflater)

        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener{
            customDialog.dismiss()
            this@ExerciseActivity.finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right)
        }

        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdaptor = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdaptor

    }
    private fun setupRestView(){
        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()
        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }
    private fun setRestProgressBar(){
        restTimer = object : CountDownTimer(restTimerDuration * 1000, 1000){
            override fun onTick(p0: Long) {
                binding?.progressBar?.progress = restTimerDuration.toInt() - restProgress
                binding?.tvTimer?.text = (restTimerDuration.toInt() - restProgress).toString()
                restProgress++
            }
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdaptor!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000){
            override fun onTick(p0: Long) {
                binding?.exerciseProgressBar?.progress = exerciseTimerDuration.toInt() - exerciseProgress
                binding?.tvExerciseTimer?.text = (exerciseTimerDuration.toInt() - exerciseProgress).toString()
                exerciseProgress++
            }
            override fun onFinish() {

                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdaptor!!.notifyDataSetChanged()

                if(currentExercisePosition < exerciseList?.size!! - 1){
                    if(playerOn){
                        try{
                            val soundURI = Uri.parse("android.resource://com.example.workoutapp/" + R.raw.press_start)
                            player = MediaPlayer.create(applicationContext,soundURI)
                            player?.isLooping = false
                            player?.start()
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun speakOut(text : String){
        if(ttsOn){
            tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        if(player != null){
            player!!.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","Initialization Failed")
            }
        }else{
            Log.e("TTS","Initialization Failed!")
        }
    }


}