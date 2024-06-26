package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /**
     * Insert a message into the message table.
     * Returns inserted message.
     */
    public Message newMessage (Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            if (accountExists(message) && isMessageTextSatisfactory(message)){
                String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, message.getPosted_by());
                preparedStatement.setString(2, message.getMessage_text());
                preparedStatement.setLong(3, message.getTime_posted_epoch());
                preparedStatement.executeUpdate();
                ResultSet pkey_rs = preparedStatement.getGeneratedKeys();
                if(pkey_rs.next()){
                    int generatedMessageId = (int) pkey_rs.getLong(1);
                    return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Checks if message_text is not empty and <= 255 characters.
     */
    public boolean isMessageTextSatisfactory(Message message){
        if ((message.getMessage_text() != "") && (message.getMessage_text().length() <= 255)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Checks if message.posted_by matches an account.account_id from account.
     */
    public boolean accountExists (Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT message.posted_by, message.message_text, message.time_posted_epoch FROM Account INNER JOIN Message ON account.account_id = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message.getPosted_by());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    /**
     * Returns all messages in the database.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "Select * from Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql); 
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    /**
     * Returns message in the database by message_id.
     */
    public Message getMessageBy_id(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM Message WHERE message_id = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Deletes message in the database by message_id.
     * Returns deleted message.
     */
    public Message deleteMessageBy_id(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            Message message = getMessageBy_id(message_id);
            if(message != null){
                String sql = "DELETE FROM Message WHERE message_id = (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, message_id);
                preparedStatement.executeUpdate();
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Updates a message by message_id.
     * Returns updated message.
     */
    public Message patchMessageBy_id(int message_id, Message messageBody){
        Connection connection = ConnectionUtil.getConnection();
        try {
            Message message = getMessageBy_id(message_id);
            if(message != null && isMessageTextSatisfactory(messageBody)){
                String sql = "UPDATE Message SET message_text = (?) WHERE message_id = (?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, messageBody.getMessage_text());
                ps.setInt(2,message_id);
                ps.executeUpdate();
                return new Message(message.getMessage_id(), message.getPosted_by(), messageBody.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Returns all messages in the database by posted_by
     */
    public List<Message> getAllMessagesBy_user(int posted_by){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM Message WHERE posted_by = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}