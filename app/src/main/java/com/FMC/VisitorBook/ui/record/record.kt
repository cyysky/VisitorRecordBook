package com.FMC.VisitorBook.ui.record

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.FMC.VisitorBook.FloatingActionClass
import com.FMC.VisitorBook.MainActivity
import com.FMC.VisitorBook.R

import com.FMC.VisitorBook.RecordModel
import com.FMC.VisitorBook.barcode.BarcodeCaptureActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.gson.Gson

class record : Fragment() {

    companion object {
        fun newInstance() = record()
    }

    private lateinit var viewModel: RecordViewModel
    private val TAG = "Record"
    private val RC_BARCODE_CAPTURE = 9001

    private var FAC : FloatingActionClass?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      var root= inflater.inflate(R.layout.record_fragment, container, false)


        val button = root.findViewById<Button>(R.id.recordButtonScanMyQR)
        button.setOnClickListener{
            Log.d(TAG,"Button pushed")

            // launch barcode activity.

            // launch barcode activity.
            val intent = Intent(activity, BarcodeCaptureActivity::class.java)
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true)
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false)

            startActivityForResult(intent,RC_BARCODE_CAPTURE)
        }

        var main = getActivity()
        if (main is MainActivity) {
            val myactivity: MainActivity = activity as MainActivity
            FAC = myactivity.FAC
            FAC?.currentFragment ="recordFragment"
            FAC?.recordModel = RecordModel()
            FAC?.setFabVisible(true)
            FAC?.recordClass = this
        }

        val buttonSave = root.findViewById<Button>(R.id.recordButtonSave)
        buttonSave.setOnClickListener{
            Log.d(TAG,"Save Button pushed")
            FAC?.recordModel= getRecordFromView()
            //FAC?.runAction(root)
            FAC?.recordFragmentSaveAction()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecordViewModel::class.java)
        // TODO: Use the ViewModel

    }
    fun getRecordFromView() : RecordModel {
        var record = RecordModel()

        record.name = getView()?.findViewById<EditText>(R.id.recordEditTextName)?.text.toString()
        record.phone = getView()?.findViewById<EditText>(R.id.recordEditTextPhone)?.text.toString()
        record.celsius =getView()?.findViewById<EditText>(R.id.recordEditTextCelsius)?.text.toString()
        record.ic =getView()?.findViewById<EditText>(R.id.recordEditTextIC)?.text.toString()
        record.emp_no =getView()?.findViewById<EditText>(R.id.recordEditTextEmpNo)?.text.toString()
        record.email =getView()?.findViewById<EditText>(R.id.recordEditTextEmail)?.text.toString()
        record.address =getView()?.findViewById<EditText>(R.id.recordEditTextAddress)?.text.toString()
        record.note =getView()?.findViewById<EditText>(R.id.recordEditTextNote)?.text.toString()
        Log.d(TAG,"get Record Form View"+record.toString())

        return record
    }

    fun updateView(record : RecordModel){
        Log.d(TAG,"from record model"+record.toString())
        getView()?.findViewById<EditText>(R.id.recordEditTextName)?.setText(if (record.name!=null) record.name.toString() else "")
        getView()?.findViewById<EditText>(R.id.recordEditTextPhone)?.setText(if (record.phone!=null) record.phone.toString() else "")
        getView()?.findViewById<EditText>(R.id.recordEditTextCelsius)?.setText(if (record.celsius!=null) record.celsius.toString() else "")
        getView()?.findViewById<EditText>(R.id.recordEditTextIC)?.setText(if (record.ic!=null) record.ic.toString() else "")
        getView()?.findViewById<EditText>(R.id.recordEditTextEmpNo)?.setText(if (record.emp_no!=null) record.emp_no.toString() else "")
        getView()?.findViewById<EditText>(R.id.recordEditTextEmail)?.setText(if (record.email!=null) record.email.toString() else "")
        getView()?.findViewById<EditText>(R.id.recordEditTextAddress)?.setText(if (record.address!=null) record.address.toString() else "")
        getView()?.findViewById<EditText>(R.id.recordEditTextNote)?.setText(if (record.note!=null) record.note.toString() else "")
    }

    fun resetView(){
        Log.d(TAG,"from record model"+ record.toString())
        getView()?.findViewById<EditText>(R.id.recordEditTextName)?.setText("")
        getView()?.findViewById<EditText>(R.id.recordEditTextPhone)?.setText("")
        getView()?.findViewById<EditText>(R.id.recordEditTextCelsius)?.setText("")
        getView()?.findViewById<EditText>(R.id.recordEditTextIC)?.setText("")
        getView()?.findViewById<EditText>(R.id.recordEditTextEmpNo)?.setText("")
        getView()?.findViewById<EditText>(R.id.recordEditTextEmail)?.setText("")
        getView()?.findViewById<EditText>(R.id.recordEditTextAddress)?.setText("")
        getView()?.findViewById<EditText>(R.id.recordEditTextNote)?.setText("")
    }
    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * [.RESULT_CANCELED] if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     *
     *
     *
     * You will receive this call immediately before onResume() when your
     * activity is re-starting.
     *
     *
     *
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this
     * result came from.
     * @param resultCode  The integer result code returned by the child activity
     * through its setResult().
     * @param data        An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     * @see .startActivityForResult
     *
     * @see .createPendingResult
     *
     * @see .setResult
     */
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode ==RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode: Barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject)
                    Toast.makeText(context, R.string.barcode_success, Toast.LENGTH_SHORT).show()


                    try{
                        var json =barcode.displayValue

                        var gson = Gson()
                        var record =gson.fromJson(json, RecordModel::class.java)
                        updateView(record)
                        FAC?.recordModel = record
                    }catch(x:Exception){
                        Log.d(TAG,x.toString())
                    }

                    Log.d(TAG, "Barcode read: " + barcode.displayValue)
                } else {
                    //scanResults?.text =getString(R.string.barcode_failure)
                    Log.d(TAG, "No barcode captured, intent data is null "+getString(R.string.barcode_failure))
                }
            } else {
                var errorMsg = String.format(
                    getString(R.string.barcode_error),
                    CommonStatusCodes.getStatusCodeString(resultCode)
                )
                Toast.makeText(context,errorMsg ,Toast.LENGTH_LONG).show()
                Log.d(TAG, errorMsg)
               /*
                scanResults?.text =
                    String.format(
                        getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)
                    )
                */
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
