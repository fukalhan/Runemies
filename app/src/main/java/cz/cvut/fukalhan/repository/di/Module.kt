package cz.cvut.fukalhan.repository.di

import cz.cvut.fukalhan.repository.challenges.ChallengesFacade
import cz.cvut.fukalhan.repository.challenges.ChallengesRepository
import cz.cvut.fukalhan.repository.login.LoginFacade
import cz.cvut.fukalhan.repository.login.LoginRepository
import cz.cvut.fukalhan.repository.user.UserFacade
import cz.cvut.fukalhan.repository.user.UserRepository
import cz.cvut.fukalhan.repository.runrecords.RunRecordsFacade
import cz.cvut.fukalhan.repository.runrecords.RunRecordsRepository
import cz.cvut.fukalhan.service.LocationTrackingRecord
import org.koin.dsl.module

object Module {
    val loginModule = module {
        single { LoginFacade() }
        single { LoginRepository() }
    }

    val challengeModule = module {
        single { ChallengesFacade() }
        single { ChallengesRepository() }
    }

    val userModule = module {
        single { UserFacade() }
        single { UserRepository() }
    }

    val userActivityModule = module {
        single { RunRecordsFacade() }
        single { RunRecordsRepository() }
    }

    val locationTrackingRecordModule = module {
        single { LocationTrackingRecord() }
    }
}