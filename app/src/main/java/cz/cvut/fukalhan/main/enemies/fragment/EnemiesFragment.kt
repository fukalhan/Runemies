package cz.cvut.fukalhan.main.enemies.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import cz.cvut.fukalhan.R
import cz.cvut.fukalhan.main.enemies.adapter.EnemiesAdapter
import cz.cvut.fukalhan.main.enemies.viewmodel.EnemiesViewModel
import cz.cvut.fukalhan.repository.entity.User
import kotlinx.android.synthetic.main.fragment_enemies.*

/**
 * A simple [Fragment] subclass.
 */
class EnemiesFragment : Fragment() {

    private lateinit var enemiesViewModel: EnemiesViewModel
    private val userAuth: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        enemiesViewModel = EnemiesViewModel()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enemies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enemiesViewModel.enemies.observe(viewLifecycleOwner, Observer {enemies ->
            setAdapter(enemies)
        })
        if (userAuth != null) {
            enemiesViewModel.getEnemies(userAuth.uid)
        }
    }

    private fun setAdapter(enemies: List<User>) {
        val enemiesAdapter = EnemiesAdapter(this, enemies)
        val viewManager = LinearLayoutManager(activity)
        enemiesRecyclerView.apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = enemiesAdapter
        }
    }

    fun showEnemy(enemyID: String) {
        val action = EnemiesFragmentDirections.showSingleEnemy(enemyID)
        findNavController().navigate(action)
    }

}
