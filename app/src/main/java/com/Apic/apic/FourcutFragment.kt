package com.Apic.apic

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo.pencilSketch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var targetView: View

    lateinit var originImg: Bitmap
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

        // 프레임 색 변경
        // 주황 버튼
        binding.btnChangeFrameOrange.setOnClickListener {
            binding.fourcutFrame.setImageResource(R.drawable.fourcut_orange_img)
        }

        // 초록 버튼
        binding.btnChangeFrameGreen.setOnClickListener {
            binding.fourcutFrame.setImageResource(R.drawable.fourcut_green_img)
        }

        // 파랑 버튼
        binding.btnChangeFrameBlue.setOnClickListener {
            binding.fourcutFrame.setImageResource(R.drawable.fourcut_blue_img)
        }

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

        // 버튼 1) 그레이 사진 효과 적용
        binding.btnChangeEffect1.setOnClickListener {

//            val assetManager = resources.assets
//            val inputStream: InputStream = assetManager.open("iv_fourcut_test_img.jpg")
//            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

            // 불러온 원본 이미지 delete 기능을 위해 저장 (첫 프레임으로 돌아감 -> 추후 수정 예정)
            if (binding.fourcutFrame.drawable is BitmapDrawable) {
                originImg = (binding.fourcutFrame.drawable as BitmapDrawable).bitmap
            } else {
                val drawable = binding.fourcutFrame.drawable
                originImg = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            lateinit var bitmap : Bitmap

            if (binding.fourcutFrame.drawable is BitmapDrawable) {
                bitmap = (binding.fourcutFrame.drawable as BitmapDrawable).bitmap
            } else {
                val drawable = binding.fourcutFrame.drawable
                bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            binding.fourcutFrame.setImageBitmap(bitmap)

            val gray = Mat()
            Utils.bitmapToMat(bitmap, gray)

            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGBA2GRAY)

            val grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(gray, grayBitmap)

            binding.fourcutFrame.setImageBitmap(grayBitmap)
            Log.d("4cut", "그레이 이미지 설정 완료")
        }

        binding.btnChangeEffect2.setOnClickListener {
            // 불러온 원본 이미지 delete 기능을 위해 저장 (첫 프레임으로 돌아감 -> 추후 수정 예정)
            if (binding.fourcutFrame.drawable is BitmapDrawable) {
                originImg = (binding.fourcutFrame.drawable as BitmapDrawable).bitmap
            } else {
                val drawable = binding.fourcutFrame.drawable
                originImg = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            val assetManager = resources.assets
            val inputStream: InputStream = assetManager.open("iv_fourcut_test_img.jpg")
            // decode 비트맵 변수
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

            binding.fourcutFrame.setImageBitmap(bitmap)

            // 비트맵 to mat할 Mat 변수
            val gray = Mat()
            Utils.bitmapToMat(bitmap, gray)
            Log.d("4cut", "1) bitmap to mat 성공")

            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGRA2BGR)
            Log.d("4cut", "Imgproc.COLOR_BGRA2BGR 성공")

            var cartoon = cartoon(gray)
            Log.d("4cut", "카툰 효과 처리 성공")

            val grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(cartoon, grayBitmap)
            // Utils.matToBitmap(cartoon, bitmap)

            binding.fourcutFrame.setImageBitmap(grayBitmap)
            Log.d("4cut", "카툰 효과 이미지 설정 완료")
        }

        // 3번째 버튼 : 스케치
        binding.btnChangeEffect3.setOnClickListener {
            // 불러온 원본 이미지 delete 기능을 위해 저장 (첫 프레임으로 돌아감 -> 추후 수정 예정)
            if (binding.fourcutFrame.drawable is BitmapDrawable) {
                originImg = (binding.fourcutFrame.drawable as BitmapDrawable).bitmap
            } else {
                val drawable = binding.fourcutFrame.drawable
                originImg = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            val assetManager = resources.assets
            val inputStream: InputStream = assetManager.open("iv_fourcut_test_img.jpg")
            // decode 비트맵 변수
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            binding.fourcutFrame.setImageBitmap(bitmap)


            binding.fourcutFrame.setImageBitmap(bitmap)

            // 비트맵 to mat할 Mat 변수
            val gray = Mat()

            Utils.bitmapToMat(bitmap, gray)
            Log.d("4cut", "1) bitmap to mat 성공")

            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGRA2BGR)
            Log.d("4cut", "Imgproc.COLOR_BGRA2BGR 성공")

            var sketch = Mat()
            sketch = sketch(gray)
            Log.d("4cut", "그레이 스케치 효과 처리 성공")

            val grayBitmap = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(sketch, grayBitmap)

            binding.fourcutFrame.setImageBitmap(grayBitmap)
            Log.d("4cut", "그레이 스케치 효과 이미지 설정 완료")
        }

        binding.btnChangeEffect4.setOnClickListener {
            // 불러온 원본 이미지 delete 기능을 위해 저장 (첫 프레임으로 돌아감 -> 추후 수정 예정)
            if (binding.fourcutFrame.drawable is BitmapDrawable) {
                originImg = (binding.fourcutFrame.drawable as BitmapDrawable).bitmap
            } else {
                val drawable = binding.fourcutFrame.drawable
                originImg = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            }

            val assetManager = resources.assets
            val inputStream: InputStream = assetManager.open("iv_fourcut_test_img.jpg")
            // decode 비트맵 변수
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            binding.fourcutFrame.setImageBitmap(bitmap)

            binding.fourcutFrame.setImageBitmap(bitmap)

            // 비트맵 to mat할 Mat 변수
            val gray = Mat()

            Utils.bitmapToMat(bitmap, gray)
            Log.d("4cut", "1) bitmap to mat 성공")

            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGRA2BGR)
            Log.d("4cut", "Imgproc.COLOR_BGRA2BGR 성공")

            var sketch = Mat()
            pencilSketch(gray, sketch, sketch, 60F, 0.07F, 0.02F)
            Log.d("4cut", "펜슬 스케치 효과 처리 성공")

            Utils.matToBitmap(sketch, bitmap)

            binding.fourcutFrame.setImageBitmap(bitmap)
            Log.d("4cut", "펜슬 스케치 효과 이미지 설정 완료")
        }

        // save 버튼 누를 시
        binding.btn4cutSave.setOnClickListener {
            // 사진 저장
            targetView = binding.fourcutFrame

            Log.d("4cut", "targetView 가져오기")
            viewSave(targetView)
            Log.d("4cut", "viewSave() 함수 통과")
        }

        // Delete 버튼 누를 시
        binding.btn4cutDelete.setOnClickListener {
            // 초기화
            binding.fourcutFrame.setImageBitmap(originImg)
        }
        return binding.root
    }

//    private fun getFilePath() : String {
//
//    }

    private fun getViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getSaveFilePathName(): String {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "$folder/$fileName.jpg"
    }

    private fun bitmapFileSave(bitmap: Bitmap, path: String) {
        val fos: FileOutputStream
        try{
            fos = FileOutputStream(File(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
            fos.close()
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun viewSave(view: View) {
        val bitmap = getViewBitmap(view)
        val filePath = getSaveFilePathName()
        bitmapFileSave(bitmap, filePath)
    }


    fun sketch(src: Mat): Mat {
        //회색조 이미지 만들기
        val gray = Mat()
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY)
        // 회색조 이미지를 블러시키기
        val blur = Mat()
        Imgproc.GaussianBlur(gray, blur, Size(0.0, 0.0), 12.0)
        // 엣지만 남고 평탄한 부분은 흰색으로 바뀜
        val dst = Mat()
        Core.divide(gray, blur, dst, 255.0)
        return dst
    }

    fun cartoon(src: Mat): Mat {
        val width = src.width()
        val height = src.height()
        // 이미지의 크기를 줄이면 효과적으로 뭉개고, 연산량을 빨리 하는 효과가 있음.
        val resizedSrc = Mat()
        Imgproc.resize(src, resizedSrc, Size(width / 8.0, height / 8.0))
        // 블러 적용
        val blur = Mat()
        Imgproc.bilateralFilter(resizedSrc, blur, -1, 20.0, 7.0)
        // 엣지 검출한 뒤, 이미지를 반전시킨다.
        val edge = Mat()
        Imgproc.Canny(resizedSrc, edge, 100.0, 150.0)
        Core.bitwise_not(edge, edge)
        Imgproc.cvtColor(edge, edge, Imgproc.COLOR_GRAY2BGR)
        //블러시킨 이미지와 반전된 edge를 and연산자로 합치면 edge부분은 검정색으로 나오고, 나머지는 많이 뭉개지고 블러처리된 이미지로 나옴, 카툰효과
        val dst = Mat()
        Core.bitwise_and(blur, edge, dst)
        Imgproc.resize(dst, dst, Size(width.toDouble(), height.toDouble()), 1.0, 1.0, Imgproc.INTER_NEAREST)
        return dst
    }


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

