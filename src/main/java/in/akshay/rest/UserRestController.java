package in.akshay.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.akshay.bindings.User;
import in.akshay.bindings.UserActiveAccRequest;
import in.akshay.bindings.UserLogin;
import in.akshay.service.UserMgmtService;

@RestController
public class UserRestController {

	@Autowired
	private UserMgmtService service;

	@PostMapping("/user-reg")
	public ResponseEntity<String> userRegister(@RequestBody User user) {
		boolean saveUser = service.saveUser(user);
		if (saveUser)
			return new ResponseEntity<>("User Registration Success ", HttpStatus.CREATED);
		else {
			return new ResponseEntity<>("User Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate-acc")
	public ResponseEntity<String> activateAccount(@RequestBody UserActiveAccRequest activeAcc) {
		boolean isActivated = service.activateUserAccount(activeAcc);
		if (isActivated) {
			return new ResponseEntity<>("Account Activated.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid Temp Password.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = service.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("/user-by-id/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {

		User userById = service.getUserById(userId);
		return new ResponseEntity<>(userById, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {

		boolean isDeleted = service.deleteUserById(userId);
		if (isDeleted) {
			return new ResponseEntity<>("User Deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("User Not Deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/status-change/{userId}/{accStatus}")
	public ResponseEntity<String> chnageUserAccountStatus(@PathVariable Integer userId,
			@PathVariable String accStatus) {
		boolean status = service.chnageUserAccountStatus(userId, accStatus);
		if (status) {
			return new ResponseEntity<>("Status is changed", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to change status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> userLogin(@RequestBody UserLogin login) {

		String userLogin = service.userLogin(login);
		return new ResponseEntity<>(userLogin, HttpStatus.OK);
	}

	@GetMapping("/forgot-password/{email}")
	public ResponseEntity<String> forgotPassword(@PathVariable String email) {

		String forgotPassword = service.forgotPassword(email);
		return new ResponseEntity<>(forgotPassword, HttpStatus.OK);
	}

}
