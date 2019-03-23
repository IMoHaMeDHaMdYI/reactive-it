package com.mohamed.reactiveit.home.ui.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.mohamed.reactiveit.R
import com.mohamed.reactiveit.common.db.ECommerceDatabase
import com.mohamed.reactiveit.common.db.repository.ProductRepository
import com.mohamed.reactiveit.common.models.Product
import com.mohamed.reactiveit.common.utils.afterTextChanged
import com.mohamed.reactiveit.common.utils.click
import com.mohamed.reactiveit.home.ui.fragments.mUser
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity() {

    private lateinit var viewModel: AddProductViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        val db = ECommerceDatabase.getInstance(this)
        val viewModelFactory = AddProductViewModel.Factory(ProductRepository(db.productDao()))
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AddProductViewModel::class.java)
        initializeSubjects()
    }

    private fun initializeSubjects() {
        with(viewModel) {
            compositeDisposable.addAll(
                editTextProductName.afterTextChanged().subscribe {
                    productNameSubject.onNext(it)
                },
                editTextProductImg.afterTextChanged().subscribe {
                    productUrlSubject.onNext(it)
                },
                stateSubject.subscribe {
                    when (it.done) {
                        SubmitState.STATE_START -> endLoading()
                        SubmitState.STATE_LOADING -> setLoading()
                        SubmitState.STATE_DONE -> {
                            endLoading()
                            when (it.success) {
                                true -> onSuccess()
                                false -> onFailure()
                            }
                        }
                    }
                },
                imgDone.click().subscribe {
                    if (!verifyInput()) {
                        Toast.makeText(this@AddProductActivity, "Please Fill All The Fields.", Toast.LENGTH_SHORT)
                            .show()
                        return@subscribe
                    }
                    if (productNameSubject.value != null && productUrlSubject.value != null) {
                        stateSubject.onNext(SubmitState(SubmitState.STATE_LOADING, false))
                        insertProduct(
                            Product(
                                name = productNameSubject.value!!,
                                url = productUrlSubject.value!!,
                                information = editTextInformation.text.toString(),
                                userId = mUser.id,
                                price = editTextPrice.text.toString().split(" ")[0].toFloat()
                            )
                        )
                    } else Toast.makeText(this@AddProductActivity, "Please Fill the field", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setLoading() {
        imgDone.isClickable = false
        imgDone.isEnabled = false
        imgDone.visibility = View.INVISIBLE
        pb.visibility = View.VISIBLE
    }

    private fun endLoading() {
        imgDone.isEnabled = true
        imgDone.isClickable = true
        imgDone.visibility = View.VISIBLE
        pb.visibility = View.INVISIBLE
    }

    private fun onSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun onFailure() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    private fun verifyInput(): Boolean {
        val invalidInputList = arrayListOf(
            editTextProductName, editTextProductImg, editTextInformation,
            editTextPrice
        ).map { it.text?.toString() }
            .filter { it == null || it.isEmpty() }
        if (invalidInputList.isNotEmpty()) {
            return false
        }
        return true
    }
}
