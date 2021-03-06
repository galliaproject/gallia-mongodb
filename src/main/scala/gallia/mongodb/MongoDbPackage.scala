package gallia

import gallia.io.in._
import gallia.atoms.utils.MongoDb

// ===========================================================================
package object mongodb {
  // FIXME: t201223100652 - very ugly, do proper DI; look into macwire

  private def _inject(value: MongoDb) = { gallia.atoms.AtomsIX._MongodbInputZ.mongoDbOpt = Some(value) }

  // ---------------------------------------------------------------------------
  def injectMongodb = _inject(gallia.mongodb.MongoDbJongo)

  // ===========================================================================
  implicit class StartReadZFluency_(u: StartReadZFluency) {
    injectMongodb

    def mongodb: MongodbFluency = new MongodbFluency(MongodbConf(u._inputString))
  }

}

// ===========================================================================
