/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Random;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peiyi
 */
@MessageDriven(mappedName = "jms/Topic", activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/Topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/Topic"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class LibraryMessageDrivenBean implements MessageListener {

    @PersistenceContext(unitName = "LibraryServer-ejbPU")
    private EntityManager em;
    @Resource(mappedName="jms/TopicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;
    private Random processingTime = new Random();
    public LibraryMessageDrivenBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        MapMessage msg = null;
        try{
            if(message instanceof MapMessage){
                msg = (MapMessage) message;
                Thread.sleep(processingTime.nextInt(5)*1000);
                setUpRequest(msg);
            }
        }
        catch(InterruptedException ie){
            System.out.println("RMSMessageBean.onMessage: InterruptedException: " + ie.toString());
        }
        catch (Throwable te){
            System.out.println("RMSMessageBean.onMessage: Exception: " + te.toString());
        }        
    }
    void setUpRequest(MapMessage msg){
        try{
            String userName = msg.getString("userName");
            String content = msg.getString("content");
            long timeCreated = msg.getLong("time");
            
            LibMemberEntity temp = em.find(LibMemberEntity.class, userName);
            if(temp!=null){
                RequestEntity request = new RequestEntity();
                request.create(timeCreated, content, "UNREAD", "");
                request.setLibMember(temp);
                temp.getRequests().add(request);
                em.persist(request);
            }
        }
        catch(IllegalArgumentException e){
            System.err.println("RMSMessageBean setUp: Illegal Argument "+e);
        }
        catch (Exception e){
            System.err.println("RMSMessageBean setUp: " + e);
        }
        /*
        try {
            Connection connection = topicConnectionFactory.createConnection();
        } catch (Exception ex) {
            System.out.println("RMSMessageBean.setUpEntities: Exception: Unable to connect to JMS provider: " + ex.toString());
        }
        */       
    }
    public void persist(Object object) {
        em.persist(object);
    }
    
}
