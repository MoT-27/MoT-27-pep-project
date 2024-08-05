package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }


    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }


    public Message addMessage(Message message){
        
        if((message.getMessage_text().isBlank()) || (message.message_text.length() > 255) || message.getPosted_by() <= 0){
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    
    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }


    public Message updateMessage(int message_id, Message message){
        Message existingMessage = messageDAO.getMessageById(message_id);

        if (existingMessage == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255){
            return null;
        } else {
            messageDAO.updateMessageText(message_id, message);
            return messageDAO.getMessageById(message_id);
        }
    }

    public Message deleteMessage(int message_id){
        Message existingMessage = messageDAO.getMessageById(message_id);

        if (existingMessage == null){
            return null;
        } else {
            return messageDAO.getMessageById(message_id);
        }
    }


    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    
    public List<Message> getAllMessagesForUser(int accountId){
        return messageDAO.getAllMessagesForUser(accountId);
    }

}
