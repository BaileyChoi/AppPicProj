package com.Apic.apic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.Apic.apic.databinding.FragmentFourcutBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo.stylization
import java.io.InputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FourcutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FourcutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FourcutFragment
    init {
        System.loadLibrary("opencv_java4")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentFourcutBinding.inflate(inflater, container, false)

        binding.btnChangeEffect1.setOnClickListener {

            val assetManager = resources.assets
            val inputStream: InputStream = assetManager.open("iv_fourcut_test_img.jpg")
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            binding.fourcutFrame.setImageBitmap(bitmap)

            val gray = Mat()
            Utils.bitmapToMat(bitmap, gray)

            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGBA2GRAY)

            // 카툰 효과 처리
            stylization(gray, gray, 60F, 0.45F)
            Log.d("4cut", "카툰 효과 처리 성공")

            val grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(gray, grayBitmap)

            binding.fourcutFrame.setImageBitmap(grayBitmap)


//            val bitmap = (binding.fourcutFrame.drawable as BitmapDrawable).bitmap
//
//            val grayBitmap = makeGray(bitmap)
//            Log.d("4cut", "그레이 영상 변환 성공")
//
//            // 비트맵을 ByteArray로 직렬화
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//
//            val bitmap2 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//
//            binding.fourcutFrame.setImageBitmap(bitmap2)
//            Log.d("4cut", "그레이 영상 세팅")
//
//            // 카툰 효과 처리
//            stylization(gray4cut, gray4cut, 60F, 0.45F)
//            Log.d("4cut", "카툰 효과 처리 성공")
//
//            // 결과 이미지로 변경
//            Utils.matToBitmap(gray4cut, bitmap)
//            Log.d("4cut", "mat to Bitmap 성공")
//
//            binding.fourcutFrame.setImageBitmap(bitmap)
//            Log.d("4cut", "결과 이미지 설정 완료")
        }


        return binding.root
    }

//    fun makeGray(bitmap: Bitmap) : Bitmap {
//
//        // Create OpenCV mat object and copy content from bitmap
//        val mat = Mat()
//        Utils.bitmapToMat(bitmap, mat)
//
//        // Convert to grayscale
//        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)
//
//        // Make a mutable bitmap to copy grayscale image
//        val grayBitmap = bitmap.copy(bitmap.config, true)
//        Utils.matToBitmap(mat, grayBitmap)
//
//        return grayBitmap // 비트맵으로 리턴
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FourcutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FourcutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

