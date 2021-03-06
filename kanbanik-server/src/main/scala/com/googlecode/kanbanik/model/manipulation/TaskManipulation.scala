package com.googlecode.kanbanik.commands
import scala.util.control.Breaks.break
import scala.util.control.Breaks.breakable
import com.googlecode.kanbanik.db.HasMongoConnection
import com.googlecode.kanbanik.model.Project
import com.googlecode.kanbanik.model.Task
import com.mongodb.casbah.Imports.$set
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

trait TaskManipulation extends HasMongoConnection {
  def generateUniqueTicketId(): String = {
    using(createConnection) { conn =>
      val maxTaskId = coll(conn, Coll.TaskId).findOne()

      if (maxTaskId.isDefined) {
        val nextMaxId = maxTaskId.get.get("maxId").asInstanceOf[Int] + 1
        coll(conn, Coll.TaskId).update(MongoDBObject("_id" -> maxTaskId.get.get("_id")),
          $set("maxId" -> nextMaxId))

        return "#" + nextMaxId
      }

      coll(conn, Coll.TaskId) += MongoDBObject("maxId" -> 1)

      "#1"
    }

  }
}