package com.FMC.VisitorBook

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider.getUriForFile
import androidx.navigation.NavController
import com.FMC.VisitorBook.ui.myqr.my_qr
import com.FMC.VisitorBook.ui.record.record
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*


class FloatingActionClass (context:Context){
    // variable to hold context
    var context: Context? = context

    val TAG ="FloatingActionClass"
    var mainActivity: MainActivity?=null
    var navCtrl :NavController?=null
    var fab :FloatingActionButton?=null

    public var currentFragment = ""
    val myqrFragment ="myqrFragment"

    //Record Fragment related
    var recordModel : RecordModel?=null
    var recordClass : record?=null

    //My QR Fragment related
    var my_qr : my_qr?=null

    public var testValue :String ="ori"
    public fun initFab(){
        Log.d("FloatingActionClass",testValue)
    }


    public fun setFabVisible(value:Boolean){
        //val p = fab?.getLayoutParams()
        //p.anchorId = View.NO_ID
        //fab.setLayoutParams(p)
        if (value){
            fab?.setVisibility(View.VISIBLE)
        }else{
            fab?.setVisibility(View.GONE)
        }
        Log.d(TAG,testValue)
    }


    fun setFABListener(inFab:FloatingActionButton){
        fab=inFab
        fab?.setOnClickListener { view ->
            runAction(view)
        }
    }

    public fun runAction(view :View){

        if( currentFragment =="mainActivity" || currentFragment =="homeFragment"){
            Snackbar.make(view, "Share File", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            shareRecord()
        }
        //Toast.makeText(mainActivity,currentFragment,Toast.LENGTH_SHORT).show()
        if( currentFragment =="recordFragment"){
            openRecord()
        }

        if(currentFragment=="myqrFragment"){
            shareImage()
        }


    }
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun openRecord(){
        try {
            val permission =
            ActivityCompat.checkSelfPermission(
                mainActivity as Context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    mainActivity as Activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                );
            }
            var path = context?.getExternalFilesDir(null)
            var pathFolder = File(path,"records")
            var file = File(pathFolder,"Records.csv");
            if(file.exists()){
                    //var contentUri = getUriForFile(context, "com.mydomain.fileprovider", "Records.csv");
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("content://com.FMC.VisitorBook.fileprovider/path/Records.csv"))
                    intent.setDataAndType(Uri.parse("content://com.FMC.VisitorBook.fileprovider/path/Records.csv"), "text/csv");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    mainActivity?.startActivity(intent)

            }else{
                Toast.makeText(mainActivity,"Records.csv not found, try save some records first.",Toast.LENGTH_LONG).show()
                Log.d(TAG,"Records.csv not found, try save some records first.")
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(mainActivity,"Please install Excel, WPS or Google Sheet to view records.csv" ,Toast.LENGTH_LONG).show()
            //Toast.makeText(mainActivity,e.message ,Toast.LENGTH_SHORT).show()
            Log.d(TAG,e.message)
        }


    }
    fun shareImage(){
        try {

            val permission =
                ActivityCompat.checkSelfPermission(
                    mainActivity as Context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    mainActivity as Activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                );
            }
            var path = context?.getExternalFilesDir(null)

            var myQRImageFile = File(path,"records")
            var success = myQRImageFile.mkdirs()
            Log.d(TAG,"Created dir success : "+success)

            my_qr?.saveQRImage()
            var file = File(myQRImageFile,"MyQR.jpg");
            if(file.exists()){
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse("content://com.FMC.VisitorBook.fileprovider/path/MyQR.jpg")
                    )
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    type = "image/jpeg"
                }
                var shareIntent = Intent.createChooser(sendIntent, "Send MyQR.jpg to ...")
                mainActivity?.startActivity(shareIntent)
            }else{
                Toast.makeText(mainActivity,"MyQR.jpg not found",Toast.LENGTH_LONG).show()
                Log.d(TAG,"MyQR.jpg not found")
            }
        }catch(e:Exception){
            Toast.makeText(mainActivity,e.message ,Toast.LENGTH_LONG).show()
            Log.d(TAG,e.message)
        }
    }

    fun shareRecord(){
        try {
            var path = context?.getExternalFilesDir(null)
            var recordPath = File(path,"records")
            var file = File(recordPath,"Records.csv");
            if(file.exists()){
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse("content://com.FMC.VisitorBook.fileprovider/path/Records.csv")
                    )
                    type = "text/*"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Send Records.csv to ...")
                mainActivity?.startActivity(shareIntent)
            }
            else{
                Toast.makeText(mainActivity,"Records.csv not found, try save some records first.",Toast.LENGTH_LONG).show()
                Log.d(TAG,"Records.csv not found, try save some records first.")
            }
        }catch(e:Exception){
            Toast.makeText(mainActivity,e.message ,Toast.LENGTH_LONG).show()
            Log.d(TAG,e.message)
        }
    }

    fun recordFragmentSaveAction(){
        try{
            val permission =
                ActivityCompat.checkSelfPermission(
                    mainActivity as Context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    mainActivity as Activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                );
            }

            Log.d(TAG,"isExternalStorageReadOnly() : "+isExternalStorageReadOnly())
            Log.d(TAG,"isExternalStorageAvailable() : "+isExternalStorageAvailable())

            var path = context?.getExternalFilesDir(null)
            var pathFolder = File(path,"records")
            //Log.d(TAG,"Created dir"+path)
            //Log.d(TAG,"Created dir 2"+pathFolder)
            val success =pathFolder.mkdirs()
            Log.d(TAG,"Created dir success : "+success)

            var myFile =File(pathFolder,"Records.csv");

            // Send all output to the Appendable object sb
            var currentTime = Calendar.getInstance().getTime()
            var sb = StringBuilder()
            //var re = Regex()
            //val ss = java.lang.String.format(Str, userName, userArea, age, userSex)
            //var recordText = String.format("%1$s,%s,%s,%s,%s,%s,%s,%s,%s\n",
            sb.append(currentTime.toString())
            sb.append(",\t")
            sb.append(clearString(if (recordModel?.name!=null) recordModel?.name.toString() else ""))
            sb.append(",\t")
            sb.append(clearString(if (recordModel?.phone!=null) recordModel?.phone.toString() else ""))
            sb.append(",")
            sb.append(clearString(if (recordModel?.celsius!=null) recordModel?.celsius.toString() else ""))
            sb.append(",\t")
            sb.append(clearString(if (recordModel?.ic!=null) recordModel?.ic.toString() else ""))
            sb.append(",\t")
            sb.append(clearString(if (recordModel?.emp_no!=null) recordModel?.emp_no.toString() else ""))
            sb.append(",")
            sb.append(clearString(if (recordModel?.email!=null) recordModel?.email.toString() else ""))
            sb.append(",")
            sb.append(clearString(if (recordModel?.address!=null) recordModel?.address.toString() else "" ))
            sb.append(",")
            sb.append(clearString(if (recordModel?.note!=null) recordModel?.note.toString() else ""))
            sb.append(",\n")
            var recordText= sb.toString()

            //myFile.writeText(text)
            if (recordText != null) {
                Log.d(TAG,recordText)
                myFile.appendText(recordText)
                //FileOutputStream(myFile).use {
                //    it.write("record goes here".getBytes())
                //}
                recordClass?.resetView()
                Toast.makeText(mainActivity,"Record Saved",Toast.LENGTH_LONG).show()
            }
            Log.d(TAG,"Record Text : "+recordText)
        }catch (e:Exception){
            Toast.makeText(mainActivity,e.message ,Toast.LENGTH_LONG).show()
            Log.d(TAG,e.message)
        }
    }

    fun clearString(str : String):String{
        return str.replace(",","")
    }

    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
            true
        } else false
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == extStorageState) {
            true
        } else false
    }
}