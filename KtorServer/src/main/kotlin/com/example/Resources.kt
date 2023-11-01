package com.example

import io.ktor.http.*
import io.ktor.http.content.*
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
import java.io.File
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
                            createTime = it[PaintTable.timeStamp]
                        )
                    }
            })
        }

        // POST - Create a new painting
        post<Paints.CreatePaint> {
            var fileDescription = ""
            var fileName = ""

            val multipartData = call.receiveMultipart()

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        fileDescription = part.value
                    }

                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        val fileBytes = part.streamProvider().readBytes()
                        File("./src/main/resources/$fileName").writeBytes(fileBytes)
                    }

                    else -> {}
                }
                part.dispose()
            }

            newSuspendedTransaction (Dispatchers.IO, DBSettings.db ) {
                PaintTable.insert {
                    it[userID] = "test"
                    it[imagePath] = fileName
                    it[timeStamp] = System.currentTimeMillis()
                }
            }

            call.respondText("$fileDescription is uploaded to '.src/main/resources/$fileName'")


            // deserialize
            val imageData = call.receive<ImageData>()
            val currentTime = Instant.now().toEpochMilli()
            val paintId = newSuspendedTransaction(Dispatchers.IO, DBSettings.db) {
                PaintTable.insertAndGetId {
                    it[userID] = imageData.userID
                    it[imagePath] = imageData.imagePath
                    it[timeStamp] = currentTime
                }
            }
            call.respond(
                HttpStatusCode.Created,
                "Paint $paintId created, by: ${imageData.userID}"
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

        get<Paints.GetImage> {
//            val fileName = call.receive<ImageRequestData>()




            call.respond("fileName is..${it.fileName}")

//
            try {
                val file = File("./src/main/resources/test.png")
            } catch (e: Exception) {
                call.respond(e.printStackTrace())
            }

            call.respond("made it after try catch")

//

        }

    }
}


// ImageDataObject  for GET
@Serializable
data class ImageDataObject(
    val userID: String,
    val paintId: Int,
    val imagePath: String,
    val createTime: Long
)


// ImageDAt for PUT
@Serializable
data class ImageData(val userID: String, val imagePath: String)
@Serializable
data class ImageRequestData(val fileName: String)

@Resource("/paint")
class Paints {
    @Resource("paintId")
    class PaintId(val parent: Paints = Paints(), val paintId: Int)

    @Resource("/create")
    class CreatePaint(val parent: Paints = Paints())

    @Resource("userId")
    class UserID(var parent: Paints = Paints(), val userID: String)

    @Resource("delete")
    class Delete(val parent: Paints = Paints(), val paintId: Int)

    @Resource("{fileName}/getImage")
    class GetImage(val parent: Paints = Paints(), val fileName: String)
}










