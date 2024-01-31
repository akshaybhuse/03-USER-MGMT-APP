package in.akshay.service;

import java.util.List;

import in.akshay.bindings.User;
import in.akshay.bindings.UserActiveAccRequest;
import in.akshay.bindings.UserLogin;

public interface UserMgmtService {

	public boolean saveUser(User user);

	public boolean activateUserAccount(UserActiveAccRequest activeAccRequest);

	public List<User> getAllUsers();

	public User getUserById(Integer userId);

	public boolean deleteUserById(Integer userId);

	public boolean chnageUserAccountStatus(Integer userId, String accountStatus);

	public String userLogin(UserLogin userLogin);

	public String forgotPassword(String email);

}
