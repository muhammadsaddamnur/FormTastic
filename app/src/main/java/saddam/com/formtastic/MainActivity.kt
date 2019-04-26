package saddam.com.formtastic
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.wajahatkarim3.easyvalidation.core.view_ktx.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDaftar.isEnabled = false

        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    afterTextChanged.invoke(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            })
        }

        fun EditText.validate(validator: (String) -> Boolean, message: String) {
            this.afterTextChanged {
                this.error = if (validator(it)) null else message
            }
            this.error = if (validator(this.text.toString())) null else message
        }



        fun String.isValidNamaDepan(): Boolean
                = this.nonEmpty() && this.noNumbers() && this.noSpecialCharacters() && this.minLength(2) && this.maxLength(10)

        fun String.isValidNamaBelakang(): Boolean
                = this.nonEmpty() && this.noNumbers() && this.noSpecialCharacters() && this.minLength(2) && this.maxLength(10)

        fun String.isValidEmail(): Boolean
                = this.nonEmpty() && this.validEmail()

        fun String.isValidPassword(): Boolean
                = this.nonEmpty() && this.contains(txtRePassword.text.toString()) && this.noSpecialCharacters() && this.minLength(6) && this.maxLength(10)

        fun String.isValidRePassword(): Boolean
                = this.nonEmpty() && this.contains(txtPassword.text.toString()) && this.noSpecialCharacters() && this.maxLength(txtPassword.text!!.length) && this.minLength(6)

        txtNamaDepan.validate({ s -> s.isValidNamaDepan() }, "Harus diisi, alphabet 2-10 karakter")
        txtNamaBelakang.validate({ s -> s.isValidNamaBelakang() } , "Harus diisi, Aplhabet 2-10 karakter")
        txtEmail.validate({ s -> s.isValidEmail() } , "Harus diisi, email harus valid")
        txtPassword.validate({ s -> s.isValidPassword() } , "Harus diisi, 6-10 karakter alphanumerik")
        txtRePassword.validate({ s -> s.isValidRePassword() } , "Password tidak sesuai")

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            btnDaftar.isEnabled = isChecked
        }

        val myStrings = arrayOf("Laki-Laki", "Perempuan")
        spJenkel.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)
        spJenkel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Toast.makeText(this@MainActivity, myStrings[p2], LENGTH_LONG).show()
            }
        }

        btnDaftar.setOnClickListener {
            val namadepan = txtNamaDepan.validator().nonEmpty().noNumbers().noSpecialCharacters().minLength(2).maxLength(10).check()
            val namabelakang = txtNamaBelakang.validator().nonEmpty().noNumbers().noSpecialCharacters().minLength(2).maxLength(10).check()
            val email = txtEmail.validator().nonEmpty().validEmail().check()
            val password = txtPassword.validator().nonEmpty().noSpecialCharacters().minLength(6).maxLength(10).check()
            val repassword = txtRePassword.validator().nonEmpty().contains(txtPassword.text.toString()).maxLength(txtPassword.text!!.length).minLength(6).check()

            if (namadepan && namabelakang && email && password && repassword){
                val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
                    .setContext(this)
                    .setMessage(R.string.custom_title)
                    .setCancelable(false)
                    .build()
                    .apply {
                        show()
                    }

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Sukses")
                val dialogOke: AlertDialog = builder.create()

                mHandler = Handler()

                mRunnable = Runnable {
                    dialog!!.show()
                    dialog.dismiss()
                    dialogOke.show()
                }

                mHandler.postDelayed(
                    mRunnable,
                    2000
                )
            }else{
                Toast.makeText(this,"Gagal",Toast.LENGTH_SHORT).show()

            }
        }

    }


}

