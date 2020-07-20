package cz.cvut.fukalhan.main.enemies.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        enemiesViewModel = EnemiesViewModel()
        return inflater.inflate(R.layout.fragment_enemies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getEnemies()
    }

    private fun getEnemies() {
        enemiesViewModel.enemies.observe(viewLifecycleOwner, Observer { enemies ->
            when {
                enemies.error -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                enemies.data?.isEmpty()!! -> enemies_state.text = getString(R.string.no_enemy_records)
                else -> setAdapter(enemies.data)
            }
        })
    }

    private fun setAdapter(enemies: List<User>) {
        val enemiesAdapter = EnemiesAdapter(requireContext(), this, enemies)
        val viewManager = LinearLayoutManager(activity)
        enemiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = enemiesAdapter
        }
    }

    fun showEnemyProfile(enemyID: String) {
        val action = EnemiesFragmentDirections.showEnemyProfile(enemyID)
        findNavController().navigate(action)
    }
}
