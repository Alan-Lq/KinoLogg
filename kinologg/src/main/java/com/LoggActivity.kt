package com.example.kinologg

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kinologg.api.Login
import com.example.kinologg.api.model.Token
import kotlinx.android.synthetic.main.activity_logg.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logg)

        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        getToken()
        login_button.setOnClickListener{
            login()
        }



    }

    fun login(){
        var token = pref.getString(TOKEN_KEY,"ошибка")
        if(token!="error") {
            var login: Login = Login(login_view.text.toString(), password_view.text.toString(),token)
            var call: Call<Token> = RetrofitService.getPostApi().login(login)
            call.enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Toast.makeText(this@AuthActivity, "ОШИБКА С ПОДКЛЮЧЕНИЕМ", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    if(response.isSuccessful){
                        var answer:Token? = response.body()
                        if(answer!!.success==true){
                            val intent = Intent(this@AuthActivity, MainActivity::class.java)
                            //        val bundle = Bundle()
                            //        bundle.putSerializable("item_el", item)
                            //        intent.putExtras(bundle)
                            startActivity(intent)
                        }
                        else{

                        }
                    }
                    else{
                        Toast.makeText(this@AuthActivity, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()

                    }
                }
            })
        }
        else{
            Log.d("Общие настройки","Не удалось правильно получить токен из общих настроек")
        }
    }


    fun getToken(){  //получить токен с сервера

        var call : Call<Token> =  RetrofitService.getPostApi().getToken()
        call.enqueue(object : Callback<Token>{
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(this@AuthActivity, "ОШИБКА", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if(response.isSuccessful){
                    var answer:Token? = response.body()
                    val editor = pref.edit()
                    editor.putString(TOKEN_KEY, answer!!.request_token)        // кладем полученный токен в shared preferences
                    editor.apply()
                }
                else{
                    Toast.makeText(this@AuthActivity, "Api ключ не верен", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
}

