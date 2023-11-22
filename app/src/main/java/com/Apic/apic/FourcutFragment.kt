package com.Apic.apic

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.Apic.apic.databinding.FragmentFourcutBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo.stylization
import java.io.InputStream

/*
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.my12application.databinding.ActivityAddBinding
 */


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

    //뷰에 나타낼 값들. 라이브데이터 형식 (사진 저장)
    var image = MutableLiveData<Int>()
    var background = MutableLiveData<Int>()
    val content = MutableLiveData<String>()

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

        // 사진 불러오기
        val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try{
                val calRatio = calculateInSampleSize(it.data!!.data!!, resources.getDimensionPixelSize(R.dimen.imgSize), resources.getDimensionPixelSize(R.dimen.imgSize) )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
                var inputStream = context?.contentResolver?.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close() // .close()로 파일을 종료(해주는 습관 필요)
                inputStream = null
                // 여기까지가 읽어오는 작업

                bitmap?.let {
                    binding.fourcutFrame.setImageBitmap(bitmap)
                } ?: let{ Log.d("mobileApp", "bitmap NULL")}
            } catch (e:Exception) { e.printStackTrace() }
        }

        binding.btn4cutphoto.setOnClickListener {
            // 사진 가져오기
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // intent 변수에 Intent() 호출해서 사진 하나를 가져오기 위해서 ACTION_PICK 사용, MediaStore를 통해서 uri를 가져오기
            // val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*" // 가져오기 전에 가져올 타입을 image만 가져오도록 타입을 지정
            requestGalleryLauncher.launch(intent)
        }

        // 사진 효과 적용
        binding.btnChangeEffect1.setOnClickListener {

            val assetManager = resources.assets
            val inputStream: InputStream = assetManager.open("iv_fourcut_test_img.jpg")
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            binding.fourcutFrame.setImageBitmap(bitmap)

            val gray = Mat()
            Utils.bitmapToMat(bitmap, gray)

            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGBA2GRAY)

            // 카툰 효과 처리
//            stylization(gray, gray, 60F, 0.45F)
//            Log.d("4cut", "카툰 효과 처리 성공")

            val grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(gray, grayBitmap)

            binding.fourcutFrame.setImageBitmap(grayBitmap)
            Log.d("4cut", "그레이 이미지 설정 완료")
        }

        return binding.root
    }

    /*
    fun makeGray(bitmap: Bitmap) : Bitmap {

        // Create OpenCV mat object and copy content from bitmap
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        // Convert to grayscale
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)

        // Make a mutable bitmap to copy grayscale image
        val grayBitmap = bitmap.copy(bitmap.config, true)
        Utils.matToBitmap(mat, grayBitmap)

        return grayBitmap // 비트맵으로 리턴
    }
     */

    // 갤러리에서 사진을 불러오는 것 (+ 불러온 이미지 크기 조정)
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = context?.contentResolver?.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

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

