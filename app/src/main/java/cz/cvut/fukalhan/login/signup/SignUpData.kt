package cz.cvut.fukalhan.login.signup

class SignUpData(
    val username: String,
    val email: String,
    val password: String,
    private val confirmPassword: String
) {
    private fun arePasswordsTheSame() = password == confirmPassword

    private fun isInputEmpty() = username.isEmpty() || email.isEmpty() || password.isEmpty()

    fun processSignUpData(
        doOnPasswordsNotSame: () -> Unit,
        doOnEmptyInput: () -> Unit,
        doOnDataOk: () -> Unit) {
        when {
            !arePasswordsTheSame() -> doOnPasswordsNotSame()
            isInputEmpty() -> doOnEmptyInput()
            else -> doOnDataOk()
        }
    }
}