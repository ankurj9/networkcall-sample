package com.olx.letgojunkseller

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.asksira.bsimagepicker.BSImagePicker
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_posting.*


class PostingActivity : AppCompatActivity(), BSImagePicker.OnMultiImageSelectedListener, View.OnClickListener {

    var imageUri: Uri? = null
    lateinit var btnList:List<Button>



    override fun onMultiImageSelected(uriList: MutableList<Uri>?, tag: String?) {
        imageUri = uriList?.firstOrNull()
        updateView()
    }

    private fun updateView() {
        if (imageUri != null) {
            imageCard.visibility = View.VISIBLE
            adImage.visibility = View.VISIBLE
            btnUpload.visibility = View.GONE
            Glide.with(this).load(imageUri).into(adImage)
        } else {
            imageCard.visibility = View.GONE
            adImage.visibility = View.GONE
            btnUpload.visibility = View.VISIBLE
        }
    }

    lateinit var multiSelectionPicker: BSImagePicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)
        supportActionBar?.title = "Post Ad"
        setUp()
        btnUpload.setOnClickListener {
            multiSelectionPicker.show(supportFragmentManager, "picker")
        }
        imageCard.setOnClickListener {
            multiSelectionPicker.show(supportFragmentManager, "picker")
        }

    }

    fun setUp() {
        btnList = listOf(button1, button2, button3, button4)
        multiSelectionPicker = BSImagePicker.Builder(BuildConfig.APPLICATION_ID + ".provider")
                .isMultiSelect() //Set this if you want to use multi selection mode.
                .setMaximumMultiSelectCount(1) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                .disableOverSelectionMessage() //You can also decide not to show this over select message.
                .build()

        btnList.forEach {
            it.setOnClickListener(this)
        }
        btnPostAd.setOnClickListener { postAd() }
    }

    override fun onClick(v: View?) {
        btnList.filter { v?.id == it.id }.forEach { it.isSelected = true }
        btnList.filter { v?.id != it.id }.forEach { it.isSelected = false }
    }

    private fun postAd() {
        val dialog = AlertDialog.Builder(this).setMessage("Posting Ad...").create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        Repository.postAd(imageUri!!.path,
                titleView.text.toString(),
                btnList.first { it.isSelected }.text.toString(),
                priceView.text.toString()).observe(this, Observer {
            when(it.status) {
                Resource.LOADING -> {
                    dialog.show()
                }
                Resource.SUCCESS -> {
                    dialog.dismiss()
                    Toast.makeText(this, "Ad Posted Successfully", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })
    }
}