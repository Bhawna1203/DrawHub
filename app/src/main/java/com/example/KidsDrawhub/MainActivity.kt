package com.example.KidsDrawhub

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Bitmap
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView?= null
    private var mImageButtonCurrentPaint: ImageButton? = null

    //Todo 2: create an activity result launcher to open an intent
    val openGalleryLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        //Todo 3: get the returned result from the lambda and check the resultcode and the data returned
        if (result.resultCode == RESULT_OK && result.data != null){
            //process the data
            //Todo 4 if the data is not null reference the imageView from the layout
            val imageBackground: ImageView = findViewById(R.id.iv_background)
            //Todo 5: set the imageuri received
            imageBackground.setImageURI(result.data?.data)
        }
    }

    /** create an ActivityResultLauncher with MultiplePermissions since we are requesting
     * both read and write
     */
    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissions ->
                permissions.entries.forEach {
                       val perMissionName = it.key
                       val isGranted = it.value
                    //Todo 3: if permission is granted show a toast and perform operation
                       if (isGranted ) {
                            Toast.makeText(
                               this@MainActivity,
                               "Permission granted now you can read the storage files.",
                                Toast.LENGTH_LONG
                            ).show()
                    //perform operation
                           val pickIntent = Intent(Intent.ACTION_PICK,
                               MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                           openGalleryLauncher.launch(pickIntent)

                       } else {
                          //Todo 4: Displaying another toast if permission is not granted and this time focus on
                          //    Read external storage
                          if (perMissionName == Manifest.permission.READ_EXTERNAL_STORAGE)
                             Toast.makeText(
                                this@MainActivity,
                                "Oops you just denied the permission.",
                                 Toast.LENGTH_LONG
                             ).show()
                      }
               }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.ll_paint_colors)
        mImageButtonCurrentPaint = linearLayoutPaintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_pressed)

        )

        val ib_brush :ImageButton = findViewById(R.id.ib_brush)
        ib_brush.setOnClickListener{
            showBrushSizeChooserDialog()
        }

        val ibUndo:ImageButton = findViewById(R.id.ib_undo)
        ibUndo.setOnClickListener{
            drawingView?.onClickUndo()
        }

        val ibGallery : ImageButton = findViewById(R.id.ib_gallery)
        ibGallery.setOnClickListener{
              requestStoragePermission()
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

    fun paintClicked(view: View){
        if(view !== mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressed)

            )

            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressed)

            )

            mImageButtonCurrentPaint = view
        }
    }

    //Todo 5: create a method to requestStorage permission
    private fun requestStoragePermission(){
        //Todo 6: Check if the permission was denied and show rationale
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            //Todo 9: call the rationale dialog to tell the user why they need to allow permission request
            showRationaleDialog("Kids Drawing App","Kids Drawing App " +
                    "needs to Access Your External Storage")
        }
        else {
            // You can directly ask for the permission.
            // Todo 7: if it has not been denied then request for permission
            //  The registered ActivityResultCallback gets the result of this request.
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

    }

    /** Todo 8: create rationale dialog
     * Shows rationale dialog for displaying why the app needs permission
     * Only shown if the user has denied the permission request previously
     */
    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }





}