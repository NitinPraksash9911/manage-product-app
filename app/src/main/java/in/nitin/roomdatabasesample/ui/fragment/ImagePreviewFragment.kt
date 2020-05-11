package `in`.nitin.roomdatabasesample.ui.fragment

import `in`.nitin.roomdatabasesample.databinding.FragmentImagePreviewBinding
import `in`.nitin.roomdatabasesample.ui.utils.ZoomLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater


/**
 * A simple [Fragment] subclass.
 */
class ImagePreviewFragment : Fragment() {

    val args: ImagePreviewFragmentArgs by navArgs()
    lateinit var binding: FragmentImagePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentImagePreviewBinding.inflate(layoutInflater)

        /**
         *
         * we are clicked image url in argument
         * */
        binding.url = args.imageUrl


        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.frameLayout.onPinchZoomListener(object : ZoomLayout.PinchZoomListener {
            override fun onPinchZoom(zoom: Float) {
                /**
                 * [closeBtn] will be fade-in and fade-out when the Image View zoom-in and zoom-out accordingly
                 * */
                binding.closeBtn.alpha = 2 - zoom
            }

        })

        binding.executePendingBindings()

        return binding.root

    }


}
