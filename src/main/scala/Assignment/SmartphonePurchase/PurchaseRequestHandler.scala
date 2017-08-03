package Assignment.SmartphonePurchase

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


class PurchaseRequestHandler(validationActor: ActorRef) extends Actor with ActorLogging{

  override def receive: Receive = {

    case customer: Customer => log.info("Customer information received in PurchaseRequestHandler." +
      "Forwarding to ValidationActor")
      validationActor.forward(customer)

    case _ => log.info("Wrong customer information received.")
      sender() ! "I can only help you buy Samsung Galaxy S8"

  }

}

object PuchaseRequestHandler {

  def props(validationActor: ActorRef): Props = Props(classOf[PurchaseRequestHandler], validationActor)

}
