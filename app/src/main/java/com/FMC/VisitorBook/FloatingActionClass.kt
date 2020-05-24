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
import androidx.navigation.NavController
import com.FMC.VisitorBook.ui.myqr.my_qr
import com.FMC.VisitorBook.ui.record.record
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*


class FloatingActionClass {
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
            var file = File(Environment.getExternalStorageDirectory().getPath()+"/VisitorBook/Records.csv");
            if(file.exists()){

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("content://com.FMC.VisitorBook.fileprovider/VisitorBookFiles/Records.csv"))
                    intent.setDataAndType(Uri.parse("content://com.FMC.VisitorBook.fileprovider/VisitorBookFiles/Records.csv"), "text/csv");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    mainActivity?.startActivity(intent)

            }else{
                Toast.makeText(mainActivity,"/VisitorBook/Records.csv not found, try save some records first.",Toast.LENGTH_LONG).show()
                Log.d(TAG,"/VisitorBook/Records.csv not found, try save some records first.")
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(mainActivity,e.message ,Toast.LENGTH_LONG).show()
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
            val success = File(Environment.getExternalStorageDirectory().getPath()+"/VisitorBook").mkdirs()
            Log.d(TAG,"Created dir success : "+success)

            my_qr?.saveQRImage()
            var file = File(Environment.getExternalStorageDirectory().getPath()+"/VisitorBook/MyQR.jpg");
            if(file.exists()){
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse("content://com.FMC.VisitorBook.fileprovider/VisitorBookFiles/MyQR.jpg")
                    )
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    type = "image/jpeg"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Send MyQR.jpg to ...")
                mainActivity?.startActivity(shareIntent)
            }else{
                Toast.makeText(mainActivity,"/VisitorBook/MyQR.jpg not found",Toast.LENGTH_LONG).show()
                Log.d(TAG,"/VisitorBook/MyQR.jpg not found")
            }
        }catch(e:Exception){
            Toast.makeText(mainActivity,e.message ,Toast.LENGTH_LONG).show()
            Log.d(TAG,e.message)
        }
    }

    fun shareRecord(){
        try {
            var file = File(Environment.getExternalStorageDirectory().getPath()+"/VisitorBook/Records.csv");
            if(file.exists()){
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse("content://com.FMC.VisitorBook.fileprovider/VisitorBookFiles/Records.csv")
                    )
                    type = "text/*"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Send Records.csv to ...")
                mainActivity?.startActivity(shareIntent)
            }
            else{
                Toast.makeText(mainActivity,"/VisitorBook/Records.csv not found, try save some records first.",Toast.LENGTH_LONG).show()
                Log.d(TAG,"/VisitorBook/Records.csv not found, try save some records first.")
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
            val success = File(Environment.getExternalStorageDirectory().getPath()+"/VisitorBook").mkdirs()
            Log.d(TAG,"Created dir success : "+success)
            val myFile = File(Environment.getExternalStorageDirectory().getPath()+"/VisitorBook/Records.csv")

            // Send all output to the Appendable object sb
            var currentTime = Calendar.getInstance().getTime()
            val sb = StringBuilder()
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
                myFile.appendText(recordText)
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
}