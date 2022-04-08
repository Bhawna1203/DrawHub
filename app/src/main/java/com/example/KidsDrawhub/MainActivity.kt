package com.example.KidsDrawhub

import android.app.Dialog
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton



class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())

        val ib_brush :ImageButton = findViewById(R.id.ib_brush)
        ib_brush.setOnClickListener{
            showBrushSizeChooserDialog()
        }




    }
    /**
     * Method is used to launch the dialog to select different brush sizes.
     */

    private fun showBrushSizeChooserDialog(){
            val brushDialog = Dialog(this)
            brushDialog.setContentView(R.layout.dialog_brush_size)
            brushDialog.setTitle("Brush size: ")
            val smallBtn:ImageButton = brushDialog.findViewById(R.id.ib_small_brush)
            smallBtn.setOnClickListener{
                drawingView?.setSizeForBrush(10.toFloat())
                brushDialog.dismiss()
            }

           val mediumBtn: ImageButton = brushDialog.findViewById(R.id.ib_medium_brush)
           mediumBtn.setOnClickListener(View.OnClickListener {
              drawingView?.setSizeForBrush(20.toFloat())
               brushDialog.dismiss()
           })

           val largeBtn: ImageButton = brushDialog.findViewById(R.id.ib_large_brush)
           largeBtn.setOnClickListener(View.OnClickListener {
              drawingView?.setSizeForBrush(30.toFloat())
              brushDialog.dismiss()
           })
        brushDialog.show()


    }


}