package cz.cvut.fukalhan.main.enemies.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.enemies.viewmodel.SingleEnemyViewModel
import kotlinx.android.synthetic.main.fragment_single_enemy.*

/**
 * A simple [Fragment] subclass.
 */
class SingleEnemyFragment : Fragment() {
    val args: SingleEnemyFragmentArgs by navArgs()
    private lateinit var singleEnemyViewModel: SingleEnemyViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        singleEnemyViewModel = ViewModelProviders.of(this).get(SingleEnemyViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_enemy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        singleEnemyViewModel.enemy.observe(viewLifecycleOwner, Observer {user ->
            enemyProfileUsername.text = user.username
        })
        singleEnemyViewModel.getEnemy(args.enemyID)
    }


}
