package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    /**
     * Insert an account into the account table.
     * Returns inserted account.
     */
    public Account insertAccount (Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            if ((account.getUsername() != "") && (account.getPassword().length() > 3)){
                String sql = "INSERT INTO Account (username, password) VALUES (?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                preparedStatement.executeUpdate();
                ResultSet pkey_rs = preparedStatement.getGeneratedKeys();
                if(pkey_rs.next()){
                    int generatedAccountId = (int) pkey_rs.getLong("account_id");
                    return new Account(generatedAccountId, account.getUsername(), account.getPassword());
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Login to an account.
     * Returns account that exists.
     */
    public Account loginAccount (Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT account_id, username, password FROM Account WHERE username=(?) AND password=(?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
