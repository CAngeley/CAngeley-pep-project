package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public Message newMessage (Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            if ((accountExists(message)) && (message.getMessage_text() != "") && (message.getMessage_text().length() <= 255)){
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
     * Checks if message.posted_by matches an account.account_id from account.
     * True if select query is not empty
     */
    public boolean accountExists (Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT message.posted_by, message.message_text, message.time_posted_epoch FROM account INNER JOIN message ON account.account_id = (?)";
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
     * Returns all messages in the database
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
     * Returns message in the database by id
     */
    public Message getMessageBy_id(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "Select * from Message WHERE message_id = (?)";
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
     * Deletes message in the database by id
     * Returns message in the database by id
     */
    public Message deleteMessageBy_id(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            Message message = getMessageBy_id(message_id);
            if(message != null){
                String sql = "DELETE FROM message WHERE message_id = (?)";
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
}
