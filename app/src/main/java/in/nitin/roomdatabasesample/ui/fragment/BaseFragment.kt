package `in`.nitin.roomdatabasesample.ui.fragment

import `in`.nitin.roomdatabasesample.application.ProductApplication
import `in`.nitin.roomdatabasesample.viewmodel.ViewModelProviderFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import javax.inject.Inject

/**
 * Base fragment use initialised the viewmodel factory for all child fragment.
 */
abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * dependency injection
         * */
        ProductApplication.getComponent()!!.inject(this)

    }

}
