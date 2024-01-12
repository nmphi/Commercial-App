package com.example.dacs3.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.dacs3.MainActivity
import com.example.dacs3.R
import com.example.dacs3.activity.HistoryActivity
import com.example.dacs3.helper.SharedPref


class AccountFragment : Fragment() {
    lateinit var s: SharedPref
    lateinit var btnLogout: TextView
    lateinit var tvName: TextView
    lateinit var tvEmail: TextView
    lateinit var btn_history: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view :View= inflater.inflate(R.layout.fragment_account, container, false)
        init(view)
        s = SharedPref(requireActivity())
        mainButton()
        setData()
        return view
    }
    fun mainButton(){
        btnLogout.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
        btn_history.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }

    }

    fun setData() {
        val user = s.getUser()!!
        tvName.text = user.name
        tvEmail.text = user.email
    }
    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvName = view.findViewById(R.id.tv_name)
        tvEmail = view.findViewById(R.id.tv_email)
        btn_history = view.findViewById(R.id.btn_history)
    }
}
