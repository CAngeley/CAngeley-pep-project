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
    
    public Message getMessageBy_id(int message_id){
        return messageDAO.getMessageBy_id(message_id);
    }

    public Message deleteMessageBy_id(int message_id){
        return messageDAO.getMessageBy_id(message_id);
    }

    public Message patchMessageBy_id(int message_id, Message message){
        return messageDAO.patchMessageBy_id(message_id, message);
    }

    public List<Message> getAllMessagesBy_user(int posted_by){
        return messageDAO.getAllMessagesBy_user(posted_by);
    }
}
