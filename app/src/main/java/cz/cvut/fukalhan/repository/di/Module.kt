package cz.cvut.fukalhan.repository.di

import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.challenges.ChallengesRepository
import cz.cvut.fukalhan.repository.challenges.ChallengesRepositoryMock
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.LoginRepository
import cz.cvut.fukalhan.repository.user.*
import org.koin.dsl.module

object Module {
    val loginModule = module {
        single { LoginFacade() }
        single { LoginRepository() }
    }

    val challengeModule = module {
        single { ChallengesFacade() }
        single { ChallengesRepository() }
        single { ChallengesRepositoryMock() }
    }

    val userModule = module {
        single { UserFacade() }
        single { UserRepository() }
        single { UserRepositoryMock() }
    }
}