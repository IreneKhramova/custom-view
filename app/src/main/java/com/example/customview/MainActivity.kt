package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slantedView.setText("Чем заняться на каникулах")
        slantedView.setImages(
            //"https://i.pinimg.com/originals/cb/8b/fd/cb8bfd4a7acda41ad657fad5592a7165.jpg",
            "https://user32265.clients-cdnnow.ru/originalStorage/post/ba/fd/59/e4/bafd59e4.jpg",
            "https://user32265.clients-cdnnow.ru/originalStorage/post/ba/fd/59/e4/bafd59e4.jpg",
            "https://user32265.clients-cdnnow.ru/originalStorage/post/ba/fd/59/e4/bafd59e4.jpg",
            "https://user32265.clients-cdnnow.ru/originalStorage/post/ba/fd/59/e4/bafd59e4.jpg"
            //"https://ipne.ws/wp-content/uploads/2019/03/buket-cvetov.jpg",
            //"https://avatars.mds.yandex.net/get-pdb/1043578/cb36e4d9-c373-4865-a0e4-3db3259194fe/orig"
        )
    }
}
