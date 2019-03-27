package com.olx.letgojunkseller.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class ImageUtils {
    companion object {
        val IMAGE_COMPRESSION_PERCENTAGE = 75
        fun getCompressedImageRequestBody(filePath: String, maxImageSize: Int): RequestBody? {
            val rotation = getRotation(filePath)
            var requestBody: RequestBody? = null
            val file = File(filePath)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                            .toLowerCase())
            if (!TextUtils.isEmpty(filePath)) {

                val decodeSizeOnly = BitmapFactory.Options()
                decodeSizeOnly.inJustDecodeBounds = true
                BitmapFactory.decodeFile(filePath, decodeSizeOnly)

                var width_tmp = decodeSizeOnly.outWidth
                var height_tmp = decodeSizeOnly.outHeight
                var scale = 1
                while (!(width_tmp / 2 < maxImageSize || height_tmp / 2 < maxImageSize)) {
                    width_tmp /= 2
                    height_tmp /= 2
                    scale *= 2
                }

                val decodeScaledImage = BitmapFactory.Options()
                decodeScaledImage.inJustDecodeBounds = false
                decodeScaledImage.inSampleSize = scale
                decodeScaledImage.inPreferredConfig = Bitmap.Config.ARGB_8888
                var bmp: Bitmap? = BitmapFactory.decodeFile(filePath, decodeScaledImage)

                if (width_tmp > height_tmp) {
                    height_tmp = maxImageSize * height_tmp / width_tmp
                    width_tmp = maxImageSize
                } else {
                    width_tmp = maxImageSize * width_tmp / height_tmp
                    height_tmp = maxImageSize
                }
                val scaledBitmap = Bitmap.createScaledBitmap(bmp!!, width_tmp, height_tmp, true)
                if (scaledBitmap != bmp) {
                    bmp.recycle()
                    bmp = scaledBitmap
                }

                if (bmp != null) {
                    val rotatedBitmap = rotatePicture(rotation, bmp)
                    if (rotatedBitmap != bmp) {
                        bmp.recycle()
                        bmp = rotatedBitmap
                    }
                    try {
                        val bos = ByteArrayOutputStream()
                        bmp.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESSION_PERCENTAGE, bos)
                        val data = bos.toByteArray()
                        bos.close()
                        bmp.recycle()
                        requestBody = RequestBody.create(MediaType.parse(if (!TextUtils.isEmpty(mimeType)) mimeType else "multipart/form-data"), data)
                    } catch (t: Throwable) {
                        //Ignore
                    }

                }
            }
            return requestBody
        }

        private fun getRotation(pathImage: String): Int {
            var rotation = 0
            try {
                val imageFile = File(pathImage)
                val exif = ExifInterface(imageFile.path)
                val exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    rotation = 90
                } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    rotation = 180
                } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    rotation = 270
                }
            } catch (e: IOException) {
                // Ignore
            }

            return rotation
        }

        private fun rotatePicture(rotation: Int, bitmap: Bitmap): Bitmap {
            var rotatedBitmap = bitmap
            if (rotation != 0) {

                val matrix = Matrix()
                matrix.postRotate(rotation.toFloat())

                rotatedBitmap = Bitmap.createBitmap(
                        bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false
                )

            }

            return rotatedBitmap
        }
    }

/*    private fun setImageInfoOnError(errorDetails: UploadPhotoResult.ErrorImageUpload, maxImageSize: Int) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(errorDetails.getFilePath(), options)

        if (maxImageSize > 0 && options.outWidth > 0 && options.outHeight > 0) {
            val photoW = options.outWidth
            val photoH = options.outHeight

            val widthRatio = maxImageSize.toFloat() / photoW.toFloat()
            val heightRatio = maxImageSize.toFloat() / photoH.toFloat()

            var finalWidth = Math.floor((photoW * widthRatio).toDouble()).toInt()
            var finalHeight = Math.floor((photoH * widthRatio).toDouble()).toInt()
            if (finalWidth > maxImageSize || finalHeight > maxImageSize) {
                finalWidth = Math.floor((photoW * heightRatio).toDouble()).toInt()
                finalHeight = Math.floor((photoH * heightRatio).toDouble()).toInt()
            }

            errorDetails.setWidth(finalWidth)
            errorDetails.setHeight(finalHeight)
        }
    }*/
}