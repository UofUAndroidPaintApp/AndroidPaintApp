package com.example

import org.jetbrains.exposed.dao.id.IntIdTable
// tables for our db
//  Table  of paints
object PaintTable : IntIdTable() {
    val userID = varchar("userID", 255)
    val imagePath = varchar("imagePath", 255)
    val timeStamp = long("timeStamp")
}

