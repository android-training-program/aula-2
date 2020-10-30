package pt.atp.bobi

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

private const val TAG = "MainActivity"
private const val REQUEST_IMAGE_CAPTURE = 100

class MainActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer
    private var untilFinished = 10000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.open_camera).setOnClickListener {
            openNativeCamera()
        }

        findViewById<Button>(R.id.open_details).setOnClickListener {
            openDetailsActivity()
        }

        findViewById<Button>(R.id.show_dialog).setOnClickListener {
            showAppDialog()
        }
    }

    override fun onResume() {
        super.onResume()

        startCountDownTimer(untilFinished)
    }

    override fun onPause() {
        super.onPause()

        timer.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Calling this method will open the default camera application.
     */
    private fun openNativeCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    /**
     * Calling this method will open a new activity.
     */
    private fun openDetailsActivity() {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    /**
     * Calling this method will show a dialog.
     */
    private fun showAppDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialog_title)
        builder.setMessage(R.string.dialog_message)
        builder.apply {
            setPositiveButton(R.string.dialog_action_ok) { _, _ ->
                Toast.makeText(this@MainActivity, R.string.dialog_action_ok_selected, Toast.LENGTH_SHORT).show()
            }
            setNegativeButton(R.string.dialog_action_cancel) { _, _ ->
                Log.d(TAG, "Dialog cancelled")
            }
        }
        builder.create().show()
    }

    private fun startCountDownTimer(time: Long) {
        timer = object: CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                untilFinished = millisUntilFinished
                findViewById<TextView>(R.id.countdown).text = getString(R.string.time_remaining, (untilFinished / 1000))
            }

            override fun onFinish() {
                findViewById<TextView>(R.id.countdown).text = getString(R.string.time_done)
            }

        }

        timer.start()
    }
}