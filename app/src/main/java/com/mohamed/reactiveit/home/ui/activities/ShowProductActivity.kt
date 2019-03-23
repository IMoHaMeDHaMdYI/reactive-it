package com.mohamed.reactiveit.home.ui.activities

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.models.User
import com.mohamed.reactiveit.common.utils.CURRENT_USER
import com.mohamed.reactiveit.common.utils.TAG
import com.mohamed.reactiveit.common.utils.onPreDraw
import kotlinx.android.synthetic.main.activity_show_product.*

class ShowProductActivity : AppCompatActivity() {

    private lateinit var center: Pair<Int, Int>
    private var target = 0f
    private var start = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_product)
        fetchData()
        getCenter()
        window.sharedElementReturnTransition =
            (TransitionInflater.from(this@ShowProductActivity).inflateTransition(R.transition.shared_element))
        window.sharedElementEnterTransition =
            (TransitionInflater.from(this@ShowProductActivity).inflateTransition(R.transition.shared_element))
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                reveal()
            }

            override fun onTransitionResume(transition: Transition?) {

            }

            override fun onTransitionPause(transition: Transition?) {

            }

            override fun onTransitionCancel(transition: Transition?) {

            }

            override fun onTransitionStart(transition: Transition?) {
            }

        })
    }

    private fun reveal() {
        Log.d(TAG, "target : $target")
        imgProduct.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        val anim = ViewAnimationUtils.createCircularReveal(imgProduct, center.first, center.second, 0f, target)
            .apply {
                duration = 400
            }
        imgProduct.visibility = View.VISIBLE
        anim.start()
    }

    private fun getCenter() {
        viewCircular.onPreDraw {
            start = viewCircular.width.toFloat()
            center = Pair((viewCircular.x + viewCircular.width / 2).toInt(),
                (viewCircular.y + viewCircular.height / 2).toInt())
        }
        imgProduct.onPreDraw {
            target = imgProduct.width.toFloat()
        }
    }

    private fun fetchData() {
        intent.apply {
            val product = getParcelableExtra<Product>("product")
            val user = getParcelableExtra<User>(CURRENT_USER)
            Glide.with(this@ShowProductActivity)
                .load(product.url)
                .into(imgProduct)
            tvInfText.text = product.information
            tvSellerInf.text = user.name
            tvName.text = product.name
            tvPriceInf.text = "Priceless"
            ratingBar.rating = 5f
        }
    }
}
