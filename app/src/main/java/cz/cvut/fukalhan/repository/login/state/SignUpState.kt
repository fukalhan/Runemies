package cz.cvut.fukalhan.repository.login.state

enum class SignUpState {
    SUCCESS, FAIL, WEAK_PASSWORD, EMAIL_ALREADY_REGISTERED, INVALID_EMAIL
}