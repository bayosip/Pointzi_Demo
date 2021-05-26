package com.pointzi.fragdialog

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.pointzi.fragdialog.databinding.FragmentDialogBinding
import java.util.*


private const val ARG_URL = "url"
private const val ARG_ID = "id"
private const val ARG_BITMAP = "bitmap"

class PointziDialog private constructor(): DialogFragment() {

    private lateinit var binding:FragmentDialogBinding
    private var imgURL: String? = null
    private var imgId: Int? = null
    private var imgBitmap:Bitmap ?= null
    private lateinit var stamper: InstallationStamper

    companion object {
        private const val TAG = "PointziDialog"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param url Parameter 1.
         * @param imgId Parameter 2.
         * @param imgBitmap
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(url: String?, imgId: Int?, bitmap: Bitmap?) =
            PointziDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                    if (imgId != null) {
                        putInt(ARG_ID, imgId)
                    }
                    putParcelable(ARG_BITMAP, bitmap)
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_dialog, container, true)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.translucent_fifty_percent)
        isCancelable = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDialogBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        stamper = InstallationStamper.newInstance(requireContext())
        initialiseWidgets()
    }

    private fun initialiseWidgets() {
        arguments?.let {
            imgURL = it.getString(ARG_URL)
            imgId = it.getInt(ARG_ID, -1)
            imgBitmap =it.getParcelable(ARG_BITMAP)
        }
        with(binding){
            val height = imgCircle.height
            val width = imgCircle.width
            if (!imgURL.isNullOrEmpty())
                context?.let { Glide.with(it)
                    .load(imgURL)
                    .override(width,height)
                    .into(imgCircle) }

            if (imgId!=-1){
                imgCircle.setImageResource(imgId!!)
                imgCircle.invalidate()
                val drawable = imgCircle.drawable
                imgCircle.setImageDrawable(resizeDrawable(drawable))
            }

            if (imgBitmap!=null){
                imgCircle.setImageBitmap(resizeBitmap(imgBitmap!!,height))
            }

            txtTimeOfInstall.text = getString(R.string.installation_stamp,getInstallTime())
            txtCurrentTime.text = InstallationStamper
                .DateCalculator(System.currentTimeMillis())
                .getFullDateTimeInString()
        }
    }

    private fun resizeDrawable(image: Drawable): Drawable {
        val bitmap = Bitmap.createBitmap(
            image.getIntrinsicWidth(),
            image.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        image.draw(canvas)
        return image
    }

    private fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap {
        return try {
            if (source.height >= source.width) {
                if (source.height <= maxLength) { // if image already smaller than the required height
                    return source
                }
                val aspectRatio = source.width.toDouble() / source.height.toDouble()
                val targetWidth = (maxLength * aspectRatio).toInt()
                Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
            } else {
                if (source.width <= maxLength) { // if image already smaller than the required height
                    return source
                }
                val aspectRatio = source.height.toDouble() / source.width.toDouble()
                val targetHeight = (maxLength * aspectRatio).toInt()
                Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
            }
        } catch (e: java.lang.Exception) {
           return source
        }
    }

    private fun getInstallTime():String{
        var dateString = ""
       if( !stamper.isNotFirstInstall()){
           val currentDateTime = Calendar.getInstance().time
           stamper.writeToInstallTimeToPointziPref()
           dateString = stamper.getInstallTime()
       }else{
           dateString =stamper.getInstallTime()
       }
        return dateString
    }

    fun show(manager: FragmentManager) {
        try {
            if (!isStateSaved) {
                show(manager, "Pointzi Dialog")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if(Build.VERSION.SDK_INT<30){
            fixWindowSize()
        }else{
            fixWindowSizeApi30()
        }
    }

    private fun fixWindowSize(){
        val displayMetrics = DisplayMetrics()
        activity?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val windowWidth = width!! * 0.65
        val windowHeight = height!!/2

        Log.d(TAG, "onResume: w: $width , h: $height")
        val window = dialog!!.window ?: return
        window.setLayout(windowWidth.toInt(),windowHeight)
    }

    @RequiresApi(30)
    private fun fixWindowSizeApi30(){
        val displayMetrics = activity?.getWindowManager()?.maximumWindowMetrics
        val height = displayMetrics?.bounds?.height()
        val width = displayMetrics?.bounds?.width()


        val windowWidth = width!! * 0.65
        val windowHeight = height!!/2

        Log.d(TAG, "onResume: w: $width , h: $height")
        val window = dialog!!.window ?: return
        window.setLayout(windowWidth.toInt(),windowHeight)
    }
}