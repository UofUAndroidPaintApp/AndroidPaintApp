package com.example

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.Instant

// Routes

fun Application.configureResources() {
    install(Resources)
    routing {

        // GET - get all images sorted desc by the time created.
        get<Paints> {
            call.respond(newSuspendedTransaction(Dispatchers.IO) {
                PaintTable.selectAll().orderBy(PaintTable.timeStamp, SortOrder.DESC)
                    .map {
                        ImageDataObject(
                            userID = it[PaintTable.userID],
                            paintId = it[PaintTable.id].value,
                            imagePath = it[PaintTable.imagePath],
                            title = it[PaintTable.title],
                            createTime = it[PaintTable.timeStamp]
                        )
                    }
            })
        }

        // POST - Create a new painting
        post<Paints.CreatePaint> {
            // deserialize
            val imageData = call.receive<ImageData>()
            val currentTime = Instant.now().toEpochMilli()
            val paintId = newSuspendedTransaction(Dispatchers.IO, DBSettings.db) {
                PaintTable.insertAndGetId {
                    it[userID] = imageData.userID
                    it[imagePath] = imageData.imagePath
                    it[title] = imageData.title
                    it[timeStamp] = currentTime
                }
            }
            call.respond(
                HttpStatusCode.Created,
                "Paint $paintId created with title: ${imageData.title}, by: ${imageData.userID}"
            )
        }

        // GET all the image objects created by the user by desc based on the time posted.
        get<Paints.UserID> { it ->
            val userId = it.userID
            val paintsByThisUser = newSuspendedTransaction(Dispatchers.IO) {
                PaintTable.select(PaintTable.userID eq userId).orderBy(PaintTable.timeStamp, SortOrder.DESC)
                    .map {
                        ImageDataObject(
                            userID = it[PaintTable.userID],
                            paintId = it[PaintTable.id].value,
                            imagePath = it[PaintTable.imagePath],
                            title = it[PaintTable.title],
                            createTime = it[PaintTable.timeStamp]
                        )
                    }
            }
            call.respond(paintsByThisUser)
        }


        // GET drawing by paintId
        get<Paints.PaintId> { it ->
            val paintId = it.paintId
            val drawing = newSuspendedTransaction(Dispatchers.IO) {
                PaintTable.select { PaintTable.id eq paintId }
                    .map {
                        ImageDataObject(
                            userID = it[PaintTable.userID],
                            paintId = it[PaintTable.id].value,
                            imagePath = it[PaintTable.imagePath],
                            title = it[PaintTable.title],
                            createTime = it[PaintTable.timeStamp]
                        )
                    }
            }
            call.respond(drawing)
        }

        // DELETE a paint by id
        delete<Paints.Delete>() {
            val paintId = it.paintId
            val deletedRow = newSuspendedTransaction(Dispatchers.IO) {
                PaintTable.deleteWhere { PaintTable.id eq paintId }
            }
            call.respond(HttpStatusCode.OK, "Paint  $deletedRow is deleted")
        }
    }
}


// ImageDataObject  for GET
@Serializable
data class ImageDataObject(
    val userID: String,
    val paintId: Int,
    val imagePath: String,
    val title: String,
    val createTime: Long
)


// ImageDAt for PUT
@Serializable
data class ImageData(val userID: String, val imagePath: String, val title: String)

@Resource("/paint")
class Paints {
    @Resource("paintId")
    class PaintId(val parent: Paints = Paints(), val paintId: Int)

    @Resource("create")
    class CreatePaint(val parent: Paints = Paints())

    @Resource("userId")
    class UserID(var parent: Paints = Paints(), val userID: String)

    @Resource("delete")
    class Delete(val parent: Paints = Paints(), val paintId: Int)
}










