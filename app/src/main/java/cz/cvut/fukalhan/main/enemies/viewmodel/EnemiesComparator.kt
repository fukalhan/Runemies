package cz.cvut.fukalhan.main.enemies.viewmodel

import cz.cvut.fukalhan.repository.entity.User

class EnemiesComparator : Comparator<User> {
    override fun compare(user1: User?, user2: User?): Int {
        return when {
            user1?.points == user2?.points -> 0
            user1?.points!! < user2?.points!! -> 1
            else -> -1
        }
    }
}