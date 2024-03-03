package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postNewAccount);
        app.post("/login", this::postLoginAccount);
        app.post("/messages", this::postNewMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageBy_id);
        app.delete("/messages/{message_id}", this::deleteMessageBy_id);
        app.patch("/messages/{message_id}", this::patchMessageBy_id);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesBy_user);
        return app;
    }

    /**
     * Handler to post a new Account.
     */
    private void postNewAccount(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler to post a login Account.
     */
    private void postLoginAccount(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.loginAccount(account);
        if(loginAccount != null){
            ctx.json(mapper.writeValueAsString(loginAccount));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    /**
     * Handler to post a new Message.
     */
    private void postNewMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.newMessage(message);
        if(newMessage != null){
            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handler to get all messages
     */
    private void getAllMessages(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * Handler to get all messages by id
     */
    private void getMessageBy_id(Context ctx) throws JsonProcessingException {
        int message_id = (int)Long.parseLong(ctx.pathParam("message_id"));
        Message message = messageService.getMessageBy_id(message_id);
        if(message != null){
            ctx.json(message);
        }
        ctx.status(200);
    }

    private void deleteMessageBy_id(Context ctx) throws JsonProcessingException {
        int message_id = (int)Long.parseLong(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageBy_id(message_id);
        if(message != null){
            ctx.json(message);
        }
        ctx.status(200);
    }

    private void patchMessageBy_id(Context ctx) throws JsonProcessingException {
        int message_id = (int)Long.parseLong(ctx.pathParam("message_id"));
        Message messageFromBody = ctx.bodyAsClass(Message.class);
        Message message = messageService.patchMessageBy_id(message_id, messageFromBody);
        if(message != null){
            ctx.json(message).status(200);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesBy_user(Context ctx) throws JsonProcessingException{
        int posted_by = (int)Long.parseLong(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesBy_user(posted_by);
        ctx.json(messages);
        ctx.status(200);
    }
}