package id.co.texttospeech

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var tts: TextToSpeech? = null
    private var btnSpeak: Button? = null
    private var etSpeak: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // view binding button and edit text
        btnSpeak = findViewById(R.id.btn_speak)
        etSpeak = findViewById(R.id.et_input)

        btnSpeak!!.isEnabled = false

        // TextToSpeech(Context: this, OnInitListener: this)
        Log.d("TTS", TextToSpeech.EngineInfo().toString())
        tts = TextToSpeech(this) {status ->
            if (status != TextToSpeech.ERROR) {
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
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
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