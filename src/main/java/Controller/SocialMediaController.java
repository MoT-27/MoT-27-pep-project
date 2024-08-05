package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     AccountService accountService = new AccountService();
     MessageService messageService = new MessageService();

    public Javalin startAPI() {

        Javalin app = Javalin.create();

        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserHandler);

        return app;

    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }



    // New accounts
    private void postNewAccountHandler (Context ctx) throws JsonProcessingException {
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account addedAccount = accountService.addAccount(account);
            if(addedAccount != null){
                ctx.status(200).json(addedAccount);
                
            }else {
                ctx.status(400); 
            }

        } catch (JsonProcessingException e){
            ctx.status(400);
        } catch (Exception e){
            ctx.status(400);
            e.printStackTrace();
        }
    }



    // Logging into accounts
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.loginAccount(account);
        if(loginAccount == null){
            ctx.status(401);
        }else {
            ctx.status(200).json(loginAccount);
        }
    }



    // Create new message
    private void postNewMessageHandler(Context ctx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message addedMessage = messageService.addMessage(message);
            if(addedMessage == null){
                ctx.status(400);
            }else {
                ctx.json(mapper.writeValueAsString(addedMessage));
            }
        } catch (JsonMappingException e) {
            ctx.status(400);
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            ctx.status(400);
            e.printStackTrace();
        } catch (Exception e){
            ctx.status(400);
            e.printStackTrace();
        }
        
    }



    // Update a message by id
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }



    // Get all messages
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }



    // Get message by id
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
        ctx.status(200);
    }


    
    // Delete a message by id
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(id);
        if(deletedMessage != null){
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
        ctx.status(200);
    }



    // Get all messages by user account
    private void getAllMessagesFromUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesForUser(accountId);
        ctx.json(mapper.writeValueAsString(messages));
        
    }

}