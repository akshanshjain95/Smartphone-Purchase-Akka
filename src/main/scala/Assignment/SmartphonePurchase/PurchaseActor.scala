package Assignment.SmartphonePurchase

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}


class PurchaseActorMaster extends Actor with ActorLogging {

  var router = {
    log.info("Creating routees")
    val routees = Vector.fill(5) {
      val purchaseActorRef = context.actorOf(Props[PurchaseActor])
      context watch purchaseActorRef
      ActorRefRoutee(purchaseActorRef)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: PartialFunction[Any, Unit] = {
    case customer: Customer => log.info("Sending customer to worker PurchaseActor")
      router.route(customer, sender())

    case Terminated(purchaseActorTerminate) => log.info("Terminating worker PurchaseActor")
      router = router.removeRoutee(purchaseActorTerminate)
      val purchaseActorRef = context.actorOf(Props[PurchaseActor])
      context watch purchaseActorRef
      router = router.addRoutee(purchaseActorRef)
  }

}


class PurchaseActor extends Actor with ActorLogging {

  override def receive: Receive = {

    case c:Customer => log.info("Sending Smartphone to customer")
      sender() ! SamsungSmartphone

  }
}

object PurchaseActorMaster {

  def props: Props = Props[PurchaseActorMaster]

}
