package org.example.Errors

sealed class JugadorError(val msg: String) {
    class InvalidoError(msg: String) : JugadorError(msg)
    class StorageError(msg: String) : JugadorError(msg)
    class NotFoundError(msg: String) : JugadorError(msg)
    class DatabaseError(msg: String) : JugadorError(msg)
}