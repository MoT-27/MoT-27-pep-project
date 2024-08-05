package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {

        String username = account.getUsername();
        String password = account.getPassword();
        

        if (username == null || username.isEmpty()) {
            return null;
        }

        if (password == null || password.length() < 4) {
            return null;
        }

        Account existingAccount = accountDAO.getAccountById(account.getAccount_id());

        if(existingAccount != null && (username == null || username.isEmpty()) && (password.length() < 4)){
            return null;
        } else {
            return accountDAO.insertAccount(account);
        }

    }



    public Account loginAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        Account existingAccount = accountDAO.getAccountUsername(username);

        if(existingAccount != null && existingAccount.getPassword().equals(password)){
            return existingAccount;
        }
        return null;
    }

}
