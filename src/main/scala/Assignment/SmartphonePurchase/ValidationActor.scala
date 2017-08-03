package Assignment.SmartphonePurchase

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by knoldus on 2/8/17.
  */
class ValidationActor(purchaseActorMaster: ActorRef) extends Actor with ActorLogging {

  var availability = 1000

  override def receive: Receive = {

    case customer: Customer => if(availability > 0)
      {
        log.info("Smartphone is available. Forwarding request to PurchaseActorMaster.")
        availability -= 1
        log.info(s"Item count is $availability")
        purchaseActorMaster.forward(customer)
      }
      else
      {
        log.info("Smartphones are out of stock. Sending out of stock message to customer.")
        sender() ! "Out of Stock!"
      }
  }

}

object ValidationActor {

  def props(purchaseActorMaster: ActorRef): Props = Props(classOf[ValidationActor], purchaseActorMaster)

}
