package cz.cvut.fukalhan.repository.di

import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.challenges.ChallengesRepository
import cz.cvut.fukalhan.repository.challenges.FakeChallengesRepository
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.LoginRepository
import org.koin.dsl.module

object Module {
    val loginModule = module {
        single { LoginFacade() }
        single { LoginRepository() }
    }

    val challengeModule = module {
        single { ChallengesFacade() }
        single { ChallengesRepository() }
        single { FakeChallengesRepository() }
    }

}