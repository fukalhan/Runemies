package cz.cvut.fukalhan.shared

class DataWrapper<Type> (val data: Type?, val error: Boolean = false, val errorMessage: String? = null, val throwable: Throwable? = null) {
}