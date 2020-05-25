package com.FMC.VisitorBook.ui.myqr

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.FMC.VisitorBook.FloatingActionClass
import com.FMC.VisitorBook.MainActivity
import com.FMC.VisitorBook.R
import com.FMC.VisitorBook.RecordModel
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream


class my_qr : Fragment() {

    companion object {
        fun newInstance() = my_qr()
    }

    private lateinit var viewModel: MyQrViewModel

    private val TAG = "My QR"
    private var root : View?=null
    private var bitmap : Bitmap?=null
    private var myqrImage : ImageView?=null

    private var strQr : String = "{}"
    private var FAC : FloatingActionClass?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       var  root =inflater.inflate(R.layout.my_qr_fragment, container, false)

        var main = getActivity()
        if (main is MainActivity) {
            val myactivity: MainActivity = activity as MainActivity
            FAC = myactivity.FAC
            FAC?.currentFragment ="myqrFragment"
            FAC?.recordModel = RecordModel()
            FAC?.setFabVisible(true)
            FAC?.my_qr = this
            FAC?.mainActivity?.fabAction?.setImageResource(R.drawable.ic_menu_send);
        }


        var etName : EditText?= root.findViewById(R.id.myqrEditTextName)
        etName?.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
                    //Log.d(TAG,s.toString())
                    setQRImage(getString())
                    //myqrImage = root.findViewById(R.id.myqrImage)
                    //myqrImage?.setImageResource(R.drawable.ic_menu_gallery)
                    //setQRImage(s.toString())
                    //var gson = Gson()
                   // var jsonString = gson.toJson(RecordModel(etName!!.text.toString(),"012"))
                   // Log.d(TAG,jsonString)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                }
            })

        root.findViewById<EditText>(R.id.myqrEditTextName)?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) { setQRImage(getString())}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {}
        })
        addListener(root,R.id.myqrEditTextPhone)
        addListener(root,R.id.myqrEditTextCelsius)
        addListener(root,R.id.myqrEditTextIC)
        addListener(root,R.id.myqrEditTextEmpNo)
        addListener(root,R.id.myqrEditTextEmail)
        addListener(root,R.id.myqrEditTextAddress)
        addListener(root,R.id.myqrEditTextNote)

        //

        return root
    }

    fun addListener(root:View,id :Int){
        root.findViewById<EditText>(id)?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) { setQRImage(getString())}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {}
        })
    }

    fun getString():String{
    //Name,Phone,Celsius,IC,EmpNo,Email,Address,note

        var etName: String? = getView()?.findViewById<EditText>(R.id.myqrEditTextName)?.getText().toString()
        var etPhone = getView()?.findViewById<EditText>(R.id.myqrEditTextPhone)?.getText().toString()
        var etCelsius = getView()?.findViewById<EditText>(R.id.myqrEditTextCelsius)?.getText().toString()
        var etIC = getView()?.findViewById<EditText>(R.id.myqrEditTextIC)?.getText().toString()
        var etEmpNo = getView()?.findViewById<EditText>(R.id.myqrEditTextEmpNo)?.getText().toString()
        var etEmail = getView()?.findViewById<EditText>(R.id.myqrEditTextEmail)?.getText().toString()
        var etAddress = getView()?.findViewById<EditText>(R.id.myqrEditTextAddress)?.getText().toString()
        var etNote = getView()?.findViewById<EditText>(R.id.myqrEditTextNote)?.getText().toString()


        var gson = Gson()
        strQr = gson.toJson(
            RecordModel(
                etName,
                etPhone,
                etCelsius,
                etIC,
                etEmpNo,
                etEmail,
                etAddress,
                etNote
            )
        )
        //setQRImage(strQr)
/*

*/
        Log.d(TAG,strQr)
        return strQr
    }

    fun saveQRImage(){
        try {
            setQRImage(getString())
            var path = context?.getExternalFilesDir(null)
            val myQRImageFile = File(path,"records")
            var file = File(myQRImageFile,"MyQR.jpg");
            FileOutputStream(file).use({ out ->
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //val qrgSaver = QRGSaver()
       // var saved = qrgSaver.save(Environment.getExternalStorageDirectory().getPath()+ "/VisitorBook/MyQR.jpg", getString(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
    }

    fun setQRImage(text:String): Bitmap? {
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        /*val manager =
            getSystemService(WINDOW_SERVICE) as WindowManager?
        val display = manager!!.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width: Int = point.x
        val height: Int = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4*/
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension


        try {
            //myqrImage = root?.findViewById(R.id.myqrImage)
            // Getting QR-Code as Bitmap
            //bitmap = qrgEncoder.getBitmap()
            //var ss =bitmap.getWidth()
            //Log.v(TAG,ss.toString())
            // Setting Bitmap to ImageView
            val qrgEncoder =QRGEncoder(text, null, QRGContents.Type.TEXT, 512)
            bitmap = qrgEncoder.getBitmap()
            activity?.runOnUiThread(java.lang.Runnable {

                //qrgEncoder.colorBlack = Color.RED
                //qrgEncoder.colorWhite = Color.BLUE
                myqrImage = activity?.findViewById(R.id.myqrImage)
                // Getting QR-Code as Bitmap

                myqrImage?.setImageBitmap(bitmap)
                //myqrImage?.setImageResource(R.drawable.ic_menu_gallery)

            })
            return bitmap
            //val qrgSaver = QRGSaver()
            //var saved = qrgSaver.save(Environment.getExternalStorageDirectory().getPath()+ "/QRCode/", text, bitmap, QRGContents.ImageType.IMAGE_JPEG);
            //myqrImage?.setImageBitmap(bitmap)

        } catch (e: Exception) {
            Log.v(TAG, e.toString())
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG,"Destoryed")
        val sharedPref = activity?.getSharedPreferences("MyQR", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            with (sharedPref.edit()) {
                putString("JSON", strQr)
                commit()
                Log.d(TAG,"Writed into sharedPref"+strQr)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"Stopped")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyQrViewModel::class.java)
        // TODO: Use the ViewModel

        //var fab : FloatingActionButton? = activity?.findViewById(R.id.fab)

        //val p = fab?.getLayoutParams()
        //p.anchorId = View.NO_ID
        //fab.setLayoutParams(p)
        //fab?.setVisibility(View.GONE)
        //fab?.setVisibility(View.VISIBLE)


        //Toast.makeText(context,"Entered My QR",Toast.LENGTH_SHORT).show()


        Log.d(TAG,"Entered My QR")
        var main = getActivity()
        if (main is MainActivity) {
            val myactivity: MainActivity = activity as MainActivity
            var FAC = myactivity.FAC
            FAC.setFabVisible(true)
            FAC.testValue ="Get My QR"
            //FAC.initFab()
        }
        try{
            var json ="{}"
            val sharedPref = activity?.getSharedPreferences("MyQR", Context.MODE_PRIVATE)
            if (sharedPref != null) {
                json  = sharedPref.getString("JSON","{}").toString()
            }
            Log.d(TAG,"read from sharedPref"+json)
            var gson = Gson()
            var record =gson.fromJson(json, RecordModel::class.java)
            Log.d(TAG,"from record model"+record.toString())
            getView()?.findViewById<EditText>(R.id.myqrEditTextName)?.setText(if (record.name!=null) record.name.toString() else "")
            getView()?.findViewById<EditText>(R.id.myqrEditTextPhone)?.setText(if (record.phone!=null) record.phone.toString() else "")
            getView()?.findViewById<EditText>(R.id.myqrEditTextCelsius)?.setText(if (record.celsius!=null) record.celsius.toString() else "")
            getView()?.findViewById<EditText>(R.id.myqrEditTextIC)?.setText(if (record.ic!=null) record.ic.toString() else "")
            getView()?.findViewById<EditText>(R.id.myqrEditTextEmpNo)?.setText(if (record.emp_no!=null) record.emp_no.toString() else "")
            getView()?.findViewById<EditText>(R.id.myqrEditTextEmail)?.setText(if (record.email!=null) record.email.toString() else "")
            getView()?.findViewById<EditText>(R.id.myqrEditTextAddress)?.setText(if (record.address!=null) record.address.toString() else "")
            getView()?.findViewById<EditText>(R.id.myqrEditTextNote)?.setText(if (record.note!=null) record.note.toString() else "")


        }catch(x:Exception){
            Log.d(TAG,x.toString())
        }

    }

}
