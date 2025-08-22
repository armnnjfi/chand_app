package com.example.chand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.chand.databinding.FragmentDetailsBinding
import com.example.chand.databinding.FragmentHomeBinding

class DetailsFragment : Fragment() {


    private lateinit var binding: FragmentDetailsBinding

    //other
    private val args : DetailsFragmentArgs by navArgs()
    private var info = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            info = args.bundleInfo

            textView.text = info
        }
    }
}