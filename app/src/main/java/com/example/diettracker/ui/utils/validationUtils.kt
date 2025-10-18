package com.example.diettracker.ui.utils



object ValidationUtils {

    // Capitalizes the first letter of each word, preserves trailing spaces
    fun formatFullName(name: String): String {
        return name.split(" ").joinToString(" ") {
            if (it.isNotEmpty()) it.replaceFirstChar { c -> c.uppercase() } else ""
        }
    }

    fun isValidFullName(name: String): Boolean {
        // Must contain at least two words (ignores extra spaces)
        val words = name.trim().split("\\s+".toRegex())
        return words.size >= 2 && words.all { it.isNotEmpty() }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val regex =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
        return regex.matches(password)
    }
}
