package in.akshay.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.akshay.bindings.User;
import in.akshay.bindings.UserActiveAccRequest;
import in.akshay.bindings.UserLogin;
import in.akshay.entity.UserMaster;
import in.akshay.repo.UserMasterRepo;
import in.akshay.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EmailUtils emailUtils;

	private Random random = new Random();

	@Override
	public boolean saveUser(User user) {

		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);

		entity.setPassword(generateRandomPassword());
		entity.setActiveStatus("In-Active");

		// send registration email
		String subject = "Your Registration Success!!";
		String emailFileName = "REG-EMAIL-BODY.txt";

		String body = readEmailBody(entity.getUserName(), entity.getPassword(), emailFileName);
		emailUtils.sendEmail(user.getUserMail(), subject, body);

		UserMaster save = userMasterRepo.save(entity);

		return save.getUserId() != null;
	}

	@Override
	public boolean activateUserAccount(UserActiveAccRequest activeAccRequest) {

		UserMaster entity = new UserMaster();
		entity.setUserMail(activeAccRequest.getUserActiveAccEmail());
		entity.setPassword(activeAccRequest.getUserActiveAccTempPwd());

		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userMasterRepo.findAll(of);

		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(activeAccRequest.getUserActiveAccConfirmPwd());
			userMaster.setActiveStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<UserMaster> findAll = userMasterRepo.findAll();

		List<User> userList = new ArrayList<>();
		for (UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			userList.add(user);
		}
		return userList;
	}

	@Override
	public User getUserById(Integer userId) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			User user = new User();
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {

		try {
			userMasterRepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean chnageUserAccountStatus(Integer userId, String accountStatus) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setActiveStatus(accountStatus);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;
	}

	@Override
	public String userLogin(UserLogin userLogin) {
		UserMaster entity = userMasterRepo.findByUserMailAndPassword(userLogin.getUserLoginEmail(),
				userLogin.getUserLoginPwd());
		if (entity == null)
			return "Invalid Credentials";
		if (entity.getActiveStatus().equals("Active")) {
			return "SUCCESS";
		} else {
			return "Account not activated";

		}
	}

	@Override
	public String forgotPassword(String email) {
		UserMaster entity = userMasterRepo.findByUserMail(email);
		if (entity == null) {
			return "Invalid email";
		}

		// Sent password to user in email
		String subject = "Forgot Password";

		String pwdFileName = "RECOVER-PWD-BODY.txt";

		String body = readEmailBody(entity.getUserName(), entity.getPassword(), pwdFileName);

		boolean sendEmail = emailUtils.sendEmail(email, subject, body);

		if (sendEmail) {
			return "Password sent to your registered email";
		}
		return null;
	}

	private String generateRandomPassword() {

		// create a string of uppercase and lowercase characters and numbers
		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";

		// combine all strings
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

		// create random string builder
		StringBuilder sb = new StringBuilder();

		// specify length of random string
		int length = 6;

		for (int i = 0; i < length; i++) {

			// generate random index number
			int index = random.nextInt(alphaNumeric.length());

			// get character specified by index
			// from the string
			char randomChar = alphaNumeric.charAt(index);

			// append the character to string builder
			sb.append(randomChar);
		}
		// String randomString = sb.toString();
		return sb.toString();
	}

	private String readEmailBody(String fullName, String pwd, String fileName) {

		String url = "";
		String mailBody = null;

		try (FileReader fileReader = new FileReader(fileName);
				BufferedReader buffReader = new BufferedReader(fileReader);) {
			String line = buffReader.readLine();

			StringBuffer buffer = new StringBuffer();

			while (line != null) {
				buffer.append(line);
				line = buffReader.readLine();
			}
			mailBody = buffer.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullName);
			mailBody = mailBody.replace("{TEMP-PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("{PWD}", pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}
}
