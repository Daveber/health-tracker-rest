package ie.setu.domain.repository

import ie.setu.domain.User

class UserDAO {

    private val users = arrayListOf<User>(
        User(name = "Eddy", email = "eddy@gmail.com", id = 0),
        User(name = "Daniel", email = "daniel@gmail.com", id = 1),
        User(name = "Ava", email = "ava@notreal.com", id = 2),
        User(name = "Dave", email = "dave@outlook.com", id = 3),
        User(name = "Danny", email = "danny@gmail.com", id = 4)
    )

    fun getAll(): ArrayList<User> {
        return users
    }

    fun findUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    fun findUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    fun save(user: User){
        users.add(user)
    }

    fun update(id: Int, user: User){
        val userindex = users.indexOfFirst { it.id == id }
        users[userindex] = user
    }

    fun delete(id: Int?){
        users.removeIf { it.id == id }
    }
}