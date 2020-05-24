package com.FMC.VisitorBook.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.FMC.VisitorBook.FloatingActionClass
import com.FMC.VisitorBook.MainActivity
import com.FMC.VisitorBook.R
import com.FMC.VisitorBook.RecordModel
import com.google.android.gms.vision.barcode.BarcodeDetector


class HomeFragment :  Fragment()  {
    private val RC_BARCODE_CAPTURE = 9001
    private lateinit var homeViewModel: HomeViewModel
    private val TAG = "BarcodeMain"
    private var detector: BarcodeDetector? = null
    private var scanResults: TextView? = null

    private var root : View?=null
    private var FAC : FloatingActionClass?=null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        scanResults= textView

        var main = getActivity()
        if (main is MainActivity) {
            val myactivity: MainActivity = activity as MainActivity
            FAC = myactivity.FAC
            FAC?.currentFragment ="homeFragment"
            FAC?.recordModel = RecordModel()
            FAC?.setFabVisible(true)
        }

        val buttonGoMyQR = root.findViewById<Button>(R.id.homeButtonGoMyQR)
        buttonGoMyQR.setOnClickListener{
            Log.d(TAG, "homeButtonGoMyQR pushed")
            val navController = main?.findNavController(R.id.nav_host_fragment)
            navController?.navigate(R.id.nav_my_qr)
        }
        val buttonGoRecord = root.findViewById<Button>(R.id.homeButtonGoRecord)
        buttonGoRecord.setOnClickListener{
            Log.d(TAG, "homeButtonGoRecord pushed")
            val navController = main?.findNavController(R.id.nav_host_fragment)
            navController?.navigate(R.id.nav_record)
        }


        Log.d(TAG, "MainActivity OnCreate Done")
        //setHasOptionsMenu(false)

        return root
    }

    fun updateText(text : String){

    }

    private fun takePicture() {

       // val launchIntent: Intent = BarcodeReaderActivity.getLaunchIntent(this, true, false)
       // startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST)

/*
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            imageFile = createImageFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        imageUri = Uri.fromFile(imageFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, PHOTO_REQUEST)
        }
*/
    }
}
