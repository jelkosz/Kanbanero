package com.googlecode.kanbanik.model

import org.apache.shiro.crypto.SecureRandomNumberGenerator
import org.apache.shiro.crypto.hash.Sha512Hash
import org.apache.shiro.util.ByteSource
import com.googlecode.kanbanik.db.HasMidAirCollisionDetection
import com.googlecode.kanbanik.db.HasMongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.DBObject
import org.bson.types.ObjectId
import com.mongodb.casbah.Imports.$set

class User(
  val name: String,
  val password: String,
  val realName: String,
  val salt: String,
  val version: Int) extends HasMongoConnection with HasMidAirCollisionDetection {

  def store: User = {
      if (exists) {
        update
      } else {
        createNew
      }
  }
  
  def exists() = {
    using(createConnection) { conn =>
      val user = coll(conn, Coll.Users).findOne(MongoDBObject(Task.Fields.id.toString() -> name))
      user.isDefined
    }
  }
  
  private def createNew = {
    val obj = User.asDBObject(this)
    using(createConnection) { conn =>
      coll(conn, Coll.Users) += obj
    }

    User.asEntity(obj)
  }

  private def update = {
    val update = $set(
      User.Fields.name.toString() -> name,
      User.Fields.password.toString() -> password,
      User.Fields.realName.toString() -> realName,
      User.Fields.salt.toString() -> salt,
      User.Fields.version.toString() -> { version + 1 })

    User.asEntity(versionedUpdate(Coll.Users, versionedQuery(name, version), update))

  }

  def delete {
    versionedDelete(Coll.Users, versionedQuery(name, version))
  }
  
  def withRealName(realName: String) = new User(name, password, realName, salt, version)
  
  def withPassword(password: String, salt: String) = new User(name, password, realName, salt, version)
  
  def withVersion(version: Int) = new User(name, password, realName, salt, version)
}

object User extends HasMongoConnection {

  object Fields extends DocumentField {
    val realName = Value("realName")
    val password = Value("password")
    val salt = Value("salt")
  }

  def apply(name: String) = new User(name, "", "", "", 1)
  
  def all(): List[User] = {
    using(createConnection) { conn =>
      coll(conn, Coll.Users).find().map(asEntity(_)).toList
    }
  }

  def byId(name: String) = {

    using(createConnection) { conn =>
      val dbProject = coll(conn, Coll.Users).findOne(MongoDBObject(User.Fields.id.toString() -> name)).getOrElse(throw new IllegalArgumentException("No such user with name: " + name))
      asEntity(dbProject)
    }

  }

  private def asEntity(dbObject: DBObject) = {
    new User(
      dbObject.get(User.Fields.id.toString()).asInstanceOf[String],
      dbObject.get(User.Fields.password.toString()).asInstanceOf[String],
      dbObject.get(User.Fields.realName.toString()).asInstanceOf[String],
      dbObject.get(User.Fields.salt.toString()).asInstanceOf[String],
      dbObject.get(User.Fields.version.toString()).asInstanceOf[Int])
  }

  private def asDBObject(entity: User): DBObject = {
    MongoDBObject(
      User.Fields.id.toString() -> entity.name,
      User.Fields.password.toString() -> entity.password,
      User.Fields.realName.toString() -> entity.realName,
      User.Fields.salt.toString() -> entity.salt,
      User.Fields.version.toString() -> entity.version)

  }

}