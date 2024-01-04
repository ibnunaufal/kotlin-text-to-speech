package id.co.texttospeech

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import java.util.Random


class MainActivity : AppCompatActivity() {

    private var tts: TextToSpeech? = null
    private var btnSpeak: Button? = null
    private var etSpeak: EditText? = null
    private var tvIsSpeaking: TextView? = null
    var mostRecentUtteranceID: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // view binding button and edit text
        btnSpeak = findViewById(R.id.btn_speak)
        etSpeak = findViewById(R.id.et_input)
        tvIsSpeaking = findViewById(R.id.isSpeaking)
        tvIsSpeaking!!.visibility = View.GONE

        btnSpeak!!.isEnabled = false

        // TextToSpeech(Context: this, OnInitListener: this)
        Log.d("TTS", TextToSpeech.EngineInfo().toString())
        tts = TextToSpeech(this) {status ->
            if (status != TextToSpeech.ERROR) {
                tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        // Speech started, disable the button
                        tvIsSpeaking!!.visibility = View.VISIBLE
                    }

                    override fun onDone(utteranceId: String?) {
                        // Speech finished, enable the button
                        if (mostRecentUtteranceID != utteranceId.toString()){
                            return
                        }
                        tvIsSpeaking!!.visibility = View.GONE
                    }

                    override fun onError(utteranceId: String?) {
                        // Speech error occurred, enable the button
                        tvIsSpeaking!!.visibility = View.GONE
                    }
                })
                tts?.setLanguage(Locale("id", "ID"))
                btnSpeak!!.isEnabled = true
            } else{
                // https://play.google.com/store/apps/details?id=com.google.android.tts
                Toast.makeText(this,
                    "Pastikan anda sudah menginstall dan mengaktifkan " +
                            "Speech Recognition & Synthesis dari google",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        btnSpeak!!.setOnClickListener { speakOut() }

    }
    private fun speakOut() {
        val text = etSpeak!!.text.toString()

        // set unique utterance ID for each utterance
        mostRecentUtteranceID = (Random().nextInt() % 9999999).toString()
        tvIsSpeaking!!.visibility = View.VISIBLE
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, mostRecentUtteranceID)
    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}