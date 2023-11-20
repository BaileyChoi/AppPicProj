package com.Apic.apic
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.Apic.apic.databinding.ActivityAddBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

//activity->empty activity

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    lateinit var filePath :String //파일주소

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MainActivity에서 전달한 데이터를 가져와서 화면에 표시
        val mystr = intent.getStringExtra("data")
        //binding.myData.text = mystr


        // 이미지 선택 후 반환 -> 전달된 Result가 OK이면 ImageView에 보이기, 업로드를 위해 파일이름 저장
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ //intent로부터 사용자 후속처리
            //전달 받은 코드가 제대로된 result인지 확인
            if(it.resultCode === android.app.Activity.RESULT_OK) {
                //이미지를 ImageView보이기
                Glide
                    .with(getApplicationContext())
                    .load(it.data?.data) //이미지 호출
                    .apply(RequestOptions().override(250,250))//동일 규격으로 가지고 옴.
                    .centerCrop()
                    .into(binding.addImageView)
                //이미지의 주소 저장
                val cursor = contentResolver.query(it.data?.data as Uri, arrayOf<String>(MediaStore.Images.Media.DATA), null, null,null) //data 밑에 data 주소를 uri로 사용하여 데이터 호출
                //맨 처음부터 찾아가겠다.
                cursor?.moveToFirst().let {
                    //파일주소
                    filePath  = cursor?.getString(0) as String //파일 주소를 첫번째로 가져오겠다.
                    Log.d("moblieApp","${filePath}")
                }
            }
        }

        //이미지를 불러오기
        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) //선택된 액션 수행
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*") //갤러리에 있는 이미지 호출 (모든 이미지)

            //선택된 이미지를 가지고 옴
            requestLauncher.launch(intent)
        }

        //save에 대한 작업 수행
        binding.btnSave.setOnClickListener {

            if(binding.addImageView.drawable !== null && binding.addEditView.text.isNotEmpty()){//반드시 글이 작성 되어 있어야 함
                //if(binding.addEditView.text.isNotEmpty()){
                //store 에 먼저 데이터를 저장 후 document id 값으로 업로드 파일 이름 지정
                saveStore()//firestore저장
            }else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
            finish() //addActivity 종료
        }
    }


    //찍은 사진을 불러옴- > 프로필 사진으로 크기 맞춤
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)

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

    //현재 시간 가져오기
    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
        //  tz = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
        //  dateFormat.setTimeZone(tz);                    //DateFormat에 TimeZone 설정

        return format.format(date)//전달받은 값을 사용자화 하여 바꿔 출력
    }

    private fun saveStore(){
        val data =mapOf(
            //사용자가 저장하지 않아도 자동 저장가능
            "email" to MyApplication.email,
            "content" to binding.addEditView.text.toString(), //사용자가 입력된 내용을 가져와서 넣겠다.
            "data" to dateToString(Date())//Java 제공 util을 통해 저장
        )

        MyApplication.db.collection("news")
            .add(data) //firebase에 데이터가 잘 들어가 있나 확인
            .addOnSuccessListener {//성공 시
                Log.d("mobileApp", "data have save ok") //데이터가 성공적으로 이루어졌을 때
                //업로드 이미지
                uploadImage(it.id)
            }
            .addOnFailureListener{ //실패 시
                Log.d("mobileApp", "data have save error",it)
            }
    }

    private fun uploadImage(docId:String){
        val storage = MyApplication.storage //전역변수로 선언해 둠
        val storageRef = storage.reference//스토리지 접근 방법
        val imageRef = storageRef.child("images/${docId}.jpg") //storage들을 images 파일에 저장 (id 사용)

        val file =Uri.fromFile(File(filePath))
        Log.d("mobileApp", file.toString())
        imageRef.putFile(file)
            //제대로 업로드 됨
            .addOnSuccessListener {
                Toast.makeText(this, "save ok..", Toast.LENGTH_SHORT).show()
                Log.d("mobileApp", "save ok..")
                finish()
            }
            //업로드 실패
            .addOnFailureListener{
                Log.d("mobileApp", "file save error", it)
            }
    }
}