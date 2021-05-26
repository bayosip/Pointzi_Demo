package com.pointzi.fragdialog

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FabToDialogBuilder private constructor(val activity: FragmentActivity,
                                             val mainView: ViewGroup) {

    private var url:String?=null
    private var imgId:Int?=null
    private var imgBitmap: Bitmap?=null
    private lateinit var fab:FloatingActionButton

    constructor(
        url: String, activity: FragmentActivity, mainView: ViewGroup,
    ):
            this(activity,mainView){
        this.url = url
    }

    constructor(imgId:Int, activity: FragmentActivity, mainView: ViewGroup):
            this(activity,mainView){
        this.imgId = imgId
    }

    constructor(bitmap: Bitmap, activity: FragmentActivity, mainView: ViewGroup):
            this(activity,mainView){
        this.imgBitmap = bitmap
    }

    fun attachFAB(){
        fab = FloatingActionButton(activity)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(32,32,32,32)
        fab.layoutParams = layoutParams
        fab.setImageResource(R.drawable.ic_notice_24)
        fab.setOnClickListener {
            val dialog =PointziDialog.newInstance(url,imgId,imgBitmap)
            dialog.show(activity.supportFragmentManager)
        }
        mainView.addView(fab)
    }

//    class Builder(){
//        private var url:String?=null
//        private var imgId:Int?=null
//        private var imgBitmap: Bitmap?=null
//
//        fun setDialogImageViaUrl(url:String?):Builder{
//            if (!url.isNullOrEmpty()&&(imgId!=null || imgBitmap!=null))
//                throw java.lang.Exception("Too many image arguments!")
//            this.url = url
//            return this@Builder
//        }
//
//        fun setDialogImageViaId(id:Int?):Builder{
//            if (id!=null&&(!url.isNullOrEmpty() || imgBitmap!=null))
//                throw java.lang.Exception("Too many image arguments!")
//            this.imgId = id
//            return this@Builder
//        }
//
//        fun setDialogImgViaBitmap(bitmap: Bitmap?):Builder{
//            if (bitmap!=null && (!url.isNullOrEmpty() || imgId!=null))
//                throw java.lang.Exception("Too many image arguments!")
//            this.imgBitmap = bitmap
//            return this@Builder
//        }
//
//        fun build():PointziDialog{
//            if (url==null && imgBitmap==null && imgId==null)
//                throw NullPointerException("There must be an image resource added to the build dialog")
//
//            return PointziDialog.newInstance(url, imgId, imgBitmap)
//        }
//    }
}