package Assignment.SmartphonePurchase

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global



object MainClass extends App {

  val customer = Customer("Akshansh", "B-62, Sector-56, Noida", 12345678998765L, 9999819877L)

  val actorSystem = ActorSystem("SmartphonePurchaseSystem")

  val purchaseActorMaster = actorSystem.actorOf(PurchaseActorMaster.props)

  val validationActor = actorSystem.actorOf(ValidationActor.props(purchaseActorMaster))

  val purchaseRequestHandler = actorSystem.actorOf(PuchaseRequestHandler.props(validationActor))

  implicit val timeout = Timeout(100 seconds)

  val resultInFuture = purchaseRequestHandler ? customer

  resultInFuture.foreach(println(_))

}
