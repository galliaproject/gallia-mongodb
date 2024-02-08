package gallia
package mongodb

import scala.collection.JavaConverters._
import java.io.Closeable

import aptus.{Anything_, String_}
import aptus.{Option_, Tuple2_}

import gallia.atoms.utils.MongoDb

// ---------------------------------------------------------------------------
import org.jongo.{Find, Jongo, MongoCollection}
import com.mongodb.{MongoClient, MongoClientURI}

// ===========================================================================
// TODO: t210114153517 - need jongo until figure out: https://stackoverflow.com/questions/35771369/mongo-java-driver-how-to-create-cursor-in-mongodb-by-cusor-id-returned-by-a-db
object MongoDbJongo extends MongoDb { import MongoDb._

  def setLogLevel(level: java.util.logging.Level): Unit = { // TODO: configurable properly
    java.util.logging.Logger
      .getLogger("org.mongodb.driver")
      .setLevel(MongoDb.DefaultLogLevel) }

  // ---------------------------------------------------------------------------
  // see https://mongodb.github.io/mongo-java-driver/3.7/javadoc/com/mongodb/MongoClientURI.html
  def uriCollectionOpt(uriString: String): Option[String] = Option(new MongoClientURI(uriString).getCollection)
  def uriDatabaseOpt  (uriString: String): Option[String] = Option(new MongoClientURI(uriString).getDatabase)

  // ===========================================================================
  def query(uri: java.net.URI, dbOpt: Option[String])(cmd: MongoDbCmd): (Iterator[List[(Symbol, Any)]], Closeable) = {
    val mongoUri    = new MongoClientURI(uri.toASCIIString())
    require((Option(mongoUri.getDatabase), dbOpt).isExclusivelyDefined, (uri, dbOpt)) // TODO: wrap validation

    val mongoClient = new MongoClient(mongoUri)
    val mongoDb     = Option(mongoUri.getDatabase).orElse(dbOpt).map(mongoClient.getDB).force

    val jongo = new Jongo(mongoDb)

    val mongoCollection = jongo.getCollection(cmd.collection)

    val jongoFind =  find(mongoCollection, cmd)

    val mongoCursor = jongoFind.as(classOf[org.bson.Document])

    val iter = mongoCursor.iterator.asScala.map(convert)

    (iter, new Closeable { def close(): Unit = { Seq(mongoCursor, mongoClient).foreach(_.close()) } })
  }

  // ===========================================================================
  private def convert(doc: org.bson.Document): List[(Symbol, Any)] =
    doc
      .entrySet()
      .pipe(scala.collection.JavaConverters.asScalaSet)
      .map { entry =>
        entry.getKey.symbol ->
          convertDocValue(entry.getValue) }
      .toList

    // ---------------------------------------------------------------------------
    private def convertDocValue(value: Any): Any =
      value match {
        case null                          => None
        case doc  : org.bson.Document      => convert(doc)
        case array: java.util.ArrayList[_] => convertArrayList(array)
        case x    : java.math.BigDecimal   => BigDecimal(x)
        case x    : java.math.BigInteger   => BigInt    (x)
        case x    : Array[Byte]            => gallia.byteBuffer(x) // necessary?
        case basic                         => basic }

      // ---------------------------------------------------------------------------
      private def convertArrayList(array: java.util.ArrayList[_]): Option[List[Any]] =
        array
          .asScala /* JListWrapper */
          .toList
          .map(convertDocValue)
          .in.noneIf(_.isEmpty)

  // ===========================================================================
  private def find(mongoCollection: MongoCollection, cmd: MongoDbCmd): Find = {
    var jongoFind: Find =
        cmd
          .filter
          .map      (mongoCollection.find)
          .getOrElse(mongoCollection.find)

      cmd.projection.foreach { x => jongoFind = jongoFind.projection(x) }
      cmd.sort      .foreach { x => jongoFind = jongoFind.sort      (x) }
      cmd.limit     .foreach { x => jongoFind = jongoFind.limit     (x) }

    jongoFind
  }

}

// ===========================================================================
