package br.me.vitorcsouza.cafe_house.help

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import br.me.vitorcsouza.cafe_house.domain.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TinyDB(val context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var DEFAULT_APP_IMAGEDATA_DIRECTORY: String? = null
    private var lastImagePath = ""

    fun putListObject(key: String, list: ArrayList<Item>) {
        val gson = Gson()
        val json = gson.toJson(list)
        preferences.edit().putString(key, json).apply()
    }

    fun getListObject(key: String): ArrayList<Item>? {
        val gson = Gson()
        val json = preferences.getString(key, null)
        val type = object : TypeToken<ArrayList<Item>>() {}.type
        return gson.fromJson(json, type)
    }

    // --- Funções de Imagem do Tutorial ---

    fun getImage(path: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun putImage(theFolder: String, theImageName: String, theBitmap: Bitmap): String? {
        if (theFolder == null || theImageName == null || theBitmap == null) return null

        DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder
        val mFullPath = setupFullPath(theImageName)

        if (mFullPath != "") {
            lastImagePath = mFullPath
            saveBitmap(mFullPath, theBitmap)
        }

        return mFullPath
    }

    private fun setupFullPath(imageName: String): String {
        val folder = File(context.cacheDir, DEFAULT_APP_IMAGEDATA_DIRECTORY)
        if (folder.exists() || folder.mkdirs()) {
            val file = File(folder, imageName)
            return file.absolutePath
        }
        return ""
    }

    private fun saveBitmap(fullPath: String, bitmap: Bitmap) {
        if (fullPath == null || bitmap == null) return

        val file = File(fullPath)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun isExternalStorageWritable(): Boolean {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

        fun isExternalStorageReadable(): Boolean {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }
    }
}
