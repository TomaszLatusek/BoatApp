package com.example.sockettest

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageUtils {
    /// @param folderName can be your app's name
    companion object {
        fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                // RELATIVE_PATH and IS_PENDING are introduced in API 29.

                val uri: Uri? =
                    context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )
                if (uri != null) {
                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                }
            } else {
                val directory =
                    File(
                        Environment.getExternalStorageDirectory()
                            .toString() + File.separator + folderName
                    )
                // getExternalStorageDirectory is deprecated in API 29

                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val fileName = System.currentTimeMillis().toString() + ".png"
                val file = File(directory, fileName)
                saveImageToStream(bitmap, FileOutputStream(file))
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
//                 .DATA is deprecated in API 29
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
            }
        }

        fun getImagesFromFolder(context: Context, folder: String): List<File> {

            val selection = MediaStore.Images.Media.DATA + " LIKE ?"

            return queryUri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, arrayOf("%$folder/%"))
                .use { it?.getResultsFromCursor() ?: listOf() }
        }

        fun getLastImageFromFolder(context: Context, folder: String): File {

            val selection = MediaStore.Images.Media.DATA + " LIKE ?"

            return queryUri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, arrayOf("%$folder/%"))
                .use { it?.getResultsFromCursor() ?: listOf() }.last()
        }

        private fun queryUri(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): Cursor? {
            return context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null)
        }

        private fun Cursor.getResultsFromCursor(): List<File> {
            val results = mutableListOf<File>()

            while (this.moveToNext()) {
                results.add(File(this.getString(this.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))))
            }
            return results
        }

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE,
        )

        private fun contentValues(): ContentValues {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            return values
        }

        private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
            if (outputStream != null) {
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}