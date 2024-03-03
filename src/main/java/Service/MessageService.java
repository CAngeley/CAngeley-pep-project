package Service;

import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO MessageDAO){
        this.messageDAO = MessageDAO;
    }

    public Message newMessage(Message Message){
        return messageDAO.newMessage(Message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    
    public List<Message> getAllMessagesBy_id(){
        return messageDAO.getAllMessagesBy_id();
    }
}
