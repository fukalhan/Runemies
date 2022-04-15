package cz.cvut.fukalhan.login.signin

class SignInData(
    val email: String,
    val password: String
) {
    private fun inputEmpty() = email.isEmpty() || password.isEmpty()

    fun processSignInData(
        doOnInputEmpty: () -> Unit,
        doOnDataOk: () -> Unit
    ) {
       when {
           inputEmpty() -> doOnInputEmpty()
           else -> doOnDataOk()
       }
    }
}